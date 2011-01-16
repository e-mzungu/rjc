package io.redis.rjc.sharding;

/**
 * @author Evgeny Dolgov
 */
public class ShardImpl<T> implements Shard<T> {

    private int weight = 1;
    private String shardId;
    private T node;

    public ShardImpl() {
    }

    public ShardImpl(String shardId, T node) {
        this.node = node;
        this.shardId = shardId;
    }

    public ShardImpl(String shardId, T node, int weight) {
        this.weight = weight;
        this.shardId = shardId;
        this.node = node;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getShardId() {
        return shardId;
    }

    public void setShardId(String shardId) {
        this.shardId = shardId;
    }

    public T getNode() {
        return node;
    }

    public void setNode(T node) {
        this.node = node;
    }
}
