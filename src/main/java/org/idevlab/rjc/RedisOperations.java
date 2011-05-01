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

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Common interface for sharded and non-sharded Redis
 */
public interface RedisOperations {
    /**
     * <h4>Time complexity</h4> O(1)
     * <p/>
     * Set key to hold the string value. If key already holds a value, it is overwritten, regardless of its type.
     *
     * @param key   the key
     * @param value the value
     * @return always OK since SET can't fail
     */
    String set(String key, String value);

    /**
     * <h4>Time complexity</h4> O(1)
     * <p/>
     * Get the value of key. If the key does not exist the special value nil is returned.
     * An error is returned if the value stored at key is not a string, because GET only handles string values.
     *
     * @param key the key
     * @return the value of key, or null when key does not exist.
     */
    String get(String key);

    /**
     * <h4>Time complexity</h4> O(1)
     * <p/>
     * Determine if a key exists
     *
     * @param key the key
     * @return true if a key exists otherwise false
     */
    Boolean exists(String key);

    /**
     * <h4>Time complexity</h4> O(N) where N is the number of keys that will be removed.
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
     * <h4>Time complexity</h4> O(1)
     * <p/>
     * Returns the string representation of the type of the value stored at key.
     * The different types that can be returned are: string, list, set, zset and hash.
     *
     * @param key the key
     * @return type of key, or none when key does not exist.
     */
    String type(String key);

    /**
     * <h4>Time complexity</h4> O(1)
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
    Boolean expire(String key, int seconds);

    /**
     * <h4>Time complexity</h4> O(1)
     * <p/>
     * Set a timeout on key. After the timeout has expired, the key will automatically be deleted.
     * A key with an associated timeout is said to be volatile in Redis terminology.
     * EXPIREAT has the same effect and semantic as EXPIRE, but instead of specifying the number of seconds
     * representing the TTL (time to live), it takes an absolute UNIX timestamp (seconds since January 1, 1970).
     * <p/>
     * <h4>Background</h4>
     * EXPIREAT was introduced in order to convert relative timeouts to absolute timeouts for the AOF persistence mode.
     * Of course, it can be used directly to specify that a given key should expire at a given time in the future.
     *
     * @param key      a key
     * @param unixTime an absolute UNIX timestamp (seconds since January 1, 1970)
     * @return true if the timeout was set otherwise false
     */
    Boolean expireAt(String key, long unixTime);

    /**
     * <h4>Time complexity</h4> O(1)
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
     * <h4>Time complexity</h4> O(1)
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
     * <h4>Time complexity</h4> O(1)
     * <p/>
     * Set key to hold string value if key does not exist. In that case, it is equal to SET.
     * When key already holds a value, no operation is performed. SETNX is short for "SET if Not eXists".
     *
     * @param key   the key
     * @param value the value
     * @return true if the key was set otherwise false
     */
    Boolean setnx(String key, String value);

    /**
     * <h4>Time complexity</h4> O(1)
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
     * <h4>Time complexity</h4> O(1)
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
     * <h4>Time complexity</h4> O(1)
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
     * <h4>Time complexity</h4> O(1)
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
     * <h4>Time complexity</h4> O(1)
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
     * <h4>Time complexity</h4>
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
     * <h4>Time complexity</h4> O(N) where N is the length of the returned string. The complexity is ultimately determined
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
     * <h4>Time complexity</h4> O(1), not counting the time taken to copy the new string in place.
     * Usually, this string is very small so the amortized complexity is O(1).
     * Otherwise, complexity is O(M) with M being the length of the value argument.
     * <p/>
     * Overwrites part of the string stored at key, starting at the specified offset, for the entire length of value. If the offset is larger than the current length of the string at key, the string is padded with zero-bytes to make offset fit. Non-existing keys are considered as empty strings, so this command will make sure it holds a string large enough to be able to set value at offset.
     * <p/>
     * Note that the maximum offset that you can set is 229 -1 (536870911), as Redis Strings are limited to 512 megabytes. If you need to grow beyond this size, you can use multiple keys.
     * <p/>
     * Warning: When setting the last possible byte and the string value stored at key does not yet hold a string value, or holds a small string value, Redis needs to allocate all intermediate memory which can block the server for some time. On a 2010 Macbook Pro, setting byte number 536870911 (512MB allocation) takes ~300ms, setting byte number 134217728 (128MB allocation) takes ~80ms, setting bit number 33554432 (32MB allocation) takes ~30ms and setting bit number 8388608 (8MB allocation) takes ~8ms. Note that once this first allocation is done, subsequent calls to SETRANGE for the same key will not have the allocation overhead.
     * <h4>Patterns</h4>
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
     * <h4>Time complexity</h4> O(1)
     * <p/>
     * Returns the length of the string value stored at key. An error is returned when key holds a non-string value.
     *
     * @param key the key
     * @return the length of the string at key, or 0 when key does not exist.
     * @since 2.1.2
     */
    Long strlen(String key);

    /**
     * <h4>Time complexity</h4> O(N) with N being the number of keys in the database, under the assumption that
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
     * <h4>Time complexity</h4> O(1)
     * <p/>
     * Remove the existing timeout on key.
     *
     * @param key the key
     * @return true if the timeout was removed otherwise false
     * @since Redis 2.1.2
     */
    Boolean persist(final String key);

