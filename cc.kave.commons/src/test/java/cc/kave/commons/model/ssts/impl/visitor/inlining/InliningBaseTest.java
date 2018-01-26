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
package cc.kave.commons.model.ssts.impl.visitor.inlining;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.ICaseBlock;
import cc.kave.commons.model.ssts.blocks.ICatchBlock;
import cc.kave.commons.model.ssts.blocks.IDoLoop;
import cc.kave.commons.model.ssts.blocks.IForEachLoop;
import cc.kave.commons.model.ssts.blocks.IForLoop;
import cc.kave.commons.model.ssts.blocks.IIfElseBlock;
import cc.kave.commons.model.ssts.blocks.ILockBlock;
import cc.kave.commons.model.ssts.blocks.ISwitchBlock;
import cc.kave.commons.model.ssts.blocks.ITryBlock;
import cc.kave.commons.model.ssts.blocks.IUncheckedBlock;
import cc.kave.commons.model.ssts.blocks.IUsingBlock;
import cc.kave.commons.model.ssts.blocks.IWhileLoop;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ILoopHeaderExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.BinaryOperator;
import cc.kave.commons.model.ssts.expressions.assignable.IBinaryExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IComposedExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIfElseExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IUnaryExpression;
import cc.kave.commons.model.ssts.expressions.assignable.UnaryOperator;
import cc.kave.commons.model.ssts.expressions.loopheader.ILoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
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
import cc.kave.commons.model.ssts.impl.expressions.assignable.CompletionExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.ComposedExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.IfElseExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.LambdaExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.UnaryExpression;
import cc.kave.commons.model.ssts.impl.expressions.loopheader.LoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.NullExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ReferenceExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.commons.model.ssts.impl.references.EventReference;
import cc.kave.commons.model.ssts.impl.references.FieldReference;
import cc.kave.commons.model.ssts.impl.references.PropertyReference;
import cc.kave.commons.model.ssts.impl.references.UnknownReference;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.Assignment;
import cc.kave.commons.model.ssts.impl.statements.BreakStatement;
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.kave.commons.model.ssts.impl.statements.GotoStatement;
import cc.kave.commons.model.ssts.impl.statements.LabelledStatement;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import cc.kave.commons.model.ssts.impl.statements.ThrowStatement;
import cc.kave.commons.model.ssts.impl.statements.UnknownStatement;
import cc.kave.commons.model.ssts.impl.statements.VariableDeclaration;
import cc.kave.commons.model.ssts.references.IAssignableReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IExpressionStatement;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;

public class InliningBaseTest {

	public static final ITypeName INTEGER = Names.newType("p:int");
	public static final ITypeName BOOLEAN = Names.newType("p:bool");
	public static final String SIGNATURE = "[p:int] [?].";

	protected IStatement returnVoid() {
		return returnStatement(new NullExpression(), true);
	}

	protected IStatement returnTrue() {
		return returnStatement(constant("true"), false);
	}

	protected IStatement returnFalse() {
		return returnStatement(constant("false"), false);
	}

	protected IStatement label(String label, IStatement statement) {
		LabelledStatement labelled = new LabelledStatement();
		labelled.setLabel(label);
		labelled.setStatement(statement);
		return labelled;
	}

	protected IStatement invocationStatement(String name, IVariableReference reference,
			ISimpleExpression... parameters) {
		return expr(invocationExpr(name, reference, parameters));
	}

	protected IBinaryExpression binary(BinaryOperator operator, ISimpleExpression left, ISimpleExpression right) {
		BinaryExpression expr = new BinaryExpression();
		expr.setLeftOperand(left);
		expr.setOperator(operator);
		expr.setRightOperand(right);
		return expr;
	}

	protected IUnaryExpression unary(UnaryOperator operator, ISimpleExpression operand) {
		UnaryExpression expr = new UnaryExpression();
		expr.setOperator(operator);
		expr.setOperand(operand);
		return expr;
	}

	protected IInvocationExpression invocationExpr(String name, IVariableReference reference,
			ISimpleExpression... parameters) {
		InvocationExpression invocation = new InvocationExpression();
		invocation.setParameters(Lists.newArrayList(parameters));
		if (reference != null)
			invocation.setReference(reference);
		invocation.setMethodName(Names.newMethod(SIGNATURE + name + "()"));
		return invocation;
	}

