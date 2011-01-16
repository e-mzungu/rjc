package io.redis.rjc;

import io.redis.rjc.ds.DataSource;

/**
 * @author Evgeny Dolgov
 */
public class RedisNodeFactory implements NodeFactory<SingleRedisOperations> {

    public SingleRedisOperations create(DataSource dataSource) {
        return new RedisNode(dataSource);
    }
}