    /**
     * <h4>Time complexity</h4>
     * <p/>
     * O(1)
     * <p/>
     * Sets field in the hash stored at key to value.
     * If key does not exist, a new key holding a hash is created.
     * If field already exists in the hash, it is overwritten.
     *
     * @param key   the key
     * @param field the field
     * @param value the value
     * @return true if field is a new field in the hash and value was set or false if field already exists in the hash and the value was updated.
     */
    Boolean hset(String key, String field, String value);

    /**
     * <h4>Time complexity</h4> O(1)
     * <p/>
     * Returns the value associated with field in the hash stored at key.
     *
     * @param key   the key
     * @param field the field
     * @return the value associated with field, or null when field is not present in the hash or key does not exist.
     */
    String hget(String key, String field);

    /**
     * <h4>Time complexity</h4>
     * <p/>
     * O(1)
     * <p/>
     * Sets field in the hash stored at key to value, only if field does not yet exist.
     * If key does not exist, a new key holding a hash is created.
     * If field already exists, this operation has no effect.
     *
     * @param key   the key
     * @param field the field
     * @param value the value
     * @return true if field is a new field in the hash and value was set or
     *         false if field already exists in the hash and no operation was performed
     */
    Boolean hsetnx(String key, String field, String value);

    /**
     * <h4>Time complexity</h4>
     * <p/>
     * O(N) where N is the number of fields being set.
     * <p/>
     * Sets the specified fields to their respective values in the hash stored at key.
     * This command overwrites any existing fields in the hash. If key does not exist, a new key holding a hash is created.
     *
     * @param key  the key
     * @param hash the hash
     * @return Status code reply
     */
    String hmset(String key, Map<String, String> hash);

    /**
     * <h4>Time complexity</h4>
     * <p/>
     * O(N) where N is the number of fields being requested.
     * <p/>
     * Returns the values associated with the specified fields in the hash stored at key.
     * <p/>
     * For every field that does not exist in the hash, a nil value is returned.
     * Because a non-existing keys are treated as empty hashes,
     * running HMGET against a non-existing key will return a list of nil values.
     *
     * @param key    the key
     * @param fields the fields
     * @return list of values associated with the given fields, in the same order as they are requested.
     */
    List<String> hmget(String key, String... fields);

    /**
     * <h4>Time complexity</h4> O(1)
     * <p/>
     * Increments the number stored at field in the hash stored at key by increment.
     * If key does not exist, a new key holding a hash is created.
     * If field does not exist or holds a string that cannot be interpreted as integer,
     * the value is set to 0 before the operation is performed.
     * <p/>
     * The range of values supported by HINCRBY is limited to 64 bit signed integers.
     *
     * @param key   the key
     * @param field the field
     * @param value the value
     * @return the value at field after the increment operation
     */
    Long hincrBy(String key, String field, int value);

    /**
     * <h4>Time complexity</h4> O(1)
     * Returns if field is an existing field in the hash stored at key.
     *
     * @param key   the key
     * @param field the field
     * @return true if the hash contains field or false if the hash does not contain field, or key does not exist.
     */
    Boolean hexists(String key, String field);

    /**
     * <h4>Time complexity</h4> O(1)
     * <p/>
     * Removes field from the hash stored at key.
     *
     * @param key   the key
     * @param field the field
     * @return true if field was present in the hash and is now removed or
     *         false if field does not exist in the hash, or key does not exist.
     */
    Boolean hdel(String key, String field);

    /**
     * <h4>Time complexity</h4>
     * <p/>
     * O(1)
     * <p/>
     * Returns the number of fields contained in the hash stored at key.
     *
     * @param key the key
     * @return number of fields in the hash, or 0 when key does not exist.
     */
    Long hlen(String key);

    /**
     * <h4>Time complexity</h4>
     * <p/>
     * O(N) where N is the size of the hash.
     * <p/>
     * Returns all field names of the hash stored at key.
     *
     * @param key the key
     * @return list of fields in the hash, or an empty list when key does not exist.
     */
    Set<String> hkeys(String key);

    /**
     * <h4>Time complexity</h4>
     * <p/>
     * O(N) where N is the size of the hash.
     * <p/>
     * Returns all values of the hash stored at key.
     *
     * @param key the key
     * @return list of values in the hash, or an empty list when key does not exist.
     */
    List<String> hvals(String key);

    /**
     * <h4>Time complexity</h4> O(N) where N is the size of the hash.
     * <p/>
     * Returns all fields and values of the hash stored at key. In the returned value,
     * every field name is followed by its value, so the length of the reply is twice the size of the hash.
     *
     * @param key the key
     * @return map of fields and their values stored in the hash, or an empty map when key does not exist
     */
    Map<String, String> hgetAll(String key);

    /**
     * <h4>Time complexity</h4>
     * <p/>
     * O(1)
     * <p/>
     * Inserts value at the tail of the list stored at key.
     * If key does not exist, it is created as empty list before performing the push operation.
     * When key holds a value that is not a list, an error is returned.
     *
     * @param key   the key
     * @param value the value
     * @return the length of the list after the push operation.
     */
    Long rpush(String key, String value);

