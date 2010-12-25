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
}
