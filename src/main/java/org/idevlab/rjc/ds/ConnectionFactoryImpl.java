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

import org.idevlab.rjc.RedisException;
import org.idevlab.rjc.protocol.RedisCommand;

/**
 * @author Evgeny Dolgov
 */
class ConnectionFactoryImpl implements ConnectionFactory {
    private final String host;
    private final int port;
    private final int timeout;
    private final String password;

    public ConnectionFactoryImpl(String host, int port, int timeout, String password) {
        this.host = host;
        this.port = port;
        this.timeout = (timeout > 0) ? timeout : -1;
        this.password = password;
    }


    public RedisConnection create() throws Exception {
        final RedisConnectionImpl redis;
        redis = new RedisConnectionImpl(this.host, this.port);
        if (timeout > 0) {
            redis.setTimeout(timeout);
        }


        redis.connect();
        if (null != this.password) {
            redis.sendCommand(RedisCommand.AUTH);
            if (!"OK".equals(redis.getStatusCodeReply())) {
                throw new RedisException("Authentication failed");
            }
        }
        return redis;
    }
}
