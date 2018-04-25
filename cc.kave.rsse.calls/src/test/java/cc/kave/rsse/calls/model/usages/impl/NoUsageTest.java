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
package cc.kave.rsse.calls.model.usages.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

@SuppressWarnings("deprecation")
public class NoUsageTest {

	@Test(expected = RuntimeException.class)
	public void getType() {
		new NoUsage().getType();
	}

	@Test(expected = RuntimeException.class)
	public void getClassContext() {
		new NoUsage().getClassContext();
	}

	@Test(expected = RuntimeException.class)
	public void getMethodContext() {
		new NoUsage().getMethodContext();
	}

	@Test(expected = RuntimeException.class)
	public void getDefinitionSite() {
		new NoUsage().getDefinition();
	}

	@Test(expected = RuntimeException.class)
	public void getUsageSites() {
		new NoUsage().getUsageSites();
	}

	@Test(expected = RuntimeException.class)
	public void getUsageSites2() {
		new NoUsage().getUsageSites(s -> false);
	}

	@Test(expected = RuntimeException.class)
	public void isQuery() {
		new NoUsage().isQuery();
	}

	@Test(expected = RuntimeException.class)
	public void cloneMethod() {
		new NoUsage().clone();
	}

	@Test
	public void equals_same() {
		assertEquals(new NoUsage(), new NoUsage());
	}

	@Test
	public void equals_different() {
		assertNotEquals(new Object(), new NoUsage());
	}

	@Test
	public void hashCodeMethod() {
		assertEquals(42, new NoUsage().hashCode());
	}

	@Test
	public void toStringIsImplemented() {
		assertEquals("{NoUsage}", new NoUsage().toString());
	}
}