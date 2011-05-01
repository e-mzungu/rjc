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

import org.idevlab.rjc.ds.RedisConnection;
import org.idevlab.rjc.protocol.RedisCommand;
import org.idevlab.rjc.protocol.RedisKeyword;
import org.idevlab.rjc.util.KeyUtil;

import java.util.*;

/**
 * @author Evgeny Dolgov
 */
class RedisSessionImpl implements Session {
    private final RedisClientImpl redis;

    public RedisSessionImpl(RedisConnection connection) {
        this.redis = new RedisClientImpl(connection);
    }

    public String set(final String key, String value) {
        return redis.getStatusReply(RedisCommand.SET, key, value);
    }

    public String get(final String key) {
        return redis.getBulkReply(RedisCommand.GET, key);
    }

    public void quit() {
        redis.noReply(RedisCommand.QUIT);
    }

    public Boolean exists(final String key) {
        return integerReplayToBoolean(redis.getIntegerReply(RedisCommand.EXISTS, key));
    }

    public Long del(final String... keys) {
        return redis.getIntegerReply(RedisCommand.DEL, keys);
    }

    public String type(final String key) {
        return redis.getStatusReply(RedisCommand.TYPE, key);
    }

    public String flushDB() {
        return redis.getStatusReply(RedisCommand.FLUSHDB);
    }

    @SuppressWarnings({"unchecked"})
    public Set<String> keys(final String pattern) {
        return new HashSet<String>(redis.getStringMultiBulkReply(RedisCommand.KEYS, pattern));
    }

    public String randomKey() {
        return redis.getBulkReply(RedisCommand.RANDOMKEY);
    }

    public String rename(final String key, final String newKey) {
        return redis.getStatusReply(RedisCommand.RENAME, key, newKey);
    }

    public Boolean renamenx(final String key, final String newKey) {
        return integerReplayToBoolean(redis.getIntegerReply(RedisCommand.RENAMENX, key, newKey));
    }

    public Long dbSize() {
        return redis.getIntegerReply(RedisCommand.DBSIZE);
    }

    public Boolean expire(final String key, final int seconds) {
        return integerReplayToBoolean(redis.getIntegerReply(RedisCommand.EXPIRE, key, String.valueOf(seconds)));
    }

    public Boolean expireAt(final String key, final long unixTime) {
        return integerReplayToBoolean(redis.getIntegerReply(RedisCommand.EXPIREAT, key, String.valueOf(unixTime)));
    }

    public Long ttl(final String key) {
        return redis.getIntegerReply(RedisCommand.TTL, key);
    }

    public String select(final int index) {
        return redis.getStatusReply(RedisCommand.SELECT, String.valueOf(index));
    }


    public Boolean move(final String key, final int dbIndex) {
        return integerReplayToBoolean(redis.getIntegerReply(RedisCommand.MOVE, key, String.valueOf(dbIndex)));
    }

    public String flushAll() {
        return redis.getStatusReply(RedisCommand.FLUSHALL);
    }

    public String getSet(final String key, final String value) {
        return redis.getBulkReply(RedisCommand.GETSET, key, value);
    }

    public List<String> mget(final String... keys) {
        return redis.getStringMultiBulkReply(RedisCommand.MGET, keys);
    }

    public Boolean setnx(final String key, final String value) {
        return integerReplayToBoolean(redis.getIntegerReply(RedisCommand.SETNX, key, value));
    }

    public String setex(final String key, final int seconds, final String value) {
        return redis.getStatusReply(RedisCommand.SETEX, key, String.valueOf(seconds), value);
    }

    public String mset(final String... keysvalues) {
        return redis.getStatusReply(RedisCommand.MSET, keysvalues);
    }

    public Boolean msetnx(final String... keysvalues) {
        return integerReplayToBoolean(redis.getIntegerReply(RedisCommand.MSETNX, keysvalues));
    }

    public Long decrBy(final String key, final int value) {
        return redis.getIntegerReply(RedisCommand.DECRBY, key, String.valueOf(value));
    }

