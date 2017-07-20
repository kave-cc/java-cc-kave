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

import java.util.ArrayList;

import org.junit.Test;

import com.google.common.collect.Lists;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.impl.SSTBaseTest;
import cc.kave.commons.model.ssts.impl.SSTTestHelper;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;
import cc.kave.commons.model.ssts.impl.statements.GotoStatement;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import cc.kave.testcommons.ToStringAssert;

public class UsingBlockTest extends SSTBaseTest {

	@Test
	public void testDefaultValues() {
		UsingBlock sut = new UsingBlock();

		assertThat(new VariableReference(), equalTo(sut.getReference()));
		assertThat(new ArrayList<IStatement>(), equalTo(sut.getBody()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		UsingBlock sut = new UsingBlock();
		sut.setReference(this.someVarRef("a"));
		sut.getBody().add(new ReturnStatement());
		assertThat(this.someVarRef("a"), equalTo(sut.getReference()));
		assertThat(Lists.newArrayList(new ReturnStatement()), equalTo(sut.getBody()));
	}

	@Test
	public void testChildrenIdentity() {
		UsingBlock sut = new UsingBlock();
		sut.setReference(this.someVarRef("a"));
		sut.getBody().add(new ReturnStatement());

		assertChildren(sut, sut.getReference(), sut.getBody().get(0));
	}

	@Test
	public void testEqualityDefault() {
		UsingBlock a = new UsingBlock();
		UsingBlock b = new UsingBlock();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualitfyReallyTheSame() {
		UsingBlock a = new UsingBlock();
		UsingBlock b = new UsingBlock();
		a.setReference(this.someVarRef("a"));
		a.getBody().add(new ReturnStatement());
		b.setReference(this.someVarRef("a"));
		b.getBody().add(new ReturnStatement());

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentReference() {
		UsingBlock a = new UsingBlock();
		UsingBlock b = new UsingBlock();
		a.setReference(this.someVarRef("a"));
		b.setReference(this.someVarRef("b"));

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentBody() {
		UsingBlock a = new UsingBlock();
		UsingBlock b = new UsingBlock();
		a.getBody().add(new ContinueStatement());
		b.getBody().add(new GotoStatement());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		UsingBlock sut = new UsingBlock();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testVisitorWithReturnIsImplemented() {
		// TODO: Visitor Test
	}

	@Test
	public void toStringIsImplemented() {
		ToStringAssert.assertToStringUtils(new UsingBlock());
	}
}