package io.redis.rjc;

/**
 * @author Evgeny Dolgov
 */
public interface Session extends SingleRedisOperations {

    String select(int index);

    String multi();

    String discard();

    String watch(String... key);

    String unwatch();

    void close();
}
