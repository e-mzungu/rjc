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
import java.util.Set;

/**
 * @author Evgeny Dolgov
 */
public interface SingleRedisOperations extends RedisOperations {

    String ping();

    /**
     * <h4>Time complexity</h4> O(1)
     * <p/>
     * Return a random key from the currently selected database.
     *
     * @return the random key, or null when the database is empty.
     */
    String randomKey();

    void quit();

    String flushDB();

    /**
     * <h4>Time complexity</h4> O(1)
     * <p/>
     * Renames key to newKey. It returns an error when the source and destination names are the same,
     * or when key does not exist. If newKey already exists it is overwritten.
     *
     * @param key    a key
     * @param newKey new key
     * @return status code reply
     */
    String rename(String key, String newKey);

    /**
     * <h4>Time complexity</h4> O(1)
     * <p/>
     * Renames key to newKey if newKey does not yet exist. It returns an error under the same conditions as RENAME.
     *
     * @param key    a key
     * @param newKey new key
     * @return true if key was renamed to newKey or false if newKey already exists
     */
    Boolean renamenx(String key, String newKey);

    Long dbSize();

    /**
     * <h4>Time complexity</h4> O(1)
     * <p/>
     * Move key from the currently selected database (see SELECT) to the speficied destination database.
     * When key already exists in the destination database, or it does not exist in the source database,
     * it does nothing. It is possible to use MOVE as a locking primitive because of this.
     *
     * @param key     a key
     * @param dbIndex database index
     * @return true if key was moved otherwise false
     */
    Boolean move(String key, int dbIndex);

    String flushAll();

    /**
     * <h4>Time complexity</h4> O(N) where N is the number of keys to retrieve
     * <p/>
     * Returns the values of all specified keys. For every key that does not hold a string value or does not
     * exist, the special value nil is returned. Because of this, the operation never fails.
     *
     * @param keys the keys
     * @return list of values at the specified keys.
     */
    List<String> mget(String... keys);

    /**
     * <h4>Time complexity</h4> O(N) where N is the number of keys to set
     * <p/>
     * Sets the given keys to their respective values. MSET replaces existing values with new values,
     * just as regular SET. See MSETNX if you don't want to overwrite existing values.
     * MSET is atomic, so all given keys are set at once. It is not possible for clients to see
     * that some of the keys were updated while others are unchanged.
     *
     * @param keysvalues array of keys and values, for instance {"key1", "value1", "key2", "value2"}
     * @return always OK since MSET can't fail.
     */
    String mset(String... keysvalues);

    /**
     * <h4>Time complexity</h4> O(N) where N is the number of keys to set
     * <p/>
     * Sets the given keys to their respective values. MSETNX will not perform
     * any operation at all even if just a single key already exists.
     * <p/>
     * Because of this semantic MSETNX can be used in order to set different keys representing different fields
     * of an unique logic object in a way that ensures that either all the fields or none at all are set.
     * <p/>
     * MSETNX is atomic, so all given keys are set at once. It is not possible for clients to see
     * that some of the keys were updated while others are unchanged.
     *
     * @param keysvalues array of keys and values, for instance {"key1", "value1", "key2", "value2"}
     * @return true if the all the keys were set or false if no key was set (at least one key already existed).
     */
    Boolean msetnx(String... keysvalues);

    /**
     * <h4>Time complexity</h4>
     * <p/>
     * O(1)
     * <p/>
     * Atomically returns and removes the last element (tail) of the list stored at source,
     * and pushes the element at the first element (head) of the list stored at destination.
     * <p/>
     * For example: consider source holding the list a,b,c, and destination holding the list x,y,z.
     * Executing RPOPLPUSH results in source holding a,b and destination holding c,x,y,z.
     * <p/>
     * If source does not exist, the value nil is returned and no operation is performed.
     * If source and destination are the same, the operation is equivalent to removing
     * the last element from the list and pushing it as first element of the list,
     * so it can be considered as a list rotation command.
     *
     * @param srckey source key
     * @param dstkey destination key
     * @return the element being popped and pushed.
     */
    String rpoplpush(String srckey, String dstkey);

    /**
     * <h4>Time complexity</h4>
     * <p/>
     * O(1).
     * <p/>
     * BRPOPLPUSH is the blocking variant of RPOPLPUSH. When source contains elements,
     * this command behaves exactly like RPOPLPUSH. When source is empty,
     * Redis will block the connection until another client pushes to it or until timeout is reached.
     * A timeout of zero can be used to block indefinitely.
     * <p/>
     * See RPOPLPUSH for more information.
     *
     * @param source      source key
     * @param destination destination key
     * @param timeout     blocking timeout
     * @return the element being popped from source and pushed to destination. If timeout is reached, a null is returned.
     */
    String brpoplpush(String source, String destination, int timeout);

