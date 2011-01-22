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
    /**
     * Time complexity: O(1)
     * <p/>
     * Set key to hold the string value. If key already holds a value, it is overwritten, regardless of its type.
     *
     * @param key   the key
     * @param value the value
     * @return always OK since SET can't fail
     */
    String set(String key, String value);

    /**
     * Time complexity: O(1)
     * <p/>
     * Get the value of key. If the key does not exist the special value nil is returned.
     * An error is returned if the value stored at key is not a string, because GET only handles string values.
     *
     * @param key the key
     * @return the value of key, or null when key does not exist.
     */
    String get(String key);

    /**
     * Time complexity: O(1)
     * <p/>
     * Determine if a key exists
     *
     * @param key the key
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
     * @param key the key
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
     * @param key     the key
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
     * EXPIREAT was introduced in order to convert relative timeouts to absolute timeouts for the AOF persistence mode.
     * Of course, it can be used directly to specify that a given key should expire at a given time in the future.
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
     * @param key the key
     * @return TTL in seconds or -1 when key does not exist or does not have a timeout.
     */
    Long ttl(String key);

    /**
     * Time complexity: O(1)
     * <p/>
     * Atomically sets key to value and returns the old value stored at key.
     * Returns an error when key exists but does not hold a string value.
     *
     * @param key   the key
     * @param value the value
     * @return the old value stored at key, or null when key did not exist.
     */
    String getSet(String key, String value);

    /**
     * Time complexity: O(1)
     * <p/>
     * Set key to hold string value if key does not exist. In that case, it is equal to SET.
     * When key already holds a value, no operation is performed. SETNX is short for "SET if Not eXists".
     *
     * @param key   the key
     * @param value the value
     * @return true if the key was set otherwise false
     */
    boolean setnx(String key, String value);

    /**
     * Time complexity: O(1)
     * Set key to hold the string value and set key to timeout after a given number of seconds.
     * This command is equivalent to executing the following commands:
     * <div>
     * SET key value<br/>
     * EXPIRE key seconds
     * </div>
     * <p/>
     * SETEX is atomic, and can be reproduced by using the previous two commands inside an MULTI/EXEC block.
     * It is provided as a faster alternative to the given sequence of operations,
     * because this operation is very common when Redis is used as a cache.
     * <p/>
     * An error is returned when seconds is invalid.
     *
     * @param key     the key
     * @param seconds ttl in seconds
     * @param value   the value
     * @return Status code reply
     */
    String setex(String key, int seconds, String value);

    /**
     * Time complexity: O(1)
     * <p/>
     * Decrements the number stored at key by decrement. If the key does not exist or contains a value of
     * the wrong type, it is set to 0 before performing the operation.
     * This operation is limited to 64 bit signed integers.
     * <p/>
     * See INCR for extra information on increment/decrement operations.
     *
     * @param key   the key
     * @param value decrement value
     * @return the value of key after the decrement
     */
    Long decrBy(String key, int value);

    /**
     * Time complexity: O(1)
     * <p/>
     * Decrements the number stored at key by one. If the key does not exist or contains a value of the wrong type,
     * it is set to 0 before performing the operation. This operation is limited to 64 bit signed integers.
     * <p/>
     * See INCR for extra information on increment/decrement operations.
     *
     * @param key the key
     * @return the value of key after the decrement
     */
    Long decr(String key);

    /**
     * Time complexity: O(1)
     * Increments the number stored at key by increment. If the key does not exist or contains
     * a value of the wrong type, it is set to 0 before performing the operation.
     * This operation is limited to 64 bit signed integers.
     * See INCR for extra information on increment/decrement operations.
     *
     * @param key   the key
     * @param value increment value
     * @return the value of key after the increment
     */
    Long incrBy(String key, int value);

    /**
     * Time complexity: O(1)
     * Increments the number stored at key by one. If the key does not exist or contains a value
     * of the wrong type, it is set to 0 before performing the operation.
     * This operation is limited to 64 bit signed integers.
     * <p/>
     * Note: this is a string operation because Redis does not have a dedicated integer type.
     * The the string stored at the key is interpreted as a base-10 64 bit signed integer to execute the operation.
     * Redis stores integers in their integer representation, so for string values that actually hold an integer, there is no overhead for storing the string representation of the integer.
     *
     * @param key the value
     * @return the value of key after the increment
     */
    Long incr(String key);

    /**
     * Time complexity:
     * O(1). The amortized time complexity is O(1) assuming the appended value is small and the already present
     * value is of any size, since the dynamic string library used by Redis will double the free space
     * available on every reallocation.
     * <p/>
     * If key already exists and is a string, this command appends the value at the end of the string.
     * If key does not exist it is created and set as an empty string, so APPEND will be similar to
     * SET in this special case.
     *
     * @param key   the key
     * @param value the value
     * @return the length of the string after the append operation
     */
    Long append(String key, String value);

    /**
     * Time complexity O(N) where N is the length of the returned string. The complexity is ultimately determined
     * by the returned length, but because creating a substring from an existing string is very cheap,
     * it can be considered O(1) for small strings.
     * <p/>
     * Warning: this command was renamed to GETRANGE, it is called SUBSTR in Redis versions <= 2.0.
     * <p/>
     * Returns the substring of the string value stored at key, determined by the offsets start
     * and end (both are inclusive). Negative offsets can be used in order to provide an offset starting
     * from the end of the string. So -1 means the last character, -2 the penultimate and so forth.
     * The function handles out of range requests by limiting the resulting range to the actual length of the string.
     *
     * @param key   the key
     * @param start offset start
     * @param end   offset end
     * @return substring of the string value stored at key
     */
    String getRange(String key, int start, int end);

    /**
     * Time complexity: O(1), not counting the time taken to copy the new string in place.
     * Usually, this string is very small so the amortized complexity is O(1).
     * Otherwise, complexity is O(M) with M being the length of the value argument.
     * <p/>
     * Overwrites part of the string stored at key, starting at the specified offset, for the entire length of value. If the offset is larger than the current length of the string at key, the string is padded with zero-bytes to make offset fit. Non-existing keys are considered as empty strings, so this command will make sure it holds a string large enough to be able to set value at offset.
     * <p/>
     * Note that the maximum offset that you can set is 229 -1 (536870911), as Redis Strings are limited to 512 megabytes. If you need to grow beyond this size, you can use multiple keys.
     * <p/>
     * Warning: When setting the last possible byte and the string value stored at key does not yet hold a string value, or holds a small string value, Redis needs to allocate all intermediate memory which can block the server for some time. On a 2010 Macbook Pro, setting byte number 536870911 (512MB allocation) takes ~300ms, setting byte number 134217728 (128MB allocation) takes ~80ms, setting bit number 33554432 (32MB allocation) takes ~30ms and setting bit number 8388608 (8MB allocation) takes ~8ms. Note that once this first allocation is done, subsequent calls to SETRANGE for the same key will not have the allocation overhead.
     * <h5>Patterns</h5>
     * Thanks to SETRANGE and the analogous GETRANGE commands, you can use Redis strings as a linear
     * array with O(1) random access. This is a very fast and efficient storage in many real world use cases.
     *
     * @param key    the key
     * @param offset the offset
     * @param value  the value
     * @return the length of the string after it was modified by the command.
     * @since 2.1.8
     */
    Long setRange(String key, int offset, String value);

    /**
     * Time complexity: O(1)
     * <p/>
     * Returns the length of the string value stored at key. An error is returned when key holds a non-string value.
     *
     * @param key the key
     * @return the length of the string at key, or 0 when key does not exist.
     * @since 2.1.2
     */
    Long strlen(String key);

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
     * @param key the key
     * @return true if the timeout was removed otherwise false
     * @since Redis 2.1.2
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
     * @param key the key
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
     * Time complexity: O(1)
     * <p/>
     * Returns the bit value at offset in the string value stored at key.
     * When offset is beyond the string length, the string is assumed to be a contiguous space with 0 bits.
     * When key does not exist it is assumed to be an empty string, so offset is always out of
     * range and the value is also assumed to be a contiguous space with 0 bits.
     *
     * @param key    the key
     * @param offset offset in the string value stored at key
     * @return the bit value stored at offset.
     * @since 2.1.8.
     */
    Long getBit(String key, int offset);

    /**
     * Time complexity: O(1)
     * <p/>
     * Sets or clears the bit at offset in the string value stored at key.
     * <p/>
     * The bit is either set or cleared depending on value, which can be either 0 or 1.
     * When key does not exist, a new string value is created. The string is grown to make sure
     * it can hold a bit at offset. The offset argument is required to be greater than or equal to 0,
     * and smaller than 232 (this limits bitmaps to 512MB).
     * When the string at key is grown, added bits are set to 0.
     * <p/>
     * Warning: When setting the last possible bit (offset equal to 232 -1) and the string value stored at key does not yet hold a string value, or holds a small string value, Redis needs to allocate all intermediate memory which can block the server for some time. On a 2010 Macbook Pro, setting bit number 232 -1 (512MB allocation) takes ~300ms, setting bit number 230 -1 (128MB allocation) takes ~80ms, setting bit number 228 -1 (32MB allocation) takes ~30ms and setting bit number 226 -1 (8MB allocation) takes ~8ms. Note that once this first allocation is done, subsequent calls to SETBIT for the same key will not have the allocation overhead.
     *
     * @param key    the key
     * @param offset the offset
     * @param value  the value, must be 0 or 1
     * @return the original bit value stored at offset.
     * @since 2.1.8.
     */
    Long setBit(String key, int offset, String value);
}
