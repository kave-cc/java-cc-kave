/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package cc.kave.commons.model.ssts.impl.visitor;

import static cc.kave.commons.utils.ssts.SSTUtils.invExpr;
import static cc.kave.commons.utils.ssts.SSTUtils.varDecl;
import static cc.kave.commons.utils.ssts.SSTUtils.varRef;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.CatchBlockKind;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.BinaryOperator;
import cc.kave.commons.model.ssts.expressions.assignable.CastOperator;
import cc.kave.commons.model.ssts.expressions.assignable.UnaryOperator;
import cc.kave.commons.model.ssts.expressions.loopheader.ILoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.blocks.CaseBlock;
import cc.kave.commons.model.ssts.impl.blocks.CatchBlock;
import cc.kave.commons.model.ssts.impl.blocks.DoLoop;
import cc.kave.commons.model.ssts.impl.blocks.ForEachLoop;
import cc.kave.commons.model.ssts.impl.blocks.ForLoop;
import cc.kave.commons.model.ssts.impl.blocks.IfElseBlock;
import cc.kave.commons.model.ssts.impl.blocks.LockBlock;
import cc.kave.commons.model.ssts.impl.blocks.SwitchBlock;
import cc.kave.commons.model.ssts.impl.blocks.TryBlock;
import cc.kave.commons.model.ssts.impl.blocks.UncheckedBlock;
import cc.kave.commons.model.ssts.impl.blocks.UnsafeBlock;
import cc.kave.commons.model.ssts.impl.blocks.UsingBlock;
import cc.kave.commons.model.ssts.impl.blocks.WhileLoop;
import cc.kave.commons.model.ssts.impl.declarations.DelegateDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.EventDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.FieldDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.PropertyDeclaration;
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
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.NullExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ReferenceExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.commons.model.ssts.impl.references.EventReference;
import cc.kave.commons.model.ssts.impl.references.FieldReference;
import cc.kave.commons.model.ssts.impl.references.IndexAccessReference;
import cc.kave.commons.model.ssts.impl.references.MethodReference;
import cc.kave.commons.model.ssts.impl.references.PropertyReference;
import cc.kave.commons.model.ssts.impl.references.UnknownReference;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.Assignment;
import cc.kave.commons.model.ssts.impl.statements.BreakStatement;
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;
import cc.kave.commons.model.ssts.impl.statements.EventSubscriptionStatement;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.kave.commons.model.ssts.impl.statements.GotoStatement;
import cc.kave.commons.model.ssts.impl.statements.LabelledStatement;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import cc.kave.commons.model.ssts.impl.statements.ThrowStatement;
import cc.kave.commons.model.ssts.impl.statements.UnknownStatement;
import cc.kave.commons.model.ssts.impl.statements.VariableDeclaration;
import cc.kave.commons.model.ssts.impl.visitor.inlining.SSTFixture;
import cc.kave.commons.model.ssts.references.IAssignableReference;
import cc.kave.commons.model.ssts.statements.EventSubscriptionOperation;
import cc.kave.commons.model.ssts.visitor.ISSTNode;

public class TypeErasureVisitorTest {

	@Test
	public void sst_defaultWorks() {
		assertErasure(new SST(), new SST());
	}

	@Test
	public void sst_fullAndNonGenericSSTIsNotAltered() {
		assertErasure(new SSTFixture().getSST(), new SSTFixture().getSST());
	}

	@Test
	public void decl_enclosingTypeIsErased() {
		SST in = new SST();
		in.setEnclosingType(someGenericType(1));

		SST expected = new SST();
		expected.setEnclosingType(someErasedType(1));

		assertErasure(expected, in);
	}

	@Test
	public void decl_delegate() {
		DelegateDeclaration in = new DelegateDeclaration();
		in.setName(Names.newType("d:[p:void] [%s].D()", someGenericType(1)).asDelegateTypeName());

		DelegateDeclaration expected = new DelegateDeclaration();
		expected.setName(Names.newType("d:[p:void] [%s].D()", someErasedType(1)).asDelegateTypeName());

		assertErasure(expected, in);
	}

	@Test
	public void decl_events() {
		EventDeclaration in = new EventDeclaration();
		in.setName(Names.newEvent("[p:void] [%s].E()", someGenericType(1)));

		EventDeclaration expected = new EventDeclaration();
		expected.setName(Names.newEvent("[p:void] [%s].E()", someErasedType(1)));

		assertErasure(expected, in);
	}

