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

package org.idevlab.rjc.message;

import org.idevlab.rjc.sharding.NodeLocator;

/**
 * @author Evgeny Dolgov
 */
public class ShardedRedisSubscriber implements RedisSubscriber {

    private NodeLocator<RedisNodeSubscriber> locator;

    public ShardedRedisSubscriber() {
    }

    public ShardedRedisSubscriber(NodeLocator<RedisNodeSubscriber> locator) {
        this.locator = locator;
    }

    public void setLocator(NodeLocator<RedisNodeSubscriber> locator) {
        this.locator = locator;
    }

    public void subscribe(String channel, MessageListener listener) {
        locator.getNode(channel).subscribe(channel, listener);
    }

    public void psubscribe(String pattern, PMessageListener listener) {
        for (RedisNodeSubscriber subscriber : locator.getNodes()) {
            subscriber.psubscribe(pattern, listener);
        }
    }

    public void unsubscribe(String... channels) {
        for (String channel : channels) {
            locator.getNode(channel).unsubscribe(channel);
        }
    }

    public void punsubscribe(String... patterns) {
        for (RedisNodeSubscriber subscriber : locator.getNodes()) {
            for (String pattern : patterns) {
                subscriber.punsubscribe(pattern);
            }
        }
    }

    public void addListener(SubscribeListener listener) {
        for (RedisNodeSubscriber subscriber : locator.getNodes()) {
            subscriber.addListener(listener);
        }
    }

    public void removeListener(SubscribeListener listener) {
        for (RedisNodeSubscriber subscriber : locator.getNodes()) {
            subscriber.removeListener(listener);
        }
    }

    public void close() {
        for (RedisNodeSubscriber subscriber : locator.getNodes()) {
            subscriber.close();
        }
    }
}
