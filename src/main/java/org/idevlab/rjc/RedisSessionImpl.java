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

import java.util.*;

/**
 * @author Evgeny Dolgov
 */
class RedisSessionImpl implements Session {
    private Client client;

    public RedisSessionImpl(RedisConnection connection) {
        this.client = new Client(connection);
    }

    public String set(final String key, String value) {
        client.set(key, value);
        return client.getStatusCodeReply();
    }

    public String get(final String key) {
        client.get(key);
        return client.getBulkReply();
    }

    public void quit() {
        client.quit();
    }


    public boolean exists(final String key) {
        client.exists(key);
        return client.getIntegerReply() == 1;
    }

    public Long del(final String... keys) {
        client.del(keys);
        return client.getIntegerReply();
    }

    public String type(final String key) {
        client.type(key);
        return client.getStatusCodeReply();
    }

    public String flushDB() {
        client.flushDB();
        return client.getStatusCodeReply();
    }

    public Set<String> keys(final String pattern) {
        client.keys(pattern);
        return new HashSet<String>(client.getMultiBulkReply());
    }

    public String randomKey() {
        client.randomKey();
        return client.getBulkReply();
    }

    public String rename(final String key, final String newKey) {
        client.rename(key, newKey);
        return client.getStatusCodeReply();
    }

    public boolean renamenx(final String key, final String newKey) {
        client.renamenx(key, newKey);
        return client.getIntegerReply() == 1;
    }

    public Long dbSize() {
        client.dbSize();
        return client.getIntegerReply();
    }

    public boolean expire(final String key, final int seconds) {
        client.expire(key, seconds);
        return client.getIntegerReply() == 1;
    }

    public boolean expireAt(final String key, final long unixTime) {
        client.expireAt(key, unixTime);
        return client.getIntegerReply() == 1;
    }

    public Long ttl(final String key) {

        client.ttl(key);
        return client.getIntegerReply();
    }

    public String select(final int index) {
        client.select(index);
        return client.getStatusCodeReply();
    }


    public Boolean move(final String key, final int dbIndex) {
        client.move(key, dbIndex);
        return client.getIntegerReply() == 1;
    }

    public String flushAll() {
        client.flushAll();
        return client.getStatusCodeReply();
    }

    public String getSet(final String key, final String value) {
        client.getSet(key, value);
        return client.getBulkReply();
    }

    public List<String> mget(final String... keys) {
        client.mget(keys);
        return client.getMultiBulkReply();
    }

    public boolean setnx(final String key, final String value) {
        client.setnx(key, value);
        return client.getIntegerReply() == 1;
    }

    public String setex(final String key, final int seconds, final String value) {
        client.setex(key, seconds, value);
        return client.getStatusCodeReply();
    }

    public String mset(final String... keysvalues) {
        client.mset(keysvalues);
        return client.getStatusCodeReply();
    }

    public boolean msetnx(final String... keysvalues) {
        client.msetnx(keysvalues);
        return client.getIntegerReply() == 1;
    }

    public Long decrBy(final String key, final int value) {
        client.decrBy(key, value);
        return client.getIntegerReply();
    }

    public Long decr(final String key) {
        client.decr(key);
        return client.getIntegerReply();
    }

    public Long incrBy(final String key, final int value) {
        client.incrBy(key, value);
        return client.getIntegerReply();
    }

    public Long incr(final String key) {
        client.incr(key);
        return client.getIntegerReply();
    }

    public Long append(final String key, final String value) {
        client.append(key, value);
        return client.getIntegerReply();
    }

    public String getRange(final String key, final int start, final int end) {
        client.getRange(key, start, end);
        return client.getBulkReply();
    }

    public Long setRange(String key, int offset, String value) {
        client.setRange(key, offset, value);
        return client.getIntegerReply();
    }

    public boolean hset(final String key, final String field, final String value) {
        client.hset(key, field, value);
        return client.getIntegerReply() == 1;
    }

    public String hget(final String key, final String field) {
        client.hget(key, field);
        return client.getBulkReply();
    }

    public boolean hsetnx(final String key, final String field, final String value) {
        client.hsetnx(key, field, value);
        return client.getIntegerReply() == 1;
    }

    public String hmset(final String key, final Map<String, String> hash) {
        client.hmset(key, hash);
        return client.getStatusCodeReply();
    }

    public List<String> hmget(final String key, final String... fields) {
        client.hmget(key, fields);
        return client.getMultiBulkReply();
    }

