package io.redis.rjc;

import io.redis.rjc.Client.LIST_POSITION;
import io.redis.rjc.ds.DataSource;
import io.redis.rjc.ds.RedisConnection;

import java.util.*;

public class RedisNode implements SingleRedisOperations {

    private DataSource dataSource;
    private int weight = 1;

    public RedisNode() {
    }

    public RedisNode(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public RedisNode(DataSource dataSource, int weight) {
        this.dataSource = dataSource;
        this.weight = weight;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String ping() {
        return execute(new RedisCommand<String>() {
            public String execute(Client client) {
                client.ping();
                return client.getStatusCodeReply();
            }
        });
    }


    private interface RedisCommand<R> {
        R execute(Client client);
    }

    private <R> R execute(RedisCommand<R> cmd) {
        RedisConnection connection = null;
        try {
            connection = dataSource.getConnection();
            return cmd.execute(new Client(connection));
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    //...
                }
            }
        }
    }


    /**
     * Set the string value as value of the key. The string can't be longer than
     * 1073741824 bytes (1 GB).
     * <p/>
     * Time complexity: O(1)
     *
     * @param key   the key
     * @param value the value
     * @return Status code reply
     */
    public String set(final String key, final String value) {
        return execute(new RedisCommand<String>() {
            public String execute(Client client) {
                client.set(key, value);
                return client.getStatusCodeReply();
            }
        });
    }

    /**
     * Get the value of the specified key. If the key does not exist the special
     * value 'nil' is returned. If the value stored at key is not a string an
     * error is returned because GET can only handle string values.
     * <p/>
     * Time complexity: O(1)
     *
     * @param key the key
     * @return Bulk reply
     */
    public String get(final String key) {
        return execute(new RedisCommand<String>() {
            public String execute(Client client) {
                client.get(key);
                return client.getBulkReply();
            }
        });
    }

    /**
     * Ask the server to silently close the connection.
     */
    public void quit() {
        execute(new RedisCommand<Object>() {
            public Object execute(Client client) {
                client.quit();
                return null;
            }
        });
    }

    /**
     * Test if the specified key exists. The command returns "0" if the key
     * exists, otherwise "1" is returned. Note that even keys set with an empty
     * string as value will return "1".
     * <p/>
     * Time complexity: O(1)
     *
     * @param key the key
     * @return Integer reply, "1" if the key exists, otherwise "0"
     */
    public Boolean exists(final String key) {
        return execute(new RedisCommand<Boolean>() {
            public Boolean execute(Client client) {
                client.exists(key);
                return client.getIntegerReply() == 1;
            }
        });
    }

