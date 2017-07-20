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

import cc.kave.commons.model.ssts.impl.SSTTestHelper;
import cc.kave.commons.model.ssts.impl.expressions.simple.NullExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.testcommons.ToStringAssert;

public class ExpressionStatementTest {

	@Test
	public void testDefaultValues() {
		ExpressionStatement sut = new ExpressionStatement();

		assertThat(new UnknownExpression(), equalTo(sut.getExpression()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		ExpressionStatement sut = new ExpressionStatement();
		sut.setExpression(new NullExpression());

		assertThat(new NullExpression(), equalTo(sut.getExpression()));
	}

	@Test
	public void testEqualityDefault() {
		ExpressionStatement a = new ExpressionStatement();
		ExpressionStatement b = new ExpressionStatement();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		ExpressionStatement a = new ExpressionStatement();
		ExpressionStatement b = new ExpressionStatement();
		a.setExpression(new NullExpression());
		b.setExpression(new NullExpression());

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentExpression() {
		ExpressionStatement a = new ExpressionStatement();
		ExpressionStatement b = new ExpressionStatement();
		a.setExpression(new NullExpression());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		ExpressionStatement sut = new ExpressionStatement();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testWithReturnIsImplemented() {
		// TODO : Visitor Test
	}

	@Test
	public void toStringIsImplemented() {
		ToStringAssert.assertToStringUtils(new ExpressionStatement());
	}
}