package io.redis.rjc.sharding;

import java.util.Collection;

/**
 * @author Evgeny Dolgov
 */
public interface ShardsFactory<T> {

    Collection<Shard<T>> create();
}
