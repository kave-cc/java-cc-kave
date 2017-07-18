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
package cc.kave.commons.model.naming.impl.v0.codeelements;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Ignore;
import org.junit.Test;

import cc.kave.commons.exceptions.ValidationException;
import cc.kave.commons.model.naming.codeelements.IAliasName;

public class AliasNameTest {

	private IAliasName sut;

	@Test
	public void DefaultValues() {
		sut = new AliasName();
		assertTrue(sut.isUnknown());
		assertFalse(sut.isHashed());
	}

	@Test
	public void ShouldRecognizeUnknownName() {
		assertTrue(new AliasName().isUnknown());
		assertTrue(new AliasName("???").isUnknown());
	}

	@Test(expected = ValidationException.class)
	public void ShouldAvoidNullParameters() {
		new AliasName(null);
	}

	@Test
	@Ignore
	public void AddValidExampleOfAnAliasName() {
		fail();
	}
}