    public Long decr(final String key) {
        return redis.getIntegerReply(RedisCommand.DECR, key);
    }

    public Long incrBy(final String key, final int value) {
        return redis.getIntegerReply(RedisCommand.INCRBY, key, String.valueOf(value));
    }

    public Long incr(final String key) {
        return redis.getIntegerReply(RedisCommand.INCR, key);
    }

    public Long append(final String key, final String value) {
        return redis.getIntegerReply(RedisCommand.APPEND, key, value);
    }

    public String getRange(final String key, final int start, final int end) {
        return redis.getBulkReply(RedisCommand.GETRANGE, key, String.valueOf(start), String.valueOf(end));
    }

    public Long setRange(String key, int offset, String value) {
        return redis.getIntegerReply(RedisCommand.SETRANGE, key, String.valueOf(offset), value);
    }

    public Boolean hset(final String key, final String field, final String value) {
        return integerReplayToBoolean(redis.getIntegerReply(RedisCommand.HSET, key, field, value));
    }

    public String hget(final String key, final String field) {
        return redis.getBulkReply(RedisCommand.HGET, key, field);
    }

    public Boolean hsetnx(final String key, final String field, final String value) {
        return integerReplayToBoolean(redis.getIntegerReply(RedisCommand.HSETNX, key, field, value));
    }

    public String hmset(final String key, final Map<String, String> hash) {
        final List<String> params = new ArrayList<String>();
        params.add(key);

        for (String field : hash.keySet()) {
            params.add(field);
            params.add(hash.get(field));
        }
        String[] args = params.toArray(new String[params.size()]);
        return redis.getStatusReply(RedisCommand.HMSET, args);
    }

    public List<String> hmget(final String key, final String... fields) {
        String[] params = new String[fields.length + 1];
        params[0] = key;
        System.arraycopy(fields, 0, params, 1, fields.length);
        return redis.getStringMultiBulkReply(RedisCommand.HMGET, params);
    }

    public Long hincrBy(final String key, final String field, final int value) {
        return redis.getIntegerReply(RedisCommand.HINCRBY, key, field, String.valueOf(value));
    }

    public Boolean hexists(final String key, final String field) {
        return integerReplayToBoolean(redis.getIntegerReply(RedisCommand.HEXISTS, key, field));
    }

    public Boolean hdel(final String key, final String field) {
        return integerReplayToBoolean(redis.getIntegerReply(RedisCommand.HDEL, key, field));
    }

    public Long hlen(final String key) {
        return redis.getIntegerReply(RedisCommand.HLEN, key);
    }

    public Set<String> hkeys(final String key) {
        return new HashSet<String>(redis.getStringMultiBulkReply(RedisCommand.HKEYS, key));
    }

    public List<String> hvals(final String key) {
        return redis.getStringMultiBulkReply(RedisCommand.HVALS, key);
    }

    public Map<String, String> hgetAll(final String key) {
        final List<String> flatHash = redis.getStringMultiBulkReply(RedisCommand.HGETALL, key);
        final Map<String, String> hash = new HashMap<String, String>();
        final Iterator<String> iterator = flatHash.iterator();
        while (iterator.hasNext()) {
            hash.put(iterator.next(), iterator.next());
        }

        return hash;
    }

    public Long rpush(final String key, final String value) {
        return redis.getIntegerReply(RedisCommand.RPUSH, key, value);
    }

    public Long lpush(final String key, final String value) {
        return redis.getIntegerReply(RedisCommand.LPUSH, key, value);
    }

    public Long llen(final String key) {
        return redis.getIntegerReply(RedisCommand.LLEN, key);
    }

    public List<String> lrange(final String key, final int start, final int end) {
        return redis.getStringMultiBulkReply(RedisCommand.LRANGE, key, String.valueOf(start), String.valueOf(end));
    }

    public String ltrim(final String key, final int start, final int end) {
        return redis.getStatusReply(RedisCommand.LTRIM, key, String.valueOf(start), String.valueOf(end));
    }

    public String lindex(final String key, final int index) {
        return redis.getBulkReply(RedisCommand.LINDEX, key, String.valueOf(index));
    }

