/*
 * Copyright 2009 - 2010 Scartel Star Lab LLC, Russia
 * Copyright 2009 - 2010 Seconca Holdings Limited, Cyprus
 *
 * This source code is Scartel Star Lab Confidential Proprietary
 * This software is protected by copyright.  All rights and titles are reserved.
 * You shall not use, copy, distribute, modify, decompile, disassemble or reverse
 * engineer the software. Otherwise this violation would be treated by law and
 * would be subject to legal prosecution.  Legal use of the software provides
 * receipt of a license from the right holder only.
 */

package io.redis.rjc;

/**
 * <description>
 *
 * @author Evgeny Dolgov
 */
public interface MultiExecCommands {
    void watch(String... keys);

    void unwatch();

    void multi();

    void discard();

    void exec();
}
