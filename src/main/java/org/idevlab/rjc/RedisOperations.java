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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Common interface for sharded and non-sharded Redis
 */
public interface RedisOperations {
    String set(String key, String value);

    String get(String key);

    /**
     * Time complexity: O(1)
     * <p/>
     * Determine if a key exists
     *
     * @param key a key
     * @return true if a key exists otherwise false
     */
    boolean exists(String key);

    /**
     * Time complexity: O(N) where N is the number of keys that will be removed.
     * When a key to remove holds a value other than a string, the individual complexity for
     * this key is O(M) where M is the number of elements in the list, set, sorted set or hash.
     * Removing a single key that holds a string value is O(1).
     * <p/>
     * Removes the specified keys. A key is ignored if it does not exist.
     *
     * @param keys keys
     * @return The number of keys that were removed.
     */
    Long del(String... keys);

    /**
     * Time complexity: O(1)
     * <p/>
     * Returns the string representation of the type of the value stored at key.
     * The different types that can be returned are: string, list, set, zset and hash.
     *
     * @param key a key
     * @return type of key, or none when key does not exist.
     */
    String type(String key);

    /**
     * Time complexity: O(1)
     * <p/>
     * Set a timeout on key. After the timeout has expired, the key will automatically be deleted.
     * A key with an associated timeout is said to be volatile in Redis terminology.
     * <p/>
     * For Redis versions < 2.1.3, existing timeouts cannot be overwritten.
     * So, if key already has an associated timeout, it will do nothing and return 0.
     * Since Redis 2.1.3, you can update the timeout of a key.
     * It is also possible to remove the timeout using the PERSIST command.
     * See the page on key expiry for more information.
     *
     * @param key     a key
     * @param seconds key timeout
     * @return true if the timeout was set otherwise false
     */
    boolean expire(String key, int seconds);

    /**
     * Time complexity: O(1)
     * <p/>
     * Set a timeout on key. After the timeout has expired, the key will automatically be deleted.
     * A key with an associated timeout is said to be volatile in Redis terminology.
     * EXPIREAT has the same effect and semantic as EXPIRE, but instead of specifying the number of seconds
     * representing the TTL (time to live), it takes an absolute UNIX timestamp (seconds since January 1, 1970).
     * <p/>
     * <h5>Background</h5>
     * EXPIREAT was introduced in order to convert relative timeouts to absolute timeouts for the AOF persistence mode. Of course, it can be used directly to specify that a given key should expire at a given time in the future.
     *
     * @param key      a key
     * @param unixTime an absolute UNIX timestamp (seconds since January 1, 1970)
     * @return true if the timeout was set otherwise false
     */
    boolean expireAt(String key, long unixTime);

    /**
     * Time complexity: O(1)
     * <p/>
     * Returns the remaining time to live of a key that has a timeout.
     * This introspection capability allows a Redis client to check how many seconds
     * a given key will continue to be part of the dataset.
     *
     * @param key a key
     * @return TTL in seconds or -1 when key does not exist or does not have a timeout.
     */
    Long ttl(String key);

    String getSet(String key, String value);

    Long setnx(String key, String value);

    String setex(String key, int seconds, String value);

    Long decrBy(String key, int integer);

    Long decr(String key);

    Long incrBy(String key, int integer);

    Long incr(String key);

    Long append(String key, String value);

    String substr(String key, int start, int end);

    /**
     * Time complexity: O(N) with N being the number of keys in the database, under the assumption that
     * the key names in the database and the given pattern have limited length.
     * <p/>
     * Returns all keys matching pattern.
     * <p/>
     * While the time complexity for this operation is O(N), the constant times are fairly low.
     * For example, Redis running on an entry level laptop can scan a 1 million key database in 40 milliseconds.
     * <p/>
     * Warning: consider KEYS as a command that should only be used in production environments with extreme care.
     * It may ruin performance when it is executed against large databases.
     * This command is intended for debugging and special operations, such as changing your keyspace layout.
     * Don't use KEYS in your regular application code. If you're looking for a way to find keys
     * in a subset of your keyspace, consider using sets.
     * <p/>
     * Supported glob-style patterns:
     * <ul>
     * <li>h?llo matches hello, hallo and hxllo</li>
     * <li>h*llo matches hllo and heeeello</li>
     * <li>h[ae]llo matches hello and hallo, but not hillo</li>
     * </ul>
     * Use \ to escape special characters if you want to match them verbatim.
     *
     * @param pattern pattern
     * @return list of keys matching pattern.
     */
    Set<String> keys(String pattern);

