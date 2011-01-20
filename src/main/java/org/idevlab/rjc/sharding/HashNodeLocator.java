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

package org.idevlab.rjc.sharding;

import org.idevlab.rjc.RedisException;
import org.idevlab.rjc.util.HashAlgorithm;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HashNodeLocator<T> implements NodeLocator<T> {

    private TreeMap<Long, Shard<T>> shardedNodes;
    private Collection<? extends Shard<T>> shards;
    private Collection<T> nodes;
    private HashAlgorithm algorithm = HashAlgorithm.MURMUR_HASH;

    /**
     * The default pattern used for extracting a key tag. The pattern must have
     * a group (between parenthesis), which delimits the tag to be hashed. A
     * null pattern avoids applying the regular expression for each lookup,
     * improving performance a little bit is key tags aren't being used.
     */
    private Pattern tagPattern = null;
    // the tag is anything between {}
    public static final Pattern DEFAULT_KEY_TAG_PATTERN = Pattern.compile("\\{(.+?)\\}");

    public HashNodeLocator() {
    }

    public HashNodeLocator(Collection<? extends Shard<T>> shards) {
        this(shards, HashAlgorithm.MURMUR_HASH); // MD5 is really not good as we works
        // with 64-bits not 128
    }

    public HashNodeLocator(Collection<? extends Shard<T>> shards, HashAlgorithm algo) {
        this.shards = new ArrayList<Shard<T>>(shards);
        this.algorithm = algo;
        initialize();
    }

    public HashNodeLocator(Collection<? extends Shard<T>> shards, Pattern tagPattern) {
        this(shards, HashAlgorithm.MURMUR_HASH, tagPattern); // MD5 is really not good
        // as we works with
        // 64-bits not 128
    }

    public HashNodeLocator(Collection<? extends Shard<T>> shards, HashAlgorithm algo, Pattern tagPattern) {
        this.shards = new ArrayList<Shard<T>>(shards);
        this.algorithm = algo;
        this.tagPattern = tagPattern;
        initialize();
    }

    public void setShards(Collection<? extends Shard<T>> shards) {
        this.shards = new ArrayList<Shard<T>>(shards);
        initialize();
    }

    public HashAlgorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(HashAlgorithm algorithm) {
        this.algorithm = algorithm;
        initialize();
    }

    public Pattern getTagPattern() {
        return tagPattern;
    }

    public void setTagPattern(Pattern tagPattern) {
        this.tagPattern = tagPattern;
    }

    private void initialize() {
        if (shards == null || algorithm == null) {
            shardedNodes = null;
            return;
        }

        Set<String> nodesId = new HashSet<String>(shards.size());
        for (Shard node : shards) {
            if(node.getShardId() == null) {
                throw new RedisException("Sharded node must have unique shard id and must not be null");
            }
            nodesId.add(node.getShardId());
        }

        if(shards.size() != nodesId.size()) {
            throw new RedisException("Sharded node must have unique shard id");
        }

        nodes = new ArrayList<T>(shards.size());
        for (Shard<T> shard : shards) {
            nodes.add(shard.getNode());
        }

        shardedNodes = new TreeMap<Long, Shard<T>>();

        for (Shard<T> node : shards) {
            for (int n = 0; n < 160 * node.getWeight(); n++) {
                this.shardedNodes.put(this.algorithm.hash(node.getShardId() + n), node);
            }
        }
    }

    public T getNode(String key) {
        key = getKeyTag(key);
        SortedMap<Long, Shard<T>> tail = shardedNodes.tailMap(algorithm.hash(key));
        if (tail.size() == 0) {
            return shardedNodes.get(shardedNodes.firstKey()).getNode();
        }
        return tail.get(tail.firstKey()).getNode();
    }


    /**
     * A key tag is a special pattern inside a key that, if preset, is the only
     * part of the key hashed in order to select the server for this key.
     *
     * @param key key
     * @return The tag if it exists, or the original key
     * @see <a href="http://redis.io/topics/faq">I'm using some form of key hashing for partitioning, but what about SORT BY?</a>
     */
    private String getKeyTag(String key) {
        if (tagPattern != null) {
            Matcher m = tagPattern.matcher(key);
            if (m.find())
                return m.group(1);
        }
        return key;
    }

    public Collection<? extends T> getNodes() {
        return Collections.unmodifiableCollection(nodes);
    }
}