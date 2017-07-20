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
package cc.kave.commons.utils.ssts.sstprintingvisitortestsuite;

import org.junit.Test;

import com.google.common.collect.Lists;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.ssts.expressions.assignable.BinaryOperator;
import cc.kave.commons.model.ssts.expressions.assignable.CastOperator;
import cc.kave.commons.model.ssts.expressions.assignable.UnaryOperator;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.expressions.assignable.BinaryExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.CastExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.CompletionExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.ComposedExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.IfElseExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.IndexAccessExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.LambdaExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.TypeCheckExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.UnaryExpression;
import cc.kave.commons.model.ssts.impl.expressions.loopheader.LoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.NullExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ReferenceExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.commons.model.ssts.impl.statements.BreakStatement;
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;

public class ExpressionPrinterTest extends SSTPrintingVisitorBaseTest {
	@Test
	public void nullExpression() {
		assertPrint(new NullExpression(), "null");
	}

	@Test
	public void constantValueExpression_WithoutValue() {
		assertPrint(constant(""), "\"...\"");
	}

	@Test
	public void constantValueExpression_WithString() {
		assertPrint(constant("val"), "\"val\"");
	}

	@Test
	public void constantValueExpression_NullLiteralIsUsedAsString() {
		assertPrint(constant("null"), "\"null\"");
	}

	@Test
	public void constantValueExpression_WithInt() {
		assertPrint(constant("1"), "1");
	}

	@Test
	public void constantValueExpression_WithFloat() {
		assertPrint(constant("0.123"), "0.123");
	}

	@Test
	public void constantValueExpression_WithBoolTrue() {
		assertPrint(constant("true"), "true");
	}

	@Test
	public void constantValueExpression_WithBoolFalse() {
		assertPrint(constant("false"), "false");
	}

	@Test
	public void invocationExpression_ConstantValue() {
		InvocationExpression sst = new InvocationExpression();
		sst.setReference(varRef("this"));
		sst.setMethodName(Names.newMethod("[R,P] [D,P].M([T,P] p)"));
		sst.getParameters().add(constant("1"));

		assertPrint(sst, "this.M(1)");
	}

	@Test
	public void invocationExpression_NullValue() {
		InvocationExpression sst = new InvocationExpression();
		sst.setReference(varRef("this"));
		sst.setMethodName(Names.newMethod("[R,P] [D,P].M([T,P] p)"));
		sst.getParameters().add(new NullExpression());

		assertPrint(sst, "this.M(null)");
	}

	@Test
	public void invocationExpression_Static() {
		InvocationExpression sst = new InvocationExpression();
		sst.setReference(varRef("should be ignored anyways"));
		sst.setMethodName(Names.newMethod("static [R,P] [D,P].M([T,P] p)"));
		sst.getParameters().add(new NullExpression());

		assertPrint(sst, "D.M(null)");
	}

	@Test
	public void ifElseExpression() {
		IfElseExpression sst = new IfElseExpression();
		sst.setCondition(constant("true"));
		sst.setThenExpression(constant("1"));
		sst.setElseExpression(constant("2"));

		assertPrint(sst, "(true) ? 1 : 2");
	}

	@Test
	public void referenceExpression() {
		ReferenceExpression sst = new ReferenceExpression();
		sst.setReference(varRef("variable"));
		assertPrint(sst, "variable");
	}

	@Test
	public void composedExpression() {
		ComposedExpression sst = new ComposedExpression();
		sst.getReferences().add(varRef("a"));
		sst.getReferences().add(varRef("b"));
		sst.getReferences().add(varRef("c"));

		assertPrint(sst, "composed(a, b, c)");
	}

	@Test
	public void loopHeaderBlockExpression() {
		LoopHeaderBlockExpression sst = new LoopHeaderBlockExpression();
		sst.getBody().add(new ContinueStatement());
		sst.getBody().add(new BreakStatement());

		assertPrint(sst, "", "{", "    continue;", "    break;", "}");
	}

	@Test
	public void unknownExpression() {
		UnknownExpression sst = new UnknownExpression();
		assertPrint(sst, "???");
	}

