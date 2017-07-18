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
import cc.kave.commons.model.naming.idecomponents.IWindowName;

public class WindowNameTest {

	private IWindowName sut;

	@Test
	public void DefaultValues() {
		sut = new WindowName();
		assertEquals("???", sut.getType());
		assertEquals("???", sut.getCaption());
		assertTrue(sut.isUnknown());
	}

	@Test
	public void ShouldImplementIsUnknown() {
		assertTrue(new WindowName().isUnknown());
		assertTrue(new WindowName("???").isUnknown());
		assertFalse(new WindowName("someType someCaption").isUnknown());
	}

	@Test(expected = ValidationException.class)
	public void ShouldAvoidNullParameters() {
		new WindowName(null);
	}

	@Test
	public void ShouldParseType() {
		assertEquals("someType", new WindowName("someType someCaption").getType());
	}

	@Test
	public void ShouldParseCaption() {
		assertEquals("someCaption", new WindowName("someType someCaption").getCaption());
	}

	@Test(expected = ValidationException.class)
	public void ShouldRejectNameWithoutSpaces() {
		new WindowName("OnlyTypeOrCaption");
	}
}