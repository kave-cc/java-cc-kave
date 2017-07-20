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
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import cc.kave.testcommons.ToStringAssert;

public class TryBlockTest extends SSTBaseTest {

	@Test
	public void testDefaultValues() {
		TryBlock sut = new TryBlock();

		assertThat(new ArrayList<IStatement>(), equalTo(sut.getBody()));
		assertThat(new ArrayList<CatchBlock>(), equalTo(sut.getCatchBlocks()));
		assertThat(new ArrayList<IStatement>(), equalTo(sut.getFinally()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		TryBlock sut = new TryBlock();
		sut.getBody().add(new ContinueStatement());
		sut.getCatchBlocks().add(new CatchBlock());
		sut.getFinally().add(new ReturnStatement());

		assertThat(Lists.newArrayList(new ContinueStatement()), equalTo(sut.getBody()));
		assertThat(Lists.newArrayList(new CatchBlock()), equalTo(sut.getCatchBlocks()));
		assertThat(Lists.newArrayList(new ReturnStatement()), equalTo(sut.getFinally()));
	}

	@Test
	public void testChildrenIdentity() {
		TryBlock sut = new TryBlock();
		sut.getBody().add(new ContinueStatement());
		sut.getCatchBlocks().add(new CatchBlock());
		sut.getFinally().add(new ReturnStatement());

		assertChildren(sut, sut.getBody().get(0), sut.getFinally().get(0));
	}

	@Test
	public void testEqualityDefault() {
		TryBlock a = new TryBlock();
		TryBlock b = new TryBlock();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		TryBlock a = new TryBlock();
		TryBlock b = new TryBlock();
		a.getBody().add(new ContinueStatement());
		a.getCatchBlocks().add(new CatchBlock());
		a.getFinally().add(new ReturnStatement());
		b.getBody().add(new ContinueStatement());
		b.getCatchBlocks().add(new CatchBlock());
		b.getFinally().add(new ReturnStatement());

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentBody() {
		TryBlock a = new TryBlock();
		TryBlock b = new TryBlock();
		a.getBody().add(new ContinueStatement());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentCatchBlocks() {
		TryBlock a = new TryBlock();
		TryBlock b = new TryBlock();
		a.getCatchBlocks().add(new CatchBlock());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentFinally() {
		TryBlock a = new TryBlock();
		TryBlock b = new TryBlock();
		a.getFinally().add(new ReturnStatement());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		TryBlock sut = new TryBlock();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testVisitorWithReturnIsImplemented() {
		// TODO: Visitor Test
	}

	@Test
	public void toStringIsImplemented() {
		ToStringAssert.assertToStringUtils(new TryBlock());
	}
}