    public Long hincrBy(final String key, final String field, final int value) {
        client.hincrBy(key, field, value);
        return client.getIntegerReply();
    }

    public boolean hexists(final String key, final String field) {
        client.hexists(key, field);
        return client.getIntegerReply() == 1;
    }

    public boolean hdel(final String key, final String field) {
        client.hdel(key, field);
        return client.getIntegerReply() == 1;
    }

    public Long hlen(final String key) {
        client.hlen(key);
        return client.getIntegerReply();
    }

    public Set<String> hkeys(final String key) {
        client.hkeys(key);
        return new HashSet<String>(client.getMultiBulkReply());
    }

    public List<String> hvals(final String key) {
        client.hvals(key);
        return client.getMultiBulkReply();
    }

    public Map<String, String> hgetAll(final String key) {
        client.hgetAll(key);
        final List<String> flatHash = client.getMultiBulkReply();
        final Map<String, String> hash = new HashMap<String, String>();
        final Iterator<String> iterator = flatHash.iterator();
        while (iterator.hasNext()) {
            hash.put(iterator.next(), iterator.next());
        }

        return hash;
    }

    public Long rpush(final String key, final String value) {
        client.rpush(key, value);
        return client.getIntegerReply();
    }

    public Long lpush(final String key, final String value) {
        client.lpush(key, value);
        return client.getIntegerReply();
    }

    public Long llen(final String key) {
        client.llen(key);
        return client.getIntegerReply();
    }

    public List<String> lrange(final String key, final int start, final int end) {
        client.lrange(key, start, end);
        return client.getMultiBulkReply();
    }

    public String ltrim(final String key, final int start, final int end) {
        client.ltrim(key, start, end);
        return client.getStatusCodeReply();
    }

    public String lindex(final String key, final int index) {
        client.lindex(key, index);
        return client.getBulkReply();
    }

    public String lset(final String key, final int index, final String value) {
        client.lset(key, index, value);
        return client.getStatusCodeReply();
    }

    public Long lrem(final String key, final int count, final String value) {
        client.lrem(key, count, value);
        return client.getIntegerReply();
    }

    public String lpop(final String key) {
        client.lpop(key);
        return client.getBulkReply();
    }

    public String rpop(final String key) {
        client.rpop(key);
        return client.getBulkReply();
    }

    public String rpoplpush(final String srckey, final String dstkey) {
        client.rpoplpush(srckey, dstkey);
        return client.getBulkReply();
    }

    public Long sadd(final String key, final String member) {
        client.sadd(key, member);
        return client.getIntegerReply();
    }

    public Set<String> smembers(final String key) {
        client.smembers(key);
        return new LinkedHashSet<String>(client.getMultiBulkReply());
    }

    public Long srem(final String key, final String member) {
        client.srem(key, member);
        return client.getIntegerReply();
    }

    public String spop(final String key) {
        client.spop(key);
        return client.getBulkReply();
    }

    public Long smove(final String srckey, final String dstkey, final String member) {
        client.smove(srckey, dstkey, member);
        return client.getIntegerReply();
    }

    public Long scard(final String key) {
        client.scard(key);
        return client.getIntegerReply();
    }

    public Boolean sismember(final String key, final String member) {
        client.sismember(key, member);
        return client.getIntegerReply() == 1;
    }

    public Set<String> sinter(final String... keys) {
        client.sinter(keys);
        final List<String> members = client.getMultiBulkReply();
        return new LinkedHashSet<String>(members);
    }

    public Long sinterstore(final String dstkey, final String... keys) {
        client.sinterstore(dstkey, keys);
        return client.getIntegerReply();
    }

    public Set<String> sunion(final String... keys) {
        client.sunion(keys);
        return new LinkedHashSet<String>(client.getMultiBulkReply());
    }

    public Long sunionstore(final String dstkey, final String... keys) {
        client.sunionstore(dstkey, keys);
        return client.getIntegerReply();
    }

    public Set<String> sdiff(final String... keys) {
        client.sdiff(keys);
        return new LinkedHashSet<String>(client.getMultiBulkReply());
    }

    public Long sdiffstore(final String dstkey, final String... keys) {
        client.sdiffstore(dstkey, keys);
        return client.getIntegerReply();
    }

    public String srandmember(final String key) {
        client.srandmember(key);
        return client.getBulkReply();
    }

