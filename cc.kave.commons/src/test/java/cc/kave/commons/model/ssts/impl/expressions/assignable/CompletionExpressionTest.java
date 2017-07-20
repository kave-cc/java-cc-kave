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

import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.ssts.impl.SSTBaseTest;
import cc.kave.commons.model.ssts.impl.SSTTestHelper;
import cc.kave.testcommons.ToStringAssert;

public class CompletionExpressionTest extends SSTBaseTest {

	@Test
	public void testDefaultValues() {
		CompletionExpression sut = new CompletionExpression();

		assertThat("", equalTo(sut.getToken()));
		assertThat(null, equalTo(sut.getVariableReference()));
		assertThat(null, equalTo(sut.getTypeReference()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		CompletionExpression sut = new CompletionExpression();
		sut.setObjectReference(someVarRef("i"));
		sut.setTypeReference(Names.getUnknownType());
		sut.setToken("t");

		assertThat("t", equalTo(sut.getToken()));
		assertThat(someVarRef("i"), equalTo(sut.getVariableReference()));
		assertThat(Names.getUnknownType(), equalTo(sut.getTypeReference()));
	}

	@Test
	public void testEqualityDefault() {
		CompletionExpression a = new CompletionExpression();
		CompletionExpression b = new CompletionExpression();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		CompletionExpression a = new CompletionExpression();
		CompletionExpression b = new CompletionExpression();
		a.setObjectReference(someVarRef("i"));
		a.setToken("t");
		a.setTypeReference(Names.getUnknownType());
		b.setObjectReference(someVarRef("i"));
		b.setToken("t");
		b.setTypeReference(Names.getUnknownType());

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentObjectReference() {
		CompletionExpression a = new CompletionExpression();
		CompletionExpression b = new CompletionExpression();
		a.setObjectReference(someVarRef("i"));
		b.setObjectReference(someVarRef("j"));

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentToken() {
		CompletionExpression a = new CompletionExpression();
		CompletionExpression b = new CompletionExpression();
		a.setToken("i");
		b.setToken("j");

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentTypeReference() {
		CompletionExpression a = new CompletionExpression();
		CompletionExpression b = new CompletionExpression();
		a.setTypeReference(Names.getUnknownType());
		b.setTypeReference(Names.newType("System.Int32, mscore, 4.0.0.0"));

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		CompletionExpression sut = new CompletionExpression();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testWithReturnIsImplemented() {
		// TODO : Visitor Test
	}

	@Test
	public void toStringIsImplemented() {
		ToStringAssert.assertToStringUtils(new CompletionExpression());
	}
}