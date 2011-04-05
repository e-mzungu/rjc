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

package org.idevlab.rjc.message;

import org.idevlab.rjc.Client;
import org.idevlab.rjc.RedisException;
import org.idevlab.rjc.ds.DataSource;
import org.idevlab.rjc.protocol.Protocol;
import org.idevlab.rjc.util.SafeEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Evgeny Dolgov
 */
public class RedisNodeSubscriber {

    private final static Logger LOG = LoggerFactory.getLogger(RedisNodeSubscriber.class);

    private DataSource dataSource;
    private MessageListener messageListener;
    private PMessageListener pMessageListener;
    private String[] patterns;
    private String[] channels;
    private SubscribeListener subscribeListener;
    private Client client;

    public RedisNodeSubscriber() {
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public MessageListener getMessageListener() {
        return messageListener;
    }

    public void setMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    public String[] getChannels() {
        return channels;
    }

    public void setChannels(String... channels) {
        this.channels = channels;
    }

    public SubscribeListener getSubscribeListener() {
        return subscribeListener;
    }

    public void setSubscribeListener(SubscribeListener subscribeListener) {
        this.subscribeListener = subscribeListener;
    }

    public PMessageListener getPMessageListener() {
        return pMessageListener;
    }

    public void setPMessageListener(PMessageListener pMessageListener) {
        this.pMessageListener = pMessageListener;
    }

    public String[] getPatterns() {
        return patterns;
    }

    public void setPatterns(String... patterns) {
        this.patterns = patterns;
    }

    public void unsubscribe(String... channels) {
        client.unsubscribe(channels);
    }

    public void punsubscribe(String... pattern) {
        client.punsubscribe(pattern);
    }

    public void subscribe() {
        client = new Client(this.dataSource.getConnection());
        client.setTimeoutInfinite();
        if (channels != null && channels.length > 0) {
            client.subscribe(channels);
        }

        if (patterns != null && patterns.length > 0) {
            client.psubscribe(patterns);
        }

        do {
            List<Object> reply;
            try {
                reply = client.getObjectMultiBulkReply();
            } catch (Exception e) {
                break;
            }
            final Object firstObj = reply.get(0);
            if (!(firstObj instanceof String)) {
                throw new RedisException("Unknown message type: " + firstObj);
            }

            Protocol.Keyword keyword = Protocol.Keyword.find((String) firstObj);
            if (keyword == null) {
                throw new RedisException("Unknown pub/sub message: " + firstObj);
            }

            switch (keyword) {
                case MESSAGE: {
                    final String channel = (String) reply.get(1);
                    final String message = (String) reply.get(2);
                    if (messageListener != null) {
                        messageListener.onMessage(channel, message);
                    }
                    break;
                }
                case PMESSAGE: {
                    final String pattern = (String) reply.get(1);
                    final String channel = (String) reply.get(2);
                    final String message = (String) reply.get(3);
                    if (pMessageListener != null) {
                        pMessageListener.onMessage(pattern, channel, message);
                    }
                    break;
                }
                case SUBSCRIBE: {
                    final String channel = (String) reply.get(1);
                    final Long subscribedChannels = (Long) reply.get(2);

                    if (subscribeListener != null) {
                        subscribeListener.onSubscribe(channel, subscribedChannels);
                    }
                    break;
                }
                case UNSUBSCRIBE: {
                    final String channel = (String) reply.get(1);
                    final Long subscribedChannels = (Long) reply.get(2);
                    if (subscribeListener != null) {
                        subscribeListener.onUnsubscribe(channel, subscribedChannels);
                    }
                    break;
                }
                case PSUBSCRIBE: {
                    final String pattern = (String) reply.get(1);
                    final Long subscribedChannels = (Long) reply.get(2);
                    if (subscribeListener != null) {
                        subscribeListener.onPSubscribe(pattern, subscribedChannels);
                    }
                    break;
                }
                case PUNSUBSCRIBE: {
                    final String pattern = (String) reply.get(1);
                    final Long subscribedChannels = (Long) reply.get(2);
                    if (subscribeListener != null) {
                        subscribeListener.onPUnsubscribe(pattern, subscribedChannels);
                    }
                    break;
                }
                default: {
                    LOG.warn("Unknown message: {}", keyword.toString());
                }
            }
        } while (true);

        LOG.debug("Subscriber is going out");
        close();
    }

    public void close() {
        client.unsubscribe();
        client.punsubscribe();
        client.rollbackTimeout();
        client.close();
        LOG.debug("Subscriber is  closed");
    }

    private String byteToStr(byte[] bytes) {
        return (bytes == null) ? null : SafeEncoder.encode(bytes);
    }
}
