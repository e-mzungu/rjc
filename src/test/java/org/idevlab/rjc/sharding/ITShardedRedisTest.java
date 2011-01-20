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

package org.idevlab.rjc.sharding;

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
