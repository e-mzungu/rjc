package io.redis.rjc.message;

import io.redis.rjc.NodeFactory;
import io.redis.rjc.ds.DataSource;

/**
 * @author Evgeny Dolgov
 */
public class RedisNodeSubscriberFactory implements NodeFactory<RedisSubscriber> {

    public RedisSubscriber create(DataSource dataSource) {
        return new RedisNodeSubscriber(dataSource);
    }
}
