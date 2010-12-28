package io.redis.rjc;

import io.redis.rjc.ds.DataSource;
import io.redis.rjc.util.SafeEncoder;

import java.util.*;

import static io.redis.rjc.protocol.Protocol.Keyword.*;

/**
 * @author Evgeny Dolgov
 */
public class RedisSubscriber {

    private Client client;
    private DataSource dataSource;
    private Thread responseThread;
    private int subscribedChannels = 0;
    private Map<String, MessageListener> listenerMap = Collections.synchronizedMap(new HashMap<String, MessageListener>());

    public RedisSubscriber() {
    }

    public RedisSubscriber(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void subscribe(String channel, MessageListener listener) {
        getClient().subscribe(channel);
        listenerMap.put(channel, listener);
    }

    private synchronized Client getClient() {
        if (client == null) {
            client = new Client(dataSource.getConnection());
            client.setTimeoutInfinite();
        }

        if (responseThread == null || responseThread.getState() == Thread.State.TERMINATED) {
            responseThread = new Thread(new ResponseRunnable());
            responseThread.start();
        }

        return client;
    }

    public synchronized void close() {
        if (client != null) {
            client.close();
            client = null;
        }
    }

    private class ResponseRunnable implements Runnable {
        public void run() {

            do {
                List<Object> reply;
                try {
                    reply = client.getObjectMultiBulkReply();
                } catch (RedisException e) {
                    return;
                }
                final Object firstObj = reply.get(0);
                if (!(firstObj instanceof byte[])) {
                    throw new RedisException("Unknown message type: " + firstObj);
                }
                final byte[] resp = (byte[]) firstObj;
                if (Arrays.equals(SUBSCRIBE.raw, resp)) {
                    subscribedChannels = ((Long) reply.get(2)).intValue();
                    final byte[] bchannel = (byte[]) reply.get(1);
                    final String strchannel = (bchannel == null) ? null
                            : SafeEncoder.encode(bchannel);
//                            onSubscribe(strchannel, subscribedChannels);
                } else if (Arrays.equals(UNSUBSCRIBE.raw, resp)) {
                    subscribedChannels = ((Long) reply.get(2)).intValue();
                    final byte[] bchannel = (byte[]) reply.get(1);
                    final String strchannel = (bchannel == null) ? null
                            : SafeEncoder.encode(bchannel);
//                            onUnsubscribe(strchannel, subscribedChannels);
                } else if (Arrays.equals(MESSAGE.raw, resp)) {
                    final byte[] bchannel = (byte[]) reply.get(1);
                    final byte[] bmesg = (byte[]) reply.get(2);
                    final String strchannel = (bchannel == null) ? null
                            : SafeEncoder.encode(bchannel);
                    final String strmesg = (bmesg == null) ? null : SafeEncoder
                            .encode(bmesg);
                    MessageListener listener = listenerMap.get(strchannel);
                    if (listener != null) {
                        listener.onMessage(strchannel, strmesg);
                    }
                } else if (Arrays.equals(PMESSAGE.raw, resp)) {
                    final byte[] bpattern = (byte[]) reply.get(1);
                    final byte[] bchannel = (byte[]) reply.get(2);
                    final byte[] bmesg = (byte[]) reply.get(3);
                    final String strpattern = (bpattern == null) ? null
                            : SafeEncoder.encode(bpattern);
                    final String strchannel = (bchannel == null) ? null
                            : SafeEncoder.encode(bchannel);
                    final String strmesg = (bmesg == null) ? null : SafeEncoder
                            .encode(bmesg);
//                            onPMessage(strpattern, strchannel, strmesg);
                } else if (Arrays.equals(PSUBSCRIBE.raw, resp)) {
                    subscribedChannels = ((Long) reply.get(2)).intValue();
                    final byte[] bpattern = (byte[]) reply.get(1);
                    final String strpattern = (bpattern == null) ? null
                            : SafeEncoder.encode(bpattern);
//                            onPSubscribe(strpattern, subscribedChannels);
                } else if (Arrays.equals(PUNSUBSCRIBE.raw, resp)) {
                    subscribedChannels = ((Long) reply.get(2)).intValue();
                    final byte[] bpattern = (byte[]) reply.get(1);
                    final String strpattern = (bpattern == null) ? null
                            : SafeEncoder.encode(bpattern);
//                            onPUnsubscribe(strpattern, subscribedChannels);
                } else {
                    throw new RedisException("Unknown message type: " + firstObj);
                }
            } while (subscribedChannels > 0);
            close();

        }
    }
}
