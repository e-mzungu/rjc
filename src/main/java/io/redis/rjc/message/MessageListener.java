package io.redis.rjc.message;

/**
 * @author Evgeny Dolgov
 */
public interface MessageListener {

    void onMessage(String channel, String message);
}
