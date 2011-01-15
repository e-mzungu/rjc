package io.redis.rjc;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Evgeny Dolgov
 */
public class ITShardedRedisTest extends ShardedRedisTestBase {

    @Test
    public void del() {
        Collection<String> keys = new ArrayList<String>(100);
        for (int i = 0; i < 100; i++) {
            String key = "key" + i;
            keys.add(key);
            redis.set(key, "value");
        }

        for (String key : keys) {
            assertTrue(redis.exists(key));
        }

        assertEquals(new Long(keys.size()), redis.del(keys.toArray(new String[keys.size()])));

        for (String key : keys) {
            assertFalse(redis.exists(key));
        }
    }

}
