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
package cc.kave.commons.model.naming.impl.v0.idecomponents;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import cc.kave.commons.exceptions.ValidationException;
import cc.kave.commons.model.naming.idecomponents.ICommandBarControlName;

public class CommandBarControlNameTest {
	private ICommandBarControlName sut;

	@Test
	public void DefaultValues() {
		sut = new CommandBarControlName();
		assertEquals("???", sut.getName());
		assertEquals(null, sut.getParent());
		assertTrue(sut.isUnknown());
	}

	@Test
	public void ShouldImplementIsUnknown() {
		assertTrue(new CommandBarControlName().isUnknown());
		assertTrue(new CommandBarControlName("???").isUnknown());
		assertFalse(new CommandBarControlName("...").isUnknown());
	}

	@Test(expected = ValidationException.class)
	public void ShouldAvoidNullParameters() {
		new CommandBarControlName(null);
	}

	@Test
	public void ShouldParseSimpleExample() {
		sut = new CommandBarControlName("a");
		assertEquals("a", sut.getName());
		assertNull(sut.getParent());
	}

	@Test
	public void ShouldParseParent() {
		sut = new CommandBarControlName("a|b|c");
		assertEquals("c", sut.getName());
		assertEquals(new CommandBarControlName("a|b"), sut.getParent());
	}

	@Test(expected = ValidationException.class)
	public void ShouldNotAcceptDoubleSeparator() {
		new CommandBarControlName("a||b");
	}
}