    /**
     * <h4>Time complexity</h4>
     * <p/>
     * O(1)
     * <p/>
     * Inserts value at the tail of the list stored at key, only if key already exists and holds a list.
     * In contrary to RPUSH, no operation will be performed when key does not yet exist.
     *
     * @param key   the key
     * @param value the value
     * @return the length of the list after the push operation.
     */
    Long rpushx(final String key, final String value);

    /**
     * <h4>Time complexity</h4>
     * <p/>
     * O(1)
     * <p/>
     * Inserts value at the head of the list stored at key.
     * If key does not exist, it is created as empty list before performing the push operation.
     * When key holds a value that is not a list, an error is returned.
     *
     * @param key   the key
     * @param value the value
     * @return the length of the list after the push operation.
     */
    Long lpush(String key, String value);

    /**
     * <h4>Time complexity</h4>
     * <p/>
     * O(1)
     * <p/>
     * Inserts value at the head of the list stored at key, only if key already exists and holds a list.
     * In contrary to LPUSH, no operation will be performed when key does not yet exist.
     *
     * @param key   the key
     * @param value the value
     * @return the length of the list after the push operation.
     */
    Long lpushx(String key, String value);

    /**
     * <h4>Time complexity</h4>
     * <p/>
     * O(1)
     * <p/>
     * Returns the length of the list stored at key. If key does not exist,
     * it is interpreted as an empty list and 0 is returned.
     * An error is returned when the value stored at key is not a list.
     *
     * @param key the key
     * @return the length of the list at key.
     */
    Long llen(String key);

    /**
     * <h4>Time complexity</h4>
     * <p/>
     * O(S+N) where S is the start offset and N is the number of elements in the specified range.
     * <p/>
     * Returns the specified elements of the list stored at key.
     * The offsets start and end are zero-based indexes,
     * with 0 being the first element of the list (the head of the list), 1 being the next element and so on.
     * <p/>
     * These offsets can also be negative numbers indicating offsets starting at the end of the list.
     * For example, -1 is the last element of the list, -2 the penultimate, and so on.
     * <h4>Consistency with range functions in various programming languages</h4>
     * <p/>
     * Note that if you have a list of numbers from 0 to 100, LRANGE list 0 10 will return 11 elements,
     * that is, the rightmost item is included. This may or may not be consistent with behavior
     * of range-related functions in your programming language of choice
     * (think Ruby's Range.new, Array#slice or Python's range() function).
     * <h4>Out-of-range indexes</h4>
     * <p/>
     * Out of range indexes will not produce an error.
     * If start is larger than the end of the list, or start > end, an empty list is returned.
     * If end is larger than the actual end of the list, Redis will treat it like the last element of the list.
     *
     * @param key   the key
     * @param start range start
     * @param end   range end
     * @return list of elements in the specified range.
     */
    List<String> lrange(String key, int start, int end);

    /**
     * <h4>Time complexity</h4>
     * <p/>
     * O(N) where N is the number of elements to be removed by the operation.
     * <p/>
     * Trim an existing list so that it will contain only the specified range of elements specified.
     * Both start and stop are zero-based indexes, where 0 is the first element of the list (the head),
     * 1 the next element and so on.
     * <p/>
     * For example: LTRIM foobar 0 2 will modify the list stored at foobar so
     * that only the first three elements of the list will remain.
     * <p/>
     * start and end can also be negative numbers indicating offsets from the end of the list,
     * where -1 is the last element of the list, -2 the penultimate element and so on.
     * <p/>
     * Out of range indexes will not produce an error: if start is larger than the end of the list,
     * or start > end, the result will be an empty list (which causes key to be removed).
     * If end is larger than the end of the list, Redis will treat it like the last element of the list.
     * <p/>
     * A common use of LTRIM is together with LPUSH/RPUSH. For example:
     * <p/>
     * LPUSH mylist someelement
     * LTRIM mylist 0 99
     * <p/>
     * This pair of commands will push a new element on the list,
     * while making sure that the list will not grow larger than 100 elements.
     * This is very useful when using Redis to store logs for example.
     * It is important to note that when used in this way LTRIM is an O(1) operation
     * because in the average case just one element is removed from the tail of the list.
     *
     * @param key   the key
     * @param start range start
     * @param end   range end
     * @return Status code reply
     */
    String ltrim(String key, int start, int end);

    /**
     * <h4>Time complexity</h4>
     * <p/>
     * O(N) where N is the number of elements to traverse to get to the element at index.
     * This makes asking for the first or the last element of the list O(1).
     * <p/>
     * Returns the element at index index in the list stored at key.
     * The index is zero-based, so 0 means the first element, 1 the second element and so on.
     * Negative indices can be used to designate elements starting at the tail of the list.
     * Here, -1 means the last element, -2 means the penultimate and so forth.
     * <p/>
     * When the value at key is not a list, an error is returned.
     *
     * @param key   the key
     * @param index the index
     * @return the requested element, or null when index is out of range.
     */
    String lindex(String key, int index);

    /**
     * <h4>Time complexity</h4>
     * <p/>
     * O(N) where N is the length of the list. Setting either the first or the last element of the list is O(1).
     * <p/>
     * Sets the list element at index to value. For more information on the index argument, see LINDEX.
     * <p/>
     * An error is returned for out of range indexes.
     *
     * @param key   the key
     * @param index the index
     * @param value the value
     * @return Status code reply
     */
    String lset(String key, int index, String value);

