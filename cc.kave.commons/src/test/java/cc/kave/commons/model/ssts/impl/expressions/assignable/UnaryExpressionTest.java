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

import cc.kave.commons.model.ssts.expressions.assignable.UnaryOperator;
import cc.kave.commons.model.ssts.impl.SSTTestHelper;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.testcommons.ToStringAssert;

public class UnaryExpressionTest {

	@Test
	public void DefaultValues() {
		UnaryExpression sut = new UnaryExpression();
		Assert.assertEquals(sut.getOperator(), UnaryOperator.Unknown);
		Assert.assertEquals(sut.getOperand(), new UnknownExpression());
		Assert.assertNotEquals(0, sut.hashCode());
		Assert.assertNotEquals(1, sut.hashCode());
	}

	@Test

	public void SettingValues() {
		UnaryExpression sut = new UnaryExpression();
		sut.setOperand(new ConstantValueExpression());
		sut.setOperator(UnaryOperator.Complement);
		Assert.assertEquals(sut.getOperator(), UnaryOperator.Complement);
		Assert.assertEquals(sut.getOperand(), new ConstantValueExpression());
	}

	@Test

	public void Equality_Default() {
		UnaryExpression a = new UnaryExpression();
		UnaryExpression b = new UnaryExpression();
		Assert.assertEquals(a, b);
		Assert.assertEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void Equality_ReallyTheSame() {
		UnaryExpression a = new UnaryExpression();
		a.setOperand(new ConstantValueExpression());
		a.setOperator(UnaryOperator.Complement);
		UnaryExpression b = new UnaryExpression();
		b.setOperand(new ConstantValueExpression());
		b.setOperator(UnaryOperator.Complement);
		Assert.assertEquals(a, b);
		Assert.assertEquals(a.hashCode(), b.hashCode());
	}

	@Test

	public void Equality_DifferentOperator() {
		UnaryExpression a = new UnaryExpression();
		a.setOperator(UnaryOperator.Complement);
		UnaryExpression b = new UnaryExpression();
		Assert.assertNotEquals(a, b);
		Assert.assertNotEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void Equality_DifferentOperand() {
		UnaryExpression a = new UnaryExpression();
		a.setOperand(new ConstantValueExpression());
		UnaryExpression b = new UnaryExpression();
		Assert.assertNotEquals(a, b);
		Assert.assertNotEquals(a.hashCode(), b.hashCode());
	}

	@Test

	public void VisitorIsImplemented() {
		UnaryExpression sut = new UnaryExpression();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void NumberingOfEnumIsStable() {
		// IMPORTANT! do not change any of these because it will affect
		// serialization

		Assert.assertEquals(0, (int) UnaryOperator.Unknown.ordinal());

		// Logical
		Assert.assertEquals(1, (int) UnaryOperator.Not.ordinal());

		// Arithmetic
		Assert.assertEquals(2, (int) UnaryOperator.PreIncrement.ordinal());
		Assert.assertEquals(3, (int) UnaryOperator.PostIncrement.ordinal());
		Assert.assertEquals(4, (int) UnaryOperator.PreDecrement.ordinal());
		Assert.assertEquals(5, (int) UnaryOperator.PostDecrement.ordinal());
		Assert.assertEquals(6, (int) UnaryOperator.Plus.ordinal());
		Assert.assertEquals(7, (int) UnaryOperator.Minus.ordinal());

		// Bitwise
		Assert.assertEquals(8, (int) UnaryOperator.Complement.ordinal());
	}

	@Test
	public void toStringIsImplemented() {
		ToStringAssert.assertToStringUtils(new UnaryExpression());
	}
}