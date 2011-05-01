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

import org.idevlab.rjc.RedisClientImpl;
import org.idevlab.rjc.RedisException;
import org.idevlab.rjc.ds.DataSource;
import org.idevlab.rjc.protocol.RedisCommand;
import org.idevlab.rjc.protocol.RedisKeyword;
import org.idevlab.rjc.util.SafeEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author Evgeny Dolgov
 */
public class RedisNodeSubscriber {

    private final static Logger LOG = LoggerFactory.getLogger(RedisNodeSubscriber.class);

    private DataSource dataSource;
    private MessageListener messageListener;
    private PMessageListener pMessageListener;
    private Set<String> patterns = Collections.synchronizedSet(new HashSet<String>());
    private Set<String> channels = Collections.synchronizedSet(new HashSet<String>());
    private SubscribeListener subscribeListener;
    private RedisClientImpl client;
    private volatile boolean connected = false;

    public RedisNodeSubscriber() {
    }

    public RedisNodeSubscriber(DataSource dataSource) {
        this.dataSource = dataSource;
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

    public void subscribe(String... channels) {
        if (channels != null) {
            this.channels.addAll(Arrays.asList(channels));
            if (connected) {
                client.noReply(RedisCommand.SUBSCRIBE, channels);
            }
        }
    }

    public void psubscribe(String... patterns) {
        if (patterns != null) {
            this.patterns.addAll(Arrays.asList(patterns));
            if (connected) {
                client.noReply(RedisCommand.PSUBSCRIBE, patterns);
            }
        }
    }

    public void unsubscribe(String... channels) {
        if (channels != null) {
            this.channels.removeAll(Arrays.asList(channels));
            if (connected) {
                client.noReply(RedisCommand.UNSUBSCRIBE, channels);
            }
        }
    }

    public void unsubscribe() {
        this.channels.clear();
        if (connected) {
            client.noReply(RedisCommand.UNSUBSCRIBE);
        }
    }


    public void punsubscribe(String... pattern) {
        if (patterns != null) {
            this.patterns.removeAll(Arrays.asList(patterns));
            if (connected) {
                client.noReply(RedisCommand.PUNSUBSCRIBE, pattern);
            }
        }
    }

    public void punsubscribe() {
        this.patterns.clear();
        if (connected) {
            client.noReply(RedisCommand.PSUBSCRIBE);
        }
    }

    public void runSubscription() {
        close();

        client = new RedisClientImpl(this.dataSource.getConnection());
        client.setTimeoutInfinite();
        connected = true;

        if (channels != null && !channels.isEmpty()) {
            client.noReply(RedisCommand.SUBSCRIBE, channels.toArray(new String[channels.size()]));
        }

        if (patterns != null && !patterns.isEmpty()) {
            client.noReply(RedisCommand.PSUBSCRIBE, patterns.toArray(new String[patterns.size()]));
        }

        do {
            List<Object> reply;
            try {
                reply = client.getMultiBulkReply();
            } catch (Exception e) {
                break;
            }
            final Object firstObj = reply.get(0);
            if (!(firstObj instanceof String)) {
                throw new RedisException("Unknown message type: " + firstObj);
            }

            RedisKeyword keyword = RedisKeyword.find((String) firstObj);
            if (keyword == null) {
                throw new RedisException("Unknown pub/sub message: " + firstObj);
            }

            Long subscribedChannels = null;
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
                    subscribedChannels = (Long) reply.get(2);

                    if (subscribeListener != null) {
                        subscribeListener.onSubscribe(channel, subscribedChannels);
                    }
                    break;
                }
                case UNSUBSCRIBE: {
                    final String channel = (String) reply.get(1);
                    subscribedChannels = (Long) reply.get(2);
                    if (subscribeListener != null) {
                        subscribeListener.onUnsubscribe(channel, subscribedChannels);
                    }
                    break;
                }
                case PSUBSCRIBE: {
                    final String pattern = (String) reply.get(1);
                    subscribedChannels = (Long) reply.get(2);
                    if (subscribeListener != null) {
                        subscribeListener.onPSubscribe(pattern, subscribedChannels);
                    }
                    break;
                }
                case PUNSUBSCRIBE: {
                    final String pattern = (String) reply.get(1);
                    subscribedChannels = (Long) reply.get(2);
                    if (subscribeListener != null) {
                        subscribeListener.onPUnsubscribe(pattern, subscribedChannels);
                    }
                    break;
                }
                default: {
                    LOG.warn("Unknown message: {}", keyword.toString());
                }
            }

            if(subscribedChannels != null && subscribedChannels == 0) {
                break;
            }
        } while (true);

        LOG.debug("Subscriber is going out");
        close();
    }

    public boolean isConnected() {
        return connected;
    }

    public void close() {
        if (connected) {
            unsubscribe();
            punsubscribe();
            client.rollbackTimeout();
            client.close();
            LOG.debug("Subscriber is  closed");
        }
        connected = false;
    }

    private String byteToStr(byte[] bytes) {
        return (bytes == null) ? null : SafeEncoder.encode(bytes);
    }
}
