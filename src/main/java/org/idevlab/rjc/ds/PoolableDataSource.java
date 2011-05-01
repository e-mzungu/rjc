/*
 * Copyright 2010-2011. Evgeny Dolgov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.idevlab.rjc.ds;

import org.idevlab.rjc.RedisException;
import org.idevlab.rjc.protocol.Protocol;
import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.idevlab.rjc.protocol.RedisCommand;

/**
 * Provides a poolable implementation of <code>DataSource</code> based on <em>commons-pool</em>
 *
 * @author Evgeny Dolgov
 */
public class PoolableDataSource implements DataSource {

    private volatile GenericObjectPool connectionPool;

    private String host;
    private int port = Protocol.DEFAULT_PORT;
    private int timeout = Protocol.DEFAULT_TIMEOUT;
    private String password;
    protected boolean closed;


    public RedisConnection getConnection() {
        try {
            return (RedisConnection) createPool().borrowObject();
        } catch (Exception e) {
            throw new RedisException("Cannot get a connection", e);
        }
    }

    public void init() {
        getConnection().close();
    }

    private synchronized GenericObjectPool createPool() {
        if (closed) {
            throw new RedisException("Data source is closed");
        }


        if (connectionPool == null) {
            GenericObjectPool gop = new GenericObjectPool();
            gop.setMaxActive(maxActive);
            gop.setMaxIdle(maxIdle);
            gop.setMinIdle(minIdle);
            gop.setMaxWait(maxWait);
            gop.setTestOnBorrow(testOnBorrow);
            gop.setTestOnReturn(testOnReturn);
            gop.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
            gop.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
            gop.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
            gop.setTestWhileIdle(testWhileIdle);
            connectionPool = gop;
            createConnectionFactory();
            try {
                for (int i = 0; i < initialSize; i++) {
                    connectionPool.addObject();
                }
            } catch (Exception e) {
                throw new RedisException("Error preloading the connection pool", e);
            }
        }

        return connectionPool;
    }

    private void createConnectionFactory() {
        PoolableConnectionFactory connectionFactory = new PoolableConnectionFactory(host, port, timeout, password, connectionPool);
        try {
            validateConnectionFactory(connectionFactory);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RedisException("Cannot create PoolableConnectionFactory", e);
        }

    }

    private static void validateConnectionFactory(PoolableConnectionFactory connectionFactory) throws Exception {
        PoolableRedisConnection conn = null;
        try {
            conn = (PoolableRedisConnection) connectionFactory.makeObject();
            connectionFactory.validateObject(conn);
        } finally {
            connectionFactory.destroyObject(conn);
        }
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public synchronized void close() {
        closed = true;
        GenericObjectPool oldPool = connectionPool;
        connectionPool = null;
        try {
            if (oldPool != null) {
                oldPool.close();
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RedisException("Cannot close connection pool", e);
        }
    }

    /**
     * The maximum number of active connections that can be allocated from
     * this pool at the same time, or negative for no limit.
     */
    protected int maxActive = GenericObjectPool.DEFAULT_MAX_ACTIVE;

    /**
     * <p>Returns the maximum number of active connections that can be
     * allocated at the same time.
     * </p>
     * <p>A negative number means that there is no limit.</p>
     *
     * @return the maximum number of active connections
     */
    public synchronized int getMaxActive() {
        return this.maxActive;
    }

    /**
     * Sets the maximum number of active connections that can be
     * allocated at the same time. Use a negative value for no limit.
     *
     * @param maxActive the new value for maxActive
     * @see #getMaxActive()
     */
    public synchronized void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
        if (connectionPool != null) {
            connectionPool.setMaxActive(maxActive);
        }
    }

    /**
     * The maximum number of connections that can remain idle in the
     * pool, without extra ones being released, or negative for no limit.
     * If maxIdle is set too low on heavily loaded systems it is possible you
     * will see connections being closed and almost immediately new connections
     * being opened. This is a result of the active threads momentarily closing
     * connections faster than they are opening them, causing the number of idle
     * connections to rise above maxIdle. The best value for maxIdle for heavily
     * loaded system will vary but the default is a good starting point.
     */
    protected int maxIdle = GenericObjectPool.DEFAULT_MAX_IDLE;

    /**
     * <p>Returns the maximum number of connections that can remain idle in the
     * pool.
     * </p>
     * <p>A negative value indicates that there is no limit</p>
     *
     * @return the maximum number of idle connections
     */
    public synchronized int getMaxIdle() {
        return this.maxIdle;
    }

    /**
     * Sets the maximum number of connections that can remain idle in the
     * pool.
     *
     * @param maxIdle the new value for maxIdle
     * @see #getMaxIdle()
     */
    public synchronized void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
        if (connectionPool != null) {
            connectionPool.setMaxIdle(maxIdle);
        }
    }

    /**
     * The minimum number of active connections that can remain idle in the
     * pool, without extra ones being created, or 0 to create none.
     */
    protected int minIdle = GenericObjectPool.DEFAULT_MIN_IDLE;

