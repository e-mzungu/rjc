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

import org.idevlab.rjc.HostAndPortUtil;
import org.idevlab.rjc.RedisException;
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

    @Test
    public void initialSizeTest2() {
        PoolableDataSource dataSource = createDataSource();
        dataSource.setInitialSize(3);
        dataSource.init();
        assertEquals(0, dataSource.getNumActive());
        assertEquals(3, dataSource.getNumIdle());
        dataSource.close();
    }

    @Test
    public void poolableDataSourceFactoryTest() {
        PoolableDataSourceFactory dataSourceFactory = new PoolableDataSourceFactory();
        dataSourceFactory.setInitialSize(3);
        PoolableDataSource dataSource = (PoolableDataSource) dataSourceFactory.create(hnp.host, hnp.port);
        assertEquals(3, dataSource.getInitialSize());

        assertEquals(0, dataSource.getNumActive());
        assertEquals(0, dataSource.getNumIdle());
        dataSource.close();

        dataSourceFactory.setInitAfterCreation(true);
        dataSource = (PoolableDataSource) dataSourceFactory.create(hnp.host, hnp.port);
        assertEquals(0, dataSource.getNumActive());
        assertEquals(3, dataSource.getNumIdle());
    }

    private PoolableDataSource createDataSource() {
        PoolableDataSource dataSource = new PoolableDataSource();
        dataSource.setHost(hnp.host);
        dataSource.setPort(hnp.port);
        return dataSource;
    }

}