    /**
     * <h4>Time complexity</h4>
     * <p/>
     * O(1)
     * <p/>
     * Move member from the set at source to the set at destination. This operation is atomic.
     * In every given moment the element will appear to be a member of source or destination for other clients.
     * <p/>
     * If the source set does not exist or does not contain the specified element,
     * no operation is performed and 0 is returned.
     * Otherwise, the element is removed from the source set and added to the destination set.
     * When the specified element already exists in the destination set, it is only removed from the source set.
     * <p/>
     * An error is returned if source or destination does not hold a set value.
     *
     * @param srckey source key
     * @param dstkey destination key
     * @param member the member
     * @return true if the element is moved or false if the element is not a member of source and no operation was performed.
     */
    Boolean smove(String srckey, String dstkey, String member);

    /**
     * <h4>Time complexity</h4>
     * <p/>
     * O(N*M) worst case where N is the cardinality of the smallest set and M is the number of sets.
     * <p/>
     * Returns the members of the set resulting from the intersection of all the given sets.
     * <p/>
     * For example:
     * <p/>
     * key1 = {a,b,c,d} <br/>
     * key2 = {c} <br/>
     * key3 = {a,c,e} <br/>
     * SINTER key1 key2 key3 = {c} <br/>
     * <p/>
     * Keys that do not exist are considered to be empty sets. With one of the keys being an empty set,
     * the resulting set is also empty (since set intersection with an empty set always results in an empty set).
     *
     * @param keys the keys
     * @return list with members of the resulting set.
     */
    Set<String> sinter(String... keys);

    /**
     * <h4>Time complexity</h4>
     * <p/>
     * O(N*M) worst case where N is the cardinality of the smallest set and M is the number of sets.
     * <p/>
     * This command is equal to SINTER, but instead of returning the resulting set, it is stored in destination.
     * <p/>
     * If destination already exists, it is overwritten.
     *
     * @param dstkey destination key
     * @param keys   the keys
     * @return the number of elements in the resulting set.
     */
    Long sinterstore(String dstkey, String... keys);

    /**
     * <h4>Time complexity</h4>
     * <p/>
     * O(N) where N is the total number of elements in all given sets.
     * <p/>
     * Returns the members of the set resulting from the union of all the given sets.
     * <p/>
     * For example:
     * <p/>
     * key1 = {a,b,c,d}<br/>
     * key2 = {c}<br/>
     * key3 = {a,c,e}<br/>
     * SUNION key1 key2 key3 = {a,b,c,d,e}<br/>
     * <p/>
     * Keys that do not exist are considered to be empty sets.
     *
     * @param keys the keys
     * @return list with members of the resulting set.
     */
    Set<String> sunion(String... keys);

    /**
     * <h4>Time complexity</h4>
     * <p/>
     * O(N) where N is the total number of elements in all given sets.
     * <p/>
     * This command is equal to SUNION, but instead of returning the resulting set, it is stored in destination.
     * <p/>
     * If destination already exists, it is overwritten.
     *
     * @param dstkey destination key
     * @param keys   the keys
     * @return he number of elements in the resulting set.
     */
    Long sunionstore(String dstkey, String... keys);

    /**
     * <h4>Time complexity</h4>
     * <p/>
     * O(N) where N is the total number of elements in all given sets.
     * <p/>
     * Returns the members of the set resulting from the difference between the first set and all the successive sets.
     * <p/>
     * For example:
     * <p/>
     * key1 = {a,b,c,d}<br/>
     * key2 = {c}<br/>
     * key3 = {a,c,e}<br/>
     * SDIFF key1 key2 key3 = {b,d}<br/>
     * <p/>
     * Keys that do not exist are considered to be empty sets.
     *
     * @param keys the keys
     * @return list with members of the resulting set.
     */
    Set<String> sdiff(String... keys);

    /**
     * <h4>Time complexity</h4>
     * <p/>
     * O(N) where N is the total number of elements in all given sets.
     * <p/>
     * This command is equal to SDIFF, but instead of returning the resulting set, it is stored in destination.
     * <p/>
     * If destination already exists, it is overwritten.
     *
     * @param dstkey destination key
     * @param keys   the keys
     * @return the number of elements in the resulting set.
     */
    Long sdiffstore(String dstkey, String... keys);