    /**
     * Returns the minimum number of idle connections in the pool
     *
     * @return the minimum number of idle connections
     * @see GenericObjectPool#getMinIdle()
     */
    public synchronized int getMinIdle() {
        return this.minIdle;
    }

    /**
     * Sets the minimum number of idle connections in the pool.
     *
     * @param minIdle the new value for minIdle
     * @see GenericObjectPool#setMinIdle(int)
     */
    public synchronized void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
        if (connectionPool != null) {
            connectionPool.setMinIdle(minIdle);
        }
    }

    /**
     * The initial number of connections that are created when the pool
     * is started.
     */
    protected int initialSize = 0;

    /**
     * Returns the initial size of the connection pool.
     *
     * @return the number of connections created when the pool is initialized
     */
    public synchronized int getInitialSize() {
        return this.initialSize;
    }

    /**
     * <p>Sets the initial size of the connection pool.</p>
     * <p>
     * Note: this method currently has no effect once the pool has been
     * initialized.  The pool is initialized the first time one of the
     * <code>getConnection</code> method is invoked.
     * </p>
     *
     * @param initialSize the number of connections created when the pool
     *                    is initialized
     */
    public synchronized void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }

    /**
     * The maximum number of milliseconds that the pool will wait (when there
     * are no available connections) for a connection to be returned before
     * throwing an exception, or <= 0 to wait indefinitely.
     */
    protected long maxWait = GenericObjectPool.DEFAULT_MAX_WAIT;

    /**
     * <p>Returns the maximum number of milliseconds that the pool will wait
     * for a connection to be returned before throwing an exception.
     * </p>
     * <p>A value less than or equal to zero means the pool is set to wait
     * indefinitely.</p>
     *
     * @return the maxWait property value
     */
    public synchronized long getMaxWait() {
        return this.maxWait;
    }

    /**
     * <p>Sets the maxWait property.
     * </p>
     * <p>Use -1 to make the pool wait indefinitely.
     * </p>
     *
     * @param maxWait the new value for maxWait
     * @see #getMaxWait()
     */
    public synchronized void setMaxWait(long maxWait) {
        this.maxWait = maxWait;
        if (connectionPool != null) {
            connectionPool.setMaxWait(maxWait);
        }
    }

    /**
     * The indication of whether objects will be validated before being
     * borrowed from the pool.  If the object fails to validate, it will be
     * dropped from the pool, and we will attempt to borrow another.
     */
    protected boolean testOnBorrow = true;

    /**
     * Returns the {@link #testOnBorrow} property.
     *
     * @return true if objects are validated before being borrowed from the
     *         pool
     * @see #testOnBorrow
     */
    public synchronized boolean getTestOnBorrow() {
        return this.testOnBorrow;
    }

    /**
     * Sets the {@link #testOnBorrow} property. This property determines
     * whether or not the pool will validate objects before they are borrowed
     * from the pool. For a <code>true</code> value to have any effect, the
     * <code>validationQuery</code> property must be set to a non-null string.
     *
     * @param testOnBorrow new value for testOnBorrow property
     */
    public synchronized void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
        if (connectionPool != null) {
            connectionPool.setTestOnBorrow(testOnBorrow);
        }
    }

    /**
     * The indication of whether objects will be validated before being
     * returned to the pool.
     */
    protected boolean testOnReturn = false;

    /**
     * Returns the value of the {@link #testOnReturn} property.
     *
     * @return true if objects are validated before being returned to the
     *         pool
     * @see #testOnReturn
     */
    public synchronized boolean getTestOnReturn() {
        return this.testOnReturn;
    }

    /**
     * Sets the <code>testOnReturn</code> property. This property determines
     * whether or not the pool will validate objects before they are returned
     * to the pool. For a <code>true</code> value to have any effect, the
     * <code>validationQuery</code> property must be set to a non-null string.
     *
     * @param testOnReturn new value for testOnReturn property
     */
    public synchronized void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
        if (connectionPool != null) {
            connectionPool.setTestOnReturn(testOnReturn);
        }
    }

    /**
     * The number of milliseconds to sleep between runs of the idle object
     * evictor thread.  When non-positive, no idle object evictor thread will
     * be run.
     */
    protected long timeBetweenEvictionRunsMillis =
            GenericObjectPool.DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS;

    /**
     * Returns the value of the {@link #timeBetweenEvictionRunsMillis}
     * property.
     *
     * @return the time (in miliseconds) between evictor runs
     * @see #timeBetweenEvictionRunsMillis
     */
    public synchronized long getTimeBetweenEvictionRunsMillis() {
        return this.timeBetweenEvictionRunsMillis;
    }

    /**
     * Sets the {@link #timeBetweenEvictionRunsMillis} property.
     *
     * @param timeBetweenEvictionRunsMillis the new time between evictor runs
     * @see #timeBetweenEvictionRunsMillis
     */
    public synchronized void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
        if (connectionPool != null) {
            connectionPool.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        }
    }

    /**
     * The number of objects to examine during each run of the idle object
     * evictor thread (if any).
     */
    protected int numTestsPerEvictionRun =
            GenericObjectPool.DEFAULT_NUM_TESTS_PER_EVICTION_RUN;

    /**
     * Returns the value of the {@link #numTestsPerEvictionRun} property.
     *
     * @return the number of objects to examine during idle object evictor
     *         runs
     * @see #numTestsPerEvictionRun
     */
    public synchronized int getNumTestsPerEvictionRun() {
        return this.numTestsPerEvictionRun;
    }

    /**
     * Sets the value of the {@link #numTestsPerEvictionRun} property.
     *
     * @param numTestsPerEvictionRun the new {@link #numTestsPerEvictionRun}
     *                               value
     * @see #numTestsPerEvictionRun
     */
    public synchronized void setNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
        this.numTestsPerEvictionRun = numTestsPerEvictionRun;
        if (connectionPool != null) {
            connectionPool.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
        }
    }

    /**
     * The minimum amount of time an object may sit idle in the pool before it
     * is eligable for eviction by the idle object evictor (if any).
     */
    protected long minEvictableIdleTimeMillis =
            GenericObjectPool.DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS;

    /**
     * Returns the {@link #minEvictableIdleTimeMillis} property.
     *
     * @return the value of the {@link #minEvictableIdleTimeMillis} property
     * @see #minEvictableIdleTimeMillis
     */
    public synchronized long getMinEvictableIdleTimeMillis() {
        return this.minEvictableIdleTimeMillis;
    }

    /**
     * Sets the {@link #minEvictableIdleTimeMillis} property.
     *
     * @param minEvictableIdleTimeMillis the minimum amount of time an object
     *                                   may sit idle in the pool
     * @see #minEvictableIdleTimeMillis
     */
    public synchronized void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
        if (connectionPool != null) {
            connectionPool.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        }
    }

    /**
     * The indication of whether objects will be validated by the idle object
     * evictor (if any).  If an object fails to validate, it will be dropped
     * from the pool.
     */
    protected boolean testWhileIdle = false;

    /**
     * Returns the value of the {@link #testWhileIdle} property.
     *
     * @return true if objects examined by the idle object evictor are
     *         validated
     * @see #testWhileIdle
     */
    public synchronized boolean getTestWhileIdle() {
        return this.testWhileIdle;
    }

    /**
     * Sets the <code>testWhileIdle</code> property. This property determines
     * whether or not the idle object evictor will validate connections.  For a
     * <code>true</code> value to have any effect, the
     * <code>validationQuery</code> property must be set to a non-null string.
     *
     * @param testWhileIdle new value for testWhileIdle property
     */
    public synchronized void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
        if (connectionPool != null) {
            connectionPool.setTestWhileIdle(testWhileIdle);
        }
    }

    /**
     * [Read Only] The current number of active connections that have been
     * allocated from this data source.
     *
     * @return the current number of active connections
     */
    public synchronized int getNumActive() {
        if (connectionPool != null) {
            return connectionPool.getNumActive();
        } else {
            return 0;
        }
    }


    /**
     * [Read Only] The current number of idle connections that are waiting
     * to be allocated from this data source.
     *
     * @return the current number of idle connections
     */
    public synchronized int getNumIdle() {
        if (connectionPool != null) {
            return connectionPool.getNumIdle();
        } else {
            return 0;
        }
    }


    private static class PoolableConnectionFactory extends BasePoolableObjectFactory {
        private final ObjectPool pool;
        private final ConnectionFactory connectionFactory;


        public PoolableConnectionFactory(final String host, final int port,
                                         final int timeout, final String password, ObjectPool pool) {
            super();
            this.connectionFactory = new ConnectionFactoryImpl(host, port, timeout, password);
            this.pool = pool;

            pool.setFactory(this);
        }

        public Object makeObject() throws Exception {
            return new PoolableRedisConnection(connectionFactory.create(), pool);
        }

        public void destroyObject(final Object obj) throws Exception {
            if (obj instanceof PoolableRedisConnection) {
                final PoolableRedisConnection redis = (PoolableRedisConnection) obj;
                if (redis.isConnected()) {
                    try {
                        redis.sendCommand(RedisCommand.QUIT);
                        redis.reallyDisconnect();
                    } catch (Exception e) {
                        //...
                    }
                }
            }
        }

        public boolean validateObject(final Object obj) {
            if (obj instanceof RedisConnection) {
                final RedisConnection redis = (RedisConnection) obj;
                try {
                    if (redis.isConnected()) {
                        redis.sendCommand(RedisCommand.PING);
                        return "PONG".equals(redis.getStatusCodeReply());
                    }
                } catch (Exception e) {
                    return false;
                }
            }
            return false;
        }
    }
}
