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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Evgeny Dolgov
 */
public class ITStringCommandsTest extends SingleNodeTestBase {
    @Test
    public void setAndGet() {
        String status = session.set("foo", "bar");
        assertEquals("OK", status);

        String value = session.get("foo");
        assertEquals("bar", value);

        assertEquals(null, session.get("bar"));
    }

    @Test
    public void getSet() {
        String value = session.getSet("foo", "bar");
        assertEquals(null, value);
        value = session.get("foo");
        assertEquals("bar", value);
    }

    @Test
    public void mget() {
        List<String> values = session.mget("foo", "bar");
        List<String> expected = new ArrayList<String>();
        expected.add(null);
        expected.add(null);

        assertEquals(expected, values);

        session.set("foo", "bar");

        expected = new ArrayList<String>();
        expected.add("bar");
        expected.add(null);
        values = session.mget("foo", "bar");

        assertEquals(expected, values);

        session.set("bar", "foo");

        expected = new ArrayList<String>();
        expected.add("bar");
        expected.add("foo");
        values = session.mget("foo", "bar");

        assertEquals(expected, values);
    }

    @Test
    public void setnx() {
        boolean status = session.setnx("foo", "bar");
        assertTrue(status);
        assertEquals("bar", session.get("foo"));

        status = session.setnx("foo", "bar2");
        assertFalse(status);
        assertEquals("bar", session.get("foo"));
    }

    @Test
    public void setex() {
        String status = session.setex("foo", 20, "bar");
        assertEquals("OK", status);
        long ttl = session.ttl("foo");
        assertTrue(ttl > 0 && ttl <= 20);
    }

    @Test
    public void mset() {
        String status = session.mset("foo", "bar", "bar", "foo");
        assertEquals("OK", status);
        assertEquals("bar", session.get("foo"));
        assertEquals("foo", session.get("bar"));
    }

    @Test
    public void msetnx() {
        boolean status = session.msetnx("foo", "bar", "bar", "foo");
        assertTrue(status);
        assertEquals("bar", session.get("foo"));
        assertEquals("foo", session.get("bar"));

        status = session.msetnx("foo", "bar1", "bar2", "foo2");
        assertFalse(status);
        assertEquals("bar", session.get("foo"));
        assertEquals("foo", session.get("bar"));
    }

    @Test(expected = RedisException.class)
    public void incrWrongValue() {
        session.set("foo", "bar");
        session.incr("foo");
    }

    @Test
    public void incr() {
        long value = session.incr("foo");
        assertEquals(1, value);
        value = session.incr("foo");
        assertEquals(2, value);
    }

    @Test(expected = RedisException.class)
    public void incrByWrongValue() {
        session.set("foo", "bar");
        session.incrBy("foo", 2);
    }

    @Test
    public void incrBy() {
        long value = session.incrBy("foo", 2);
        assertEquals(2, value);
        value = session.incrBy("foo", 2);
        assertEquals(4, value);
    }

    @Test(expected = RedisException.class)
    public void decrWrongValue() {
        session.set("foo", "bar");
        session.decr("foo");
    }

    @Test
    public void decr() {
        long value = session.decr("foo");
        assertEquals(-1, value);
        value = session.decr("foo");
        assertEquals(-2, value);
    }

    @Test(expected = RedisException.class)
    public void decrByWrongValue() {
        session.set("foo", "bar");
        session.decrBy("foo", 2);
    }

    @Test
    public void decrBy() {
        long value = session.decrBy("foo", 2);
        assertEquals(-2, value);
        value = session.decrBy("foo", 2);
        assertEquals(-4, value);
    }

    @Test
    public void append() {
        long value = session.append("foo", "bar");
        assertEquals(3, value);
        assertEquals("bar", session.get("foo"));
        value = session.append("foo", "bar");
        assertEquals(6, value);
        assertEquals("barbar", session.get("foo"));
    }

    @Test
    public void strlen() {
        session.set("s", "This is a string");
        assertEquals("This is a string".length(), session.strlen("s").intValue());
    }

    @Test
    public void incrLargeNumbers() {
        long value = session.incr("foo");
        assertEquals(1, value);
        assertEquals(1L + Integer.MAX_VALUE, (long) session.incrBy("foo",
                Integer.MAX_VALUE));
    }

    @Test(expected = RedisException.class)
    public void incrReallyLargeNumbers() {
        session.set("foo", Long.toString(Long.MAX_VALUE));
        long value = session.incr("foo");
        assertEquals(Long.MIN_VALUE, value);
    }


    @Test
    public void getRange() {
        session.set("foo", "This is a string");
        assertEquals("This", session.getRange("foo", 0, 3));
        assertEquals("ing", session.getRange("foo", -3, -1));
        assertEquals("This is a string", session.getRange("foo", 0, -1));
        assertEquals("string", session.getRange("foo", 10, 100));
    }

    @Test
    public void setRange() {
        session.set("key1", "Hello World");
        assertEquals(11L, session.setRange("key1", 6, "Redis").longValue());
        assertEquals("Hello Redis", session.get("key1"));
    }
}
