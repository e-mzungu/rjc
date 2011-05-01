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

package org.idevlab.rjc.util;

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
     *
     * @param separator the separator character to use
     * @param blocks    the key's blocks to concatenate
     * @return the resulting key
     */
    private static String toKey(char separator, Object... blocks) {
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
     *
     * @param blocks the key's blocks to concatenate
     * @return the resulting key
     */
    public static String toKey(Object... blocks) {
        return toKey(':', blocks);
    }

    public static String[] joinParams(String dstkey, String[] keys) {
        final String[] params = new String[keys.length + 1];
        params[0] = dstkey;
        System.arraycopy(keys, 0, params, 1, keys.length);
        return params;
    }
}
