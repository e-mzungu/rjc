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
import org.idevlab.rjc.protocol.Protocol;
import org.idevlab.rjc.protocol.RedisCommand;
import org.idevlab.rjc.protocol.RedisKeyword;
import org.idevlab.rjc.util.SafeEncoder;

import java.util.*;

public class Client implements RedisCommands {

    private final RedisConnection conn;


    public enum LIST_POSITION {
        BEFORE, AFTER
    }

    private boolean isInMulti;

    public Client(RedisConnection conn) {
        this.conn = conn;
    }

    /**
     * Closes underlying connection
     */
    public void close() {
        conn.close();
    }

    public boolean isInMulti() {
        return isInMulti;
    }

    public void ping() {
        conn.sendCommand(RedisCommand.PING);
    }

    public void set(final String key, final String value) {
        conn.sendCommand(RedisCommand.SET, key, value);
    }

    public void get(final String key) {
        conn.sendCommand(RedisCommand.GET, SafeEncoder.encode(key));
    }

    public void quit() {
        conn.sendCommand(RedisCommand.QUIT);
    }

    public void exists(final String key) {
        conn.sendCommand(RedisCommand.EXISTS, SafeEncoder.encode(key));
    }

    public void del(final String... keys) {
        conn.sendCommand(RedisCommand.DEL, keys);
    }

    public void type(final String key) {
        conn.sendCommand(RedisCommand.TYPE, SafeEncoder.encode(key));
    }

    public void flushDB() {
        conn.sendCommand(RedisCommand.FLUSHDB);
    }

    public void keys(final String pattern) {
        conn.sendCommand(RedisCommand.KEYS, pattern);
    }

    public void randomKey() {
        conn.sendCommand(RedisCommand.RANDOMKEY);
    }

    public void rename(final String oldkey, final String newkey) {
        conn.sendCommand(RedisCommand.RENAME, oldkey, newkey);
    }

    public void renamenx(final String oldkey, final String newkey) {
        conn.sendCommand(RedisCommand.RENAMENX, oldkey, newkey);
    }

    public void dbSize() {
        conn.sendCommand(RedisCommand.DBSIZE);
    }

    public void expire(final String key, final int seconds) {
        conn.sendCommand(RedisCommand.EXPIRE, SafeEncoder.encode(key), Protocol.toByteArray(seconds));
    }

    public void expireAt(final String key, final long unixTime) {
        conn.sendCommand(RedisCommand.EXPIREAT, SafeEncoder.encode(key), Protocol.toByteArray(unixTime));
    }

    public void ttl(final String key) {
        conn.sendCommand(RedisCommand.TTL, SafeEncoder.encode(key));
    }

    public void select(final int index) {
        conn.sendCommand(RedisCommand.SELECT, Protocol.toByteArray(index));
    }

    public void move(final String key, final int dbIndex) {
        conn.sendCommand(RedisCommand.MOVE, SafeEncoder.encode(key), Protocol.toByteArray(dbIndex));
    }

    public void flushAll() {
        conn.sendCommand(RedisCommand.FLUSHALL);
    }

    public void getSet(final String key, String value) {
        conn.sendCommand(RedisCommand.GETSET, key, value);
    }

    public void mget(final String... keys) {
        conn.sendCommand(RedisCommand.MGET, keys);
    }

    public void setnx(final String key, String value) {
        conn.sendCommand(RedisCommand.SETNX, key, value);
    }

    public void setex(final String key, final int seconds, String value) {
        conn.sendCommand(RedisCommand.SETEX, key, String.valueOf(seconds), value);
    }

    public void mset(String... keysvalues) {
        conn.sendCommand(RedisCommand.MSET, keysvalues);
    }

    public void msetnx(String... keysvalues) {
        conn.sendCommand(RedisCommand.MSETNX, keysvalues);
    }

    public void decrBy(final String key, final int integer) {
        conn.sendCommand(RedisCommand.DECRBY, key, String.valueOf(integer));
    }

    public void decr(final String key) {
        conn.sendCommand(RedisCommand.DECR, key);
    }

    public void incrBy(final String key, final int integer) {
        conn.sendCommand(RedisCommand.INCRBY, key, String.valueOf(integer));
    }

    public void incr(final String key) {
        conn.sendCommand(RedisCommand.INCR, key);
    }

    public void append(final String key, String value) {
        conn.sendCommand(RedisCommand.APPEND, key, value);
    }

    public void getRange(final String key, final int start, final int end) {
        conn.sendCommand(RedisCommand.GETRANGE, key, String.valueOf(start), String.valueOf(end));
    }

