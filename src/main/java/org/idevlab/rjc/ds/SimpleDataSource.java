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
import org.idevlab.rjc.protocol.Protocol;

/**
 * Simple data source mostly for testing purpose.
 * Always creates new connection.
 *
 * @author Evgeny Dolgov
 */
public class SimpleDataSource implements DataSource{

    private String host;
    private int port = Protocol.DEFAULT_PORT;
    private int timeout = Protocol.DEFAULT_TIMEOUT;
    private String password;

    private ConnectionFactory connectionFactory;

    public SimpleDataSource() {
    }

    public SimpleDataSource(String host) {
        this.host = host;
    }

    public SimpleDataSource(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public SimpleDataSource(String host, int port, int timeout) {
        this.host = host;
        this.port = port;
        this.timeout = timeout;
    }

    public SimpleDataSource(String host, int port, int timeout, String password) {
        this.host = host;
        this.port = port;
        this.timeout = timeout;
        this.password = password;
    }

    public synchronized RedisConnection getConnection() {
        if(connectionFactory == null) {
            connectionFactory = new ConnectionFactoryImpl(host, port, timeout, password);
        }
        try {
            return connectionFactory.create();
        } catch (Exception e) {
            throw new RedisException("Cannot get a connection", e);
        }
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
