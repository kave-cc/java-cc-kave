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

import org.junit.Assert;
import org.junit.Test;

import cc.kave.commons.model.ssts.expressions.assignable.BinaryOperator;
import cc.kave.commons.model.ssts.impl.SSTTestHelper;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ReferenceExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.testcommons.ToStringAssert;

public class BinaryExpressionTest {
	@Test
	public void DefaultValues() {
		BinaryExpression sut = new BinaryExpression();
		Assert.assertEquals(BinaryOperator.Unknown, sut.getOperator());
		Assert.assertEquals(new UnknownExpression(), sut.getLeftOperand());
		Assert.assertEquals(new UnknownExpression(), sut.getRightOperand());
		Assert.assertNotEquals(0, sut.hashCode());
		Assert.assertNotEquals(1, sut.hashCode());
	}

	@Test

	public void SettingValues() {
		BinaryExpression sut = new BinaryExpression();
		sut.setLeftOperand(new ConstantValueExpression());
		sut.setOperator(BinaryOperator.And);
		sut.setRightOperand(new ReferenceExpression());
		Assert.assertEquals(BinaryOperator.And, sut.getOperator());
		Assert.assertEquals(new ConstantValueExpression(), sut.getLeftOperand());
		Assert.assertEquals(new ReferenceExpression(), sut.getRightOperand());
	}

	@Test

	public void Equality_Default() {
		BinaryExpression a = new BinaryExpression();
		BinaryExpression b = new BinaryExpression();
		Assert.assertEquals(a, b);
		Assert.assertEquals(a.hashCode(), b.hashCode());
	}

	@Test

	public void Equality_ReallyTheSame() {
		BinaryExpression a = new BinaryExpression();
		a.setLeftOperand(new ConstantValueExpression());
		a.setOperator(BinaryOperator.And);
		a.setRightOperand(new ReferenceExpression());
		BinaryExpression b = new BinaryExpression();
		b.setLeftOperand(new ConstantValueExpression());
		b.setOperator(BinaryOperator.And);
		b.setRightOperand(new ReferenceExpression());
		Assert.assertEquals(a, b);
		Assert.assertEquals(a.hashCode(), b.hashCode());
	}

	@Test

	public void Equality_DifferentLeft() {
		BinaryExpression a = new BinaryExpression();
		a.setLeftOperand(new ConstantValueExpression());
		BinaryExpression b = new BinaryExpression();
		Assert.assertNotEquals(a, b);
		Assert.assertNotEquals(a.hashCode(), b.hashCode());
	}

	@Test

	public void Equality_DifferentOp() {
		BinaryExpression a = new BinaryExpression();
		a.setOperator(BinaryOperator.And);
		BinaryExpression b = new BinaryExpression();
		Assert.assertNotEquals(a, b);
		Assert.assertNotEquals(a.hashCode(), b.hashCode());
	}

	@Test

	public void Equality_DifferentRight() {
		BinaryExpression a = new BinaryExpression();
		a.setRightOperand(new ReferenceExpression());
		BinaryExpression b = new BinaryExpression();
		Assert.assertNotEquals(a, b);
		Assert.assertNotEquals(a.hashCode(), b.hashCode());
	}

	@Test

	public void VisitorIsImplemented() {
		BinaryExpression sut = new BinaryExpression();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test

	public void NumberingOfEnumIsStable() {
		// IMPORTANT! do not change any of these because it will affect
		// serialization

		Assert.assertEquals(0, (int) BinaryOperator.Unknown.ordinal());

		// Logical
		Assert.assertEquals(1, (int) BinaryOperator.LessThan.ordinal());
		Assert.assertEquals(2, (int) BinaryOperator.LessThanOrEqual.ordinal());
		Assert.assertEquals(3, (int) BinaryOperator.Equal.ordinal());
		Assert.assertEquals(4, (int) BinaryOperator.GreaterThanOrEqual.ordinal());
		Assert.assertEquals(5, (int) BinaryOperator.GreaterThan.ordinal());
		Assert.assertEquals(6, (int) BinaryOperator.NotEqual.ordinal());
		Assert.assertEquals(7, (int) BinaryOperator.And.ordinal());
		Assert.assertEquals(8, (int) BinaryOperator.Or.ordinal());

		// Arithmetic
		Assert.assertEquals(9, (int) BinaryOperator.Plus.ordinal());
		Assert.assertEquals(10, (int) BinaryOperator.Minus.ordinal());
		Assert.assertEquals(11, (int) BinaryOperator.Multiply.ordinal());
		Assert.assertEquals(12, (int) BinaryOperator.Divide.ordinal());
		Assert.assertEquals(13, (int) BinaryOperator.Modulo.ordinal());

		// Bitwise
		Assert.assertEquals(14, (int) BinaryOperator.BitwiseAnd.ordinal());
		Assert.assertEquals(15, (int) BinaryOperator.BitwiseOr.ordinal());
		Assert.assertEquals(16, (int) BinaryOperator.BitwiseXor.ordinal());
		Assert.assertEquals(17, (int) BinaryOperator.ShiftLeft.ordinal());
		Assert.assertEquals(18, (int) BinaryOperator.ShiftRight.ordinal());
	}

	@Test
	public void toStringIsImplemented() {
		ToStringAssert.assertToStringUtils(new BinaryExpression());
	}
}