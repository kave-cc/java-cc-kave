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
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import cc.kave.commons.exceptions.ValidationException;
import cc.kave.commons.model.naming.idecomponents.ICommandName;

public class CommandNameTest {

	private ICommandName sut;

	@Test
	public void DefaultValues() {
		sut = new CommandName();
		assertEquals("???", sut.getGuid());
		assertEquals(-1, sut.getId());
		assertEquals("???", sut.getName());
		assertTrue(sut.isUnknown());
	}

	@Test
	public void ShouldImplementIsUnknown() {
		assertTrue(new CommandName().isUnknown());
		assertTrue(new CommandName("???").isUnknown());
		assertFalse(new CommandName("a:1:abc").isUnknown());
	}

	@Test(expected = ValidationException.class)
	public void ShouldAvoidNullParameters() {
		new CommandName(null);
	}

	@Test
	public void ShouldParseCommandsWithId() {
		sut = new CommandName("a:1:abc");
		assertEquals("a", sut.getGuid());
		assertEquals(1, sut.getId());
		assertEquals("abc", sut.getName());
	}

	@Test
	public void ShouldIncludeAdditionalColonsInName() {
		sut = new CommandName("a:1:funny :)");
		assertEquals("a", sut.getGuid());
		assertEquals(1, sut.getId());
		assertEquals("funny :)", sut.getName());
	}

	@Test
	public void ShouldParseSimpleButtonClicks() {
		sut = new CommandName("funny :)");
		assertEquals("???", sut.getGuid());
		assertEquals(-1, sut.getId());
		assertEquals("funny :)", sut.getName());
	}
}