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
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.ssts.impl.SSTBaseTest;
import cc.kave.commons.model.ssts.impl.SSTTestHelper;
import cc.kave.testcommons.ToStringAssert;

public class PropertyReferenceTest extends SSTBaseTest {

	private static IPropertyName someProperty() {
		return Names.newProperty("get [T1,P1] [T2,P2].P()");
	}

	@Test
	public void testDefaultValues() {
		PropertyReference sut = new PropertyReference();

		assertThat(new VariableReference(), equalTo(sut.getReference()));
		assertThat(Names.getUnknownProperty(), equalTo(sut.getPropertyName()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		PropertyReference sut = new PropertyReference();
		sut.setPropertyName(someProperty());

		assertThat(someProperty(), equalTo(sut.getPropertyName()));
	}

	@Test
	public void testEqualityDefault() {
		PropertyReference a = new PropertyReference();
		PropertyReference b = new PropertyReference();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		PropertyReference a = new PropertyReference();
		PropertyReference b = new PropertyReference();
		a.setPropertyName(someProperty());
		b.setPropertyName(someProperty());

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentName() {
		PropertyReference a = new PropertyReference();
		PropertyReference b = new PropertyReference();
		a.setPropertyName(someProperty());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentReference() {
		PropertyReference a = new PropertyReference();
		PropertyReference b = new PropertyReference();
		a.setReference(someVarRef());
		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		PropertyReference sut = new PropertyReference();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testWithReturnIsImplemented() {
		// TODO : Visitor Test
	}

	@Test
	public void toStringIsImplemented() {
		ToStringAssert.assertToStringUtils(new PropertyReference());
	}
}