	@Test
	public void decl_field() {
		FieldDeclaration in = new FieldDeclaration();
		in.setName(Names.newField("[p:int] [%s]._f", someGenericType(1)));

		FieldDeclaration expected = new FieldDeclaration();
		expected.setName(Names.newField("[p:int] [%s]._f", someErasedType(1)));

		assertErasure(expected, in);
	}

	@Test
	public void decl_method() {
		MethodDeclaration in = new MethodDeclaration();
		in.setName(Names.newMethod("[T,P] [%s].M()", someGenericType(1)));
		in.setEntryPoint(true);
		in.getBody().add(someGenericStmt(2));

		MethodDeclaration expected = new MethodDeclaration();
		expected.setName(Names.newMethod("[T,P] [%s].M()", someErasedType(1)));
		expected.setEntryPoint(true);
		expected.getBody().add(someErasedStmt(2));

		assertErasure(expected, in);
	}

	@Test
	public void decl_property() {
		PropertyDeclaration in = new PropertyDeclaration();
		in.setName(Names.newProperty("get set [p:int] [%s].P()", someGenericType(1)));
		in.getGet().add(someGenericStmt(2));
		in.getSet().add(someGenericStmt(3));

		PropertyDeclaration expected = new PropertyDeclaration();
		expected.setName(Names.newProperty("get set [p:int] [%s].P()", someErasedType(1)));
		expected.getGet().add(someErasedStmt(2));
		expected.getSet().add(someErasedStmt(3));

		assertErasure(expected, in);
	}

	@Test
	public void block_do() {
		DoLoop in = new DoLoop();
		in.setCondition(loopHeader(someGenericType(1)));
		in.getBody().add(someGenericStmt(2));

		DoLoop expected = new DoLoop();
		expected.setCondition(loopHeader(someErasedType(1)));
		expected.getBody().add(someErasedStmt(2));

		assertErasure(expected, in);
	}

	@Test
	public void block_foreach() {
		ForEachLoop in = new ForEachLoop();
		in.setDeclaration(varDecl("x", someGenericType(1)));
		in.setLoopedReference(varRef("y"));
		in.getBody().add(someGenericStmt(2));

		ForEachLoop expected = new ForEachLoop();
		expected.setDeclaration(varDecl("x", someErasedType(1)));
		expected.setLoopedReference(varRef("y"));
		expected.getBody().add(someErasedStmt(2));

		assertErasure(expected, in);
	}

	@Test
	public void block_for() {
		ForLoop in = new ForLoop();
		in.getInit().add(someGenericStmt(1));
		in.setCondition(loopHeader(someGenericType(2)));
		in.getStep().add(someGenericStmt(3));
		in.getBody().add(someGenericStmt(4));

		ForLoop expected = new ForLoop();
		expected.getInit().add(someErasedStmt(1));
		expected.setCondition(loopHeader(someErasedType(2)));
		expected.getStep().add(someErasedStmt(3));
		expected.getBody().add(someErasedStmt(4));

		assertErasure(expected, in);
	}

	@Test
	public void block_if() {
		IfElseBlock in = new IfElseBlock();
		in.setCondition(someGenericSimpleExpr(1));
		in.getThen().add(someGenericStmt(2));
		in.getElse().add(someGenericStmt(3));

		IfElseBlock expected = new IfElseBlock();
		expected.setCondition(someErasedSimpleExpr(1));
		expected.getThen().add(someErasedStmt(2));
		expected.getElse().add(someErasedStmt(3));

		assertErasure(expected, in);
	}

	@Test
	public void block_lock() {
		LockBlock in = new LockBlock();
		in.setReference(varRef("x"));
		in.getBody().add(someGenericStmt(1));

		LockBlock expected = new LockBlock();
		expected.setReference(varRef("x"));
		expected.getBody().add(someErasedStmt(1));

		assertErasure(expected, in);
	}

	@Test
	public void block_switch() {

		CaseBlock icb = new CaseBlock();
		icb.setLabel(someGenericSimpleExpr(1));
		icb.getBody().add(someGenericStmt(2));

		SwitchBlock in = new SwitchBlock();
		in.setReference(varRef("x"));
		in.getSections().add(icb);
		in.getDefaultSection().add(someGenericStmt(3));
		// ---
		CaseBlock ecb = new CaseBlock();
		ecb.setLabel(someErasedSimpleExpr(1));
		ecb.getBody().add(someErasedStmt(2));

		SwitchBlock expected = new SwitchBlock();
		expected.setReference(varRef("x"));
		expected.getSections().add(ecb);
		expected.getDefaultSection().add(someErasedStmt(3));
		// ---
		assertErasure(expected, in);
	}

