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
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import cc.kave.testcommons.ToStringAssert;

public class DoLoopTest extends SSTBaseTest {
	@Test
	public void testDefaultValues() {
		DoLoop sut = new DoLoop();

		assertThat(new UnknownExpression(), equalTo(sut.getCondition()));
		assertThat(Lists.newArrayList(), equalTo(sut.getBody()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		DoLoop sut = new DoLoop();
		sut.setCondition(new ConstantValueExpression());
		sut.getBody().add(new ReturnStatement());

		assertThat(new ConstantValueExpression(), equalTo(sut.getCondition()));
		assertThat(Lists.newArrayList(new ReturnStatement()), equalTo(sut.getBody()));
	}

	@Test
	public void testChildrenIdentity() {
		DoLoop sut = new DoLoop();
		sut.setCondition(new ConstantValueExpression());
		sut.getBody().add(new ReturnStatement());
		assertChildren(sut, sut.getCondition(), sut.getBody().get(0));
	}

	@Test
	public void testEqualityDefault() {
		DoLoop a = new DoLoop();
		DoLoop b = new DoLoop();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		DoLoop a = new DoLoop();
		DoLoop b = new DoLoop();
		a.setCondition(new ConstantValueExpression());
		a.getBody().add(new ReturnStatement());
		b.setCondition(new ConstantValueExpression());
		b.getBody().add(new ReturnStatement());

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentCondition() {
		DoLoop a = new DoLoop();
		DoLoop b = new DoLoop();
		a.setCondition(new ConstantValueExpression());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentBody() {
		DoLoop a = new DoLoop();
		DoLoop b = new DoLoop();
		a.getBody().add(new ReturnStatement());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		DoLoop sut = new DoLoop();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testWithReturnIsImplemented() {
		// TODO : Visitor Test
	}

	@Test
	public void toStringIsImplemented() {
		ToStringAssert.assertToStringUtils(new DoLoop());
	}
}