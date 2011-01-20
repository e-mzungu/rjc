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

/**
 * @author Evgeny Dolgov
 */
public interface SubscribeListener {
    /**
     * Means that we successfully subscribed to the given channel     *
     * @param channel subscribed channel
     * @param subscribedChannels the number of channels we are currently subscribed to
     */
    void onSubscribe(String channel, long subscribedChannels);

    /**
     * Means that we successfully unsubscribed from the given channel
     *
     * @param channel unsubscribed channel
     * @param subscribedChannels the number of channels we are currently subscribed to
     */
    void onUnsubscribe(String channel, long subscribedChannels);

    /**
     * Means that we successfully subscribed to the given pattern
     * @param pattern subscribed pattern
     * @param subscribedChannels the number of channels we are currently subscribed to
     */
    void onPSubscribe(String pattern, long subscribedChannels);

    /**
     * Means that we successfully unsubscribed from the given pattern
     *
     * @param pattern unsubscribed pattern
     * @param subscribedChannels the number of channels we are currently subscribed to
     */
    void onPUnsubscribe(String pattern, long subscribedChannels);
}
