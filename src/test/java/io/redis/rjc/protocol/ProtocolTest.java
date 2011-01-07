package io.redis.rjc.protocol;

import io.redis.rjc.util.SafeEncoder;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ProtocolTest {
    @Test
    public void buildACommand() throws IOException {
        PipedInputStream pis = new PipedInputStream();
        BufferedInputStream bis = new BufferedInputStream(pis);
        PipedOutputStream pos = new PipedOutputStream(pis);

        Protocol protocol = new Protocol();
        protocol.sendCommand(new RedisOutputStream(pos), Protocol.Command.GET,
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
        byte[] response = (byte[]) protocol.read(new RedisInputStream(is));
        assertArrayEquals(SafeEncoder.encode("foobar"), response);
    }

    @Test
    public void fragmentedBulkReply() {
        FragmentedByteArrayInputStream fis = new FragmentedByteArrayInputStream(
                "$30\r\n012345678901234567890123456789\r\n".getBytes());
        Protocol protocol = new Protocol();
        byte[] response = (byte[]) protocol.read(new RedisInputStream(fis));
        assertArrayEquals(SafeEncoder.encode("012345678901234567890123456789"),
                response);
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
        byte[] response = (byte[]) protocol.read(new RedisInputStream(is));
        assertArrayEquals(SafeEncoder.encode("OK"), response);
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
        List<byte[]> response = (List<byte[]>) protocol
                .read(new RedisInputStream(is));
        List<byte[]> expected = new ArrayList<byte[]>();
        expected.add(SafeEncoder.encode("foo"));
        expected.add(SafeEncoder.encode("bar"));
        expected.add(SafeEncoder.encode("Hello"));
        expected.add(SafeEncoder.encode("World"));

        assertEquals(expected.size(), response.size());
        for (int i = 0; i < expected.size(); i++) {
            assertArrayEquals(expected.get(i), response.get(i));
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
