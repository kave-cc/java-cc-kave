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

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.ssts.impl.SSTTestHelper;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.testcommons.ToStringAssert;

public class VariableDeclarationTest {

	@Test
	public void testDefaultValues() {
		VariableDeclaration sut = new VariableDeclaration();

		assertThat(new VariableReference(), equalTo(sut.getReference()));
		assertThat(Names.getUnknownType(), equalTo(sut.getType()));
		assertThat(true, equalTo(sut.isMissing()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		VariableDeclaration sut = new VariableDeclaration();
		sut.setReference(SSTUtil.variableReference("a"));
		sut.setType(Names.getUnknownType());

		assertThat(false, equalTo(sut.isMissing()));
		assertThat(SSTUtil.variableReference("a"), equalTo(sut.getReference()));
		assertThat(Names.getUnknownType(), equalTo(sut.getType()));
	}

	@Test
	public void testEqualityDefault() {
		VariableDeclaration a = new VariableDeclaration();
		VariableDeclaration b = new VariableDeclaration();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		VariableDeclaration a = new VariableDeclaration();
		VariableDeclaration b = new VariableDeclaration();
		a.setReference(SSTUtil.variableReference("a"));
		a.setType(Names.newType("T1,P1"));
		b.setReference(SSTUtil.variableReference("a"));
		b.setType(Names.newType("T1,P1"));

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentReference() {
		VariableDeclaration a = new VariableDeclaration();
		VariableDeclaration b = new VariableDeclaration();
		a.setReference(SSTUtil.variableReference("a"));

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentType() {
		VariableDeclaration a = new VariableDeclaration();
		VariableDeclaration b = new VariableDeclaration();
		a.setType(Names.newType("T1,P1"));

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		VariableDeclaration sut = new VariableDeclaration();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testWithReturnIsImplemented() {
		// TODO : Visitor Test
	}

	@Test
	public void toStringIsImplemented() {
		ToStringAssert.assertToStringUtils(new VariableDeclaration());
	}
}