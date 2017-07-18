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
import cc.kave.commons.model.naming.idecomponents.IProjectItemName;

public class ProjectItemNameTest {

	private IProjectItemName sut;

	@Test
	public void DefaultValues() {
		sut = new ProjectItemName();
		assertEquals("???", sut.getType());
		assertEquals("???", sut.getName());
		assertTrue(sut.isUnknown());
	}

	@Test
	public void ShouldImplementIsUnknown() {
		assertTrue(new ProjectItemName().isUnknown());
		assertTrue(new ProjectItemName("?").isUnknown());
		assertFalse(new ProjectItemName("someType someName").isUnknown());
	}

	@Test(expected = ValidationException.class)
	public void ShouldAvoidNullParameters() {
		new ProjectItemName(null);
	}

	@Test
	public void ShouldParseType() {
		sut = new ProjectItemName("File C:\\Project\\File.txt");

		assertEquals("File", sut.getType());
	}

	@Test
	public void ShouldParseName() {
		sut = new ProjectItemName("File C:\\Project\\File.txt");

		assertEquals("C:\\Project\\File.txt", sut.getName());
	}

	@Test(expected = ValidationException.class)
	public void ShouldRejectNameWithoutSpaces() {
		new ProjectItemName("C:\\No\\Type\\Only\\File.cs");
	}
}