    /**
     * Remove the specified keys. If a given key does not exist no operation is
     * performed for this key. The command returns the number of keys removed.
     * <p/>
     * Time complexity: O(1)
     *
     * @param keys the keys
     * @return Integer reply, specifically: an integer greater than 0 if one or
     *         more keys were removed 0 if none of the specified key existed
     */
    public Long del(final String... keys) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.del(keys);
                return client.getIntegerReply();
            }
        });

    }

    /**
     * Return the type of the value stored at key in form of a string. The type
     * can be one of "none", "string", "list", "set". "none" is returned if the
     * key does not exist.
     * <p/>
     * Time complexity: O(1)
     *
     * @param key the key
     * @return Status code reply, specifically: "none" if the key does not exist
     *         "string" if the key contains a String value "list" if the key
     *         contains a List value "set" if the key contains a Set value
     *         "zset" if the key contains a Sorted Set value "hash" if the key
     *         contains a Hash value
     */
    public String type(final String key) {
        return execute(new RedisCommand<String>() {
            public String execute(Client client) {
                client.type(key);
                return client.getStatusCodeReply();
            }
        });
    }

    /**
     * Delete all the keys of the currently selected DB. This command never
     * fails.
     *
     * @return Status code reply
     */
    public String flushDB() {
        return execute(new RedisCommand<String>() {
            public String execute(Client client) {
                client.flushDB();
                return client.getStatusCodeReply();
            }
        });
    }

    /**
     * Returns all the keys matching the glob-style pattern as space separated
     * strings. For example if you have in the database the keys "foo" and
     * "foobar" the command "KEYS foo*" will return "foo foobar".
     * <p/>
     * Note that while the time complexity for this operation is O(n) the
     * constant times are pretty low. For example Redis running on an entry
     * level laptop can scan a 1 million keys database in 40 milliseconds.
     * <b>Still it's better to consider this one of the slow commands that may
     * ruin the DB performance if not used with care.</b>
     * <p/>
     * In other words this command is intended only for debugging and special
     * operations like creating a script to change the DB schema. Don't use it
     * in your normal code. Use Redis Sets in order to group together a subset
     * of objects.
     * <p/>
     * Glob style patterns examples:
     * <ul>
     * <li>h?llo will match hello hallo hhllo
     * <li>h*llo will match hllo heeeello
     * <li>h[ae]llo will match hello and hallo, but not hillo
     * </ul>
     * <p/>
     * Use \ to escape special chars if you want to match them verbatim.
     * <p/>
     * Time complexity: O(n) (with n being the number of keys in the DB, and
     * assuming keys and pattern of limited length)
     *
     * @param pattern pattern
     * @return Multi bulk reply
     */
    public Set<String> keys(final String pattern) {
        return execute(new RedisCommand<Set<String>>() {
            public Set<String> execute(Client client) {
                client.keys(pattern);
                return new LinkedHashSet<String>(client.getMultiBulkReply());
            }
        });
    }

    /**
     * Return a randomly selected key from the currently selected DB.
     * <p/>
     * Time complexity: O(1)
     *
     * @return Singe line reply, specifically the randomly selected key or an
     *         empty string is the database is empty
     */
    public String randomBinaryKey() {
        return execute(new RedisCommand<String>() {
            public String execute(Client client) {
                client.randomKey();
                return client.getBulkReply();
            }
        });
    }

    /**
     * Atomically renames the key oldkey to newkey. If the source and
     * destination name are the same an error is returned. If newkey already
     * exists it is overwritten.
     * <p/>
     * Time complexity: O(1)
     *
     * @param oldkey oldkey
     * @param newkey newkey
     * @return Status code repy
     */
    public String rename(final String oldkey, final String newkey) {
        return execute(new RedisCommand<String>() {
            public String execute(Client client) {
                client.rename(oldkey, newkey);
                return client.getStatusCodeReply();
            }
        });
    }

    /**
     * Rename oldkey into newkey but fails if the destination key newkey already
     * exists.
     * <p/>
     * Time complexity: O(1)
     *
     * @param oldkey oldkey
     * @param newkey newkey
     * @return Integer reply, specifically: 1 if the key was renamed 0 if the
     *         target key already exist
     */
    public Long renamenx(final String oldkey, final String newkey) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.renamenx(oldkey, newkey);
                return client.getIntegerReply();
            }
        });
    }

    /**
     * Return the number of keys in the currently selected database.
     *
     * @return Integer reply
     */
    public Long dbSize() {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.dbSize();
                return client.getIntegerReply();
            }
        });
    }

    /**
     * Set a timeout on the specified key. After the timeout the key will be
     * automatically deleted by the server. A key with an associated timeout is
     * said to be volatile in Redis terminology.
     * <p/>
     * Voltile keys are stored on disk like the other keys, the timeout is
     * persistent too like all the other aspects of the dataset. Saving a
     * dataset containing expires and stopping the server does not stop the flow
     * of time as Redis stores on disk the time when the key will no longer be
     * available as Unix time, and not the remaining seconds.
     * <p/>
     * Since Redis 2.1.3 you can update the value of the timeout of a key
     * already having an expire set. It is also possible to undo the expire at
     * all turning the key into a normal key using the {@link #persist(String)
     * PERSIST} command.
     * <p/>
     * Time complexity: O(1)
     *
     * @param key     the key
     * @param seconds seconds
     * @return Integer reply, specifically: 1: the timeout was set. 0: the
     *         timeout was not set since the key already has an associated
     *         timeout (this may happen only in Redis versions < 2.1.3, Redis >=
     *         2.1.3 will happily update the timeout), or the key does not
     *         exist.
     * @see <ahref="http://code.google.com/p/redis/wiki/ExpireCommand">ExpireCommand</a>
     */
    public Long expire(final String key, final int seconds) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.expire(key, seconds);
                return client.getIntegerReply();
            }
        });
    }

    /**
     * EXPIREAT works exctly like {@link #expire(String, int) EXPIRE} but
     * instead to get the number of seconds representing the Time To Live of the
     * key as a second argument (that is a relative way of specifing the TTL),
     * it takes an absolute one in the form of a UNIX timestamp (Number of
     * seconds elapsed since 1 Gen 1970).
     * <p/>
     * EXPIREAT was introduced in order to implement the Append Only File
     * persistence mode so that EXPIRE commands are automatically translated
     * into EXPIREAT commands for the append only file. Of course EXPIREAT can
     * also used by programmers that need a way to simply specify that a given
     * key should expire at a given time in the future.
     * <p/>
     * Since Redis 2.1.3 you can update the value of the timeout of a key
     * already having an expire set. It is also possible to undo the expire at
     * all turning the key into a normal key using the {@link #persist(String)
     * PERSIST} command.
     * <p/>
     * Time complexity: O(1)
     *
     * @param key      the key
     * @param unixTime unix time
     * @return Integer reply, specifically: 1: the timeout was set. 0: the
     *         timeout was not set since the key already has an associated
     *         timeout (this may happen only in Redis versions < 2.1.3, Redis >=
     *         2.1.3 will happily update the timeout), or the key does not
     *         exist.
     * @see <ahref="http://code.google.com/p/redis/wiki/ExpireCommand">ExpireCommand</a>
     */
    public Long expireAt(final String key, final long unixTime) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.expireAt(key, unixTime);
                return client.getIntegerReply();
            }
        });
    }

    /**
     * The TTL command returns the remaining time to live in seconds of a key
     * that has an {@link #expire(String, int) EXPIRE} set. This introspection
     * capability allows a Redis client to check how many seconds a given key
     * will continue to be part of the dataset.
     *
     * @param key the key
     * @return Integer reply, returns the remaining time to live in seconds of a
     *         key that has an EXPIRE. If the Key does not exists or does not
     *         have an associated expire, -1 is returned.
     */
    public Long ttl(final String key) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.ttl(key);
                return client.getIntegerReply();
            }
        });
    }

    /**
     * Select the DB with having the specified zero-based numeric index. For
     * default every new client connection is automatically selected to DB 0.
     *
     * @param index index
     * @return Status code reply
     */
    public String select(final int index) {
        return execute(new RedisCommand<String>() {
            public String execute(Client client) {
                client.select(index);
                return client.getStatusCodeReply();
            }
        });
    }

    /**
     * Move the specified key from the currently selected DB to the specified
     * destination DB. Note that this command returns 1 only if the key was
     * successfully moved, and 0 if the target key was already there or if the
     * source key was not found at all, so it is possible to use MOVE as a
     * locking primitive.
     *
     * @param key     the key
     * @param dbIndex
     * @return Integer reply, specifically: 1 if the key was moved 0 if the key
     *         was not moved because already present on the target DB or was not
     *         found in the current DB.
     */
    public Long move(final String key, final int dbIndex) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.move(key, dbIndex);
                return client.getIntegerReply();
            }
        });
    }

    /**
     * Delete all the keys of all the existing databases, not just the currently
     * selected one. This command never fails.
     *
     * @return Status code reply
     */
    public String flushAll() {
        return execute(new RedisCommand<String>() {
            public String execute(Client client) {
                client.flushAll();
                return client.getStatusCodeReply();
            }
        });
    }

    /**
     * GETSET is an atomic set this value and return the old value command. Set
     * key to the string value and return the old value stored at key. The
     * string can't be longer than 1073741824 bytes (1 GB).
     * <p/>
     * Time complexity: O(1)
     *
     * @param key   the key
     * @param value the value
     * @return Bulk reply
     */
    public String getSet(final String key, final String value) {
        return execute(new RedisCommand<String>() {
            public String execute(Client client) {
                client.getSet(key, value);
                return client.getBulkReply();
            }
        });
    }

    /**
     * Get the values of all the specified keys. If one or more keys dont exist
     * or is not of type String, a 'nil' value is returned instead of the value
     * of the specified key, but the operation never fails.
     * <p/>
     * Time complexity: O(1) for every key
     *
     * @param keys the keys
     * @return Multi bulk reply
     */
    public List<String> mget(final String... keys) {
        return execute(new RedisCommand<List<String>>() {
            public List<String> execute(Client client) {
                client.mget(keys);
                return client.getMultiBulkReply();
            }
        });
    }

    /**
     * SETNX works exactly like {@link #set(String, String) SET} with the only
     * difference that if the key already exists no operation is performed.
     * SETNX actually means "SET if Not eXists".
     * <p/>
     * Time complexity: O(1)
     *
     * @param key   the key
     * @param value the value
     * @return Integer reply, specifically: 1 if the key was set 0 if the key
     *         was not set
     */
    public Long setnx(final String key, final String value) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.setnx(key, value);
                return client.getIntegerReply();
            }
        });
    }

    /**
     * The command is exactly equivalent to the following group of commands:
     * {@link #set(String, String) SET} + {@link #expire(String, int) EXPIRE}.
     * The operation is atomic.
     * <p/>
     * Time complexity: O(1)
     *
     * @param key     the key
     * @param seconds
     * @param value   the value
     * @return Status code reply
     */
    public String setex(final String key, final int seconds, final String value) {
        return execute(new RedisCommand<String>() {
            public String execute(Client client) {
                client.setex(key, seconds, value);
                return client.getStatusCodeReply();
            }
        });
    }

    /**
     * Set the the respective keys to the respective values. MSET will replace
     * old values with new values, while {@link #msetnx(String...) MSETNX} will
     * not perform any operation at all even if just a single key already
     * exists.
     * <p/>
     * Because of this semantic MSETNX can be used in order to set different
     * keys representing different fields of an unique logic object in a way
     * that ensures that either all the fields or none at all are set.
     * <p/>
     * Both MSET and MSETNX are atomic operations. This means that for instance
     * if the keys A and B are modified, another client talking to Redis can
     * either see the changes to both A and B at once, or no modification at
     * all.
     *
     * @param keysvalues
     * @return Status code reply Basically +OK as MSET can't fail
     * @see #msetnx(String...)
     */
    public String mset(final String... keysvalues) {
        return execute(new RedisCommand<String>() {
            public String execute(Client client) {
                client.mset(keysvalues);
                return client.getStatusCodeReply();
            }
        });
    }

    /**
     * Set the the respective keys to the respective values.
     * {@link #mset(String...) MSET} will replace old values with new values,
     * while MSETNX will not perform any operation at all even if just a single
     * key already exists.
     * <p/>
     * Because of this semantic MSETNX can be used in order to set different
     * keys representing different fields of an unique logic object in a way
     * that ensures that either all the fields or none at all are set.
     * <p/>
     * Both MSET and MSETNX are atomic operations. This means that for instance
     * if the keys A and B are modified, another client talking to Redis can
     * either see the changes to both A and B at once, or no modification at
     * all.
     *
     * @param keysvalues
     * @return Integer reply, specifically: 1 if the all the keys were set 0 if
     *         no key was set (at least one key already existed)
     * @see #mset(String...)
     */
    public Long msetnx(final String... keysvalues) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.msetnx(keysvalues);
                return client.getIntegerReply();
            }
        });
    }

    /**
     * IDECRBY work just like {@link #decr(String) INCR} but instead to
     * decrement by 1 the decrement is integer.
     * <p/>
     * INCR commands are limited to 64 bit signed integers.
     * <p/>
     * Note: this is actually a string operation, that is, in Redis there are
     * not "integer" types. Simply the string stored at the key is parsed as a
     * base 10 64 bit signed integer, incremented, and then converted back as a
     * string.
     * <p/>
     * Time complexity: O(1)
     *
     * @param key     the key
     * @param integer
     * @return Integer reply, this commands will reply with the new value of key
     *         after the increment.
     * @see #incr(String)
     * @see #decr(String)
     * @see #incrBy(String, int)
     */
    public Long decrBy(final String key, final int integer) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.decrBy(key, integer);
                return client.getIntegerReply();
            }
        });
    }

    /**
     * Decrement the number stored at key by one. If the key does not exist or
     * contains a value of a wrong type, set the key to the value of "0" before
     * to perform the decrement operation.
     * <p/>
     * INCR commands are limited to 64 bit signed integers.
     * <p/>
     * Note: this is actually a string operation, that is, in Redis there are
     * not "integer" types. Simply the string stored at the key is parsed as a
     * base 10 64 bit signed integer, incremented, and then converted back as a
     * string.
     * <p/>
     * Time complexity: O(1)
     *
     * @param key the key
     * @return Integer reply, this commands will reply with the new value of key
     *         after the increment.
     * @see #incr(String)
     * @see #incrBy(String, int)
     * @see #decrBy(String, int)
     */
    public Long decr(final String key) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.decr(key);
                return client.getIntegerReply();
            }
        });
    }

    /**
     * INCRBY work just like {@link #incr(String) INCR} but instead to increment
     * by 1 the increment is integer.
     * <p/>
     * INCR commands are limited to 64 bit signed integers.
     * <p/>
     * Note: this is actually a string operation, that is, in Redis there are
     * not "integer" types. Simply the string stored at the key is parsed as a
     * base 10 64 bit signed integer, incremented, and then converted back as a
     * string.
     * <p/>
     * Time complexity: O(1)
     *
     * @param key     the key
     * @param integer
     * @return Integer reply, this commands will reply with the new value of key
     *         after the increment.
     * @see #incr(String)
     * @see #decr(String)
     * @see #decrBy(String, int)
     */
    public Long incrBy(final String key, final int integer) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.incrBy(key, integer);
                return client.getIntegerReply();
            }
        });
    }

    /**
     * Increment the number stored at key by one. If the key does not exist or
     * contains a value of a wrong type, set the key to the value of "0" before
     * to perform the increment operation.
     * <p/>
     * INCR commands are limited to 64 bit signed integers.
     * <p/>
     * Note: this is actually a string operation, that is, in Redis there are
     * not "integer" types. Simply the string stored at the key is parsed as a
     * base 10 64 bit signed integer, incremented, and then converted back as a
     * string.
     * <p/>
     * Time complexity: O(1)
     *
     * @param key the key
     * @return Integer reply, this commands will reply with the new value of key
     *         after the increment.
     * @see #incrBy(String, int)
     * @see #decr(String)
     * @see #decrBy(String, int)
     */
    public Long incr(final String key) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.incr(key);
                return client.getIntegerReply();
            }
        });
    }

    /**
     * If the key already exists and is a string, this command appends the
     * provided value at the end of the string. If the key does not exist it is
     * created and set as an empty string, so APPEND will be very similar to SET
     * in this special case.
     * <p/>
     * Time complexity: O(1). The amortized time complexity is O(1) assuming the
     * appended value is small and the already present value is of any size,
     * since the dynamic string library used by Redis will double the free space
     * available on every reallocation.
     *
     * @param key   the key
     * @param value the value
     * @return Integer reply, specifically the total length of the string after
     *         the append operation.
     */
    public Long append(final String key, final String value) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.append(key, value);
                return client.getIntegerReply();
            }
        });
    }

    /**
     * Return a subset of the string from offset start to offset end (both
     * offsets are inclusive). Negative offsets can be used in order to provide
     * an offset starting from the end of the string. So -1 means the last char,
     * -2 the penultimate and so forth.
     * <p/>
     * The function handles out of range requests without raising an error, but
     * just limiting the resulting range to the actual length of the string.
     * <p/>
     * Time complexity: O(start+n) (with start being the start index and n the
     * total length of the requested range). Note that the lookup part of this
     * command is O(1) so for small strings this is actually an O(1) command.
     *
     * @param key   the key
     * @param start
     * @param end
     * @return Bulk reply
     */
    public String substr(final String key, final int start, final int end) {
        return execute(new RedisCommand<String>() {
            public String execute(Client client) {
                client.substr(key, start, end);
                return client.getBulkReply();
            }
        });
    }

    /**
     * Set the specified hash field to the specified value.
     * <p/>
     * If key does not exist, a new key holding a hash is created.
     * <p/>
     * <b>Time complexity:</b> O(1)
     *
     * @param key   the key
     * @param field
     * @param value the value
     * @return If the field already exists, and the HSET just produced an update
     *         of the value, 0 is returned, otherwise if a new field is created
     *         1 is returned.
     */
    public Long hset(final String key, final String field, final String value) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.hset(key, field, value);
                return client.getIntegerReply();
            }
        });
    }

    /**
     * If key holds a hash, retrieve the value associated to the specified
     * field.
     * <p/>
     * If the field is not found or the key does not exist, a special 'nil'
     * value is returned.
     * <p/>
     * <b>Time complexity:</b> O(1)
     *
     * @param key   the key
     * @param field
     * @return Bulk reply
     */
    public String hget(final String key, final String field) {
        return execute(new RedisCommand<String>() {
            public String execute(Client client) {
                client.hget(key, field);
                return client.getBulkReply();
            }
        });
    }

    /**
     * Set the specified hash field to the specified value if the field not
     * exists. <b>Time complexity:</b> O(1)
     *
     * @param key   the key
     * @param field
     * @param value the value
     * @return If the field already exists, 0 is returned, otherwise if a new
     *         field is created 1 is returned.
     */
    public Long hsetnx(final String key, final String field, final String value) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.hsetnx(key, field, value);
                return client.getIntegerReply();
            }
        });
    }

    /**
     * Set the respective fields to the respective values. HMSET replaces old
     * values with new values.
     * <p/>
     * If key does not exist, a new key holding a hash is created.
     * <p/>
     * <b>Time complexity:</b> O(N) (with N being the number of fields)
     *
     * @param key  the key
     * @param hash
     * @return Always OK because HMSET can't fail
     */
    public String hmset(final String key, final Map<String, String> hash) {
        return execute(new RedisCommand<String>() {
            public String execute(Client client) {
                client.hmset(key, hash);
                return client.getStatusCodeReply();
            }
        });
    }

    /**
     * Retrieve the values associated to the specified fields.
     * <p/>
     * If some of the specified fields do not exist, nil values are returned.
     * Non existing keys are considered like empty hashes.
     * <p/>
     * <b>Time complexity:</b> O(N) (with N being the number of fields)
     *
     * @param key    the key
     * @param fields
     * @return Multi Bulk Reply specifically a list of all the values associated
     *         with the specified fields, in the same order of the request.
     */
    public List<String> hmget(final String key, final String... fields) {
        return execute(new RedisCommand<List<String>>() {
            public List<String> execute(Client client) {
                client.hmget(key, fields);
                return client.getMultiBulkReply();
            }
        });
    }

    /**
     * Increment the number stored at field in the hash at key by value. If key
     * does not exist, a new key holding a hash is created. If field does not
     * exist or holds a string, the value is set to 0 before applying the
     * operation. Since the value argument is signed you can use this command to
     * perform both increments and decrements.
     * <p/>
     * The range of values supported by HINCRBY is limited to 64 bit signed
     * integers.
     * <p/>
     * <b>Time complexity:</b> O(1)
     *
     * @param key   the key
     * @param field
     * @param value the value
     * @return Integer reply The new value at field after the increment
     *         operation.
     */
    public Long hincrBy(final String key, final String field, final int value) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.hincrBy(key, field, value);
                return client.getIntegerReply();
            }
        });
    }

    /**
     * Test for existence of a specified field in a hash.
     * <p/>
     * <b>Time complexity:</b> O(1)
     *
     * @param key   the key
     * @param field
     * @return Return 1 if the hash stored at key contains the specified field.
     *         Return 0 if the key is not found or the field is not present.
     */
    public Boolean hexists(final String key, final String field) {
        return execute(new RedisCommand<Boolean>() {
            public Boolean execute(Client client) {
                client.hexists(key, field);
                return client.getIntegerReply() == 1;
            }
        });
    }

    /**
     * Remove the specified field from an hash stored at key.
     * <p/>
     * <b>Time complexity:</b> O(1)
     *
     * @param key   the key
     * @param field
     * @return If the field was present in the hash it is deleted and 1 is
     *         returned, otherwise 0 is returned and no operation is performed.
     */
    public Long hdel(final String key, final String field) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.hdel(key, field);
                return client.getIntegerReply();
            }
        });
    }

    /**
     * Return the number of items in a hash.
     * <p/>
     * <b>Time complexity:</b> O(1)
     *
     * @param key the key
     * @return The number of entries (fields) contained in the hash stored at
     *         key. If the specified key does not exist, 0 is returned assuming
     *         an empty hash.
     */
    public Long hlen(final String key) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.hlen(key);
                return client.getIntegerReply();
            }
        });
    }

    /**
     * Return all the fields in a hash.
     * <p/>
     * <b>Time complexity:</b> O(N), where N is the total number of entries
     *
     * @param key the key
     * @return All the fields names contained into a hash.
     */
    public Set<String> hkeys(final String key) {
        return execute(new RedisCommand<Set<String>>() {
            public Set<String> execute(Client client) {
                client.hkeys(key);
                return new LinkedHashSet<String>(client.getMultiBulkReply());
            }
        });
    }

    /**
     * Return all the values in a hash.
     * <p/>
     * <b>Time complexity:</b> O(N), where N is the total number of entries
     *
     * @param key the key
     * @return All the fields values contained into a hash.
     */
    public List<String> hvals(final String key) {
        return execute(new RedisCommand<List<String>>() {
            public List<String> execute(Client client) {
                client.hvals(key);
                return client.getMultiBulkReply();
            }
        });
    }

    /**
     * Return all the fields and associated values in a hash.
     * <p/>
     * <b>Time complexity:</b> O(N), where N is the total number of entries
     *
     * @param key the key
     * @return All the fields and values contained into a hash.
     */
    public Map<String, String> hgetAll(final String key) {
        return execute(new RedisCommand<Map<String, String>>() {
            public Map<String, String> execute(Client client) {
                client.hgetAll(key);
                final List<String> flatHash = client.getMultiBulkReply();
                final Map<String, String> hash = new HashMap<String, String>();
                final Iterator<String> iterator = flatHash.iterator();
                while (iterator.hasNext()) {
                    hash.put(iterator.next(), iterator.next());
                }

                return hash;
            }
        });
    }

    /**
     * Add the string value to the head (LPUSH) or tail (RPUSH) of the list
     * stored at key. If the key does not exist an empty list is created just
     * before the append operation. If the key exists but is not a List an error
     * is returned.
     * <p/>
     * Time complexity: O(1)
     *
     * @param key    the key
     * @param string
     * @return Integer reply, specifically, the number of elements inside the
     *         list after the push operation.
     * @see RedisNode#lpush(String, String)
     */
    public Long rpush(final String key, final String string) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.rpush(key, string);
                return client.getIntegerReply();
            }
        });
    }

    /**
     * Add the string value to the head (LPUSH) or tail (RPUSH) of the list
     * stored at key. If the key does not exist an empty list is created just
     * before the append operation. If the key exists but is not a List an error
     * is returned.
     * <p/>
     * Time complexity: O(1)
     *
     * @param key    the key
     * @param string
     * @return Integer reply, specifically, the number of elements inside the
     *         list after the push operation.
     * @see RedisNode#rpush(String, String)
     */
    public Long lpush(final String key, final String string) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.lpush(key, string);
                return client.getIntegerReply();
            }
        });
    }

    /**
     * Return the length of the list stored at the specified key. If the key
     * does not exist zero is returned (the same behaviour as for empty lists).
     * If the value stored at key is not a list an error is returned.
     * <p/>
     * Time complexity: O(1)
     *
     * @param key the key
     * @return The length of the list.
     */
    public Long llen(final String key) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.llen(key);
                return client.getIntegerReply();
            }
        });
    }

    /**
     * Return the specified elements of the list stored at the specified key.
     * Start and end are zero-based indexes. 0 is the first element of the list
     * (the list head), 1 the next element and so on.
     * <p/>
     * For example LRANGE foobar 0 2 will return the first three elements of the
     * list.
     * <p/>
     * start and end can also be negative numbers indicating offsets from the
     * end of the list. For example -1 is the last element of the list, -2 the
     * penultimate element and so on.
     * <p/>
     * <b>Consistency with range functions in various programming languages</b>
     * <p/>
     * Note that if you have a list of numbers from 0 to 100, LRANGE 0 10 will
     * return 11 elements, that is, rightmost item is included. This may or may
     * not be consistent with behavior of range-related functions in your
     * programming language of choice (think Ruby's Range.new, Array#slice or
     * Python's range() function).
     * <p/>
     * LRANGE behavior is consistent with one of Tcl.
     * <p/>
     * <b>Out-of-range indexes</b>
     * <p/>
     * Indexes out of range will not produce an error: if start is over the end
     * of the list, or start > end, an empty list is returned. If end is over
     * the end of the list Redis will threat it just like the last element of
     * the list.
     * <p/>
     * Time complexity: O(start+n) (with n being the length of the range and
     * start being the start offset)
     *
     * @param key   the key
     * @param start
     * @param end
     * @return Multi bulk reply, specifically a list of elements in the
     *         specified range.
     */
    public List<String> lrange(final String key, final int start, final int end) {
        return execute(new RedisCommand<List<String>>() {
            public List<String> execute(Client client) {
                client.lrange(key, start, end);
                return client.getMultiBulkReply();
            }
        });
    }

    /**
     * Trim an existing list so that it will contain only the specified range of
     * elements specified. Start and end are zero-based indexes. 0 is the first
     * element of the list (the list head), 1 the next element and so on.
     * <p/>
     * For example LTRIM foobar 0 2 will modify the list stored at foobar key so
     * that only the first three elements of the list will remain.
     * <p/>
     * start and end can also be negative numbers indicating offsets from the
     * end of the list. For example -1 is the last element of the list, -2 the
     * penultimate element and so on.
     * <p/>
     * Indexes out of range will not produce an error: if start is over the end
     * of the list, or start > end, an empty list is left as value. If end over
     * the end of the list Redis will threat it just like the last element of
     * the list.
     * <p/>
     * Hint: the obvious use of LTRIM is together with LPUSH/RPUSH. For example:
     * <p/>
     * {@code lpush("mylist", "someelement"); ltrim("mylist", 0, 99); * }
     * <p/>
     * The above two commands will push elements in the list taking care that
     * the list will not grow without limits. This is very useful when using
     * Redis to store logs for example. It is important to note that when used
     * in this way LTRIM is an O(1) operation because in the average case just
     * one element is removed from the tail of the list.
     * <p/>
     * Time complexity: O(n) (with n being len of list - len of range)
     *
     * @param key   the key
     * @param start
     * @param end
     * @return Status code reply
     */
    public String ltrim(final String key, final int start, final int end) {
        return execute(new RedisCommand<String>() {
            public String execute(Client client) {
                client.ltrim(key, start, end);
                return client.getStatusCodeReply();
            }
        });
    }

    /**
     * Return the specified element of the list stored at the specified key. 0
     * is the first element, 1 the second and so on. Negative indexes are
     * supported, for example -1 is the last element, -2 the penultimate and so
     * on.
     * <p/>
     * If the value stored at key is not of list type an error is returned. If
     * the index is out of range a 'nil' reply is returned.
     * <p/>
     * Note that even if the average time complexity is O(n) asking for the
     * first or the last element of the list is O(1).
     * <p/>
     * Time complexity: O(n) (with n being the length of the list)
     *
     * @param key   the key
     * @param index
     * @return Bulk reply, specifically the requested element
     */
    public String lindex(final String key, final int index) {
        return execute(new RedisCommand<String>() {
            public String execute(Client client) {
                client.lindex(key, index);
                return client.getBulkReply();
            }
        });
    }

    /**
     * Set a new value as the element at index position of the List at key.
     * <p/>
     * Out of range indexes will generate an error.
     * <p/>
     * Similarly to other list commands accepting indexes, the index can be
     * negative to access elements starting from the end of the list. So -1 is
     * the last element, -2 is the penultimate, and so forth.
     * <p/>
     * <b>Time complexity:</b>
     * <p/>
     * O(N) (with N being the length of the list), setting the first or last
     * elements of the list is O(1).
     *
     * @param key   the key
     * @param index
     * @param value the value
     * @return Status code reply
     * @see #lindex(String, int)
     */
    public String lset(final String key, final int index, final String value) {
        return execute(new RedisCommand<String>() {
            public String execute(Client client) {
                client.lset(key, index, value);
                return client.getStatusCodeReply();
            }
        });
    }

    /**
     * Remove the first count occurrences of the value element from the list. If
     * count is zero all the elements are removed. If count is negative elements
     * are removed from tail to head, instead to go from head to tail that is
     * the normal behaviour. So for example LREM with count -2 and hello as
     * value to remove against the list (a,b,c,hello,x,hello,hello) will lave
     * the list (a,b,c,hello,x). The number of removed elements is returned as
     * an integer, see below for more information about the returned value. Note
     * that non existing keys are considered like empty lists by LREM, so LREM
     * against non existing keys will always return 0.
     * <p/>
     * Time complexity: O(N) (with N being the length of the list)
     *
     * @param key   the key
     * @param count count
     * @param value the value
     * @return Integer Reply, specifically: The number of removed elements if
     *         the operation succeeded
     */
    public Long lrem(final String key, final int count, final String value) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.lrem(key, count, value);
                return client.getIntegerReply();
            }
        });
    }

    /**
     * Atomically return and remove the first (LPOP) or last (RPOP) element of
     * the list. For example if the list contains the elements "a","b","c" LPOP
     * will return "a" and the list will become "b","c".
     * <p/>
     * If the key does not exist or the list is already empty the special value
     * 'nil' is returned.
     *
     * @param key the key
     * @return Bulk reply
     * @see #rpop(String)
     */
    public String lpop(final String key) {
        return execute(new RedisCommand<String>() {
            public String execute(Client client) {
                client.lpop(key);
                return client.getBulkReply();
            }
        });
    }

    /**
     * Atomically return and remove the first (LPOP) or last (RPOP) element of
     * the list. For example if the list contains the elements "a","b","c" LPOP
     * will return "a" and the list will become "b","c".
     * <p/>
     * If the key does not exist or the list is already empty the special value
     * 'nil' is returned.
     *
     * @param key the key
     * @return Bulk reply
     * @see #lpop(String)
     */
    public String rpop(final String key) {
        return execute(new RedisCommand<String>() {
            public String execute(Client client) {
                client.rpop(key);
                return client.getBulkReply();
            }
        });
    }

    /**
     * Atomically return and remove the last (tail) element of the srckey list,
     * and push the element as the first (head) element of the dstkey list. For
     * example if the source list contains the elements "a","b","c" and the
     * destination list contains the elements "foo","bar" after an RPOPLPUSH
     * command the content of the two lists will be "a","b" and "c","foo","bar".
     * <p/>
     * If the key does not exist or the list is already empty the special value
     * 'nil' is returned. If the srckey and dstkey are the same the operation is
     * equivalent to removing the last element from the list and pusing it as
     * first element of the list, so it's a "list rotation" command.
     * <p/>
     * Time complexity: O(1)
     *
     * @param srckey
     * @param dstkey
     * @return Bulk reply
     */
    public String rpoplpush(final String srckey, final String dstkey) {
        return execute(new RedisCommand<String>() {
            public String execute(Client client) {
                client.rpoplpush(srckey, dstkey);
                return client.getBulkReply();
            }
        });
    }

    /**
     * Add the specified member to the set value stored at key. If member is
     * already a member of the set no operation is performed. If key does not
     * exist a new set with the specified member as sole member is created. If
     * the key exists but does not hold a set value an error is returned.
     * <p/>
     * Time complexity O(1)
     *
     * @param key    the key
     * @param member
     * @return Integer reply, specifically: 1 if the new element was added 0 if
     *         the element was already a member of the set
     */
    public Long sadd(final String key, final String member) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.sadd(key, member);
                return client.getIntegerReply();
            }
        });
    }

    /**
     * Return all the members (elements) of the set value stored at key. This is
     * just syntax glue for {@link #sinter(String...) SINTER}.
     * <p/>
     * Time complexity O(N)
     *
     * @param key the key
     * @return Multi bulk reply
     */
    public Set<String> smembers(final String key) {
        return execute(new RedisCommand<Set<String>>() {
            public Set<String> execute(Client client) {
                client.smembers(key);
                final List<String> members = client.getMultiBulkReply();
                return new LinkedHashSet<String>(members);
            }
        });
    }

    /**
     * Remove the specified member from the set value stored at key. If member
     * was not a member of the set no operation is performed. If key does not
     * hold a set value an error is returned.
     * <p/>
     * Time complexity O(1)
     *
     * @param key    the key
     * @param member
     * @return Integer reply, specifically: 1 if the new element was removed 0
     *         if the new element was not a member of the set
     */
    public Long srem(final String key, final String member) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.srem(key, member);
                return client.getIntegerReply();
            }
        });
    }

    /**
     * Remove a random element from a Set returning it as return value. If the
     * Set is empty or the key does not exist, a nil object is returned.
     * <p/>
     * The {@link #srandmember(String)} command does a similar work but the
     * returned element is not removed from the Set.
     * <p/>
     * Time complexity O(1)
     *
     * @param key the key
     * @return Bulk reply
     */
    public String spop(final String key) {
        return execute(new RedisCommand<String>() {
            public String execute(Client client) {
                client.spop(key);
                return client.getBulkReply();
            }
        });
    }

    /**
     * Move the specifided member from the set at srckey to the set at dstkey.
     * This operation is atomic, in every given moment the element will appear
     * to be in the source or destination set for accessing clients.
     * <p/>
     * If the source set does not exist or does not contain the specified
     * element no operation is performed and zero is returned, otherwise the
     * element is removed from the source set and added to the destination set.
     * On success one is returned, even if the element was already present in
     * the destination set.
     * <p/>
     * An error is raised if the source or destination keys contain a non Set
     * value.
     * <p/>
     * Time complexity O(1)
     *
     * @param srckey
     * @param dstkey
     * @param member
     * @return Integer reply, specifically: 1 if the element was moved 0 if the
     *         element was not found on the first set and no operation was
     *         performed
     */
    public Long smove(final String srckey, final String dstkey, final String member) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.smove(srckey, dstkey, member);
                return client.getIntegerReply();
            }
        });
    }

    /**
     * Return the set cardinality (number of elements). If the key does not
     * exist 0 is returned, like for empty sets.
     *
     * @param key the key
     * @return Integer reply, specifically: the cardinality (number of elements)
     *         of the set as an integer.
     */
    public Long scard(final String key) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.scard(key);
                return client.getIntegerReply();
            }
        });
    }

    /**
     * Return 1 if member is a member of the set stored at key, otherwise 0 is
     * returned.
     * <p/>
     * Time complexity O(1)
     *
     * @param key    the key
     * @param member
     * @return Integer reply, specifically: 1 if the element is a member of the
     *         set 0 if the element is not a member of the set OR if the key
     *         does not exist
     */
    public Boolean sismember(final String key, final String member) {
        return execute(new RedisCommand<Boolean>() {
            public Boolean execute(Client client) {
                client.sismember(key, member);
                return client.getIntegerReply() == 1;
            }
        });
    }

    /**
     * Return the members of a set resulting from the intersection of all the
     * sets hold at the specified keys. Like in
     * {@link #lrange(String, int, int) LRANGE} the result is sent to the client
     * as a multi-bulk reply (see the protocol specification for more
     * information). If just a single key is specified, then this command
     * produces the same result as {@link #smembers(String) SMEMBERS}. Actually
     * SMEMBERS is just syntax sugar for SINTER.
     * <p/>
     * Non existing keys are considered like empty sets, so if one of the keys
     * is missing an empty set is returned (since the intersection with an empty
     * set always is an empty set).
     * <p/>
     * Time complexity O(N*M) worst case where N is the cardinality of the
     * smallest set and M the number of sets
     *
     * @param keys the keys
     * @return Multi bulk reply, specifically the list of common elements.
     */
    public Set<String> sinter(final String... keys) {
        return execute(new RedisCommand<Set<String>>() {
            public Set<String> execute(Client client) {
                client.sinter(keys);
                final List<String> members = client.getMultiBulkReply();
                return new LinkedHashSet<String>(members);
            }
        });
    }

    /**
     * This commnad works exactly like {@link #sinter(String...) SINTER} but
     * instead of being returned the resulting set is sotred as dstkey.
     * <p/>
     * Time complexity O(N*M) worst case where N is the cardinality of the
     * smallest set and M the number of sets
     *
     * @param dstkey
     * @param keys   the keys
     * @return Status code reply
     */
    public Long sinterstore(final String dstkey, final String... keys) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.sinterstore(dstkey, keys);
                return client.getIntegerReply();
            }
        });
    }

    /**
     * Return the members of a set resulting from the union of all the sets hold
     * at the specified keys. Like in {@link #lrange(String, int, int) LRANGE}
     * the result is sent to the client as a multi-bulk reply (see the protocol
     * specification for more information). If just a single key is specified,
     * then this command produces the same result as {@link #smembers(String)
     * SMEMBERS}.
     * <p/>
     * Non existing keys are considered like empty sets.
     * <p/>
     * Time complexity O(N) where N is the total number of elements in all the
     * provided sets
     *
     * @param keys the keys
     * @return Multi bulk reply, specifically the list of common elements.
     */
    public Set<String> sunion(final String... keys) {
        return execute(new RedisCommand<Set<String>>() {
            public Set<String> execute(Client client) {
                client.sunion(keys);
                final List<String> members = client.getMultiBulkReply();
                return new LinkedHashSet<String>(members);
            }
        });
    }

    /**
     * This command works exactly like {@link #sunion(String...) SUNION} but
     * instead of being returned the resulting set is stored as dstkey. Any
     * existing value in dstkey will be over-written.
     * <p/>
     * Time complexity O(N) where N is the total number of elements in all the
     * provided sets
     *
     * @param dstkey
     * @param keys   the keys
     * @return Status code reply
     */
    public Long sunionstore(final String dstkey, final String... keys) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.sunionstore(dstkey, keys);
                return client.getIntegerReply();
            }
        });
    }

    /**
     * Return the difference between the Set stored at key1 and all the Sets
     * key2, ..., keyN
     * <p/>
     * <b>Example:</b>
     * <p/>
     * <pre>
     * key1 = [x, a, b, c]
     * key2 = [c]
     * key3 = [a, d]
     * SDIFF key1,key2,key3 => [x, b]
     * </pre>
     * <p/>
     * Non existing keys are considered like empty sets.
     * <p/>
     * <b>Time complexity:</b>
     * <p/>
     * O(N) with N being the total number of elements of all the sets
     *
     * @param keys the keys
     * @return Return the members of a set resulting from the difference between
     *         the first set provided and all the successive sets.
     */
    public Set<String> sdiff(final String... keys) {
        return execute(new RedisCommand<Set<String>>() {
            public Set<String> execute(Client client) {
                client.sdiff(keys);
                final List<String> members = client.getMultiBulkReply();
                return new LinkedHashSet<String>(members);
            }
        });
    }

    /**
     * This command works exactly like {@link #sdiff(String...) SDIFF} but
     * instead of being returned the resulting set is stored in dstkey.
     *
     * @param dstkey
     * @param keys   the keys
     * @return Status code reply
     */
    public Long sdiffstore(final String dstkey, final String... keys) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.sdiffstore(dstkey, keys);
                return client.getIntegerReply();
            }
        });
    }

    /**
     * Return a random element from a Set, without removing the element. If the
     * Set is empty or the key does not exist, a nil object is returned.
     * <p/>
     * The SPOP command does a similar work but the returned element is popped
     * (removed) from the Set.
     * <p/>
     * Time complexity O(1)
     *
     * @param key the key
     * @return Bulk reply
     */
    public String srandmember(final String key) {
        return execute(new RedisCommand<String>() {
            public String execute(Client client) {
                client.srandmember(key);
                return client.getBulkReply();
            }
        });
    }

    /**
     * Add the specified member having the specifeid score to the sorted set
     * stored at key. If member is already a member of the sorted set the score
     * is updated, and the element reinserted in the right position to ensure
     * sorting. If key does not exist a new sorted set with the specified member
     * as sole member is crated. If the key exists but does not hold a sorted
     * set value an error is returned.
     * <p/>
     * The score value can be the string representation of a double precision
     * floating point number.
     * <p/>
     * Time complexity O(log(N)) with N being the number of elements in the
     * sorted set
     *
     *
     * @param key    the key
     * @param score
     * @param member
     * @return Integer reply, specifically: 1 if the new element was added 0 if
     *         the element was already a member of the sorted set and the score
     *         was updated
     */
    public Long zadd(final String key, final Number score, final String member) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.zadd(key, score, member);
                return client.getIntegerReply();
            }
        });
    }

    public Set<String> zrange(final String key, final int start, final int end) {
        return execute(new RedisCommand<Set<String>>() {
            public Set<String> execute(Client client) {
                client.zrange(key, start, end);
                final List<String> members = client.getMultiBulkReply();
                return new LinkedHashSet<String>(members);
            }
        });
    }

    /**
     * Remove the specified member from the sorted set value stored at key. If
     * member was not a member of the set no operation is performed. If key does
     * not not hold a set value an error is returned.
     * <p/>
     * Time complexity O(log(N)) with N being the number of elements in the
     * sorted set
     *
     * @param key    the key
     * @param member
     * @return Integer reply, specifically: 1 if the new element was removed 0
     *         if the new element was not a member of the set
     */
    public Long zrem(final String key, final String member) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.zrem(key, member);
                return client.getIntegerReply();
            }
        });
    }

    /**
     * If member already exists in the sorted set adds the increment to its
     * score and updates the position of the element in the sorted set
     * accordingly. If member does not already exist in the sorted set it is
     * added with increment as score (that is, like if the previous score was
     * virtually zero). If key does not exist a new sorted set with the
     * specified member as sole member is crated. If the key exists but does not
     * hold a sorted set value an error is returned.
     * <p/>
     * The score value can be the string representation of a double precision
     * floating point number. It's possible to provide a negative value to
     * perform a decrement.
     * <p/>
     * For an introduction to sorted sets check the Introduction to Redis data
     * types page.
     * <p/>
     * Time complexity O(log(N)) with N being the number of elements in the
     * sorted set
     *
     *
     *
     * @param key    the key
     * @param score
     * @param member
     * @return The new score
     */
    public String zincrby(final String key, final Number score,
                          final String member) {
        return execute(new RedisCommand<String>() {
            public String execute(Client client) {
                client.zincrby(key, score, member);
                return client.getBulkReply();
            }
        });
    }

    /**
     * Return the rank (or index) or member in the sorted set at key, with
     * scores being ordered from low to high.
     * <p/>
     * When the given member does not exist in the sorted set, the special value
     * 'nil' is returned. The returned rank (or index) of the member is 0-based
     * for both commands.
     * <p/>
     * <b>Time complexity:</b>
     * <p/>
     * O(log(N))
     *
     * @param key    the key
     * @param member
     * @return Integer reply or a nil bulk reply, specifically: the rank of the
     *         element as an integer reply if the element exists. A nil bulk
     *         reply if there is no such element.
     * @see #zrevrank(String, String)
     */
    public Long zrank(final String key, final String member) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.zrank(key, member);
                return client.getIntegerReply();
            }
        });
    }

    /**
     * Return the rank (or index) or member in the sorted set at key, with
     * scores being ordered from high to low.
     * <p/>
     * When the given member does not exist in the sorted set, the special value
     * 'nil' is returned. The returned rank (or index) of the member is 0-based
     * for both commands.
     * <p/>
     * <b>Time complexity:</b>
     * <p/>
     * O(log(N))
     *
     * @param key    the key
     * @param member
     * @return Integer reply or a nil bulk reply, specifically: the rank of the
     *         element as an integer reply if the element exists. A nil bulk
     *         reply if there is no such element.
     * @see #zrank(String, String)
     */
    public Long zrevrank(final String key, final String member) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.zrevrank(key, member);
                return client.getIntegerReply();
            }
        });
    }

    public Set<String> zrevrange(final String key, final int start,
                                 final int end) {
        return execute(new RedisCommand<Set<String>>() {
            public Set<String> execute(Client client) {
                client.zrevrange(key, start, end);
                final List<String> members = client.getMultiBulkReply();
                return new LinkedHashSet<String>(members);
            }
        });
    }

    public Map<String, String> zrangeWithScores(final String key, final int start,
                                       final int end) {
        return execute(new RedisCommand<Map<String, String>>() {
            public Map<String, String> execute(Client client) {
                client.zrangeWithScores(key, start, end);
                return getReplyAsMap(client);
            }
        });
    }

    public Map<String, String> zrevrangeWithScores(final String key, final int start,
                                          final int end) {
        return execute(new RedisCommand<Map<String, String>>() {
            public Map<String, String> execute(Client client) {
                client.zrevrangeWithScores(key, start, end);
                return getReplyAsMap(client);
            }
        });
    }

    /**
     * Return the sorted set cardinality (number of elements). If the key does
     * not exist 0 is returned, like for empty sorted sets.
     * <p/>
     * Time complexity O(1)
     *
     * @param key the key
     * @return the cardinality (number of elements) of the set as an integer.
     */
    public Long zcard(final String key) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.zcard(key);
                return client.getIntegerReply();
            }
        });
    }

    /**
     * Return the score of the specified element of the sorted set at key. If
     * the specified element does not exist in the sorted set, or the key does
     * not exist at all, a special 'nil' value is returned.
     * <p/>
     * <b>Time complexity:</b> O(1)
     *
     *
     * @param key    the key
     * @param member
     * @return the score
     */
    public String zscore(final String key, final String member) {
        return execute(new RedisCommand<String>() {
            public String execute(Client client) {
                client.zscore(key, member);
                return client.getBulkReply();
            }
        });
    }

    public String multi() {
         return execute(new RedisCommand<String>() {
            public String execute(Client client) {
                client.multi();
                return client.getStatusCodeReply();
            }
        });
    }

    public String discard() {
        return execute(new RedisCommand<String>() {
            public String execute(Client client) {
                client.discard();
                return client.getStatusCodeReply();
            }
        });
    }


    public String watch(final String... key) {
        return execute(new RedisCommand<String>() {
            public String execute(Client client) {
                client.watch(key);
                return client.getStatusCodeReply();
            }
        });
    }

    public String unwatch() {
        return execute(new RedisCommand<String>() {
            public String execute(Client client) {
                client.unwatch();
                return client.getStatusCodeReply();
            }
        });
    }

    /**
     * Sort a Set or a List.
     * <p/>
     * Sort the elements contained in the List, Set, or Sorted Set value at key.
     * By default sorting is numeric with elements being compared as double
     * precision floating point numbers. This is the simplest form of SORT.
     *
     * @param key the key
     * @return Assuming the Set/List at key contains a list of numbers, the
     *         return value will be the list of numbers ordered from the
     *         smallest to the biggest number.
     * @see #sort(String, String)
     * @see #sort(String, SortingParams)
     * @see #sort(String, SortingParams, String)
     */
    public List<String> sort(final String key) {
        return execute(new RedisCommand<List<String>>() {
            public List<String> execute(Client client) {
                client.sort(key);
                return client.getMultiBulkReply();
            }
        });
    }

    /**
     * Sort a Set or a List accordingly to the specified parameters.
     * <p/>
     * <b>examples:</b>
     * <p/>
     * Given are the following sets and key/values:
     * <p/>
     * <pre>
     * x = [1, 2, 3]
     * y = [a, b, c]
     * <p/>
     * k1 = z
     * k2 = y
     * k3 = x
     * <p/>
     * w1 = 9
     * w2 = 8
     * w3 = 7
     * </pre>
     * <p/>
     * Sort Order:
     * <p/>
     * <pre>
     * sort(x) or sort(x, sp.asc())
     * -> [1, 2, 3]
     * <p/>
     * sort(x, sp.desc())
     * -> [3, 2, 1]
     * <p/>
     * sort(y)
     * -> [c, a, b]
     * <p/>
     * sort(y, sp.alpha())
     * -> [a, b, c]
     * <p/>
     * sort(y, sp.alpha().desc())
     * -> [c, a, b]
     * </pre>
     * <p/>
     * Limit (e.g. for Pagination):
     * <p/>
     * <pre>
     * sort(x, sp.limit(0, 2))
     * -> [1, 2]
     * <p/>
     * sort(y, sp.alpha().desc().limit(1, 2))
     * -> [b, a]
     * </pre>
     * <p/>
     * Sorting by external keys:
     * <p/>
     * <pre>
     * sort(x, sb.by(w*))
     * -> [3, 2, 1]
     * <p/>
     * sort(x, sb.by(w*).desc())
     * -> [1, 2, 3]
     * </pre>
     * <p/>
     * Getting external keys:
     * <p/>
     * <pre>
     * sort(x, sp.by(w*).get(k*))
     * -> [x, y, z]
     * <p/>
     * sort(x, sp.by(w*).get(#).get(k*))
     * -> [3, x, 2, y, 1, z]
     * </pre>
     *
     * @param key               the key
     * @param sortingParameters
     * @return a list of sorted elements.
     * @see #sort(String)
     * @see #sort(String, SortingParams, String)
     */
    public List<String> sort(final String key,
                             final SortingParams sortingParameters) {
        return execute(new RedisCommand<List<String>>() {
            public List<String> execute(Client client) {
                client.sort(key, sortingParameters);
                return client.getMultiBulkReply();
            }
        });
    }

    /**
     * BLPOP (and BRPOP) is a blocking list pop primitive. You can see this
     * commands as blocking versions of LPOP and RPOP able to block if the
     * specified keys don't exist or contain empty lists.
     * <p/>
     * The following is a description of the exact semantic. We describe BLPOP
     * but the two commands are identical, the only difference is that BLPOP
     * pops the element from the left (head) of the list, and BRPOP pops from
     * the right (tail).
     * <p/>
     * <b>Non blocking behavior</b>
     * <p/>
     * When BLPOP is called, if at least one of the specified keys contain a non
     * empty list, an element is popped from the head of the list and returned
     * to the caller together with the name of the key (BLPOP returns a two
     * elements array, the first element is the key, the second the popped
     * value).
     * <p/>
     * Keys are scanned from left to right, so for instance if you issue BLPOP
     * list1 list2 list3 0 against a dataset where list1 does not exist but
     * list2 and list3 contain non empty lists, BLPOP guarantees to return an
     * element from the list stored at list2 (since it is the first non empty
     * list starting from the left).
     * <p/>
     * <b>Blocking behavior</b>
     * <p/>
     * If none of the specified keys exist or contain non empty lists, BLPOP
     * blocks until some other client performs a LPUSH or an RPUSH operation
     * against one of the lists.
     * <p/>
     * Once new data is present on one of the lists, the client finally returns
     * with the name of the key unblocking it and the popped value.
     * <p/>
     * When blocking, if a non-zero timeout is specified, the client will
     * unblock returning a nil special value if the specified amount of seconds
     * passed without a push operation against at least one of the specified
     * keys.
     * <p/>
     * The timeout argument is interpreted as an integer value. A timeout of
     * zero means instead to block forever.
     * <p/>
     * <b>Multiple clients blocking for the same keys</b>
     * <p/>
     * Multiple clients can block for the same key. They are put into a queue,
     * so the first to be served will be the one that started to wait earlier,
     * in a first-blpopping first-served fashion.
     * <p/>
     * <b>blocking POP inside a MULTI/EXEC transaction</b>
     * <p/>
     * BLPOP and BRPOP can be used with pipelining (sending multiple commands
     * and reading the replies in batch), but it does not make sense to use
     * BLPOP or BRPOP inside a MULTI/EXEC block (a Redis transaction).
     * <p/>
     * The behavior of BLPOP inside MULTI/EXEC when the list is empty is to
     * return a multi-bulk nil reply, exactly what happens when the timeout is
     * reached. If you like science fiction, think at it like if inside
     * MULTI/EXEC the time will flow at infinite speed :)
     * <p/>
     * Time complexity: O(1)
     *
     * @param timeout
     * @param keys    the keys
     * @return BLPOP returns a two-elements array via a multi bulk reply in
     *         order to return both the unblocking key and the popped value.
     *         <p/>
     *         When a non-zero timeout is specified, and the BLPOP operation
     *         timed out, the return value is a nil multi bulk reply. Most
     *         client values will return false or nil accordingly to the
     *         programming language used.
     * @see #brpop(int, String...)
     */
    public List<String> blpop(final int timeout, final String... keys) {
        return execute(new RedisCommand<List<String>>() {
            public List<String> execute(Client client) {
                final List<String> args = new ArrayList<String>();
                args.addAll(Arrays.asList(keys));
                args.add(String.valueOf(timeout));

                client.blpop(args.toArray(new String[args.size()]));
                client.setTimeoutInfinite();
                final List<String> multiBulkReply = client.getMultiBulkReply();
                client.rollbackTimeout();
                return multiBulkReply;
            }
        });
    }

    /**
     * Sort a Set or a List accordingly to the specified parameters and store
     * the result at dstkey.
     *
     * @param key               the key
     * @param sortingParameters
     * @param dstkey
     * @return The number of elements of the list at dstkey.
     * @see #sort(String, SortingParams)
     * @see #sort(String)
     * @see #sort(String, String)
     */
    public Long sort(final String key, final SortingParams sortingParameters,
                     final String dstkey) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.sort(key, sortingParameters, dstkey);
                return client.getIntegerReply();
            }
        });
    }

    /**
     * Sort a Set or a List and Store the Result at dstkey.
     * <p/>
     * Sort the elements contained in the List, Set, or Sorted Set value at key
     * and store the result at dstkey. By default sorting is numeric with
     * elements being compared as double precision floating point numbers. This
     * is the simplest form of SORT.
     *
     * @param key    the key
     * @param dstkey
     * @return The number of elements of the list at dstkey.
     * @see #sort(String)
     * @see #sort(String, SortingParams)
     * @see #sort(String, SortingParams, String)
     */
    public Long sort(final String key, final String dstkey) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.sort(key, dstkey);
                return client.getIntegerReply();
            }
        });
    }

    /**
     * BLPOP (and BRPOP) is a blocking list pop primitive. You can see this
     * commands as blocking versions of LPOP and RPOP able to block if the
     * specified keys don't exist or contain empty lists.
     * <p/>
     * The following is a description of the exact semantic. We describe BLPOP
     * but the two commands are identical, the only difference is that BLPOP
     * pops the element from the left (head) of the list, and BRPOP pops from
     * the right (tail).
     * <p/>
     * <b>Non blocking behavior</b>
     * <p/>
     * When BLPOP is called, if at least one of the specified keys contain a non
     * empty list, an element is popped from the head of the list and returned
     * to the caller together with the name of the key (BLPOP returns a two
     * elements array, the first element is the key, the second the popped
     * value).
     * <p/>
     * Keys are scanned from left to right, so for instance if you issue BLPOP
     * list1 list2 list3 0 against a dataset where list1 does not exist but
     * list2 and list3 contain non empty lists, BLPOP guarantees to return an
     * element from the list stored at list2 (since it is the first non empty
     * list starting from the left).
     * <p/>
     * <b>Blocking behavior</b>
     * <p/>
     * If none of the specified keys exist or contain non empty lists, BLPOP
     * blocks until some other client performs a LPUSH or an RPUSH operation
     * against one of the lists.
     * <p/>
     * Once new data is present on one of the lists, the client finally returns
     * with the name of the key unblocking it and the popped value.
     * <p/>
     * When blocking, if a non-zero timeout is specified, the client will
     * unblock returning a nil special value if the specified amount of seconds
     * passed without a push operation against at least one of the specified
     * keys.
     * <p/>
     * The timeout argument is interpreted as an integer value. A timeout of
     * zero means instead to block forever.
     * <p/>
     * <b>Multiple clients blocking for the same keys</b>
     * <p/>
     * Multiple clients can block for the same key. They are put into a queue,
     * so the first to be served will be the one that started to wait earlier,
     * in a first-blpopping first-served fashion.
     * <p/>
     * <b>blocking POP inside a MULTI/EXEC transaction</b>
     * <p/>
     * BLPOP and BRPOP can be used with pipelining (sending multiple commands
     * and reading the replies in batch), but it does not make sense to use
     * BLPOP or BRPOP inside a MULTI/EXEC block (a Redis transaction).
     * <p/>
     * The behavior of BLPOP inside MULTI/EXEC when the list is empty is to
     * return a multi-bulk nil reply, exactly what happens when the timeout is
     * reached. If you like science fiction, think at it like if inside
     * MULTI/EXEC the time will flow at infinite speed :)
     * <p/>
     * Time complexity: O(1)
     *
     * @param timeout
     * @param keys    the keys
     * @return BLPOP returns a two-elements array via a multi bulk reply in
     *         order to return both the unblocking key and the popped value.
     *         <p/>
     *         When a non-zero timeout is specified, and the BLPOP operation
     *         timed out, the return value is a nil multi bulk reply. Most
     *         client values will return false or nil accordingly to the
     *         programming language used.
     * @see #blpop(int, String...)
     */
    public List<String> brpop(final int timeout, final String... keys) {
        return execute(new RedisCommand<List<String>>() {
            public List<String> execute(Client client) {
                final List<String> args = new ArrayList<String>(keys.length + 1);
                args.addAll(Arrays.asList(keys));
                args.add(String.valueOf(timeout));

                client.brpop(args.toArray(new String[args.size()]));
                client.setTimeoutInfinite();
                final List<String> multiBulkReply = client.getMultiBulkReply();
                client.rollbackTimeout();

                return multiBulkReply;
            }
        });
    }

    /**
     * Request for authentication in a password protected Redis server. A Redis
     * server can be instructed to require a password before to allow clients to
     * issue commands. This is done using the requirepass directive in the Redis
     * configuration file. If the password given by the client is correct the
     * server replies with an OK status code reply and starts accepting commands
     * from the client. Otherwise an error is returned and the clients needs to
     * try a new password. Note that for the high performance nature of Redis it
     * is possible to try a lot of passwords in parallel in very short time, so
     * make sure to generate a strong and very long password so that this attack
     * is infeasible.
     *
     * @param password
     * @return Status code reply
     */
    public String auth(final String password) {
        return execute(new RedisCommand<String>() {
            public String execute(Client client) {
                client.auth(password);
                return client.getStatusCodeReply();
            }
        });
    }

    public List<Object> pipelined(final Pipeline pipeline) {
        return execute(new RedisCommand<List<Object>>() {
            public List<Object> execute(Client client) {
                pipeline.execute(client);
                return client.getAll();
            }
        });
    }

    public void subscribe(final RedisPubSub redisPubSub,
                          final String... channels) {
        execute(new RedisCommand<Object>() {
            public Object execute(Client client) {
                client.setTimeoutInfinite();
                redisPubSub.proceed(client, channels);
                client.rollbackTimeout();
                return null;
            }
        });
    }

    public Long publish(final String channel, final String message) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.publish(channel, message);
                return client.getIntegerReply();
            }
        });
    }

    public void psubscribe(final RedisPubSub redisPubSub,
                           final String... patterns) {
        execute(new RedisCommand<Object>() {
            public Object execute(Client client) {
                client.setTimeoutInfinite();
                redisPubSub.proceedWithPatterns(client, patterns);
                client.rollbackTimeout();
                return null;
            }
        });
    }

    public Long zcount(final String key, final Number min, final Number max) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.zcount(key, min, max);
                return client.getIntegerReply();
            }
        });
    }

    /**
     * Return the all the elements in the sorted set at key with a score between
     * min and max (including elements with score equal to min or max).
     * <p/>
     * The elements having the same score are returned sorted lexicographically
     * as ASCII strings (this follows from a property of Redis sorted sets and
     * does not involve further computation).
     * <p/>
     * Using the optional
     * {@link #zrangeByScore(String, Number, Number, int, int) LIMIT} it's
     * possible to get only a range of the matching elements in an SQL-alike
     * way. Note that if offset is large the commands needs to traverse the list
     * for offset elements and this adds up to the O(M) figure.
     * <p/>
     * The {@link #zcount(String, Number, Number) ZCOUNT} command is similar to
     * {@link #zrangeByScore(String, Number, Number) ZRANGEBYSCORE} but instead
     * of returning the actual elements in the specified interval, it just
     * returns the number of matching elements.
     * <p/>
     * <b>Exclusive intervals and infinity</b>
     * <p/>
     * min and max can be -inf and +inf, so that you are not required to know
     * what's the greatest or smallest element in order to take, for instance,
     * elements "up to a given value".
     * <p/>
     * Also while the interval is for default closed (inclusive) it's possible
     * to specify open intervals prefixing the score with a "(" character, so
     * for instance:
     * <p/>
     * {@code ZRANGEBYSCORE zset (1.3 5}
     * <p/>
     * Will return all the values with score > 1.3 and <= 5, while for instance:
     * <p/>
     * {@code ZRANGEBYSCORE zset (5 (10}
     * <p/>
     * Will return all the values with score > 5 and < 10 (5 and 10 excluded).
     * <p/>
     * <b>Time complexity:</b>
     * <p/>
     * O(log(N))+O(M) with N being the number of elements in the sorted set and
     * M the number of elements returned by the command, so if M is constant
     * (for instance you always ask for the first ten elements with LIMIT) you
     * can consider it O(log(N))
     *
     * @param key the key
     * @param min
     * @param max
     * @return Multi bulk reply specifically a list of elements in the specified
     *         score range.
     * @see #zrangeByScore(String, Number, Number)
     * @see #zrangeByScore(String, Number, Number, int, int)
     * @see #zrangeByScoreWithScores(String, Number, Number)
     * @see #zrangeByScoreWithScores(String, Number, Number, int, int)
     * @see #zcount(String, Number, Number)
     */
    public Set<String> zrangeByScore(final String key, final Number min, final Number max) {
        return execute(new RedisCommand<Set<String>>() {
            public Set<String> execute(Client client) {
                client.zrangeByScore(key, min, max);
                return new LinkedHashSet<String>(client.getMultiBulkReply());
            }
        });
    }

    /**
     * Return the all the elements in the sorted set at key with a score between
     * min and max (including elements with score equal to min or max).
     * <p/>
     * The elements having the same score are returned sorted lexicographically
     * as ASCII strings (this follows from a property of Redis sorted sets and
     * does not involve further computation).
     * <p/>
     * Using the optional
     * {@link #zrangeByScore(String, Number, Number, int, int) LIMIT} it's
     * possible to get only a range of the matching elements in an SQL-alike
     * way. Note that if offset is large the commands needs to traverse the list
     * for offset elements and this adds up to the O(M) figure.
     * <p/>
     * The {@link #zcount(String, Number, Number) ZCOUNT} command is similar to
     * {@link #zrangeByScore(String, Number, Number) ZRANGEBYSCORE} but instead
     * of returning the actual elements in the specified interval, it just
     * returns the number of matching elements.
     * <p/>
     * <b>Exclusive intervals and infinity</b>
     * <p/>
     * min and max can be -inf and +inf, so that you are not required to know
     * what's the greatest or smallest element in order to take, for instance,
     * elements "up to a given value".
     * <p/>
     * Also while the interval is for default closed (inclusive) it's possible
     * to specify open intervals prefixing the score with a "(" character, so
     * for instance:
     * <p/>
     * {@code ZRANGEBYSCORE zset (1.3 5}
     * <p/>
     * Will return all the values with score > 1.3 and <= 5, while for instance:
     * <p/>
     * {@code ZRANGEBYSCORE zset (5 (10}
     * <p/>
     * Will return all the values with score > 5 and < 10 (5 and 10 excluded).
     * <p/>
     * <b>Time complexity:</b>
     * <p/>
     * O(log(N))+O(M) with N being the number of elements in the sorted set and
     * M the number of elements returned by the command, so if M is constant
     * (for instance you always ask for the first ten elements with LIMIT) you
     * can consider it O(log(N))
     *
     * @param key the key
     * @param min
     * @param max
     * @return Multi bulk reply specifically a list of elements in the specified
     *         score range.
     * @see #zrangeByScore(String, Number, Number)
     * @see #zrangeByScore(String, Number, Number, int, int)
     * @see #zrangeByScoreWithScores(String, Number, Number)
     * @see #zrangeByScoreWithScores(String, Number, Number, int, int)
     * @see #zcount(String, Number, Number)
     */
    public Set<String> zrangeByScore(final String key, final Number min,
                                     final Number max, final int offset, final int count) {
        return execute(new RedisCommand<Set<String>>() {
            public Set<String> execute(Client client) {
                client.zrangeByScore(key, min, max, offset, count);
                return new LinkedHashSet<String>(client.getMultiBulkReply());
            }
        });
    }

    /**
     * Return the all the elements in the sorted set at key with a score between
     * min and max (including elements with score equal to min or max).
     * <p/>
     * The elements having the same score are returned sorted lexicographically
     * as ASCII strings (this follows from a property of Redis sorted sets and
     * does not involve further computation).
     * <p/>
     * Using the optional
     * {@link #zrangeByScore(String, Number, Number, int, int) LIMIT} it's
     * possible to get only a range of the matching elements in an SQL-alike
     * way. Note that if offset is large the commands needs to traverse the list
     * for offset elements and this adds up to the O(M) figure.
     * <p/>
     * The {@link #zcount(String, Number, Number) ZCOUNT} command is similar to
     * {@link #zrangeByScore(String, Number, Number) ZRANGEBYSCORE} but instead
     * of returning the actual elements in the specified interval, it just
     * returns the number of matching elements.
     * <p/>
     * <b>Exclusive intervals and infinity</b>
     * <p/>
     * min and max can be -inf and +inf, so that you are not required to know
     * what's the greatest or smallest element in order to take, for instance,
     * elements "up to a given value".
     * <p/>
     * Also while the interval is for default closed (inclusive) it's possible
     * to specify open intervals prefixing the score with a "(" character, so
     * for instance:
     * <p/>
     * {@code ZRANGEBYSCORE zset (1.3 5}
     * <p/>
     * Will return all the values with score > 1.3 and <= 5, while for instance:
     * <p/>
     * {@code ZRANGEBYSCORE zset (5 (10}
     * <p/>
     * Will return all the values with score > 5 and < 10 (5 and 10 excluded).
     * <p/>
     * <b>Time complexity:</b>
     * <p/>
     * O(log(N))+O(M) with N being the number of elements in the sorted set and
     * M the number of elements returned by the command, so if M is constant
     * (for instance you always ask for the first ten elements with LIMIT) you
     * can consider it O(log(N))
     *
     * @param key the key
     * @param min
     * @param max
     * @return Multi bulk reply specifically a list of elements in the specified
     *         score range.
     * @see #zrangeByScore(String, Number, Number)
     * @see #zrangeByScore(String, Number, Number, int, int)
     * @see #zrangeByScoreWithScores(String, Number, Number)
     * @see #zrangeByScoreWithScores(String, Number, Number, int, int)
     * @see #zcount(String, Number, Number)
     */
    public Map<String, String> zrangeByScoreWithScores(final String key,
                                              final Number min, final Number max) {
        return execute(new RedisCommand<Map<String, String>>() {
            public Map<String, String> execute(Client client) {
                client.zrangeByScoreWithScores(key, min, max);
                return getReplyAsMap(client);
            }
        });
    }

    /**
     * Return the all the elements in the sorted set at key with a score between
     * min and max (including elements with score equal to min or max).
     * <p/>
     * The elements having the same score are returned sorted lexicographically
     * as ASCII strings (this follows from a property of Redis sorted sets and
     * does not involve further computation).
     * <p/>
     * Using the optional
     * {@link #zrangeByScore(String, Number, Number, int, int) LIMIT} it's
     * possible to get only a range of the matching elements in an SQL-alike
     * way. Note that if offset is large the commands needs to traverse the list
     * for offset elements and this adds up to the O(M) figure.
     * <p/>
     * The {@link #zcount(String, Number, Number) ZCOUNT} command is similar to
     * {@link #zrangeByScore(String, Number, Number) ZRANGEBYSCORE} but instead
     * of returning the actual elements in the specified interval, it just
     * returns the number of matching elements.
     * <p/>
     * <b>Exclusive intervals and infinity</b>
     * <p/>
     * min and max can be -inf and +inf, so that you are not required to know
     * what's the greatest or smallest element in order to take, for instance,
     * elements "up to a given value".
     * <p/>
     * Also while the interval is for default closed (inclusive) it's possible
     * to specify open intervals prefixing the score with a "(" character, so
     * for instance:
     * <p/>
     * {@code ZRANGEBYSCORE zset (1.3 5}
     * <p/>
     * Will return all the values with score > 1.3 and <= 5, while for instance:
     * <p/>
     * {@code ZRANGEBYSCORE zset (5 (10}
     * <p/>
     * Will return all the values with score > 5 and < 10 (5 and 10 excluded).
     * <p/>
     * <b>Time complexity:</b>
     * <p/>
     * O(log(N))+O(M) with N being the number of elements in the sorted set and
     * M the number of elements returned by the command, so if M is constant
     * (for instance you always ask for the first ten elements with LIMIT) you
     * can consider it O(log(N))
     *
     * @param key the key
     * @param min
     * @param max
     * @return Multi bulk reply specifically a list of elements in the specified
     *         score range.
     * @see #zrangeByScore(String, Number, Number)
     * @see #zrangeByScore(String, Number, Number, int, int)
     * @see #zrangeByScoreWithScores(String, Number, Number)
     * @see #zrangeByScoreWithScores(String, Number, Number, int, int)
     * @see #zcount(String, Number, Number)
     */
    public Map<String, String> zrangeByScoreWithScores(final String key,
                                              final Number min, final Number max, final int offset,
                                              final int count) {
        return execute(new RedisCommand<Map<String, String>>() {
            public Map<String, String> execute(Client client) {
                client.zrangeByScoreWithScores(key, min, max, offset, count);
                return getReplyAsMap(client);
            }
        });
    }

    private Map<String, String> getReplyAsMap(Client client) {
        List<String> membersWithScores = client.getMultiBulkReply();
        Map<String, String> result = new HashMap<String, String>();
        Iterator<String> iterator = membersWithScores.iterator();
        while (iterator.hasNext()) {
            result.put(iterator.next(), iterator.next());
        }
        return result;
    }

    /**
     * Remove all elements in the sorted set at key with rank between start and
     * end. Start and end are 0-based with rank 0 being the element with the
     * lowest score. Both start and end can be negative numbers, where they
     * indicate offsets starting at the element with the highest rank. For
     * example: -1 is the element with the highest score, -2 the element with
     * the second highest score and so forth.
     * <p/>
     * <b>Time complexity:</b> O(log(N))+O(M) with N being the number of
     * elements in the sorted set and M the number of elements removed by the
     * operation
     */
    public Long zremrangeByRank(final String key, final int start, final int end) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.zremrangeByRank(key, start, end);
                return client.getIntegerReply();
            }
        });
    }

    /**
     * Remove all the elements in the sorted set at key with a score between min
     * and max (including elements with score equal to min or max).
     * <p/>
     * <b>Time complexity:</b>
     * <p/>
     * O(log(N))+O(M) with N being the number of elements in the sorted set and
     * M the number of elements removed by the operation
     *
     * @param key   the key
     * @param start
     * @param end
     * @return Integer reply, specifically the number of elements removed.
     */
    public Long zremrangeByScore(final String key, final Number start,
                                 final Number end) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.zremrangeByScore(key, start, end);
                return client.getIntegerReply();
            }
        });
    }

    /**
     * Creates a union or intersection of N sorted sets given by keys k1 through
     * kN, and stores it at dstkey. It is mandatory to provide the number of
     * input keys N, before passing the input keys and the other (optional)
     * arguments.
     * <p/>
     * As the terms imply, the {@link #zinterstore(String, String...)
     * ZINTERSTORE} command requires an element to be present in each of the
     * given inputs to be inserted in the result. The
     * {@link #zunionstore(String, String...) ZUNIONSTORE} command inserts all
     * elements across all inputs.
     * <p/>
     * Using the WEIGHTS option, it is possible to add weight to each input
     * sorted set. This means that the score of each element in the sorted set
     * is first multiplied by this weight before being passed to the
     * aggregation. When this option is not given, all weights default to 1.
     * <p/>
     * With the AGGREGATE option, it's possible to specify how the results of
     * the union or intersection are aggregated. This option defaults to SUM,
     * where the score of an element is summed across the inputs where it
     * exists. When this option is set to be either MIN or MAX, the resulting
     * set will contain the minimum or maximum score of an element across the
     * inputs where it exists.
     * <p/>
     * <b>Time complexity:</b> O(N) + O(M log(M)) with N being the sum of the
     * sizes of the input sorted sets, and M being the number of elements in the
     * resulting sorted set
     *
     * @param dstkey
     * @param sets
     * @return Integer reply, specifically the number of elements in the sorted
     *         set at dstkey
     * @see #zunionstore(String, ZParams, String...)
     */
    public Long zunionstore(final String dstkey, final String... sets) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.zunionstore(dstkey, sets);
                return client.getIntegerReply();
            }
        });
    }

    /**
     * Creates a union or intersection of N sorted sets given by keys k1 through
     * kN, and stores it at dstkey. It is mandatory to provide the number of
     * input keys N, before passing the input keys and the other (optional)
     * arguments.
     * <p/>
     * As the terms imply, the {@link #zinterstore(String, String...)
     * ZINTERSTORE} command requires an element to be present in each of the
     * given inputs to be inserted in the result. The
     * {@link #zunionstore(String, String...) ZUNIONSTORE} command inserts all
     * elements across all inputs.
     * <p/>
     * Using the WEIGHTS option, it is possible to add weight to each input
     * sorted set. This means that the score of each element in the sorted set
     * is first multiplied by this weight before being passed to the
     * aggregation. When this option is not given, all weights default to 1.
     * <p/>
     * With the AGGREGATE option, it's possible to specify how the results of
     * the union or intersection are aggregated. This option defaults to SUM,
     * where the score of an element is summed across the inputs where it
     * exists. When this option is set to be either MIN or MAX, the resulting
     * set will contain the minimum or maximum score of an element across the
     * inputs where it exists.
     * <p/>
     * <b>Time complexity:</b> O(N) + O(M log(M)) with N being the sum of the
     * sizes of the input sorted sets, and M being the number of elements in the
     * resulting sorted set
     *
     * @param dstkey
     * @param sets
     * @param params
     * @return Integer reply, specifically the number of elements in the sorted
     *         set at dstkey
     * @see #zunionstore(String, String...)
     */
    public Long zunionstore(final String dstkey, final ZParams params, final String... sets) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.zunionstore(dstkey, params, sets);
                return client.getIntegerReply();
            }
        });
    }

    /**
     * Creates a union or intersection of N sorted sets given by keys k1 through
     * kN, and stores it at dstkey. It is mandatory to provide the number of
     * input keys N, before passing the input keys and the other (optional)
     * arguments.
     * <p/>
     * As the terms imply, the {@link #zinterstore(String, String...)
     * ZINTERSTORE} command requires an element to be present in each of the
     * given inputs to be inserted in the result. The
     * {@link #zunionstore(String, String...) ZUNIONSTORE} command inserts all
     * elements across all inputs.
     * <p/>
     * Using the WEIGHTS option, it is possible to add weight to each input
     * sorted set. This means that the score of each element in the sorted set
     * is first multiplied by this weight before being passed to the
     * aggregation. When this option is not given, all weights default to 1.
     * <p/>
     * With the AGGREGATE option, it's possible to specify how the results of
     * the union or intersection are aggregated. This option defaults to SUM,
     * where the score of an element is summed across the inputs where it
     * exists. When this option is set to be either MIN or MAX, the resulting
     * set will contain the minimum or maximum score of an element across the
     * inputs where it exists.
     * <p/>
     * <b>Time complexity:</b> O(N) + O(M log(M)) with N being the sum of the
     * sizes of the input sorted sets, and M being the number of elements in the
     * resulting sorted set
     *
     * @param dstkey
     * @param sets
     * @return Integer reply, specifically the number of elements in the sorted
     *         set at dstkey
     * @see #zunionstore(String, String...)
     * @see #zunionstore(String, ZParams, String...)
     */
    public Long zinterstore(final String dstkey, final String... sets) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.zinterstore(dstkey, sets);
                return client.getIntegerReply();
            }
        });
    }

    /**
     * Creates a union or intersection of N sorted sets given by keys k1 through
     * kN, and stores it at dstkey. It is mandatory to provide the number of
     * input keys N, before passing the input keys and the other (optional)
     * arguments.
     * <p/>
     * As the terms imply, the {@link #zinterstore(String, String...)
     * ZINTERSTORE} command requires an element to be present in each of the
     * given inputs to be inserted in the result. The
     * {@link #zunionstore(String, String...) ZUNIONSTORE} command inserts all
     * elements across all inputs.
     * <p/>
     * Using the WEIGHTS option, it is possible to add weight to each input
     * sorted set. This means that the score of each element in the sorted set
     * is first multiplied by this weight before being passed to the
     * aggregation. When this option is not given, all weights default to 1.
     * <p/>
     * With the AGGREGATE option, it's possible to specify how the results of
     * the union or intersection are aggregated. This option defaults to SUM,
     * where the score of an element is summed across the inputs where it
     * exists. When this option is set to be either MIN or MAX, the resulting
     * set will contain the minimum or maximum score of an element across the
     * inputs where it exists.
     * <p/>
     * <b>Time complexity:</b> O(N) + O(M log(M)) with N being the sum of the
     * sizes of the input sorted sets, and M being the number of elements in the
     * resulting sorted set
     *
     * @param dstkey
     * @param sets
     * @param params
     * @return Integer reply, specifically the number of elements in the sorted
     *         set at dstkey
     */
    public Long zinterstore(final String dstkey, final ZParams params, final String... sets) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.zinterstore(dstkey, params, sets);
                return client.getIntegerReply();
            }
        });
    }

    /**
     * Synchronously save the DB on disk.
     * <p/>
     * Save the whole dataset on disk (this means that all the databases are
     * saved, as well as keys with an EXPIRE set (the expire is preserved). The
     * server hangs while the saving is not completed, no connection is served
     * in the meanwhile. An OK code is returned when the DB was fully stored in
     * disk.
     * <p/>
     * The background variant of this command is {@link #bgsave() BGSAVE} that
     * is able to perform the saving in the background while the server
     * continues serving other clients.
     * <p/>
     *
     * @return Status code reply
     */
    public String save() {
        return execute(new RedisCommand<String>() {
            public String execute(Client client) {
                client.save();
                return client.getStatusCodeReply();
            }
        });
    }

    /**
     * Asynchronously save the DB on disk.
     * <p/>
     * Save the DB in background. The OK code is immediately returned. Redis
     * forks, the parent continues to server the clients, the child saves the DB
     * on disk then exit. A client my be able to check if the operation
     * succeeded using the LASTSAVE command.
     *
     * @return Status code reply
     */
    public String bgsave() {
        return execute(new RedisCommand<String>() {
            public String execute(Client client) {
                client.bgsave();
                return client.getStatusCodeReply();
            }
        });
    }

    /**
     * Rewrite the append only file in background when it gets too big. Please
     * for detailed information about the Redis Append Only File check the <a
     * href="http://code.google.com/p/redis/wiki/AppendOnlyFileHowto">Append
     * Only File Howto</a>.
     * <p/>
     * BGREWRITEAOF rewrites the Append Only File in background when it gets too
     * big. The Redis Append Only File is a Journal, so every operation
     * modifying the dataset is logged in the Append Only File (and replayed at
     * startup). This means that the Append Only File always grows. In order to
     * rebuild its content the BGREWRITEAOF creates a new version of the append
     * only file starting directly form the dataset in memory in order to
     * guarantee the generation of the minimal number of commands needed to
     * rebuild the database.
     * <p/>
     *
     * @return Status code reply
     */
    public String bgrewriteaof() {
        return execute(new RedisCommand<String>() {
            public String execute(Client client) {
                client.bgrewriteaof();
                return client.getStatusCodeReply();
            }
        });
    }

    /**
     * Return the UNIX time stamp of the last successfully saving of the dataset
     * on disk.
     * <p/>
     * Return the UNIX TIME of the last DB save executed with success. A client
     * may check if a {@link #bgsave() BGSAVE} command succeeded reading the
     * LASTSAVE value, then issuing a BGSAVE command and checking at regular
     * intervals every N seconds if LASTSAVE changed.
     *
     * @return Integer reply, specifically an UNIX time stamp.
     */
    public Long lastsave() {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.lastsave();
                return client.getIntegerReply();
            }
        });
    }

    /**
     * Synchronously save the DB on disk, then shutdown the server.
     * <p/>
     * Stop all the clients, save the DB, then quit the server. This commands
     * makes sure that the DB is switched off without the lost of any data. This
     * is not guaranteed if the client uses simply {@link #save() SAVE} and then
     * {@link #quit() QUIT} because other clients may alter the DB data between
     * the two commands.
     *
     * @return Status code reply on error. On success nothing is returned since
     *         the server quits and the connection is closed.
     */
    public String shutdown() {
        return execute(new RedisCommand<String>() {
            public String execute(Client client) {
                client.shutdown();
                String status = null;
                try {
                    status = client.getStatusCodeReply();
                } catch (RedisException ex) {
                    status = null;
                }
                return status;
            }
        });
    }

    /**
     * Provide information and statistics about the server.
     * <p/>
     * The info command returns different information and statistics about the
     * server in an format that's simple to parse by computers and easy to read
     * by humans.
     * <p/>
     * <b>Format of the returned String:</b>
     * <p/>
     * All the fields are in the form field:value
     * <p/>
     * <pre>
     * edis_version:0.07
     * connected_clients:1
     * connected_slaves:0
     * used_memory:3187
     * changes_since_last_save:0
     * last_save_time:1237655729
     * total_connections_received:1
     * total_commands_processed:1
     * uptime_in_seconds:25
     * uptime_in_days:0
     * </pre>
     * <p/>
     * <b>Notes</b>
     * <p/>
     * used_memory is returned in bytes, and is the total number of bytes
     * allocated by the program using malloc.
     * <p/>
     * uptime_in_days is redundant since the uptime in seconds contains already
     * the full uptime information, this field is only mainly present for
     * humans.
     * <p/>
     * changes_since_last_save does not refer to the number of key changes, but
     * to the number of operations that produced some kind of change in the
     * dataset.
     * <p/>
     *
     * @return Bulk reply
     */
    public String info() {
        return execute(new RedisCommand<String>() {
            public String execute(Client client) {
                client.info();
                return client.getBulkReply();
            }
        });
    }

    /**
     * Dump all the received requests in real time.
     * <p/>
     * MONITOR is a debugging command that outputs the whole sequence of
     * commands received by the Redis server. is very handy in order to
     * understand what is happening into the database. This command is used
     * directly via telnet.
     *
     * @param redisMonitor
     */
    public void monitor(final RedisMonitor redisMonitor) {
        execute(new RedisCommand<String>() {
            public String execute(Client client) {
                client.monitor();
                do {
                    String command = client.getBulkReply();
                    redisMonitor.onCommand(command);
                } while (client.isConnected());
                return null;
            }
        });
    }

    /**
     * Change the replication settings.
     * <p/>
     * The SLAVEOF command can change the replication settings of a slave on the
     * fly. If a Redis server is arleady acting as slave, the command SLAVEOF NO
     * ONE will turn off the replicaiton turning the Redis server into a MASTER.
     * In the proper form SLAVEOF hostname port will make the server a slave of
     * the specific server listening at the specified hostname and port.
     * <p/>
     * If a server is already a slave of some master, SLAVEOF hostname port will
     * stop the replication against the old server and start the
     * synchrnonization against the new one discarding the old dataset.
     * <p/>
     * The form SLAVEOF no one will stop replication turning the server into a
     * MASTER but will not discard the replication. So if the old master stop
     * working it is possible to turn the slave into a master and set the
     * application to use the new master in read/write. Later when the other
     * Redis server will be fixed it can be configured in order to work as
     * slave.
     * <p/>
     *
     * @param host
     * @param port
     * @return Status code reply
     */
    public String slaveof(final String host, final int port) {
        return execute(new RedisCommand<String>() {
            public String execute(Client client) {
                client.slaveof(host, port);
                return client.getStatusCodeReply();
            }
        });
    }

    public String slaveofNoOne() {
        return execute(new RedisCommand<String>() {
            public String execute(Client client) {
                client.slaveofNoOne();
                return client.getStatusCodeReply();
            }
        });
    }

    /**
     * Retrieve the configuration of a running Redis server. Not all the
     * configuration parameters are supported.
     * <p/>
     * CONFIG GET returns the current configuration parameters. This sub command
     * only accepts a single argument, that is glob style pattern. All the
     * configuration parameters matching this parameter are reported as a list
     * of key-value pairs.
     * <p/>
     * <b>Example:</b>
     * <p/>
     * <pre>
     * $ redis-cli config get '*'
     * 1. "dbfilename"
     * 2. "dump.rdb"
     * 3. "requirepass"
     * 4. (nil)
     * 5. "masterauth"
     * 6. (nil)
     * 7. "maxmemory"
     * 8. "0\n"
     * 9. "appendfsync"
     * 10. "everysec"
     * 11. "save"
     * 12. "3600 1 300 100 60 10000"
     * <p/>
     * $ redis-cli config get 'm*'
     * 1. "masterauth"
     * 2. (nil)
     * 3. "maxmemory"
     * 4. "0\n"
     * </pre>
     *
     * @param pattern
     * @return Bulk reply.
     */
    public List<String> configGet(final String pattern) {
        return execute(new RedisCommand<List<String>>() {
            public List<String> execute(Client client) {
                client.configGet(pattern);
                return client.getMultiBulkReply();
            }
        });
    }

    /**
     * Alter the configuration of a running Redis server. Not all the
     * configuration parameters are supported.
     * <p/>
     * The list of configuration parameters supported by CONFIG SET can be
     * obtained issuing a {@link #configGet(String) CONFIG GET *} command.
     * <p/>
     * The configuration set using CONFIG SET is immediately loaded by the Redis
     * server that will start acting as specified starting from the next
     * command.
     * <p/>
     * <p/>
     * <b>Parameters value format</b>
     * <p/>
     * The value of the configuration parameter is the same as the one of the
     * same parameter in the Redis configuration file, with the following
     * exceptions:
     * <p/>
     * <ul>
     * <li>The save paramter is a list of space-separated integers. Every pair
     * of integers specify the time and number of changes limit to trigger a
     * save. For instance the command CONFIG SET save "3600 10 60 10000" will
     * configure the server to issue a background saving of the RDB file every
     * 3600 seconds if there are at least 10 changes in the dataset, and every
     * 60 seconds if there are at least 10000 changes. To completely disable
     * automatic snapshots just set the parameter as an empty string.
     * <li>All the integer parameters representing memory are returned and
     * accepted only using bytes as unit.
     * </ul>
     *
     * @param parameter
     * @param value     the value
     * @return Status code reply
     */
    public String configSet(final String parameter, final String value) {
        return execute(new RedisCommand<String>() {
            public String execute(Client client) {
                client.configSet(parameter, value);
                return client.getStatusCodeReply();
            }
        });
    }

    public Long strlen(final String key) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.strlen(key);
                return client.getIntegerReply();
            }
        });
    }

    public void sync() {
        execute(new RedisCommand<String>() {
            public String execute(Client client) {
                client.sync();
                return null;
            }
        });
    }

    public Long lpushx(final String key, final String string) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.lpushx(key, string);
                return client.getIntegerReply();
            }
        });
    }

    /**
     * Undo a {@link #expire(String, int) expire} at turning the expire key into
     * a normal key.
     * <p/>
     * Time complexity: O(1)
     *
     * @param key the key
     * @return Integer reply, specifically: 1: the key is now persist. 0: the
     *         key is not persist (only happens when key not set).
     */
    public Long persist(final String key) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.persist(key);
                return client.getIntegerReply();
            }
        });
    }

    public Long rpushx(final String key, final String string) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.rpushx(key, string);
                return client.getIntegerReply();
            }
        });
    }

    public String echo(final String string) {
        return execute(new RedisCommand<String>() {
            public String execute(Client client) {
                client.echo(string);
                return client.getBulkReply();
            }
        });
    }

    public Long linsert(final String key, final LIST_POSITION where,
                        final String pivot, final String value) {
        return execute(new RedisCommand<Long>() {
            public Long execute(Client client) {
                client.linsert(key, where, pivot, value);
                return client.getIntegerReply();
            }
        });
    }

    public String debug(final DebugParams params) {
        return execute(new RedisCommand<String>() {
            public String execute(Client client) {
                client.debug(params);
                return client.getStatusCodeReply();
            }
        });
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RedisNode redisNode = (RedisNode) o;

        if (weight != redisNode.weight) return false;
        if (!dataSource.equals(redisNode.dataSource)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = dataSource.hashCode();
        result = 31 * result + weight;
        return result;
    }
}