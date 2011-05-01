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

package org.idevlab.rjc.ds;

import org.idevlab.rjc.protocol.RedisCommand;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

/**
 * @author Evgeny Dolgov
 */
public interface RedisConnection {
    int getTimeout();

    void rollbackTimeout();

    String getHost();

    int getPort();

    void connect() throws UnknownHostException, IOException;

    void close();

    boolean isConnected();

    void setTimeoutInfinite();

    String getStatusCodeReply();

    String getBulkReply();

    byte[] getBinaryBulkReply();

    Long getIntegerReply();

    List<String> getMultiBulkReply();

    /**
     * Reply may contains Long and String objects
     *
     * @return Long and String objects
     */
    List<Object> getObjectMultiBulkReply();

    /**
     * Reply may contains Long and byte[] objects
     *
     * @return Long and byte[] objects
     */
    List<Object> getBinaryObjectMultiBulkReply();

    List<Object> getAll();

    List<Object> getBinaryAll();

    Object getOne();

    Object getBinaryOne();

    void sendCommand(final RedisCommand cmd, final String... args);

    void sendCommand(final RedisCommand cmd, final byte[]... args);

    void sendCommand(final RedisCommand cmd);
}
