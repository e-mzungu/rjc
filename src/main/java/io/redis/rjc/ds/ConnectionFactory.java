package io.redis.rjc.ds;

/**
 * @author Evgeny Dolgov
 */
interface ConnectionFactory {

    RedisConnection create() throws Exception;
}
