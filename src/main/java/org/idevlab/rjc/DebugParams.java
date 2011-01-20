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

public class DebugParams {
    private String[] command;

    public String[] getCommand() {
	return command;
    }

    private DebugParams() {

    }

    public static DebugParams SEGFAULT() {
	DebugParams debugParams = new DebugParams();
	debugParams.command = new String[] { "SEGFAULT" };
	return debugParams;
    }

    public static DebugParams OBJECT(String key) {
	DebugParams debugParams = new DebugParams();
	debugParams.command = new String[] { "OBJECT", key };
	return debugParams;
    }

    public static DebugParams SWAPIN(String key) {
	DebugParams debugParams = new DebugParams();
	debugParams.command = new String[] { "SWAPIN", key };
	return debugParams;
    }

    public static DebugParams RELOAD() {
	DebugParams debugParams = new DebugParams();
	debugParams.command = new String[] { "RELOAD" };
	return debugParams;
    }

    public static DebugParams SWAPOUT(String key) {
	DebugParams debugParams = new DebugParams();
	debugParams.command = new String[] { "SWAPOUT", key };
	return debugParams;
    }
}
