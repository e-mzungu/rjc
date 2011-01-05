package io.redis.rjc.message;

/**
 * @author Evgeny Dolgov
 */
public interface RedisSubscriber {
    void subscribe(String channel, MessageListener listener);

    void psubscribe(String pattern, PMessageListener listener);

    void unsubscribe(String... channels);

    void punsubscribe(String... patterns);

    void addListener(SubscribeListener listener);

    void removeListener(SubscribeListener listener);
}