    /**
     * <h4>Time complexity</h4>
     * <p/>
     * O(N) where N is the length of the list.
     * <p/>
     * Removes the first count occurrences of elements equal to value from the list stored at key.
     * The count argument influences the operation in the following ways:
     * <ul>
     * <li>count > 0: Remove elements equal to value moving from head to tail.</li>
     * <li>count < 0: Remove elements equal to value moving from tail to head.</li>
     * <li>count = 0: Remove all elements equal to value.</li>
     * </ul>
     * For example, LREM list -2 "hello" will remove the last two occurrences of "hello" in the list stored at list.
     * <p/>
     * Note that non-existing keys are treated like empty lists, so when key does not exist, the command will always return 0.
     *
     * @param key   the key
     * @param count the count
     * @param value the value
     * @return the number of removed elements.
     */
    Long lrem(String key, int count, String value);

    /**
     * <h4>Time complexity</h4>
     * <p/>
     * O(1)
     * <p/>
     * Removes and returns the first element of the list stored at key.
     *
     * @param key the key
     * @return the value of the first element, or null when key does not exist.
     */
    String lpop(String key);

    /**
     * <h4>Time complexity</h4>
     * <p/>
     * O(1)
     * <p/>
     * Removes and returns the last element of the list stored at key.
     *
     * @param key the key
     * @return the value of the last element, or nil when key does not exist.
     */
    String rpop(String key);

    /**
     * <h4>Time complexity</h4>
     * <p/>
     * O(1)
     * <p/>
     * Add member to the set stored at key. If member is already a member of this set, no operation is performed.
     * If key does not exist, a new set is created with member as its sole member.
     * <p/>
     * An error is returned when the value stored at key is not a set.
     *
     * @param key    the key
     * @param member the member
     * @return true if the element was added or false if the element was already a member of the set
     */
    Boolean sadd(String key, String member);

    /**
     * <h4>Time complexity</h4>
     * <p/>
     * O(N) where N is the set cardinality.
     * <p/>
     * Returns all the members of the set value stored at key.
     * <p/>
     * This has the same effect as running SINTER with one argument key.
     *
     * @param key the key
     * @return all elements of the set.
     */
    Set<String> smembers(String key);

    /**
     * <h4>Time complexity</h4>
     * <p/>
     * O(1)
     * <p/>
     * Remove member from the set stored at key. If member is not a member of this set, no operation is performed.
     * <p/>
     * An error is returned when the value stored at key is not a set.
     *
     * @param key    the key
     * @param member the member
     * @return true if the element was removed or false if the element was not a member of the set.
     */
    Boolean srem(String key, String member);

    /**
     * <h4>Time complexity</h4>
     * <p/>
     * O(1)
     * <p/>
     * Removes and returns a random element from the set value stored at key.
     * <p/>
     * This operation is similar to SRANDMEMBER, that returns a random element from a set but does not remove it.
     *
     * @param key the key
     * @return the removed element, or null when key does not exist.
     */
    String spop(String key);

    /**
     * <h4>Time complexity</h4>
     * <p/>
     * O(1)
     * <p/>
     * Returns the set cardinality (number of elements) of the set stored at key.
     *
     * @param key the key
     * @return the cardinality (number of elements) of the set, or 0 if key does not exist.
     */
    Long scard(String key);

    /**
     * <h4>Time complexity</h4>
     * <p/>
     * O(1)
     * <p/>
     * Returns if member is a member of the set stored at key.
     *
     * @param key    the key
     * @param member the member
     * @return if the element is a member of the set or false if the element is not a member of the set, or if key does not exist.
     */
    Boolean sismember(String key, String member);

    /**
     * <h4>Time complexity</h4>
     * <p/>
     * O(1)
     * <p/>
     * Returns a random element from the set value stored at key.
     * <p/>
     * This operation is similar to SPOP, that also removes the randomly selected element.
     *
     * @param key the key
     * @return the randomly selected element, or null when key does not exist.
     */
    String srandmember(String key);

    /**
     * Time complexity
     * <p/>
     * O(log(N)) where N is the number of elements in the sorted set.
     * <p/>
     * Adds the member with the specified score to the sorted set stored at key.
     * If member is already a member of the sorted set, the score is updated and the element
     * reinserted at the right position to ensure the correct ordering.
     * If key does not exist, a new sorted set with the specified member as sole member is created.
     * If the key exists but does not hold a sorted set, an error is returned.
     * <p/>
     * The score value should be the string representation of a numeric value,
     * and accepts double precision floating point numbers.
     * <p/>
     * For an introduction to sorted sets, see the data types page on sorted sets.
     *
     * @param key    the key
     * @param score  the score
     * @param member the member
     * @return true if the element was added or false if the element was already a member of the sorted set and the score was updated.
     */
    Boolean zadd(String key, Number score, String member);

