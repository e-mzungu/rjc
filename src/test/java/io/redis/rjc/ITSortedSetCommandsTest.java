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

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Evgeny Dolgov
 */
public class ITSortedSetCommandsTest extends SingleNodeTestBase {
    @Test
    public void zadd() {
        long status = session.zadd("foo", 1d, "a");
        assertEquals(1, status);

        status = session.zadd("foo", 10d, "b");
        assertEquals(1, status);

        status = session.zadd("foo", 0.1d, "c");
        assertEquals(1, status);

        status = session.zadd("foo", 2d, "a");
        assertEquals(0, status);

    }

    @Test
    public void zrange() {
        session.zadd("foo", 1d, "a");
        session.zadd("foo", 10d, "b");
        session.zadd("foo", 0.1d, "c");
        session.zadd("foo", 2d, "a");

        Set<String> expected = new LinkedHashSet<String>();
        expected.add("c");
        expected.add("a");

        Set<String> range = session.zrange("foo", 0, 1);
        assertEquals(expected, range);

        expected.add("b");
        range = session.zrange("foo", 0, 100);
        assertEquals(expected, range);
    }

    @Test
    public void zrevrange() {
        session.zadd("foo", 1d, "a");
        session.zadd("foo", 10d, "b");
        session.zadd("foo", 0.1d, "c");
        session.zadd("foo", 2d, "a");

        Set<String> expected = new LinkedHashSet<String>();
        expected.add("b");
        expected.add("a");

        Set<String> range = session.zrevrange("foo", 0, 1);
        assertEquals(expected, range);

        expected.add("c");
        range = session.zrevrange("foo", 0, 100);
        assertEquals(expected, range);
    }

    @Test
    public void zrem() {
        session.zadd("foo", 1d, "a");
        session.zadd("foo", 2d, "b");

        long status = session.zrem("foo", "a");

        Set<String> expected = new LinkedHashSet<String>();
        expected.add("b");

        assertEquals(1, status);
        assertEquals(expected, session.zrange("foo", 0, 100));

        status = session.zrem("foo", "bar");

        assertEquals(0, status);
    }

    @Test
    public void zincrby() {
        session.zadd("foo", 1d, "a");
        session.zadd("foo", 2d, "b");

        String score = session.zincrby("foo", 2d, "a");

        Set<String> expected = new LinkedHashSet<String>();
        expected.add("a");
        expected.add("b");

        assertEquals(3d, Double.valueOf(score), 0);
        assertEquals(expected, session.zrange("foo", 0, 100));
    }

    @Test
    public void zrank() {
        session.zadd("foo", 1d, "a");
        session.zadd("foo", 2d, "b");

        long rank = session.zrank("foo", "a");
        assertEquals(0, rank);

        rank = session.zrank("foo", "b");
        assertEquals(1, rank);

        assertNull(session.zrank("car", "b"));
    }

    @Test
    public void zrevrank() {
        session.zadd("foo", 1d, "a");
        session.zadd("foo", 2d, "b");

        long rank = session.zrevrank("foo", "a");
        assertEquals(1, rank);

        rank = session.zrevrank("foo", "b");
        assertEquals(0, rank);
    }

    @Test
    public void zrangeWithScores() {
        session.zadd("foo", 1d, "a");
        session.zadd("foo", 10d, "b");
        session.zadd("foo", 0.1d, "c");
        session.zadd("foo", 2d, "a");

        Map<String, Double> expected = new HashMap<String, Double>();
        expected.put("c", 0.1D);
        expected.put("a", 2D);

        Map<String, String> range = session.zrangeWithScores("foo", 0, 1);
        Map<String, Double> actual = toStrDblMap(range);
        assertEquals(expected, actual);

        expected.put("b", 10D);
        range = session.zrangeWithScores("foo", 0, 100);
        assertEquals(expected, toStrDblMap(range));
    }

    private Map<String, Double> toStrDblMap(Map<String, String> range) {
        Map<String, Double> actual = new HashMap<String, Double>();
        for (Map.Entry<String, String> entry : range.entrySet()) {
            actual.put(entry.getKey(), Double.valueOf(entry.getValue()));
        }
        return actual;
    }

    @Test
    public void zrevrangeWithScores() {
        session.zadd("foo", 1d, "a");
        session.zadd("foo", 10d, "b");
        session.zadd("foo", 0.1d, "c");
        session.zadd("foo", 2d, "a");

        Map<String, Double> expected = new HashMap<String, Double>();
        expected.put("b", 10D);
        expected.put("a", 2D);

        Map<String, String> range = session.zrevrangeWithScores("foo", 0, 1);
        assertEquals(expected, toStrDblMap(range));

        expected.put("c", 0.1D);
        range = session.zrevrangeWithScores("foo", 0, 100);
        assertEquals(expected, toStrDblMap(range));
    }

    @Test
    public void zcard() {
        session.zadd("foo", 1d, "a");
        session.zadd("foo", 10d, "b");
        session.zadd("foo", 0.1d, "c");
        session.zadd("foo", 2d, "a");

        long size = session.zcard("foo");
        assertEquals(3, size);
    }

    @Test
    public void zscore() {
        session.zadd("foo", 1d, "a");
        session.zadd("foo", 10d, "b");
        session.zadd("foo", 0.1d, "c");
        session.zadd("foo", 2d, "a");

        String score = session.zscore("foo", "b");
        assertEquals("10", score);

        score = session.zscore("foo", "c");
        assertEquals(0.1D, Double.valueOf(score), 0);

        score = session.zscore("foo", "s");
        assertNull(score);
    }