    /**
     * <h4>Time complexity</h4>
     * <p/>
     * O(1)
     * <p/>
     * BLPOP is a blocking list pop primitive. It is the blocking version of LPOP because it blocks
     * the connection when there are no elements to pop from any of the given lists.
     * An element is popped from the head of the first list that is non-empty,
     * with the given keys being checked in the order that they are given.
     * <h4>Non-blocking behavior</h4>
     * <p/>
     * When BLPOP is called, if at least one of the specified keys contain a non-empty list,
     * an element is popped from the head of the list and returned to the caller
     * together with the key it was popped from.
     * <p/>
     * Keys are checked in the order that they are given.
     * Let's say that the key list1 doesn't exist and list2 and list3 hold non-empty lists.
     * Consider the following command:
     * <p/>
     * BLPOP list1 list2 list3 0
     * <p/>
     * BLPOP guarantees to return an element from the list stored at list2
     * (since it is the first non empty list when checking list1, list2 and list3 in that order).
     * <h4>Blocking behavior</h4>
     * <p/>
     * If none of the specified keys exist or contain non-empty lists,
     * BLPOP blocks the connection until another client performs an LPUSH or RPUSH operation against one of the lists.
     * <p/>
     * Once new data is present on one of the lists,
     * the client returns with the name of the key unblocking it and the popped value.
     * <p/>
     * When BLPOP causes a client to block and a non-zero timeout is specified,
     * the client will unblock returning a nil multi-bulk value when the specified timeout has expired
     * without a push operation against at least one of the specified keys.
     * <p/>
     * The timeout argument is interpreted as an integer value. A timeout of zero can be used to block indefinitely.
     * <h4>Multiple clients blocking for the same keys</h4>
     * <p/>
     * Multiple clients can block for the same key. They are put into a queue, so the first to be served will be
     * the one that started to wait earlier, in a first-BLPOP first-served fashion.
     * BLPOP inside a MULTI/EXEC transaction
     * <p/>
     * BLPOP can be used with pipelining (sending multiple commands and reading the replies in batch),
     * but it does not make sense to use BLPOP inside a MULTI/EXEC block. This would require blocking the entire
     * server in order to execute the block atomically,
     * which in turn does not allow other clients to perform a push operation.
     * <p/>
     * The behavior of BLPOP inside MULTI/EXEC when the list is empty is to return a nil multi-bulk reply,
     * which is the same thing that happens when the timeout is reached. If you like science fiction, think of time flowing at infinite speed inside a MULTI/EXEC block.
     *
     * @param timeout blocking timeout. A timeout of zero can be used to block indefinitely.
     * @param keys    the keys
     * @return <ul><li>A null when no element could be popped and the timeout expired.</li>
     *         <li>A two-element multi-bulk with the first element being the name of the key where an element was
     *         popped and the second element being the value of the popped element.</li></ul>
     */
    List<String> blpop(int timeout, String... keys);

    Long sort(String key, SortingParams sortingParameters, String dstkey);

    Long sort(String key, String dstkey);

    /**
     * <h4>Time complexity</h4>
     * <p/>
     * O(1)
     * <p/>
     * BRPOP is a blocking list pop primitive. It is the blocking version of RPOP because
     * it blocks the connection when there are no elements to pop from any of the given lists.
     * An element is popped from the tail of the first list that is non-empty,
     * with the given keys being checked in the order that they are given.
     * <p/>
     * See BLPOP for the exact semantics. BRPOP is identical to BLPOP,
     * apart from popping from the tail of a list instead of the head of a list.
     *
     * @param timeout blocking timeout
     * @param keys    the keys
     * @return <ul><li>A null when no element could be popped and the timeout expired.</li>
     *         <li>A two-element multi-bulk with the first element being the name of the key where an element was
     *         popped and the second element being the value of the popped element.</li></ul>
     */
    List<String> brpop(int timeout, String... keys);

    String auth(String password);

    List<Object> pipeline(Pipeline pipeline);

    /**
     * Time complexity
     * <p/>
     * O(N)+O(M log(M)) with N being the sum of the sizes of the input sorted sets,
     * and M being the number of elements in the resulting sorted set.
     * <p/>
     * Computes the union of numkeys sorted sets given by the specified keys, and stores the result in destination.
     * It is mandatory to provide the number of input keys (numkeys) before passing the input keys
     * and the other (optional) arguments.
     * <p/>
     * By default, the resulting score of an element is the sum of its scores in the sorted sets where it exists.
     * <p/>
     * Using the WEIGHTS option, it is possible to specify a multiplication factor for each input sorted set.
     * This means that the score of every element in every input sorted set is multiplied by this factor
     * before being passed to the aggregation function.
     * When WEIGHTS is not given, the multiplication factors default to 1.
     * <p/>
     * With the AGGREGATE option, it is possible to specify how the results of the union are aggregated.
     * This option defaults to SUM, where the score of an element is summed across the inputs where it exists.
     * When this option is set to either MIN or MAX, the resulting set will contain the minimum or maximum score
     * of an element across the inputs where it exists.
     * <p/>
     * If destination already exists, it is overwritten.
     *
     * @param dstkey destination key
     * @param sets   the keys
     * @return the number of elements in the resulting sorted set at destination.
     */
    Long zunionstore(String dstkey, String... sets);

