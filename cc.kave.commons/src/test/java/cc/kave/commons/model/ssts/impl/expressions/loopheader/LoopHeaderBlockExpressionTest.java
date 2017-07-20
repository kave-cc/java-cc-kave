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
package cc.kave.commons.model.ssts.impl.expressions.loopheader;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;

import org.junit.Test;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.impl.SSTBaseTest;
import cc.kave.commons.model.ssts.impl.SSTTestHelper;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import cc.kave.testcommons.ToStringAssert;

public class LoopHeaderBlockExpressionTest extends SSTBaseTest {

	@Test
	public void testDefaultValues() {
		LoopHeaderBlockExpression sut = new LoopHeaderBlockExpression();

		assertThat(new ArrayList<IStatement>(), equalTo(sut.getBody()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		LoopHeaderBlockExpression sut = new LoopHeaderBlockExpression();
		sut.getBody().add(new ReturnStatement());

		ArrayList<IStatement> expected = new ArrayList<IStatement>();
		expected.add(new ReturnStatement());

		assertThat(expected, equalTo(sut.getBody()));
	}

	@Test
	public void testEqualityDefault() {
		LoopHeaderBlockExpression a = new LoopHeaderBlockExpression();
		LoopHeaderBlockExpression b = new LoopHeaderBlockExpression();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		LoopHeaderBlockExpression a = new LoopHeaderBlockExpression();
		LoopHeaderBlockExpression b = new LoopHeaderBlockExpression();
		a.getBody().add(new ReturnStatement());
		b.getBody().add(new ReturnStatement());

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentBody() {
		LoopHeaderBlockExpression a = new LoopHeaderBlockExpression();
		LoopHeaderBlockExpression b = new LoopHeaderBlockExpression();
		a.getBody().add(new ReturnStatement());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		LoopHeaderBlockExpression sut = new LoopHeaderBlockExpression();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testWithReturnIsImplemented() {
		// TODO : Visitor Test
	}

	@Test
	public void toStringIsImplemented() {
		ToStringAssert.assertToStringUtils(new LoopHeaderBlockExpression());
	}
}