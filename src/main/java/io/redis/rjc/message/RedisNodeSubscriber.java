package io.redis.rjc.message;

import io.redis.rjc.Client;
import io.redis.rjc.RedisException;
import io.redis.rjc.ds.DataSource;
import io.redis.rjc.protocol.Protocol;
import io.redis.rjc.util.SafeEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author Evgeny Dolgov
 */
public class RedisNodeSubscriber implements RedisSubscriber {

    private final static Logger LOG = LoggerFactory.getLogger(RedisNodeSubscriber.class);

    private Client client;
    private DataSource dataSource;
    private Thread responseThread;
    private final Map<String, MessageListener> msgListenerMap = Collections.synchronizedMap(new HashMap<String, MessageListener>());
    private final Map<String, PMessageListener> pmsgListenerMap = Collections.synchronizedMap(new HashMap<String, PMessageListener>());
    private final Set<SubscribeListener> subListenerSet = Collections.synchronizedSet(new HashSet<SubscribeListener>());

    public RedisNodeSubscriber() {
    }

    public RedisNodeSubscriber(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void subscribe(String channel, MessageListener listener) {
        getClient().subscribe(channel);
        msgListenerMap.put(channel, listener);
    }

    public void psubscribe(String pattern, PMessageListener listener) {
        getClient().psubscribe(pattern);
        pmsgListenerMap.put(pattern, listener);
    }

    public void unsubscribe(String... channels) {
        getClient().unsubscribe(channels);
        for (String channel : channels) {
            msgListenerMap.remove(channel);
        }
    }

    public void punsubscribe(String... patterns) {
        getClient().punsubscribe(patterns);
        for (String pattern : patterns) {
            pmsgListenerMap.remove(pattern);
        }
    }

    public void addListener(SubscribeListener listener) {
        subListenerSet.add(listener);
    }

    public void removeListener(SubscribeListener listener) {
        subListenerSet.remove(listener);
    }

    private synchronized Client getClient() {
        if (client == null || !client.isConnected()) {
            client = new Client(dataSource.getConnection());
            client.setTimeoutInfinite();
        }

        if (responseThread == null || responseThread.getState() == Thread.State.TERMINATED ||
                responseThread.isInterrupted()) {
            responseThread = new ResponseThread(client);
            responseThread.start();
        }

        return client;
    }

    /**
     * Closes connection and clean ups listeners
     */
    public synchronized void close() {
        msgListenerMap.clear();
        pmsgListenerMap.clear();
        subListenerSet.clear();

        if (client != null) {
            client.close();
            client = null;
        }

        if (responseThread != null && responseThread.getState() != Thread.State.TERMINATED) {
            responseThread.interrupt();
        }

        responseThread = null;
    }

    private class ResponseThread extends Thread {

        private Client _client;

        private ResponseThread(Client client) {

            this._client = client;
        }

        public void run() {
            do {
                List<Object> reply;
                try {
                    reply = _client.getObjectMultiBulkReply();
                } catch (RedisException e) {
                    return;
                }
                final Object firstObj = reply.get(0);
                if (!(firstObj instanceof byte[])) {
                    throw new RedisException("Unknown message type: " + firstObj);
                }

                Protocol.Keyword keyword = Protocol.Keyword.find((byte[]) firstObj);
                if (keyword == null) {
                    throw new RedisException("Unknown pub/sub message: " + byteToStr((byte[]) firstObj));
                }

                switch (keyword) {
                    case MESSAGE: {
                        final String channel = byteToStr((byte[]) reply.get(1));
                        final String message = byteToStr((byte[]) reply.get(2));
                        MessageListener listener = msgListenerMap.get(channel);
                        if (listener != null) {
                            listener.onMessage(channel, message);
                        }
                        break;
                    }
                    case PMESSAGE: {
                        final String pattern = byteToStr((byte[]) reply.get(1));
                        final String channel = byteToStr((byte[]) reply.get(2));
                        final String message = byteToStr((byte[]) reply.get(3));
                        PMessageListener listener = pmsgListenerMap.get(pattern);
                        if (listener != null) {
                            listener.onMessage(pattern, channel, message);
                        }
                        break;
                    }
                    case SUBSCRIBE: {
                        final String channel = byteToStr((byte[]) reply.get(1));
                        final Long subscribedChannels = (Long) reply.get(2);
                        synchronized (subListenerSet) {
                            for (SubscribeListener listener : subListenerSet) {
                                listener.onSubscribe(channel, subscribedChannels);
                            }
                        }
                        break;
                    }
                    case UNSUBSCRIBE: {
                        final String channel = byteToStr((byte[]) reply.get(1));
                        final Long subscribedChannels = (Long) reply.get(2);
                        synchronized (subListenerSet) {
                            for (SubscribeListener listener : subListenerSet) {
                                listener.onUnsubscribe(channel, subscribedChannels);
                            }
                        }
                        break;
                    }
                    case PSUBSCRIBE: {
                        final String pattern = byteToStr((byte[]) reply.get(1));
                        final Long subscribedChannels = (Long) reply.get(2);
                        synchronized (subListenerSet) {
                            for (SubscribeListener listener : subListenerSet) {
                                listener.onPSubscribe(pattern, subscribedChannels);
                            }
                        }
                        break;
                    }
                    case PUNSUBSCRIBE: {
                        final String pattern = byteToStr((byte[]) reply.get(1));
                        final Long subscribedChannels = (Long) reply.get(2);
                        synchronized (subListenerSet) {
                            for (SubscribeListener listener : subListenerSet) {
                                listener.onPUnsubscribe(pattern, subscribedChannels);
                            }
                        }
                        break;
                    }
                    default: {
                        LOG.warn("Unknown message: {}", keyword.toString());
                    }
                }
            } while (!isInterrupted());
        }

        private String byteToStr(byte[] bytes) {
            return (bytes == null) ? null : SafeEncoder.encode(bytes);
        }
    }
}
