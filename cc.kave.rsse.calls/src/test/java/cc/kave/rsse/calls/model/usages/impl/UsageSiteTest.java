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
import static cc.kave.commons.testing.DataStructureEqualityAsserts.assertNotEqualDataStructures;
import static cc.kave.commons.testing.ToStringAsserts.assertToStringUtils;
import static cc.kave.rsse.calls.model.usages.UsageSiteType.FIELD_ACCESS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.rsse.calls.model.usages.impl.UsageSite;

public class UsageSiteTest {

	private static final IMethodName MEMBER = Names.newMethod("[p:int] [T,P].m1()");

	@Test
	public void defaultValues() {
		UsageSite c = new UsageSite();
		assertNull(c.getType());
		assertNull(c.getMember(IMethodName.class));
		assertEquals(-1, c.getArgIndex());
	}

	@Test
	public void realValues() {
		UsageSite c = new UsageSite();
		c.type = FIELD_ACCESS;
		c.member = MEMBER;
		c.argIndex = 13;
		assertEquals(FIELD_ACCESS, c.getType());
		assertEquals(MEMBER, c.getMember(IMethodName.class));
		assertEquals(13, c.getArgIndex());
	}

	@Test
	public void equality_default() {
		assertEqualDataStructures(new UsageSite(), new UsageSite());
	}

	@Test
	public void equality_values() {
		UsageSite a = new UsageSite();
		a.type = FIELD_ACCESS;
		a.member = MEMBER;
		a.argIndex = 13;

		UsageSite b = new UsageSite();
		b.type = FIELD_ACCESS;
		b.member = MEMBER;
		b.argIndex = 13;

		assertEqualDataStructures(a, b);
	}

	@Test
	public void equality_diffTyep() {
		UsageSite a = new UsageSite();
		a.type = FIELD_ACCESS;

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