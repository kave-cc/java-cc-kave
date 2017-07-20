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

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;

import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.impl.SSTBaseTest;
import cc.kave.commons.model.ssts.impl.SSTTestHelper;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.testcommons.ToStringAssert;

public class IndexAccessExpressionTest extends SSTBaseTest {
	@Test
	public void testDefaultValues() {
		IndexAccessExpression sut = new IndexAccessExpression();
		Assert.assertEquals(new VariableReference(), sut.getReference());
		Assert.assertEquals(new ArrayList<ISimpleExpression>(), sut.getIndices());
		Assert.assertNotEquals(0, sut.hashCode());
		Assert.assertNotEquals(1, sut.hashCode());
	}

	@Test
	public void testSettingValues() {
		IndexAccessExpression sut = new IndexAccessExpression();
		sut.setReference(someVarRef());
		sut.setIndices(Lists.newArrayList(new ConstantValueExpression()));

		Assert.assertEquals(someVarRef(), sut.getReference());
		Assert.assertEquals(Lists.newArrayList(new ConstantValueExpression()), sut.getIndices());
	}

	@Test
	public void testEquality_Default() {
		IndexAccessExpression a = new IndexAccessExpression();
		IndexAccessExpression b = new IndexAccessExpression();
		Assert.assertEquals(a, b);
		Assert.assertEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void testEquality_ReallyTheSame() {
		IndexAccessExpression a = new IndexAccessExpression();
		a.setReference(someVarRef());
		a.setIndices(Lists.newArrayList(new ConstantValueExpression()));

		IndexAccessExpression b = new IndexAccessExpression();
		b.setReference(someVarRef());
		b.setIndices(Lists.newArrayList(new ConstantValueExpression()));

		Assert.assertEquals(a, b);
		Assert.assertEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void testEquality_DifferentIndex() {
		ConstantValueExpression constant1 = new ConstantValueExpression();
		ConstantValueExpression constant2 = new ConstantValueExpression();
		constant1.setValue("1");
		constant2.setValue("2");
		IndexAccessExpression a = new IndexAccessExpression();
		a.setReference(someVarRef());
		a.setIndices(Lists.newArrayList(constant1));

		IndexAccessExpression b = new IndexAccessExpression();
		b.setReference(someVarRef());
		b.setIndices(Lists.newArrayList(constant2));

		Assert.assertNotEquals(a, b);
		Assert.assertNotEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void testEquality_DifferentVarRef() {
		IndexAccessExpression a = new IndexAccessExpression();
		a.setReference(someVarRef("i"));
		a.setIndices(Lists.newArrayList(new ConstantValueExpression()));

		IndexAccessExpression b = new IndexAccessExpression();
		b.setReference(someVarRef("j"));
		b.setIndices(Lists.newArrayList(new ConstantValueExpression()));

		Assert.assertNotEquals(a, b);
		Assert.assertNotEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void testVisitorIsImplemented() {
		IndexAccessExpression sut = new IndexAccessExpression();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void toStringIsImplemented() {
		ToStringAssert.assertToStringUtils(new IndexAccessExpression());
	}
}