	@Test
	public void block_try() {
		CatchBlock icb = new CatchBlock();
		icb.setKind(CatchBlockKind.General);
		icb.setParameter(Names.newParameter("[%s] p", someGenericType(1)));
		icb.getBody().add(someGenericStmt(2));

		TryBlock in = new TryBlock();
		in.getBody().add(someGenericStmt(3));
		in.getCatchBlocks().add(icb);
		in.getFinally().add(someGenericStmt(4));
		// ---
		CatchBlock ecb = new CatchBlock();
		ecb.setKind(CatchBlockKind.General);
		ecb.setParameter(Names.newParameter("[%s] p", someErasedType(1)));
		ecb.getBody().add(someErasedStmt(2));

		TryBlock expected = new TryBlock();
		expected.getBody().add(someErasedStmt(3));
		expected.getCatchBlocks().add(ecb);
		expected.getFinally().add(someErasedStmt(4));
		// ---
		assertErasure(expected, in);
	}

	@Test
	public void block_unchecked() {
		UncheckedBlock in = new UncheckedBlock();
		in.getBody().add(someGenericStmt(1));

		UncheckedBlock expected = new UncheckedBlock();
		expected.getBody().add(someErasedStmt(1));

		assertErasure(expected, in);
	}

	@Test
	public void block_unsafe() {
		UnsafeBlock in = new UnsafeBlock();
		UnsafeBlock expected = new UnsafeBlock();
		assertErasure(expected, in);
	}

	@Test
	public void block_using() {
		UsingBlock in = new UsingBlock();
		in.setReference(varRef("x"));
		in.getBody().add(someGenericStmt(1));

		UsingBlock expected = new UsingBlock();
		expected.setReference(varRef("x"));
		expected.getBody().add(someErasedStmt(1));

		assertErasure(expected, in);
	}

	@Test
	public void block_while() {
		WhileLoop in = new WhileLoop();
		in.setCondition(loopHeader(someGenericType(1)));
		in.getBody().add(someGenericStmt(2));

		WhileLoop expected = new WhileLoop();
		expected.setCondition(loopHeader(someErasedType(1)));
		expected.getBody().add(someErasedStmt(2));

		assertErasure(expected, in);
	}

	@Test
	public void stmt_assign() {
		Assignment in = new Assignment();
		in.setReference(someGenericRef(1));
		in.setExpression(invExpr("this", someGenericMethod(2)));

		Assignment expected = new Assignment();
		expected.setReference(someErasedRef(1));
		expected.setExpression(invExpr("this", someErasedMethod(2)));

		assertErasure(expected, in);
	}

	@Test
	public void stmt_break() {
		BreakStatement in = new BreakStatement();
		BreakStatement expected = new BreakStatement();
		assertErasure(expected, in);
	}

	@Test
	public void stmt_continue() {
		ContinueStatement in = new ContinueStatement();
		ContinueStatement expected = new ContinueStatement();
		assertErasure(expected, in);
	}

	@Test
	public void stmt_eventSubscription() {
		EventSubscriptionStatement in = new EventSubscriptionStatement();
		in.setReference(someGenericRef(1));
		in.setOperation(EventSubscriptionOperation.Remove);
		in.setExpression(someGenericSimpleExpr(1));

		EventSubscriptionStatement expected = new EventSubscriptionStatement();
		expected.setReference(someErasedRef(1));
		expected.setOperation(EventSubscriptionOperation.Remove);
		expected.setExpression(someErasedSimpleExpr(1));

		assertErasure(expected, in);
	}

	@Test
	public void stmt_exprStmt() {
		ExpressionStatement in = new ExpressionStatement();
		in.setExpression(someGenericSimpleExpr(1));

		ExpressionStatement expected = new ExpressionStatement();
		expected.setExpression(someErasedSimpleExpr(1));

		assertErasure(expected, in);
	}

	@Test
	public void stmt_goto() {
		GotoStatement in = new GotoStatement();
		in.setLabel("x");

		GotoStatement expected = new GotoStatement();
		expected.setLabel("x");

		assertErasure(expected, in);
	}

