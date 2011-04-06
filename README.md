# RJC
RJC is a [Redis](http://redis.io/) Java Client.

It provides connection pooling in Apache DBCP style, sharding, pipelines, transactions and messages.

It's aimed to work in multi threading environments.

RJC is fully compatible with Redis 2.x.

OSGi ready (thanks [iocanel](https://github.com/iocanel)).

See code examples in the project [wiki page](https://github.com/e-mzungu/rjc/wiki/Code-examples).

# How to use it with Maven
Include maven dependency to you project

        <dependency>
            <groupId>org.idevlab</groupId>
            <artifactId>rjc</artifactId>
            <version>0.6.4</version>
        </dependency>

# Quick start

Install RJC as described above.

Run Redis.

Perform:

        DataSource dataSource = new SimpleDataSource("localhost");
        SingleRedisOperations redis = new RedisNode(dataSource);
        redis.set("foo", "hello");
        String value = redis.get("foo");

See more examples [here](https://github.com/e-mzungu/rjc/wiki/Code-examples).