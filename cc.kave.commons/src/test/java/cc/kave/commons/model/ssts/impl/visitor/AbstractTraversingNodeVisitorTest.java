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
package cc.kave.commons.model.ssts.impl.visitor;

import static org.mockito.Mockito.mock;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IDelegateDeclaration;
import cc.kave.commons.model.ssts.declarations.IEventDeclaration;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIndexAccessExpression;
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
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
import cc.kave.commons.model.ssts.visitor.ISSTNode;

public class AbstractTraversingNodeVisitorTest {

	private AbstractTraversingNodeVisitor<Integer, Void> sut;

	private IStatement stmt1;
	private IStatement stmt2;
	private IStatement stmt3;
	private ISimpleExpression expr1;
	private ISimpleExpression expr2;
	private ISimpleExpression expr3;

	private IVariableReference varRef;

	@Before
	public void setup() {
		stmt1 = mock(IStatement.class);
		stmt2 = mock(IStatement.class);
		stmt3 = mock(IStatement.class);
		expr1 = mock(ISimpleExpression.class);
		expr2 = mock(ISimpleExpression.class);
		expr3 = mock(ISimpleExpression.class);
		varRef = mock(IVariableReference.class);

		sut = new TestVisitor();
	}

	@Test
	public void sst() {
		IDelegateDeclaration d = mock(IDelegateDeclaration.class);
		IEventDeclaration e = mock(IEventDeclaration.class);
		IFieldDeclaration f = mock(IFieldDeclaration.class);
		IMethodDeclaration m = mock(IMethodDeclaration.class);
		IPropertyDeclaration p = mock(IPropertyDeclaration.class);

		SST sst = new SST();
		sst.getDelegates().add(d);
		sst.getEvents().add(e);
		sst.getFields().add(f);
		sst.getMethods().add(m);
		sst.getProperties().add(p);

		assertVisitor(sst, d, e, f, m, p);
	}

	// ######## member declarations ###########################################

	@Test
	public void delegateDeclaration() {
		assertVisitor(new DelegateDeclaration());
	}

	@Test
	public void eventDeclaration() {
		assertVisitor(new EventDeclaration());
	}

	@Test
	public void fieldDeclaration() {
		assertVisitor(new FieldDeclaration());
	}

	@Test
	public void methodDeclaration() {
		MethodDeclaration decl = new MethodDeclaration();
		decl.getBody().add(stmt1);
		assertVisitor(decl, stmt1);
	}

	@Test
	public void propertyDeclaration() {
		PropertyDeclaration decl = new PropertyDeclaration();
		decl.getGet().add(stmt1);
		decl.getSet().add(stmt2);
		assertVisitor(decl, stmt1, stmt2);
	}

	// ######## blocks ###########################################

	@Test
	public void doLoop() {
		DoLoop node = new DoLoop();
		node.getBody().add(stmt1);
		node.setCondition(expr1);
		assertVisitor(node, expr1, stmt1);
	}

	@Test
	public void forEachLoop() {
		IVariableDeclaration decl = mock(IVariableDeclaration.class);
		ForEachLoop node = new ForEachLoop();
		node.setDeclaration(decl);
		node.setLoopedReference(varRef);
		node.getBody().add(stmt1);
		assertVisitor(node, decl, varRef, stmt1);
	}

	@Test
	public void forLoop() {
		ForLoop node = new ForLoop();
		node.getInit().add(stmt1);
		node.setCondition(expr1);
		node.getStep().add(stmt2);
		node.getBody().add(stmt3);
		assertVisitor(node, stmt1, expr1, stmt2, stmt3);
	}

	@Test
	public void ifElseBlock() {
		IfElseBlock node = new IfElseBlock();
		node.setCondition(expr1);
		node.getThen().add(stmt1);
		node.getElse().add(stmt2);
		assertVisitor(node, expr1, stmt1, stmt2);
	}

	@Test
	public void lockBlock() {
		LockBlock node = new LockBlock();
		node.setReference(varRef);
		node.getBody().add(stmt1);
		assertVisitor(node, varRef, stmt1);
	}

	@Test
	public void switchBlock() {
		CaseBlock caseBlock = new CaseBlock();
		caseBlock.setLabel(expr1);
		caseBlock.getBody().add(stmt1);

		SwitchBlock node = new SwitchBlock();
		node.setReference(varRef);
		node.getSections().add(caseBlock);
		node.getDefaultSection().add(stmt2);

		assertVisitor(node, varRef, expr1, stmt1, stmt2);
	}

	@Test
	public void tryBlock() {
		CatchBlock catchBlock = new CatchBlock();
		catchBlock.getBody().add(stmt2);

		TryBlock node = new TryBlock();
		node.getBody().add(stmt1);
		node.getCatchBlocks().add(catchBlock);
		node.getFinally().add(stmt3);

		assertVisitor(node, stmt1, stmt2, stmt3);
	}

	@Test
	public void uncheckedBlock() {
		UncheckedBlock node = new UncheckedBlock();
		node.getBody().add(stmt1);

		assertVisitor(node, stmt1);
	}

	@Test
	public void unsafeBlock() {
		UnsafeBlock node = new UnsafeBlock();

		assertVisitor(node);
	}

	@Test
	public void usingBlock() {
		UsingBlock node = new UsingBlock();
		node.setReference(varRef);
		node.getBody().add(stmt1);
		assertVisitor(node, varRef, stmt1);
	}

	@Test
	public void whileLoop() {
		WhileLoop node = new WhileLoop();
		node.setCondition(expr1);
		node.getBody().add(stmt1);
		assertVisitor(node, expr1, stmt1);
	}

	// ######## statements ###########################################

