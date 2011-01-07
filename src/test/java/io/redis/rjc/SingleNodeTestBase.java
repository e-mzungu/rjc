package io.redis.rjc;

import io.redis.rjc.ds.SimpleDataSource;
import org.junit.After;
import org.junit.Before;


/**
 * @author Evgeny Dolgov
 */
public abstract class SingleNodeTestBase {
    protected static HostAndPortUtil.HostAndPort hnp = HostAndPortUtil.getRedisServers().get(0);

    protected RedisNode redis;

    @Before
    public void setUp() throws Exception {
        this.redis = new RedisNode(new SimpleDataSource(hnp.host, hnp.port, 500));
        this.redis.configSet("timeout", "300");
        this.redis.flushAll();
    }

    @After
    public void tearDown() throws Exception {

    }

//    protected Jedis createJedis() throws UnknownHostException, IOException {
//        Jedis j = new Jedis(hnp.host, hnp.port);
//        j.connect();
//        j.auth("foobared");
//        j.flushAll();
//        return j;
//    }
}
