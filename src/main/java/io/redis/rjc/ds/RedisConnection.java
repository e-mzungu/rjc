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

package io.redis.rjc.ds;

import io.redis.rjc.protocol.Protocol;

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

    @SuppressWarnings("unchecked")
    List<byte[]> getBinaryMultiBulkReply();

    @SuppressWarnings("unchecked")
    List<Object> getObjectMultiBulkReply();

    List<Object> getAll();

    Object getOne();

    void sendCommand(final Protocol.Command cmd, final String... args);

    void sendCommand(final Protocol.Command cmd, final byte[]... args);

    void sendCommand(final Protocol.Command cmd);
}
