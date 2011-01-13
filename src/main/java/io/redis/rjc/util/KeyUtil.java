package io.redis.rjc.util;

import java.io.UnsupportedEncodingException;

/**
 * @author Evgeny Dolgov
 */
public class KeyUtil {

    private KeyUtil() {
    }

    /**
	 * Get the bytes for a key.
	 *
	 * @param k the key
	 * @return the bytes
	 */
	public static byte[] getKeyBytes(String k) {
		try {
			return k.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

    /**
     * Creates a key from the given blocks using the given separator.
     * @param separator the separator charachter to use
     * @param blocks the key's blocks to concatenate
     * @return the resulting key
     */
    public static String toKey(char separator, Object... blocks) {
        if (blocks.length == 0) {
            throw new IllegalArgumentException("Empty key");
        }
        // concatenate blocks
        StringBuilder result = new StringBuilder();
        for (Object block : blocks) {
            result.append(block);
            result.append(separator);
        }
        // remove trailing separator
        result.deleteCharAt(result.length() - 1);
        return result.toString();
    }

    /**
     * Creates a key from the given blocks using the default separator character (':').
     * @param blocks the key's blocks to concatenate
     * @return the resulting key
     */
    public static String toKey(Object... blocks) {
        return toKey(':', blocks);
    }

}