    /**
     * Time complexity
     * <p/>
     * O(log(N)+M) with N being the number of elements in the sorted set and M the number of elements returned.
     * <p/>
     * Returns the specified range of elements in the sorted set stored at key.
     * The elements are considered to be ordered from the lowest to the highest score.
     * <p/>
     * See ZREVRANGE when you need the elements ordered from highest to lowest score.
     * <p/>
     * Both start and stop are zero-based indexes, where 0 is the first element, 1 is the next element and so on.
     * They can also be negative numbers indicating offsets from the end of the sorted set,
     * with -1 being the last element of the sorted set, -2 the penultimate element and so on.
     * <p/>
     * Out of range indexes will not produce an error.
     * If start is larger than the largest index in the sorted set, or start > stop, an empty list is returned.
     * If stop is larger than the end of the sorted set Redis will treat
     * it like it is the last element of the sorted set.
     * <p/>
     * It is possible to pass the WITHSCORES option in order to return the scores of the elements
     * together with the elements. The returned list will contain value1,score1,...,valueN,scoreN instead of
     * value1,...,valueN. Client libraries are free to return a more appropriate data type
     * (suggestion: an array with (value, score)
     *
     * @param key   the key
     * @param start range start
     * @param end   range end
     * @return list of elements in the specified range.
     */
    List<String> zrange(String key, int start, int end);

    /**
     * Time complexity
     * <p/>
     * O(log(N)) with N being the number of elements in the sorted set.
     * <p/>
     * Removes the member from the sorted set stored at key. If member is not a member of the sorted set, no operation is performed.
     * <p/>
     * An error is returned when key exists and does not hold a sorted set.
     *
     * @param key    the key
     * @param member the member
     * @return true if member was removed or false if member is not a member of the sorted set.
     */
    Boolean zrem(String key, String member);

    /**
     * Time complexity
     * <p/>
     * O(log(N)) where N is the number of elements in the sorted set.
     * <p/>
     * Increments the score of member in the sorted set stored at key by increment.
     * If member does not exist in the sorted set, it is added with increment as
     * its score (as if its previous score was 0.0).
     * If key does not exist, a new sorted set with the specified member as its sole member is created.
     * <p/>
     * An error is returned when key exists but does not hold a sorted set.
     * <p/>
     * The score value should be the string representation of a numeric value,
     * and accepts double precision floating point numbers.
     * It is possible to provide a negative value to decrement the score.
     *
     * @param key    the key
     * @param score  the score
     * @param member the member
     * @return he new score of member (a double precision floating point number), represented as string.
     */
    String zincrby(String key, Number score, String member);

    /**
     * Time complexity
     * <p/>
     * O(log(N))
     * <p/>
     * Returns the rank of member in the sorted set stored at key, with the scores ordered from low to high.
     * The rank (or index) is 0-based, which means that the member with the lowest score has rank 0.
     * <p/>
     * Use ZREVRANK to get the rank of an element with the scores ordered from high to low.
     *
     * @param key    the key
     * @param member the member
     * @return <ul>
     *         <li>If member exists in the sorted set, the rank of member.</li>
     *         <li>If member does not exist in the sorted set or key does not exist, null.</li>
     *         </ul>
     */
    Long zrank(String key, String member);

    /**
     * Time complexity
     * <p/>
     * O(log(N))
     * <p/>
     * Returns the rank of member in the sorted set stored at key, with the scores ordered from high to low. The rank (or index) is 0-based, which means that the member with the highest score has rank 0.
     * <p/>
     * Use ZRANK to get the rank of an element with the scores ordered from low to high.
     *
     * @param key    the key
     * @param member the member
     * @return <ul><li>If member exists in the sorted set, the rank of member.</li>
     *         <li>If member does not exist in the sorted set or key does not exist, Bulk reply: null.</li><ul>
     */
    Long zrevrank(String key, String member);

    /**
     * Time complexity
     * <p/>
     * O(log(N)+M) with N being the number of elements in the sorted set and M the number of elements returned.
     * <p/>
     * Returns the specified range of elements in the sorted set stored at key. The elements are considered to be ordered from the highest to the lowest score.
     * <p/>
     * Apart from the reversed ordering, ZREVRANGE is similar to ZRANGE.
     *
     * @param key   the key
     * @param start range start
     * @param end   range end
     * @return list of elements in the specified range
     */
    List<String> zrevrange(String key, int start, int end);

    /**
     * Time complexity
     * <p/>
     * O(log(N)+M) with N being the number of elements in the sorted set and M the number of elements returned.
     * <p/>
     * Returns the specified range of elements in the sorted set stored at key.
     * The elements are considered to be ordered from the lowest to the highest score.
     * <p/>
     * See ZREVRANGE when you need the elements ordered from highest to lowest score.
     * <p/>
     * Both start and stop are zero-based indexes, where 0 is the first element, 1 is the next element and so on.
     * They can also be negative numbers indicating offsets from the end of the sorted set,
     * with -1 being the last element of the sorted set, -2 the penultimate element and so on.
     * <p/>
     * Out of range indexes will not produce an error.
     * If start is larger than the largest index in the sorted set, or start > stop, an empty list is returned.
     * If stop is larger than the end of the sorted set Redis will treat
     * it like it is the last element of the sorted set.
     * <p/>
     * It is possible to pass the WITHSCORES option in order to return the scores of the elements
     * together with the elements. The returned list will contain value1,score1,...,valueN,scoreN instead of
     * value1,...,valueN. Client libraries are free to return a more appropriate data type
     * (suggestion: an array with (value, score)
     *
     * @param key   the key
     * @param start range start
     * @param end   range end
     * @return list of elements in the specified range with their scores.
     */
    List<ElementScore> zrangeWithScores(String key, int start, int end);

