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
public class RedisSessionImpl implements Session {
    private final RedisClientImpl client;

    public RedisSessionImpl(RedisConnection connection) {
        this.client = new RedisClientImpl(connection);
    }

    public String set(final String key, String value) {
        return client.getStatusReply(RedisCommand.SET, key, value);
    }

    public String get(final String key) {
        return client.getBulkReply(RedisCommand.GET, key);
    }

    public void quit() {
        client.noReply(RedisCommand.QUIT);
    }

    public Boolean exists(final String key) {
        return integerReplayToBoolean(client.getIntegerReply(RedisCommand.EXISTS, key));
    }

    public Long del(final String... keys) {
        return client.getIntegerReply(RedisCommand.DEL, keys);
    }

    public String type(final String key) {
        return client.getStatusReply(RedisCommand.TYPE, key);
    }

    public String flushDB() {
        return client.getStatusReply(RedisCommand.FLUSHDB);
    }

    @SuppressWarnings({"unchecked"})
    public Set<String> keys(final String pattern) {
        return new HashSet<String>(client.getStringMultiBulkReply(RedisCommand.KEYS, pattern));
    }

    public String randomKey() {
        return client.getBulkReply(RedisCommand.RANDOMKEY);
    }

    public String rename(final String key, final String newKey) {
        return client.getStatusReply(RedisCommand.RENAME, key, newKey);
    }

    public Boolean renamenx(final String key, final String newKey) {
        return integerReplayToBoolean(client.getIntegerReply(RedisCommand.RENAMENX, key, newKey));
    }

    public Long dbSize() {
        return client.getIntegerReply(RedisCommand.DBSIZE);
    }

    public Boolean expire(final String key, final int seconds) {
        return integerReplayToBoolean(client.getIntegerReply(RedisCommand.EXPIRE, key, String.valueOf(seconds)));
    }

    public Boolean expireAt(final String key, final long unixTime) {
        return integerReplayToBoolean(client.getIntegerReply(RedisCommand.EXPIREAT, key, String.valueOf(unixTime)));
    }

    public Long ttl(final String key) {
        return client.getIntegerReply(RedisCommand.TTL, key);
    }

    public String select(final int index) {
        return client.getStatusReply(RedisCommand.SELECT, String.valueOf(index));
    }


    public Boolean move(final String key, final int dbIndex) {
        return integerReplayToBoolean(client.getIntegerReply(RedisCommand.MOVE, key, String.valueOf(dbIndex)));
    }

    public String flushAll() {
        return client.getStatusReply(RedisCommand.FLUSHALL);
    }

    public String getSet(final String key, final String value) {
        return client.getBulkReply(RedisCommand.GETSET, key, value);
    }

    public List<String> mget(final String... keys) {
        return client.getStringMultiBulkReply(RedisCommand.MGET, keys);
    }

    public Boolean setnx(final String key, final String value) {
        return integerReplayToBoolean(client.getIntegerReply(RedisCommand.SETNX, key, value));
    }

    public String setex(final String key, final int seconds, final String value) {
        return client.getStatusReply(RedisCommand.SETEX, key, String.valueOf(seconds), value);
    }

    public String mset(final String... keysvalues) {
        return client.getStatusReply(RedisCommand.MSET, keysvalues);
    }

    public Boolean msetnx(final String... keysvalues) {
        return integerReplayToBoolean(client.getIntegerReply(RedisCommand.MSETNX, keysvalues));
    }

    public Long decrBy(final String key, final int value) {
        return client.getIntegerReply(RedisCommand.DECRBY, key, String.valueOf(value));
    }

    public Long decr(final String key) {
        return client.getIntegerReply(RedisCommand.DECR, key);
    }

    public Long incrBy(final String key, final int value) {
        return client.getIntegerReply(RedisCommand.INCRBY, key, String.valueOf(value));
    }

    public Long incr(final String key) {
        return client.getIntegerReply(RedisCommand.INCR, key);
    }

    public Long append(final String key, final String value) {
        return client.getIntegerReply(RedisCommand.APPEND, key, value);
    }

    public String getRange(final String key, final int start, final int end) {
        return client.getBulkReply(RedisCommand.GETRANGE, key, String.valueOf(start), String.valueOf(end));
    }

    public Long setRange(String key, int offset, String value) {
        return client.getIntegerReply(RedisCommand.SETRANGE, key, String.valueOf(offset), value);
    }

    public Boolean hset(final String key, final String field, final String value) {
        return integerReplayToBoolean(client.getIntegerReply(RedisCommand.HSET, key, field, value));
    }

