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
package cc.kave.commons.model.ssts.impl.references;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;

import cc.kave.commons.model.ssts.expressions.assignable.IIndexAccessExpression;
import cc.kave.commons.model.ssts.impl.SSTTestHelper;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.expressions.assignable.IndexAccessExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.testcommons.ToStringAssert;

public class IndexAccessReferenceTest {

	private static IIndexAccessExpression someIndexAccess() {
		IndexAccessExpression expr = new IndexAccessExpression();
		expr.setReference(SSTUtil.variableReference("arr"));
		expr.setIndices(Lists.newArrayList(new ConstantValueExpression()));
		return expr;
	}

	@Test
	public void testDefaultValues() {
		IndexAccessReference sut = new IndexAccessReference();
		Assert.assertEquals(new IndexAccessExpression(), sut.getExpression());
		Assert.assertNotEquals(0, sut.hashCode());
		Assert.assertNotEquals(1, sut.hashCode());
	}

	@Test
	public void testSettingValues() {
		IndexAccessReference sut = new IndexAccessReference();
		sut.setExpression(someIndexAccess());
		Assert.assertEquals(someIndexAccess(), sut.getExpression());
	}

	@Test
	public void testEquality_Default() {
		IndexAccessReference a = new IndexAccessReference();
		IndexAccessReference b = new IndexAccessReference();
		Assert.assertEquals(a, b);
		Assert.assertEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void testEquality_ReallyTheSame() {
		IndexAccessReference a = new IndexAccessReference();
		a.setExpression(someIndexAccess());
		IndexAccessReference b = new IndexAccessReference();
		b.setExpression(someIndexAccess());
		Assert.assertEquals(a, b);
		Assert.assertEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void testEquality_DifferentName() {
		IndexAccessReference a = new IndexAccessReference();
		a.setExpression(someIndexAccess());
		IndexAccessReference b = new IndexAccessReference();
		Assert.assertNotEquals(a, b);
		Assert.assertNotEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void testVisitorIsImplemented() {
		IndexAccessReference sut = new IndexAccessReference();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void toStringIsImplemented() {
		ToStringAssert.assertToStringUtils(new IndexAccessReference());
	}
}