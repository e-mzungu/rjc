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

import java.io.ByteArrayInputStream;

/**
 * Test class the fragment a byte array for testing purpose.
 */
public class FragmentedByteArrayInputStream extends ByteArrayInputStream {
	private int readMethodCallCount = 0;
	public FragmentedByteArrayInputStream(final byte[] buf) {
		super(buf);
	}

	@Override
	public synchronized int read(final byte[] b, final int off, final int len) {
		readMethodCallCount++;
		if (len <= 10) {
			// if the len <= 10, return as usual ..
			return super.read(b, off, len);
		} else {
			// else return the first half ..
			return super.read(b, off, len / 2);
		}
	}

	public int getReadMethodCallCount() {
		return readMethodCallCount;
	}

}