    public Long zadd(final String key, final Number score, final String member) {
        client.zadd(key, score, member);
        return client.getIntegerReply();
    }

    public Set<String> zrange(final String key, final int start, final int end) {
        client.zrange(key, start, end);
        return new LinkedHashSet<String>(client.getMultiBulkReply());
    }

    public Long zrem(final String key, final String member) {
        client.zrem(key, member);
        return client.getIntegerReply();
    }

    public Long zrank(final String key, final String member) {

        client.zrank(key, member);
        return client.getIntegerReply();
    }

    public Long zrevrank(final String key, final String member) {

        client.zrevrank(key, member);
        return client.getIntegerReply();
    }

    public Set<String> zrevrange(final String key, final int start,
                                 final int end) {

        client.zrevrange(key, start, end);
        final List<String> members = client.getMultiBulkReply();
        return new LinkedHashSet<String>(members);
    }

    public Map<String, String> zrangeWithScores(final String key, final int start,
                                                final int end) {
        client.zrangeWithScores(key, start, end);
        return getReplyAsMap(client);
    }

    public Map<String, String> zrevrangeWithScores(final String key, final int start,
                                                   final int end) {
        client.zrevrangeWithScores(key, start, end);
        return getReplyAsMap(client);
    }

    public Long zcard(final String key) {
        client.zcard(key);
        return client.getIntegerReply();
    }

    public String zscore(final String key, final String member) {
        client.zscore(key, member);
        return client.getBulkReply();
    }

    public String watch(final String... keys) {
        client.watch(keys);
        return client.getStatusCodeReply();
    }

    public String unwatch() {
        client.unwatch();
        return client.getStatusCodeReply();
    }

    public List<Object> exec() {
        client.exec();
        return client.getObjectMultiBulkReply();
    }

    public void close() {
        client.close();
    }

    public List<String> sort(final String key) {
        client.sort(key);
        return client.getMultiBulkReply();
    }

    public List<String> sort(final String key, final SortingParams sortingParameters) {
        client.sort(key, sortingParameters);
        return client.getMultiBulkReply();
    }

    public List<String> blpop(final int timeout, final String... keys) {
        List<String> args = new ArrayList<String>();
        args.addAll(Arrays.asList(keys));
        args.add(String.valueOf(timeout));

        client.blpop(args.toArray(new String[args.size()]));
        client.setTimeoutInfinite();
        final List<String> multiBulkReply = client.getMultiBulkReply();
        client.rollbackTimeout();
        return multiBulkReply;
    }

    public Long sort(final String key, final SortingParams sortingParameters, final String dstkey) {
        client.sort(key, sortingParameters, dstkey);
        return client.getIntegerReply();
    }

    public Long sort(final String key, final String dstkey) {
        client.sort(key, dstkey);
        return client.getIntegerReply();
    }

    public List<String> brpop(final int timeout, final String... keys) {
        List<String> args = new ArrayList<String>();
        args.addAll(Arrays.asList(keys));
        args.add(String.valueOf(timeout));

        client.brpop(args.toArray(new String[args.size()]));
        client.setTimeoutInfinite();
        List<String> multiBulkReply = client.getMultiBulkReply();
        client.rollbackTimeout();

        return multiBulkReply;
    }

    public String auth(final String password) {
        client.auth(password);
        return client.getStatusCodeReply();
    }

    public Long publish(String channel, String message) {
        client.publish(channel, message);
        return client.getIntegerReply();
    }

    public Long zcount(final String key, final Number min, final Number max) {
        client.zcount(key, min, max);
        return client.getIntegerReply();
    }

    public Set<String> zrangeByScore(final String key, final String min, final String max) {
        client.zrangeByScore(key, min, max);
        return new LinkedHashSet<String>(client.getMultiBulkReply());
    }

    public Set<String> zrangeByScore(final String key, final String min,
                                     final String max, final int offset, final int count) {
        client.zrangeByScore(key, min, max, offset, count);
        return new LinkedHashSet<String>(client.getMultiBulkReply());
    }

    public Map<String, String> zrangeByScoreWithScores(final String key, final String min, final String max) {
        client.zrangeByScoreWithScores(key, min, max);
        return getReplyAsMap(client);
    }

    public Map<String, String> zrangeByScoreWithScores(final String key,
                                                       final String min, final String max, final int offset,
                                                       final int count) {
        client.zrangeByScoreWithScores(key, min, max, offset, count);
        return getReplyAsMap(client);
    }

