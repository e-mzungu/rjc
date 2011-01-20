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

package io.redis.rjc.ds;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.UnknownHostException;


public class ITConnectionTest {
    private RedisConnectionImpl client;

    @Before
    public void setUp() throws Exception {
        client = new RedisConnectionImpl();
    }

    @After
    public void tearDown() throws Exception {
        client.close();
    }

    @Test(expected = UnknownHostException.class)
    public void checkUnknownHost() throws UnknownHostException, IOException {
        client.setHost("someunknownhost");
        client.connect();
    }

    @Test(expected = IOException.class)
    public void checkWrongPort() throws UnknownHostException, IOException {
        client.setHost("localhost");
        client.setPort(55665);
        client.connect();
    }
}