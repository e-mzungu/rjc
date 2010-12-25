package io.redis.rjc;

import java.io.IOException;

public class RedisException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = -2946266495682282677L;

    public RedisException(String message) {
        super(message);
    }

    public RedisException(IOException e) {
        super(e);
    }

    public RedisException(String message, Throwable cause) {
        super(message, cause);
    }
}
