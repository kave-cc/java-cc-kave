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
package cc.kave.commons.utils.ssts.completioninfo;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.exceptions.AssertionException;

public class VariableScopeTest {

	private VariableScope<String> sut;

	@Before
	public void setup() {
		sut = new VariableScope<String>();
	}

	@Test(expected = AssertionException.class)
	public void cannotCloseDefaultScope() {
		sut.close();
	}

	@Test
	public void canCloseOtherScopes() {
		sut.open();
		sut.close();
	}

	@Test
	public void isDeclared() {
		assertFalse(sut.isDeclared("x"));
		assertFalse(sut.isDeclaredInCurrentScope("x"));

		sut.declare("x", "...");

		assertTrue(sut.isDeclared("x"));
		assertTrue(sut.isDeclaredInCurrentScope("x"));

		sut.open();
		sut.declare("y", "0000");

		assertTrue(sut.isDeclared("x"));
		assertTrue(sut.isDeclared("y"));
		assertFalse(sut.isDeclaredInCurrentScope("x"));
		assertTrue(sut.isDeclaredInCurrentScope("y"));

		sut.close();

		assertTrue(sut.isDeclared("x"));
		assertTrue(sut.isDeclaredInCurrentScope("x"));
		assertFalse(sut.isDeclared("y"));
		assertFalse(sut.isDeclaredInCurrentScope("y"));
	}

	@Test
	public void getValue() {
		sut.declare("x", "1");
		Assert.assertEquals("1", sut.get("x"));
	}

	@Test
	public void getValue_parentScope() {
		sut.declare("x", "1");
		sut.open();
		Assert.assertEquals("1", sut.get("x"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void getValue_undefined() {
		sut.get("x");
	}

	@Test(expected = AssertionException.class)
	public void cannotRedeclareInSameScope() {
		sut.declare("x", "1");
		sut.declare("x", "2");
	}

	@Test
	public void canRedeclareInNewScope() {
		sut.declare("x", "1");
		sut.open();
		sut.declare("x", "2");
	}

	@Test
	public void valuesAreNotOverridden() {
		sut.declare("x", "1");
		sut.open();
		sut.declare("x", "2");

		Assert.assertEquals("2", sut.get("x"));
		sut.close();
		Assert.assertEquals("1", sut.get("x"));
	}

	@Test
	public void valuesInNestedScopesAreForgotten() {
		sut.open();
		sut.declare("x", "1");
		sut.close();

		assertFalse(sut.isDeclared("y"));
	}
}