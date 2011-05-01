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

package org.idevlab.rjc;

import org.idevlab.rjc.protocol.RedisKeyword;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ZParams {
    public enum Aggregate {
        SUM, MIN, MAX
    }

    private List<String> params = new ArrayList<String>();

    public ZParams weights(final int... weights) {
        params.add(RedisKeyword.WEIGHTS.str);
        for (final int weight : weights) {
            params.add(String.valueOf(weight));
        }

        return this;
    }

    public Collection<String> getParams() {
        return Collections.unmodifiableCollection(params);
    }

    public ZParams aggregate(final Aggregate aggregate) {
        params.add(RedisKeyword.AGGREGATE.str);
        params.add(aggregate.name());
        return this;
    }
}
