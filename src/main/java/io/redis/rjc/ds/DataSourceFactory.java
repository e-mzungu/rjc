package io.redis.rjc.ds;

/**
 * @author Evgeny Dolgov
 */
public interface DataSourceFactory {

    /**
     * Creates Redis data source
     * @param host Redis host
     * @param port Redis port
     * @return Redis data source
     */
    DataSource create(String host, int port);
}
