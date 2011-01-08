package io.redis.rjc;

import io.redis.rjc.ds.DataSource;

/**
 * @author Evgeny Dolgov
 */
public class SessionFactoryImpl implements SessionFactory {

    private DataSource dataSource;

    public SessionFactoryImpl() {
    }

    public SessionFactoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Session create() {
        return new RedisSessionImpl(dataSource.getConnection());
    }
}
