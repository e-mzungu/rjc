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

/**
 * Builder Class for {@link RedisNode#sort(String, SortingParams) SORT} Parameters.
 * 
 */
public class SortingParams {
    private List<String> params = new ArrayList<String>();

    /**
     * Sort by weight in keys.
     * <p>
     * Takes a pattern that is used in order to generate the key names of the
     * weights used for sorting. Weight key names are obtained substituting the
     * first occurrence of * with the actual value of the elements on the list.
     * <p>
     * The pattern for a normal key/value pair is "keyname*" and for a value in
     * a hash "keyname*->fieldname".
     * 
     * @param pattern
     * @return the SortingParams Object
     */
    public SortingParams by(final String pattern) {
        params.add(RedisKeyword.BY.str);
        params.add(pattern);
        return this;
    }


    /**
     * No sorting.
     * <p>
     * This is useful if you want to retrieve a external key (using
     * {@link #get(String...) GET}) but you don't want the sorting overhead.
     * 
     * @return the SortingParams Object
     */
    public SortingParams nosort() {
        params.add(RedisKeyword.BY.str);
        params.add(RedisKeyword.NOSORT.str);
        return this;
    }

    public Collection<String> getParams() {
        return Collections.unmodifiableCollection(params);
    }

    /**
     * Get the Sorting in Descending Order.
     * 
     * @return the sortingParams Object
     */
    public SortingParams desc() {
        params.add(RedisKeyword.DESC.str);
        return this;
    }

    /**
     * Get the Sorting in Ascending Order. This is the default order.
     * 
     * @return the SortingParams Object
     */
    public SortingParams asc() {
        params.add(RedisKeyword.ASC.str);
        return this;
    }

    /**
     * Limit the Numbers of returned Elements.
     * 
     * @param start
     *            is zero based
     * @param count
     * @return the SortingParams Object
     */
    public SortingParams limit(final int start, final int count) {
        params.add(RedisKeyword.LIMIT.str);
        params.add(String.valueOf(start));
        params.add(String.valueOf(count));
        return this;
    }

    /**
     * Sort lexicographicaly. Note that Redis is utf-8 aware assuming you set
     * the right value for the LC_COLLATE environment variable.
     * 
     * @return the SortingParams Object
     */
    public SortingParams alpha() {
        params.add(RedisKeyword.ALPHA.str);
        return this;
    }

    /**
     * Retrieving external keys from the result of the search.
     * <p>
     * Takes a pattern that is used in order to generate the key names of the
     * result of sorting. The key names are obtained substituting the first
     * occurrence of * with the actual value of the elements on the list.
     * <p>
     * The pattern for a normal key/value pair is "keyname*" and for a value in
     * a hash "keyname*->fieldname".
     * <p>
     * To get the list itself use the char # as pattern.
     * 
     * @param patterns
     * @return the SortingParams Object
     */
    public SortingParams get(String... patterns) {
        for (final String pattern : patterns) {
            params.add(RedisKeyword.GET.str);
            params.add(pattern);
        }
        return this;
    }
}