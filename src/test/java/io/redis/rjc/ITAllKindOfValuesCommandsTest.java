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
        String status = redis.ping();
        assertEquals("PONG", status);
    }

    @Test
    public void exists() {
        String status = redis.set("foo", "bar");
        assertEquals("OK", status);

        boolean reply = redis.exists("foo");
        assertTrue(reply);


        long lreply = redis.del("foo");
        assertEquals(1, lreply);

        reply = redis.exists("foo");
        assertFalse(reply);
    }

    @Test
    public void del() {
        redis.set("foo1", "bar1");
        redis.set("foo2", "bar2");
        redis.set("foo3", "bar3");

        long reply = redis.del("foo1", "foo2", "foo3");
        assertEquals(3, reply);

        Boolean breply = redis.exists("foo1");
        assertFalse(breply);
        breply = redis.exists("foo2");
        assertFalse(breply);
        breply = redis.exists("foo3");
        assertFalse(breply);

        redis.set("foo1", "bar1");

        reply = redis.del("foo1", "foo2");
        assertEquals(1, reply);

        reply = redis.del("foo1", "foo2");
        assertEquals(0, reply);
    }

    @Test
    public void type() {
        redis.set("foo", "bar");
        String status = redis.type("foo");
        assertEquals("string", status);
    }

    @Test
    public void keys() {
        redis.set("foo", "bar");
        redis.set("foobar", "bar");

        Set<String> keys = new TreeSet<String>(redis.keys("foo*"));
        Set<String> expected = new TreeSet<String>();
        expected.add("foo");
        expected.add("foobar");
        assertArrayEquals(expected.toArray(), keys.toArray());

        expected = new HashSet<String>();
        keys = redis.keys("bar*");

        assertArrayEquals(expected.toArray(), keys.toArray());
    }

    @Test
    public void randomKey() {
        assertEquals(null, redis.randomKey());

        redis.set("foo", "bar");

        assertEquals("foo", redis.randomKey());

        redis.set("bar", "foo");

        String randomkey = redis.randomKey();
        assertTrue(randomkey.equals("foo") || randomkey.equals("bar"));
    }

    @Test
    public void rename() {
        redis.set("foo", "bar");
        String status = redis.rename("foo", "bar");
        assertEquals("OK", status);

        String value = redis.get("foo");
        assertEquals(null, value);

        value = redis.get("bar");
        assertEquals("bar", value);
    }

    @Test
    public void renameOldAndNewAreTheSame() {
        try {
            redis.set("foo", "bar");
            redis.rename("foo", "foo");
            fail("redisException expected");
        } catch (final RedisException e) {
        }
    }

    @Test
    public void renamenx() {
        redis.set("foo", "bar");
        long status = redis.renamenx("foo", "bar");
        assertEquals(1, status);

        redis.set("foo", "bar");
        status = redis.renamenx("foo", "bar");
        assertEquals(0, status);
    }

    @Test
    public void dbSize() {
        long size = redis.dbSize();
        assertEquals(0, size);

        redis.set("foo", "bar");
        size = redis.dbSize();
        assertEquals(1, size);
    }

    @Test
    public void expire() {
        long status = redis.expire("foo", 20);
        assertEquals(0, status);

        redis.set("foo", "bar");
        status = redis.expire("foo", 20);
        assertEquals(1, status);
    }

    @Test
    public void expireAt() {
        long unixTime = (System.currentTimeMillis() / 1000L) + 20;

        long status = redis.expireAt("foo", unixTime);
        assertEquals(0, status);

        redis.set("foo", "bar");
        unixTime = (System.currentTimeMillis() / 1000L) + 20;
        status = redis.expireAt("foo", unixTime);
        assertEquals(1, status);
    }

    @Test
    public void ttl() {
        long ttl = redis.ttl("foo");
        assertEquals(-1, ttl);

        redis.set("foo", "bar");
        ttl = redis.ttl("foo");
        assertEquals(-1, ttl);

        redis.expire("foo", 20);
        ttl = redis.ttl("foo");
        assertTrue(ttl >= 0 && ttl <= 20);
    }

    @Test
    public void select() {
        redis.set("foo", "bar");
        String status = redis.select(1);
        assertEquals("OK", status);
        assertEquals(null, redis.get("foo"));
        status = redis.select(0);
        assertEquals("OK", status);
        assertEquals("bar", redis.get("foo"));
    }

    @Test
    public void move() {
        long status = redis.move("foo", 1);
        assertEquals(0, status);

        redis.set("foo", "bar");
        status = redis.move("foo", 1);
        assertEquals(1, status);
        assertEquals(null, redis.get("foo"));

        redis.select(1);
        assertEquals("bar", redis.get("foo"));
    }

    @Test
    public void flushDB() {
        redis.set("foo", "bar");
        assertEquals(1, redis.dbSize().intValue());
        redis.set("bar", "foo");
        redis.move("bar", 1);
        String status = redis.flushDB();
        assertEquals("OK", status);
        assertEquals(0, redis.dbSize().intValue());
        redis.select(1);
        assertEquals(1, redis.dbSize().intValue());
        redis.del("bar");
    }

    @Test
    public void flushAll() {
        redis.set("foo", "bar");
        assertEquals(1, redis.dbSize().intValue());
        redis.set("bar", "foo");
        redis.move("bar", 1);
        String status = redis.flushAll();
        assertEquals("OK", status);
        assertEquals(0, redis.dbSize().intValue());
        redis.select(1);
        assertEquals(0, redis.dbSize().intValue());
    }

    @Test
    public void persist() {
        redis.setex("foo", 60 * 60, "bar");
        assertTrue(redis.ttl("foo") > 0);
        long status = redis.persist("foo");
        assertEquals(1, status);
        assertEquals(-1, redis.ttl("foo").intValue());
    }

    @Test
    public void echo() {
        String result = redis.echo("hello world");
        assertEquals("hello world", result);
    } 

}
