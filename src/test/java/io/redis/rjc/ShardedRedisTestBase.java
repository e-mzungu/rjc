package io.redis.rjc;

import io.redis.rjc.ds.SimpleDataSource;
import org.junit.Before;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Evgeny Dolgov
 */
public class ShardedRedisTestBase {
    protected static List<HostAndPortUtil.HostAndPort> hnps = HostAndPortUtil.getRedisServers();

    protected RedisOperations redis;

    @Before
    public void setUp() throws Exception {
        Collection<Shard<SingleRedisOperations>> shards = new ArrayList<Shard<SingleRedisOperations>>();
        for (HostAndPortUtil.HostAndPort hostAndPort : hnps) {
            shards.add(new ShardImpl<SingleRedisOperations>(hostAndPort.host + hostAndPort.port,
                    new RedisNode(new SimpleDataSource(hostAndPort.host, hostAndPort.port))));
        }

        NodeLocator<SingleRedisOperations> locator = new HashNodeLocator<SingleRedisOperations>(shards, HashNodeLocator.DEFAULT_KEY_TAG_PATTERN);
        redis = new ShardedRedis(locator);
        for (SingleRedisOperations node : locator.getNodes()) {
            node.flushAll();
        }
    }

}
