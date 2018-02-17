/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package cc.kave.rsse.calls.usages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import cc.kave.rsse.calls.usages.NoUsage;

@SuppressWarnings("deprecation")
public class NoUsageTest {

	@Test
	public void equality() {
		assertEquals(new NoUsage(), new NoUsage());
	}

	@Test
	public void equality_different() {
		assertNotEquals(new Object(), new NoUsage());
	}

	@Test
	public void hashCodeIsImplemented() {
		assertEquals(42, new NoUsage().hashCode());
	}

	@Test
	public void toStringIsImplemented() {
		assertEquals("{NoUsage}", new NoUsage().toString());
	}
}