    public String hget(final String key, final String field) {
        return client.getBulkReply(RedisCommand.HGET, key, field);
    }

    public Boolean hsetnx(final String key, final String field, final String value) {
        return integerReplayToBoolean(client.getIntegerReply(RedisCommand.HSETNX, key, field, value));
    }

    public String hmset(final String key, final Map<String, String> hash) {
        final List<String> params = new ArrayList<String>();
        params.add(key);

        for (String field : hash.keySet()) {
            params.add(field);
            params.add(hash.get(field));
        }
        String[] args = params.toArray(new String[params.size()]);
        return client.getStatusReply(RedisCommand.HMSET, args);
    }

    public List<String> hmget(final String key, final String... fields) {
        String[] params = new String[fields.length + 1];
        params[0] = key;
        System.arraycopy(fields, 0, params, 1, fields.length);
        return client.getStringMultiBulkReply(RedisCommand.HMGET, params);
    }

    public Long hincrBy(final String key, final String field, final int value) {
        return client.getIntegerReply(RedisCommand.HINCRBY, key, field, String.valueOf(value));
    }

    public Boolean hexists(final String key, final String field) {
        return integerReplayToBoolean(client.getIntegerReply(RedisCommand.HEXISTS, key, field));
    }

    public Boolean hdel(final String key, final String field) {
        return integerReplayToBoolean(client.getIntegerReply(RedisCommand.HDEL, key, field));
    }

    public Long hlen(final String key) {
        return client.getIntegerReply(RedisCommand.HLEN, key);
    }

    public Set<String> hkeys(final String key) {
        return new HashSet<String>(client.getStringMultiBulkReply(RedisCommand.HKEYS, key));
    }

    public List<String> hvals(final String key) {
        return client.getStringMultiBulkReply(RedisCommand.HVALS, key);
    }

    public Map<String, String> hgetAll(final String key) {
        final List<String> flatHash = client.getStringMultiBulkReply(RedisCommand.HGETALL, key);
        final Map<String, String> hash = new HashMap<String, String>();
        final Iterator<String> iterator = flatHash.iterator();
        while (iterator.hasNext()) {
            hash.put(iterator.next(), iterator.next());
        }

        return hash;
    }

    public Long rpush(final String key, final String value) {
        return client.getIntegerReply(RedisCommand.RPUSH, key, value);
    }

    public Long lpush(final String key, final String value) {
        return client.getIntegerReply(RedisCommand.LPUSH, key, value);
    }

    public Long llen(final String key) {
        return client.getIntegerReply(RedisCommand.LLEN, key);
    }

    public List<String> lrange(final String key, final int start, final int end) {
        return client.getStringMultiBulkReply(RedisCommand.LRANGE, key, String.valueOf(start), String.valueOf(end));
    }

    public String ltrim(final String key, final int start, final int end) {
        return client.getStatusReply(RedisCommand.LTRIM, key, String.valueOf(start), String.valueOf(end));
    }

    public String lindex(final String key, final int index) {
        return client.getBulkReply(RedisCommand.LINDEX, key, String.valueOf(index));
    }

    public String lset(final String key, final int index, final String value) {
        return client.getStatusReply(RedisCommand.LSET, key, String.valueOf(index), value);
    }

    public Long lrem(final String key, final int count, final String value) {
        return client.getIntegerReply(RedisCommand.LREM, key, String.valueOf(count), value);
    }

    public String lpop(final String key) {
        return client.getBulkReply(RedisCommand.LPOP, key);
    }

    public String rpop(final String key) {
        return client.getBulkReply(RedisCommand.RPOP, key);
    }

    public String rpoplpush(final String srckey, final String dstkey) {
        return client.getBulkReply(RedisCommand.RPOPLPUSH, srckey, dstkey);
    }

    public Boolean sadd(final String key, final String member) {
        return integerReplayToBoolean(client.getIntegerReply(RedisCommand.SADD, key, member));
    }

    public Set<String> smembers(final String key) {
        return new LinkedHashSet<String>(client.getStringMultiBulkReply(RedisCommand.SMEMBERS, key));
    }

    public Boolean srem(final String key, final String member) {
        return integerReplayToBoolean(client.getIntegerReply(RedisCommand.SREM, key, member));
    }

    public String spop(final String key) {
        return client.getBulkReply(RedisCommand.SPOP, key);
    }

    public Boolean smove(final String srckey, final String dstkey, final String member) {
        return integerReplayToBoolean(client.getIntegerReply(RedisCommand.SMOVE, srckey, dstkey, member));
    }

