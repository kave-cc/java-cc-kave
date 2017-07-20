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
package cc.kave.commons.model.ssts.impl.expressions.assignable;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;

import org.junit.Test;

import com.google.common.collect.Lists;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.impl.SSTBaseTest;
import cc.kave.commons.model.ssts.impl.SSTTestHelper;
import cc.kave.commons.model.ssts.impl.statements.GotoStatement;
import cc.kave.testcommons.ToStringAssert;

public class LambdaExpressionTest extends SSTBaseTest {

	@Test
	public void testDefaulValues() {
		LambdaExpression sut = new LambdaExpression();

		assertThat(Names.getUnknownLambda(), equalTo(sut.getName()));
		assertThat(new ArrayList<IStatement>(), equalTo(sut.getBody()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		LambdaExpression sut = new LambdaExpression();
		sut.getBody().add(new GotoStatement());
		sut.setName(someLambdaName());

		assertThat(Lists.newArrayList(new GotoStatement()), equalTo(sut.getBody()));
		assertThat(someLambdaName(), equalTo(sut.getName()));
	}

	@Test
	public void testEqualityDefault() {
		LambdaExpression a = new LambdaExpression();
		LambdaExpression b = new LambdaExpression();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		LambdaExpression a = new LambdaExpression();
		LambdaExpression b = new LambdaExpression();
		a.getBody().add(new GotoStatement());
		a.setName(someLambdaName());
		b.getBody().add(new GotoStatement());
		b.setName(someLambdaName());

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentName() {
		LambdaExpression a = new LambdaExpression();
		LambdaExpression b = new LambdaExpression();
		a.setName(someLambdaName());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityBody() {
		LambdaExpression a = new LambdaExpression();
		LambdaExpression b = new LambdaExpression();
		a.getBody().add(new GotoStatement());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		LambdaExpression sut = new LambdaExpression();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testWithReturnIsImplemented() {
		// TODO : Visitor Test
	}

	@Test
	public void toStringIsImplemented() {
		ToStringAssert.assertToStringUtils(new LambdaExpression());
	}
}