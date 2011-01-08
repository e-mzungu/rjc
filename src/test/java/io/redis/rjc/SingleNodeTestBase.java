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
    private SessionFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = new SessionFactoryImpl(new SimpleDataSource(hnp.host, hnp.port, 500));
        this.session = createSession();
        this.session.flushAll();
    }

    protected Session createSession() {
        Session result = factory.create();
        result.configSet("timeout", "300");
        return result;
    }

    @After
    public void tearDown() throws Exception {
        this.session.close();
    }
}
