/*
 * Copyright 2009 - 2010 Scartel Star Lab LLC, Russia
 * Copyright 2009 - 2010 Seconca Holdings Limited, Cyprus
 *
 * This source code is Scartel Star Lab Confidential Proprietary
 * This software is protected by copyright.  All rights and titles are reserved.
 * You shall not use, copy, distribute, modify, decompile, disassemble or reverse
 * engineer the software. Otherwise this violation would be treated by law and
 * would be subject to legal prosecution.  Legal use of the software provides
 * receipt of a license from the right holder only.
 */

package io.redis.rjc;

import org.apache.commons.pool.ObjectPool;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

/**
 * <description>
 *
 * @author Evgeny Dolgov
 */
public class PoolableRedisConnection implements RedisConnection {

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

    public void disconnect() {
        boolean isUnderlyingConnectionClosed;
        try {
            isUnderlyingConnectionClosed = !conn.isConnected();
        } catch (Exception e) {
            try {
                pool.invalidateObject(this); // XXX should be guarded to happen at most once
            } catch (IllegalStateException ise) {
                // pool is closed, so close the connection
                conn.disconnect();
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
                conn.disconnect();
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
                conn.disconnect();
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

    public List<byte[]> getBinaryMultiBulkReply() {
        return conn.getBinaryMultiBulkReply();
    }

    public List<Object> getObjectMultiBulkReply() {
        return conn.getObjectMultiBulkReply();
    }

    public List<Object> getAll() {
        return conn.getAll();
    }

    public Object getOne() {
        return conn.getOne();
    }

    public void sendCommand(Protocol.Command cmd, String... args) {
        conn.sendCommand(cmd, args);
    }

    public void sendCommand(Protocol.Command cmd, byte[]... args) {
        conn.sendCommand(cmd, args);
    }

    public void sendCommand(Protocol.Command cmd) {
        conn.sendCommand(cmd);
    }

    public void reallyDisconnect() {
        conn.disconnect();
    }
}
