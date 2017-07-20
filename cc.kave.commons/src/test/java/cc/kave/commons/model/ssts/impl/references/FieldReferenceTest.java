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
import static org.junit.Assert.assertThat;

import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.ssts.impl.SSTBaseTest;
import cc.kave.commons.model.ssts.impl.SSTTestHelper;
import cc.kave.testcommons.ToStringAssert;

public class FieldReferenceTest extends SSTBaseTest {

	private static IFieldName someField() {
		return Names.newField("[T1,P1] [T2,P2].E");
	}

	@Test
	public void testDefaultValues() {
		FieldReference sut = new FieldReference();

		assertThat(new VariableReference(), equalTo(sut.getReference()));
		assertThat(Names.getUnknownField(), equalTo(sut.getFieldName()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		FieldReference sut = new FieldReference();
		sut.setFieldName(someField());

		assertThat(someField(), equalTo(sut.getFieldName()));
	}

	@Test
	public void testEqualityDefault() {
		FieldReference a = new FieldReference();
		FieldReference b = new FieldReference();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		FieldReference a = new FieldReference();
		FieldReference b = new FieldReference();
		a.setFieldName(someField());
		b.setFieldName(someField());

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentName() {
		FieldReference a = new FieldReference();
		FieldReference b = new FieldReference();
		a.setFieldName(someField());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentReference() {
		FieldReference a = new FieldReference();
		FieldReference b = new FieldReference();
		a.setReference(someVarRef());
		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		FieldReference sut = new FieldReference();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testWithReturnIsImplemented() {
		// TODO : Visitor Test
	}

	@Test
	public void toStringIsImplemented() {
		ToStringAssert.assertToStringUtils(new FieldReference());
	}
}