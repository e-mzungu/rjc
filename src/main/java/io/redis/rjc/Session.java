package io.redis.rjc;

import java.util.List;

/**
 * @author Evgeny Dolgov
 */
public interface Session extends SingleRedisOperations {

    String select(int index);

    String multi();

    String discard();

    /**
     * @since 2.1.0
     */
    String watch(String... key);

    /**
     * @since 2.1.0
     */
    String unwatch();

    List<Object> exec();

    void close();
}
