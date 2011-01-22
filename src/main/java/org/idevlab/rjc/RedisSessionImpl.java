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

    public Long hset(final String key, final String field, final String value) {

        client.hset(key, field, value);
        return client.getIntegerReply();
    }

    public String hget(final String key, final String field) {

        client.hget(key, field);
        return client.getBulkReply();
    }

    public Long hsetnx(final String key, final String field, final String value) {

        client.hsetnx(key, field, value);
        return client.getIntegerReply();
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

    public Boolean hexists(final String key, final String field) {

        client.hexists(key, field);
        return client.getIntegerReply() == 1;
    }

    public Long hdel(final String key, final String field) {

        client.hdel(key, field);
        return client.getIntegerReply();
    }

    public Long hlen(final String key) {

        client.hlen(key);
        return client.getIntegerReply();
    }

    public Set<String> hkeys(final String key) {

        client.hkeys(key);
        final List<String> lresult = client.getMultiBulkReply();
        return new HashSet<String>(lresult);
    }

    public List<String> hvals(final String key) {

        client.hvals(key);
        final List<String> lresult = client.getMultiBulkReply();
        return lresult;
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

    public Long rpush(final String key, final String string) {
        client.rpush(key, string);
        return client.getIntegerReply();
    }

    public Long lpush(final String key, final String string) {

        client.lpush(key, string);
        return client.getIntegerReply();
    }

    public Long llen(final String key) {

        client.llen(key);
        return client.getIntegerReply();
    }

    /**
     * Return the specified elements of the list stored at the specified key.
     * Start and end are zero-based indexes. 0 is the first element of the list
     * (the list head), 1 the next element and so on.
     * <p/>
     * For example LRANGE foobar 0 2 will return the first three elements of the
     * list.
     * <p/>
     * start and end can also be negative numbers indicating offsets from the
     * end of the list. For example -1 is the last element of the list, -2 the
     * penultimate element and so on.
     * <p/>
     * <b>Consistency with range functions in various programming languages</b>
     * <p/>
     * Note that if you have a list of numbers from 0 to 100, LRANGE 0 10 will
     * return 11 elements, that is, rightmost item is included. This may or may
     * not be consistent with behavior of range-related functions in your
     * programming language of choice (think Ruby's Range.new, Array#slice or
     * Python's range() function).
     * <p/>
     * LRANGE behavior is consistent with one of Tcl.
     * <p/>
     * <b>Out-of-range indexes</b>
     * <p/>
     * Indexes out of range will not produce an error: if start is over the end
     * of the list, or start > end, an empty list is returned. If end is over
     * the end of the list Redis will threat it just like the last element of
     * the list.
     * <p/>
     * Time complexity: O(start+n) (with n being the length of the range and
     * start being the start offset)
     *
     * @param key
     * @param start
     * @param end
     * @return Multi bulk reply, specifically a list of elements in the
     *         specified range.
     */
    public List<String> lrange(final String key, final int start, final int end) {

        client.lrange(key, start, end);
        return client.getMultiBulkReply();
    }

    /**
     * Trim an existing list so that it will contain only the specified range of
     * elements specified. Start and end are zero-based indexes. 0 is the first
     * element of the list (the list head), 1 the next element and so on.
     * <p/>
     * For example LTRIM foobar 0 2 will modify the list stored at foobar key so
     * that only the first three elements of the list will remain.
     * <p/>
     * start and end can also be negative numbers indicating offsets from the
     * end of the list. For example -1 is the last element of the list, -2 the
     * penultimate element and so on.
     * <p/>
     * Indexes out of range will not produce an error: if start is over the end
     * of the list, or start > end, an empty list is left as value. If end over
     * the end of the list Redis will threat it just like the last element of
     * the list.
     * <p/>
     * Hint: the obvious use of LTRIM is together with LPUSH/RPUSH. For example:
     * <p/>
     * {@code lpush("mylist", "someelement"); ltrim("mylist", 0, 99); * }
     * <p/>
     * The above two commands will push elements in the list taking care that
     * the list will not grow without limits. This is very useful when using
     * Redis to store logs for example. It is important to note that when used
     * in this way LTRIM is an O(1) operation because in the average case just
     * one element is removed from the tail of the list.
     * <p/>
     * Time complexity: O(n) (with n being len of list - len of range)
     *
     * @param key
     * @param start
     * @param end
     * @return Status code reply
     */
    public String ltrim(final String key, final int start, final int end) {

        client.ltrim(key, start, end);
        return client.getStatusCodeReply();
    }

    /**
     * Return the specified element of the list stored at the specified key. 0
     * is the first element, 1 the second and so on. Negative indexes are
     * supported, for example -1 is the last element, -2 the penultimate and so
     * on.
     * <p/>
     * If the value stored at key is not of list type an error is returned. If
     * the index is out of range a 'nil' reply is returned.
     * <p/>
     * Note that even if the average time complexity is O(n) asking for the
     * first or the last element of the list is O(1).
     * <p/>
     * Time complexity: O(n) (with n being the length of the list)
     *
     * @param key
     * @param index
     * @return Bulk reply, specifically the requested element
     */
    public String lindex(final String key, final int index) {

        client.lindex(key, index);
        return client.getBulkReply();
    }

    /**
     * Set a new value as the element at index position of the List at key.
     * <p/>
     * Out of range indexes will generate an error.
     * <p/>
     * Similarly to other list commands accepting indexes, the index can be
     * negative to access elements starting from the end of the list. So -1 is
     * the last element, -2 is the penultimate, and so forth.
     * <p/>
     * <b>Time complexity:</b>
     * <p/>
     * O(N) (with N being the length of the list), setting the first or last
     * elements of the list is O(1).
     *
     * @param key
     * @param index
     * @param value
     * @return Status code reply
     * @see #lindex(String, int)
     */
    public String lset(final String key, final int index, final String value) {

        client.lset(key, index, value);
        return client.getStatusCodeReply();
    }

    /**
     * Remove the first count occurrences of the value element from the list. If
     * count is zero all the elements are removed. If count is negative elements
     * are removed from tail to head, instead to go from head to tail that is
     * the normal behaviour. So for example LREM with count -2 and hello as
     * value to remove against the list (a,b,c,hello,x,hello,hello) will lave
     * the list (a,b,c,hello,x). The number of removed elements is returned as
     * an integer, see below for more information about the returned value. Note
     * that non existing keys are considered like empty lists by LREM, so LREM
     * against non existing keys will always return 0.
     * <p/>
     * Time complexity: O(N) (with N being the length of the list)
     *
     * @param key
     * @param count
     * @param value
     * @return Integer Reply, specifically: The number of removed elements if
     *         the operation succeeded
     */
    public Long lrem(final String key, final int count, final String value) {

        client.lrem(key, count, value);
        return client.getIntegerReply();
    }

    /**
     * Atomically return and remove the first (LPOP) or last (RPOP) element of
     * the list. For example if the list contains the elements "a","b","c" LPOP
     * will return "a" and the list will become "b","c".
     * <p/>
     * If the key does not exist or the list is already empty the special value
     * 'nil' is returned.
     *
     * @param key
     * @return Bulk reply
     * @see #rpop(String)
     */
    public String lpop(final String key) {

        client.lpop(key);
        return client.getBulkReply();
    }

    /**
     * Atomically return and remove the first (LPOP) or last (RPOP) element of
     * the list. For example if the list contains the elements "a","b","c" LPOP
     * will return "a" and the list will become "b","c".
     * <p/>
     * If the key does not exist or the list is already empty the special value
     * 'nil' is returned.
     *
     * @param key
     * @return Bulk reply
     * @see #lpop(String)
     */
    public String rpop(final String key) {

        client.rpop(key);
        return client.getBulkReply();
    }

    /**
     * Atomically return and remove the last (tail) element of the srckey list,
     * and push the element as the first (head) element of the dstkey list. For
     * example if the source list contains the elements "a","b","c" and the
     * destination list contains the elements "foo","bar" after an RPOPLPUSH
     * command the content of the two lists will be "a","b" and "c","foo","bar".
     * <p/>
     * If the key does not exist or the list is already empty the special value
     * 'nil' is returned. If the srckey and dstkey are the same the operation is
     * equivalent to removing the last element from the list and pusing it as
     * first element of the list, so it's a "list rotation" command.
     * <p/>
     * Time complexity: O(1)
     *
     * @param srckey
     * @param dstkey
     * @return Bulk reply
     */
    public String rpoplpush(final String srckey, final String dstkey) {

        client.rpoplpush(srckey, dstkey);
        return client.getBulkReply();
    }

    /**
     * Add the specified member to the set value stored at key. If member is
     * already a member of the set no operation is performed. If key does not
     * exist a new set with the specified member as sole member is created. If
     * the key exists but does not hold a set value an error is returned.
     * <p/>
     * Time complexity O(1)
     *
     * @param key
     * @param member
     * @return Integer reply, specifically: 1 if the new element was added 0 if
     *         the element was already a member of the set
     */
    public Long sadd(final String key, final String member) {

        client.sadd(key, member);
        return client.getIntegerReply();
    }

    /**
     * Return all the members (elements) of the set value stored at key. This is
     * just syntax glue for {@link #sinter(String...) SINTER}.
     * <p/>
     * Time complexity O(N)
     *
     * @param key
     * @return Multi bulk reply
     */
    public Set<String> smembers(final String key) {

        client.smembers(key);
        final List<String> members = client.getMultiBulkReply();
        return new LinkedHashSet<String>(members);
    }

    /**
     * Remove the specified member from the set value stored at key. If member
     * was not a member of the set no operation is performed. If key does not
     * hold a set value an error is returned.
     * <p/>
     * Time complexity O(1)
     *
     * @param key
     * @param member
     * @return Integer reply, specifically: 1 if the new element was removed 0
     *         if the new element was not a member of the set
     */
    public Long srem(final String key, final String member) {

        client.srem(key, member);
        return client.getIntegerReply();
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
     * @param key
     * @return Bulk reply
     */
    public String spop(final String key) {

        client.spop(key);
        return client.getBulkReply();
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
    public Long smove(final String srckey, final String dstkey,
                      final String member) {

        client.smove(srckey, dstkey, member);
        return client.getIntegerReply();
    }

    /**
     * Return the set cardinality (number of elements). If the key does not
     * exist 0 is returned, like for empty sets.
     *
     * @param key
     * @return Integer reply, specifically: the cardinality (number of elements)
     *         of the set as an integer.
     */
    public Long scard(final String key) {

        client.scard(key);
        return client.getIntegerReply();
    }

    /**
     * Return 1 if member is a member of the set stored at key, otherwise 0 is
     * returned.
     * <p/>
     * Time complexity O(1)
     *
     * @param key
     * @param member
     * @return Integer reply, specifically: 1 if the element is a member of the
     *         set 0 if the element is not a member of the set OR if the key
     *         does not exist
     */
    public Boolean sismember(final String key, final String member) {

        client.sismember(key, member);
        return client.getIntegerReply() == 1;
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
     * @param keys
     * @return Multi bulk reply, specifically the list of common elements.
     */
    public Set<String> sinter(final String... keys) {

        client.sinter(keys);
        final List<String> members = client.getMultiBulkReply();
        return new LinkedHashSet<String>(members);
    }

    /**
     * This commnad works exactly like {@link #sinter(String...) SINTER} but
     * instead of being returned the resulting set is sotred as dstkey.
     * <p/>
     * Time complexity O(N*M) worst case where N is the cardinality of the
     * smallest set and M the number of sets
     *
     * @param dstkey
     * @param keys
     * @return Status code reply
     */
    public Long sinterstore(final String dstkey, final String... keys) {

        client.sinterstore(dstkey, keys);
        return client.getIntegerReply();
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
     * @param keys
     * @return Multi bulk reply, specifically the list of common elements.
     */
    public Set<String> sunion(final String... keys) {

        client.sunion(keys);
        final List<String> members = client.getMultiBulkReply();
        return new LinkedHashSet<String>(members);
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
     * @param keys
     * @return Status code reply
     */
    public Long sunionstore(final String dstkey, final String... keys) {

        client.sunionstore(dstkey, keys);
        return client.getIntegerReply();
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
     * @param keys
     * @return Return the members of a set resulting from the difference between
     *         the first set provided and all the successive sets.
     */
    public Set<String> sdiff(final String... keys) {

        client.sdiff(keys);
        final List<String> members = client.getMultiBulkReply();
        return new LinkedHashSet<String>(members);
    }

    /**
     * This command works exactly like {@link #sdiff(String...) SDIFF} but
     * instead of being returned the resulting set is stored in dstkey.
     *
     * @param dstkey
     * @param keys
     * @return Status code reply
     */
    public Long sdiffstore(final String dstkey, final String... keys) {

        client.sdiffstore(dstkey, keys);
        return client.getIntegerReply();
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
     * @param key
     * @return Bulk reply
     */
    public String srandmember(final String key) {

        client.srandmember(key);
        return client.getBulkReply();
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
     * @param key
     * @param score
     * @param member
     * @return Integer reply, specifically: 1 if the new element was added 0 if
     *         the element was already a member of the sorted set and the score
     *         was updated
     */
    public Long zadd(final String key, final Number score, final String member) {

        client.zadd(key, score, member);
        return client.getIntegerReply();
    }

    public Set<String> zrange(final String key, final int start, final int end) {

        client.zrange(key, start, end);
        return new LinkedHashSet<String>(client.getMultiBulkReply());
    }

    /**
     * Remove the specified member from the sorted set value stored at key. If
     * member was not a member of the set no operation is performed. If key does
     * not not hold a set value an error is returned.
     * <p/>
     * Time complexity O(log(N)) with N being the number of elements in the
     * sorted set
     *
     * @param key
     * @param member
     * @return Integer reply, specifically: 1 if the new element was removed 0
     *         if the new element was not a member of the set
     */
    public Long zrem(final String key, final String member) {

        client.zrem(key, member);
        return client.getIntegerReply();
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
     * @param key
     * @param score
     * @param member
     * @return The new score
     */
    public Double zincrby(final String key, final double score,
                          final String member) {

        client.zincrby(key, score, member);
        String newscore = client.getBulkReply();
        return Double.valueOf(newscore);
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
     * @param key
     * @param member
     * @return Integer reply or a nil bulk reply, specifically: the rank of the
     *         element as an integer reply if the element exists. A nil bulk
     *         reply if there is no such element.
     * @see #zrevrank(String, String)
     */
    public Long zrank(final String key, final String member) {

        client.zrank(key, member);
        return client.getIntegerReply();
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
     * @param key
     * @param member
     * @return Integer reply or a nil bulk reply, specifically: the rank of the
     *         element as an integer reply if the element exists. A nil bulk
     *         reply if there is no such element.
     * @see #zrank(String, String)
     */
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

    /**
     * Return the sorted set cardinality (number of elements). If the key does
     * not exist 0 is returned, like for empty sorted sets.
     * <p/>
     * Time complexity O(1)
     *
     * @param key
     * @return the cardinality (number of elements) of the set as an integer.
     */
    public Long zcard(final String key) {

        client.zcard(key);
        return client.getIntegerReply();
    }

    /**
     * Return the score of the specified element of the sorted set at key. If
     * the specified element does not exist in the sorted set, or the key does
     * not exist at all, a special 'nil' value is returned.
     * <p/>
     * <b>Time complexity:</b> O(1)
     *
     * @param key
     * @param member
     * @return the score
     */
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
        for (String arg : keys) {
            args.add(arg);
        }
        args.add(String.valueOf(timeout));

        client.blpop(args.toArray(new String[args.size()]));
        client.setTimeoutInfinite();
        final List<String> multiBulkReply = client.getMultiBulkReply();
        client.rollbackTimeout();
        return multiBulkReply;
    }

    /**
     * Sort a Set or a List accordingly to the specified parameters and store
     * the result at dstkey.
     *
     * @param key
     * @param sortingParameters
     * @param dstkey
     * @return The number of elements of the list at dstkey.
     * @see #sort(String, SortingParams)
     * @see #sort(String)
     * @see #sort(String, String)
     */
    public Long sort(final String key, final SortingParams sortingParameters,
                     final String dstkey) {

        client.sort(key, sortingParameters, dstkey);
        return client.getIntegerReply();
    }

    /**
     * Sort a Set or a List and Store the Result at dstkey.
     * <p/>
     * Sort the elements contained in the List, Set, or Sorted Set value at key
     * and store the result at dstkey. By default sorting is numeric with
     * elements being compared as double precision floating point numbers. This
     * is the simplest form of SORT.
     *
     * @param key
     * @param dstkey
     * @return The number of elements of the list at dstkey.
     * @see #sort(String)
     * @see #sort(String, SortingParams)
     * @see #sort(String, SortingParams, String)
     */
    public Long sort(final String key, final String dstkey) {

        client.sort(key, dstkey);
        return client.getIntegerReply();
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
     * @param keys
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

        List<String> args = new ArrayList<String>();
        for (String arg : keys) {
            args.add(arg);
        }
        args.add(String.valueOf(timeout));

        client.brpop(args.toArray(new String[args.size()]));
        client.setTimeoutInfinite();
        List<String> multiBulkReply = client.getMultiBulkReply();
        client.rollbackTimeout();

        return multiBulkReply;
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
        client.zrangeByScore(key, min, max);
        return new LinkedHashSet<String>(client.getMultiBulkReply());
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
        client.zrangeByScore(key, min, max, offset, count);
        return new LinkedHashSet<String>(client.getMultiBulkReply());
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

        client.zrangeByScoreWithScores(key, min, max);
        return getReplyAsMap(client);
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

        client.zremrangeByRank(key, start, end);
        return client.getIntegerReply();
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
     * @param key
     * @param start
     * @param end
     * @return Integer reply, specifically the number of elements removed.
     */
    public Long zremrangeByScore(final String key, final double start,
                                 final double end) {

        client.zremrangeByScore(key, start, end);
        return client.getIntegerReply();
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
     * @see #zinterstore(String, String...)
     * @see #zinterstore(String, ZParams, String...)
     */
    public Long zunionstore(final String dstkey, final String... sets) {

        client.zunionstore(dstkey, sets);
        return client.getIntegerReply();
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
     * @see #zunionstore(String, ZParams, String...)
     * @see #zinterstore(String, String...)
     * @see #zinterstore(String, ZParams, String...)
     */
    public Long zunionstore(final String dstkey, final ZParams params,
                            final String... sets) {

        client.zunionstore(dstkey, params, sets);
        return client.getIntegerReply();
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
     * @see #zinterstore(String, String...)
     * @see #zinterstore(String, ZParams, String...)
     */
    public Long zinterstore(final String dstkey, final String... sets) {

        client.zinterstore(dstkey, sets);
        return client.getIntegerReply();
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
     * @see #zunionstore(String, ZParams, String...)
     * @see #zinterstore(String, String...)
     * @see #zinterstore(String, ZParams, String...)
     */
    public Long zinterstore(final String dstkey, final ZParams params,
                            final String... sets) {

        client.zinterstore(dstkey, params, sets);
        return client.getIntegerReply();
    }

    public Long strlen(final String key) {
        client.strlen(key);
        return client.getIntegerReply();
    }

    public Long lpushx(final String key, final String string) {
        client.lpushx(key, string);
        return client.getIntegerReply();
    }

    public boolean persist(final String key) {
        client.persist(key);
        return client.getIntegerReply() == 1;
    }

    public Long rpushx(final String key, final String string) {
        client.rpushx(key, string);
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

    /**
     * Pop a value from a list, push it to another list and return it; or block
     * until one is available
     *
     * @param source
     * @param destination
     * @param timeout
     * @return the element
     */
    public String brpoplpush(String source, String destination, int timeout) {
        client.brpoplpush(source, destination, timeout);
        return client.getBulkReply();
    }

    /**
     * Sets or clears the bit at offset in the string value stored at key
     *
     * @param key
     * @param offset
     * @param value
     * @return
     */
    public Long setBit(String key, int offset, String value) {
        client.setbit(key, offset, value);
        return client.getIntegerReply();
    }

    /**
     * Returns the bit value at offset in the string value stored at key
     *
     * @param key
     * @param offset
     * @return
     */
    public Long getBit(String key, int offset) {
        client.getbit(key, offset);
        return client.getIntegerReply();
    }


    public String ping() {

        client.ping();
        return client.getStatusCodeReply();

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

        client.zremrangeByScore(key, start, end);
        return client.getIntegerReply();
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

        client.save();
        return client.getStatusCodeReply();
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
        client.bgsave();
        return client.getStatusCodeReply();
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
        client.bgrewriteaof();
        return client.getStatusCodeReply();
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
        client.lastsave();
        return client.getIntegerReply();
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
        client.shutdown();
        String status = null;
        try {
            status = client.getStatusCodeReply();
        } catch (RedisException ex) {
            status = null;
        }
        return status;
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
        client.info();
        return client.getBulkReply();
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
        client.monitor();
        do {
            String command = client.getBulkReply();
            redisMonitor.onCommand(command);
        } while (client.isConnected());
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
        client.slaveof(host, port);
        return client.getStatusCodeReply();
    }

    public String slaveofNoOne() {
        client.slaveofNoOne();
        return client.getStatusCodeReply();
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
        client.configGet(pattern);
        return client.getMultiBulkReply();
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
