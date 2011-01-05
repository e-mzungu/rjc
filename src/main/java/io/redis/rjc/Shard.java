package io.redis.rjc;

/**
 * @author Evgeny Dolgov
 */
public interface Shard<T> {
    /**
     * Shard weight
     * @return Shard weight
     */
    int getWeight();

    /**
     * Shard id
     * @return shard id, must not be null
     */
    String getShardId();

    /**
     * Underlying shard node
     *
     * @return shard node
     */
    T getNode();
}
