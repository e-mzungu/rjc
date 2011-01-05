package io.redis.rjc.message;

/**
 * @author Evgeny Dolgov
 */
public interface SubscribeListener {
    /**
     * Means that we successfully subscribed to the given channel     *
     * @param channel subscribed channel
     * @param subscribedChannels the number of channels we are currently subscribed to
     */
    void onSubscribe(String channel, long subscribedChannels);

    /**
     * Means that we successfully unsubscribed from the given channel
     *
     * @param channel unsubscribed channel
     * @param subscribedChannels the number of channels we are currently subscribed to
     */
    void onUnsubscribe(String channel, long subscribedChannels);

    /**
     * Means that we successfully subscribed to the given pattern
     * @param pattern subscribed pattern
     * @param subscribedChannels the number of channels we are currently subscribed to
     */
    void onPSubscribe(String pattern, long subscribedChannels);

    /**
     * Means that we successfully unsubscribed from the given pattern
     *
     * @param pattern unsubscribed pattern
     * @param subscribedChannels the number of channels we are currently subscribed to
     */
    void onPUnsubscribe(String pattern, long subscribedChannels);
}
