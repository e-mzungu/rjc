/*
 * Copyright 2010-2011. Evgeny Dolgov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.idevlab.rjc.ds;

import org.idevlab.rjc.RedisException;
import org.apache.commons.pool.ObjectPool;
import org.idevlab.rjc.protocol.RedisCommand;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Wrapper for using in <code>PoolableDataSource</code>
 *
 * @author Evgeny Dolgov
 */
class PoolableRedisConnection implements RedisConnection {

    private final RedisConnection conn;
    private final ObjectPool pool;

    public PoolableRedisConnection(RedisConnection conn, ObjectPool pool) {
        this.conn = conn;
        this.pool = pool;
    }

    public int getTimeout() {
        return conn.getTimeout();
    }

    public void rollbackTimeout() {
        conn.rollbackTimeout();
    }

    public String getHost() {
        return conn.getHost();
    }

    public int getPort() {
        return conn.getPort();
    }

    public void connect() throws UnknownHostException, IOException {
        conn.connect();
    }

    public void close() {
        boolean isUnderlyingConnectionClosed;
        try {
            isUnderlyingConnectionClosed = !conn.isConnected();
        } catch (Exception e) {
            try {
                pool.invalidateObject(this); // XXX should be guarded to happen at most once
            } catch (IllegalStateException ise) {
                // pool is closed, so close the connection
                conn.close();
            } catch (Exception ie) {
                // DO NOTHING the original exception will be rethrown
            }
            throw new RedisException("Cannot close connection (isClosed check failed)");
        }

        if (!isUnderlyingConnectionClosed) {
            // Normal close: underlying connection is still open, so we
            // simply need to return this proxy to the pool
            try {
                pool.returnObject(this); // XXX should be guarded to happen at most once
            } catch (IllegalStateException e) {
                // pool is closed, so close the connection
                conn.close();
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new RedisException("Cannot close connection (return to pool failed)", e);
            }
        } else {
            // Abnormal close: underlying connection closed unexpectedly, so we
            // must destroy this proxy
            try {
                pool.invalidateObject(this); // XXX should be guarded to happen at most once
            } catch (IllegalStateException e) {
                // pool is closed, so close the connection
                conn.close();
            } catch (Exception ie) {
                // DO NOTHING, "Already closed" exception thrown below
            }
            throw new RedisException("Already closed.");
        }
    }

    public boolean isConnected() {
        return conn.isConnected();
    }

    public void setTimeoutInfinite() {
        conn.setTimeoutInfinite();
    }

    public String getStatusCodeReply() {
        return conn.getStatusCodeReply();
    }

    public String getBulkReply() {
        return conn.getBulkReply();
    }

    public byte[] getBinaryBulkReply() {
        return conn.getBinaryBulkReply();
    }

    public Long getIntegerReply() {
        return conn.getIntegerReply();
    }

    public List<String> getMultiBulkReply() {
        return conn.getMultiBulkReply();
    }

    public List<Object> getObjectMultiBulkReply() {
        return conn.getObjectMultiBulkReply();
    }

    public List<Object> getBinaryObjectMultiBulkReply() {
        return conn.getBinaryObjectMultiBulkReply();
    }

    public List<Object> getAll() {
        return conn.getAll();
    }

    public List<Object> getBinaryAll() {
        return conn.getBinaryAll();
    }

    public Object getOne() {
        return conn.getOne();
    }

    public Object getBinaryOne() {
        return conn.getBinaryOne();
    }

    public void sendCommand(RedisCommand cmd, String... args) {
        conn.sendCommand(cmd, args);
    }

    public void sendCommand(RedisCommand cmd, byte[]... args) {
        conn.sendCommand(cmd, args);
    }

    public void sendCommand(RedisCommand cmd) {
        conn.sendCommand(cmd);
    }

    public void reallyDisconnect() {
        conn.close();
    }
}