    public String lset(final String key, final int index, final String value) {
        return redis.getStatusReply(RedisCommand.LSET, key, String.valueOf(index), value);
    }

    public Long lrem(final String key, final int count, final String value) {
        return redis.getIntegerReply(RedisCommand.LREM, key, String.valueOf(count), value);
    }

    public String lpop(final String key) {
        return redis.getBulkReply(RedisCommand.LPOP, key);
    }

    public String rpop(final String key) {
        return redis.getBulkReply(RedisCommand.RPOP, key);
    }

    public String rpoplpush(final String srckey, final String dstkey) {
        return redis.getBulkReply(RedisCommand.RPOPLPUSH, srckey, dstkey);
    }

    public Boolean sadd(final String key, final String member) {
        return integerReplayToBoolean(redis.getIntegerReply(RedisCommand.SADD, key, member));
    }

    public Set<String> smembers(final String key) {
        return new LinkedHashSet<String>(redis.getStringMultiBulkReply(RedisCommand.SMEMBERS, key));
    }

    public Boolean srem(final String key, final String member) {
        return integerReplayToBoolean(redis.getIntegerReply(RedisCommand.SREM, key, member));
    }

    public String spop(final String key) {
        return redis.getBulkReply(RedisCommand.SPOP, key);
    }

    public Boolean smove(final String srckey, final String dstkey, final String member) {
        return integerReplayToBoolean(redis.getIntegerReply(RedisCommand.SMOVE, srckey, dstkey, member));
    }

    public Long scard(final String key) {
        return redis.getIntegerReply(RedisCommand.SCARD, key);
    }

    public Boolean sismember(final String key, final String member) {
        return integerReplayToBoolean(redis.getIntegerReply(RedisCommand.SISMEMBER, key, member));
    }

    public Set<String> sinter(final String... keys) {
        return new LinkedHashSet<String>(redis.getStringMultiBulkReply(RedisCommand.SINTER, keys));
    }

    public Long sinterstore(final String dstkey, final String... keys) {
        return redis.getIntegerReply(RedisCommand.SINTERSTORE, KeyUtil.joinParams(dstkey, keys));
    }

    public Set<String> sunion(final String... keys) {
        return new LinkedHashSet<String>(redis.getStringMultiBulkReply(RedisCommand.SUNION, keys));
    }

    public Long sunionstore(final String dstkey, final String... keys) {
        return redis.getIntegerReply(RedisCommand.SUNIONSTORE, KeyUtil.joinParams(dstkey, keys));
    }

    public Set<String> sdiff(final String... keys) {
        return new LinkedHashSet<String>(redis.getStringMultiBulkReply(RedisCommand.SDIFF, keys));
    }

    public Long sdiffstore(final String dstkey, final String... keys) {
        return redis.getIntegerReply(RedisCommand.SDIFFSTORE, KeyUtil.joinParams(dstkey, keys));
    }

    public String srandmember(final String key) {
        return redis.getBulkReply(RedisCommand.SRANDMEMBER, key);
    }

    public Boolean zadd(final String key, final Number score, final String member) {
        return integerReplayToBoolean(redis.getIntegerReply(RedisCommand.ZADD, key, String.valueOf(score), member));
    }

    public List<String> zrange(final String key, final int start, final int end) {
        return redis.getStringMultiBulkReply(RedisCommand.ZRANGE, key, String.valueOf(start), String.valueOf(end));
    }

    public Boolean zrem(final String key, final String member) {
        return integerReplayToBoolean(redis.getIntegerReply(RedisCommand.ZREM, key, member));
    }

    public Long zrank(final String key, final String member) {
        return redis.getIntegerReply(RedisCommand.ZRANK, key, member);
    }

    public Long zrevrank(final String key, final String member) {
        return redis.getIntegerReply(RedisCommand.ZREVRANK, key, member);
    }

    public List<String> zrevrange(final String key, final int start, final int end) {
        return redis.getStringMultiBulkReply(RedisCommand.ZREVRANGE, key, String.valueOf(start), String.valueOf(end));
    }

