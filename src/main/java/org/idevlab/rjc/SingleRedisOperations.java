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
     * Time complexity: O(1)
     * <p/>
     * Return a random key from the currently selected database.
     *
     * @return the random key, or null when the database is empty.
     */
    String randomKey();

    void quit();

    String flushDB();

    /**
     * Time complexity: O(1)
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
     * Time complexity: O(1)
     * <p/>
     * Renames key to newKey if newKey does not yet exist. It returns an error under the same conditions as RENAME.
     *
     * @param key    a key
     * @param newKey new key
     * @return true if key was renamed to newKey or false if newKey already exists
     */
    boolean renamenx(String key, String newKey);

    Long dbSize();

    /**
     * Time complexity: O(1)
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

    List<String> mget(String... keys);

    String mset(String... keysvalues);

    Long msetnx(String... keysvalues);

    String rpoplpush(String srckey, String dstkey);

    Long smove(String srckey, String dstkey, String member);

    Set<String> sinter(String... keys);

    Long sinterstore(String dstkey, String... keys);

    Set<String> sunion(String... keys);

    Long sunionstore(String dstkey, String... keys);

    Set<String> sdiff(String... keys);

    Long sdiffstore(String dstkey, String... keys);

    List<String> blpop(int timeout, String... keys);

    Long sort(String key, SortingParams sortingParameters, String dstkey);

    Long sort(String key, String dstkey);

    List<String> brpop(int timeout, String... keys);

    String auth(String password);

    List<Object> pipeline(Pipeline pipeline);

    Long zunionstore(String dstkey, String... sets);

    Long zunionstore(String dstkey, ZParams params, String... sets);

    Long zinterstore(String dstkey, String... sets);

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

    Long strlen(String key);

    void sync();

    String echo(String string);

    String debug(DebugParams params);
}
