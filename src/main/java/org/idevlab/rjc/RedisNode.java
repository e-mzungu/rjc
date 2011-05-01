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

import org.idevlab.rjc.ds.DataSource;

import java.util.*;

/**
 * Creates new Session object for each command execution
 */
public class RedisNode implements SingleRedisOperations {

    private RedisTemplate template;

    public RedisNode() {
    }

    public RedisNode(DataSource dataSource) {
        this.template = new RedisTemplate(new SessionFactoryImpl(dataSource));
    }

    public void setDataSource(DataSource dataSource) {
        this.template = new RedisTemplate(new SessionFactoryImpl(dataSource));
    }

    public String ping() {
        return execute(new RedisCallback<String>() {
            public String doIt(Session session) {
                return session.ping();
            }
        });
    }

    public String randomKey() {
        return execute(new RedisCallback<String>() {
            public String doIt(Session session) {
                return session.randomKey();

            }
        });
    }

    public String set(final String key, final String value) {
        return execute(new RedisCallback<String>() {
            public String doIt(Session session) {
                return session.set(key, value);

            }
        });
    }

    public String get(final String key) {
        return execute(new RedisCallback<String>() {
            public String doIt(Session session) {
                return session.get(key);

            }
        });
    }

    public void quit() {
        execute(new RedisCallback<Object>() {
            public Object doIt(Session session) {
                session.quit();
                return null;
            }
        });
    }

    public Boolean exists(final String key) {
        return execute(new RedisCallback<Boolean>() {
            public Boolean doIt(Session session) {
                return session.exists(key);
            }
        });
    }

