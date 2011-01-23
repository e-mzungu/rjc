/*
 * Copyright 2010-2011. Evgeny Dolgov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.idevlab.rjc;

import java.util.Map;

public interface RedisCommands extends MultiExecCommands {

    void set(String key, String value);

    void get(String key);

    void exists(String key);

    void del(String... keys);

    void type(String key);

    void keys(String pattern);

    void rename(String oldkey, String newkey);

    void renamenx(String oldkey, String newkey);

    void expire(String key, int seconds);

    void expireAt(String key, long unixTime);

    void ttl(String key);

    void move(String key, int dbIndex);

    void getSet(String key, String value);

    void mget(String... keys);

    void setnx(String key, String value);

    void setex(String key, int seconds, String value);

    void mset(String... keysvalues);

    void msetnx(String... keysvalues);

    void decrBy(String key, int integer);

    void decr(String key);

    void incrBy(String key, int integer);

    void incr(String key);

    void append(String key, String value);

    void getRange(String key, int start, int end);

    void setRange(String key, int offset, String value);

    void hset(String key, String field, String value);

    void hget(String key, String field);

    void hsetnx(String key, String field, String value);

    void hmset(String key, Map<String, String> hash);

    void hmget(String key, String... fields);

    void hincrBy(String key, String field, int value);

    void hexists(String key, String field);

    void hdel(String key, String field);

    void hlen(String key);

    void hkeys(String key);

    void hvals(String key);

    void hgetAll(String key);

    void rpush(String key, String value);

    void lpush(String key, String value);

    void llen(String key);

    void lrange(String key, int start, int end);

    void ltrim(String key, int start, int end);

    void lindex(String key, int index);

    void lset(String key, int index, String value);

    void lrem(String key, int count, String value);

    void lpop(String key);

    void rpop(String key);

    void rpoplpush(String srckey, String dstkey);

    void sadd(String key, String member);

    void smembers(String key);

    void srem(String key, String member);

    void spop(String key);

    void smove(String srckey, String dstkey, String member);

    void scard(String key);

    void sismember(String key, String member);

    void sinter(String... keys);

    void sinterstore(String dstkey, String... keys);

    void sunion(String... keys);

    void sunionstore(String dstkey, String... keys);

    void sdiff(String... keys);

    void sdiffstore(String dstkey, String... keys);

    void srandmember(String key);

    void zadd(String key, Number score, String member);

    void zrange(String key, int start, int end);

    void zrem(String key, String member);

    void zincrby(String key, Number score, String member);

    void zrank(String key, String member);

    void zrevrank(String key, String member);

    void zrevrange(String key, int start, int end);

    void zrangeWithScores(String key, int start, int end);

    void zrevrangeWithScores(String key, int start, int end);

    void zrevrangeByScore(final String key, final String max, final String min);

    void zrevrangeByScore(final String key, final String max, final String min, final int offset, int count);

    void zrevrangeByScoreWithScores(final String key, final String max, final String min);

    void zrevrangeByScoreWithScores(final String key, final String max, final String min, final int offset, final int count);

    void zcard(String key);

    void zscore(String key, String member);

    void sort(String key);

    void sort(String key, SortingParams sortingParameters);

    void blpop(String... keys);

    void sort(String key, SortingParams sortingParameters, String dstkey);

    void sort(String key, String dstkey);

    void brpop(String... keys);

    void zcount(String key, Number min, Number max);

    void zrangeByScore(String key, String min, String max);

    void zrangeByScore(String key, String min, String max, int offset, int count);

    void zrangeByScoreWithScores(String key, String min, String max);

    void zrangeByScoreWithScores(String key, String min, String max, int offset, int count);

    void zremrangeByRank(String key, int start, int end);

    void zremrangeByScore(String key, String start, String end);

    void zunionstore(String dstkey, String... sets);

    void zunionstore(String dstkey, ZParams params, String... sets);

    void zinterstore(String dstkey, String... sets);

    void zinterstore(String dstkey, ZParams params, String... sets);

    void strlen(String key);

    void lpushx(String key, String value);

    void persist(String key);

    void rpushx(String key, String value);

    void select(int index);

    void debug(final DebugParams params);

    void configResetStat();

    void brpoplpush(String source, String destination, int timeout);

    void setbit(final String key, final int offset, final String value);

    void getbit(String key, int offset);
}