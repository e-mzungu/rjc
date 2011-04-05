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

import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Evgeny Dolgov
 */
public class ITTransactionCommandsTest extends SingleNodeTestBase {
    @Test
    public void multi() {
        session.multi();

        Boolean status = session.sadd("foo", "a");
        assertNull(status);

        status = session.sadd("foo", "b");
        assertNull(status);

        Long lStatus = session.scard("foo");
        assertNull(lStatus);

        session.set("boo", "val1");
        session.get("boo");

        List<Object> response = session.exec();

        List<Object> expected = new ArrayList<Object>();
        expected.add(1L);
        expected.add(1L);
        expected.add(2L);
        expected.add("OK");
        expected.add("val1");
        assertEquals(expected, response);
    }


    @Test
    public void watch() throws IOException {
        session.watch("mykey", "somekey");
        session.multi();

        Session newSession = createSession();

        newSession.set("mykey", "bar");
        newSession.close();

        session.set("mykey", "foo");
        List<Object> resp = session.exec();
        assertEquals(null, resp);
        assertEquals("bar", session.get("mykey"));
    }

    @Test
    public void unwatch() throws UnsupportedEncodingException {
        session.watch("mykey");
        session.get("mykey");
        String status = session.unwatch();
        assertEquals("OK", status);
        session.multi();

        Session newSession = createSession();
        newSession.set("mykey", "bar");
        newSession.close();

        session.set("mykey", "foo");
        List<Object> resp = session.exec();
        assertEquals(1, resp.size());

        assertEquals("OK", resp.get(0));
    }

    @Test
    public void discard() {
        session.multi();
        String status = session.discard();
        assertEquals("OK", status);
    }
}