	@Test
	public void stmt_label() {
		LabelledStatement in = new LabelledStatement();
		in.setLabel("x");
		in.setStatement(someGenericStmt(1));

		LabelledStatement expected = new LabelledStatement();
		expected.setLabel("x");
		expected.setStatement(someErasedStmt(1));

		assertErasure(expected, in);
	}

	@Test
	public void stmt_return() {
		ReturnStatement in = new ReturnStatement();
		in.setExpression(someGenericSimpleExpr(1));
		in.setIsVoid(true);

		ReturnStatement expected = new ReturnStatement();
		expected.setExpression(someErasedSimpleExpr(1));
		expected.setIsVoid(true);

		assertErasure(expected, in);
	}

	@Test
	public void stmt_throw() {
		ThrowStatement in = new ThrowStatement();
		in.setReference(varRef("x"));
		ThrowStatement expected = new ThrowStatement();
		expected.setReference(varRef("x"));
		assertErasure(expected, in);
	}

	@Test
	public void stmt_unknown() {
		UnknownStatement in = new UnknownStatement();
		UnknownStatement expected = new UnknownStatement();
		assertErasure(expected, in);
	}

	@Test
	public void stmt_varDecl() {
		VariableDeclaration in = new VariableDeclaration();
		in.setType(someGenericType(1));

		VariableDeclaration expected = new VariableDeclaration();
		expected.setType(someErasedType(1));

		assertErasure(expected, in);
	}

	@Test
	public void expr_constant() {
		ConstantValueExpression in = new ConstantValueExpression();
		in.setValue("123");
		ConstantValueExpression expected = new ConstantValueExpression();
		expected.setValue("123");
		assertErasure(expected, in);
	}

	@Test
	public void expr_null() {
		NullExpression in = new NullExpression();
		NullExpression expected = new NullExpression();
		assertErasure(expected, in);
	}

	@Test
	public void expr_refExpr() {
		ReferenceExpression in = new ReferenceExpression();
		in.setReference(someGenericRef(1));
		ReferenceExpression expected = new ReferenceExpression();
		expected.setReference(someErasedRef(1));
		assertErasure(expected, in);
	}

	@Test
	public void expr_unknown() {
		UnknownExpression in = new UnknownExpression();
		UnknownExpression expected = new UnknownExpression();
		assertErasure(expected, in);
	}

	@Test
	public void expr_loopHeaderBlock() {
		LoopHeaderBlockExpression in = new LoopHeaderBlockExpression();
		in.getBody().add(someGenericStmt(1));
		LoopHeaderBlockExpression expected = new LoopHeaderBlockExpression();
		expected.getBody().add(someErasedStmt(1));
		assertErasure(expected, in);
	}

	@Test
	public void expr_binary() {
		BinaryExpression in = new BinaryExpression();
		in.setLeftOperand(someGenericSimpleExpr(1));
		in.setOperator(BinaryOperator.BitwiseXor);
		in.setRightOperand(someGenericSimpleExpr(2));

		BinaryExpression expected = new BinaryExpression();
		expected.setLeftOperand(someErasedSimpleExpr(1));
		expected.setOperator(BinaryOperator.BitwiseXor);
		expected.setRightOperand(someErasedSimpleExpr(2));

		assertErasure(expected, in);
	}

	@Test
	public void expr_castExpr() {
		CastExpression in = new CastExpression();
		in.setReference(varRef("x"));
		in.setOperator(CastOperator.SafeCast);
		in.setTargetType(someGenericType(1));

		CastExpression expected = new CastExpression();
		expected.setReference(varRef("x"));
		expected.setOperator(CastOperator.SafeCast);
		expected.setTargetType(someErasedType(1));

		assertErasure(expected, in);
	}

	@Test
	public void expr_completion() {
		CompletionExpression in = new CompletionExpression();
		in.setObjectReference(varRef("x"));
		in.setToken("t");
		in.setTypeReference(someGenericType(1));

		CompletionExpression expected = new CompletionExpression();
		expected.setObjectReference(varRef("x"));
		expected.setToken("t");
		expected.setTypeReference(someErasedType(1));

		assertErasure(expected, in);
	}

	@Test
	public void expr_composed() {
		ComposedExpression in = new ComposedExpression();
		in.getReferences().add(varRef("x"));
		in.getReferences().add(varRef("y"));

		ComposedExpression expected = new ComposedExpression();
		expected.getReferences().add(varRef("x"));
		expected.getReferences().add(varRef("y"));

		assertErasure(expected, in);
	}