    /**
     * Time complexity
     * <p/>
     * O(log(N)+M) with N being the number of elements in the sorted set and M the number of elements returned.
     * <p/>
     * Returns the specified range of elements in the sorted set stored at key. The elements are considered to be ordered from the highest to the lowest score.
     * <p/>
     * Apart from the reversed ordering, ZREVRANGE is similar to ZRANGE.
     *
     * @param key   the key
     * @param start range start
     * @param end   range end
     * @return list of elements in the specified range with their scores
     */
    List<ElementScore> zrevrangeWithScores(String key, int start, int end);

    /**
     * Time complexity
     * <p/>
     * O(log(N)+M) with N being the number of elements in the sorted set and M the number of elements being returned.
     * If M is constant (e.g. always asking for the first 10 elements with LIMIT), you can consider it O(log(N)).
     * <p/>
     * Returns all the elements in the sorted set at key with a score between max and min
     * (including elements with score equal to max or min). In contrary to the default ordering of sorted sets,
     * for this command the elements are considered to be ordered from high to low scores.
     * <p/>
     * The elements having the same score are returned in reverse lexicographical order.
     * <p/>
     * Apart from the reversed ordering, ZREVRANGEBYSCORE is similar to ZRANGEBYSCORE.
     *
     * @param key the key
     * @param max max score
     * @param min min score
     * @return list of elements in the specified score range (optionally with their scores).
     */
    List<String> zrevrangeByScore(String key, String max, String min);

    /**
     * Time complexity
     * <p/>
     * O(log(N)+M) with N being the number of elements in the sorted set and M the number of elements being returned.
     * If M is constant (e.g. always asking for the first 10 elements with LIMIT), you can consider it O(log(N)).
     * <p/>
     * Returns all the elements in the sorted set at key with a score between max and min
     * (including elements with score equal to max or min). In contrary to the default ordering of sorted sets,
     * for this command the elements are considered to be ordered from high to low scores.
     * <p/>
     * The elements having the same score are returned in reverse lexicographical order.
     * <p/>
     * Apart from the reversed ordering, ZREVRANGEBYSCORE is similar to ZRANGEBYSCORE.
     *
     * @param key    the key
     * @param max    max score
     * @param min    min score
     * @param offset limit offset
     * @param count  limit count
     * @return list of elements in the specified score range (optionally with their scores).
     */
    List<String> zrevrangeByScore(String key, String max, String min, int offset, int count);

    /**
     * Time complexity
     * <p/>
     * O(log(N)+M) with N being the number of elements in the sorted set and M the number of elements being returned.
     * If M is constant (e.g. always asking for the first 10 elements with LIMIT), you can consider it O(log(N)).
     * <p/>
     * Returns all the elements in the sorted set at key with a score between max and min
     * (including elements with score equal to max or min). In contrary to the default ordering of sorted sets,
     * for this command the elements are considered to be ordered from high to low scores.
     * <p/>
     * The elements having the same score are returned in reverse lexicographical order.
     * <p/>
     * Apart from the reversed ordering, ZREVRANGEBYSCORE is similar to ZRANGEBYSCORE.
     *
     * @param key the key
     * @param max max score
     * @param min min score
     * @return list of elements in the specified score range (optionally with their scores).
     */
    List<ElementScore> zrevrangeByScoreWithScores(String key, String max, String min);

    /**
     * Time complexity
     * <p/>
     * O(log(N)+M) with N being the number of elements in the sorted set and M the number of elements being returned.
     * If M is constant (e.g. always asking for the first 10 elements with LIMIT), you can consider it O(log(N)).
     * <p/>
     * Returns all the elements in the sorted set at key with a score between max and min
     * (including elements with score equal to max or min). In contrary to the default ordering of sorted sets,
     * for this command the elements are considered to be ordered from high to low scores.
     * <p/>
     * The elements having the same score are returned in reverse lexicographical order.
     * <p/>
     * Apart from the reversed ordering, ZREVRANGEBYSCORE is similar to ZRANGEBYSCORE.
     *
     * @param key    the key
     * @param max    max score
     * @param min    min score
     * @param offset limit offset
     * @param count  limit count
     * @return list of elements in the specified score range (optionally with their scores).
     */
    List<ElementScore> zrevrangeByScoreWithScores(String key, String max, String min, int offset, int count);

    /**
     * Time complexity
     * <p/>
     * O(1)
     * <p/>
     * Returns the sorted set cardinality (number of elements) of the sorted set stored at key.
     *
     * @param key the key
     * @return the cardinality (number of elements) of the sorted set, or 0 if key does not exist.
     */
    Long zcard(String key);

    /**
     * Time complexity
     * <p/>
     * O(1)
     * <p/>
     * Returns the score of member in the sorted set at key.
     * <p/>
     * If member does not exist in the sorted set, or key does not exist, nil is returned.
     *
     * @param key    the key
     * @param member the member
     * @return the score of member (a double precision floating point number), represented as string.
     */
    String zscore(String key, String member);