    public List<ElementScore> zrangeWithScores(final String key, final int start,
                                               final int end) {
        return getReplyAsElementScoreSet(redis.getStringMultiBulkReply(RedisCommand.ZRANGE, key, String.valueOf(start), String.valueOf(end), RedisKeyword.WITHSCORES.str));
    }

    public List<ElementScore> zrevrangeWithScores(final String key, final int start,
                                                  final int end) {
        return getReplyAsElementScoreSet(redis.getStringMultiBulkReply(RedisCommand.ZREVRANGE, key, String.valueOf(start), String.valueOf(end), RedisKeyword.WITHSCORES.str));
    }

    public List<String> zrevrangeByScore(String key, String max, String min) {
        return redis.getStringMultiBulkReply(RedisCommand.ZREVRANGEBYSCORE, key, max, min);
    }

    public List<String> zrevrangeByScore(String key, String max, String min, int offset, int count) {
        return redis.getStringMultiBulkReply(RedisCommand.ZREVRANGEBYSCORE, key, max, min, RedisKeyword.LIMIT.str, String.valueOf(offset), String.valueOf(count));
    }

    public List<ElementScore> zrevrangeByScoreWithScores(String key, String max, String min) {
        return getReplyAsElementScoreSet(redis.getStringMultiBulkReply(RedisCommand.ZREVRANGEBYSCORE, key, max, min, RedisKeyword.WITHSCORES.str));
    }

    public List<ElementScore> zrevrangeByScoreWithScores(String key, String max, String min, int offset, int count) {
        return getReplyAsElementScoreSet(redis.getStringMultiBulkReply(RedisCommand.ZREVRANGEBYSCORE, key, max, min, RedisKeyword.LIMIT.str, String.valueOf(offset), String.valueOf(count), RedisKeyword.WITHSCORES.str));
    }

    public Long zcard(final String key) {
        return redis.getIntegerReply(RedisCommand.ZCARD, key);
    }

    public String zscore(final String key, final String member) {
        return redis.getBulkReply(RedisCommand.ZSCORE, key, member);
    }

    public String watch(final String... keys) {
        return redis.getStatusReply(RedisCommand.WATCH, keys);
    }

    public String unwatch() {
        return redis.getStatusReply(RedisCommand.UNWATCH);
    }

    public List<Object> exec() {
        return redis.getMultiBulkReply(RedisCommand.EXEC);
    }

    public void close() {
        redis.close();
    }

    public List<String> sort(final String key) {
        return redis.getStringMultiBulkReply(RedisCommand.SORT, key);
    }

    public List<String> sort(final String key, final SortingParams sortingParameters) {
        final List<String> args = new ArrayList<String>();
        args.add(key);
        args.addAll(sortingParameters.getParams());
        String[] args1 = args.toArray(new String[args.size()]);
        return redis.getStringMultiBulkReply(RedisCommand.SORT, args1);
    }

    public List<String> blpop(final int timeout, final String... keys) {
        List<String> args = new ArrayList<String>();
        args.addAll(Arrays.asList(keys));
        args.add(String.valueOf(timeout));

        List<String> result;
        redis.setTimeoutInfinite();
        try {
            result = redis.getStringMultiBulkReply(RedisCommand.BLPOP, args.toArray(new String[args.size()]));
        } finally {
            redis.rollbackTimeout();
        }

        return result;
    }

    public Long sort(final String key, final SortingParams sortingParameters, final String dstkey) {
        final List<String> args = new ArrayList<String>();
        args.add(key);
        args.addAll(sortingParameters.getParams());
        args.add(RedisKeyword.STORE.str);
        args.add(dstkey);
        String[] args1 = args.toArray(new String[args.size()]);
        return redis.getIntegerReply(RedisCommand.SORT, args1);
    }

    public Long sort(final String key, final String dstkey) {
        return redis.getIntegerReply(RedisCommand.SORT, key, RedisKeyword.STORE.str, dstkey);
    }

