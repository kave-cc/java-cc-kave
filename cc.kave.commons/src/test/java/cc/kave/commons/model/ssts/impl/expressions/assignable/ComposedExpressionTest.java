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

import cc.kave.commons.model.ssts.impl.SSTBaseTest;
import cc.kave.commons.model.ssts.impl.SSTTestHelper;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.testcommons.ToStringAssert;

public class ComposedExpressionTest extends SSTBaseTest {

	@Test
	public void testDefaultValues() {
		ComposedExpression sut = new ComposedExpression();

		assertThat(new ArrayList<IVariableReference>(), equalTo(sut.getReferences()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		ComposedExpression sut = new ComposedExpression();
		String[] str = { "a" };
		sut.setReferences(refs(str));
	}

	@Test
	public void testEqualityDefault() {
		ComposedExpression a = new ComposedExpression();
		ComposedExpression b = new ComposedExpression();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		ComposedExpression a = new ComposedExpression();
		ComposedExpression b = new ComposedExpression();
		String[] str = { "a" };
		a.setReferences(refs(str));
		b.setReferences(refs(str));

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentReferences() {
		ComposedExpression a = new ComposedExpression();
		ComposedExpression b = new ComposedExpression();
		String[] str = { "a" };
		String[] str2 = { "b" };
		a.setReferences(refs(str));
		b.setReferences(refs(str2));

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		ComposedExpression sut = new ComposedExpression();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testWithReturnIsImplemented() {
		// TODO : Visitor Test
	}

	@Test
	public void toStringIsImplemented() {
		ToStringAssert.assertToStringUtils(new ComposedExpression());
	}
}