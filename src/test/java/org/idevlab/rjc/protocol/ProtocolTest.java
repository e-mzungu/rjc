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

package org.idevlab.rjc.protocol;

import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ProtocolTest {
    @Test
    public void buildACommand() throws IOException {
        PipedInputStream pis = new PipedInputStream();
        BufferedInputStream bis = new BufferedInputStream(pis);
        PipedOutputStream pos = new PipedOutputStream(pis);

        Protocol protocol = new Protocol();
        protocol.sendCommand(new RedisOutputStream(pos), RedisCommand.GET,
                "SOMEKEY".getBytes(Protocol.CHARSET));

        pos.close();
        String expectedCommand = "*2\r\n$3\r\nGET\r\n$7\r\nSOMEKEY\r\n";

        int b;
        StringBuilder sb = new StringBuilder();
        while ((b = bis.read()) != -1) {
            sb.append((char) b);
        }

        assertEquals(expectedCommand, sb.toString());
    }

    @Test
    public void bulkReply() {
        InputStream is = new ByteArrayInputStream("$6\r\nfoobar\r\n".getBytes());
        Protocol protocol = new Protocol();
        String response = (String) protocol.read(new RedisInputStream(is));
        assertEquals("foobar", response);
    }

    @Test
    public void fragmentedBulkReply() {
        FragmentedByteArrayInputStream fis = new FragmentedByteArrayInputStream(
                "$30\r\n012345678901234567890123456789\r\n".getBytes());
        Protocol protocol = new Protocol();
        String response = (String) protocol.read(new RedisInputStream(fis));
        assertEquals("012345678901234567890123456789", response);
    }

    @Test
    public void nullBulkReply() {
        InputStream is = new ByteArrayInputStream("$-1\r\n".getBytes());
        Protocol protocol = new Protocol();
        String response = (String) protocol.read(new RedisInputStream(is));
        assertEquals(null, response);
    }

    @Test
    public void singleLineReply() {
        InputStream is = new ByteArrayInputStream("+OK\r\n".getBytes());
        Protocol protocol = new Protocol();
        String response = (String) protocol.read(new RedisInputStream(is));
        assertEquals("OK", response);
    }

    @Test
    public void integerReply() {
        InputStream is = new ByteArrayInputStream(":123\r\n".getBytes());
        Protocol protocol = new Protocol();
        long response = (Long) protocol.read(new RedisInputStream(is));
        assertEquals(123, response);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void multiBulkReply() {
        InputStream is = new ByteArrayInputStream(
                "*4\r\n$3\r\nfoo\r\n$3\r\nbar\r\n$5\r\nHello\r\n$5\r\nWorld\r\n"
                        .getBytes());
        Protocol protocol = new Protocol();
        List<String> response = (List<String>) protocol
                .read(new RedisInputStream(is));
        List<String> expected = new ArrayList<String>();
        expected.add("foo");
        expected.add("bar");
        expected.add("Hello");
        expected.add("World");

        assertEquals(expected.size(), response.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), response.get(i));
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void nullMultiBulkReply() {
        InputStream is = new ByteArrayInputStream("*-1\r\n".getBytes());
        Protocol protocol = new Protocol();
        List<String> response = (List<String>) protocol
                .read(new RedisInputStream(is));
        assertNull(response);
    }


}