    public Long scard(final String key) {
        return client.getIntegerReply(RedisCommand.SCARD, key);
    }

    public Boolean sismember(final String key, final String member) {
        return integerReplayToBoolean(client.getIntegerReply(RedisCommand.SISMEMBER, key, member));
    }

    public Set<String> sinter(final String... keys) {
        return new LinkedHashSet<String>(client.getStringMultiBulkReply(RedisCommand.SINTER, keys));
    }

    public Long sinterstore(final String dstkey, final String... keys) {
        return client.getIntegerReply(RedisCommand.SINTERSTORE, KeyUtil.joinParams(dstkey, keys));
    }

    public Set<String> sunion(final String... keys) {
        return new LinkedHashSet<String>(client.getStringMultiBulkReply(RedisCommand.SUNION, keys));
    }

    public Long sunionstore(final String dstkey, final String... keys) {
        return client.getIntegerReply(RedisCommand.SUNIONSTORE, KeyUtil.joinParams(dstkey, keys));
    }

    public Set<String> sdiff(final String... keys) {
        return new LinkedHashSet<String>(client.getStringMultiBulkReply(RedisCommand.SDIFF, keys));
    }

    public Long sdiffstore(final String dstkey, final String... keys) {
        return client.getIntegerReply(RedisCommand.SDIFFSTORE, KeyUtil.joinParams(dstkey, keys));
    }

    public String srandmember(final String key) {
        return client.getBulkReply(RedisCommand.SRANDMEMBER, key);
    }

    public Boolean zadd(final String key, final Number score, final String member) {
        return integerReplayToBoolean(client.getIntegerReply(RedisCommand.ZADD, key, String.valueOf(score), member));
    }

    public List<String> zrange(final String key, final int start, final int end) {
        return client.getStringMultiBulkReply(RedisCommand.ZRANGE, key, String.valueOf(start), String.valueOf(end));
    }

    public Boolean zrem(final String key, final String member) {
        return integerReplayToBoolean(client.getIntegerReply(RedisCommand.ZREM, key, member));
    }

    public Long zrank(final String key, final String member) {
        return client.getIntegerReply(RedisCommand.ZRANK, key, member);
    }

    public Long zrevrank(final String key, final String member) {
        return client.getIntegerReply(RedisCommand.ZREVRANK, key, member);
    }

    public List<String> zrevrange(final String key, final int start, final int end) {
        return client.getStringMultiBulkReply(RedisCommand.ZREVRANGE, key, String.valueOf(start), String.valueOf(end));
    }

    public List<ElementScore> zrangeWithScores(final String key, final int start,
                                               final int end) {
        return getReplyAsElementScoreSet(client.getStringMultiBulkReply(RedisCommand.ZRANGE, key, String.valueOf(start), String.valueOf(end), RedisKeyword.WITHSCORES.str));
    }

    public List<ElementScore> zrevrangeWithScores(final String key, final int start,
                                                  final int end) {
        return getReplyAsElementScoreSet(client.getStringMultiBulkReply(RedisCommand.ZREVRANGE, key, String.valueOf(start), String.valueOf(end), RedisKeyword.WITHSCORES.str));
    }

    public List<String> zrevrangeByScore(String key, String max, String min) {
        return client.getStringMultiBulkReply(RedisCommand.ZREVRANGEBYSCORE, key, max, min);
    }

    public List<String> zrevrangeByScore(String key, String max, String min, int offset, int count) {
        return client.getStringMultiBulkReply(RedisCommand.ZREVRANGEBYSCORE, key, max, min, RedisKeyword.LIMIT.str, String.valueOf(offset), String.valueOf(count));
    }

    public List<ElementScore> zrevrangeByScoreWithScores(String key, String max, String min) {
        return getReplyAsElementScoreSet(client.getStringMultiBulkReply(RedisCommand.ZREVRANGEBYSCORE, key, max, min, RedisKeyword.WITHSCORES.str));
    }

    public List<ElementScore> zrevrangeByScoreWithScores(String key, String max, String min, int offset, int count) {
        return getReplyAsElementScoreSet(client.getStringMultiBulkReply(RedisCommand.ZREVRANGEBYSCORE, key, max, min, RedisKeyword.LIMIT.str, String.valueOf(offset), String.valueOf(count), RedisKeyword.WITHSCORES.str));
    }

    public Long zcard(final String key) {
        return client.getIntegerReply(RedisCommand.ZCARD, key);
    }

    public String zscore(final String key, final String member) {
        return client.getBulkReply(RedisCommand.ZSCORE, key, member);
    }