    public void setRange(String key, int offset, String value) {
        conn.sendCommand(RedisCommand.SETRANGE, key, String.valueOf(offset), value);
    }

    public void hset(final String key, String field, String value) {
        conn.sendCommand(RedisCommand.HSET, key, field, value);
    }

    public void hget(final String key, String field) {
        conn.sendCommand(RedisCommand.HGET, key, field);
    }

    public void hsetnx(final String key, String field, String value) {
        conn.sendCommand(RedisCommand.HSETNX, key, field, value);
    }

    public void hmset(final String key, final Map<String, String> hash) {
        final List<String> params = new ArrayList<String>();
        params.add(key);

        for (String field : hash.keySet()) {
            params.add(field);
            params.add(hash.get(field));
        }
        String[] args = params.toArray(new String[params.size()]);
        conn.sendCommand(RedisCommand.HMSET, args);
    }

    public void hmget(final String key, String... fields) {
        String[] params = new String[fields.length + 1];
        params[0] = key;
        System.arraycopy(fields, 0, params, 1, fields.length);
        conn.sendCommand(RedisCommand.HMGET, params);
    }

    public void hincrBy(final String key, String field, final int value) {
        conn.sendCommand(RedisCommand.HINCRBY, key, field, String.valueOf(value));
    }

    public void hexists(final String key, String field) {
        conn.sendCommand(RedisCommand.HEXISTS, key, field);
    }

    public void hdel(final String key, String field) {
        conn.sendCommand(RedisCommand.HDEL, key, field);
    }

    public void hlen(final String key) {
        conn.sendCommand(RedisCommand.HLEN, key);
    }

    public void hkeys(final String key) {
        conn.sendCommand(RedisCommand.HKEYS, key);
    }

    public void hvals(final String key) {
        conn.sendCommand(RedisCommand.HVALS, key);
    }

    public void hgetAll(final String key) {
        conn.sendCommand(RedisCommand.HGETALL, key);
    }

    public void rpush(final String key, String string) {
        conn.sendCommand(RedisCommand.RPUSH, key, string);
    }

    public void lpush(final String key, String string) {
        conn.sendCommand(RedisCommand.LPUSH, key, string);
    }

    public void llen(final String key) {
        conn.sendCommand(RedisCommand.LLEN, key);
    }

    public void lrange(final String key, final int start, final int end) {
        conn.sendCommand(RedisCommand.LRANGE, key, String.valueOf(start), String.valueOf(end));
    }

    public void ltrim(final String key, final int start, final int end) {
        conn.sendCommand(RedisCommand.LTRIM, key, String.valueOf(start), String.valueOf(end));
    }

    public void lindex(final String key, final int index) {
        conn.sendCommand(RedisCommand.LINDEX, key, String.valueOf(index));
    }

    public void lset(final String key, final int index, String value) {
        conn.sendCommand(RedisCommand.LSET, key, String.valueOf(index), value);
    }

    public void lrem(final String key, int count, String value) {
        conn.sendCommand(RedisCommand.LREM, key, String.valueOf(count), value);
    }

    public void lpop(final String key) {
        conn.sendCommand(RedisCommand.LPOP, key);
    }

    public void rpop(final String key) {
        conn.sendCommand(RedisCommand.RPOP, key);
    }

    public void rpoplpush(final String srckey, final String dstkey) {
        conn.sendCommand(RedisCommand.RPOPLPUSH, srckey, dstkey);
    }

    public void sadd(final String key, String member) {
        conn.sendCommand(RedisCommand.SADD, key, member);
    }

    public void smembers(final String key) {
        conn.sendCommand(RedisCommand.SMEMBERS, key);
    }

    public void srem(final String key, String member) {
        conn.sendCommand(RedisCommand.SREM, key, member);
    }

    public void spop(final String key) {
        conn.sendCommand(RedisCommand.SPOP, key);
    }

    public void smove(final String srckey, final String dstkey,
                      String member) {
        conn.sendCommand(RedisCommand.SMOVE, srckey, dstkey, member);
    }

    public void scard(final String key) {
        conn.sendCommand(RedisCommand.SCARD, key);
    }

    public void sismember(final String key, String member) {
        conn.sendCommand(RedisCommand.SISMEMBER, key, member);
    }

    public void sinter(final String... keys) {
        conn.sendCommand(RedisCommand.SINTER, keys);
    }

    public void sinterstore(final String dstkey, final String... keys) {
        conn.sendCommand(RedisCommand.SINTERSTORE, joinParams(dstkey, keys));
    }

