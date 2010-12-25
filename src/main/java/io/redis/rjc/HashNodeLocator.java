package io.redis.rjc;

import io.redis.rjc.util.HashAlgorithm;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HashNodeLocator implements NodeLocator {

    public static final int DEFAULT_WEIGHT = 1;
    private TreeMap<Long, RedisNode> shardedNodes;
    private List<RedisNode> nodes;
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

    public HashNodeLocator(List<RedisNode> nodes) {
        this(nodes, HashAlgorithm.MURMUR_HASH); // MD5 is really not good as we works
        // with 64-bits not 128
    }

    public HashNodeLocator(List<RedisNode> nodes, HashAlgorithm algo) {
        this.nodes = new ArrayList<RedisNode>(nodes);
        this.algorithm = algo;
        initialize();
    }

    public HashNodeLocator(List<RedisNode> nodes, Pattern tagPattern) {
        this(nodes, HashAlgorithm.MURMUR_HASH, tagPattern); // MD5 is really not good
        // as we works with
        // 64-bits not 128
    }

    public HashNodeLocator(List<RedisNode> nodes, HashAlgorithm algo, Pattern tagPattern) {
        this.nodes = new ArrayList<RedisNode>(nodes);
        this.algorithm = algo;
        this.tagPattern = tagPattern;
        initialize();
    }

    public List<RedisNode> getNodes() {
        return Collections.unmodifiableList(nodes);
    }

    public void setNodes(List<RedisNode> nodes) {
        this.nodes = new ArrayList<RedisNode>(nodes);
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
        shardedNodes = new TreeMap<Long, RedisNode>();

        for (int i = 0; i != nodes.size(); ++i) {
            final RedisNode node = nodes.get(i);
            for (int n = 0; n < 160 * node.getWeight(); n++) {
                this.shardedNodes.put(this.algorithm.hash(node.toString() + n), node);
            }
        }
    }

    public SingleRedisOperations getNode(String key) {
        key = getKeyTag(key);
        SortedMap<Long, RedisNode> tail = shardedNodes.tailMap(algorithm.hash(key));
        if (tail.size() == 0) {
            return shardedNodes.get(shardedNodes.firstKey());
        }
        return tail.get(tail.firstKey());
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

    public Collection<? extends SingleRedisOperations> getShardedNodes() {
        return Collections.unmodifiableCollection(shardedNodes.values());
    }
}