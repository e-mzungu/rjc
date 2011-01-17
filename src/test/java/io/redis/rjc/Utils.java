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

import org.junit.Assert;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author Evgeny Dolgov
 */
public class Utils {

    private Utils() {
    }

    public static void assertEquals(List<byte[]> expected, List<byte[]> actual) {
        Assert.assertEquals(expected.size(), actual.size());
        for (int n = 0; n < expected.size(); n++) {
            Assert.assertArrayEquals(expected.get(n), actual.get(n));
        }
    }

    public static void assertEquals(Set<byte[]> expected, Set<byte[]> actual) {
        Assert.assertEquals(expected.size(), actual.size());
        Iterator<byte[]> iterator = expected.iterator();
        Iterator<byte[]> iterator2 = actual.iterator();
        while (iterator.hasNext() || iterator2.hasNext()) {
            Assert.assertArrayEquals(iterator.next(), iterator2.next());
        }
    }

    public static boolean arrayContains(List<byte[]> array, byte[] expected) {
        for (byte[] a : array) {
            try {
                Assert.assertArrayEquals(a, expected);
                return true;
            } catch (AssertionError e) {

            }
        }
        return false;
    }

    public static boolean setContains(Set<byte[]> set, byte[] expected) {
        for (byte[] a : set) {
            try {
                Assert.assertArrayEquals(a, expected);
                return true;
            } catch (AssertionError e) {

            }
        }
        return false;
    }
}
