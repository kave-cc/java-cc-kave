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
package cc.kave.commons.model.ssts.impl.expressions.simple;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import cc.kave.commons.model.ssts.impl.SSTBaseTest;
import cc.kave.commons.model.ssts.impl.SSTTestHelper;
import cc.kave.commons.model.ssts.impl.references.UnknownReference;
import cc.kave.testcommons.ToStringAssert;

public class ReferenceExpressionTest extends SSTBaseTest {

	@Test
	public void testDefaultValues() {
		ReferenceExpression sut = new ReferenceExpression();

		assertThat(new UnknownReference(), equalTo(sut.getReference()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		ReferenceExpression sut = new ReferenceExpression();
		sut.setReference(someVarRef("a"));

		assertThat(someVarRef("a"), equalTo(sut.getReference()));
	}

	@Test
	public void testEqualityDefault() {
		ReferenceExpression a = new ReferenceExpression();
		ReferenceExpression b = new ReferenceExpression();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		ReferenceExpression a = new ReferenceExpression();
		ReferenceExpression b = new ReferenceExpression();
		a.setReference(someVarRef("a"));
		b.setReference(someVarRef("a"));

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentReference() {
		ReferenceExpression a = new ReferenceExpression();
		ReferenceExpression b = new ReferenceExpression();
		a.setReference(someVarRef("a"));
		b.setReference(someVarRef("b"));

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		ReferenceExpression sut = new ReferenceExpression();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testWithReturnIsImplemented() {
		// TODO : Visitor Test
	}

	@Test
	public void toStringIsImplemented() {
		ToStringAssert.assertToStringUtils(new ReferenceExpression());
	}
}