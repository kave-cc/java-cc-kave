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
import static cc.kave.rsse.calls.model.usages.MemberAccessType.MEMBER_REFERENCE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;

public class MemberAccessTest {

	private static final IMethodName MEMBER = Names.newMethod("[p:int] [T,P].m1()");

	@Test
	public void defaultValues() {
		MemberAccess c = new MemberAccess();
		assertNull(c.getType());
		assertNull(c.getMember());
	}

	@Test
	public void realValues() {
		MemberAccess c = getFullExample();
		assertEquals(MEMBER_REFERENCE, c.getType());
		assertEquals(MEMBER, c.getMember());
	}

	@Test
	public void equality_default() {
		assertEqualDataStructures(new MemberAccess(), new MemberAccess());
	}

	@Test
	public void equality_values() {
		assertEqualDataStructures(getFullExample(), getFullExample());
	}

	@Test
	public void equality_mixedCases() {
		assertMixedCase(MemberAccessTest::getFullExample, u -> u.type = null);
		assertMixedCase(MemberAccessTest::getFullExample, u -> u.member = null);
	}

	private static MemberAccess getFullExample() {
		MemberAccess a = new MemberAccess();
		a.type = MEMBER_REFERENCE;
		a.member = MEMBER;
		return a;
	}

	@Test
	public void equality_diffType() {
		MemberAccess a = new MemberAccess();
		a.type = MEMBER_REFERENCE;

		assertNotEqualDataStructures(a, new MemberAccess());
	}

	@Test
	public void equality_diffMember() {
		MemberAccess a = new MemberAccess();
		a.member = MEMBER;

		assertNotEqualDataStructures(a, new MemberAccess());
	}

	@Test
	public void toStringIsImplemented() {
		assertToStringUtils(new MemberAccess());
	}
}