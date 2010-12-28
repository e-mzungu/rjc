package io.redis.rjc;

/**
 * @author Evgeny Dolgov
 */
public interface Shard {
    /**
     * Shard weight
     * @return Shard weight
     */
    int getWeight();
}
