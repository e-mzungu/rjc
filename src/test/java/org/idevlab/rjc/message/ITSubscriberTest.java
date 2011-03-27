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

import org.idevlab.rjc.SingleNodeTestBase;
import org.idevlab.rjc.ds.SimpleDataSource;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Evgeny Dolgov
 */
public class ITSubscriberTest extends SingleNodeTestBase {

    @Test
    public void subscribe() throws InterruptedException {
        for (int cnt = 0; cnt < 5; cnt++) {
            subscribe("c1", "c*");
        }

        subscribe(null, "c*");
        subscribe("c1", null);
        subscribe(null, null);
    }

    @Test
    public void unsubscribe() throws InterruptedException {
        final List<String> msgReceive = Collections.synchronizedList(new ArrayList<String>());
        final List<String> pmsgReceive = Collections.synchronizedList(new ArrayList<String>());

        final RedisNodeSubscriber subscriber = new RedisNodeSubscriber();
        subscriber.setChannels("c1", "c22");
        subscriber.setPatterns("c*", "c2*");
        subscriber.setDataSource(new SimpleDataSource(hnp.host, hnp.port));
        subscriber.setMessageListener(new MessageListener() {
            public void onMessage(String channel, String message) {
                msgReceive.add(message);
            }
        });
        subscriber.setPMessageListener(new PMessageListener() {
            public void onMessage(String pattern, String channel, String message) {
                pmsgReceive.add(message);
            }
        });

        Thread t = new Thread(new Runnable() {
            public void run() {
                subscriber.subscribe();
            }
        });
        t.start();

        Thread.sleep(500);

        for (int i = 0; i < 5; i++) {
            session.publish("c1", String.valueOf(i));
            session.publish("c22", String.valueOf(i));
        }

        subscriber.unsubscribe("c1");
        subscriber.punsubscribe("c2*");

        for (int i = 0; i < 5; i++) {
            session.publish("c1", String.valueOf(i));
            session.publish("c22", String.valueOf(i));
        }

        waitForMessages(msgReceive, 15);
        waitForMessages(pmsgReceive, 25);

        subscriber.close();
        t.join();

        Assert.assertEquals(15, msgReceive.size());
        Assert.assertEquals(25, pmsgReceive.size());

    }

    private void subscribe(String channel, String pattern) throws InterruptedException {
        final List<String> msgReceive = Collections.synchronizedList(new ArrayList<String>());
        final List<String> pmsgReceive = Collections.synchronizedList(new ArrayList<String>());
        final boolean[] onSubscribe = {false, false};

        final RedisNodeSubscriber subscriber = new RedisNodeSubscriber();
        if (channel != null) {
            subscriber.setChannels(channel);
        }
        subscriber.setDataSource(new SimpleDataSource(hnp.host, hnp.port));
        subscriber.setMessageListener(new MessageListener() {
            public void onMessage(String channel, String message) {
                msgReceive.add(message);
            }
        });
        subscriber.setSubscribeListener(new SubscribeListener() {
            public void onSubscribe(String channel, long subscribedChannels) {
                onSubscribe[0] = true;
            }

            public void onUnsubscribe(String channel, long subscribedChannels) {
            }

            public void onPSubscribe(String pattern, long subscribedChannels) {
                onSubscribe[1] = true;
            }

            public void onPUnsubscribe(String pattern, long subscribedChannels) {
            }
        });
        subscriber.setPMessageListener(new PMessageListener() {
            public void onMessage(String pattern, String channel, String message) {
                pmsgReceive.add(message);
            }
        });
        if (pattern != null) {
            subscriber.setPatterns(pattern);
        }


        Thread t = new Thread(new Runnable() {
            public void run() {
                subscriber.subscribe();
            }
        });
        t.start();

        Thread.sleep(500);

        List<String> msgCSend = new ArrayList<String>();
        if (channel != null) {
            for (int i = 0; i < 100; i++) {
                msgCSend.add(String.valueOf(i));
                session.publish(channel, String.valueOf(i));
            }
        }

        waitForMessages(msgReceive, msgCSend.size());
        waitForMessages(pmsgReceive, msgCSend.size());

        subscriber.close();
        t.join();

        Assert.assertArrayEquals(msgCSend.toArray(), msgReceive.toArray());
        if (channel != null) {
            Assert.assertTrue("No onSubscribe event", onSubscribe[0]);
        }

        if (pattern != null) {
            Assert.assertTrue("No onPSubscribe event", onSubscribe[1]);
        }
    }

    private void waitForMessages(List<String> msgReceive, int waitingSize) {
        long startTime = System.currentTimeMillis();
        while (waitingSize != msgReceive.size() && System.currentTimeMillis() - startTime < 3000) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
