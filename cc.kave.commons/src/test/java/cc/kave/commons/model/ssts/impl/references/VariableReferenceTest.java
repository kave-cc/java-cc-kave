/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.kave.commons.model.ssts.impl.references;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import cc.kave.commons.model.ssts.impl.SSTTestHelper;

public class VariableReferenceTest {

	@Test
	public void testDefaultValues() {
		VariableReference sut = new VariableReference();

		assertThat("", equalTo(sut.getIdentifier()));
		assertThat(true, equalTo(sut.isMissing()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		VariableReference sut = new VariableReference();
		sut.setIdentifier("a");

		assertThat(false, equalTo(sut.isMissing()));
		assertThat("a", equalTo(sut.getIdentifier()));
	}

	@Test
	public void testEqualityDefault() {
		VariableReference a = new VariableReference();
		VariableReference b = new VariableReference();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		VariableReference a = new VariableReference();
		VariableReference b = new VariableReference();
		a.setIdentifier("a");
		b.setIdentifier("a");

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentIdentifier() {
		VariableReference a = new VariableReference();
		VariableReference b = new VariableReference();
		a.setIdentifier("a");

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		VariableReference sut = new VariableReference();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testWithReturnIsImplemented() {
		// TODO : Visitor Test
	}

	@Test
	public void toStringIsImplemented() {
		VariableReference ref = new VariableReference();
		ref.setIdentifier("x");
		assertEquals("VarRef(x)", ref.toString());
	}
}