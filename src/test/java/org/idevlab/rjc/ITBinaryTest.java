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

import org.idevlab.rjc.ds.SimpleDataSource;
import org.idevlab.rjc.protocol.RedisCommand;
import org.idevlab.rjc.protocol.RedisKeyword;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Evgeny Dolgov
 */
public class ITBinaryTest {

    private static HostAndPortUtil.HostAndPort hnp = HostAndPortUtil.getRedisServers().get(0);

    private RedisClient client;

    @Before
    public void setUp() throws Exception {
        client = new RedisClientImpl(new SimpleDataSource(hnp.host, hnp.port, 2000).getConnection());
        client.getStatusReply(RedisCommand.FLUSHALL);
    }

    @After
    public void tearDown() throws Exception {
        client.close();
    }

    @Test
    public void noReplyTest() throws InterruptedException {
        byte[] c1 = new byte[]{1,2,3};
        byte[] c2 = new byte[]{4,5,6};
        client.noReply(RedisCommand.SUBSCRIBE, c1, c2);

        final List<Object> result = new ArrayList<Object>();
        Thread t = new Thread(new Runnable() {
            public void run() {
                result.addAll(client.getBinaryMultiBulkReply());
                result.addAll(client.getBinaryMultiBulkReply());
                result.addAll(client.getBinaryMultiBulkReply());
            }
        });
        t.start();

        Thread.sleep(500);

        RedisClient client2 = new RedisClientImpl(new SimpleDataSource(hnp.host, hnp.port, 2000).getConnection());
        byte[] msg = new byte[]{7,8,9};
        client2.getIntegerReply(RedisCommand.PUBLISH, c1, msg);
        client2.close();

        t.join();


        Assert.assertArrayEquals(RedisKeyword.SUBSCRIBE.raw, (byte[]) result.get(0));
        Assert.assertArrayEquals(c1, (byte[]) result.get(1));
        Assert.assertEquals(1L, result.get(2));
        Assert.assertArrayEquals(RedisKeyword.SUBSCRIBE.raw, (byte[]) result.get(3));
        Assert.assertArrayEquals(c2, (byte[]) result.get(4));
        Assert.assertEquals(2L, result.get(5));
        Assert.assertArrayEquals(RedisKeyword.MESSAGE.raw, (byte[]) result.get(6));
        Assert.assertArrayEquals(c1, (byte[]) result.get(7));
        Assert.assertArrayEquals(msg, (byte[]) result.get(8));
    }

    @Test
    public void statusBulkReplyTest() {
        byte[] key = new byte[]{1,2,3};
        byte[] value = new byte[]{4,5,6};
        Assert.assertEquals("OK", client.getStatusReply(RedisCommand.SET, key, value));
        Assert.assertArrayEquals(value, client.getBinaryBulkReply(RedisCommand.GET, key));
        Assert.assertArrayEquals(key, client.getBinaryBulkReply(RedisCommand.RANDOMKEY));
    }

    @Test
    public void multiBulkReplyTest() {
        byte[] key1 = new byte[]{1,2,3};
        byte[] key2 = new byte[]{1,2,3,4};
        byte[] value1 = new byte[]{4,5,6};
        byte[] value2 = new byte[]{4,5,6,7};

        client.getStatusReply(RedisCommand.MULTI);

        Assert.assertEquals("QUEUED", client.getStatusReply(RedisCommand.SADD, key1, value1));
        Assert.assertEquals("QUEUED", client.getStatusReply(RedisCommand.SADD, key1, value2));
        Assert.assertEquals("QUEUED", client.getStatusReply(RedisCommand.SCARD, key1));

        Assert.assertEquals("QUEUED", client.getStatusReply(RedisCommand.SET, key2, value1));
        Assert.assertEquals("QUEUED", client.getStatusReply(RedisCommand.GET, key2));

        List<Object> response = client.getBinaryMultiBulkReply(RedisCommand.EXEC);
        Assert.assertEquals(1L, response.get(0));
        Assert.assertEquals(1L, response.get(1));
        Assert.assertEquals(2L, response.get(2));
        Assert.assertEquals("OK", response.get(3));
        Assert.assertArrayEquals(value1, (byte[]) response.get(4));
    }

    @Test
    public void allTest() {
        byte[] key = new byte[]{1,2,3};
        byte[] value = new byte[]{4,5,6};
        client.noReply(RedisCommand.SET, key, value);
        client.noReply(RedisCommand.GET, key);

        List<Object> response = client.getBinaryAll();
        Assert.assertEquals("OK", response.get(0));
        Assert.assertArrayEquals(value, (byte[]) response.get(1));
    }
}
