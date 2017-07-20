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
package cc.kave.commons.model.ssts.impl.declarations;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.types.IDelegateTypeName;
import cc.kave.commons.model.ssts.impl.SSTTestHelper;
import cc.kave.testcommons.ToStringAssert;

public class DelegateDeclarationTest {

	@Test
	public void testDefaultValues() {
		DelegateDeclaration sut = new DelegateDeclaration();

		assertThat(Names.getUnknownDelegateType(), equalTo(sut.getName()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		DelegateDeclaration sut = new DelegateDeclaration();
		sut.setName(someDelegateType());

		assertThat(someDelegateType(), equalTo(sut.getName()));
	}

	@Test
	public void testEqualityDefault() {
		DelegateDeclaration a = new DelegateDeclaration();
		DelegateDeclaration b = new DelegateDeclaration();
		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		DelegateDeclaration a = new DelegateDeclaration();
		DelegateDeclaration b = new DelegateDeclaration();
		a.setName(someDelegateType());
		b.setName(someDelegateType());

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentType() {
		DelegateDeclaration a = new DelegateDeclaration();
		DelegateDeclaration b = new DelegateDeclaration();
		a.setName(someDelegateType());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		DelegateDeclaration sut = new DelegateDeclaration();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testWithReturnIsImplemented() {
		// TODO : Visitor Test
	}

	private static IDelegateTypeName someDelegateType() {
		return Names.newType("d:[R,P] [SomeDelegateType,P].()").asDelegateTypeName();
	}

	@Test
	public void toStringIsImplemented() {
		ToStringAssert.assertToStringUtils(new DelegateDeclaration());
	}
}