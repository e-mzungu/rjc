package io.redis.rjc.ds;

import io.redis.rjc.protocol.Protocol;
import io.redis.rjc.protocol.Protocol.Command;
import io.redis.rjc.RedisException;
import io.redis.rjc.protocol.RedisInputStream;
import io.redis.rjc.protocol.RedisOutputStream;
import io.redis.rjc.util.SafeEncoder;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

class RedisConnectionImpl implements RedisConnection {
    private String host;
    private int port = Protocol.DEFAULT_PORT;
    private Socket socket;
    private Protocol protocol = new Protocol();
    private RedisOutputStream outputStream;
    private RedisInputStream inputStream;
    private int pipelinedCommands = 0;
    private int timeout = Protocol.DEFAULT_TIMEOUT;

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(final int timeout) {
        this.timeout = timeout;
    }

    public void setTimeoutInfinite() {
        try {
            socket.setSoTimeout(0);
        } catch (SocketException ex) {
            throw new RedisException(ex);
        }
    }

    public void rollbackTimeout() {
        try {
            socket.setSoTimeout(timeout);
        } catch (SocketException ex) {
            throw new RedisException(ex);
        }
    }

    public RedisConnectionImpl(final String host) {
        this.host = host;
    }

    public RedisConnectionImpl(final String host, final int port) {
        this.host = host;
        this.port = port;
    }



    public void sendCommand(final Command cmd, final String... args) {
        final byte[][] bargs = new byte[args.length][];
        for (int i = 0; i < args.length; i++) {
            bargs[i] = SafeEncoder.encode(args[i]);
        }
        sendCommand(cmd, bargs);
    }

    public void sendCommand(final Command cmd, final byte[]... args) {
        try {
            connect();
        } catch (UnknownHostException e) {
            throw new RedisException("Could not connect to redis-server", e);
        } catch (IOException e) {
            throw new RedisException("Could not connect to redis-server", e);
        }
        protocol.sendCommand(outputStream, cmd, args);
        pipelinedCommands++;
    }

    public void sendCommand(final Command cmd) {
        sendCommand(cmd, new byte[0][]);
    }

    public String getHost() {
        return host;
    }

    public void setHost(final String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(final int port) {
        this.port = port;
    }

    public RedisConnectionImpl() {
    }

    public void connect() throws UnknownHostException, IOException {
        if (!isConnected()) {
            socket = new Socket(host, port);
            socket.setSoTimeout(timeout);
            outputStream = new RedisOutputStream(socket.getOutputStream());
            inputStream = new RedisInputStream(socket.getInputStream());
        }
    }

    public void close() {
        if (isConnected()) {
            try {
                inputStream.close();
                outputStream.close();
                if (!socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException ex) {
                throw new RedisException(ex);
            }
        }
    }

    public boolean isConnected() {
        return socket != null && socket.isBound() && !socket.isClosed()
                && socket.isConnected() && !socket.isInputShutdown()
                && !socket.isOutputShutdown();
    }

    public String getStatusCodeReply() {
        pipelinedCommands--;
        final byte[] resp = (byte[]) protocol.read(inputStream);
        if (null == resp) {
            return null;
        } else {
            return SafeEncoder.encode(resp);
        }
    }

    public String getBulkReply() {
        final byte[] result = getBinaryBulkReply();
        if (null != result) {
            return SafeEncoder.encode(result);
        } else {
            return null;
        }
    }

    public byte[] getBinaryBulkReply() {
        pipelinedCommands--;
        return (byte[]) protocol.read(inputStream);
    }

    public Long getIntegerReply() {
        pipelinedCommands--;
        return (Long) protocol.read(inputStream);
    }

    public List<String> getMultiBulkReply() {
        final List<byte[]> bresult = getBinaryMultiBulkReply();
        if (null == bresult) {
            return null;
        }
        final ArrayList<String> result = new ArrayList<String>(bresult.size());
        for (final byte[] barray : bresult) {
            if (barray == null) {
                result.add(null);
            } else {
                result.add(SafeEncoder.encode(barray));
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public List<byte[]> getBinaryMultiBulkReply() {
        pipelinedCommands--;
        return (List<byte[]>) protocol.read(inputStream);
    }

    @SuppressWarnings("unchecked")
    public List<Object> getObjectMultiBulkReply() {
        pipelinedCommands--;
        return (List<Object>) protocol.read(inputStream);
    }

    public List<Object> getAll() {
        List<Object> all = new ArrayList<Object>();
        while (pipelinedCommands > 0) {
            all.add(protocol.read(inputStream));
            pipelinedCommands--;
        }
        return all;
    }

    public Object getOne() {
        pipelinedCommands--;
        return protocol.read(inputStream);
    }
}