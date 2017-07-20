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
package cc.kave.commons.model.ssts.impl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.google.common.collect.Lists;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.impl.blocks.LockBlock;
import cc.kave.commons.model.ssts.impl.expressions.assignable.ComposedExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ReferenceExpression;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import cc.kave.commons.model.ssts.impl.statements.VariableDeclaration;
import cc.kave.commons.model.ssts.references.IVariableReference;

public class SSTUtilTest {
	@Test
	public void testDeclare() {
		VariableDeclaration actual = (VariableDeclaration) SSTUtil.declare("a", Names.getUnknownType());
		VariableDeclaration expected = new VariableDeclaration();
		expected.setType(Names.getUnknownType());
		expected.setReference(ref("a"));

		assertThat(expected, equalTo(actual));
	}

	@Test
	public void testReturn() {
		ReturnStatement actual = (ReturnStatement) SSTUtil.returnStatement(new ConstantValueExpression());
		ReturnStatement expected = new ReturnStatement();
		expected.setExpression(new ConstantValueExpression());

		assertThat(expected, equalTo(actual));
	}

	@Test
	public void testReturnVariable() {
		ReturnStatement actual = (ReturnStatement) SSTUtil.returnVariable("a");
		ReturnStatement expected = new ReturnStatement();
		ReferenceExpression refExpr = new ReferenceExpression();
		VariableReference varRef = new VariableReference();
		varRef.setIdentifier("a");
		refExpr.setReference(varRef);
		expected.setExpression(refExpr);

		assertThat(expected, equalTo(actual));
	}

	@Test
	public void testReferenceExprToVariable() {
		ReferenceExpression actual = (ReferenceExpression) SSTUtil.referenceExprToVariable("a");
		ReferenceExpression expected = new ReferenceExpression();
		VariableReference varRef = new VariableReference();
		varRef.setIdentifier("a");
		expected.setReference(varRef);

		assertThat(expected, equalTo(actual));
	}

	@Test
	public void testComposedExpression() {
		ComposedExpression actual = (ComposedExpression) SSTUtil.composedExpression("a", "b");
		ComposedExpression expected = new ComposedExpression();
		expected.setReferences(Lists.newArrayList(ref("a"), ref("b")));

		assertThat(expected, equalTo(actual));
	}

	@Test
	public void testSettingValues() {
		InvocationExpression a = (InvocationExpression) SSTUtil.invocationExpression("a1", getMethod("A2"),
				Lists.newArrayList(refs("a3")).iterator());
		VariableReference varRef = new VariableReference();
		varRef.setIdentifier("a1");

		assertThat(varRef, equalTo(a.getReference()));
		assertThat(getMethod("A2"), equalTo(a.getMethodName()));
		assertThat(Lists.newArrayList(refs("a3")), equalTo(a.getParameters()));
	}

	@Test
	public void testInvocationExpressionStatic() {
		InvocationExpression a = (InvocationExpression) SSTUtil.invocationExpression(getStaticMethod("B2"),
				Lists.newArrayList(refs("c2")).iterator());

		assertThat(new VariableReference(), equalTo(a.getReference()));
		assertThat(getStaticMethod("B2"), equalTo(a.getMethodName()));
		assertThat(Lists.newArrayList(refs("c2")), equalTo(a.getParameters()));
	}

	@Test
	public void testInvocationExpressionNonStatic() {
		InvocationExpression a = (InvocationExpression) SSTUtil.invocationExpression("a1", getMethod("B1"),
				Lists.newArrayList(refs("c1")).iterator());
		assertThat(SSTUtil.variableReference("a1"), equalTo(a.getReference()));
		assertThat(getMethod("B1"), equalTo(a.getMethodName()));
		assertThat(Lists.newArrayList(refs("c1")), equalTo(a.getParameters()));
	}

	@Test
	public void testInvocationStatementStatic() {
		ExpressionStatement actual = (ExpressionStatement) SSTUtil.invocationStatement(getStaticMethod("B2"),
				Lists.newArrayList(refs("c2")).iterator());
		ExpressionStatement expected = new ExpressionStatement();
		InvocationExpression expr = new InvocationExpression();
		expr.setMethodName(getStaticMethod("B2"));
		expr.setParameters(Lists.newArrayList(varRefExpr("c2")));
		expected.setExpression(expr);

		assertThat(expected, equalTo(actual));
	}

	@Test
	public void testInvocationStatementNonStatic() {
		ExpressionStatement actual = (ExpressionStatement) SSTUtil.invocationStatement("a", getMethod("B2"),
				Lists.newArrayList(refs("c2")).iterator());
		ExpressionStatement expected = new ExpressionStatement();
		InvocationExpression expr = new InvocationExpression();
		VariableReference varRef = new VariableReference();
		varRef.setIdentifier("a");
		expr.setReference(varRef);
		expr.setMethodName(getMethod("B2"));
		expr.setParameters(Lists.newArrayList(varRefExpr("c2")));
		expected.setExpression(expr);

		assertThat(expected, equalTo(actual));
	}

	@Test
	public void testLockBlock() {
		LockBlock actual = (LockBlock) SSTUtil.lockBlock("a");
		LockBlock expected = new LockBlock();
		VariableReference varRef = new VariableReference();
		varRef.setIdentifier("a");
		expected.setReference(varRef);

		assertThat(expected, equalTo(actual));
	}

	/*
	 * @Test(expected = AssertionError.class) public void
	 * testCustomConstructorNonStaticAssert() {
	 * SSTUtil.invocationExpression("a1", getStaticMethod("B1"),
	 * Lists.newArrayList(refs("c1")).iterator()); }
	 * 
	 * @Test(expected = AssertionError.class) public void
	 * testCustomConstructorStaticAssert() {
	 * SSTUtil.invocationExpression(getMethod("B2"),
	 * Lists.newArrayList(refs("c2")).iterator()); }
	 */

	private static IVariableReference ref(String id) {
		VariableReference ref = new VariableReference();
		ref.setIdentifier(id);
		return ref;
	}

	private static IMethodName getMethod(String simpleName) {
		String methodName = "[p:string] [p:string]." + simpleName + "()";
		return Names.newMethod(methodName);
	}

	private static IMethodName getStaticMethod(String simpleName) {
		String methodName = "static [p:string] [p:string]" + simpleName + "()";
		return Names.newMethod(methodName);
	}

	private static ISimpleExpression varRefExpr(String id) {
		ReferenceExpression refExpr = new ReferenceExpression();
		VariableReference varRef = new VariableReference();
		varRef.setIdentifier("id");
		refExpr.setReference(varRef);
		return refExpr;
	}

	private static ISimpleExpression[] refs(String... ids) {
		ISimpleExpression[] output = new ISimpleExpression[ids.length];
		for (int i = 0; i < ids.length; i++) {
			ReferenceExpression ref = new ReferenceExpression();
			VariableReference varRef = new VariableReference();
			varRef.setIdentifier("id");
			ref.setReference(varRef);
			output[i] = ref;
		}
		return output;
	}
}