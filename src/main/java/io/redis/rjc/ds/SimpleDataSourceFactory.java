package io.redis.rjc.ds;

import io.redis.rjc.protocol.Protocol;

/**
 * Creates io.redis.rjc.ds.SimpleDataSource instances
 *
 * @author Evgeny Dolgov
 */
public class SimpleDataSourceFactory implements DataSourceFactory {

    private int timeout = Protocol.DEFAULT_TIMEOUT;
    private String password;

    public DataSource create(String host, int port) {
        return new SimpleDataSource(host, port, timeout, password);
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
