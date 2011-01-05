package io.redis.rjc.message;

/**
 * @author Evgeny Dolgov
 */
public interface PMessageListener {

    void onMessage(String pattern, String channel, String message);
}
