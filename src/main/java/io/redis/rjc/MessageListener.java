package io.redis.rjc;

/**
 * @author Evgeny Dolgov
 */
public interface MessageListener {

    void onMessage(String channel, String message);

//    void onPMessage(String pattern, String channel,
//            String message);
}