    private Map<String, String> getReplyAsMap(Client client) {
        List<String> membersWithScores = client.getMultiBulkReply();
        Map<String, String> result = new HashMap<String, String>();
        Iterator<String> iterator = membersWithScores.iterator();
        while (iterator.hasNext()) {
            result.put(iterator.next(), iterator.next());
        }
        return result;
    }

    public Long zremrangeByRank(final String key, final int start, final int end) {
        client.zremrangeByRank(key, start, end);
        return client.getIntegerReply();
    }

    public Long zunionstore(final String dstkey, final String... sets) {
        client.zunionstore(dstkey, sets);
        return client.getIntegerReply();
    }

    public Long zunionstore(final String dstkey, final ZParams params, final String... sets) {
        client.zunionstore(dstkey, params, sets);
        return client.getIntegerReply();
    }

    public Long zinterstore(final String dstkey, final String... sets) {
        client.zinterstore(dstkey, sets);
        return client.getIntegerReply();
    }

    public Long zinterstore(final String dstkey, final ZParams params, final String... sets) {
        client.zinterstore(dstkey, params, sets);
        return client.getIntegerReply();
    }

    public Long strlen(final String key) {
        client.strlen(key);
        return client.getIntegerReply();
    }

    public Long lpushx(final String key, final String value) {
        client.lpushx(key, value);
        return client.getIntegerReply();
    }

    public boolean persist(final String key) {
        client.persist(key);
        return client.getIntegerReply() == 1;
    }

    public Long rpushx(final String key, final String value) {
        client.rpushx(key, value);
        return client.getIntegerReply();
    }

    public String echo(final String string) {
        client.echo(string);
        return client.getBulkReply();
    }

    public Long linsert(final String key, final Client.LIST_POSITION where,
                        final String pivot, final String value) {
        client.linsert(key, where, pivot, value);
        return client.getIntegerReply();
    }

    public String brpoplpush(String source, String destination, int timeout) {
        client.brpoplpush(source, destination, timeout);
        return client.getBulkReply();
    }

    public Long setBit(String key, int offset, String value) {
        client.setbit(key, offset, value);
        return client.getIntegerReply();
    }

    public Long getBit(String key, int offset) {
        client.getbit(key, offset);
        return client.getIntegerReply();
    }

    public String ping() {
        client.ping();
        return client.getStatusCodeReply();
    }

    public String zincrby(final String key, final Number score, final String member) {
        client.zincrby(key, score, member);
        return client.getBulkReply();
    }


    public String multi() {
        client.multi();
        return client.getStatusCodeReply();
    }

    public String discard() {
        client.discard();
        return client.getStatusCodeReply();
    }

    public List<Object> pipeline(final Pipeline pipeline) {
        pipeline.execute(client);
        return client.getAll();
    }

    public Long zremrangeByScore(final String key, final Number start, final Number end) {
        client.zremrangeByScore(key, start, end);
        return client.getIntegerReply();
    }

    public String save() {
        client.save();
        return client.getStatusCodeReply();
    }

    public String bgsave() {
        client.bgsave();
        return client.getStatusCodeReply();
    }

    public String bgrewriteaof() {
        client.bgrewriteaof();
        return client.getStatusCodeReply();
    }

    public Long lastsave() {
        client.lastsave();
        return client.getIntegerReply();
    }

    public String shutdown() {
        client.shutdown();
        String status;
        try {
            status = client.getStatusCodeReply();
        } catch (RedisException ex) {
            status = null;
        }
        return status;
    }

    public String info() {
        client.info();
        return client.getBulkReply();
    }

    public void monitor(final RedisMonitor redisMonitor) {
        client.monitor();
        do {
            String command = client.getBulkReply();
            redisMonitor.onCommand(command);
        } while (client.isConnected());
    }

    public String slaveof(final String host, final int port) {
        client.slaveof(host, port);
        return client.getStatusCodeReply();
    }

    public String slaveofNoOne() {
        client.slaveofNoOne();
        return client.getStatusCodeReply();
    }

    public List<String> configGet(final String pattern) {
        client.configGet(pattern);
        return client.getMultiBulkReply();
    }

    public String configSet(final String parameter, final String value) {
        client.configSet(parameter, value);
        return client.getStatusCodeReply();
    }

    public void sync() {

        client.sync();
    }

    public String debug(final DebugParams params) {
        client.debug(params);
        return client.getStatusCodeReply();
    }
}
