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

/**
 * @author Evgeny Dolgov
 */
public interface Session extends SingleRedisOperations {

    String select(int index);

    /**
     * Marks the start of a transaction block. Subsequent commands will be queued for atomic execution using EXEC.
     *
     * @return interface for commands execution
     */
    RedisClient multi();

    /**
     * Flushes all previously queued commands in a transaction and restores the connection state to normal.
     * <p/>
     * If WATCH was used, DISCARD unwatches all keys.
     *
     * @return always OK
     */
    String discard();

    /**
     * O(1) for every keys.
     * <p/>
     * Marks the given keys to be watched for conditional execution of a transaction.
     *
     * @param keys keys to be watched
     * @return always OK.
     * @since 2.1.0
     */
    String watch(String... keys);

    /**
     * O(1).
     * <p/>
     * Flushes all the previously watched keys for a transaction.
     * <p/>
     * If you call EXEC or DISCARD, there's no need to manually call UNWATCH.
     *
     * @return always OK.
     * @since 2.1.0
     */
    String unwatch();

    /**
     * Executes all previously queued commands in a transaction and restores the connection state to normal.
     * <p/>
     * When using WATCH, EXEC will execute commands only if the watched keys were not modified, allowing for a check-and-set mechanism.
     *
     * @return each element being the reply to each of the commands in the atomic transaction.
     *         <p/>
     *         When using WATCH, EXEC can return a Null multi-bulk reply if the execution was aborted.
     */
    List<Object> exec();

    void close();
}
