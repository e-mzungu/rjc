package io.redis.rjc;

import io.redis.rjc.ds.RedisConnection;
import io.redis.rjc.protocol.Protocol;
import io.redis.rjc.util.SafeEncoder;

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
        conn.sendCommand(Protocol.Command.PING);
    }

    public void set(final String key, final String value) {
        conn.sendCommand(Protocol.Command.SET, key, value);
    }

    public void get(final String key) {
        conn.sendCommand(Protocol.Command.GET, SafeEncoder.encode(key));
    }

    public void quit() {
        conn.sendCommand(Protocol.Command.QUIT);
    }

    public void exists(final String key) {
        conn.sendCommand(Protocol.Command.EXISTS, SafeEncoder.encode(key));
    }

    public void del(final String... keys) {
        conn.sendCommand(Protocol.Command.DEL, keys);
    }

    public void type(final String key) {
        conn.sendCommand(Protocol.Command.TYPE, SafeEncoder.encode(key));
    }

    public void flushDB() {
        conn.sendCommand(Protocol.Command.FLUSHDB);
    }

    public void keys(final String pattern) {
        conn.sendCommand(Protocol.Command.KEYS, pattern);
    }

    public void randomKey() {
        conn.sendCommand(Protocol.Command.RANDOMKEY);
    }

    public void rename(final String oldkey, final String newkey) {
        conn.sendCommand(Protocol.Command.RENAME, oldkey, newkey);
    }

    public void renamenx(final String oldkey, final String newkey) {
        conn.sendCommand(Protocol.Command.RENAMENX, oldkey, newkey);
    }

    public void dbSize() {
        conn.sendCommand(Protocol.Command.DBSIZE);
    }

    public void expire(final String key, final int seconds) {
        conn.sendCommand(Protocol.Command.EXPIRE, SafeEncoder.encode(key), Protocol.toByteArray(seconds));
    }

    public void expireAt(final String key, final long unixTime) {
        conn.sendCommand(Protocol.Command.EXPIREAT, SafeEncoder.encode(key), Protocol.toByteArray(unixTime));
    }

    public void ttl(final String key) {
        conn.sendCommand(Protocol.Command.TTL, SafeEncoder.encode(key));
    }

    public void select(final int index) {
        conn.sendCommand(Protocol.Command.SELECT, Protocol.toByteArray(index));
    }

    public void move(final String key, final int dbIndex) {
        conn.sendCommand(Protocol.Command.MOVE, SafeEncoder.encode(key), Protocol.toByteArray(dbIndex));
    }

    public void flushAll() {
        conn.sendCommand(Protocol.Command.FLUSHALL);
    }

    public void getSet(final String key, String value) {
        conn.sendCommand(Protocol.Command.GETSET, key, value);
    }

    public void mget(final String... keys) {
        conn.sendCommand(Protocol.Command.MGET, keys);
    }

    public void setnx(final String key, String value) {
        conn.sendCommand(Protocol.Command.SETNX, key, value);
    }

    public void setex(final String key, final int seconds, String value) {
        conn.sendCommand(Protocol.Command.SETEX, key, String.valueOf(seconds), value);
    }

    public void mset(String... keysvalues) {
        conn.sendCommand(Protocol.Command.MSET, keysvalues);
    }

    public void msetnx(String... keysvalues) {
        conn.sendCommand(Protocol.Command.MSETNX, keysvalues);
    }

    public void decrBy(final String key, final int integer) {
        conn.sendCommand(Protocol.Command.DECRBY, key, String.valueOf(integer));
    }

    public void decr(final String key) {
        conn.sendCommand(Protocol.Command.DECR, key);
    }

    public void incrBy(final String key, final int integer) {
        conn.sendCommand(Protocol.Command.INCRBY, key, String.valueOf(integer));
    }

    public void incr(final String key) {
        conn.sendCommand(Protocol.Command.INCR, key);
    }

    public void append(final String key, String value) {
        conn.sendCommand(Protocol.Command.APPEND, key, value);
    }

    public void substr(final String key, final int start, final int end) {
        conn.sendCommand(Protocol.Command.SUBSTR, key, String.valueOf(start), String.valueOf(end));
    }

    public void hset(final String key, String field, String value) {
        conn.sendCommand(Protocol.Command.HSET, key, field, value);
    }

    public void hget(final String key, String field) {
        conn.sendCommand(Protocol.Command.HGET, key, field);
    }

    public void hsetnx(final String key, String field, String value) {
        conn.sendCommand(Protocol.Command.HSETNX, key, field, value);
    }

    public void hmset(final String key, final Map<String, String> hash) {
        final List<String> params = new ArrayList<String>();
        params.add(key);

        for (String field : hash.keySet()) {
            params.add(field);
            params.add(hash.get(field));
        }
        String[] args = params.toArray(new String[params.size()]);
        conn.sendCommand(Protocol.Command.HMSET, args);
    }

    public void hmget(final String key, String... fields) {
        String[] params = new String[fields.length + 1];
        params[0] = key;
        System.arraycopy(fields, 0, params, 1, fields.length);
        conn.sendCommand(Protocol.Command.HMGET, params);
    }

    public void hincrBy(final String key, String field, final int value) {
        conn.sendCommand(Protocol.Command.HINCRBY, key, field, String.valueOf(value));
    }

    public void hexists(final String key, String field) {
        conn.sendCommand(Protocol.Command.HEXISTS, key, field);
    }

    public void hdel(final String key, String field) {
        conn.sendCommand(Protocol.Command.HDEL, key, field);
    }

    public void hlen(final String key) {
        conn.sendCommand(Protocol.Command.HLEN, key);
    }

    public void hkeys(final String key) {
        conn.sendCommand(Protocol.Command.HKEYS, key);
    }

    public void hvals(final String key) {
        conn.sendCommand(Protocol.Command.HVALS, key);
    }

    public void hgetAll(final String key) {
        conn.sendCommand(Protocol.Command.HGETALL, key);
    }

    public void rpush(final String key, String string) {
        conn.sendCommand(Protocol.Command.RPUSH, key, string);
    }

    public void lpush(final String key, String string) {
        conn.sendCommand(Protocol.Command.LPUSH, key, string);
    }

    public void llen(final String key) {
        conn.sendCommand(Protocol.Command.LLEN, key);
    }

    public void lrange(final String key, final int start, final int end) {
        conn.sendCommand(Protocol.Command.LRANGE, key, String.valueOf(start), String.valueOf(end));
    }

    public void ltrim(final String key, final int start, final int end) {
        conn.sendCommand(Protocol.Command.LTRIM, key, String.valueOf(start), String.valueOf(end));
    }

    public void lindex(final String key, final int index) {
        conn.sendCommand(Protocol.Command.LINDEX, key, String.valueOf(index));
    }

    public void lset(final String key, final int index, String value) {
        conn.sendCommand(Protocol.Command.LSET, key, String.valueOf(index), value);
    }

    public void lrem(final String key, int count, String value) {
        conn.sendCommand(Protocol.Command.LREM, key, String.valueOf(count), value);
    }

    public void lpop(final String key) {
        conn.sendCommand(Protocol.Command.LPOP, key);
    }

    public void rpop(final String key) {
        conn.sendCommand(Protocol.Command.RPOP, key);
    }

    public void rpoplpush(final String srckey, final String dstkey) {
        conn.sendCommand(Protocol.Command.RPOPLPUSH, srckey, dstkey);
    }

    public void sadd(final String key, String member) {
        conn.sendCommand(Protocol.Command.SADD, key, member);
    }

    public void smembers(final String key) {
        conn.sendCommand(Protocol.Command.SMEMBERS, key);
    }

    public void srem(final String key, String member) {
        conn.sendCommand(Protocol.Command.SREM, key, member);
    }

    public void spop(final String key) {
        conn.sendCommand(Protocol.Command.SPOP, key);
    }

    public void smove(final String srckey, final String dstkey,
                      String member) {
        conn.sendCommand(Protocol.Command.SMOVE, srckey, dstkey, member);
    }

    public void scard(final String key) {
        conn.sendCommand(Protocol.Command.SCARD, key);
    }

    public void sismember(final String key, String member) {
        conn.sendCommand(Protocol.Command.SISMEMBER, key, member);
    }

    public void sinter(final String... keys) {
        conn.sendCommand(Protocol.Command.SINTER, keys);
    }

    public void sinterstore(final String dstkey, final String... keys) {
        conn.sendCommand(Protocol.Command.SINTERSTORE, joinParams(dstkey, keys));
    }

    private static String[] joinParams(String dstkey, String[] keys) {
        final String[] params = new String[keys.length + 1];
        params[0] = dstkey;
        System.arraycopy(keys, 0, params, 1, keys.length);
        return params;
    }

    public void sunion(final String... keys) {
        conn.sendCommand(Protocol.Command.SUNION, keys);
    }

    public void sunionstore(final String dstkey, final String... keys) {
        conn.sendCommand(Protocol.Command.SUNIONSTORE, joinParams(dstkey, keys));
    }

    public void sdiff(final String... keys) {
        conn.sendCommand(Protocol.Command.SDIFF, keys);
    }

    public void sdiffstore(final String dstkey, final String... keys) {
        conn.sendCommand(Protocol.Command.SDIFFSTORE, joinParams(dstkey, keys));
    }

    public void srandmember(final String key) {
        conn.sendCommand(Protocol.Command.SRANDMEMBER, key);
    }

    public void zadd(final String key, final Number score, String member) {
        conn.sendCommand(Protocol.Command.ZADD, key, String.valueOf(score), member);
    }

    public void zrange(final String key, final int start, final int end) {
        conn.sendCommand(Protocol.Command.ZRANGE, key, String.valueOf(start), String.valueOf(end));
    }

    public void zrem(final String key, String member) {
        conn.sendCommand(Protocol.Command.ZREM, key, member);
    }

    public void zincrby(final String key, final Number score,
                        String member) {
        conn.sendCommand(Protocol.Command.ZINCRBY, key, String.valueOf(score), member);
    }

    public void zrank(final String key, String member) {
        conn.sendCommand(Protocol.Command.ZRANK, key, member);
    }

    public void zrevrank(final String key, String member) {
        conn.sendCommand(Protocol.Command.ZREVRANK, key, member);
    }

    public void zrevrange(final String key, final int start, final int end) {
        conn.sendCommand(Protocol.Command.ZREVRANGE, key, String.valueOf(start), String.valueOf(end));
    }

    public void zrangeWithScores(final String key, final int start,
                                 final int end) {
        conn.sendCommand(Protocol.Command.ZRANGE, key, String.valueOf(start), String.valueOf(end), Protocol.Keyword.WITHSCORES.str);
    }

    public void zrevrangeWithScores(final String key, final int start,
                                    final int end) {
        conn.sendCommand(Protocol.Command.ZREVRANGE, key, String.valueOf(start), String.valueOf(end), Protocol.Keyword.WITHSCORES.str);
    }

    public void zcard(final String key) {
        conn.sendCommand(Protocol.Command.ZCARD, key);
    }

    public void zscore(final String key, String member) {
        conn.sendCommand(Protocol.Command.ZSCORE, key, member);
    }

    public void multi() {
        conn.sendCommand(Protocol.Command.MULTI);
        isInMulti = true;
    }

    public void discard() {
        conn.sendCommand(Protocol.Command.DISCARD);
        isInMulti = false;
    }

    public void exec() {
        conn.sendCommand(Protocol.Command.EXEC);
        isInMulti = false;
    }

    public void watch(final String... keys) {
        conn.sendCommand(Protocol.Command.WATCH, keys);
    }

    public void unwatch() {
        conn.sendCommand(Protocol.Command.UNWATCH);
    }

    public void sort(final String key) {
        conn.sendCommand(Protocol.Command.SORT, key);
    }

    public void sort(final String key, final SortingParams sortingParameters) {
        final List<String> args = new ArrayList<String>();
        args.add(key);
        args.addAll(sortingParameters.getParams());
        String[] args1 = args.toArray(new String[args.size()]);
        conn.sendCommand(Protocol.Command.SORT, args1);
    }

    public void blpop(String... args) {
        conn.sendCommand(Protocol.Command.BLPOP, args);
    }

    public void sort(final String key, final SortingParams sortingParameters,
                     final String dstkey) {
        final List<String> args = new ArrayList<String>();
        args.add(key);
        args.addAll(sortingParameters.getParams());
        args.add(Protocol.Keyword.STORE.str);
        args.add(dstkey);
        String[] args1 = args.toArray(new String[args.size()]);
        conn.sendCommand(Protocol.Command.SORT, args1);
    }

    public void sort(final String key, final String dstkey) {
        conn.sendCommand(Protocol.Command.SORT, key, Protocol.Keyword.STORE.str, dstkey);
    }

    public void brpop(String... keys) {
        conn.sendCommand(Protocol.Command.BRPOP, keys);
    }

    public void auth(final String password) {
        conn.sendCommand(Protocol.Command.AUTH, password);
    }

    public void subscribe(final String... channels) {
        conn.sendCommand(Protocol.Command.SUBSCRIBE, channels);
    }

    public void publish(final String channel, final String message) {
        conn.sendCommand(Protocol.Command.PUBLISH, channel, message);
    }

    public void unsubscribe() {
        conn.sendCommand(Protocol.Command.UNSUBSCRIBE);
    }

    public void unsubscribe(final String... channels) {
        conn.sendCommand(Protocol.Command.UNSUBSCRIBE, channels);
    }

    public void psubscribe(final String pattern) {
        conn.sendCommand(Protocol.Command.PSUBSCRIBE, pattern);
    }

    public void punsubscribe() {
        conn.sendCommand(Protocol.Command.PUNSUBSCRIBE);
    }

    public void punsubscribe(final String... patterns) {
        conn.sendCommand(Protocol.Command.PUNSUBSCRIBE, patterns);
    }

    public void zcount(final String key, final Number min, final Number max) {
        conn.sendCommand(Protocol.Command.ZCOUNT, key, String.valueOf(min), String.valueOf(max));
    }

    public void zrangeByScore(final String key, final String min, final String max) {
        conn.sendCommand(Protocol.Command.ZRANGEBYSCORE, key, min, max);
    }

    public void zrangeByScore(final String key, final String min, final String max, final int offset, int count) {
        conn.sendCommand(Protocol.Command.ZRANGEBYSCORE, key, min, max, Protocol.Keyword.LIMIT.str, String.valueOf(offset), String.valueOf(count));
    }

    public void zrangeByScoreWithScores(final String key, final String min, final String max) {
        conn.sendCommand(Protocol.Command.ZRANGEBYSCORE, key, String.valueOf(min), String.valueOf(max), Protocol.Keyword.WITHSCORES.str);
    }

    public void zrangeByScoreWithScores(final String key, final String min,
                                        final String max, final int offset, final int count) {
        conn.sendCommand(Protocol.Command.ZRANGEBYSCORE, key, String.valueOf(min), String.valueOf(max), Protocol.Keyword.LIMIT.str, String.valueOf(offset), String.valueOf(count), Protocol.Keyword.WITHSCORES.str);
    }

    public void zremrangeByRank(final String key, final int start, final int end) {
        conn.sendCommand(Protocol.Command.ZREMRANGEBYRANK, key, String.valueOf(start), String.valueOf(end));
    }

    public void zremrangeByScore(final String key, final Number start,
                                 final Number end) {
        conn.sendCommand(Protocol.Command.ZREMRANGEBYSCORE, key, String.valueOf(start), String.valueOf(end));
    }

    public void zunionstore(final String dstkey, String... sets) {
        String[] params = new String[sets.length + 2];
        params[0] = dstkey;
        params[1] = String.valueOf(sets.length);
        System.arraycopy(sets, 0, params, 2, sets.length);
        conn.sendCommand(Protocol.Command.ZUNIONSTORE, params);
    }

    public void zunionstore(final String dstkey, final ZParams params,
                            String... sets) {
        final List<String> args = new ArrayList<String>();
        args.add(dstkey);
        args.add(String.valueOf(sets.length));
        args.addAll(Arrays.asList(sets));
        args.addAll(params.getParams());
        String[] args1 = args.toArray(new String[args.size()]);
        conn.sendCommand(Protocol.Command.ZUNIONSTORE, args1);
    }

    public void zinterstore(final String dstkey, String... sets) {
        String[] params = new String[sets.length + 2];
        params[0] = dstkey;
        params[1] = String.valueOf(sets.length);
        System.arraycopy(sets, 0, params, 2, sets.length);
        conn.sendCommand(Protocol.Command.ZINTERSTORE, params);
    }

    public void zinterstore(final String dstkey, final ZParams params, String... sets) {
        final List<String> args = new ArrayList<String>();
        args.add(dstkey);
        args.add(String.valueOf(sets.length));
        args.addAll(Arrays.asList(sets));
        args.addAll(params.getParams());
        String[] args1 = args.toArray(new String[args.size()]);
        conn.sendCommand(Protocol.Command.ZINTERSTORE, args1);
    }

    public void save() {
        conn.sendCommand(Protocol.Command.SAVE);
    }

    public void bgsave() {
        conn.sendCommand(Protocol.Command.BGSAVE);
    }

    public void bgrewriteaof() {
        conn.sendCommand(Protocol.Command.BGREWRITEAOF);
    }

    public void lastsave() {
        conn.sendCommand(Protocol.Command.LASTSAVE);
    }

    public void shutdown() {
        conn.sendCommand(Protocol.Command.SHUTDOWN);
    }

    public void info() {
        conn.sendCommand(Protocol.Command.INFO);
    }

    public void monitor() {
        conn.sendCommand(Protocol.Command.MONITOR);
    }

    public void slaveof(final String host, final int port) {
        conn.sendCommand(Protocol.Command.SLAVEOF, host, String.valueOf(port));
    }

    public void slaveofNoOne() {
        conn.sendCommand(Protocol.Command.SLAVEOF, Protocol.Keyword.NO.str, Protocol.Keyword.ONE.str);
    }

    public void configGet(final String pattern) {
        conn.sendCommand(Protocol.Command.CONFIG, Protocol.Keyword.GET.name(), pattern);
    }

    public void configSet(final String parameter, final String value) {
        conn.sendCommand(Protocol.Command.CONFIG, Protocol.Keyword.SET.name(), parameter, value);
    }

    public void strlen(final String key) {
        conn.sendCommand(Protocol.Command.STRLEN, key);
    }

    public void sync() {
        conn.sendCommand(Protocol.Command.SYNC);
    }

    public void lpushx(final String key, String string) {
        conn.sendCommand(Protocol.Command.LPUSHX, key, string);
    }

    public void persist(final String key) {
        conn.sendCommand(Protocol.Command.PERSIST, key);
    }

    public void rpushx(final String key, String string) {
        conn.sendCommand(Protocol.Command.RPUSHX, key, string);
    }

    public void echo(final String string) {
        conn.sendCommand(Protocol.Command.ECHO, string);
    }

    public void linsert(final String key, final LIST_POSITION where,
                        String pivot, String value) {
        conn.sendCommand(Protocol.Command.LINSERT, key, where.name(), pivot, value);
    }

    public void debug(final DebugParams params) {
        conn.sendCommand(Protocol.Command.DEBUG, params.getCommand());
    }

    public void configResetStat() {
        conn.sendCommand(Protocol.Command.CONFIG, Protocol.Keyword.RESETSTAT.name());
    }

    public void brpoplpush(String source, String destination, int timeout) {
       conn.sendCommand(Protocol.Command.BRPOPLPUSH, source, destination, String.valueOf(timeout));
    }

    public void setbit(final String key, final int offset, final String value) {
        conn.sendCommand(Protocol.Command.SETBIT, key, String.valueOf(offset), value);
    }

    public void getbit(String key, int offset) {
        conn.sendCommand(Protocol.Command.GETBIT, key, String.valueOf(offset));
    }

    public String getStatusCodeReply() {
        return conn.getStatusCodeReply();
    }

    public String getBulkReply() {
        if(isInMulti()) {
            conn.getStatusCodeReply();
            return null;
        }
        return conn.getBulkReply();
    }

    public byte[] getBinaryBulkReply() {
        if(isInMulti()) {
            conn.getStatusCodeReply();
            return null;
        }
        return conn.getBinaryBulkReply();
    }

    public Long getIntegerReply() {
        if(isInMulti()) {
            conn.getStatusCodeReply();
            return null;
        }
        return conn.getIntegerReply();
    }

    public List<String> getMultiBulkReply() {
        if(isInMulti()) {
            conn.getStatusCodeReply();
            return Collections.emptyList();
        }
        return conn.getMultiBulkReply();
    }

    public List<byte[]> getBinaryMultiBulkReply() {
        if(isInMulti()) {
            conn.getStatusCodeReply();
            return Collections.emptyList();
        }
        return conn.getBinaryMultiBulkReply();
    }

    public List<Object> getObjectMultiBulkReply() {
        if(isInMulti()) {
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