	protected IStatement invocationStatement(IMethodName name, ISimpleExpression... parameters) {
		return expr(invocationExpr(name, parameters));
	}

	protected IInvocationExpression invocationExpr(IMethodName name, ISimpleExpression... parameters) {
		InvocationExpression invocation = new InvocationExpression();
		invocation.setParameters(Lists.newArrayList(parameters));
		invocation.setMethodName(name);
		return invocation;
	}

	protected IStatement returnStatement(ISimpleExpression expr, boolean isVoid) {
		ReturnStatement statement = new ReturnStatement();
		statement.setIsVoid(isVoid);
		statement.setExpression(expr);
		return statement;
	}

	protected IExpressionStatement expr(IAssignableExpression expr) {
		ExpressionStatement statement = new ExpressionStatement();
		statement.setExpression(expr);
		return statement;
	}

	protected IStatement invocationStatement(String name, ISimpleExpression... parameters) {
		return invocationStatement(name, null, parameters);
	}

	protected IMethodDeclaration declareEntryPoint(String name, IStatement... statements) {
		return declareMethod(name, true, statements);
	}

	protected IMethodDeclaration declareNonEntryPoint(String name, IStatement... statements) {
		return declareMethod(name, false, statements);
	}

	protected IMethodDeclaration declareMethod(IMethodName name, boolean entryPoint, IStatement... statements) {
		MethodDeclaration method = new MethodDeclaration();
		method.setName(name);
		method.setEntryPoint(entryPoint);
		for (IStatement s : statements)
			method.getBody().add(s);
		return method;
	}

	protected IMethodDeclaration declareMethod(String name, boolean entryPoint, IStatement... statements) {
		MethodDeclaration method = new MethodDeclaration();
		method.setName(Names.newMethod(SIGNATURE + name + "()"));
		method.setEntryPoint(entryPoint);
		for (IStatement s : statements)
			method.getBody().add(s);
		return method;
	}

	protected IComposedExpression compose(IVariableReference... refs) {
		ComposedExpression composedExpr = new ComposedExpression();
		composedExpr.setReferences(Lists.newArrayList(refs));
		return composedExpr;
	}

	protected IForLoop forLoop(String var, ILoopHeaderExpression condition, IStatement... body) {
		ForLoop forLoop = new ForLoop();
		forLoop.setInit(Lists.newArrayList(declareVar(var), assign(ref(var), constant("0"))));
		forLoop.setStep(Lists.newArrayList(assign(ref(var), constant("2"))));
		forLoop.setBody(Lists.newArrayList(body));
		forLoop.setCondition(condition);
		return forLoop;
	}

	protected IIfElseBlock simpleIf(List<IStatement> elseStatement, ISimpleExpression condition, IStatement... body) {
		IfElseBlock ifElse = new IfElseBlock();
		ifElse.setCondition(condition);
		ifElse.setThen(Lists.newArrayList(body));
		ifElse.setElse(elseStatement);
		return ifElse;
	}

	protected IDoLoop doLoop(ILoopHeaderExpression condition, IStatement... body) {
		DoLoop doLoop = new DoLoop();
		doLoop.setBody(Lists.newArrayList(body));
		doLoop.setCondition(condition);
		return doLoop;
	}

	protected IForEachLoop forEachLoop(String variable, String ref, IStatement... body) {
		ForEachLoop forEachLoop = new ForEachLoop();
		forEachLoop.setBody(Lists.newArrayList(body));
		forEachLoop.setDeclaration(declareVar(variable));
		forEachLoop.setLoopedReference(ref(ref));
		return forEachLoop;
	}

	protected ILockBlock lockBlock(String identifier, IStatement... body) {
		LockBlock lockBlock = new LockBlock();
		lockBlock.setBody(Lists.newArrayList(body));
		lockBlock.setReference(ref(identifier));
		return lockBlock;
	}

	protected ISwitchBlock switchBlock(String identifier, ICaseBlock... caseBlocks) {
		SwitchBlock switchBlock = new SwitchBlock();
		switchBlock.setReference(ref(identifier));
		switchBlock.setSections(Lists.newArrayList(caseBlocks));
		return switchBlock;
	}

