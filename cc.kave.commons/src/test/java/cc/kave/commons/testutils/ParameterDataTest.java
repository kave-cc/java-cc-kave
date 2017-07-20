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
package cc.kave.commons.testutils;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.testing.ParameterData;

public class ParameterDataTest {

	private ParameterData sut;

	@Before
	public void setup() {
		sut = new ParameterData();
	}

	@Test
	public void addSimpleCase() {
		sut.add(1);
		assertArr(new Object[] { new Object[] { 1 } });
	}

	@Test
	public void addArrayCase() {
		sut.add(1, 2, 3);
		assertArr(new Object[] { new Object[] { 1, 2, 3 } });
	}

	@Test
	public void addString() {
		sut.add("x");
		assertArr(new Object[] { new Object[] { "x" } });
	}

	@Test
	public void addStringDoesNotAddSeparators() {
		sut.add("x,y|z");
		assertArr(new Object[] { new Object[] { "x,y|z" } });
	}

	@Test
	public void addStringDoesNotAddSeparatorsArr() {
		sut.add("x,y", "y|z");
		assertArr(new Object[] { new Object[] { "x,y", "y|z" } });
	}

	private void assertArr(Object[] expecteds) {
		Object[][] actuals = sut.toArray();
		assertArrayEquals(expecteds, actuals);
	}
}