    /**
     * <h4>Time complexity</h4> O(N*log(N)) where N is the number of elements returned.
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
     * <h4>Time complexity</h4> O(N*log(N)) where N is the number of elements returned.
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

    /**
     * Time complexity
     * <p/>
     * O(log(N)+M) with N being the number of elements in the sorted set and M being the number of elements between min and max.
     * <p/>
     * Returns the number of elements in the sorted set at key with a score between min and max.
     * <p/>
     * The min and max arguments have the same semantic as described for ZRANGEBYSCORE.
     *
     * @param key the key
     * @param min min value
     * @param max max value
     * @return the number of elements in the specified score range.
     */
    Long zcount(String key, Number min, Number max);

    /**
     * Time complexity
     * <p/>
     * O(log(N)+M) with N being the number of elements in the sorted set and M the number of elements being returned.
     * If M is constant (e.g. always asking for the first 10 elements with LIMIT), you can consider it O(log(N)).
     * <p/>
     * Returns all the elements in the sorted set at key with a score between min and max
     * (including elements with score equal to min or max).
     * The elements are considered to be ordered from low to high scores.
     * <p/>
     * The elements having the same score are returned in lexicographical order
     * (this follows from a property of the sorted set implementation in
     * Redis and does not involve further computation).
     * <p/>
     * The optional LIMIT argument can be used to only get a range of the matching elements
     * (similar to SELECT LIMIT offset, count in SQL). Keep in mind that if offset is large,
     * the sorted set needs to be traversed for offset elements before getting to the elements
     * to return, which can add up to O(N) time complexity.
     * <p/>
     * The optional WITHSCORES argument makes the command return both the element and its score,
     * instead of the element alone. This option is available since Redis 2.0.
     * <h4>Exclusive intervals and infinity</h4>
     * <p/>
     * min and max can be -inf and +inf, so that you are not required to know the highest or
     * lowest score in the sorted set to get all elements from or up to a certain score.
     * <p/>
     * By default, the interval specified by min and max is closed (inclusive).
     * It is possible to specify an open interval (exclusive) by prefixing the score with the character (.
     * For example:
     * <p/>
     * ZRANGEBYSCORE zset (1 5
     * <p/>
     * Will return all elements with 1 < score <= 5 while:
     * <p/>
     * ZRANGEBYSCORE zset (5 (10
     * <p/>
     * Will return all the elements with 5 < score < 10 (5 and 10 excluded).
     *
     * @param key the key
     * @param min min score
     * @param max max score
     * @return list of elements in the specified score range.
     */
    List<String> zrangeByScore(String key, String min, String max);

    /**
     * Time complexity
     * <p/>
     * O(log(N)+M) with N being the number of elements in the sorted set and M the number of elements being returned.
     * If M is constant (e.g. always asking for the first 10 elements with LIMIT), you can consider it O(log(N)).
     * <p/>
     * Returns all the elements in the sorted set at key with a score between min and max
     * (including elements with score equal to min or max).
     * The elements are considered to be ordered from low to high scores.
     * <p/>
     * The elements having the same score are returned in lexicographical order
     * (this follows from a property of the sorted set implementation in
     * Redis and does not involve further computation).
     * <p/>
     * The optional LIMIT argument can be used to only get a range of the matching elements
     * (similar to SELECT LIMIT offset, count in SQL). Keep in mind that if offset is large,
     * the sorted set needs to be traversed for offset elements before getting to the elements
     * to return, which can add up to O(N) time complexity.
     * <p/>
     * The optional WITHSCORES argument makes the command return both the element and its score,
     * instead of the element alone. This option is available since Redis 2.0.
     * <h4>Exclusive intervals and infinity</h4>
     * <p/>
     * min and max can be -inf and +inf, so that you are not required to know the highest or
     * lowest score in the sorted set to get all elements from or up to a certain score.
     * <p/>
     * By default, the interval specified by min and max is closed (inclusive).
     * It is possible to specify an open interval (exclusive) by prefixing the score with the character (.
     * For example:
     * <p/>
     * ZRANGEBYSCORE zset (1 5
     * <p/>
     * Will return all elements with 1 < score <= 5 while:
     * <p/>
     * ZRANGEBYSCORE zset (5 (10
     * <p/>
     * Will return all the elements with 5 < score < 10 (5 and 10 excluded).
     *
     * @param key    the key
     * @param min    min score
     * @param max    max score
     * @param offset limit offset
     * @param count  limit count
     * @return list of elements in the specified score range.
     */
    List<String> zrangeByScore(String key, String min, String max, int offset, int count);

