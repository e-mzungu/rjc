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