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

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.exceptions.AssertionException;
import cc.kave.commons.utils.io.Logger;
import cc.kave.commons.utils.ssts.completioninfo.VariableScope.ErrorHandling;

public class VariableScopeTest {

	private VariableScope<String> sut;

	@Before
	public void setup() {
		sut = new VariableScope<String>(ErrorHandling.THROW);
	}

	@After
	public void teardown() {
		Logger.reset();
	}

	@Test(expected = AssertionException.class)
	public void cannotCloseDefaultScope() {
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
	public void redeclareInSameScope_cannotRedefine() {
		sut.declare("x", "1");
		sut.declare("x", "2");
	}

	@Test
	public void redeclareInSameScope_overridesWithSameValueAreIgnored() {
		sut.declare("x", "1");
		sut.declare("x", "1");
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

	@Test
	public void errorHandling_ignore() {
		List<String> log = produceError(ErrorHandling.IGNORE);
		Assert.assertEquals(2, log.size());
		Assert.assertTrue(log.get(0).contains("~~ Redefining the variable 'x' with same type (1)."));
		Assert.assertTrue(log.get(1).contains("~~ Trying to change the value of 'x' from '1' to '2'."));
	}

	@Test
	public void errorHandling_log() {
		List<String> log = produceError(ErrorHandling.LOG);
		Assert.assertEquals(2, log.size());
		Assert.assertTrue(log.get(0).contains("~~ Redefining the variable 'x' with same type (1)."));
		Assert.assertTrue(log.get(1).contains("EE Trying to change the value of 'x' from '1' to '2'."));
	}

	@Test
	public void errorHandling_throw() {
		boolean hasThrown = false;
		try {
			produceError(ErrorHandling.THROW);
		} catch (AssertionException e) {
			hasThrown = true;
		}
		Assert.assertTrue(hasThrown);
		List<String> log = Logger.getCapturedLog();
		Assert.assertEquals(1, log.size());
		Assert.assertTrue(log.get(0).contains("~~ Redefining the variable 'x' with same type (1)."));
	}

	private static List<String> produceError(ErrorHandling handlingStrategy) {
		Logger.setCapturing(true);
		Logger.setDebugging(true);

		VariableScope<String> sut = new VariableScope<>(handlingStrategy);
		sut.declare("x", "1");
		sut.declare("x", "1"); // redefine ("warning")
		sut.declare("x", "2"); // change ("issue")

		return Logger.getCapturedLog();
	}
}