	@Test
	public void completionExpression_OnNothing() {
		CompletionExpression sst = new CompletionExpression();
		assertPrint(sst, "$");
	}

	@Test
	public void completionExpression_OnToken() {
		CompletionExpression sst = new CompletionExpression();
		sst.setToken("t");
		assertPrint(sst, "t$");
	}

	@Test
	public void completionExpression_OnVariableReference() {
		CompletionExpression sst = new CompletionExpression();
		sst.setObjectReference(varRef("o"));
		assertPrint(sst, "o.$");
	}

	@Test
	public void completionExpression_OnVariableReferenceWithToken() {
		CompletionExpression sst = new CompletionExpression();
		sst.setObjectReference(varRef("o"));
		sst.setToken("t");
		assertPrint(sst, "o.t$");
	}

	@Test
	public void completionExpression_OnTypeReference() {
		CompletionExpression sst = new CompletionExpression();
		sst.setTypeReference(Names.newType("T,P"));
		assertPrint(sst, "T.$");
	}

	@Test
	public void completionExpression_OnTypeReferenceWithToken() {
		CompletionExpression sst = new CompletionExpression();
		sst.setTypeReference(Names.newType("T,P"));
		sst.setToken("t");
		assertPrint(sst, "T.t$");
	}

	@Test
	public void completionExpression_OnTypeReference_GenericType() {
		CompletionExpression sst = new CompletionExpression();
		sst.setTypeReference(Names.newType("T`1[[G -> A,P]],P"));
		assertPrint(sst, "T<A>.$");
	}

	@Test
	public void completionExpression_OnTypeReference_UnresolvedGenericType() {
		CompletionExpression sst = new CompletionExpression();
		sst.setTypeReference(Names.newType("T`1[[G]],P"));
		assertPrint(sst, "T<G>.$");
	}

	@Test
	public void lambdaExpression() {
		LambdaExpression sst = new LambdaExpression();
		sst.setName(Names.newLambda("[T,P]([C, A] p1, [C, B] p2)"));
		sst.getBody().add(new ContinueStatement());
		sst.getBody().add(new BreakStatement());

		assertPrint(sst, "(C p1, C p2) =>", "{", "    continue;", "    break;", "}");
	}

	@Test
	public void lambdaExpression_NoParametersAndEmptyBody() {
		LambdaExpression sst = new LambdaExpression();

		assertPrint(sst, "() => { }");
	}

	@Test
	public void binaryExpression() {
		// TODO: test more operations
		BinaryExpression sst = new BinaryExpression();
		sst.setLeftOperand(constant("1"));
		sst.setRightOperand(constant("1"));
		sst.setOperator(BinaryOperator.Equal);
		assertPrint(sst, "1 == 1");
	}

	@Test
	public void testCastExpression() {
		CastExpression sst = new CastExpression();
		sst.setReference(SSTUtil.variableReference("x"));
		assertPrint(sst, "(?) x");
	}

	@Test
	public void testCastExpressionSafe() {
		CastExpression sst = new CastExpression();
		sst.setOperator(CastOperator.SafeCast);
		sst.setReference(SSTUtil.variableReference("x"));
		assertPrint(sst, "x as ?");
	}

	@Test
	public void testIndexAccessExpression() {
		IndexAccessExpression sst = new IndexAccessExpression();
		sst.setIndices(Lists.newArrayList(constant("1"), constant("2")));
		sst.setReference(varRef("x"));
		assertPrint(sst, "x[1,2]");
	}

	@Test
	public void testTypeCheckExpression() {
		TypeCheckExpression sst = new TypeCheckExpression();
		sst.setReference(varRef("x"));
		assertPrint(sst, "x instanceof ?");
	}

	@Test
	public void testDefaultUnaryExpression() {
		UnaryExpression sst = new UnaryExpression();
		sst.setOperand(constant("2"));
		assertPrint(sst, "?2");
	}

	@Test
	public void testPostDecrementUnaryExpression() {
		// TODO: Test ALL Operators
		UnaryExpression sst = new UnaryExpression();
		sst.setOperand(constant("2"));
		sst.setOperator(UnaryOperator.PostDecrement);
		assertPrint(sst, "2--");
	}
}