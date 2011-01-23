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

package org.idevlab.rjc;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Evgeny Dolgov
 */
public class ITListsCommandsTest extends SingleNodeTestBase {
    @Test
    public void rpush() {
        long size = session.rpush("foo", "bar");
        assertEquals(1, size);
        size = session.rpush("foo", "foo");
        assertEquals(2, size);
    }

    @Test
    public void lpush() {
        long size = session.lpush("foo", "bar");
        assertEquals(1, size);
        size = session.lpush("foo", "foo");
        assertEquals(2, size);
    }

    @Test
    public void llen() {
        assertEquals(0, session.llen("foo").intValue());
        session.lpush("foo", "bar");
        session.lpush("foo", "car");
        assertEquals(2, session.llen("foo").intValue());
    }

    @Test
    public void llenNotOnList() {
        try {
            session.set("foo", "bar");
            session.llen("foo");
            fail("sessionException expected");
        } catch (final RedisException e) {
        }
    }

    @Test
    public void lrange() {
        session.rpush("foo", "a");
        session.rpush("foo", "b");
        session.rpush("foo", "c");

        List<String> expected = new ArrayList<String>();
        expected.add("a");
        expected.add("b");
        expected.add("c");

        List<String> range = session.lrange("foo", 0, 2);
        assertEquals(expected, range);

        range = session.lrange("foo", 0, 20);
        assertEquals(expected, range);

        expected = new ArrayList<String>();
        expected.add("b");
        expected.add("c");

        range = session.lrange("foo", 1, 2);
        assertEquals(expected, range);

        expected = new ArrayList<String>();
        range = session.lrange("foo", 2, 1);
        assertEquals(expected, range);
    }

    @Test
    public void ltrim() {
        session.lpush("foo", "1");
        session.lpush("foo", "2");
        session.lpush("foo", "3");
        String status = session.ltrim("foo", 0, 1);

        List<String> expected = new ArrayList<String>();
        expected.add("3");
        expected.add("2");

        assertEquals("OK", status);
        assertEquals(2, session.llen("foo").intValue());
        assertEquals(expected, session.lrange("foo", 0, 100));
    }

    @Test
    public void lindex() {
        session.lpush("foo", "1");
        session.lpush("foo", "2");
        session.lpush("foo", "3");

        List<String> expected = new ArrayList<String>();
        expected.add("3");
        expected.add("bar");
        expected.add("1");

        String status = session.lset("foo", 1, "bar");

        assertEquals("OK", status);
        assertEquals(expected, session.lrange("foo", 0, 100));
    }

    @Test
    public void lset() {
        session.lpush("foo", "1");
        session.lpush("foo", "2");
        session.lpush("foo", "3");

        assertEquals("3", session.lindex("foo", 0));
        assertEquals(null, session.lindex("foo", 100));
    }

    @Test
    public void lrem() {
        session.lpush("foo", "hello");
        session.lpush("foo", "hello");
        session.lpush("foo", "x");
        session.lpush("foo", "hello");
        session.lpush("foo", "c");
        session.lpush("foo", "b");
        session.lpush("foo", "a");

        long count = session.lrem("foo", -2, "hello");

        List<String> expected = new ArrayList<String>();
        expected.add("a");
        expected.add("b");
        expected.add("c");
        expected.add("hello");
        expected.add("x");

        assertEquals(2, count);
        assertEquals(expected, session.lrange("foo", 0, 1000));
        assertEquals(0, session.lrem("bar", 100, "foo").intValue());
    }

    @Test
    public void lpop() {
        session.rpush("foo", "a");
        session.rpush("foo", "b");
        session.rpush("foo", "c");

        String element = session.lpop("foo");
        assertEquals("a", element);

        List<String> expected = new ArrayList<String>();
        expected.add("b");
        expected.add("c");

        assertEquals(expected, session.lrange("foo", 0, 1000));
        session.lpop("foo");
        session.lpop("foo");

        element = session.lpop("foo");
        assertEquals(null, element);
    }

    @Test
    public void rpop() {
        session.rpush("foo", "a");
        session.rpush("foo", "b");
        session.rpush("foo", "c");

        String element = session.rpop("foo");
        assertEquals("c", element);

        List<String> expected = new ArrayList<String>();
        expected.add("a");
        expected.add("b");

        assertEquals(expected, session.lrange("foo", 0, 1000));
        session.rpop("foo");
        session.rpop("foo");

        element = session.rpop("foo");
        assertEquals(null, element);
    }

    @Test
    public void rpoplpush() {
        session.rpush("foo", "a");
        session.rpush("foo", "b");
        session.rpush("foo", "c");

        session.rpush("dst", "foo");
        session.rpush("dst", "bar");

        String element = session.rpoplpush("foo", "dst");

        assertEquals("c", element);

        List<String> srcExpected = new ArrayList<String>();
        srcExpected.add("a");
        srcExpected.add("b");

        List<String> dstExpected = new ArrayList<String>();
        dstExpected.add("c");
        dstExpected.add("foo");
        dstExpected.add("bar");

        assertEquals(srcExpected, session.lrange("foo", 0, 1000));
        assertEquals(dstExpected, session.lrange("dst", 0, 1000));
    }

    @Test
    public void blpop() throws InterruptedException {
        List<String> result = session.blpop(1, "foo");
        assertNull(result);

        new Thread(new Runnable() {
            public void run() {
                try {
                    Session j = createSession();
                    j.lpush("foo", "bar");
                    j.close();
                } catch (Exception ex) {
                    fail(ex.getMessage());
                }
            }
        }).start();

        result = session.blpop(1, "foo");
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("foo", result.get(0));
        assertEquals("bar", result.get(1));
    }

    @Test
    public void brpop() throws InterruptedException {
        List<String> result = session.brpop(1, "foo");
        assertNull(result);

        new Thread(new Runnable() {
            public void run() {
                try {
                    Session j = createSession();
                    j.lpush("foo", "bar");
                    j.close();
                } catch (Exception ex) {
                    fail(ex.getMessage());
                }
            }
        }).start();

        result = session.brpop(1, "foo");
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("foo", result.get(0));
        assertEquals("bar", result.get(1));
    }

    @Test
    public void lpushx() {
        long status = session.lpushx("foo", "bar");
        assertEquals(0, status);

        session.lpush("foo", "a");
        status = session.lpushx("foo", "b");
        assertEquals(2, status);
    }

    @Test
    public void rpushx() {
        long status = session.rpushx("foo", "bar");
        assertEquals(0, status);

        session.lpush("foo", "a");
        status = session.rpushx("foo", "b");
        assertEquals(2, status);
    }

    @Test
    public void linsert() {
        long status = session.linsert("foo", Client.LIST_POSITION.BEFORE, "bar",
                "car");
        assertEquals(0, status);

        session.lpush("foo", "a");
        status = session.linsert("foo", Client.LIST_POSITION.AFTER, "a", "b");
        assertEquals(2, status);

        List<String> actual = session.lrange("foo", 0, 100);
        List<String> expected = new ArrayList<String>();
        expected.add("a");
        expected.add("b");

        assertEquals(expected, actual);

        status = session.linsert("foo", Client.LIST_POSITION.BEFORE, "bar", "car");
        assertEquals(-1, status);
    }

    @Test
    public void brpoplpush() {
        (new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(100);
                    Session j = createSession();
                    j.lpush("foo", "a");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        })).start();

        String element = session.brpoplpush("foo", "bar", 0);

        assertEquals("a", element);
        assertEquals(1, session.llen("bar").longValue());
        assertEquals("a", session.lrange("bar", 0, -1).get(0));
    }
}
