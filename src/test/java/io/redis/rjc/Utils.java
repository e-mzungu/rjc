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
