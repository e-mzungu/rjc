package io.redis.rjc;

import java.util.Collection;

/**
 * @author Evgeny Dolgov
 */
public interface NodeLocator<T> {
    T getNode(String key);

    Collection<? extends T> getNodes();
}
