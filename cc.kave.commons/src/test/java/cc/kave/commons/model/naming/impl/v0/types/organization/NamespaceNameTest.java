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
package cc.kave.commons.model.naming.impl.v0.types.organization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;

import cc.kave.commons.exceptions.ValidationException;
import cc.kave.commons.model.naming.types.organization.INamespaceName;
import cc.kave.testcommons.ParameterData;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class NamespaceNameTest {

	@Test
	public void DefaultValues() {
		INamespaceName sut = new NamespaceName();
		assertFalse(sut.isGlobalNamespace());
		assertTrue(sut.isUnknown());
		assertEquals("???", sut.getName());
		assertEquals(new NamespaceName(), sut.getParentNamespace());
	}

	@Test
	public void ShouldRecognizeUnknownName() {
		assertTrue(new NamespaceName().isUnknown());
		assertTrue(new NamespaceName("???").isUnknown());
		assertFalse(new NamespaceName("a.b.c").isUnknown());
	}

	@Test(expected = ValidationException.class)
	public void ShouldAvoidNullParameters() {
		new NamespaceName(null);
	}

	@Test
	public void GlobalNamespace() {
		INamespaceName sut = new NamespaceName("");
		assertEquals("", sut.getName());
		assertTrue(sut.isGlobalNamespace());
		assertEquals("", sut.getIdentifier());
		assertNull(sut.getParentNamespace());
	}

	public static Object[][] provideNamespaces() {
		ParameterData pd = new ParameterData();
		// id, name, parent
		pd.add("", "", null);
		pd.add("a", "a", "");
		pd.add("a.b", "b", "a");
		pd.add("a.b.c", "c", "a.b");

		return pd.toArray();
	}

	@Test
	@Parameters(method = "provideNamespaces")
	public void ShoulParseName(String id, String name, String parentId) {
		assertEquals(name, new NamespaceName(id).getName());
	}

	@Test
	@Parameters(method = "provideNamespaces")
	public void ShoulParseParent(String id, String name, String parentId) {
		if (parentId == null) {
			assertNull(new NamespaceName(id).getParentNamespace());
		} else {
			assertEquals(new NamespaceName(parentId), new NamespaceName(id).getParentNamespace());
		}
	}
}