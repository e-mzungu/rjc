package io.redis.rjc.sharding;

import io.redis.rjc.RedisNodeFactory;
import io.redis.rjc.RedisOperations;
import io.redis.rjc.SingleRedisOperations;
import io.redis.rjc.ds.SimpleDataSourceFactory;
import org.junit.Before;

import java.util.Collection;

/**
 * @author Evgeny Dolgov
 */
public class ShardedRedisTestBase {
    protected RedisOperations redis;

    @Before
    public void setUp() throws Exception {
        ShardsFactoryImpl<SingleRedisOperations> shardsFactory = new ShardsFactoryImpl<SingleRedisOperations>();
        shardsFactory.setDataSourceFactory(new SimpleDataSourceFactory());
        shardsFactory.setNodeFactory(new RedisNodeFactory());
        shardsFactory.setAddresses(System.getProperty("redis-hosts"));

        Collection<Shard<SingleRedisOperations>> shards = shardsFactory.create();

        NodeLocator<SingleRedisOperations> locator = new HashNodeLocator<SingleRedisOperations>(shards, HashNodeLocator.DEFAULT_KEY_TAG_PATTERN);
        redis = new ShardedRedis(locator);
        for (SingleRedisOperations node : locator.getNodes()) {
            node.flushAll();
        }
    }

}
