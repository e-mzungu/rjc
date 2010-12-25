package io.redis.rjc;

public interface RedisMonitor {
    
    void onCommand(String command);
}