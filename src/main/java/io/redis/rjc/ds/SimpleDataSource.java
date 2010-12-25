package io.redis.rjc.ds;

import io.redis.rjc.RedisException;
import io.redis.rjc.protocol.Protocol;

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