    public Long del(final String... keys) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.del(keys);
            }
        });

    }

    public String type(final String key) {
        return execute(new RedisCallback<String>() {
            public String doIt(Session session) {
                return session.type(key);

            }
        });
    }

    public String flushDB() {
        return execute(new RedisCallback<String>() {
            public String doIt(Session session) {
                return session.flushDB();

            }
        });
    }

    public Set<String> keys(final String pattern) {
        return execute(new RedisCallback<Set<String>>() {
            public Set<String> doIt(Session session) {
                return session.keys(pattern);
            }
        });
    }

    public String rename(final String key, final String newKey) {
        return execute(new RedisCallback<String>() {
            public String doIt(Session session) {
                return session.rename(key, newKey);

            }
        });
    }

    public Boolean renamenx(final String key, final String newKey) {
        return execute(new RedisCallback<Boolean>() {
            public Boolean doIt(Session session) {
                return session.renamenx(key, newKey);

            }
        });
    }

    public Long dbSize() {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.dbSize();

            }
        });
    }

    public Boolean expire(final String key, final int seconds) {
        return execute(new RedisCallback<Boolean>() {
            public Boolean doIt(Session session) {
                return session.expire(key, seconds);
            }
        });
    }

    public Boolean expireAt(final String key, final long unixTime) {
        return execute(new RedisCallback<Boolean>() {
            public Boolean doIt(Session session) {
                return session.expireAt(key, unixTime);
            }
        });
    }

    public Long ttl(final String key) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.ttl(key);

            }
        });
    }

    public Boolean move(final String key, final int dbIndex) {
        return execute(new RedisCallback<Boolean>() {
            public Boolean doIt(Session session) {
                return session.move(key, dbIndex);

            }
        });
    }

    public String flushAll() {
        return execute(new RedisCallback<String>() {
            public String doIt(Session session) {
                return session.flushAll();

            }
        });
    }

    public String getSet(final String key, final String value) {
        return execute(new RedisCallback<String>() {
            public String doIt(Session session) {
                return session.getSet(key, value);

            }
        });
    }

    public List<String> mget(final String... keys) {
        return execute(new RedisCallback<List<String>>() {
            public List<String> doIt(Session session) {
                return session.mget(keys);

            }
        });
    }

    public Boolean setnx(final String key, final String value) {
        return execute(new RedisCallback<Boolean>() {
            public Boolean doIt(Session session) {
                return session.setnx(key, value);

            }
        });
    }

    public String setex(final String key, final int seconds, final String value) {
        return execute(new RedisCallback<String>() {
            public String doIt(Session session) {
                return session.setex(key, seconds, value);

            }
        });
    }

    public String mset(final String... keysvalues) {
        return execute(new RedisCallback<String>() {
            public String doIt(Session session) {
                return session.mset(keysvalues);

            }
        });
    }

    public Boolean msetnx(final String... keysvalues) {
        return execute(new RedisCallback<Boolean>() {
            public Boolean doIt(Session session) {
                return session.msetnx(keysvalues);
            }
        });
    }

    public Long decrBy(final String key, final int value) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.decrBy(key, value);

            }
        });
    }

    public Long decr(final String key) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.decr(key);

            }
        });
    }

    public Long incrBy(final String key, final int value) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.incrBy(key, value);

            }
        });
    }

    public Long incr(final String key) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.incr(key);
            }
        });
    }

    public Long append(final String key, final String value) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.append(key, value);
            }
        });
    }

    public String getRange(final String key, final int start, final int end) {
        return execute(new RedisCallback<String>() {
            public String doIt(Session session) {
                return session.getRange(key, start, end);
            }
        });
    }

    public Long setRange(final String key, final int offset, final String value) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.setRange(key, offset, value);

            }
        });
    }

    public Boolean hset(final String key, final String field, final String value) {
        return execute(new RedisCallback<Boolean>() {
            public Boolean doIt(Session session) {
                return session.hset(key, field, value);
            }
        });
    }

    public String hget(final String key, final String field) {
        return execute(new RedisCallback<String>() {
            public String doIt(Session session) {
                return session.hget(key, field);

            }
        });
    }

    public Boolean hsetnx(final String key, final String field, final String value) {
        return execute(new RedisCallback<Boolean>() {
            public Boolean doIt(Session session) {
                return session.hsetnx(key, field, value);
            }
        });
    }

    public String hmset(final String key, final Map<String, String> hash) {
        return execute(new RedisCallback<String>() {
            public String doIt(Session session) {
                return session.hmset(key, hash);
            }
        });
    }

    public List<String> hmget(final String key, final String... fields) {
        return execute(new RedisCallback<List<String>>() {
            public List<String> doIt(Session session) {
                return session.hmget(key, fields);
            }
        });
    }

    public Long hincrBy(final String key, final String field, final int value) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.hincrBy(key, field, value);
            }
        });
    }

    public Boolean hexists(final String key, final String field) {
        return execute(new RedisCallback<Boolean>() {
            public Boolean doIt(Session session) {
                return session.hexists(key, field);
            }
        });
    }

    public Boolean hdel(final String key, final String field) {
        return execute(new RedisCallback<Boolean>() {
            public Boolean doIt(Session session) {
                return session.hdel(key, field);
            }
        });
    }

    public Long hlen(final String key) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.hlen(key);
            }
        });
    }

    public Set<String> hkeys(final String key) {
        return execute(new RedisCallback<Set<String>>() {
            public Set<String> doIt(Session session) {
                return session.hkeys(key);
            }
        });
    }

    public List<String> hvals(final String key) {
        return execute(new RedisCallback<List<String>>() {
            public List<String> doIt(Session session) {
                return session.hvals(key);
            }
        });
    }

    public Map<String, String> hgetAll(final String key) {
        return execute(new RedisCallback<Map<String, String>>() {
            public Map<String, String> doIt(Session session) {
                return session.hgetAll(key);
            }
        });
    }

    public Long rpush(final String key, final String value) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.rpush(key, value);

            }
        });
    }

    public Long lpush(final String key, final String value) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.lpush(key, value);

            }
        });
    }

    public Long llen(final String key) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.llen(key);

            }
        });
    }

    public List<String> lrange(final String key, final int start, final int end) {
        return execute(new RedisCallback<List<String>>() {
            public List<String> doIt(Session session) {
                return session.lrange(key, start, end);
            }
        });
    }

    public String ltrim(final String key, final int start, final int end) {
        return execute(new RedisCallback<String>() {
            public String doIt(Session session) {
                return session.ltrim(key, start, end);

            }
        });
    }

    public String lindex(final String key, final int index) {
        return execute(new RedisCallback<String>() {
            public String doIt(Session session) {
                return session.lindex(key, index);

            }
        });
    }

    public String lset(final String key, final int index, final String value) {
        return execute(new RedisCallback<String>() {
            public String doIt(Session session) {
                return session.lset(key, index, value);

            }
        });
    }

    public Long lrem(final String key, final int count, final String value) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.lrem(key, count, value);

            }
        });
    }

    public String lpop(final String key) {
        return execute(new RedisCallback<String>() {
            public String doIt(Session session) {
                return session.lpop(key);
            }
        });
    }

    public String rpop(final String key) {
        return execute(new RedisCallback<String>() {
            public String doIt(Session session) {
                return session.rpop(key);
            }
        });
    }

    public String rpoplpush(final String srckey, final String dstkey) {
        return execute(new RedisCallback<String>() {
            public String doIt(Session session) {
                return session.rpoplpush(srckey, dstkey);
            }
        });
    }

    public String brpoplpush(final String source, final String destination, final int timeout) {
        return execute(new RedisCallback<String>() {
            public String doIt(Session session) {
                return session.brpoplpush(source, destination, timeout);
            }
        });
    }

    public Boolean sadd(final String key, final String member) {
        return execute(new RedisCallback<Boolean>() {
            public Boolean doIt(Session session) {
                return session.sadd(key, member);
            }
        });
    }

    public Set<String> smembers(final String key) {
        return execute(new RedisCallback<Set<String>>() {
            public Set<String> doIt(Session session) {
                return session.smembers(key);
            }
        });
    }

    public Boolean srem(final String key, final String member) {
        return execute(new RedisCallback<Boolean>() {
            public Boolean doIt(Session session) {
                return session.srem(key, member);
            }
        });
    }

    public String spop(final String key) {
        return execute(new RedisCallback<String>() {
            public String doIt(Session session) {
                return session.spop(key);
            }
        });
    }

    public Boolean smove(final String srckey, final String dstkey, final String member) {
        return execute(new RedisCallback<Boolean>() {
            public Boolean doIt(Session session) {
                return session.smove(srckey, dstkey, member);
            }
        });
    }

    public Long scard(final String key) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.scard(key);
            }
        });
    }

    public Boolean sismember(final String key, final String member) {
        return execute(new RedisCallback<Boolean>() {
            public Boolean doIt(Session session) {
                return session.sismember(key, member);
            }
        });
    }

    public Set<String> sinter(final String... keys) {
        return execute(new RedisCallback<Set<String>>() {
            public Set<String> doIt(Session session) {
                return session.sinter(keys);
            }
        });
    }

    public Long sinterstore(final String dstkey, final String... keys) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.sinterstore(dstkey, keys);
            }
        });
    }

    public Set<String> sunion(final String... keys) {
        return execute(new RedisCallback<Set<String>>() {
            public Set<String> doIt(Session session) {
                return session.sunion(keys);
            }
        });
    }

    public Long sunionstore(final String dstkey, final String... keys) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.sunionstore(dstkey, keys);
            }
        });
    }

    public Set<String> sdiff(final String... keys) {
        return execute(new RedisCallback<Set<String>>() {
            public Set<String> doIt(Session session) {
                return session.sdiff(keys);
            }
        });
    }

    public Long sdiffstore(final String dstkey, final String... keys) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.sdiffstore(dstkey, keys);
            }
        });
    }

    public String srandmember(final String key) {
        return execute(new RedisCallback<String>() {
            public String doIt(Session session) {
                return session.srandmember(key);
            }
        });
    }

    public Boolean zadd(final String key, final Number score, final String member) {
        return execute(new RedisCallback<Boolean>() {
            public Boolean doIt(Session session) {
                return session.zadd(key, score, member);
            }
        });
    }

    public List<String> zrange(final String key, final int start, final int end) {
        return execute(new RedisCallback<List<String>>() {
            public List<String> doIt(Session session) {
                return session.zrange(key, start, end);
            }
        });
    }

    public Boolean zrem(final String key, final String member) {
        return execute(new RedisCallback<Boolean>() {
            public Boolean doIt(Session session) {
                return session.zrem(key, member);
            }
        });
    }

    public String zincrby(final String key, final Number score,
                          final String member) {
        return execute(new RedisCallback<String>() {
            public String doIt(Session session) {
                return session.zincrby(key, score, member);
            }
        });
    }

    public Long zrank(final String key, final String member) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.zrank(key, member);
            }
        });
    }

    public Long zrevrank(final String key, final String member) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.zrevrank(key, member);
            }
        });
    }

    public List<String> zrevrange(final String key, final int start,
                                  final int end) {
        return execute(new RedisCallback<List<String>>() {
            public List<String> doIt(Session session) {
                return session.zrevrange(key, start, end);
            }
        });
    }

    public List<ElementScore> zrangeWithScores(final String key, final int start,
                                               final int end) {
        return execute(new RedisCallback<List<ElementScore>>() {
            public List<ElementScore> doIt(Session session) {
                return session.zrangeWithScores(key, start, end);
            }
        });
    }

    public List<ElementScore> zrevrangeWithScores(final String key, final int start,
                                                  final int end) {
        return execute(new RedisCallback<List<ElementScore>>() {
            public List<ElementScore> doIt(Session session) {
                return session.zrevrangeWithScores(key, start, end);
            }
        });
    }

    public List<String> zrevrangeByScore(final String key, final String max, final String min) {
        return execute(new RedisCallback<List<String>>() {
            public List<String> doIt(Session session) {
                return session.zrevrangeByScore(key, max, min);
            }
        });
    }

    public List<String> zrevrangeByScore(final String key, final String max, final String min, final int offset, final int count) {
        return execute(new RedisCallback<List<String>>() {
            public List<String> doIt(Session session) {
                return session.zrevrangeByScore(key, max, min, offset, count);
            }
        });
    }

    public List<ElementScore> zrevrangeByScoreWithScores(final String key, final String max, final String min) {
        return execute(new RedisCallback<List<ElementScore>>() {
            public List<ElementScore> doIt(Session session) {
                return session.zrevrangeByScoreWithScores(key, max, min);
            }
        });
    }

    public List<ElementScore> zrevrangeByScoreWithScores(final String key, final String max, final String min,
                                                         final int offset, final int count) {
        return execute(new RedisCallback<List<ElementScore>>() {
            public List<ElementScore> doIt(Session session) {
                return session.zrevrangeByScoreWithScores(key, max, min, offset, count);
            }
        });
    }

    public Long zcard(final String key) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.zcard(key);

            }
        });
    }

    public String zscore(final String key, final String member) {
        return execute(new RedisCallback<String>() {
            public String doIt(Session session) {
                return session.zscore(key, member);

            }
        });
    }

    public List<String> sort(final String key) {
        return execute(new RedisCallback<List<String>>() {
            public List<String> doIt(Session session) {
                return session.sort(key);

            }
        });
    }

    public List<String> sort(final String key,
                             final SortingParams sortingParameters) {
        return execute(new RedisCallback<List<String>>() {
            public List<String> doIt(Session session) {
                return session.sort(key, sortingParameters);

            }
        });
    }

    public List<String> blpop(final int timeout, final String... keys) {
        return execute(new RedisCallback<List<String>>() {
            public List<String> doIt(Session session) {
                return session.blpop(timeout, keys);
            }
        });
    }

    public Long sort(final String key, final SortingParams sortingParameters,
                     final String dstkey) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.sort(key, sortingParameters, dstkey);

            }
        });
    }

    public Long sort(final String key, final String dstkey) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.sort(key, dstkey);

            }
        });
    }

    public List<String> brpop(final int timeout, final String... keys) {
        return execute(new RedisCallback<List<String>>() {
            public List<String> doIt(Session session) {
                return session.brpop(timeout, keys);
            }
        });
    }

    public String auth(final String password) {
        return execute(new RedisCallback<String>() {
            public String doIt(Session session) {
                return session.auth(password);

            }
        });
    }

    public List<Object> pipeline(final Pipeline pipeline) {
        return execute(new RedisCallback<List<Object>>() {
            public List<Object> doIt(Session session) {
                return session.pipeline(pipeline);
            }
        });
    }

    public Long publish(final String channel, final String message) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.publish(channel, message);

            }
        });
    }

    public Long getBit(final String key, final int offset) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.getBit(key, offset);

            }
        });
    }

    public Long setBit(final String key, final int offset, final String value) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.setBit(key, offset, value);

            }
        });
    }

    public Long zcount(final String key, final Number min, final Number max) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.zcount(key, min, max);

            }
        });
    }

    public List<String> zrangeByScore(final String key, final String min, final String max) {
        return execute(new RedisCallback<List<String>>() {
            public List<String> doIt(Session session) {
                return session.zrangeByScore(key, min, max);
            }
        });
    }

    public List<String> zrangeByScore(final String key, final String min,
                                      final String max, final int offset, final int count) {
        return execute(new RedisCallback<List<String>>() {
            public List<String> doIt(Session session) {
                return session.zrangeByScore(key, min, max, offset, count);
            }
        });
    }

    public List<ElementScore> zrangeByScoreWithScores(final String key,
                                                      final String min, final String max) {
        return execute(new RedisCallback<List<ElementScore>>() {
            public List<ElementScore> doIt(Session session) {
                return session.zrangeByScoreWithScores(key, min, max);
            }
        });
    }

    public List<ElementScore> zrangeByScoreWithScores(final String key,
                                                      final String min, final String max, final int offset,
                                                      final int count) {
        return execute(new RedisCallback<List<ElementScore>>() {
            public List<ElementScore> doIt(Session session) {
                return session.zrangeByScoreWithScores(key, min, max, offset, count);
            }
        });
    }

    public Long zremrangeByRank(final String key, final int start, final int end) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.zremrangeByRank(key, start, end);

            }
        });
    }

    public Long zremrangeByScore(final String key, final String min,
                                 final String max) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.zremrangeByScore(key, min, max);
            }
        });
    }

    public Long zunionstore(final String dstkey, final String... sets) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.zunionstore(dstkey, sets);
            }
        });
    }

    public Long zunionstore(final String dstkey, final ZParams params, final String... sets) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.zunionstore(dstkey, params, sets);
            }
        });
    }

    public Long zinterstore(final String dstkey, final String... sets) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.zinterstore(dstkey, sets);
            }
        });
    }

    public Long zinterstore(final String dstkey, final ZParams params, final String... sets) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.zinterstore(dstkey, params, sets);

            }
        });
    }

    public String save() {
        return execute(new RedisCallback<String>() {
            public String doIt(Session session) {
                return session.save();

            }
        });
    }

    public String bgsave() {
        return execute(new RedisCallback<String>() {
            public String doIt(Session session) {
                return session.bgsave();

            }
        });
    }

    public String bgrewriteaof() {
        return execute(new RedisCallback<String>() {
            public String doIt(Session session) {
                return session.bgrewriteaof();

            }
        });
    }

    public Long lastsave() {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.lastsave();

            }
        });
    }

    public String shutdown() {
        return execute(new RedisCallback<String>() {
            public String doIt(Session session) {
                return session.shutdown();
            }
        });
    }

    public String info() {
        return execute(new RedisCallback<String>() {
            public String doIt(Session session) {
                return session.info();

            }
        });
    }

    public void monitor(final RedisMonitor redisMonitor) {
        execute(new RedisCallback<String>() {
            public String doIt(Session session) {
                session.monitor(redisMonitor);
                return null;
            }
        });
    }

    public String slaveof(final String host, final int port) {
        return execute(new RedisCallback<String>() {
            public String doIt(Session session) {
                return session.slaveof(host, port);

            }
        });
    }

    public String slaveofNoOne() {
        return execute(new RedisCallback<String>() {
            public String doIt(Session session) {
                return session.slaveofNoOne();

            }
        });
    }

    public List<String> configGet(final String pattern) {
        return execute(new RedisCallback<List<String>>() {
            public List<String> doIt(Session session) {
                return session.configGet(pattern);

            }
        });
    }

    public String configSet(final String parameter, final String value) {
        return execute(new RedisCallback<String>() {
            public String doIt(Session session) {
                return session.configSet(parameter, value);

            }
        });
    }

    public Long strlen(final String key) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.strlen(key);

            }
        });
    }

    public void sync() {
        execute(new RedisCallback<String>() {
            public String doIt(Session session) {
                session.sync();
                return null;
            }
        });
    }

    public Long lpushx(final String key, final String value) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.lpushx(key, value);

            }
        });
    }

    public Boolean persist(final String key) {
        return execute(new RedisCallback<Boolean>() {
            public Boolean doIt(Session session) {
                return session.persist(key);

            }
        });
    }

    public Long rpushx(final String key, final String value) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.rpushx(key, value);

            }
        });
    }

    public String echo(final String string) {
        return execute(new RedisCallback<String>() {
            public String doIt(Session session) {
                return session.echo(string);

            }
        });
    }

    public Long linsert(final String key, final ListPosition where,
                        final String pivot, final String value) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.linsert(key, where, pivot, value);

            }
        });
    }

    public String debug(final DebugParams params) {
        return execute(new RedisCallback<String>() {
            public String doIt(Session session) {
                return session.debug(params);

            }
        });
    }

    private <R> R execute(RedisCallback<R> cmd) {
        return template.execute(cmd);
    }
}