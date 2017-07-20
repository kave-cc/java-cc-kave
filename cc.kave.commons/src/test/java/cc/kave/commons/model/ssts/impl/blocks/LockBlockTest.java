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
import cc.kave.commons.model.ssts.impl.statements.BreakStatement;
import cc.kave.testcommons.ToStringAssert;

public class LockBlockTest extends SSTBaseTest {

	@Test
	public void testDefaultValues() {
		LockBlock sut = new LockBlock();

		assertThat(new VariableReference(), equalTo(sut.getReference()));
		assertThat(new ArrayList<IStatement>(), equalTo(sut.getBody()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		LockBlock sut = new LockBlock();
		sut.setReference(this.someVarRef("x"));
		sut.setBody(Lists.newArrayList(new BreakStatement()));

		assertThat(this.someVarRef("x"), equalTo(sut.getReference()));
		assertThat(Lists.newArrayList(new BreakStatement()), equalTo(sut.getBody()));
	}

	@Test
	public void testChildrenIdentity() {
		LockBlock sut = new LockBlock();
		sut.setReference(this.someVarRef("x"));
		sut.setBody(Lists.newArrayList(new BreakStatement()));

		assertChildren(sut, sut.getReference(), sut.getBody().get(0));
	}

	@Test
	public void testEqualityDefault() {
		LockBlock a = new LockBlock();
		LockBlock b = new LockBlock();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		LockBlock a = new LockBlock();
		a.setReference(this.someVarRef("x"));
		a.setBody(Lists.newArrayList(new BreakStatement()));
		LockBlock b = new LockBlock();
		b.setReference(this.someVarRef("x"));
		b.setBody(Lists.newArrayList(new BreakStatement()));

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentReference() {
		LockBlock a = new LockBlock();
		a.setReference(this.someVarRef("a"));
		LockBlock b = new LockBlock();
		b.setReference(this.someVarRef("b"));

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentBody() {
		LockBlock a = new LockBlock();
		a.setBody(Lists.newArrayList(new BreakStatement()));
		LockBlock b = new LockBlock();

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		LockBlock sut = new LockBlock();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testVisitorWithReturnIsImplemented() {
		// TODO: Visitor Test
	}

	@Test
	public void toStringIsImplemented() {
		ToStringAssert.assertToStringUtils(new LockBlock());
	}
}