package io.redis.rjc;

import io.redis.rjc.ds.SimpleDataSource;
import org.junit.After;
import org.junit.Before;


/**
 * @author Evgeny Dolgov
 */
public abstract class SingleNodeTestBase {
    protected static HostAndPortUtil.HostAndPort hnp = HostAndPortUtil.getRedisServers().get(0);

    protected Session session;

    @Before
    public void setUp() throws Exception {
        this.session = new SessionFactoryImpl(new SimpleDataSource(hnp.host, hnp.port, 500)).create();
        this.session.configSet("timeout", "300");
        this.session.flushAll();
    }

    @After
    public void tearDown() throws Exception {
        this.session.close();
    }
}
