package io.redis.rjc.message;

import io.redis.rjc.NodeLocator;

/**
 * @author Evgeny Dolgov
 */
public class ShardedRedisSubscriber implements RedisSubscriber {

    private NodeLocator<RedisNodeSubscriber> locator;

    public ShardedRedisSubscriber() {
    }

    public ShardedRedisSubscriber(NodeLocator<RedisNodeSubscriber> locator) {
        this.locator = locator;
    }

    public void setLocator(NodeLocator<RedisNodeSubscriber> locator) {
        this.locator = locator;
    }

    public void subscribe(String channel, MessageListener listener) {
        locator.getNode(channel).subscribe(channel, listener);
    }

    public void psubscribe(String pattern, PMessageListener listener) {
        for (RedisNodeSubscriber subscriber : locator.getNodes()) {
            subscriber.psubscribe(pattern, listener);
        }
    }

    public void unsubscribe(String... channels) {
        for (String channel : channels) {
            locator.getNode(channel).unsubscribe(channel);
        }
    }

    public void punsubscribe(String... patterns) {
        for (RedisNodeSubscriber subscriber : locator.getNodes()) {
            for (String pattern : patterns) {
                subscriber.punsubscribe(pattern);
            }
        }
    }

    public void addListener(SubscribeListener listener) {
        for (RedisNodeSubscriber subscriber : locator.getNodes()) {
            subscriber.addListener(listener);
        }
    }

    public void removeListener(SubscribeListener listener) {
        for (RedisNodeSubscriber subscriber : locator.getNodes()) {
            subscriber.removeListener(listener);
        }
    }

    public void close() {
        for (RedisNodeSubscriber subscriber : locator.getNodes()) {
            subscriber.close();
        }
    }
}