    /**
     * Time complexity
     * <p/>
     * O(N)+O(M log(M)) with N being the sum of the sizes of the input sorted sets,
     * and M being the number of elements in the resulting sorted set.
     * <p/>
     * Computes the union of numkeys sorted sets given by the specified keys, and stores the result in destination.
     * It is mandatory to provide the number of input keys (numkeys) before passing the input keys
     * and the other (optional) arguments.
     * <p/>
     * By default, the resulting score of an element is the sum of its scores in the sorted sets where it exists.
     * <p/>
     * Using the WEIGHTS option, it is possible to specify a multiplication factor for each input sorted set.
     * This means that the score of every element in every input sorted set is multiplied by this factor
     * before being passed to the aggregation function.
     * When WEIGHTS is not given, the multiplication factors default to 1.
     * <p/>
     * With the AGGREGATE option, it is possible to specify how the results of the union are aggregated.
     * This option defaults to SUM, where the score of an element is summed across the inputs where it exists.
     * When this option is set to either MIN or MAX, the resulting set will contain the minimum or maximum score
     * of an element across the inputs where it exists.
     * <p/>
     * If destination already exists, it is overwritten.
     *
     * @param dstkey destination key
     * @param params union params
     * @param sets   the keys
     * @return the number of elements in the resulting sorted set at destination.
     */
    Long zunionstore(String dstkey, ZParams params, String... sets);

    /**
     * Time complexity
     * <p/>
     * O(N*K)+O(M*log(M)) worst case with N being the smallest input sorted set,
     * K being the number of input sorted sets and M being the number of elements in
     * the resulting sorted set.
     * <p/>
     * Computes the intersection of numkeys sorted sets given by the specified keys,
     * and stores the result in destination. It is mandatory to provide the number of input keys
     * (numkeys) before passing the input keys and the other (optional) arguments.
     * <p/>
     * By default, the resulting score of an element is the sum of its scores in the sorted sets where it exists.
     * Because intersection requires an element to be a member of every given sorted set,
     * this results in the score of every element in the resulting sorted set to be equal
     * to the number of input sorted sets.
     * <p/>
     * For a description of the WEIGHTS and AGGREGATE options, see ZUNIONSTORE.
     * <p/>
     * If destination already exists, it is overwritten.
     *
     * @param dstkey destination key
     * @param sets   the keys
     * @return the number of elements in the resulting sorted set at destination.
     */
    Long zinterstore(String dstkey, String... sets);

    /**
     * Time complexity
     * <p/>
     * O(N*K)+O(M*log(M)) worst case with N being the smallest input sorted set,
     * K being the number of input sorted sets and M being the number of elements in
     * the resulting sorted set.
     * <p/>
     * Computes the intersection of numkeys sorted sets given by the specified keys,
     * and stores the result in destination. It is mandatory to provide the number of input keys
     * (numkeys) before passing the input keys and the other (optional) arguments.
     * <p/>
     * By default, the resulting score of an element is the sum of its scores in the sorted sets where it exists.
     * Because intersection requires an element to be a member of every given sorted set,
     * this results in the score of every element in the resulting sorted set to be equal
     * to the number of input sorted sets.
     * <p/>
     * For a description of the WEIGHTS and AGGREGATE options, see ZUNIONSTORE.
     * <p/>
     * If destination already exists, it is overwritten.
     *
     * @param dstkey destination key
     * @param params the intersection params
     * @param sets   the keys
     * @return the number of elements in the resulting sorted set at destination.
     */
    Long zinterstore(String dstkey, ZParams params, String... sets);

    String save();

    String bgsave();

    String bgrewriteaof();

    Long lastsave();

    String shutdown();

    String info();

    void monitor(RedisMonitor redisMonitor);

    String slaveof(String host, int port);

    String slaveofNoOne();

    List<String> configGet(String pattern);

    String configSet(String parameter, String value);

    void sync();

    String echo(String string);

    String debug(DebugParams params);
}
