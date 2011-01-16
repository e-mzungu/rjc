# RJC
RJC is a [Redis](http://redis.io/) Java client.

It provides connection pooling in Apache DBCP style, sharding, pipelines, transactions and messages.

It's aimed to work in multi threading environments.

RJC is fully compatible with Redis 2.0.x.

See code examples in the project [wiki page](https://github.com/e-mzungu/rjc/wiki/Code-examples).

# How to use it with Maven
Check out master branch and doIt:
    mvn install
or [download](https://github.com/e-mzungu/rjc/downloads) jar file and install it into you repository.

Then include maven dependency to you project
        <dependency>
            <groupId>io.redis</groupId>
            <artifactId>rjc</artifactId>
            <version>0.5.4</version>
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