    public String watch(final String... keys) {
        return client.getStatusReply(RedisCommand.WATCH, keys);
    }

    public String unwatch() {
        return client.getStatusReply(RedisCommand.UNWATCH);
    }

    public List<Object> exec() {
        return client.getMultiBulkReply(RedisCommand.EXEC);
    }

    public void close() {
        client.close();
    }

    public List<String> sort(final String key) {
        return client.getStringMultiBulkReply(RedisCommand.SORT, key);
    }

    public List<String> sort(final String key, final SortingParams sortingParameters) {
        final List<String> args = new ArrayList<String>();
        args.add(key);
        args.addAll(sortingParameters.getParams());
        String[] args1 = args.toArray(new String[args.size()]);
        return client.getStringMultiBulkReply(RedisCommand.SORT, args1);
    }

    public List<String> blpop(final int timeout, final String... keys) {
        List<String> args = new ArrayList<String>();
        args.addAll(Arrays.asList(keys));
        args.add(String.valueOf(timeout));

        List<String> result;
        client.setTimeoutInfinite();
        try {
            result = client.getStringMultiBulkReply(RedisCommand.BLPOP, args.toArray(new String[args.size()]));
        } finally {
            client.rollbackTimeout();
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
        return client.getIntegerReply(RedisCommand.SORT, args1);
    }

    public Long sort(final String key, final String dstkey) {
        return client.getIntegerReply(RedisCommand.SORT, key, RedisKeyword.STORE.str, dstkey);
    }

    public List<String> brpop(final int timeout, final String... keys) {
        List<String> args = new ArrayList<String>();
        args.addAll(Arrays.asList(keys));
        args.add(String.valueOf(timeout));
        List<String> result;
        client.setTimeoutInfinite();
        try {
            result = client.getStringMultiBulkReply(RedisCommand.BRPOP, args.toArray(new String[args.size()]));
        } finally {
            client.rollbackTimeout();
        }

        return result;
    }

    public String auth(final String password) {
        return client.getStatusReply(RedisCommand.AUTH, password);
    }

    public Long publish(String channel, String message) {
        return client.getIntegerReply(RedisCommand.PUBLISH, channel, message);
    }

    public Long zcount(final String key, final Number min, final Number max) {
        return client.getIntegerReply(RedisCommand.ZCOUNT, key, String.valueOf(min), String.valueOf(max));
    }

    public List<String> zrangeByScore(final String key, final String min, final String max) {
        return client.getStringMultiBulkReply(RedisCommand.ZRANGEBYSCORE, key, min, max);
    }

    public List<String> zrangeByScore(final String key, final String min, final String max, final int offset, final int count) {
        return client.getStringMultiBulkReply(RedisCommand.ZRANGEBYSCORE, key, min, max, RedisKeyword.LIMIT.str, String.valueOf(offset), String.valueOf(count));
    }

    public List<ElementScore> zrangeByScoreWithScores(final String key, final String min, final String max) {
        return getReplyAsElementScoreSet(client.getStringMultiBulkReply(RedisCommand.ZRANGEBYSCORE, key, min, max, RedisKeyword.WITHSCORES.str));
    }

    public List<ElementScore> zrangeByScoreWithScores(final String key, final String min, final String max, final int offset, final int count) {
        return getReplyAsElementScoreSet(client.getStringMultiBulkReply(RedisCommand.ZRANGEBYSCORE, key, min, max, RedisKeyword.LIMIT.str, String.valueOf(offset), String.valueOf(count), RedisKeyword.WITHSCORES.str));
    }

    public Long zremrangeByRank(final String key, final int start, final int end) {
        return client.getIntegerReply(RedisCommand.ZREMRANGEBYRANK, key, String.valueOf(start), String.valueOf(end));
    }

    public Long zunionstore(final String dstkey, final String... sets) {
        String[] params = new String[sets.length + 2];
        params[0] = dstkey;
        params[1] = String.valueOf(sets.length);
        System.arraycopy(sets, 0, params, 2, sets.length);
        return client.getIntegerReply(RedisCommand.ZUNIONSTORE, params);
    }

    public Long zunionstore(final String dstkey, final ZParams params, final String... sets) {
        final List<String> args = new ArrayList<String>();
        args.add(dstkey);
        args.add(String.valueOf(sets.length));
        args.addAll(Arrays.asList(sets));
        args.addAll(params.getParams());
        String[] args1 = args.toArray(new String[args.size()]);
        return client.getIntegerReply(RedisCommand.ZUNIONSTORE, args1);
    }

    public Long zinterstore(final String dstkey, final String... sets) {
        String[] params = new String[sets.length + 2];
        params[0] = dstkey;
        params[1] = String.valueOf(sets.length);
        System.arraycopy(sets, 0, params, 2, sets.length);
        return client.getIntegerReply(RedisCommand.ZINTERSTORE, params);
    }

    public Long zinterstore(final String dstkey, final ZParams params, final String... sets) {
        final List<String> args = new ArrayList<String>();
        args.add(dstkey);
        args.add(String.valueOf(sets.length));
        args.addAll(Arrays.asList(sets));
        args.addAll(params.getParams());
        String[] args1 = args.toArray(new String[args.size()]);
        return client.getIntegerReply(RedisCommand.ZINTERSTORE, args1);
    }

    public Long strlen(final String key) {
        return client.getIntegerReply(RedisCommand.STRLEN, key);
    }

    public Long lpushx(final String key, final String value) {
        return client.getIntegerReply(RedisCommand.LPUSHX, key, value);
    }

    public Boolean persist(final String key) {
        return integerReplayToBoolean(client.getIntegerReply(RedisCommand.PERSIST, key));
    }

    public Long rpushx(final String key, final String value) {
        return client.getIntegerReply(RedisCommand.RPUSHX, key, value);
    }

    public String echo(final String string) {
        return client.getBulkReply(RedisCommand.ECHO, string);
    }

    public Long linsert(final String key, final ListPosition where,
                        final String pivot, final String value) {
        return client.getIntegerReply(RedisCommand.LINSERT, key, where.name(), pivot, value);
    }

    public String brpoplpush(String source, String destination, int timeout) {
        return client.getBulkReply(RedisCommand.BRPOPLPUSH, source, destination, String.valueOf(timeout));
    }

    public Long setBit(String key, int offset, String value) {
        return client.getIntegerReply(RedisCommand.SETBIT, key, String.valueOf(offset), value);
    }

    public Long getBit(String key, int offset) {
        return client.getIntegerReply(RedisCommand.GETBIT, key, String.valueOf(offset));
    }

    public String ping() {
        return client.getStatusReply(RedisCommand.PING);
    }

    public String zincrby(final String key, final Number score, final String member) {
        return client.getBulkReply(RedisCommand.ZINCRBY, key, String.valueOf(score), member);
    }

    public RedisClient multi() {
        client.getStatusReply(RedisCommand.MULTI);
        return client;
    }

    public String discard() {
        return client.getStatusReply(RedisCommand.DISCARD);
    }

    public List<Object> pipeline(final Pipeline pipeline) {
        pipeline.execute(client);
        return client.getAll();
    }

    public Long zremrangeByScore(final String key, final String min, final String max) {
        return client.getIntegerReply(RedisCommand.ZREMRANGEBYSCORE, key, min, max);
    }

    public String save() {
        return client.getStatusReply(RedisCommand.SAVE);
    }

    public String bgsave() {
        return client.getStatusReply(RedisCommand.BGSAVE);
    }

    public String bgrewriteaof() {
        return client.getStatusReply(RedisCommand.BGREWRITEAOF);
    }

    public Long lastsave() {
        return client.getIntegerReply(RedisCommand.LASTSAVE);
    }

    public String shutdown() {
        String status;
        try {
            status = client.getStatusReply(RedisCommand.SHUTDOWN);
        } catch (RedisException ex) {
            status = null;
        }
        return status;
    }

    public String info() {
        return client.getBulkReply(RedisCommand.INFO);
    }

    public void monitor(final RedisMonitor redisMonitor) {
        client.noReply(RedisCommand.MONITOR);
        do {
            String command = client.getBulkReply();
            redisMonitor.onCommand(command);
        } while (client.isConnected());
    }

    public String slaveof(final String host, final int port) {
        return client.getStatusReply(RedisCommand.SLAVEOF, host, String.valueOf(port));
    }

    public String slaveofNoOne() {
        return client.getStatusReply(RedisCommand.SLAVEOF, RedisKeyword.NO.str, RedisKeyword.ONE.str);
    }

    public List<String> configGet(final String pattern) {
        return client.getStringMultiBulkReply(RedisCommand.CONFIG, RedisKeyword.GET.str, pattern);
    }

    public String configSet(final String parameter, final String value) {
        return client.getStatusReply(RedisCommand.CONFIG, RedisKeyword.SET.str, parameter, value);
    }

    public void sync() {
        client.noReply(RedisCommand.SYNC);
    }

    public String debug(final DebugParams params) {
        return client.getStatusReply(RedisCommand.DEBUG, params.getCommand());
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
