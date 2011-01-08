package io.redis.rjc;

import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
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

        Long status = session.sadd("foo", "a");
        assertNull(status);

        status = session.sadd("foo", "b");
        assertNull(status);

        status = session.scard("foo");
        assertNull(status);

        List<Object> response = session.exec();

        List<Object> expected = new ArrayList<Object>();
        expected.add(1L);
        expected.add(1L);
        expected.add(2L);
        assertEquals(expected, response);
    }


    @Test
    public void watch() throws UnknownHostException, IOException {
//        session.watch("mykey", "somekey");
//        session.multi();
//
//        Session newSession = createSession();
//
//        newSession.set("mykey", "bar");
//        newSession.close();
//
//        session.set("mykey", "foo");
//        List<Object> resp = session.exec();
//        assertEquals(null, resp);
//        assertEquals("bar", session.get("mykey"));
    }

    @Test
    public void unwatch() throws UnsupportedEncodingException {
//        session.watch("mykey");
//        session.get("mykey");
//        String status = session.unwatch();
//        assertEquals("OK", status);
//        session.multi();
//
//        Session newSession = createSession();
//        newSession.set("mykey", "bar");
//        newSession.close();
//
//        session.set("mykey", "foo");
//        List<Object> resp = session.exec();
//        assertEquals(1, resp.size());
//
//        assertArrayEquals(Protocol.Keyword.OK.name().getBytes(Protocol.CHARSET),
//                (byte[]) resp.get(0));
    }

    @Test
    public void discard() {
        session.multi();
        String status = session.discard();
        assertEquals("OK", status);
    }
}
