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
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.commons.model.ssts.impl.statements.BreakStatement;
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;
import cc.kave.commons.model.ssts.impl.statements.GotoStatement;
import cc.kave.testcommons.ToStringAssert;

public class ForLoopTest extends SSTBaseTest {

	@Test
	public void testDefaultValues() {
		ForLoop sut = new ForLoop();
		assertThat(new ArrayList<IStatement>(), equalTo(sut.getInit()));
		assertThat(new UnknownExpression(), equalTo(sut.getCondition()));
		assertThat(new ArrayList<IStatement>(), equalTo(sut.getStep()));
		assertThat(new ArrayList<IStatement>(), equalTo(sut.getBody()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		ForLoop sut = new ForLoop();
		sut.setCondition(new ConstantValueExpression());
		sut.getInit().add(new GotoStatement());
		sut.getStep().add(new BreakStatement());
		sut.getBody().add(new ContinueStatement());

		assertThat(new ConstantValueExpression(), equalTo(sut.getCondition()));
		assertThat(Lists.newArrayList(new GotoStatement()), equalTo(sut.getInit()));
		assertThat(Lists.newArrayList(new BreakStatement()), equalTo(sut.getStep()));
		assertThat(Lists.newArrayList(new ContinueStatement()), equalTo(sut.getBody()));
	}

	@Test
	public void testChildrenIdentity() {
		ForLoop sut = new ForLoop();
		sut.setCondition(new ConstantValueExpression());
		sut.getInit().add(new GotoStatement());
		sut.getStep().add(new BreakStatement());
		sut.getBody().add(new ContinueStatement());

		assertChildren(sut, sut.getInit().get(0), sut.getCondition(), sut.getStep().get(0), sut.getBody().get(0));
	}

	@Test
	public void testEqualityDefault() {
		ForLoop a = new ForLoop();
		ForLoop b = new ForLoop();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		ForLoop a = new ForLoop();
		a.setCondition(new ConstantValueExpression());
		a.getInit().add(new GotoStatement());
		a.getBody().add(new ContinueStatement());
		a.getStep().add(new BreakStatement());
		ForLoop b = new ForLoop();
		b.getInit().add(new GotoStatement());
		b.getBody().add(new ContinueStatement());
		b.getStep().add(new BreakStatement());
		b.setCondition(new ConstantValueExpression());

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentCondition() {
		ForLoop a = new ForLoop();
		ForLoop b = new ForLoop();
		a.setCondition(new ConstantValueExpression());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentInit() {
		ForLoop a = new ForLoop();
		ForLoop b = new ForLoop();
		a.getInit().add(new GotoStatement());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentBody() {
		ForLoop a = new ForLoop();
		ForLoop b = new ForLoop();
		a.getBody().add(new ContinueStatement());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentStep() {
		ForLoop a = new ForLoop();
		ForLoop b = new ForLoop();
		a.getStep().add(new BreakStatement());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		ForLoop sut = new ForLoop();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void VisitorWithReturnIsImplemented() {
		// TODO : Visitor Test
	}

	@Test
	public void toStringIsImplemented() {
		ToStringAssert.assertToStringUtils(new ForLoop());
	}
}