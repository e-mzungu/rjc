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

import org.idevlab.rjc.Client.LIST_POSITION;
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

    public boolean exists(final String key) {
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

    public boolean renamenx(final String key, final String newKey) {
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

    public boolean expire(final String key, final int seconds) {
        return execute(new RedisCallback<Boolean>() {
            public Boolean doIt(Session session) {
                return session.expire(key, seconds);
            }
        });
    }

    public boolean expireAt(final String key, final long unixTime) {
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

    public boolean setnx(final String key, final String value) {
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

    public boolean msetnx(final String... keysvalues) {
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

    public Long hset(final String key, final String field, final String value) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
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

    public Long hsetnx(final String key, final String field, final String value) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
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

    public Long hdel(final String key, final String field) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
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

    public Collection<String> hvals(final String key) {
        return execute(new RedisCallback<Collection<String>>() {
            public Collection<String> doIt(Session session) {
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

    public Long rpush(final String key, final String string) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.rpush(key, string);

            }
        });
    }

    public Long lpush(final String key, final String string) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.lpush(key, string);

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

    public Long sadd(final String key, final String member) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
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

    /**
     * Remove the specified member from the set value stored at key. If member
     * was not a member of the set no operation is performed. If key does not
     * hold a set value an error is returned.
     * <p/>
     * Time complexity O(1)
     *
     * @param key    the key
     * @param member
     * @return Integer reply, specifically: 1 if the new element was removed 0
     *         if the new element was not a member of the set
     */
    public Long srem(final String key, final String member) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.srem(key, member);

            }
        });
    }

    /**
     * Remove a random element from a Set returning it as return value. If the
     * Set is empty or the key does not exist, a nil object is returned.
     * <p/>
     * The {@link #srandmember(String)} command does a similar work but the
     * returned element is not removed from the Set.
     * <p/>
     * Time complexity O(1)
     *
     * @param key the key
     * @return Bulk reply
     */
    public String spop(final String key) {
        return execute(new RedisCallback<String>() {
            public String doIt(Session session) {
                return session.spop(key);

            }
        });
    }

    /**
     * Move the specifided member from the set at srckey to the set at dstkey.
     * This operation is atomic, in every given moment the element will appear
     * to be in the source or destination set for accessing clients.
     * <p/>
     * If the source set does not exist or does not contain the specified
     * element no operation is performed and zero is returned, otherwise the
     * element is removed from the source set and added to the destination set.
     * On success one is returned, even if the element was already present in
     * the destination set.
     * <p/>
     * An error is raised if the source or destination keys contain a non Set
     * value.
     * <p/>
     * Time complexity O(1)
     *
     * @param srckey
     * @param dstkey
     * @param member
     * @return Integer reply, specifically: 1 if the element was moved 0 if the
     *         element was not found on the first set and no operation was
     *         performed
     */
    public Long smove(final String srckey, final String dstkey, final String member) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.smove(srckey, dstkey, member);

            }
        });
    }

    /**
     * Return the set cardinality (number of elements). If the key does not
     * exist 0 is returned, like for empty sets.
     *
     * @param key the key
     * @return Integer reply, specifically: the cardinality (number of elements)
     *         of the set as an integer.
     */
    public Long scard(final String key) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.scard(key);

            }
        });
    }

    /**
     * Return 1 if member is a member of the set stored at key, otherwise 0 is
     * returned.
     * <p/>
     * Time complexity O(1)
     *
     * @param key    the key
     * @param member
     * @return Integer reply, specifically: 1 if the element is a member of the
     *         set 0 if the element is not a member of the set OR if the key
     *         does not exist
     */
    public Boolean sismember(final String key, final String member) {
        return execute(new RedisCallback<Boolean>() {
            public Boolean doIt(Session session) {
                return session.sismember(key, member);
            }
        });
    }

    /**
     * Return the members of a set resulting from the intersection of all the
     * sets hold at the specified keys. Like in
     * {@link #lrange(String, int, int) LRANGE} the result is sent to the client
     * as a multi-bulk reply (see the protocol specification for more
     * information). If just a single key is specified, then this command
     * produces the same result as {@link #smembers(String) SMEMBERS}. Actually
     * SMEMBERS is just syntax sugar for SINTER.
     * <p/>
     * Non existing keys are considered like empty sets, so if one of the keys
     * is missing an empty set is returned (since the intersection with an empty
     * set always is an empty set).
     * <p/>
     * Time complexity O(N*M) worst case where N is the cardinality of the
     * smallest set and M the number of sets
     *
     * @param keys the keys
     * @return Multi bulk reply, specifically the list of common elements.
     */
    public Set<String> sinter(final String... keys) {
        return execute(new RedisCallback<Set<String>>() {
            public Set<String> doIt(Session session) {
                return session.sinter(keys);
            }
        });
    }

    /**
     * This commnad works exactly like {@link #sinter(String...) SINTER} but
     * instead of being returned the resulting set is sotred as dstkey.
     * <p/>
     * Time complexity O(N*M) worst case where N is the cardinality of the
     * smallest set and M the number of sets
     *
     * @param dstkey
     * @param keys   the keys
     * @return Status code reply
     */
    public Long sinterstore(final String dstkey, final String... keys) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.sinterstore(dstkey, keys);

            }
        });
    }

    /**
     * Return the members of a set resulting from the union of all the sets hold
     * at the specified keys. Like in {@link #lrange(String, int, int) LRANGE}
     * the result is sent to the client as a multi-bulk reply (see the protocol
     * specification for more information). If just a single key is specified,
     * then this command produces the same result as {@link #smembers(String)
     * SMEMBERS}.
     * <p/>
     * Non existing keys are considered like empty sets.
     * <p/>
     * Time complexity O(N) where N is the total number of elements in all the
     * provided sets
     *
     * @param keys the keys
     * @return Multi bulk reply, specifically the list of common elements.
     */
    public Set<String> sunion(final String... keys) {
        return execute(new RedisCallback<Set<String>>() {
            public Set<String> doIt(Session session) {
                return session.sunion(keys);
            }
        });
    }

    /**
     * This command works exactly like {@link #sunion(String...) SUNION} but
     * instead of being returned the resulting set is stored as dstkey. Any
     * existing value in dstkey will be over-written.
     * <p/>
     * Time complexity O(N) where N is the total number of elements in all the
     * provided sets
     *
     * @param dstkey
     * @param keys   the keys
     * @return Status code reply
     */
    public Long sunionstore(final String dstkey, final String... keys) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.sunionstore(dstkey, keys);

            }
        });
    }

    /**
     * Return the difference between the Set stored at key1 and all the Sets
     * key2, ..., keyN
     * <p/>
     * <b>Example:</b>
     * <p/>
     * <pre>
     * key1 = [x, a, b, c]
     * key2 = [c]
     * key3 = [a, d]
     * SDIFF key1,key2,key3 => [x, b]
     * </pre>
     * <p/>
     * Non existing keys are considered like empty sets.
     * <p/>
     * <b>Time complexity:</b>
     * <p/>
     * O(N) with N being the total number of elements of all the sets
     *
     * @param keys the keys
     * @return Return the members of a set resulting from the difference between
     *         the first set provided and all the successive sets.
     */
    public Set<String> sdiff(final String... keys) {
        return execute(new RedisCallback<Set<String>>() {
            public Set<String> doIt(Session session) {
                return session.sdiff(keys);
            }
        });
    }

    /**
     * This command works exactly like {@link #sdiff(String...) SDIFF} but
     * instead of being returned the resulting set is stored in dstkey.
     *
     * @param dstkey
     * @param keys   the keys
     * @return Status code reply
     */
    public Long sdiffstore(final String dstkey, final String... keys) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.sdiffstore(dstkey, keys);

            }
        });
    }

    /**
     * Return a random element from a Set, without removing the element. If the
     * Set is empty or the key does not exist, a nil object is returned.
     * <p/>
     * The SPOP command does a similar work but the returned element is popped
     * (removed) from the Set.
     * <p/>
     * Time complexity O(1)
     *
     * @param key the key
     * @return Bulk reply
     */
    public String srandmember(final String key) {
        return execute(new RedisCallback<String>() {
            public String doIt(Session session) {
                return session.srandmember(key);

            }
        });
    }

    /**
     * Add the specified member having the specifeid score to the sorted set
     * stored at key. If member is already a member of the sorted set the score
     * is updated, and the element reinserted in the right position to ensure
     * sorting. If key does not exist a new sorted set with the specified member
     * as sole member is crated. If the key exists but does not hold a sorted
     * set value an error is returned.
     * <p/>
     * The score value can be the string representation of a double precision
     * floating point number.
     * <p/>
     * Time complexity O(log(N)) with N being the number of elements in the
     * sorted set
     *
     * @param key    the key
     * @param score
     * @param member
     * @return Integer reply, specifically: 1 if the new element was added 0 if
     *         the element was already a member of the sorted set and the score
     *         was updated
     */
    public Long zadd(final String key, final Number score, final String member) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.zadd(key, score, member);

            }
        });
    }

    public Set<String> zrange(final String key, final int start, final int end) {
        return execute(new RedisCallback<Set<String>>() {
            public Set<String> doIt(Session session) {
                return session.zrange(key, start, end);
            }
        });
    }

    /**
     * Remove the specified member from the sorted set value stored at key. If
     * member was not a member of the set no operation is performed. If key does
     * not not hold a set value an error is returned.
     * <p/>
     * Time complexity O(log(N)) with N being the number of elements in the
     * sorted set
     *
     * @param key    the key
     * @param member
     * @return Integer reply, specifically: 1 if the new element was removed 0
     *         if the new element was not a member of the set
     */
    public Long zrem(final String key, final String member) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.zrem(key, member);

            }
        });
    }

    /**
     * If member already exists in the sorted set adds the increment to its
     * score and updates the position of the element in the sorted set
     * accordingly. If member does not already exist in the sorted set it is
     * added with increment as score (that is, like if the previous score was
     * virtually zero). If key does not exist a new sorted set with the
     * specified member as sole member is crated. If the key exists but does not
     * hold a sorted set value an error is returned.
     * <p/>
     * The score value can be the string representation of a double precision
     * floating point number. It's possible to provide a negative value to
     * perform a decrement.
     * <p/>
     * For an introduction to sorted sets check the Introduction to Redis data
     * types page.
     * <p/>
     * Time complexity O(log(N)) with N being the number of elements in the
     * sorted set
     *
     * @param key    the key
     * @param score
     * @param member
     * @return The new score
     */
    public String zincrby(final String key, final Number score,
                          final String member) {
        return execute(new RedisCallback<String>() {
            public String doIt(Session session) {
                return session.zincrby(key, score, member);

            }
        });
    }

    /**
     * Return the rank (or index) or member in the sorted set at key, with
     * scores being ordered from low to high.
     * <p/>
     * When the given member does not exist in the sorted set, the special value
     * 'nil' is returned. The returned rank (or index) of the member is 0-based
     * for both commands.
     * <p/>
     * <b>Time complexity:</b>
     * <p/>
     * O(log(N))
     *
     * @param key    the key
     * @param member
     * @return Integer reply or a nil bulk reply, specifically: the rank of the
     *         element as an integer reply if the element exists. A nil bulk
     *         reply if there is no such element.
     * @see #zrevrank(String, String)
     */
    public Long zrank(final String key, final String member) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.zrank(key, member);

            }
        });
    }

    /**
     * Return the rank (or index) or member in the sorted set at key, with
     * scores being ordered from high to low.
     * <p/>
     * When the given member does not exist in the sorted set, the special value
     * 'nil' is returned. The returned rank (or index) of the member is 0-based
     * for both commands.
     * <p/>
     * <b>Time complexity:</b>
     * <p/>
     * O(log(N))
     *
     * @param key    the key
     * @param member
     * @return Integer reply or a nil bulk reply, specifically: the rank of the
     *         element as an integer reply if the element exists. A nil bulk
     *         reply if there is no such element.
     * @see #zrank(String, String)
     */
    public Long zrevrank(final String key, final String member) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.zrevrank(key, member);

            }
        });
    }

    public Set<String> zrevrange(final String key, final int start,
                                 final int end) {
        return execute(new RedisCallback<Set<String>>() {
            public Set<String> doIt(Session session) {
                return session.zrevrange(key, start, end);
            }
        });
    }

    public Map<String, String> zrangeWithScores(final String key, final int start,
                                                final int end) {
        return execute(new RedisCallback<Map<String, String>>() {
            public Map<String, String> doIt(Session session) {
                return session.zrangeWithScores(key, start, end);
            }
        });
    }

    public Map<String, String> zrevrangeWithScores(final String key, final int start,
                                                   final int end) {
        return execute(new RedisCallback<Map<String, String>>() {
            public Map<String, String> doIt(Session session) {
                return session.zrevrangeWithScores(key, start, end);
            }
        });
    }

    /**
     * Return the sorted set cardinality (number of elements). If the key does
     * not exist 0 is returned, like for empty sorted sets.
     * <p/>
     * Time complexity O(1)
     *
     * @param key the key
     * @return the cardinality (number of elements) of the set as an integer.
     */
    public Long zcard(final String key) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.zcard(key);

            }
        });
    }

    /**
     * Return the score of the specified element of the sorted set at key. If
     * the specified element does not exist in the sorted set, or the key does
     * not exist at all, a special 'nil' value is returned.
     * <p/>
     * <b>Time complexity:</b> O(1)
     *
     * @param key    the key
     * @param member
     * @return the score
     */
    public String zscore(final String key, final String member) {
        return execute(new RedisCallback<String>() {
            public String doIt(Session session) {
                return session.zscore(key, member);

            }
        });
    }

    /**
     * Sort a Set or a List.
     * <p/>
     * Sort the elements contained in the List, Set, or Sorted Set value at key.
     * By default sorting is numeric with elements being compared as double
     * precision floating point numbers. This is the simplest form of SORT.
     *
     * @param key the key
     * @return Assuming the Set/List at key contains a list of numbers, the
     *         return value will be the list of numbers ordered from the
     *         smallest to the biggest number.
     * @see #sort(String, String)
     * @see #sort(String, SortingParams)
     * @see #sort(String, SortingParams, String)
     */
    public List<String> sort(final String key) {
        return execute(new RedisCallback<List<String>>() {
            public List<String> doIt(Session session) {
                return session.sort(key);

            }
        });
    }

    /**
     * Sort a Set or a List accordingly to the specified parameters.
     * <p/>
     * <b>examples:</b>
     * <p/>
     * Given are the following sets and key/values:
     * <p/>
     * <pre>
     * x = [1, 2, 3]
     * y = [a, b, c]
     * <p/>
     * k1 = z
     * k2 = y
     * k3 = x
     * <p/>
     * w1 = 9
     * w2 = 8
     * w3 = 7
     * </pre>
     * <p/>
     * Sort Order:
     * <p/>
     * <pre>
     * sort(x) or sort(x, sp.asc())
     * -> [1, 2, 3]
     * <p/>
     * sort(x, sp.desc())
     * -> [3, 2, 1]
     * <p/>
     * sort(y)
     * -> [c, a, b]
     * <p/>
     * sort(y, sp.alpha())
     * -> [a, b, c]
     * <p/>
     * sort(y, sp.alpha().desc())
     * -> [c, a, b]
     * </pre>
     * <p/>
     * Limit (e.g. for Pagination):
     * <p/>
     * <pre>
     * sort(x, sp.limit(0, 2))
     * -> [1, 2]
     * <p/>
     * sort(y, sp.alpha().desc().limit(1, 2))
     * -> [b, a]
     * </pre>
     * <p/>
     * Sorting by external keys:
     * <p/>
     * <pre>
     * sort(x, sb.by(w*))
     * -> [3, 2, 1]
     * <p/>
     * sort(x, sb.by(w*).desc())
     * -> [1, 2, 3]
     * </pre>
     * <p/>
     * Getting external keys:
     * <p/>
     * <pre>
     * sort(x, sp.by(w*).get(k*))
     * -> [x, y, z]
     * <p/>
     * sort(x, sp.by(w*).get(#).get(k*))
     * -> [3, x, 2, y, 1, z]
     * </pre>
     *
     * @param key               the key
     * @param sortingParameters
     * @return a list of sorted elements.
     * @see #sort(String)
     * @see #sort(String, SortingParams, String)
     */
    public List<String> sort(final String key,
                             final SortingParams sortingParameters) {
        return execute(new RedisCallback<List<String>>() {
            public List<String> doIt(Session session) {
                return session.sort(key, sortingParameters);

            }
        });
    }

    /**
     * BLPOP (and BRPOP) is a blocking list pop primitive. You can see this
     * commands as blocking versions of LPOP and RPOP able to block if the
     * specified keys don't exist or contain empty lists.
     * <p/>
     * The following is a description of the exact semantic. We describe BLPOP
     * but the two commands are identical, the only difference is that BLPOP
     * pops the element from the left (head) of the list, and BRPOP pops from
     * the right (tail).
     * <p/>
     * <b>Non blocking behavior</b>
     * <p/>
     * When BLPOP is called, if at least one of the specified keys contain a non
     * empty list, an element is popped from the head of the list and returned
     * to the caller together with the name of the key (BLPOP returns a two
     * elements array, the first element is the key, the second the popped
     * value).
     * <p/>
     * Keys are scanned from left to right, so for instance if you issue BLPOP
     * list1 list2 list3 0 against a dataset where list1 does not exist but
     * list2 and list3 contain non empty lists, BLPOP guarantees to return an
     * element from the list stored at list2 (since it is the first non empty
     * list starting from the left).
     * <p/>
     * <b>Blocking behavior</b>
     * <p/>
     * If none of the specified keys exist or contain non empty lists, BLPOP
     * blocks until some other client performs a LPUSH or an RPUSH operation
     * against one of the lists.
     * <p/>
     * Once new data is present on one of the lists, the client finally returns
     * with the name of the key unblocking it and the popped value.
     * <p/>
     * When blocking, if a non-zero timeout is specified, the client will
     * unblock returning a nil special value if the specified amount of seconds
     * passed without a push operation against at least one of the specified
     * keys.
     * <p/>
     * The timeout argument is interpreted as an integer value. A timeout of
     * zero means instead to block forever.
     * <p/>
     * <b>Multiple clients blocking for the same keys</b>
     * <p/>
     * Multiple clients can block for the same key. They are put into a queue,
     * so the first to be served will be the one that started to wait earlier,
     * in a first-blpopping first-served fashion.
     * <p/>
     * <b>blocking POP inside a MULTI/EXEC transaction</b>
     * <p/>
     * BLPOP and BRPOP can be used with pipelining (sending multiple commands
     * and reading the replies in batch), but it does not make sense to use
     * BLPOP or BRPOP inside a MULTI/EXEC block (a Redis transaction).
     * <p/>
     * The behavior of BLPOP inside MULTI/EXEC when the list is empty is to
     * return a multi-bulk nil reply, exactly what happens when the timeout is
     * reached. If you like science fiction, think at it like if inside
     * MULTI/EXEC the time will flow at infinite speed :)
     * <p/>
     * Time complexity: O(1)
     *
     * @param timeout
     * @param keys    the keys
     * @return BLPOP returns a two-elements array via a multi bulk reply in
     *         order to return both the unblocking key and the popped value.
     *         <p/>
     *         When a non-zero timeout is specified, and the BLPOP operation
     *         timed out, the return value is a nil multi bulk reply. Most
     *         client values will return false or nil accordingly to the
     *         programming language used.
     * @see #brpop(int, String...)
     */
    public List<String> blpop(final int timeout, final String... keys) {
        return execute(new RedisCallback<List<String>>() {
            public List<String> doIt(Session session) {
                return session.blpop(timeout, keys);
            }
        });
    }

    /**
     * Sort a Set or a List accordingly to the specified parameters and store
     * the result at dstkey.
     *
     * @param key               the key
     * @param sortingParameters
     * @param dstkey
     * @return The number of elements of the list at dstkey.
     * @see #sort(String, SortingParams)
     * @see #sort(String)
     * @see #sort(String, String)
     */
    public Long sort(final String key, final SortingParams sortingParameters,
                     final String dstkey) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.sort(key, sortingParameters, dstkey);

            }
        });
    }

    /**
     * Sort a Set or a List and Store the Result at dstkey.
     * <p/>
     * Sort the elements contained in the List, Set, or Sorted Set value at key
     * and store the result at dstkey. By default sorting is numeric with
     * elements being compared as double precision floating point numbers. This
     * is the simplest form of SORT.
     *
     * @param key    the key
     * @param dstkey
     * @return The number of elements of the list at dstkey.
     * @see #sort(String)
     * @see #sort(String, SortingParams)
     * @see #sort(String, SortingParams, String)
     */
    public Long sort(final String key, final String dstkey) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.sort(key, dstkey);

            }
        });
    }

    /**
     * BLPOP (and BRPOP) is a blocking list pop primitive. You can see this
     * commands as blocking versions of LPOP and RPOP able to block if the
     * specified keys don't exist or contain empty lists.
     * <p/>
     * The following is a description of the exact semantic. We describe BLPOP
     * but the two commands are identical, the only difference is that BLPOP
     * pops the element from the left (head) of the list, and BRPOP pops from
     * the right (tail).
     * <p/>
     * <b>Non blocking behavior</b>
     * <p/>
     * When BLPOP is called, if at least one of the specified keys contain a non
     * empty list, an element is popped from the head of the list and returned
     * to the caller together with the name of the key (BLPOP returns a two
     * elements array, the first element is the key, the second the popped
     * value).
     * <p/>
     * Keys are scanned from left to right, so for instance if you issue BLPOP
     * list1 list2 list3 0 against a dataset where list1 does not exist but
     * list2 and list3 contain non empty lists, BLPOP guarantees to return an
     * element from the list stored at list2 (since it is the first non empty
     * list starting from the left).
     * <p/>
     * <b>Blocking behavior</b>
     * <p/>
     * If none of the specified keys exist or contain non empty lists, BLPOP
     * blocks until some other client performs a LPUSH or an RPUSH operation
     * against one of the lists.
     * <p/>
     * Once new data is present on one of the lists, the client finally returns
     * with the name of the key unblocking it and the popped value.
     * <p/>
     * When blocking, if a non-zero timeout is specified, the client will
     * unblock returning a nil special value if the specified amount of seconds
     * passed without a push operation against at least one of the specified
     * keys.
     * <p/>
     * The timeout argument is interpreted as an integer value. A timeout of
     * zero means instead to block forever.
     * <p/>
     * <b>Multiple clients blocking for the same keys</b>
     * <p/>
     * Multiple clients can block for the same key. They are put into a queue,
     * so the first to be served will be the one that started to wait earlier,
     * in a first-blpopping first-served fashion.
     * <p/>
     * <b>blocking POP inside a MULTI/EXEC transaction</b>
     * <p/>
     * BLPOP and BRPOP can be used with pipelining (sending multiple commands
     * and reading the replies in batch), but it does not make sense to use
     * BLPOP or BRPOP inside a MULTI/EXEC block (a Redis transaction).
     * <p/>
     * The behavior of BLPOP inside MULTI/EXEC when the list is empty is to
     * return a multi-bulk nil reply, exactly what happens when the timeout is
     * reached. If you like science fiction, think at it like if inside
     * MULTI/EXEC the time will flow at infinite speed :)
     * <p/>
     * Time complexity: O(1)
     *
     * @param timeout
     * @param keys    the keys
     * @return BLPOP returns a two-elements array via a multi bulk reply in
     *         order to return both the unblocking key and the popped value.
     *         <p/>
     *         When a non-zero timeout is specified, and the BLPOP operation
     *         timed out, the return value is a nil multi bulk reply. Most
     *         client values will return false or nil accordingly to the
     *         programming language used.
     * @see #blpop(int, String...)
     */
    public List<String> brpop(final int timeout, final String... keys) {
        return execute(new RedisCallback<List<String>>() {
            public List<String> doIt(Session session) {
                return session.brpop(timeout, keys);
            }
        });
    }

    /**
     * Request for authentication in a password protected Redis server. A Redis
     * server can be instructed to require a password before to allow clients to
     * issue commands. This is done using the requirepass directive in the Redis
     * configuration file. If the password given by the client is correct the
     * server replies with an OK status code reply and starts accepting commands
     * from the client. Otherwise an error is returned and the clients needs to
     * try a new password. Note that for the high performance nature of Redis it
     * is possible to try a lot of passwords in parallel in very short time, so
     * make sure to generate a strong and very long password so that this attack
     * is infeasible.
     *
     * @param password
     * @return Status code reply
     */
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

    /**
     * Return the all the elements in the sorted set at key with a score between
     * min and max (including elements with score equal to min or max).
     * <p/>
     * The elements having the same score are returned sorted lexicographically
     * as ASCII strings (this follows from a property of Redis sorted sets and
     * does not involve further computation).
     * <p/>
     * Using the optional
     * {@link RedisOperations#zrangeByScore(String, String, String, int, int) LIMIT} it's
     * possible to get only a range of the matching elements in an SQL-alike
     * way. Note that if offset is large the commands needs to traverse the list
     * for offset elements and this adds up to the O(M) figure.
     * <p/>
     * The {@link #zcount(String, Number, Number) ZCOUNT} command is similar to
     * {@link RedisOperations#zrangeByScore(String, String, String) ZRANGEBYSCORE} but instead
     * of returning the actual elements in the specified interval, it just
     * returns the number of matching elements.
     * <p/>
     * <b>Exclusive intervals and infinity</b>
     * <p/>
     * min and max can be -inf and +inf, so that you are not required to know
     * what's the greatest or smallest element in order to take, for instance,
     * elements "up to a given value".
     * <p/>
     * Also while the interval is for default closed (inclusive) it's possible
     * to specify open intervals prefixing the score with a "(" character, so
     * for instance:
     * <p/>
     * {@code ZRANGEBYSCORE zset (1.3 5}
     * <p/>
     * Will return all the values with score > 1.3 and <= 5, while for instance:
     * <p/>
     * {@code ZRANGEBYSCORE zset (5 (10}
     * <p/>
     * Will return all the values with score > 5 and < 10 (5 and 10 excluded).
     * <p/>
     * <b>Time complexity:</b>
     * <p/>
     * O(log(N))+O(M) with N being the number of elements in the sorted set and
     * M the number of elements returned by the command, so if M is constant
     * (for instance you always ask for the first ten elements with LIMIT) you
     * can consider it O(log(N))
     *
     *
     * @param key the key
     * @param min
     * @param max
     * @return Multi bulk reply specifically a list of elements in the specified
     *         score range.
     * @see RedisOperations#zrangeByScore(String, String, String)
     * @see RedisOperations#zrangeByScore(String, String, String, int, int)
     * @see RedisOperations#zrangeByScoreWithScores(String, String, String)
     * @see RedisOperations#zrangeByScoreWithScores(String, String, String, int, int)
     * @see #zcount(String, Number, Number)
     */
    public Set<String> zrangeByScore(final String key, final String min, final String max) {
        return execute(new RedisCallback<Set<String>>() {
            public Set<String> doIt(Session session) {
                return session.zrangeByScore(key, min, max);
            }
        });
    }

    /**
     * Return the all the elements in the sorted set at key with a score between
     * min and max (including elements with score equal to min or max).
     * <p/>
     * The elements having the same score are returned sorted lexicographically
     * as ASCII strings (this follows from a property of Redis sorted sets and
     * does not involve further computation).
     * <p/>
     * Using the optional
     * {@link RedisOperations#zrangeByScore(String, String, String, int, int) LIMIT} it's
     * possible to get only a range of the matching elements in an SQL-alike
     * way. Note that if offset is large the commands needs to traverse the list
     * for offset elements and this adds up to the O(M) figure.
     * <p/>
     * The {@link #zcount(String, Number, Number) ZCOUNT} command is similar to
     * {@link RedisOperations#zrangeByScore(String, String, String) ZRANGEBYSCORE} but instead
     * of returning the actual elements in the specified interval, it just
     * returns the number of matching elements.
     * <p/>
     * <b>Exclusive intervals and infinity</b>
     * <p/>
     * min and max can be -inf and +inf, so that you are not required to know
     * what's the greatest or smallest element in order to take, for instance,
     * elements "up to a given value".
     * <p/>
     * Also while the interval is for default closed (inclusive) it's possible
     * to specify open intervals prefixing the score with a "(" character, so
     * for instance:
     * <p/>
     * {@code ZRANGEBYSCORE zset (1.3 5}
     * <p/>
     * Will return all the values with score > 1.3 and <= 5, while for instance:
     * <p/>
     * {@code ZRANGEBYSCORE zset (5 (10}
     * <p/>
     * Will return all the values with score > 5 and < 10 (5 and 10 excluded).
     * <p/>
     * <b>Time complexity:</b>
     * <p/>
     * O(log(N))+O(M) with N being the number of elements in the sorted set and
     * M the number of elements returned by the command, so if M is constant
     * (for instance you always ask for the first ten elements with LIMIT) you
     * can consider it O(log(N))
     *
     *
     * @param key the key
     * @param min
     * @param max
     * @return Multi bulk reply specifically a list of elements in the specified
     *         score range.
     * @see RedisOperations#zrangeByScore(String, String, String)
     * @see RedisOperations#zrangeByScore(String, String, String, int, int)
     * @see RedisOperations#zrangeByScoreWithScores(String, String, String)
     * @see RedisOperations#zrangeByScoreWithScores(String, String, String, int, int)
     * @see #zcount(String, Number, Number)
     */
    public Set<String> zrangeByScore(final String key, final String min,
                                     final String max, final int offset, final int count) {
        return execute(new RedisCallback<Set<String>>() {
            public Set<String> doIt(Session session) {
                return session.zrangeByScore(key, min, max, offset, count);
            }
        });
    }

    /**
     * Return the all the elements in the sorted set at key with a score between
     * min and max (including elements with score equal to min or max).
     * <p/>
     * The elements having the same score are returned sorted lexicographically
     * as ASCII strings (this follows from a property of Redis sorted sets and
     * does not involve further computation).
     * <p/>
     * Using the optional
     * {@link RedisOperations#zrangeByScore(String, String, String, int, int) LIMIT} it's
     * possible to get only a range of the matching elements in an SQL-alike
     * way. Note that if offset is large the commands needs to traverse the list
     * for offset elements and this adds up to the O(M) figure.
     * <p/>
     * The {@link #zcount(String, Number, Number) ZCOUNT} command is similar to
     * {@link RedisOperations#zrangeByScore(String, String, String) ZRANGEBYSCORE} but instead
     * of returning the actual elements in the specified interval, it just
     * returns the number of matching elements.
     * <p/>
     * <b>Exclusive intervals and infinity</b>
     * <p/>
     * min and max can be -inf and +inf, so that you are not required to know
     * what's the greatest or smallest element in order to take, for instance,
     * elements "up to a given value".
     * <p/>
     * Also while the interval is for default closed (inclusive) it's possible
     * to specify open intervals prefixing the score with a "(" character, so
     * for instance:
     * <p/>
     * {@code ZRANGEBYSCORE zset (1.3 5}
     * <p/>
     * Will return all the values with score > 1.3 and <= 5, while for instance:
     * <p/>
     * {@code ZRANGEBYSCORE zset (5 (10}
     * <p/>
     * Will return all the values with score > 5 and < 10 (5 and 10 excluded).
     * <p/>
     * <b>Time complexity:</b>
     * <p/>
     * O(log(N))+O(M) with N being the number of elements in the sorted set and
     * M the number of elements returned by the command, so if M is constant
     * (for instance you always ask for the first ten elements with LIMIT) you
     * can consider it O(log(N))
     *
     *
     * @param key the key
     * @param min
     * @param max
     * @return Multi bulk reply specifically a list of elements in the specified
     *         score range.
     * @see RedisOperations#zrangeByScore(String, String, String)
     * @see RedisOperations#zrangeByScore(String, String, String, int, int)
     * @see RedisOperations#zrangeByScoreWithScores(String, String, String)
     * @see RedisOperations#zrangeByScoreWithScores(String, String, String, int, int)
     * @see #zcount(String, Number, Number)
     */
    public Map<String, String> zrangeByScoreWithScores(final String key,
                                                       final String min, final String max) {
        return execute(new RedisCallback<Map<String, String>>() {
            public Map<String, String> doIt(Session session) {
                return session.zrangeByScoreWithScores(key, min, max);
            }
        });
    }

    /**
     * Return the all the elements in the sorted set at key with a score between
     * min and max (including elements with score equal to min or max).
     * <p/>
     * The elements having the same score are returned sorted lexicographically
     * as ASCII strings (this follows from a property of Redis sorted sets and
     * does not involve further computation).
     * <p/>
     * Using the optional
     * {@link RedisOperations#zrangeByScore(String, String, String, int, int) LIMIT} it's
     * possible to get only a range of the matching elements in an SQL-alike
     * way. Note that if offset is large the commands needs to traverse the list
     * for offset elements and this adds up to the O(M) figure.
     * <p/>
     * The {@link #zcount(String, Number, Number) ZCOUNT} command is similar to
     * {@link RedisOperations#zrangeByScore(String, String, String) ZRANGEBYSCORE} but instead
     * of returning the actual elements in the specified interval, it just
     * returns the number of matching elements.
     * <p/>
     * <b>Exclusive intervals and infinity</b>
     * <p/>
     * min and max can be -inf and +inf, so that you are not required to know
     * what's the greatest or smallest element in order to take, for instance,
     * elements "up to a given value".
     * <p/>
     * Also while the interval is for default closed (inclusive) it's possible
     * to specify open intervals prefixing the score with a "(" character, so
     * for instance:
     * <p/>
     * {@code ZRANGEBYSCORE zset (1.3 5}
     * <p/>
     * Will return all the values with score > 1.3 and <= 5, while for instance:
     * <p/>
     * {@code ZRANGEBYSCORE zset (5 (10}
     * <p/>
     * Will return all the values with score > 5 and < 10 (5 and 10 excluded).
     * <p/>
     * <b>Time complexity:</b>
     * <p/>
     * O(log(N))+O(M) with N being the number of elements in the sorted set and
     * M the number of elements returned by the command, so if M is constant
     * (for instance you always ask for the first ten elements with LIMIT) you
     * can consider it O(log(N))
     *
     *
     * @param key the key
     * @param min
     * @param max
     * @return Multi bulk reply specifically a list of elements in the specified
     *         score range.
     * @see RedisOperations#zrangeByScore(String, String, String)
     * @see RedisOperations#zrangeByScore(String, String, String, int, int)
     * @see RedisOperations#zrangeByScoreWithScores(String, String, String)
     * @see RedisOperations#zrangeByScoreWithScores(String, String, String, int, int)
     * @see #zcount(String, Number, Number)
     */
    public Map<String, String> zrangeByScoreWithScores(final String key,
                                                       final String min, final String max, final int offset,
                                                       final int count) {
        return execute(new RedisCallback<Map<String, String>>() {
            public Map<String, String> doIt(Session session) {
                return session.zrangeByScoreWithScores(key, min, max, offset, count);
            }
        });
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

    /**
     * Remove all elements in the sorted set at key with rank between start and
     * end. Start and end are 0-based with rank 0 being the element with the
     * lowest score. Both start and end can be negative numbers, where they
     * indicate offsets starting at the element with the highest rank. For
     * example: -1 is the element with the highest score, -2 the element with
     * the second highest score and so forth.
     * <p/>
     * <b>Time complexity:</b> O(log(N))+O(M) with N being the number of
     * elements in the sorted set and M the number of elements removed by the
     * operation
     */
    public Long zremrangeByRank(final String key, final int start, final int end) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.zremrangeByRank(key, start, end);

            }
        });
    }

    /**
     * Remove all the elements in the sorted set at key with a score between min
     * and max (including elements with score equal to min or max).
     * <p/>
     * <b>Time complexity:</b>
     * <p/>
     * O(log(N))+O(M) with N being the number of elements in the sorted set and
     * M the number of elements removed by the operation
     *
     * @param key   the key
     * @param start
     * @param end
     * @return Integer reply, specifically the number of elements removed.
     */
    public Long zremrangeByScore(final String key, final Number start,
                                 final Number end) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.zremrangeByScore(key, start, end);

            }
        });
    }

    /**
     * Creates a union or intersection of N sorted sets given by keys k1 through
     * kN, and stores it at dstkey. It is mandatory to provide the number of
     * input keys N, before passing the input keys and the other (optional)
     * arguments.
     * <p/>
     * As the terms imply, the {@link #zinterstore(String, String...)
     * ZINTERSTORE} command requires an element to be present in each of the
     * given inputs to be inserted in the result. The
     * {@link #zunionstore(String, String...) ZUNIONSTORE} command inserts all
     * elements across all inputs.
     * <p/>
     * Using the WEIGHTS option, it is possible to add weight to each input
     * sorted set. This means that the score of each element in the sorted set
     * is first multiplied by this weight before being passed to the
     * aggregation. When this option is not given, all weights default to 1.
     * <p/>
     * With the AGGREGATE option, it's possible to specify how the results of
     * the union or intersection are aggregated. This option defaults to SUM,
     * where the score of an element is summed across the inputs where it
     * exists. When this option is set to be either MIN or MAX, the resulting
     * set will contain the minimum or maximum score of an element across the
     * inputs where it exists.
     * <p/>
     * <b>Time complexity:</b> O(N) + O(M log(M)) with N being the sum of the
     * sizes of the input sorted sets, and M being the number of elements in the
     * resulting sorted set
     *
     * @param dstkey
     * @param sets
     * @return Integer reply, specifically the number of elements in the sorted
     *         set at dstkey
     * @see #zunionstore(String, ZParams, String...)
     */
    public Long zunionstore(final String dstkey, final String... sets) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.zunionstore(dstkey, sets);

            }
        });
    }

    /**
     * Creates a union or intersection of N sorted sets given by keys k1 through
     * kN, and stores it at dstkey. It is mandatory to provide the number of
     * input keys N, before passing the input keys and the other (optional)
     * arguments.
     * <p/>
     * As the terms imply, the {@link #zinterstore(String, String...)
     * ZINTERSTORE} command requires an element to be present in each of the
     * given inputs to be inserted in the result. The
     * {@link #zunionstore(String, String...) ZUNIONSTORE} command inserts all
     * elements across all inputs.
     * <p/>
     * Using the WEIGHTS option, it is possible to add weight to each input
     * sorted set. This means that the score of each element in the sorted set
     * is first multiplied by this weight before being passed to the
     * aggregation. When this option is not given, all weights default to 1.
     * <p/>
     * With the AGGREGATE option, it's possible to specify how the results of
     * the union or intersection are aggregated. This option defaults to SUM,
     * where the score of an element is summed across the inputs where it
     * exists. When this option is set to be either MIN or MAX, the resulting
     * set will contain the minimum or maximum score of an element across the
     * inputs where it exists.
     * <p/>
     * <b>Time complexity:</b> O(N) + O(M log(M)) with N being the sum of the
     * sizes of the input sorted sets, and M being the number of elements in the
     * resulting sorted set
     *
     * @param dstkey
     * @param sets
     * @param params
     * @return Integer reply, specifically the number of elements in the sorted
     *         set at dstkey
     * @see #zunionstore(String, String...)
     */
    public Long zunionstore(final String dstkey, final ZParams params, final String... sets) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.zunionstore(dstkey, params, sets);

            }
        });
    }

    /**
     * Creates a union or intersection of N sorted sets given by keys k1 through
     * kN, and stores it at dstkey. It is mandatory to provide the number of
     * input keys N, before passing the input keys and the other (optional)
     * arguments.
     * <p/>
     * As the terms imply, the {@link #zinterstore(String, String...)
     * ZINTERSTORE} command requires an element to be present in each of the
     * given inputs to be inserted in the result. The
     * {@link #zunionstore(String, String...) ZUNIONSTORE} command inserts all
     * elements across all inputs.
     * <p/>
     * Using the WEIGHTS option, it is possible to add weight to each input
     * sorted set. This means that the score of each element in the sorted set
     * is first multiplied by this weight before being passed to the
     * aggregation. When this option is not given, all weights default to 1.
     * <p/>
     * With the AGGREGATE option, it's possible to specify how the results of
     * the union or intersection are aggregated. This option defaults to SUM,
     * where the score of an element is summed across the inputs where it
     * exists. When this option is set to be either MIN or MAX, the resulting
     * set will contain the minimum or maximum score of an element across the
     * inputs where it exists.
     * <p/>
     * <b>Time complexity:</b> O(N) + O(M log(M)) with N being the sum of the
     * sizes of the input sorted sets, and M being the number of elements in the
     * resulting sorted set
     *
     * @param dstkey
     * @param sets
     * @return Integer reply, specifically the number of elements in the sorted
     *         set at dstkey
     * @see #zunionstore(String, String...)
     * @see #zunionstore(String, ZParams, String...)
     */
    public Long zinterstore(final String dstkey, final String... sets) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.zinterstore(dstkey, sets);

            }
        });
    }

    /**
     * Creates a union or intersection of N sorted sets given by keys k1 through
     * kN, and stores it at dstkey. It is mandatory to provide the number of
     * input keys N, before passing the input keys and the other (optional)
     * arguments.
     * <p/>
     * As the terms imply, the {@link #zinterstore(String, String...)
     * ZINTERSTORE} command requires an element to be present in each of the
     * given inputs to be inserted in the result. The
     * {@link #zunionstore(String, String...) ZUNIONSTORE} command inserts all
     * elements across all inputs.
     * <p/>
     * Using the WEIGHTS option, it is possible to add weight to each input
     * sorted set. This means that the score of each element in the sorted set
     * is first multiplied by this weight before being passed to the
     * aggregation. When this option is not given, all weights default to 1.
     * <p/>
     * With the AGGREGATE option, it's possible to specify how the results of
     * the union or intersection are aggregated. This option defaults to SUM,
     * where the score of an element is summed across the inputs where it
     * exists. When this option is set to be either MIN or MAX, the resulting
     * set will contain the minimum or maximum score of an element across the
     * inputs where it exists.
     * <p/>
     * <b>Time complexity:</b> O(N) + O(M log(M)) with N being the sum of the
     * sizes of the input sorted sets, and M being the number of elements in the
     * resulting sorted set
     *
     * @param dstkey
     * @param sets
     * @param params
     * @return Integer reply, specifically the number of elements in the sorted
     *         set at dstkey
     */
    public Long zinterstore(final String dstkey, final ZParams params, final String... sets) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.zinterstore(dstkey, params, sets);

            }
        });
    }

    /**
     * Synchronously save the DB on disk.
     * <p/>
     * Save the whole dataset on disk (this means that all the databases are
     * saved, as well as keys with an EXPIRE set (the expire is preserved). The
     * server hangs while the saving is not completed, no connection is served
     * in the meanwhile. An OK code is returned when the DB was fully stored in
     * disk.
     * <p/>
     * The background variant of this command is {@link #bgsave() BGSAVE} that
     * is able to perform the saving in the background while the server
     * continues serving other clients.
     * <p/>
     *
     * @return Status code reply
     */
    public String save() {
        return execute(new RedisCallback<String>() {
            public String doIt(Session session) {
                return session.save();

            }
        });
    }

    /**
     * Asynchronously save the DB on disk.
     * <p/>
     * Save the DB in background. The OK code is immediately returned. Redis
     * forks, the parent continues to server the clients, the child saves the DB
     * on disk then exit. A client my be able to check if the operation
     * succeeded using the LASTSAVE command.
     *
     * @return Status code reply
     */
    public String bgsave() {
        return execute(new RedisCallback<String>() {
            public String doIt(Session session) {
                return session.bgsave();

            }
        });
    }

    /**
     * Rewrite the append only file in background when it gets too big. Please
     * for detailed information about the Redis Append Only File check the <a
     * href="http://code.google.com/p/redis/wiki/AppendOnlyFileHowto">Append
     * Only File Howto</a>.
     * <p/>
     * BGREWRITEAOF rewrites the Append Only File in background when it gets too
     * big. The Redis Append Only File is a Journal, so every operation
     * modifying the dataset is logged in the Append Only File (and replayed at
     * startup). This means that the Append Only File always grows. In order to
     * rebuild its content the BGREWRITEAOF creates a new version of the append
     * only file starting directly form the dataset in memory in order to
     * guarantee the generation of the minimal number of commands needed to
     * rebuild the database.
     * <p/>
     *
     * @return Status code reply
     */
    public String bgrewriteaof() {
        return execute(new RedisCallback<String>() {
            public String doIt(Session session) {
                return session.bgrewriteaof();

            }
        });
    }

    /**
     * Return the UNIX time stamp of the last successfully saving of the dataset
     * on disk.
     * <p/>
     * Return the UNIX TIME of the last DB save executed with success. A client
     * may check if a {@link #bgsave() BGSAVE} command succeeded reading the
     * LASTSAVE value, then issuing a BGSAVE command and checking at regular
     * intervals every N seconds if LASTSAVE changed.
     *
     * @return Integer reply, specifically an UNIX time stamp.
     */
    public Long lastsave() {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.lastsave();

            }
        });
    }

    /**
     * Synchronously save the DB on disk, then shutdown the server.
     * <p/>
     * Stop all the clients, save the DB, then quit the server. This commands
     * makes sure that the DB is switched off without the lost of any data. This
     * is not guaranteed if the client uses simply {@link #save() SAVE} and then
     * {@link #quit() QUIT} because other clients may alter the DB data between
     * the two commands.
     *
     * @return Status code reply on error. On success nothing is returned since
     *         the server quits and the connection is closed.
     */
    public String shutdown() {
        return execute(new RedisCallback<String>() {
            public String doIt(Session session) {
                return session.shutdown();
            }
        });
    }

    /**
     * Provide information and statistics about the server.
     * <p/>
     * The info command returns different information and statistics about the
     * server in an format that's simple to parse by computers and easy to read
     * by humans.
     * <p/>
     * <b>Format of the returned String:</b>
     * <p/>
     * All the fields are in the form field:value
     * <p/>
     * <pre>
     * edis_version:0.07
     * connected_clients:1
     * connected_slaves:0
     * used_memory:3187
     * changes_since_last_save:0
     * last_save_time:1237655729
     * total_connections_received:1
     * total_commands_processed:1
     * uptime_in_seconds:25
     * uptime_in_days:0
     * </pre>
     * <p/>
     * <b>Notes</b>
     * <p/>
     * used_memory is returned in bytes, and is the total number of bytes
     * allocated by the program using malloc.
     * <p/>
     * uptime_in_days is redundant since the uptime in seconds contains already
     * the full uptime information, this field is only mainly present for
     * humans.
     * <p/>
     * changes_since_last_save does not refer to the number of key changes, but
     * to the number of operations that produced some kind of change in the
     * dataset.
     * <p/>
     *
     * @return Bulk reply
     */
    public String info() {
        return execute(new RedisCallback<String>() {
            public String doIt(Session session) {
                return session.info();

            }
        });
    }

    /**
     * Dump all the received requests in real time.
     * <p/>
     * MONITOR is a debugging command that outputs the whole sequence of
     * commands received by the Redis server. is very handy in order to
     * understand what is happening into the database. This command is used
     * directly via telnet.
     *
     * @param redisMonitor
     */
    public void monitor(final RedisMonitor redisMonitor) {
        execute(new RedisCallback<String>() {
            public String doIt(Session session) {
                session.monitor(redisMonitor);
                return null;
            }
        });
    }

    /**
     * Change the replication settings.
     * <p/>
     * The SLAVEOF command can change the replication settings of a slave on the
     * fly. If a Redis server is arleady acting as slave, the command SLAVEOF NO
     * ONE will turn off the replicaiton turning the Redis server into a MASTER.
     * In the proper form SLAVEOF hostname port will make the server a slave of
     * the specific server listening at the specified hostname and port.
     * <p/>
     * If a server is already a slave of some master, SLAVEOF hostname port will
     * stop the replication against the old server and start the
     * synchrnonization against the new one discarding the old dataset.
     * <p/>
     * The form SLAVEOF no one will stop replication turning the server into a
     * MASTER but will not discard the replication. So if the old master stop
     * working it is possible to turn the slave into a master and set the
     * application to use the new master in read/write. Later when the other
     * Redis server will be fixed it can be configured in order to work as
     * slave.
     * <p/>
     *
     * @param host
     * @param port
     * @return Status code reply
     */
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

    /**
     * Retrieve the configuration of a running Redis server. Not all the
     * configuration parameters are supported.
     * <p/>
     * CONFIG GET returns the current configuration parameters. This sub command
     * only accepts a single argument, that is glob style pattern. All the
     * configuration parameters matching this parameter are reported as a list
     * of key-value pairs.
     * <p/>
     * <b>Example:</b>
     * <p/>
     * <pre>
     * $ redis-cli config get '*'
     * 1. "dbfilename"
     * 2. "dump.rdb"
     * 3. "requirepass"
     * 4. (nil)
     * 5. "masterauth"
     * 6. (nil)
     * 7. "maxmemory"
     * 8. "0\n"
     * 9. "appendfsync"
     * 10. "everysec"
     * 11. "save"
     * 12. "3600 1 300 100 60 10000"
     * <p/>
     * $ redis-cli config get 'm*'
     * 1. "masterauth"
     * 2. (nil)
     * 3. "maxmemory"
     * 4. "0\n"
     * </pre>
     *
     * @param pattern
     * @return Bulk reply.
     */
    public List<String> configGet(final String pattern) {
        return execute(new RedisCallback<List<String>>() {
            public List<String> doIt(Session session) {
                return session.configGet(pattern);

            }
        });
    }

    /**
     * Alter the configuration of a running Redis server. Not all the
     * configuration parameters are supported.
     * <p/>
     * The list of configuration parameters supported by CONFIG SET can be
     * obtained issuing a {@link #configGet(String) CONFIG GET *} command.
     * <p/>
     * The configuration set using CONFIG SET is immediately loaded by the Redis
     * server that will start acting as specified starting from the next
     * command.
     * <p/>
     * <p/>
     * <b>Parameters value format</b>
     * <p/>
     * The value of the configuration parameter is the same as the one of the
     * same parameter in the Redis configuration file, with the following
     * exceptions:
     * <p/>
     * <ul>
     * <li>The save paramter is a list of space-separated integers. Every pair
     * of integers specify the time and number of changes limit to trigger a
     * save. For instance the command CONFIG SET save "3600 10 60 10000" will
     * configure the server to issue a background saving of the RDB file every
     * 3600 seconds if there are at least 10 changes in the dataset, and every
     * 60 seconds if there are at least 10000 changes. To completely disable
     * automatic snapshots just set the parameter as an empty string.
     * <li>All the integer parameters representing memory are returned and
     * accepted only using bytes as unit.
     * </ul>
     *
     * @param parameter
     * @param value     the value
     * @return Status code reply
     */
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

    public Long lpushx(final String key, final String string) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.lpushx(key, string);

            }
        });
    }

    /**
     * Undo a {@link #expire(String, int) expire} at turning the expire key into
     * a normal key.
     * <p/>
     * Time complexity: O(1)
     *
     *
     * @param key the key
     * @return Integer reply, specifically: 1: the key is now persist. 0: the
     *         key is not persist (only happens when key not set).
     */
    public boolean persist(final String key) {
        return execute(new RedisCallback<Boolean>() {
            public Boolean doIt(Session session) {
                return session.persist(key);

            }
        });
    }

    public Long rpushx(final String key, final String string) {
        return execute(new RedisCallback<Long>() {
            public Long doIt(Session session) {
                return session.rpushx(key, string);

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

    public Long linsert(final String key, final LIST_POSITION where,
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