    /**
     * Time complexity: O(1)
     * <p/>
     * Remove the existing timeout on key.
     *
     * @param key a key
     * @return true if the timeout was removed otherwise false
     */
    boolean persist(final String key);

    Long hset(String key, String field, String value);

    String hget(String key, String field);

    Long hsetnx(String key, String field, String value);

    String hmset(String key, Map<String, String> hash);

    List<String> hmget(String key, String... fields);

    Long hincrBy(String key, String field, int value);

    Boolean hexists(String key, String field);

    Long hdel(String key, String field);

    Long hlen(String key);

    Set<String> hkeys(String key);

    Collection<String> hvals(String key);

    Map<String, String> hgetAll(String key);

    Long rpush(String key, String string);

    Long rpushx(final String key, final String string);

    Long lpush(String key, String string);

    Long lpushx(final String key, final String string);

    Long llen(String key);

    List<String> lrange(String key, int start, int end);

    String ltrim(String key, int start, int end);

    String lindex(String key, int index);

    String lset(String key, int index, String value);

    Long lrem(String key, int count, String value);

    String lpop(String key);

    String rpop(String key);

    Long sadd(String key, String member);

    Set<String> smembers(String key);

    Long srem(String key, String member);

    String spop(String key);

    Long scard(String key);

    Boolean sismember(String key, String member);

    String srandmember(String key);

    Long zadd(String key, Number score, String member);

    Set<String> zrange(String key, int start, int end);

    Long zrem(String key, String member);

    String zincrby(String key, Number score, String member);

    Long zrank(String key, String member);

    Long zrevrank(String key, String member);

    Set<String> zrevrange(String key, int start, int end);

    Map<String, String> zrangeWithScores(String key, int start, int end);

    Map<String, String> zrevrangeWithScores(String key, int start, int end);

    Long zcard(String key);

    String zscore(String key, String member);

    /**
     * Time complexity: O(N*log(N)) where N is the number of elements returned.
     * When the elements are not sorted, complexity is O(N).
     * <p/>
     * Returns the elements contained in the list, set or sorted set at key.
     * By default, sorting is numeric and elements are compared by their value
     * interpreted as double precision floating point number.
     *
     * @param key a key
     * @return list of sorted elements.
     */
    List<String> sort(String key);

    /**
     * Time complexity: O(N*log(N)) where N is the number of elements returned.
     * When the elements are not sorted, complexity is O(N).
     * <p/>
     * Returns the elements contained in the list, set or sorted set at key.
     * By default, sorting is numeric and elements are compared by their value
     * interpreted as double precision floating point number.
     *
     * @param key               a key
     * @param sortingParameters sorting parameters
     * @return list of sorted elements.
     * @see <a href="http://redis.io/commands/sort">SORT command</a>
     */
    List<String> sort(String key, SortingParams sortingParameters);

    Long zcount(String key, Number min, Number max);

    Set<String> zrangeByScore(String key, String min, String max);

    Set<String> zrangeByScore(String key, String min, String max, int offset, int count);

    Map<String, String> zrangeByScoreWithScores(String key, String min, String max);

    Map<String, String> zrangeByScoreWithScores(String key, String min, String max, int offset, int count);

    Long zremrangeByRank(String key, int start, int end);

    Long zremrangeByScore(String key, Number start, Number end);

    Long linsert(String key, Client.LIST_POSITION where, String pivot, String value);

    Long publish(String channel, String message);

    /**
     * @since 2.1.8.
     */
    Long getBit(String key, int offset);

    /**
     * @since 2.1.8.
     */
    Long setBit(String key, int offset, String value);
}
