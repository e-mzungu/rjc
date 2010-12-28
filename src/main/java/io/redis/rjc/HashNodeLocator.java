package io.redis.rjc;

import io.redis.rjc.util.HashAlgorithm;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HashNodeLocator<T> implements NodeLocator<T> {

    public static final int DEFAULT_WEIGHT = 1;
    private TreeMap<Long, Shard> shardedNodes;
    private Collection<? extends Shard> nodes;
    private HashAlgorithm algorithm;

    /**
     * The default pattern used for extracting a key tag. The pattern must have
     * a group (between parenthesis), which delimits the tag to be hashed. A
     * null pattern avoids applying the regular expression for each lookup,
     * improving performance a little bit is key tags aren't being used.
     */
    private Pattern tagPattern = null;
    // the tag is anything between {}
    public static final Pattern DEFAULT_KEY_TAG_PATTERN = Pattern
            .compile("\\{(.+?)\\}");


    public HashNodeLocator() {
    }

    public HashNodeLocator(Collection<? extends Shard> nodes) {
        this(nodes, HashAlgorithm.MURMUR_HASH); // MD5 is really not good as we works
        // with 64-bits not 128
    }

    public HashNodeLocator(Collection<? extends Shard> nodes, HashAlgorithm algo) {
        this.nodes = new ArrayList<Shard>(nodes);
        this.algorithm = algo;
        initialize();
    }

    public HashNodeLocator(Collection<? extends Shard> nodes, Pattern tagPattern) {
        this(nodes, HashAlgorithm.MURMUR_HASH, tagPattern); // MD5 is really not good
        // as we works with
        // 64-bits not 128
    }

    public HashNodeLocator(Collection<? extends Shard> nodes, HashAlgorithm algo, Pattern tagPattern) {
        this.nodes = new ArrayList<Shard>(nodes);
        this.algorithm = algo;
        this.tagPattern = tagPattern;
        initialize();
    }

    public void setNodes(Collection<? extends Shard> nodes) {
        this.nodes = new ArrayList<Shard>(nodes);
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
        if (nodes == null || algorithm == null) {
            shardedNodes = null;
            return;
        }
        shardedNodes = new TreeMap<Long, Shard>();

        for (Shard node : nodes) {
            for (int n = 0; n < 160 * node.getWeight(); n++) {
                this.shardedNodes.put(this.algorithm.hash(node.toString() + n), node);
            }
        }
    }

    @SuppressWarnings({"unchecked"})
    public T getNode(String key) {
        key = getKeyTag(key);
        SortedMap<Long, Shard> tail = shardedNodes.tailMap(algorithm.hash(key));
        if (tail.size() == 0) {
            return (T) shardedNodes.get(shardedNodes.firstKey());
        }
        return (T) tail.get(tail.firstKey());
    }


    /**
     * A key tag is a special pattern inside a key that, if preset, is the only
     * part of the key hashed in order to select the server for this key.
     *
     * @param key key
     * @return The tag if it exists, or the original key
     * @see http://code.google.com/p/redis/wiki/FAQ#I'm_using_some_form_of_key_hashing_for_partitioning,_but_wh
     */
    private String getKeyTag(String key) {
        if (tagPattern != null) {
            Matcher m = tagPattern.matcher(key);
            if (m.find())
                return m.group(1);
        }
        return key;
    }

    @SuppressWarnings({"unchecked"})
    public Collection<? extends T> getNodes() {
        return (Collection<? extends T>) Collections.unmodifiableCollection(shardedNodes.values());
    }
}