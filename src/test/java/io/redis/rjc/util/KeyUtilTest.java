package io.redis.rjc.util;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * @author Evgeny Dolgov
 */
public class KeyUtilTest {

    @Test
    public void toKeyTest() {
        assertEquals("1$2$3", KeyUtil.toKey("$", 1, 2, 3));
        assertEquals("1:2:3", KeyUtil.toKey(1, 2, 3));
    }
}
