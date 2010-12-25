package io.redis.rjc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static io.redis.rjc.Protocol.Keyword.AGGREGATE;
import static io.redis.rjc.Protocol.Keyword.WEIGHTS;

public class ZParams {
    public enum Aggregate {
        SUM, MIN, MAX
    }

    private List<String> params = new ArrayList<String>();

    public ZParams weights(final int... weights) {
        params.add(WEIGHTS.str);
        for (final int weight : weights) {
            params.add(String.valueOf(weight));
        }

        return this;
    }

    public Collection<String> getParams() {
        return Collections.unmodifiableCollection(params);
    }

    public ZParams aggregate(final Aggregate aggregate) {
        params.add(AGGREGATE.str);
        params.add(aggregate.name());
        return this;
    }
}