    @Test
    public void zcount() {
        session.zadd("foo", 1d, "a");
        session.zadd("foo", 10d, "b");
        session.zadd("foo", 0.1d, "c");
        session.zadd("foo", 2d, "a");

        long result = session.zcount("foo", 0.01d, 2.1d);

        assertEquals(2, result);
    }

    @Test
    public void zrangebyscore() {
        session.zadd("foo", 1d, "a");
        session.zadd("foo", 10d, "b");
        session.zadd("foo", 0.1d, "c");
        session.zadd("foo", 2d, "a");

        Set<String> range = session.zrangeByScore("foo", "0", "2");

        Set<String> expected = new LinkedHashSet<String>();
        expected.add("c");
        expected.add("a");

        assertEquals(expected, range);

        range = session.zrangeByScore("foo", "0", "2", 0, 1);

        expected = new LinkedHashSet<String>();
        expected.add("c");

        assertEquals(expected, range);

        range = session.zrangeByScore("foo", "0", "2", 1, 1);
        Set<String> range2 = session.zrangeByScore("foo", "-inf", "(2");
        assertEquals(expected, range2);

        expected = new LinkedHashSet<String>();
        expected.add("a");

        assertEquals(expected, range);

    }

    @Test
    public void zrangebyscoreWithScores() {
        session.zadd("foo", 1d, "a");
        session.zadd("foo", 10d, "b");
        session.zadd("foo", 0.1d, "c");
        session.zadd("foo", 2d, "a");

         Map<String, String> range = session.zrangeByScoreWithScores("foo", "0", "2");

        Map<String, Double> expected = new HashMap<String, Double>();
        expected.put("c", 0.1D);
        expected.put("a", 2D);

        assertEquals(expected, toStrDblMap(range));

        range = session.zrangeByScoreWithScores("foo", "0", "2", 0, 1);

        expected = new HashMap<String, Double>();
        expected.put("c", 0.1D);

        assertEquals(expected, toStrDblMap(range));

        range = session.zrangeByScoreWithScores("foo", "0", "2", 1, 1);

        expected = new HashMap<String, Double>();
        expected.put("a", 2D);

        assertEquals(expected, toStrDblMap(range));
    }

    @Test
    public void zremrangeByRank() {
        session.zadd("foo", 1d, "a");
        session.zadd("foo", 10d, "b");
        session.zadd("foo", 0.1d, "c");
        session.zadd("foo", 2d, "a");

        long result = session.zremrangeByRank("foo", 0, 0);

        assertEquals(1, result);

        Set<String> expected = new LinkedHashSet<String>();
        expected.add("a");
        expected.add("b");

        assertEquals(expected, session.zrange("foo", 0, 100));
    }

    @Test
    public void zremrangeByScore() {
        session.zadd("foo", 1d, "a");
        session.zadd("foo", 10d, "b");
        session.zadd("foo", 0.1d, "c");
        session.zadd("foo", 2d, "a");

        long result = session.zremrangeByScore("foo", 0, 2);

        assertEquals(2, result);

        Set<String> expected = new LinkedHashSet<String>();
        expected.add("b");

        assertEquals(expected, session.zrange("foo", 0, 100));
    }

    @Test
    public void zunionstore() {
        session.zadd("foo", 1, "a");
        session.zadd("foo", 2, "b");
        session.zadd("bar", 2, "a");
        session.zadd("bar", 2, "b");

        long result = session.zunionstore("dst", "foo", "bar");

        assertEquals(2, result);

        Map<String, Double> expected = new HashMap<String, Double>();
        expected.put("b", 4D);
        expected.put("a", 3D);

        assertEquals(expected, toStrDblMap(session.zrangeWithScores("dst", 0, 100)));
    }

    @Test
    public void zunionstoreParams() {
        session.zadd("foo", 1, "a");
        session.zadd("foo", 2, "b");
        session.zadd("bar", 2, "a");
        session.zadd("bar", 2, "b");

        ZParams params = new ZParams();
        params.weights(2, 2);
        params.aggregate(ZParams.Aggregate.SUM);
        long result = session.zunionstore("dst", params, "foo", "bar");

        assertEquals(2, result);

        Map<String, Double> expected = new HashMap<String, Double>();
        expected.put("b", 8D);
        expected.put("a", 6D);

        assertEquals(expected, toStrDblMap(session.zrangeWithScores("dst", 0, 100)));

    }

    @Test
    public void zinterstore() {
        session.zadd("foo", 1, "a");
        session.zadd("foo", 2, "b");
        session.zadd("bar", 2, "a");

        long result = session.zinterstore("dst", "foo", "bar");

        assertEquals(1, result);

        Map<String, Double> expected = new HashMap<String, Double>();
        expected.put("a", 3D);

        assertEquals(expected, toStrDblMap(session.zrangeWithScores("dst", 0, 100)));

    }

    @Test
    public void zintertoreParams() {
        session.zadd("foo", 1, "a");
        session.zadd("foo", 2, "b");
        session.zadd("bar", 2, "a");

        ZParams params = new ZParams();
        params.weights(2, 2);
        params.aggregate(ZParams.Aggregate.SUM);
        long result = session.zinterstore("dst", params, "foo", "bar");

        assertEquals(1, result);

        Map<String, Double> expected = new HashMap<String, Double>();
        expected.put("a", 6D);

        assertEquals(expected, toStrDblMap(session.zrangeWithScores("dst", 0, 100)));
    }
}