    public List<String> brpop(final int timeout, final String... keys) {
        List<String> args = new ArrayList<String>();
        args.addAll(Arrays.asList(keys));
        args.add(String.valueOf(timeout));
        return redis.getStringMultiBulkReply(RedisCommand.BRPOP, args.toArray(new String[args.size()]));
    }

    public String auth(final String password) {
        return redis.getStatusReply(RedisCommand.AUTH, password);
    }

    public Long publish(String channel, String message) {
        return redis.getIntegerReply(RedisCommand.PUBLISH, channel, message);
    }

    public Long zcount(final String key, final Number min, final Number max) {
        return redis.getIntegerReply(RedisCommand.ZCOUNT, key, String.valueOf(min), String.valueOf(max));
    }

    public List<String> zrangeByScore(final String key, final String min, final String max) {
        return redis.getStringMultiBulkReply(RedisCommand.ZRANGEBYSCORE, key, min, max);
    }

    public List<String> zrangeByScore(final String key, final String min, final String max, final int offset, final int count) {
        return redis.getStringMultiBulkReply(RedisCommand.ZRANGEBYSCORE, key, min, max, RedisKeyword.LIMIT.str, String.valueOf(offset), String.valueOf(count));
    }

    public List<ElementScore> zrangeByScoreWithScores(final String key, final String min, final String max) {
        return getReplyAsElementScoreSet(redis.getStringMultiBulkReply(RedisCommand.ZRANGEBYSCORE, key, min, max, RedisKeyword.WITHSCORES.str));
    }

    public List<ElementScore> zrangeByScoreWithScores(final String key, final String min, final String max, final int offset, final int count) {
        return getReplyAsElementScoreSet(redis.getStringMultiBulkReply(RedisCommand.ZRANGEBYSCORE, key, min, max, RedisKeyword.LIMIT.str, String.valueOf(offset), String.valueOf(count), RedisKeyword.WITHSCORES.str));
    }

    public Long zremrangeByRank(final String key, final int start, final int end) {
        return redis.getIntegerReply(RedisCommand.ZREMRANGEBYRANK, key, String.valueOf(start), String.valueOf(end));
    }

    public Long zunionstore(final String dstkey, final String... sets) {
        String[] params = new String[sets.length + 2];
        params[0] = dstkey;
        params[1] = String.valueOf(sets.length);
        System.arraycopy(sets, 0, params, 2, sets.length);
        return redis.getIntegerReply(RedisCommand.ZUNIONSTORE, params);
    }

    public Long zunionstore(final String dstkey, final ZParams params, final String... sets) {
        final List<String> args = new ArrayList<String>();
        args.add(dstkey);
        args.add(String.valueOf(sets.length));
        args.addAll(Arrays.asList(sets));
        args.addAll(params.getParams());
        String[] args1 = args.toArray(new String[args.size()]);
        return redis.getIntegerReply(RedisCommand.ZUNIONSTORE, args1);
    }

    public Long zinterstore(final String dstkey, final String... sets) {
        String[] params = new String[sets.length + 2];
        params[0] = dstkey;
        params[1] = String.valueOf(sets.length);
        System.arraycopy(sets, 0, params, 2, sets.length);
        return redis.getIntegerReply(RedisCommand.ZINTERSTORE, params);
    }

    public Long zinterstore(final String dstkey, final ZParams params, final String... sets) {
        final List<String> args = new ArrayList<String>();
        args.add(dstkey);
        args.add(String.valueOf(sets.length));
        args.addAll(Arrays.asList(sets));
        args.addAll(params.getParams());
        String[] args1 = args.toArray(new String[args.size()]);
        return redis.getIntegerReply(RedisCommand.ZINTERSTORE, args1);
    }

    public Long strlen(final String key) {
        return redis.getIntegerReply(RedisCommand.STRLEN, key);
    }

    public Long lpushx(final String key, final String value) {
        return redis.getIntegerReply(RedisCommand.LPUSHX, key, value);
    }

    public Boolean persist(final String key) {
        return integerReplayToBoolean(redis.getIntegerReply(RedisCommand.PERSIST, key));
    }