	@Test
	public void assignment() {
		Assignment node = new Assignment();
		node.setReference(varRef);
		node.setExpression(expr1);
		assertVisitor(node, varRef, expr1);
	}

	@Test
	public void breakStatement() {
		assertVisitor(new BreakStatement());
	}

	@Test
	public void continueStatement() {
		assertVisitor(new ContinueStatement());
	}

	@Test
	public void eventSubscriptionStatement() {
		EventSubscriptionStatement node = new EventSubscriptionStatement();
		node.setReference(varRef);
		node.setExpression(expr1);
		assertVisitor(node, varRef, expr1);
	}

	@Test
	public void expressionStatement() {
		ExpressionStatement node = new ExpressionStatement();
		node.setExpression(expr1);
		assertVisitor(node, expr1);
	}

	@Test
	public void gotoStatement() {
		assertVisitor(new GotoStatement());
	}

	@Test
	public void labelledStatement() {
		LabelledStatement node = new LabelledStatement();
		node.setStatement(stmt1);
		assertVisitor(node, stmt1);
	}

	@Test
	public void returnStatement() {
		ReturnStatement node = new ReturnStatement();
		node.setExpression(expr1);
		assertVisitor(node, expr1);
	}

	@Test
	public void throwStatement() {
		ThrowStatement node = new ThrowStatement();
		node.setReference(varRef);
		assertVisitor(node, varRef);
	}

	@Test
	public void unknownStatement() {
		assertVisitor(new UnknownStatement());
	}

	@Test
	public void variableDeclaration() {
		VariableDeclaration node = new VariableDeclaration();
		node.setReference(varRef);
		assertVisitor(node, varRef);
	}

	// ######## expressions ###########################################

	@Test
	public void binaryExpression() {
		BinaryExpression node = new BinaryExpression();
		node.setLeftOperand(expr1);
		node.setRightOperand(expr2);
		assertVisitor(node, expr1, expr2);
	}

	@Test
	public void castExpression() {
		CastExpression node = new CastExpression();
		node.setReference(varRef);
		assertVisitor(node, varRef);
	}

	@Test
	public void completionExpression() {
		assertVisitor(new CompletionExpression());
	}

	@Test
	public void composedExpression() {
		ComposedExpression node = new ComposedExpression();
		node.getReferences().add(varRef);
		assertVisitor(node, varRef);
	}

	@Test
	public void ifElseExpression() {
		IfElseExpression node = new IfElseExpression();
		node.setCondition(expr1);
		node.setThenExpression(expr2);
		node.setElseExpression(expr3);
		assertVisitor(node, expr1, expr2, expr3);
	}

	@Test
	public void indexAccessExpression() {
		IndexAccessExpression node = new IndexAccessExpression();
		node.setReference(varRef);
		node.getIndices().add(expr1);
		assertVisitor(node, varRef, expr1);
	}

	@Test
	public void invocationExpression() {
		InvocationExpression node = new InvocationExpression();
		node.setReference(varRef);
		node.getParameters().add(expr1);
		assertVisitor(node, varRef, expr1);
	}

	@Test
	public void lambdaExpression() {
		LambdaExpression node = new LambdaExpression();
		node.getBody().add(stmt1);
		assertVisitor(node, stmt1);
	}

	@Test
	public void typeCheckExpression() {
		TypeCheckExpression node = new TypeCheckExpression();
		node.setReference(varRef);
		assertVisitor(node, varRef);
	}

	@Test
	public void unaryExpression() {
		UnaryExpression node = new UnaryExpression();
		node.setOperand(expr1);
		assertVisitor(node, expr1);
	}

	@Test
	public void loopHeaderBlockExpression() {
		LoopHeaderBlockExpression node = new LoopHeaderBlockExpression();
		node.getBody().add(stmt1);
		assertVisitor(node, stmt1);
	}

	@Test
	public void constantValueExpression() {
		assertVisitor(new ConstantValueExpression());
	}

	@Test
	public void nullExpression() {
		assertVisitor(new NullExpression());
	}

	@Test
	public void referenceExpression() {
		ReferenceExpression node = new ReferenceExpression();
		node.setReference(varRef);
		assertVisitor(node, varRef);
	}

	@Test
	public void unknownExpression() {
		assertVisitor(new UnknownExpression());
	}

	// ######## references ###########################################

	@Test
	public void eventReference() {
		EventReference node = new EventReference();
		node.setReference(varRef);
		assertVisitor(node, varRef);
	}

	@Test
	public void fieldReference() {
		FieldReference node = new FieldReference();
		node.setReference(varRef);
		assertVisitor(node, varRef);
	}

	@Test
	public void indexAccessReference() {
		IIndexAccessExpression idxExpr = mock(IIndexAccessExpression.class);
		IndexAccessReference node = new IndexAccessReference();
		node.setExpression(idxExpr);
		assertVisitor(node, idxExpr);
	}

	@Test
	public void methodReference() {
		MethodReference node = new MethodReference();
		node.setReference(varRef);
		assertVisitor(node, varRef);
	}

	@Test
	public void propertyReference() {
		PropertyReference node = new PropertyReference();
		node.setReference(varRef);
		assertVisitor(node, varRef);
	}

	@Test
	public void unknownReference() {
		assertVisitor(new UnknownReference());
	}

	@Test
	public void variableReference() {
		assertVisitor(new VariableReference());
	}

	// ######## test helper ###########################################

	private void assertVisitor(ISSTNode node, ISSTNode... ns) {
		Void res = node.accept(sut, 12);

		Assert.assertNull(res);

		for (ISSTNode n : ns) {
			Mockito.verify(n).accept(sut, 12);
		}
	}

	public static class TestVisitor extends AbstractTraversingNodeVisitor<Integer, Void> {
	}
}