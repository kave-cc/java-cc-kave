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
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.impl.SSTTestHelper;
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import cc.kave.testcommons.ToStringAssert;

public class MethodDeclarationTest {

	private IMethodName _mA = Names.newMethod("[T1,P1] [T2,P2].A()");
	private IMethodName _mB = Names.newMethod("[T1,P1] [T2,P2].B()");

	@Test
	public void testDefaultValues() {
		MethodDeclaration sut = new MethodDeclaration();

		assertThat(Names.getUnknownMethod(), equalTo(sut.getName()));
		assertThat(false, equalTo(sut.isEntryPoint()));
		assertThat(new ArrayList<IMethodDeclaration>(), equalTo(sut.getBody()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		MethodDeclaration sut = new MethodDeclaration();
		sut.setName(_mA);
		sut.setEntryPoint(true);
		sut.getBody().add(new ReturnStatement());

		assertThat(this._mA, equalTo(sut.getName()));
		assertThat(true, equalTo(sut.isEntryPoint()));
		assertThat(Lists.newArrayList(new ReturnStatement()), equalTo(sut.getBody()));
	}

	@Test
	public void testEqualityDefault() {
		MethodDeclaration a = new MethodDeclaration();
		MethodDeclaration b = new MethodDeclaration();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		MethodDeclaration a = new MethodDeclaration();
		MethodDeclaration b = new MethodDeclaration();
		a.setName(_mA);
		a.setEntryPoint(true);
		a.getBody().add(new ReturnStatement());
		b.setName(_mA);
		b.setEntryPoint(true);
		b.getBody().add(new ReturnStatement());

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentName() {
		MethodDeclaration a = new MethodDeclaration();
		MethodDeclaration b = new MethodDeclaration();
		a.setName(_mA);
		b.setName(_mB);

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentEntryPoint() {
		MethodDeclaration a = new MethodDeclaration();
		MethodDeclaration b = new MethodDeclaration();
		a.setEntryPoint(true);

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentBody() {
		MethodDeclaration a = new MethodDeclaration();
		MethodDeclaration b = new MethodDeclaration();
		a.getBody().add(new ContinueStatement());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		MethodDeclaration sut = new MethodDeclaration();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testWithReturnIsImplemented() {
		// TODO : Visitor Test
	}

	@Test
	public void toStringIsImplemented() {
		ToStringAssert.assertToStringUtils(new MethodDeclaration());
	}
}