	protected ICaseBlock caseBlock(String label, IStatement... body) {
		CaseBlock caseBlock = new CaseBlock();
		caseBlock.setLabel(constant(label));
		caseBlock.setBody(Lists.newArrayList(body));
		return caseBlock;
	}

	protected ITryBlock simpleTryBlock(IStatement... body) {
		TryBlock tryBlock = new TryBlock();
		tryBlock.setBody(Lists.newArrayList(body));
		return tryBlock;
	}

	protected ITryBlock simpleTryBlock(List<IStatement> body, ICatchBlock... catchBlocks) {
		TryBlock tryBlock = new TryBlock();
		tryBlock.setBody(body);
		tryBlock.setCatchBlocks(Lists.newArrayList(catchBlocks));
		return tryBlock;
	}

	protected ITryBlock tryBlock(IStatement body, IStatement finallyB, ICatchBlock block) {
		TryBlock tryBlock = new TryBlock();
		tryBlock.setBody(Lists.newArrayList(body));
		tryBlock.setCatchBlocks(Lists.newArrayList(block));
		tryBlock.setFinally(Lists.newArrayList(finallyB));
		return tryBlock;
	}

	protected ICatchBlock catchBlock(IStatement... body) {
		CatchBlock catchBlock = new CatchBlock();
		catchBlock.setBody(Lists.newArrayList(body));
		return catchBlock;
	}

	protected IUncheckedBlock uncheckedBlock(IStatement... body) {
		UncheckedBlock uncheckedBlock = new UncheckedBlock();
		uncheckedBlock.setBody(Lists.newArrayList(body));
		return uncheckedBlock;
	}

	protected IUsingBlock usingBlock(IVariableReference ref, IStatement... body) {
		UsingBlock usingBlock = new UsingBlock();
		usingBlock.setReference(ref);
		usingBlock.setBody(Lists.newArrayList(body));
		return usingBlock;
	}

	protected ILambdaExpression lambdaExpr(IStatement... body) {
		LambdaExpression expr = new LambdaExpression();
		expr.setBody(Lists.newArrayList(body));
		return expr;
	}

	protected IWhileLoop whileLoop(ILoopHeaderExpression condition, IStatement... body) {
		WhileLoop whileLoop = new WhileLoop();
		whileLoop.setBody(Lists.newArrayList(body));
		whileLoop.setCondition(condition);
		return whileLoop;
	}

	protected IIfElseExpression ifElseExpr(ISimpleExpression condition, ISimpleExpression elseExpr,
			ISimpleExpression thenExpr) {
		IfElseExpression expr = new IfElseExpression();
		expr.setCondition(condition);
		expr.setElseExpression(elseExpr);
		expr.setThenExpression(thenExpr);
		return expr;
	}

	protected ILoopHeaderBlockExpression loopHeader(IStatement... condition) {
		LoopHeaderBlockExpression loopheader = new LoopHeaderBlockExpression();
		loopheader.setBody(Lists.newArrayList(condition));
		return loopheader;
	}

	protected IAssignment assign(IAssignableReference ref, IAssignableExpression expr) {
		Assignment statement = new Assignment();
		statement.setReference(ref);
		statement.setExpression(expr);
		return statement;
	}

	protected IConstantValueExpression constant(String value) {
		ConstantValueExpression constant = new ConstantValueExpression();
		constant.setValue(value);
		return constant;
	}

	protected IReferenceExpression refExpr(String identifier) {
		ReferenceExpression refExpr = new ReferenceExpression();
		refExpr.setReference(ref(identifier));
		return refExpr;
	}

	protected IReferenceExpression refExpr(IReference identifier) {
		ReferenceExpression refExpr = new ReferenceExpression();
		refExpr.setReference(identifier);
		return refExpr;
	}

	protected IVariableReference ref(String identifier) {
		VariableReference ref = new VariableReference();
		ref.setIdentifier(identifier);
		return ref;
	}

	protected IFieldReference refField(String identifier) {
		FieldReference ref = new FieldReference();
		ref.setFieldName(Names.newField(identifier));
		ref.setReference(ref(identifier));
		return ref;
	}

	protected IStatement breakStmt() {
		BreakStatement breakStatement = new BreakStatement();
		return breakStatement;
	}

	protected IStatement continueStmt() {
		ContinueStatement continueStatement = new ContinueStatement();
		return continueStatement;
	}

