/**
 * Copyright 2018 University of Zurich
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

import static cc.kave.commons.testing.DataStructureEqualityAsserts.assertEqualDataStructures;
import static cc.kave.commons.testing.DataStructureEqualityAsserts.assertMixedCase;
import static cc.kave.commons.testing.DataStructureEqualityAsserts.assertNotEqualDataStructures;
import static cc.kave.commons.testing.ToStringAsserts.assertToStringUtils;
import static cc.kave.rsse.calls.model.usages.UsageSiteType.MEMBER_ACCESS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;

public class UsageSiteTest {

	private static final IMethodName MEMBER = Names.newMethod("[p:int] [T,P].m1()");

	@Test
	public void defaultValues() {
		UsageSite c = new UsageSite();
		assertNull(c.getType());
		assertNull(c.getMember());
		assertEquals(-1, c.getArgIndex());
	}

	@Test
	public void realValues() {
		UsageSite c = getFullExample();
		assertEquals(MEMBER_ACCESS, c.getType());
		assertEquals(MEMBER, c.getMember());
		assertEquals(13, c.getArgIndex());
	}

	@Test
	public void equality_default() {
		assertEqualDataStructures(new UsageSite(), new UsageSite());
	}

	@Test
	public void equality_values() {
		assertEqualDataStructures(getFullExample(), getFullExample());
	}

	@Test
	public void equality_mixedCases() {
		assertMixedCase(UsageSiteTest::getFullExample, u -> u.type = null);
		assertMixedCase(UsageSiteTest::getFullExample, u -> u.member = null);
	}

	private static UsageSite getFullExample() {
		UsageSite a = new UsageSite();
		a.type = MEMBER_ACCESS;
		a.member = MEMBER;
		a.argIndex = 13;
		return a;
	}

	@Test
	public void equality_diffType() {
		UsageSite a = new UsageSite();
		a.type = MEMBER_ACCESS;

		assertNotEqualDataStructures(a, new UsageSite());
	}

	@Test
	public void equality_diffMember() {
		UsageSite a = new UsageSite();
		a.member = MEMBER;

		assertNotEqualDataStructures(a, new UsageSite());
	}

	@Test
	public void equality_diffArgIndex() {
		UsageSite a = new UsageSite();
		a.argIndex = 13;

		assertNotEqualDataStructures(a, new UsageSite());
	}

	@Test
	public void toStringIsImplemented() {
		assertToStringUtils(new UsageSite());
	}
}