    /**
     * Time complexity
     * <p/>
     * O(log(N)+M) with N being the number of elements in the sorted set and M the number of elements being returned.
     * If M is constant (e.g. always asking for the first 10 elements with LIMIT), you can consider it O(log(N)).
     * <p/>
     * Returns all the elements in the sorted set at key with a score between min and max
     * (including elements with score equal to min or max).
     * The elements are considered to be ordered from low to high scores.
     * <p/>
     * The elements having the same score are returned in lexicographical order
     * (this follows from a property of the sorted set implementation in
     * Redis and does not involve further computation).
     * <p/>
     * The optional LIMIT argument can be used to only get a range of the matching elements
     * (similar to SELECT LIMIT offset, count in SQL). Keep in mind that if offset is large,
     * the sorted set needs to be traversed for offset elements before getting to the elements
     * to return, which can add up to O(N) time complexity.
     * <p/>
     * The optional WITHSCORES argument makes the command return both the element and its score,
     * instead of the element alone. This option is available since Redis 2.0.
     * <h4>Exclusive intervals and infinity</h4>
     * <p/>
     * min and max can be -inf and +inf, so that you are not required to know the highest or
     * lowest score in the sorted set to get all elements from or up to a certain score.
     * <p/>
     * By default, the interval specified by min and max is closed (inclusive).
     * It is possible to specify an open interval (exclusive) by prefixing the score with the character (.
     * For example:
     * <p/>
     * ZRANGEBYSCORE zset (1 5
     * <p/>
     * Will return all elements with 1 < score <= 5 while:
     * <p/>
     * ZRANGEBYSCORE zset (5 (10
     * <p/>
     * Will return all the elements with 5 < score < 10 (5 and 10 excluded).
     *
     * @param key the key
     * @param min min score
     * @param max max score
     * @return list of elements in the specified score range with their scores.
     */
    List<ElementScore> zrangeByScoreWithScores(String key, String min, String max);

    /**
     * Time complexity
     * <p/>
     * O(log(N)+M) with N being the number of elements in the sorted set and M the number of elements being returned.
     * If M is constant (e.g. always asking for the first 10 elements with LIMIT), you can consider it O(log(N)).
     * <p/>
     * Returns all the elements in the sorted set at key with a score between min and max
     * (including elements with score equal to min or max).
     * The elements are considered to be ordered from low to high scores.
     * <p/>
     * The elements having the same score are returned in lexicographical order
     * (this follows from a property of the sorted set implementation in
     * Redis and does not involve further computation).
     * <p/>
     * The optional LIMIT argument can be used to only get a range of the matching elements
     * (similar to SELECT LIMIT offset, count in SQL). Keep in mind that if offset is large,
     * the sorted set needs to be traversed for offset elements before getting to the elements
     * to return, which can add up to O(N) time complexity.
     * <p/>
     * The optional WITHSCORES argument makes the command return both the element and its score,
     * instead of the element alone. This option is available since Redis 2.0.
     * <h4>Exclusive intervals and infinity</h4>
     * <p/>
     * min and max can be -inf and +inf, so that you are not required to know the highest or
     * lowest score in the sorted set to get all elements from or up to a certain score.
     * <p/>
     * By default, the interval specified by min and max is closed (inclusive).
     * It is possible to specify an open interval (exclusive) by prefixing the score with the character (.
     * For example:
     * <p/>
     * ZRANGEBYSCORE zset (1 5
     * <p/>
     * Will return all elements with 1 < score <= 5 while:
     * <p/>
     * ZRANGEBYSCORE zset (5 (10
     * <p/>
     * Will return all the elements with 5 < score < 10 (5 and 10 excluded).
     *
     * @param key    the key
     * @param min    min score
     * @param max    max score
     * @param offset limit offset
     * @param count  limit count
     * @return list of elements in the specified score range (optionally with their scores).
     */
    List<ElementScore> zrangeByScoreWithScores(String key, String min, String max, int offset, int count);

    /**
     * Time complexity
     * <p/>
     * O(log(N)+M) with N being the number of elements in the sorted set and M the number of
     * elements removed by the operation.
     * <p/>
     * Removes all elements in the sorted set stored at key with rank between start and stop.
     * Both start and stop are 0-based indexes with 0 being the element with the lowest score.
     * These indexes can be negative numbers, where they indicate offsets starting at the element
     * with the highest score.
     * For example: -1 is the element with the highest score, -2 the element with the second highest score and so forth.
     *
     * @param key   the key
     * @param start range start
     * @param end   range end
     * @return the number of elements removed.
     */
    Long zremrangeByRank(String key, int start, int end);

    /**
     * Time complexity
     * <p/>
     * O(log(N)+M) with N being the number of elements in the sorted set and M the number of elements removed by the operation.
     * <p/>
     * Removes all elements in the sorted set stored at key with a score between min and max (inclusive).
     * <p/>
     * Since version 2.1.6, min and max can be exclusive, following the syntax of ZRANGEBYSCORE.
     *
     * @param key the key
     * @param min min score
     * @param max max score
     * @return the number of elements removed.
     */
    Long zremrangeByScore(String key, String min, String max);

    /**
     * <h4>Time complexity</h4>
     * <p/>
     * O(N) where N is the number of elements to traverse before seeing the value pivot.
     * This means that inserting somewhere on the left end on the list (head)
     * can be considered O(1) and inserting somewhere on the right end (tail) is O(N).
     * <p/>
     * Inserts value in the list stored at key either before or after the reference value pivot.
     * <p/>
     * When key does not exist, it is considered an empty list and no operation is performed.
     * <p/>
     * An error is returned when key exists but does not hold a list value.
     *
     * @param key   the key
     * @param where list position
     * @param pivot the pivot
     * @param value the value
     * @return the length of the list after the insert operation, or -1 when the value pivot was not found.
     */
    Long linsert(String key, ListPosition where, String pivot, String value);

    Long publish(String channel, String message);

    /**
     * <h4>Time complexity</h4> O(1)
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
     * <h4>Time complexity</h4> O(1)
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