    private static String[] joinParams(String dstkey, String[] keys) {
        final String[] params = new String[keys.length + 1];
        params[0] = dstkey;
        System.arraycopy(keys, 0, params, 1, keys.length);
        return params;
    }

    public void sunion(final String... keys) {
        conn.sendCommand(RedisCommand.SUNION, keys);
    }

    public void sunionstore(final String dstkey, final String... keys) {
        conn.sendCommand(RedisCommand.SUNIONSTORE, joinParams(dstkey, keys));
    }

    public void sdiff(final String... keys) {
        conn.sendCommand(RedisCommand.SDIFF, keys);
    }

    public void sdiffstore(final String dstkey, final String... keys) {
        conn.sendCommand(RedisCommand.SDIFFSTORE, joinParams(dstkey, keys));
    }

    public void srandmember(final String key) {
        conn.sendCommand(RedisCommand.SRANDMEMBER, key);
    }

    public void zadd(final String key, final Number score, String member) {
        conn.sendCommand(RedisCommand.ZADD, key, String.valueOf(score), member);
    }

    public void zrange(final String key, final int start, final int end) {
        conn.sendCommand(RedisCommand.ZRANGE, key, String.valueOf(start), String.valueOf(end));
    }

    public void zrem(final String key, String member) {
        conn.sendCommand(RedisCommand.ZREM, key, member);
    }

    public void zincrby(final String key, final Number score,
                        String member) {
        conn.sendCommand(RedisCommand.ZINCRBY, key, String.valueOf(score), member);
    }

    public void zrank(final String key, String member) {
        conn.sendCommand(RedisCommand.ZRANK, key, member);
    }

    public void zrevrank(final String key, String member) {
        conn.sendCommand(RedisCommand.ZREVRANK, key, member);
    }

    public void zrevrange(final String key, final int start, final int end) {
        conn.sendCommand(RedisCommand.ZREVRANGE, key, String.valueOf(start), String.valueOf(end));
    }

    public void zrangeWithScores(final String key, final int start,
                                 final int end) {
        conn.sendCommand(RedisCommand.ZRANGE, key, String.valueOf(start), String.valueOf(end), RedisKeyword.WITHSCORES.str);
    }

    public void zrevrangeWithScores(final String key, final int start,
                                    final int end) {
        conn.sendCommand(RedisCommand.ZREVRANGE, key, String.valueOf(start), String.valueOf(end), RedisKeyword.WITHSCORES.str);
    }

    public void zrevrangeByScore(final String key, final String max, final String min) {
        conn.sendCommand(RedisCommand.ZREVRANGEBYSCORE, key, max, min);
    }

    public void zrevrangeByScore(final String key, final String max, final String min, final int offset, int count) {
        conn.sendCommand(RedisCommand.ZREVRANGEBYSCORE, key, max, min, RedisKeyword.LIMIT.str, String.valueOf(offset), String.valueOf(count));
    }

    public void zrevrangeByScoreWithScores(final String key, final String max, final String min) {
        conn.sendCommand(RedisCommand.ZREVRANGEBYSCORE, key, max, min, RedisKeyword.WITHSCORES.str);
    }

    public void zrevrangeByScoreWithScores(final String key, final String max, final String min,
                                           final int offset, final int count) {
        conn.sendCommand(RedisCommand.ZREVRANGEBYSCORE, key, max, min, RedisKeyword.LIMIT.str, String.valueOf(offset), String.valueOf(count), RedisKeyword.WITHSCORES.str);
    }

    public void zcard(final String key) {
        conn.sendCommand(RedisCommand.ZCARD, key);
    }

    public void zscore(final String key, String member) {
        conn.sendCommand(RedisCommand.ZSCORE, key, member);
    }

    public void multi() {
        conn.sendCommand(RedisCommand.MULTI);
        isInMulti = true;
    }

    public void discard() {
        conn.sendCommand(RedisCommand.DISCARD);
        isInMulti = false;
    }

    public void exec() {
        conn.sendCommand(RedisCommand.EXEC);
        isInMulti = false;
    }

    public void watch(final String... keys) {
        conn.sendCommand(RedisCommand.WATCH, keys);
    }

    public void unwatch() {
        conn.sendCommand(RedisCommand.UNWATCH);
    }

    public void sort(final String key) {
        conn.sendCommand(RedisCommand.SORT, key);
    }

    public void sort(final String key, final SortingParams sortingParameters) {
        final List<String> args = new ArrayList<String>();
        args.add(key);
        args.addAll(sortingParameters.getParams());
        String[] args1 = args.toArray(new String[args.size()]);
        conn.sendCommand(RedisCommand.SORT, args1);
    }

