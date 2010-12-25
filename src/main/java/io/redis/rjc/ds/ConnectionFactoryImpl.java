package io.redis.rjc.ds;

import io.redis.rjc.RedisException;
import io.redis.rjc.protocol.Protocol;

/**
 * @author Evgeny Dolgov
 */
class ConnectionFactoryImpl implements ConnectionFactory {
    private final String host;
    private final int port;
    private final int timeout;
    private final String password;

    public ConnectionFactoryImpl(String host, int port, int timeout, String password) {
        this.host = host;
        this.port = port;
        this.timeout = (timeout > 0) ? timeout : -1;
        this.password = password;
    }


    public RedisConnection create() throws Exception {
        final RedisConnectionImpl redis;
        redis = new RedisConnectionImpl(this.host, this.port);
        if (timeout > 0) {
            redis.setTimeout(timeout);
        }


        redis.connect();
        if (null != this.password) {
            redis.sendCommand(Protocol.Command.AUTH);
            if (!"OK".equals(redis.getStatusCodeReply())) {
                throw new RedisException("Authentication failed");
            }
        }
        return redis;
    }
}
