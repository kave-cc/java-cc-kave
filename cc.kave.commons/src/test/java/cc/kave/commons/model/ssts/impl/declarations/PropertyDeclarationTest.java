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

import java.util.ArrayList;

import org.junit.Test;

import com.google.common.collect.Lists;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.impl.SSTTestHelper;
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import cc.kave.testcommons.ToStringAssert;

public class PropertyDeclarationTest {

	@Test
	public void testDefaultValues() {
		PropertyDeclaration sut = new PropertyDeclaration();

		assertThat(Names.getUnknownProperty(), equalTo(sut.getName()));
		assertThat(new ArrayList<IStatement>(), equalTo(sut.getGet()));
		assertThat(new ArrayList<IStatement>(), equalTo(sut.getSet()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		PropertyDeclaration sut = new PropertyDeclaration();
		sut.setName(someProperty());
		sut.setGet(Lists.newArrayList(new ReturnStatement()));
		sut.setSet(Lists.newArrayList(new ContinueStatement()));

		assertThat(someProperty(), equalTo(sut.getName()));
		assertThat(Lists.newArrayList(new ReturnStatement()), equalTo(sut.getGet()));
		assertThat(Lists.newArrayList(new ContinueStatement()), equalTo(sut.getSet()));
	}

	@Test
	public void testEqualityDefault() {
		PropertyDeclaration a = new PropertyDeclaration();
		PropertyDeclaration b = new PropertyDeclaration();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		PropertyDeclaration a = new PropertyDeclaration();
		PropertyDeclaration b = new PropertyDeclaration();
		a.setName(someProperty());
		b.setName(someProperty());

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentType() {
		PropertyDeclaration a = new PropertyDeclaration();
		PropertyDeclaration b = new PropertyDeclaration();
		a.setName(someProperty());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentGet() {
		PropertyDeclaration a = new PropertyDeclaration();
		PropertyDeclaration b = new PropertyDeclaration();
		a.getGet().add(new ReturnStatement());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentSet() {
		PropertyDeclaration a = new PropertyDeclaration();
		PropertyDeclaration b = new PropertyDeclaration();
		a.getSet().add(new ReturnStatement());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		PropertyDeclaration sut = new PropertyDeclaration();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testWithReturnIsImplemented() {
		// TODO : Visitor Test
	}

	private IPropertyName someProperty() {
		return Names.newProperty("get [T1,P1] [T2,P2].Property()");
	}

	@Test
	public void toStringIsImplemented() {
		ToStringAssert.assertToStringUtils(new PropertyDeclaration());
	}
}