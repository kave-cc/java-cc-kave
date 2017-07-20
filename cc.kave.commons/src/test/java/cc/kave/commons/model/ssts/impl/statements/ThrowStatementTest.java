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
package cc.kave.commons.model.ssts.impl.statements;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import cc.kave.commons.model.ssts.impl.SSTTestHelper;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.testcommons.ToStringAssert;

public class ThrowStatementTest {

	public IVariableReference someVarRef(String s) {
		VariableReference ref = new VariableReference();
		ref.setIdentifier(s);
		return ref;
	}

	@Test
	public void testDefaultValues() {
		ThrowStatement sut = new ThrowStatement();

		assertThat(new VariableReference(), equalTo(sut.getReference()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		ThrowStatement sut = new ThrowStatement();
		sut.setReference(someVarRef("e"));
		assertThat(someVarRef("e"), equalTo(sut.getReference()));
	}

	@Test
	public void testEqualityDefault() {
		ThrowStatement a = new ThrowStatement();
		ThrowStatement b = new ThrowStatement();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		ThrowStatement a = new ThrowStatement();
		ThrowStatement b = new ThrowStatement();
		a.setReference(someVarRef("e"));
		b.setReference(someVarRef("e"));

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentException() {
		ThrowStatement a = new ThrowStatement();
		ThrowStatement b = new ThrowStatement();
		a.setReference(someVarRef("e1"));
		b.setReference(someVarRef("e2"));

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		ThrowStatement sut = new ThrowStatement();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testWithReturnIsImplemented() {
		// TODO : Visitor Test
	}

	@Test
	public void toStringIsImplemented() {
		ToStringAssert.assertToStringUtils(new ThrowStatement());
	}
}