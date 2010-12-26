package io.redis.rjc;

import java.util.Collection;

/**
 * @author Evgeny Dolgov
 */
public interface NodeLocator {
    SingleRedisOperations getNode(String key);

    Collection<? extends SingleRedisOperations> getNodes();
}
