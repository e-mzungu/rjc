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

package io.redis.rjc;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.*;

/**
 * @author Evgeny Dolgov
 */
public class ITAllKindOfValuesCommandsTest extends SingleNodeTestBase {

    @Test
    public void ping() {
        String status = session.ping();
        assertEquals("PONG", status);
    }

    @Test
    public void exists() {
        String status = session.set("foo", "bar");
        assertEquals("OK", status);

        boolean reply = session.exists("foo");
        assertTrue(reply);


        long lreply = session.del("foo");
        assertEquals(1, lreply);

        reply = session.exists("foo");
        assertFalse(reply);
    }

    @Test
    public void del() {
        session.set("foo1", "bar1");
        session.set("foo2", "bar2");
        session.set("foo3", "bar3");

        long reply = session.del("foo1", "foo2", "foo3");
        assertEquals(3, reply);

        Boolean breply = session.exists("foo1");
        assertFalse(breply);
        breply = session.exists("foo2");
        assertFalse(breply);
        breply = session.exists("foo3");
        assertFalse(breply);

        session.set("foo1", "bar1");

        reply = session.del("foo1", "foo2");
        assertEquals(1, reply);

        reply = session.del("foo1", "foo2");
        assertEquals(0, reply);
    }

    @Test
    public void type() {
        session.set("foo", "bar");
        String status = session.type("foo");
        assertEquals("string", status);
    }

    @Test
    public void keys() {
        session.set("foo", "bar");
        session.set("foobar", "bar");

        Set<String> keys = new TreeSet<String>(session.keys("foo*"));
        Set<String> expected = new TreeSet<String>();
        expected.add("foo");
        expected.add("foobar");
        assertArrayEquals(expected.toArray(), keys.toArray());

        expected = new HashSet<String>();
        keys = session.keys("bar*");

        assertArrayEquals(expected.toArray(), keys.toArray());
    }

    @Test
    public void randomKey() {
        assertEquals(null, session.randomKey());

        session.set("foo", "bar");

        assertEquals("foo", session.randomKey());

        session.set("bar", "foo");

        String randomkey = session.randomKey();
        assertTrue(randomkey.equals("foo") || randomkey.equals("bar"));
    }

    @Test
    public void rename() {
        session.set("foo", "bar");
        String status = session.rename("foo", "bar");
        assertEquals("OK", status);

        String value = session.get("foo");
        assertEquals(null, value);

        value = session.get("bar");
        assertEquals("bar", value);
    }

    @Test
    public void renameOldAndNewAreTheSame() {
        try {
            session.set("foo", "bar");
            session.rename("foo", "foo");
            fail("redisException expected");
        } catch (final RedisException e) {
        }
    }

    @Test
    public void renamenx() {
        session.set("foo", "bar");
        long status = session.renamenx("foo", "bar");
        assertEquals(1, status);

        session.set("foo", "bar");
        status = session.renamenx("foo", "bar");
        assertEquals(0, status);
    }

    @Test
    public void dbSize() {
        long size = session.dbSize();
        assertEquals(0, size);

        session.set("foo", "bar");
        size = session.dbSize();
        assertEquals(1, size);
    }

    @Test
    public void expire() {
        long status = session.expire("foo", 20);
        assertEquals(0, status);

        session.set("foo", "bar");
        status = session.expire("foo", 20);
        assertEquals(1, status);
    }

    @Test
    public void expireAt() {
        long unixTime = (System.currentTimeMillis() / 1000L) + 20;

        long status = session.expireAt("foo", unixTime);
        assertEquals(0, status);

        session.set("foo", "bar");
        unixTime = (System.currentTimeMillis() / 1000L) + 20;
        status = session.expireAt("foo", unixTime);
        assertEquals(1, status);
    }

    @Test
    public void ttl() {
        long ttl = session.ttl("foo");
        assertEquals(-1, ttl);

        session.set("foo", "bar");
        ttl = session.ttl("foo");
        assertEquals(-1, ttl);

        session.expire("foo", 20);
        ttl = session.ttl("foo");
        assertTrue(ttl >= 0 && ttl <= 20);
    }

    @Test
    public void select() {
        session.set("foo", "bar");
        String status = session.select(1);
        assertEquals("OK", status);
        assertEquals(null, session.get("foo"));
        status = session.select(0);
        assertEquals("OK", status);
        assertEquals("bar", session.get("foo"));
    }

    @Test
    public void move() {
        long status = session.move("foo", 1);
        assertEquals(0, status);

        session.set("foo", "bar");
        status = session.move("foo", 1);
        assertEquals(1, status);
        assertEquals(null, session.get("foo"));

        session.select(1);
        assertEquals("bar", session.get("foo"));
    }

    @Test
    public void flushDB() {
        session.set("foo", "bar");
        assertEquals(1, session.dbSize().intValue());
        session.set("bar", "foo");
        session.move("bar", 1);
        String status = session.flushDB();
        assertEquals("OK", status);
        assertEquals(0, session.dbSize().intValue());
        session.select(1);
        assertEquals(1, session.dbSize().intValue());
        session.del("bar");
    }

    @Test
    public void flushAll() {
        session.set("foo", "bar");
        assertEquals(1, session.dbSize().intValue());
        session.set("bar", "foo");
        session.move("bar", 1);
        String status = session.flushAll();
        assertEquals("OK", status);
        assertEquals(0, session.dbSize().intValue());
        session.select(1);
        assertEquals(0, session.dbSize().intValue());
    }

    @Test
    public void persist() {
        //todo: Available since 2.1.2.
//        session.setex("foo", 60 * 60, "bar");
//        assertTrue(session.ttl("foo") > 0);
//        long status = session.persist("foo");
//        assertEquals(1, status);
//        assertEquals(-1, session.ttl("foo").intValue());
    }

    @Test
    public void echo() {
        String result = session.echo("hello world");
        assertEquals("hello world", result);
    } 

}
