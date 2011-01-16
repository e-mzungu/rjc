package io.redis.rjc;

import io.redis.rjc.ds.DataSource;

/**
 * @author Evgeny Dolgov
 */
public interface NodeFactory<T> {
    T create(DataSource dataSource);
}
