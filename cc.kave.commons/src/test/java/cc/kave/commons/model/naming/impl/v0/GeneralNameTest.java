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
package cc.kave.commons.model.naming.impl.v0;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import cc.kave.commons.exceptions.ValidationException;
import cc.kave.commons.model.naming.IName;
import cc.kave.commons.model.naming.impl.v0.types.DelegateTypeName;
import cc.kave.commons.model.naming.impl.v0.types.PredefinedTypeName;
import cc.kave.commons.model.naming.impl.v0.types.TypeName;
import cc.kave.commons.model.naming.types.ITypeName;

public class GeneralNameTest {

	@Test
	public void ShouldRecognizeUnknownName() {
		assertTrue(new GeneralName().isUnknown());
		assertTrue(new GeneralName("???").isUnknown());
		assertFalse(new GeneralName("x").isUnknown());
	}

	@Test
	public void ShouldDifferentiateEqualityOnRuntimeTypeToo() {
		IName a = new PredefinedTypeName("p:int");
		IName b = new GeneralName("p:int");
		assertNotEquals(a, b);
	}

	@Test
	public void UnknownTypesAreNotEqualToOtherNames() {
		ITypeName[] unknowns = new ITypeName[] { new TypeName(), new DelegateTypeName() };

		for (ITypeName u : unknowns) {
			assertNotEquals(u, new GeneralName());
			assertNotEquals(new GeneralName(), u);
		}
	}

	@Test(expected = ValidationException.class)
	public void ShouldAvoidNullParameters() {
		// ReSharper disable once ObjectCreationAsStatement
		// ReSharper disable once AssignNullToNotNullAttribute
		new GeneralName(null);
	}

	@Test
	public void ShouldImplementIsHashed() {
		assertTrue(new GeneralName("72launbJW34oSO9wR5XBdw==").isHashed());
		assertFalse(new GeneralName("x").isHashed());
	}

	@Test
	public void ShouldNotChangeOrCloneIdentfier() {
		String expected = "x";
		String actual = new GeneralName(expected).getIdentifier();
		assertSame(expected, actual);
	}
}