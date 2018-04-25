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
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByConstant;
import static cc.kave.rsse.calls.model.usages.impl.UsageSites.call;
import static cc.kave.rsse.calls.model.usages.impl.UsageSites.callParameter;
import static cc.kave.rsse.calls.model.usages.impl.UsageSites.memberAccessToField;
import static cc.kave.rsse.calls.model.usages.impl.UsageSites.memberAccessToProperty;
import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;

import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IFieldName;

public class UsageTest {

	@Test
	public void defaultValues() {
		Usage sut = new Usage();
		assertNull(sut.type);
		assertNull(sut.classCtx);
		assertNull(sut.methodCtx);
		assertNull(sut.definition);
		assertEquals(new ArrayList<>(), sut.usageSites);
		assertFalse(sut.isQuery);
	}

	@Test
	public void getters() {
		Usage sut = new Usage();
		sut.type = Names.newType("T, P");
		sut.classCtx = Names.newType("S, P");
		sut.methodCtx = Names.newMethod("[p:void] [T, P].ctx()");
		sut.definition = definedByConstant();
		sut.usageSites.add(call("[p:void] [T, P].m()"));
		sut.isQuery = true;

		assertSame(sut.type, sut.getType());
		assertSame(sut.classCtx, sut.getClassContext());
		assertSame(sut.methodCtx, sut.getMethodContext());
		assertSame(sut.definition, sut.getDefinition());
		assertSame(sut.usageSites, sut.getUsageSites());
		assertSame(sut.isQuery, sut.isQuery());
	}

	@Test
	public void getSpecificUsageSites() {
		UsageSite f = memberAccessToField("[p:int] [T, P]._f");

		Usage sut = new Usage();
		sut.usageSites.add(call("[p:void] [T, P].m()"));
		sut.usageSites.add(callParameter("[p:void] [T, P].m([p:int] p)", 0));
		sut.usageSites.add(f);
		sut.usageSites.add(memberAccessToProperty("set get [p:int] [T, P].P()"));

		assertEquals(newArrayList(f), sut.getUsageSites(s -> s.getMember() instanceof IFieldName));
	}

	@Test
	public void equality_default() {
		assertEqualDataStructures(new Usage(), new Usage());
	}

	@Test
	public void equality_withValues() {
		assertEqualDataStructures(getFullExample(), getFullExample());
	}

	@Test
	public void equality_mixedCases() {
		assertMixedCase(UsageTest::getFullExample, u -> u.type = null);
		assertMixedCase(UsageTest::getFullExample, u -> u.classCtx = null);
		assertMixedCase(UsageTest::getFullExample, u -> u.methodCtx = null);
		assertMixedCase(UsageTest::getFullExample, u -> u.definition = null);
	}

	private static Usage getFullExample() {
		Usage a = new Usage();
		a.type = Names.newType("T, P");
		a.classCtx = Names.newType("S, P");
		a.methodCtx = Names.newMethod("[p:void] [T, P].ctx()");
		a.definition = definedByConstant();
		a.usageSites.add(call("[p:void] [T, P].m("));
		a.isQuery = true;
		return a;
	}

	@Test
	public void equality_diffType() {
		Usage a = new Usage();
		a.type = Names.newType("T, P");

		assertNotEqualDataStructures(a, new Usage());
	}

	@Test
	public void equality_diffClassCtx() {
		Usage a = new Usage();
		a.classCtx = Names.newType("S, P");

		assertNotEqualDataStructures(a, new Usage());
	}

	@Test
	public void equality_diffMethodCtx() {
		Usage a = new Usage();
		a.methodCtx = Names.newMethod("[p:void] [T, P].ctx()");

		assertNotEqualDataStructures(a, new Usage());
	}

	@Test
	public void equality_diffDef() {
		Usage a = new Usage();
		a.definition = definedByConstant();

		assertNotEqualDataStructures(a, new Usage());
	}

	@Test
	public void equality_diffUsageSites() {
		Usage a = new Usage();
		a.usageSites.add(call("[p:void] [T, P].m("));

		assertNotEqualDataStructures(a, new Usage());
	}

	@Test
	public void equality_diffIsQuery() {
		Usage a = new Usage();
		a.isQuery = true;

		assertNotEqualDataStructures(a, new Usage());
	}

	@Test
	public void cloning() {
		Usage sut = new Usage();
		sut.type = Names.newType("T, P");
		sut.classCtx = Names.newType("S, P");
		sut.methodCtx = Names.newMethod("[p:void] [T, P].ctx()");
		sut.definition = definedByConstant();
		sut.usageSites.add(call("[p:void] [T, P].m()"));

		assertNotSame(sut, sut.clone());
		assertEqualDataStructures(sut, sut.clone());
	}

	@Test
	public void toStringTest() {
		assertToStringUtils(new Usage());
	}
}