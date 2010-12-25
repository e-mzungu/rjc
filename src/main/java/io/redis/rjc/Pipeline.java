package io.redis.rjc;

public interface Pipeline {
    
    void execute(RedisCommands commands);
}