	@Test
	public void expr_ifelse() {
		IfElseExpression in = new IfElseExpression();
		in.setCondition(someGenericSimpleExpr(1));
		in.setThenExpression(someGenericSimpleExpr(2));
		in.setElseExpression(someGenericSimpleExpr(3));

		IfElseExpression expected = new IfElseExpression();
		expected.setCondition(someErasedSimpleExpr(1));
		expected.setThenExpression(someErasedSimpleExpr(2));
		expected.setElseExpression(someErasedSimpleExpr(3));

		assertErasure(expected, in);
	}

	@Test
	public void expr_indexAccess() {
		IndexAccessExpression in = new IndexAccessExpression();
		in.setReference(varRef("x"));
		in.getIndices().add(someGenericSimpleExpr(1));

		IndexAccessExpression expected = new IndexAccessExpression();
		expected.setReference(varRef("x"));
		expected.getIndices().add(someErasedSimpleExpr(1));

		assertErasure(expected, in);
	}

	@Test
	public void expr_invocation() {
		InvocationExpression in = new InvocationExpression();
		in.setReference(varRef("o"));
		in.setMethodName(someGenericMethod(1));
		in.getParameters().add(someGenericSimpleExpr(2));

		InvocationExpression expected = new InvocationExpression();
		expected.setReference(varRef("o"));
		expected.setMethodName(someErasedMethod(1));
		expected.getParameters().add(someErasedSimpleExpr(2));

		assertErasure(expected, in);
	}

	@Test
	public void expr_lambda() {
		LambdaExpression in = new LambdaExpression();
		in.setName(Names.newLambda("[p:void] [%s].()", someGenericType(1)));
		in.getBody().add(someGenericStmt(2));

		LambdaExpression expected = new LambdaExpression();
		expected.setName(Names.newLambda("[p:void] [%s].()", someErasedType(1)));
		expected.getBody().add(someErasedStmt(2));

		assertErasure(expected, in);
	}

	@Test
	public void expr_typecheck() {
		TypeCheckExpression in = new TypeCheckExpression();
		in.setReference(varRef("x"));
		in.setType(someGenericType(1));

		TypeCheckExpression expected = new TypeCheckExpression();
		expected.setReference(varRef("x"));
		expected.setType(someErasedType(1));

		assertErasure(expected, in);
	}

	@Test
	public void expr_unary() {
		UnaryExpression in = new UnaryExpression();
		in.setOperator(UnaryOperator.Plus);
		in.setOperand(someGenericSimpleExpr(1));

		UnaryExpression expected = new UnaryExpression();
		expected.setOperator(UnaryOperator.Plus);
		expected.setOperand(someErasedSimpleExpr(1));

		assertErasure(expected, in);
	}

	@Test
	public void ref_event() {
		String id = "[p:void] [%s].E";

		EventReference in = new EventReference();
		in.setReference(varRef("x"));
		in.setEventName(Names.newEvent(id, someGenericType(1)));

		EventReference expected = new EventReference();
		expected.setReference(varRef("x"));
		expected.setEventName(Names.newEvent(id, someErasedType(1)));

		assertErasure(expected, in);
	}

	@Test
	public void ref_field() {
		String id = "[p:int] [%s]._f";

		FieldReference in = new FieldReference();
		in.setReference(varRef("x"));
		in.setFieldName(Names.newField(id, someGenericType(1)));

		FieldReference expected = new FieldReference();
		expected.setReference(varRef("x"));
		expected.setFieldName(Names.newField(id, someErasedType(1)));

		assertErasure(expected, in);
	}

	@Test
	public void ref_indexAccess() {
		IndexAccessExpression ie = new IndexAccessExpression();
		ie.setReference(varRef("x"));
		ie.getIndices().add(someGenericSimpleExpr(1));
		IndexAccessReference in = new IndexAccessReference();
		in.setExpression(ie);

		IndexAccessExpression ee = new IndexAccessExpression();
		ee.setReference(varRef("x"));
		ee.getIndices().add(someErasedSimpleExpr(1));
		IndexAccessReference expected = new IndexAccessReference();
		expected.setExpression(ee);

		assertErasure(expected, in);
	}

