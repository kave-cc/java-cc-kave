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

import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ILoopHeaderExpression;
import cc.kave.commons.model.ssts.impl.SST;
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

/*
 *  visitor tests with this sst only fail for unimplemented expressions and references 
 * 	when the visitor traverses ExpressionStatements expression, DoLoop´s condition and ReferenceExpressions reference.
 * 	TODO: extend test to include all possible combinations
 */

public class SSTFixture {
	private SST sst;

	public SSTFixture() {
		sst = new SST();
		initSST();
	}

	public ISST getSST() {
		return sst;
	}

	private void initSST() {
		setDeclarations();
		MethodDeclaration method = new MethodDeclaration();
		method.setEntryPoint(true);
		method.setName(Names.newMethod("[?] [?].m1()"));
		method.setBody(getAllISSTNodes());
		sst.setMethods(Sets.newHashSet(method));
	}

	private void setDeclarations() {
		FieldDeclaration fieldDeclaration = new FieldDeclaration();
		fieldDeclaration.setName(Names.newField("[T4,P] [T5,P].F"));

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
		sst.setFields(Sets.newHashSet(fieldDeclaration));
	}

	private List<IStatement> getAllISSTNodes() {
		return Lists.newArrayList( //
				new BreakStatement(), //
				new ContinueStatement(), //
				new UnknownStatement(), //
				new UnsafeBlock(), //
				new Assignment(), //
				new EventSubscriptionStatement(), //
				new ExpressionStatement(), //
				new GotoStatement(), //
				new LabelledStatement(), //
				new ReturnStatement(), //
				new ThrowStatement(), //
				new VariableDeclaration(), //
				new DoLoop(), //
				new WhileLoop(), //
				new ForLoop(), //
				new ForEachLoop(), //
				new IfElseBlock(), //
				new SwitchBlock(), //
				new TryBlock(), //
				new UncheckedBlock(), //
				new UsingBlock(), //
				new LockBlock(),

				// assignable expressions
				assExpr(new BinaryExpression()), //
				assExpr(new UnaryExpression()), //
				assExpr(new CastExpression()), //
				assExpr(new TypeCheckExpression()), //
				assExpr(new CompletionExpression()), //
				assExpr(new ComposedExpression()), //
				assExpr(new IfElseExpression()), //
				assExpr(new IndexAccessExpression()), //
				assExpr(new InvocationExpression()), //
				assExpr(new LambdaExpression()), //

				// simple expressions
				assExpr(new ConstantValueExpression()), //
				assExpr(new NullExpression()), //
				assExpr(new ReferenceExpression()), //
				assExpr(new UnknownExpression()), //

				// loopheaderblock expression
				loopheaderExpr(new LoopHeaderBlockExpression()),

				// references
				ref(new EventReference()), //
				ref(new FieldReference()), //
				ref(new IndexAccessReference()), //
				ref(new PropertyReference()), //
				ref(new UnknownReference()), //
				ref(new VariableReference()), //
				ref(new MethodReference()));
	}

	private IStatement assExpr(IAssignableExpression e) {
		ExpressionStatement stmt = new ExpressionStatement();
		stmt.setExpression(e);
		return stmt;
	}

	private IStatement ref(IReference r) {
		ReferenceExpression e = new ReferenceExpression();
		e.setReference(r);
		return assExpr(e);
	}

	private IStatement loopheaderExpr(ILoopHeaderExpression e) {
		DoLoop stmt = new DoLoop();
		stmt.setCondition(e);
		return stmt;
	}
}