	protected IStatement gotoStatement(String label) {
		GotoStatement gotoStatement = new GotoStatement();
		gotoStatement.setLabel(label);
		return gotoStatement;
	}

	protected IStatement unknownStatement() {
		UnknownStatement statement = new UnknownStatement();
		return statement;
	}

	protected IReference refEvent(String identifier) {
		EventReference event = new EventReference();
		event.setReference(ref(identifier));
		return event;
	}

	protected ISimpleExpression unknownExpression() {
		UnknownExpression expr = new UnknownExpression();
		return expr;
	}

	protected ISimpleExpression nullExpr() {
		NullExpression expr = new NullExpression();
		return expr;
	}

	protected IAssignableExpression completionExpr(String ref) {
		CompletionExpression completionExpr = new CompletionExpression();
		completionExpr.setToken("token");
		completionExpr.setObjectReference(ref(ref));
		return completionExpr;
	}

	protected IReference unknownRef() {
		UnknownReference ref = new UnknownReference();
		return ref;
	}

	protected IReference refProperty(String identifier) {
		PropertyReference ref = new PropertyReference();
		ref.setReference(ref(identifier));
		return ref;
	}

	protected IStatement throwStatement() {
		ThrowStatement statement = new ThrowStatement();
		return statement;
	}

	protected IStatement unsafeBlock() {
		UnsafeBlock block = new UnsafeBlock();
		return block;
	}

	protected IVariableDeclaration declareVar(String identifier) {
		// TODO: int, bool, unknown Type methods
		VariableDeclaration variable = new VariableDeclaration();
		variable.setReference(ref(identifier));
		return variable;
	}

	protected IVariableDeclaration declareInt(String identifier) {
		VariableDeclaration variable = new VariableDeclaration();
		variable.setReference(ref(identifier));
		variable.setType(INTEGER);
		return variable;
	}

	protected IVariableDeclaration declareBoolean(String identifier) {
		VariableDeclaration variable = new VariableDeclaration();
		variable.setReference(ref(identifier));
		variable.setType(BOOLEAN);
		return variable;
	}

	protected IVariableDeclaration declareVar(String identifier, ITypeName type) {
		VariableDeclaration variable = new VariableDeclaration();
		variable.setType(type);
		variable.setReference(ref(identifier));
		return variable;
	}

	protected Set<IFieldDeclaration> declareFields(String... fieldNames) {
		Set<IFieldDeclaration> fields = new HashSet<>();
		for (String name : fieldNames) {
			FieldDeclaration fieldDeclaration = new FieldDeclaration();
			fieldDeclaration.setName(Names.newField(name));
			fields.add(fieldDeclaration);
		}
		return fields;
	}

	protected ISST buildSST(IMethodDeclaration... declarations) {
		return buildSST(null, declarations);
	}

	protected ISST buildSST(Set<IFieldDeclaration> fields, IMethodDeclaration... declarations) {
		SST sst = new SST();
		if (fields == null) {
			FieldDeclaration fieldDeclaration = new FieldDeclaration();
			fieldDeclaration.setName(Names.newField("[T4,P] [T5,P].F"));
			sst.setFields(Sets.newHashSet(fieldDeclaration));
		} else {
			sst.setFields(fields);
		}

		PropertyDeclaration propertyDeclaration = new PropertyDeclaration();
		propertyDeclaration.setName(Names.newProperty("get [T10,P] [T11,P].P()"));
		propertyDeclaration.setGet(Lists.newArrayList(new ReturnStatement()));
		propertyDeclaration.setSet(Lists.newArrayList(new Assignment()));

		EventDeclaration eventDeclaration = new EventDeclaration();
		eventDeclaration.setName(Names.newEvent("[T2,P] [T3,P].E"));

		DelegateDeclaration delegateDeclaration = new DelegateDeclaration();
		delegateDeclaration.setName(Names.newType("d:[R,P] [T2,P].()").asDelegateTypeName());
		sst.setEnclosingType(Names.newType("T,P"));
		sst.setDelegates(Sets.newHashSet(delegateDeclaration));
		sst.setEvents(Sets.newHashSet(eventDeclaration));
		sst.setProperties(Sets.newHashSet(propertyDeclaration));

		for (IMethodDeclaration method : declarations) {
			sst.getMethods().add(method);
		}
		return sst;
	}
}