	@Test
	public void ref_method() {
		String id = "[p:void] [%s].M()";

		MethodReference in = new MethodReference();
		in.setReference(varRef("x"));
		in.setMethodName(Names.newMethod(id, someGenericType(1)));

		MethodReference expected = new MethodReference();
		expected.setReference(varRef("x"));
		expected.setMethodName(Names.newMethod(id, someErasedType(1)));

		assertErasure(expected, in);
	}

	@Test
	public void ref_property() {
		String id = "get set [p:int] [%s].P()";

		PropertyReference in = new PropertyReference();
		in.setReference(varRef("x"));
		in.setPropertyName(Names.newProperty(id, someGenericType(1)));

		PropertyReference expected = new PropertyReference();
		expected.setReference(varRef("x"));
		expected.setPropertyName(Names.newProperty(id, someErasedType(1)));

		assertErasure(expected, in);
	}

	@Test
	public void ref_unknown() {
		UnknownReference in = new UnknownReference();
		UnknownReference expected = new UnknownReference();
		assertErasure(expected, in);
	}

	@Test
	public void ref_var() {
		VariableReference in = new VariableReference();
		in.setIdentifier("x");
		VariableReference expected = new VariableReference();
		expected.setIdentifier("x");
		assertErasure(expected, in);
	}

	@Deprecated
	private static IStatement someGenericStmt() {
		VariableDeclaration decl = new VariableDeclaration();
		decl.setType(someGenericType());
		return decl;
	}

	private static IStatement someGenericStmt(int num) {
		VariableDeclaration decl = new VariableDeclaration();
		decl.setType(someGenericType(num));
		return decl;
	}

	@Deprecated
	private static IStatement someErasedStmt() {
		VariableDeclaration decl = new VariableDeclaration();
		decl.setType(someErasedType());
		return decl;
	}

	private static IStatement someErasedStmt(int num) {
		VariableDeclaration decl = new VariableDeclaration();
		decl.setType(someErasedType(num));
		return decl;
	}

	@Deprecated
	private static ITypeName someGenericType() {
		return Names.newType("T`1[[G1->p:int]], P");
	}

	private static ITypeName someGenericType(int num) {
		return Names.newType("T`1[[G1->p:int]], P" + num);
	}

	@Deprecated
	private static ITypeName someErasedType() {
		return Names.newType("T`1[[G1]], P");
	}

	private static ITypeName someErasedType(int num) {
		return Names.newType("T`1[[G1]], P" + num);
	}

	@Deprecated
	private static IMethodName someGenericMethod() {
		return Names.newMethod("[p:void] [%s].m()", someGenericType());
	}

	@Deprecated
	private static IMethodName someErasedMethod() {
		return Names.newMethod("[p:void] [%s].m()", someErasedType());
	}

	private static IMethodName someGenericMethod(int num) {
		return Names.newMethod("[p:void] [%s].m()", someGenericType(num));
	}

	private static IMethodName someErasedMethod(int num) {
		return Names.newMethod("[p:void] [%s].m()", someErasedType(num));
	}

	private static void assertErasure(ISSTNode expected, ISSTNode in) {
		Object actual = erase(in);
		assertNotSame(in, actual);
		assertEquals(expected, actual);
	}

	private static Object erase(ISSTNode n) {
		return n.accept(new TypeErasureVisitor(), null);
	}

	public static ILoopHeaderBlockExpression loopHeader(ITypeName t) {
		LoopHeaderBlockExpression loopHeader = new LoopHeaderBlockExpression();
		loopHeader.getBody().add(varDecl("x", t));
		return loopHeader;
	}

	private static ISimpleExpression someGenericSimpleExpr(int i) {
		ReferenceExpression expr = new ReferenceExpression();
		expr.setReference(someGenericRef(i));
		return expr;
	}

	private static ISimpleExpression someErasedSimpleExpr(int i) {
		ReferenceExpression expr = new ReferenceExpression();
		expr.setReference(someErasedRef(i));
		return expr;
	}

	private static IAssignableReference someGenericRef(int i) {
		FieldReference fr = new FieldReference();
		fr.setReference(varRef("x"));
		fr.setFieldName(Names.newField("[p:int] [%s]._f", someGenericType(i)));
		return fr;
	}

	private static IAssignableReference someErasedRef(int i) {
		FieldReference fr = new FieldReference();
		fr.setReference(varRef("x"));
		fr.setFieldName(Names.newField("[p:int] [%s]._f", someErasedType(i)));
		return fr;
	}
}