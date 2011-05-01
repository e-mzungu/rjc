/*
 * Copyright 2010-2011. Evgeny Dolgov
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.idevlab.rjc;

import org.idevlab.rjc.protocol.RedisCommand;

import java.util.List;

/**
 * @author Evgeny Dolgov
 */
public interface Redis {
    String getStatusReply(RedisCommand command);
    String getStatusReply(RedisCommand command, String... args);
    String getStatusReply(RedisCommand command, byte[]... args);

    Long getIntegerReply(RedisCommand command);
    Long getIntegerReply(RedisCommand command, String... args);
    Long getIntegerReply(RedisCommand command, byte[]... args);

    String getBulkReply(RedisCommand command);
    String getBulkReply(RedisCommand command, String... args);
    byte[] getBulkReply(RedisCommand command, byte[]... args);

    byte[] getBinaryBulkReply(RedisCommand command);


    /**
     * Reply may contains Long and String objects
     *
     * @param command redis command
     * @param args command arguments
     * @return Long and String objects
     */
    List<Object> getMultiBulkReply(RedisCommand command, String... args);

    List<String> getStringMultiBulkReply(RedisCommand command, String... args);

    /**
     * Reply may contains Long and byte[] objects
     *
     * @param command redis command
     * @param args command arguments
     * @return Long and byte[] objects
     */
    List<Object> getBinaryMultiBulkReply(RedisCommand command, byte[]... args);

    void noReply(RedisCommand command);
}
