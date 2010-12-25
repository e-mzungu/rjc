package io.redis.rjc.ds;

import io.redis.rjc.protocol.Protocol;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

/**
 * @author Evgeny Dolgov
 */
public interface RedisConnection {
    int getTimeout();

    void rollbackTimeout();

    String getHost();

    int getPort();

    void connect() throws UnknownHostException, IOException;

    void close();

    boolean isConnected();

    void setTimeoutInfinite();

    String getStatusCodeReply();

    String getBulkReply();

    byte[] getBinaryBulkReply();

    Long getIntegerReply();

    List<String> getMultiBulkReply();

    @SuppressWarnings("unchecked")
    List<byte[]> getBinaryMultiBulkReply();

    @SuppressWarnings("unchecked")
    List<Object> getObjectMultiBulkReply();

    List<Object> getAll();

    Object getOne();

    void sendCommand(final Protocol.Command cmd, final String... args);

    void sendCommand(final Protocol.Command cmd, final byte[]... args);

    void sendCommand(final Protocol.Command cmd);
}
