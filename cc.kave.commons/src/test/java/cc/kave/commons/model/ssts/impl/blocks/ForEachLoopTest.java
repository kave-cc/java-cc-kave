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
package cc.kave.commons.model.ssts.impl.blocks;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.google.common.collect.Lists;

import cc.kave.commons.model.ssts.impl.SSTBaseTest;
import cc.kave.commons.model.ssts.impl.SSTTestHelper;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import cc.kave.commons.model.ssts.impl.statements.VariableDeclaration;
import cc.kave.testcommons.ToStringAssert;

public class ForEachLoopTest extends SSTBaseTest {
	@Test
	public void testDefaultValues() {
		ForEachLoop sut = new ForEachLoop();

		assertThat(new VariableDeclaration(), equalTo(sut.getDeclaration()));
		assertThat(new VariableReference(), equalTo(sut.getLoopedReference()));
		assertThat(Lists.newArrayList(), equalTo(sut.getBody()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		ForEachLoop sut = new ForEachLoop();
		sut.setDeclaration(someDeclaration());
		sut.setLoopedReference(this.someVarRef("a"));
		sut.getBody().add(new ReturnStatement());

		assertThat(this.someDeclaration(), equalTo(sut.getDeclaration()));
		assertThat(this.someVarRef("a"), equalTo(sut.getLoopedReference()));
		assertThat(Lists.newArrayList(new ReturnStatement()), equalTo(sut.getBody()));
	}

	@Test
	public void testChildrenIdentity() {
		ForEachLoop sut = new ForEachLoop();
		sut.setDeclaration(someDeclaration());
		sut.setLoopedReference(this.someVarRef("a"));
		sut.getBody().add(new ReturnStatement());

		assertChildren(sut, sut.getDeclaration(), sut.getLoopedReference(), sut.getBody().get(0));
	}

	@Test
	public void testEqualityDefault() {
		ForEachLoop a = new ForEachLoop();
		ForEachLoop b = new ForEachLoop();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		ForEachLoop a = new ForEachLoop();
		ForEachLoop b = new ForEachLoop();
		a.setDeclaration(someDeclaration());
		a.setLoopedReference(this.someVarRef("a"));
		a.getBody().add(new ReturnStatement());
		b.setDeclaration(someDeclaration());
		b.setLoopedReference(this.someVarRef("a"));
		b.getBody().add(new ReturnStatement());

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentDeclaration() {
		ForEachLoop a = new ForEachLoop();
		ForEachLoop b = new ForEachLoop();
		a.setDeclaration(someDeclaration());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentBody() {
		ForEachLoop a = new ForEachLoop();
		ForEachLoop b = new ForEachLoop();
		a.getBody().add(new ReturnStatement());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentLoopedReference() {
		ForEachLoop a = new ForEachLoop();
		ForEachLoop b = new ForEachLoop();
		a.setLoopedReference(this.someVarRef("a"));

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		ForEachLoop sut = new ForEachLoop();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testWithReturnIsImplemented() {
		// TODO : Visitor Test
	}

	@Test
	public void toStringIsImplemented() {
		ToStringAssert.assertToStringUtils(new ForEachLoop());
	}
}