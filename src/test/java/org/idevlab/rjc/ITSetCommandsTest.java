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

import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author Evgeny Dolgov
 */
public class ITSetCommandsTest extends SingleNodeTestBase {

    @Test
    public void sadd() {
        long status = session.sadd("foo", "a");
        assertEquals(1, status);

        status = session.sadd("foo", "a");
        assertEquals(0, status);
    }

    @Test
    public void smembers() {
        session.sadd("foo", "a");
        session.sadd("foo", "b");

        Set<String> expected = new LinkedHashSet<String>();
        expected.add("a");
        expected.add("b");

        Set<String> members = session.smembers("foo");

        assertEquals(expected, members);
    }

    @Test
    public void srem() {
        session.sadd("foo", "a");
        session.sadd("foo", "b");

        long status = session.srem("foo", "a");

        Set<String> expected = new LinkedHashSet<String>();
        expected.add("b");

        assertEquals(1, status);
        Assert.assertEquals(expected, session.smembers("foo"));

        status = session.srem("foo", "bar");

        assertEquals(0, status);
    }

    @Test
    public void spop() {
        session.sadd("foo", "a");
        session.sadd("foo", "b");

        String member = session.spop("foo");

        assertTrue("a".equals(member) || "b".equals(member));
        Assert.assertEquals(1, session.smembers("foo").size());

        member = session.spop("bar");
        assertNull(member);
    }

    @Test
    public void smove() {
        session.sadd("foo", "a");
        session.sadd("foo", "b");

        session.sadd("bar", "c");

        long status = session.smove("foo", "bar", "a");

        Set<String> expectedSrc = new LinkedHashSet<String>();
        expectedSrc.add("b");

        Set<String> expectedDst = new LinkedHashSet<String>();
        expectedDst.add("c");
        expectedDst.add("a");

        assertEquals(status, 1);
        Assert.assertEquals(expectedSrc, session.smembers("foo"));
        Assert.assertEquals(expectedDst, session.smembers("bar"));

        status = session.smove("foo", "bar", "a");

        assertEquals(status, 0);
    }

    @Test
    public void scard() {
        session.sadd("foo", "a");
        session.sadd("foo", "b");

        long card = session.scard("foo");

        assertEquals(2, card);

        card = session.scard("bar");
        assertEquals(0, card);
    }

    @Test
    public void sismember() {
        session.sadd("foo", "a");
        session.sadd("foo", "b");

        assertTrue(session.sismember("foo", "a"));

        assertFalse(session.sismember("foo", "c"));
    }

    @Test
    public void sinter() {
        session.sadd("foo", "a");
        session.sadd("foo", "b");

        session.sadd("bar", "b");
        session.sadd("bar", "c");

        Set<String> expected = new LinkedHashSet<String>();
        expected.add("b");

        Set<String> intersection = session.sinter("foo", "bar");
        assertEquals(expected, intersection);
    }

    @Test
    public void sinterstore() {
        session.sadd("foo", "a");
        session.sadd("foo", "b");

        session.sadd("bar", "b");
        session.sadd("bar", "c");

        Set<String> expected = new LinkedHashSet<String>();
        expected.add("b");

        long status = session.sinterstore("car", "foo", "bar");
        assertEquals(1, status);

        Assert.assertEquals(expected, session.smembers("car"));
    }

    @Test
    public void sunion() {
        session.sadd("foo", "a");
        session.sadd("foo", "b");

        session.sadd("bar", "b");
        session.sadd("bar", "c");

        Set<String> expected = new LinkedHashSet<String>();
        expected.add("a");
        expected.add("b");
        expected.add("c");

        Set<String> union = session.sunion("foo", "bar");
        assertEquals(expected, union);
    }

    @Test
    public void sunionstore() {
        session.sadd("foo", "a");
        session.sadd("foo", "b");

        session.sadd("bar", "b");
        session.sadd("bar", "c");

        Set<String> expected = new LinkedHashSet<String>();
        expected.add("a");
        expected.add("b");
        expected.add("c");

        long status = session.sunionstore("car", "foo", "bar");
        assertEquals(3, status);

        Assert.assertEquals(expected, session.smembers("car"));
    }

    @Test
    public void sdiff() {
        session.sadd("foo", "x");
        session.sadd("foo", "a");
        session.sadd("foo", "b");
        session.sadd("foo", "c");

        session.sadd("bar", "c");

        session.sadd("car", "a");
        session.sadd("car", "d");

        Set<String> expected = new LinkedHashSet<String>();
        expected.add("x");
        expected.add("b");

        Set<String> diff = session.sdiff("foo", "bar", "car");
        assertEquals(expected, diff);
    }

    @Test
    public void sdiffstore() {
        session.sadd("foo", "x");
        session.sadd("foo", "a");
        session.sadd("foo", "b");
        session.sadd("foo", "c");

        session.sadd("bar", "c");

        session.sadd("car", "a");
        session.sadd("car", "d");

        Set<String> expected = new LinkedHashSet<String>();
        expected.add("d");
        expected.add("a");

        long status = session.sdiffstore("tar", "foo", "bar", "car");
        assertEquals(2, status);
        Assert.assertEquals(expected, session.smembers("car"));
    }

    @Test
    public void srandmember() {
        session.sadd("foo", "a");
        session.sadd("foo", "b");

        String member = session.srandmember("foo");

        assertTrue("a".equals(member) || "b".equals(member));
        Assert.assertEquals(2, session.smembers("foo").size());

        member = session.srandmember("bar");
        assertNull(member);
    }

}