    public Long rpushx(final String key, final String value) {
        return redis.getIntegerReply(RedisCommand.RPUSHX, key, value);
    }

    public String echo(final String string) {
        return redis.getBulkReply(RedisCommand.ECHO, string);
    }

    public Long linsert(final String key, final ListPosition where,
                        final String pivot, final String value) {
        return redis.getIntegerReply(RedisCommand.LINSERT, key, where.name(), pivot, value);
    }

    public String brpoplpush(String source, String destination, int timeout) {
        return redis.getBulkReply(RedisCommand.BRPOPLPUSH, source, destination, String.valueOf(timeout));
    }

    public Long setBit(String key, int offset, String value) {
        return redis.getIntegerReply(RedisCommand.SETBIT, key, String.valueOf(offset), value);
    }

    public Long getBit(String key, int offset) {
        return redis.getIntegerReply(RedisCommand.GETBIT, key, String.valueOf(offset));
    }

    public String ping() {
        return redis.getStatusReply(RedisCommand.PING);
    }

    public String zincrby(final String key, final Number score, final String member) {
        return redis.getBulkReply(RedisCommand.ZINCRBY, key, String.valueOf(score), member);
    }

    public StatusReplyCommands multi() {
        redis.getStatusReply(RedisCommand.MULTI);
        return redis;
    }

    public String discard() {
        return redis.getStatusReply(RedisCommand.DISCARD);
    }

    public List<Object> pipeline(final Pipeline pipeline) {
        pipeline.execute(redis);
        return redis.getAll();
    }

    public Long zremrangeByScore(final String key, final String min, final String max) {
        return redis.getIntegerReply(RedisCommand.ZREMRANGEBYSCORE, key, min, max);
    }

    public String save() {
        return redis.getStatusReply(RedisCommand.SAVE);
    }

    public String bgsave() {
        return redis.getStatusReply(RedisCommand.BGSAVE);
    }

    public String bgrewriteaof() {
        return redis.getStatusReply(RedisCommand.BGREWRITEAOF);
    }

    public Long lastsave() {
        return redis.getIntegerReply(RedisCommand.LASTSAVE);
    }

    public String shutdown() {
        String status;
        try {
            status = redis.getStatusReply(RedisCommand.SHUTDOWN);
        } catch (RedisException ex) {
            status = null;
        }
        return status;
    }

    public String info() {
        return redis.getBulkReply(RedisCommand.INFO);
    }

    public void monitor(final RedisMonitor redisMonitor) {
        redis.noReply(RedisCommand.MONITOR);
        do {
            String command = redis.getBulkReply();
            redisMonitor.onCommand(command);
        } while (redis.isConnected());
    }

    public String slaveof(final String host, final int port) {
        return redis.getStatusReply(RedisCommand.SLAVEOF, host, String.valueOf(port));
    }

    public String slaveofNoOne() {
        return redis.getStatusReply(RedisCommand.SLAVEOF, RedisKeyword.NO.str, RedisKeyword.ONE.str);
    }

    public List<String> configGet(final String pattern) {
        return redis.getStringMultiBulkReply(RedisCommand.CONFIG, RedisKeyword.GET.str, pattern);
    }

    public String configSet(final String parameter, final String value) {
        return redis.getStatusReply(RedisCommand.CONFIG, RedisKeyword.SET.str, parameter, value);
    }

    public void sync() {
        redis.noReply(RedisCommand.SYNC);
    }

    public String debug(final DebugParams params) {
        return redis.getStatusReply(RedisCommand.DEBUG, params.getCommand());
    }

    private Boolean integerReplayToBoolean(Long replay) {
        if (replay == null) {
            return null;
        } else {
            return replay == 1;
        }
    }

    private List<ElementScore> getReplyAsElementScoreSet(List<String> membersWithScores) {
        List<ElementScore> result = new ArrayList<ElementScore>();
        Iterator<String> iterator = membersWithScores.iterator();
        while (iterator.hasNext()) {
            result.add(new ElementScore(iterator.next(), iterator.next()));
        }
        return result;
    }
}
