package io.redis.rjc;

/**
 * @author Evgeny Dolgov
 */
public interface RedisCallback<T> {

    T doIt(Session session);
}
