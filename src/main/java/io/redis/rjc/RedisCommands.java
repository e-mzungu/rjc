package io.redis.rjc;

import java.util.Map;

public interface RedisCommands extends MultiExecCommands {

    public void set(String key, String value);

    public void get(String key);

    public void exists(String key);

    public void del(String... keys);

    public void type(String key);

    public void keys(String pattern);

    public void rename(String oldkey, String newkey);

    public void renamenx(String oldkey, String newkey);

    public void expire(String key, int seconds);

    public void expireAt(String key, long unixTime);

    public void ttl(String key);

    public void move(String key, int dbIndex);

    public void getSet(String key, String value);

    public void mget(String... keys);

    public void setnx(String key, String value);

    public void setex(String key, int seconds, String value);

    public void mset(String... keysvalues);

    public void msetnx(String... keysvalues);

    public void decrBy(String key, int integer);

    public void decr(String key);

    public void incrBy(String key, int integer);

    public void incr(String key);

    public void append(String key, String value);

    public void substr(String key, int start, int end);

    public void hset(String key, String field, String value);

    public void hget(String key, String field);

    public void hsetnx(String key, String field, String value);

    public void hmset(String key, Map<String, String> hash);

    public void hmget(String key, String... fields);

    public void hincrBy(String key, String field, int value);

    public void hexists(String key, String field);

    public void hdel(String key, String field);

    public void hlen(String key);

    public void hkeys(String key);

    public void hvals(String key);

    public void hgetAll(String key);

    public void rpush(String key, String value);

    public void lpush(String key, String value);

    public void llen(String key);

    public void lrange(String key, int start, int end);

    public void ltrim(String key, int start, int end);

    public void lindex(String key, int index);

    public void lset(String key, int index, String value);

    public void lrem(String key, int count, String value);

    public void lpop(String key);

    public void rpop(String key);

    public void rpoplpush(String srckey, String dstkey);

    public void sadd(String key, String member);

    public void smembers(String key);

    public void srem(String key, String member);

    public void spop(String key);

    public void smove(String srckey, String dstkey, String member);

    public void scard(String key);

    public void sismember(String key, String member);

    public void sinter(String... keys);

    public void sinterstore(String dstkey, String... keys);

    public void sunion(String... keys);

    public void sunionstore(String dstkey, String... keys);

    public void sdiff(String... keys);

    public void sdiffstore(String dstkey, String... keys);

    public void srandmember(String key);

    public void zadd(String key, Number score, String member);

    public void zrange(String key, int start, int end);

    public void zrem(String key, String member);

    public void zincrby(String key, Number score, String member);

    public void zrank(String key, String member);

    public void zrevrank(String key, String member);

    public void zrevrange(String key, int start, int end);

    public void zrangeWithScores(String key, int start, int end);

    public void zrevrangeWithScores(String key, int start, int end);

    public void zcard(String key);

    public void zscore(String key, String member);

    public void sort(String key);

    public void sort(String key, SortingParams sortingParameters);

    public void blpop(String... keys);

    public void sort(String key, SortingParams sortingParameters, String dstkey);

    public void sort(String key, String dstkey);

    public void brpop(String... keys);

    public void zcount(String key, Number min, Number max);

    public void zrangeByScore(String key, Number min, Number max);

    public void zrangeByScore(String key, Number min, Number max, int offset, int count);

    public void zrangeByScoreWithScores(String key, Number min, Number max);

    public void zrangeByScoreWithScores(String key, Number min, Number max, int offset, int count);

    public void zremrangeByRank(String key, int start, int end);

    public void zremrangeByScore(String key, Number start, Number end);

    public void zunionstore(String dstkey, String... sets);

    public void zunionstore(String dstkey, ZParams params, String... sets);

    public void zinterstore(String dstkey, String... sets);

    public void zinterstore(String dstkey, ZParams params, String... sets);

    public void strlen(String key);

    public void lpushx(String key, String value);

    public void persist(String key);

    public void rpushx(String key, String value);

    void select(int index);

    void debug(final DebugParams params);

    void configResetStat();

    void brpoplpush(String source, String destination, int timeout);

    public void setbit(final String key, final int offset, final String value);

    void getbit(String key, int offset);
}