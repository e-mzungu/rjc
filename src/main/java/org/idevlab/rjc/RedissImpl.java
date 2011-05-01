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

import org.idevlab.rjc.ds.RedisConnection;
import org.idevlab.rjc.protocol.RedisCommand;

import java.util.List;

/**
 * @author Evgeny Dolgov
 */
public class RedissImpl implements Redis {

    private RedisConnection connection;

    public RedissImpl() {
    }

    public RedissImpl(RedisConnection connection) {
        this.connection = connection;
    }

    public RedisConnection getConnection() {
        return connection;
    }

    public void setConnection(RedisConnection connection) {
        this.connection = connection;
    }

    public String getStatusReply(RedisCommand command) {
        connection.sendCommand(command);
        return connection.getStatusCodeReply();
    }

    public String getStatusReply(RedisCommand command, String... args) {
        connection.sendCommand(command, args);
        return connection.getStatusCodeReply();
    }

    public String getStatusReply(RedisCommand command, byte[]... args) {
        connection.sendCommand(command, args);
        return connection.getStatusCodeReply();
    }

    public Long getIntegerReply(RedisCommand command) {
        connection.sendCommand(command);
        return connection.getIntegerReply();
    }

    public Long getIntegerReply(RedisCommand command, String... args) {
        connection.sendCommand(command, args);
        return connection.getIntegerReply();
    }

    public Long getIntegerReply(RedisCommand command, byte[]... args) {
        connection.sendCommand(command, args);
        return connection.getIntegerReply();
    }

    public String getBulkReply(RedisCommand command) {
        connection.sendCommand(command);
        return connection.getBulkReply();
    }

    public String getBulkReply(RedisCommand command, String... args) {
        connection.sendCommand(command, args);
        return connection.getBulkReply();
    }

    public byte[] getBulkReply(RedisCommand command, byte[]... args) {
        connection.sendCommand(command, args);
        return connection.getBinaryBulkReply();
    }

    public byte[] getBinaryBulkReply(RedisCommand command) {
        connection.sendCommand(command);
        return connection.getBinaryBulkReply();
    }

    public List<Object> getMultiBulkReply(RedisCommand command, String... args) {
        connection.sendCommand(command, args);
        return connection.getObjectMultiBulkReply();
    }

    public List<String> getStringMultiBulkReply(RedisCommand command, String... args) {
        connection.sendCommand(command, args);
        return connection.getMultiBulkReply();
    }

    public List<Object> getBinaryMultiBulkReply(RedisCommand command, byte[]... args) {
        connection.sendCommand(command, args);
        return connection.getObjectMultiBulkReply();
    }

    public void noReply(RedisCommand command) {
        connection.sendCommand(command);
    }

    public void close() {
        connection.close();
    }
}
