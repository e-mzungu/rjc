package io.redis.rjc.ds;

import io.redis.rjc.HostAndPortUtil;
import io.redis.rjc.RedisException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Evgeny Dolgov
 */
public class ITPoolableConnectionTest {
    protected static HostAndPortUtil.HostAndPort hnp = HostAndPortUtil.getRedisServers().get(0);


    @Test(expected = RedisException.class)
    public void connectionValidationTest() {
        PoolableDataSource dataSource = new PoolableDataSource();
        dataSource.setHost("unknownhost");
        dataSource.getConnection();
    }

    @Test
    public void getConnectionTest() {
        PoolableDataSource dataSource = createDataSource();
        RedisConnection redisConnection = dataSource.getConnection();
        assertEquals(1, dataSource.getNumActive());
        assertEquals(0, dataSource.getNumIdle());
        redisConnection.close();
        assertEquals(0, dataSource.getNumActive());
        assertEquals(1, dataSource.getNumIdle());
        dataSource.close();
    }

    @Test
    public void initialSizeTest() {
        PoolableDataSource dataSource = createDataSource();
        dataSource.setInitialSize(3);
        dataSource.getConnection();
        assertEquals(1, dataSource.getNumActive());
        assertEquals(2, dataSource.getNumIdle());
        dataSource.close();
    }

    private PoolableDataSource createDataSource() {
        PoolableDataSource dataSource = new PoolableDataSource();
        dataSource.setHost(hnp.host);
        dataSource.setPort(hnp.port);
        return dataSource;
    }

}