    public void blpop(String... args) {
        conn.sendCommand(RedisCommand.BLPOP, args);
    }

    public void sort(final String key, final SortingParams sortingParameters,
                     final String dstkey) {
        final List<String> args = new ArrayList<String>();
        args.add(key);
        args.addAll(sortingParameters.getParams());
        args.add(RedisKeyword.STORE.str);
        args.add(dstkey);
        String[] args1 = args.toArray(new String[args.size()]);
        conn.sendCommand(RedisCommand.SORT, args1);
    }

    public void sort(final String key, final String dstkey) {
        conn.sendCommand(RedisCommand.SORT, key, RedisKeyword.STORE.str, dstkey);
    }

    public void brpop(String... keys) {
        conn.sendCommand(RedisCommand.BRPOP, keys);
    }

    public void auth(final String password) {
        conn.sendCommand(RedisCommand.AUTH, password);
    }

    public void subscribe(final String... channels) {
        conn.sendCommand(RedisCommand.SUBSCRIBE, channels);
    }

    public void publish(final String channel, final String message) {
        conn.sendCommand(RedisCommand.PUBLISH, channel, message);
    }

    public void unsubscribe() {
        conn.sendCommand(RedisCommand.UNSUBSCRIBE);
    }

    public void unsubscribe(final String... channels) {
        conn.sendCommand(RedisCommand.UNSUBSCRIBE, channels);
    }

    public void psubscribe(final String... pattern) {
        conn.sendCommand(RedisCommand.PSUBSCRIBE, pattern);
    }

    public void punsubscribe() {
        conn.sendCommand(RedisCommand.PUNSUBSCRIBE);
    }

    public void punsubscribe(final String... patterns) {
        conn.sendCommand(RedisCommand.PUNSUBSCRIBE, patterns);
    }

    public void zcount(final String key, final Number min, final Number max) {
        conn.sendCommand(RedisCommand.ZCOUNT, key, String.valueOf(min), String.valueOf(max));
    }

    public void zrangeByScore(final String key, final String min, final String max) {
        conn.sendCommand(RedisCommand.ZRANGEBYSCORE, key, min, max);
    }

    public void zrangeByScore(final String key, final String min, final String max, final int offset, int count) {
        conn.sendCommand(RedisCommand.ZRANGEBYSCORE, key, min, max, RedisKeyword.LIMIT.str, String.valueOf(offset), String.valueOf(count));
    }

    public void zrangeByScoreWithScores(final String key, final String min, final String max) {
        conn.sendCommand(RedisCommand.ZRANGEBYSCORE, key, min, max, RedisKeyword.WITHSCORES.str);
    }

    public void zrangeByScoreWithScores(final String key, final String min,
                                        final String max, final int offset, final int count) {
        conn.sendCommand(RedisCommand.ZRANGEBYSCORE, key, min, max, RedisKeyword.LIMIT.str, String.valueOf(offset), String.valueOf(count), RedisKeyword.WITHSCORES.str);
    }

    public void zremrangeByRank(final String key, final int start, final int end) {
        conn.sendCommand(RedisCommand.ZREMRANGEBYRANK, key, String.valueOf(start), String.valueOf(end));
    }

    public void zremrangeByScore(final String key, final String start, final String end) {
        conn.sendCommand(RedisCommand.ZREMRANGEBYSCORE, key, start, end);
    }

    public void zunionstore(final String dstkey, String... sets) {
        String[] params = new String[sets.length + 2];
        params[0] = dstkey;
        params[1] = String.valueOf(sets.length);
        System.arraycopy(sets, 0, params, 2, sets.length);
        conn.sendCommand(RedisCommand.ZUNIONSTORE, params);
    }

    public void zunionstore(final String dstkey, final ZParams params,
                            String... sets) {
        final List<String> args = new ArrayList<String>();
        args.add(dstkey);
        args.add(String.valueOf(sets.length));
        args.addAll(Arrays.asList(sets));
        args.addAll(params.getParams());
        String[] args1 = args.toArray(new String[args.size()]);
        conn.sendCommand(RedisCommand.ZUNIONSTORE, args1);
    }

    public void zinterstore(final String dstkey, String... sets) {
        String[] params = new String[sets.length + 2];
        params[0] = dstkey;
        params[1] = String.valueOf(sets.length);
        System.arraycopy(sets, 0, params, 2, sets.length);
        conn.sendCommand(RedisCommand.ZINTERSTORE, params);
    }

