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

package org.idevlab.rjc.protocol;

import org.idevlab.rjc.RedisException;
import org.idevlab.rjc.util.SafeEncoder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class Protocol {

    public static final int DEFAULT_PORT = 6379;
    public static final int DEFAULT_TIMEOUT = 2000;

    public static final String CHARSET = "UTF-8";

    public static final byte DOLLAR_BYTE = '$';
    public static final byte ASTERISK_BYTE = '*';
    public static final byte PLUS_BYTE = '+';
    public static final byte MINUS_BYTE = '-';
    public static final byte COLON_BYTE = ':';

    public void sendCommand(final RedisOutputStream os, final RedisCommand command,
                            final byte[]... args) {
        sendCommand(os, command.raw, args);
    }

    private void sendCommand(final RedisOutputStream os, final byte[] command,
                             final byte[]... args) {
        try {
            os.write(ASTERISK_BYTE);
            os.writeIntCrLf(args.length + 1);
            os.write(DOLLAR_BYTE);
            os.writeIntCrLf(command.length);
            os.write(command);
            os.writeCrLf();

            for (final byte[] arg : args) {
                os.write(DOLLAR_BYTE);
                os.writeIntCrLf(arg.length);
                os.write(arg);
                os.writeCrLf();
            }
            os.flush();
        } catch (IOException e) {
            throw new RedisException(e);
        }
    }

    private void processError(final RedisInputStream is) {
        String message = is.readLine();
        throw new RedisException(message);
    }

    private Object process(final RedisInputStream is, boolean stringsOnly) {
        try {
            byte b = is.readByte();
            if (b == MINUS_BYTE) {
                processError(is);
            } else if (b == ASTERISK_BYTE) {
                return processMultiBulkReply(is, stringsOnly);
            } else if (b == COLON_BYTE) {
                return processInteger(is);
            } else if (b == DOLLAR_BYTE) {
                if (stringsOnly) {
                    return processBulkReply(is);
                }
                return processBinaryBulkReply(is);
            } else if (b == PLUS_BYTE) {
                return processStatusCodeReply(is);
            } else {
                throw new RedisException("Unknown reply: " + (char) b);
            }
        } catch (IOException e) {
            throw new RedisException(e);
        }
        return null;
    }

    private String processStatusCodeReply(final RedisInputStream is) {
        return is.readLine();
    }

    private String processBulkReply(final RedisInputStream is) {
        byte[] result = processBinaryBulkReply(is);
        if (result == null) {
            return null;
        }
        return SafeEncoder.encode(result);
    }

    private byte[] processBinaryBulkReply(final RedisInputStream is) {
        int len = Integer.parseInt(is.readLine());
        if (len == -1) {
            return null;
        }
        byte[] read = new byte[len];
        int offset = 0;
        try {
            while (offset < len) {
                offset += is.read(read, offset, (len - offset));
            }
            // read 2 more bytes for the command delimiter
            is.readByte();
            is.readByte();
        } catch (IOException e) {
            throw new RedisException(e);
        }

        return read;
    }

    private Long processInteger(final RedisInputStream is) {
        String num = is.readLine();
        return Long.valueOf(num);
    }

    private List<Object> processMultiBulkReply(final RedisInputStream is, boolean stringsOnly) {
        int num = Integer.parseInt(is.readLine());
        if (num == -1) {
            return null;
        }
        List<Object> ret = new ArrayList<Object>(num);
        for (int i = 0; i < num; i++) {
            ret.add(process(is, stringsOnly));
        }
        return ret;
    }

    public Object read(final RedisInputStream is) {
        return read(is, true);
    }

    public Object read(final RedisInputStream is, boolean stringsOnly) {
        return process(is, stringsOnly);
    }

    public static byte[] toByteArray(final int value) {
        return SafeEncoder.encode(String.valueOf(value));
    }

    public static byte[] toByteArray(final long value) {
        return SafeEncoder.encode(String.valueOf(value));
    }

    public static byte[] toByteArray(final double value) {
        return SafeEncoder.encode(String.valueOf(value));
    }

}