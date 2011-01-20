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

import org.idevlab.rjc.RedisNodeFactory;
import org.idevlab.rjc.RedisOperations;
import org.idevlab.rjc.SingleRedisOperations;
import org.idevlab.rjc.ds.SimpleDataSourceFactory;
import org.junit.Before;

import java.util.Collection;

/**
 * @author Evgeny Dolgov
 */
public class ShardedRedisTestBase {
    protected RedisOperations redis;

    @Before
    public void setUp() throws Exception {
        ShardsFactoryImpl<SingleRedisOperations> shardsFactory = new ShardsFactoryImpl<SingleRedisOperations>();
        shardsFactory.setDataSourceFactory(new SimpleDataSourceFactory());
        shardsFactory.setNodeFactory(new RedisNodeFactory());
        shardsFactory.setAddresses(System.getProperty("redis-hosts"));

        Collection<Shard<SingleRedisOperations>> shards = shardsFactory.create();

        NodeLocator<SingleRedisOperations> locator = new HashNodeLocator<SingleRedisOperations>(shards, HashNodeLocator.DEFAULT_KEY_TAG_PATTERN);
        redis = new ShardedRedis(locator);
        for (SingleRedisOperations node : locator.getNodes()) {
            node.flushAll();
        }
    }

}
