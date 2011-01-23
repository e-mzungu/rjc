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

import java.util.*;

import static org.junit.Assert.*;

/**
 * @author Evgeny Dolgov
 */
public class ITHashesCommandsTest extends SingleNodeTestBase {
     @Test
    public void hset() {
        boolean status = session.hset("foo", "bar", "car");
        assertTrue(status);
        status = session.hset("foo", "bar", "foo");
        assertFalse(status);
    }

    @Test
    public void hget() {
        session.hset("foo", "bar", "car");
        assertEquals(null, session.hget("bar", "foo"));
        assertEquals(null, session.hget("foo", "car"));
        assertEquals("car", session.hget("foo", "bar"));
    }

    @Test
    public void hsetnx() {
        boolean status = session.hsetnx("foo", "bar", "car");
        assertTrue(status);
        assertEquals("car", session.hget("foo", "bar"));

        status = session.hsetnx("foo", "bar", "foo");
        assertFalse(status);
        assertEquals("car", session.hget("foo", "bar"));

        status = session.hsetnx("foo", "car", "bar");
        assertTrue(status);
        assertEquals("bar", session.hget("foo", "car"));
    }

    @Test
    public void hmset() {
        Map<String, String> hash = new HashMap<String, String>();
        hash.put("bar", "car");
        hash.put("car", "bar");
        String status = session.hmset("foo", hash);
        assertEquals("OK", status);
        assertEquals("car", session.hget("foo", "bar"));
        assertEquals("bar", session.hget("foo", "car"));
    }

    @Test
    public void hmget() {
        Map<String, String> hash = new HashMap<String, String>();
        hash.put("bar", "car");
        hash.put("car", "bar");
        session.hmset("foo", hash);

        List<String> values = session.hmget("foo", "bar", "car", "foo");
        List<String> expected = new ArrayList<String>();
        expected.add("car");
        expected.add("bar");
        expected.add(null);

        assertEquals(expected, values);
    }

    @Test
    public void hincrBy() {
        long value = session.hincrBy("foo", "bar", 1);
        assertEquals(1, value);
        value = session.hincrBy("foo", "bar", -1);
        assertEquals(0, value);
        value = session.hincrBy("foo", "bar", -10);
        assertEquals(-10, value);
    }

    @Test
    public void hexists() {
        Map<String, String> hash = new HashMap<String, String>();
        hash.put("bar", "car");
        hash.put("car", "bar");
        session.hmset("foo", hash);

        assertFalse(session.hexists("bar", "foo"));
        assertFalse(session.hexists("foo", "foo"));
        assertTrue(session.hexists("foo", "bar"));
    }

    @Test
    public void hdel() {
        Map<String, String> hash = new HashMap<String, String>();
        hash.put("bar", "car");
        hash.put("car", "bar");
        session.hmset("foo", hash);

        assertFalse(session.hdel("bar", "foo"));
        assertFalse(session.hdel("foo", "foo"));
        assertTrue(session.hdel("foo", "bar"));
        assertEquals(null, session.hget("foo", "bar"));
    }

    @Test
    public void hlen() {
        Map<String, String> hash = new HashMap<String, String>();
        hash.put("bar", "car");
        hash.put("car", "bar");
        session.hmset("foo", hash);

        assertEquals(0, session.hlen("bar").intValue());
        assertEquals(2, session.hlen("foo").intValue());
    }

    @Test
    public void hkeys() {
        Map<String, String> hash = new LinkedHashMap<String, String>();
        hash.put("bar", "car");
        hash.put("car", "bar");
        session.hmset("foo", hash);

        Set<String> keys = session.hkeys("foo");
        Set<String> expected = new LinkedHashSet<String>();
        expected.add("bar");
        expected.add("car");
        assertEquals(expected, keys);
    }

    @Test
    public void hvals() {
        Map<String, String> hash = new LinkedHashMap<String, String>();
        hash.put("bar", "car");
        hash.put("car", "bar");
        session.hmset("foo", hash);

        Collection<String> vals = session.hvals("foo");
        assertEquals(2, vals.size());
        assertTrue(vals.contains("bar"));
        assertTrue(vals.contains("car"));
    }

    @Test
    public void hgetAll() {
        Map<String, String> h = new HashMap<String, String>();
        h.put("bar", "car");
        h.put("car", "bar");
        session.hmset("foo", h);

        Map<String, String> hash = session.hgetAll("foo");
        assertEquals(2, hash.size());
        assertEquals("car", hash.get("bar"));
        assertEquals("bar", hash.get("car"));
    }
}