    public void zinterstore(final String dstkey, final ZParams params, String... sets) {
        final List<String> args = new ArrayList<String>();
        args.add(dstkey);
        args.add(String.valueOf(sets.length));
        args.addAll(Arrays.asList(sets));
        args.addAll(params.getParams());
        String[] args1 = args.toArray(new String[args.size()]);
        conn.sendCommand(RedisCommand.ZINTERSTORE, args1);
    }

    public void save() {
        conn.sendCommand(RedisCommand.SAVE);
    }

    public void bgsave() {
        conn.sendCommand(RedisCommand.BGSAVE);
    }

    public void bgrewriteaof() {
        conn.sendCommand(RedisCommand.BGREWRITEAOF);
    }

    public void lastsave() {
        conn.sendCommand(RedisCommand.LASTSAVE);
    }

    public void shutdown() {
        conn.sendCommand(RedisCommand.SHUTDOWN);
    }

    public void info() {
        conn.sendCommand(RedisCommand.INFO);
    }

    public void monitor() {
        conn.sendCommand(RedisCommand.MONITOR);
    }

    public void slaveof(final String host, final int port) {
        conn.sendCommand(RedisCommand.SLAVEOF, host, String.valueOf(port));
    }

    public void slaveofNoOne() {
        conn.sendCommand(RedisCommand.SLAVEOF, RedisKeyword.NO.str, RedisKeyword.ONE.str);
    }

    public void configGet(final String pattern) {
        conn.sendCommand(RedisCommand.CONFIG, RedisKeyword.GET.name(), pattern);
    }

    public void configSet(final String parameter, final String value) {
        conn.sendCommand(RedisCommand.CONFIG, RedisKeyword.SET.name(), parameter, value);
    }

    public void strlen(final String key) {
        conn.sendCommand(RedisCommand.STRLEN, key);
    }

    public void sync() {
        conn.sendCommand(RedisCommand.SYNC);
    }

    public void lpushx(final String key, String string) {
        conn.sendCommand(RedisCommand.LPUSHX, key, string);
    }

    public void persist(final String key) {
        conn.sendCommand(RedisCommand.PERSIST, key);
    }

    public void rpushx(final String key, String string) {
        conn.sendCommand(RedisCommand.RPUSHX, key, string);
    }

    public void echo(final String string) {
        conn.sendCommand(RedisCommand.ECHO, string);
    }

    public void linsert(final String key, final LIST_POSITION where,
                        String pivot, String value) {
        conn.sendCommand(RedisCommand.LINSERT, key, where.name(), pivot, value);
    }

    public void debug(final DebugParams params) {
        conn.sendCommand(RedisCommand.DEBUG, params.getCommand());
    }

    public void configResetStat() {
        conn.sendCommand(RedisCommand.CONFIG, RedisKeyword.RESETSTAT.name());
    }

    public void brpoplpush(String source, String destination, int timeout) {
        conn.sendCommand(RedisCommand.BRPOPLPUSH, source, destination, String.valueOf(timeout));
    }

    public void setbit(final String key, final int offset, final String value) {
        conn.sendCommand(RedisCommand.SETBIT, key, String.valueOf(offset), value);
    }

    public void getbit(String key, int offset) {
        conn.sendCommand(RedisCommand.GETBIT, key, String.valueOf(offset));
    }

    public String getStatusCodeReply() {
        return conn.getStatusCodeReply();
    }

    public String getBulkReply() {
        if (isInMulti()) {
            conn.getStatusCodeReply();
            return null;
        }
        return conn.getBulkReply();
    }

    public Long getIntegerReply() {
        if (isInMulti()) {
            conn.getStatusCodeReply();
            return null;
        }
        return conn.getIntegerReply();
    }

    public List<String> getMultiBulkReply() {
        if (isInMulti()) {
            conn.getStatusCodeReply();
            return Collections.emptyList();
        }
        return conn.getMultiBulkReply();
    }

    public List<Object> getObjectMultiBulkReply() {
        if (isInMulti()) {
            conn.getStatusCodeReply();
            return Collections.emptyList();
        }
        return conn.getObjectMultiBulkReply();
    }

    public List<Object> getAll() {
        return conn.getAll();
    }

    public Object getOne() {
        return conn.getOne();
    }

    public boolean isConnected() {
        return conn.isConnected();
    }

    public void setTimeoutInfinite() {
        conn.setTimeoutInfinite();
    }

    public void rollbackTimeout() {
        conn.rollbackTimeout();
    }
}