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

import static org.mockito.Mockito.mock;

import org.mockito.Mockito;

import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.blocks.IDoLoop;
import cc.kave.commons.model.ssts.blocks.IForEachLoop;
import cc.kave.commons.model.ssts.blocks.IForLoop;
import cc.kave.commons.model.ssts.blocks.IIfElseBlock;
import cc.kave.commons.model.ssts.blocks.ILockBlock;
import cc.kave.commons.model.ssts.blocks.ISwitchBlock;
import cc.kave.commons.model.ssts.blocks.ITryBlock;
import cc.kave.commons.model.ssts.blocks.IUncheckedBlock;
import cc.kave.commons.model.ssts.blocks.IUnsafeBlock;
import cc.kave.commons.model.ssts.blocks.IUsingBlock;
import cc.kave.commons.model.ssts.blocks.IWhileLoop;
import cc.kave.commons.model.ssts.declarations.IDelegateDeclaration;
import cc.kave.commons.model.ssts.declarations.IEventDeclaration;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.IBinaryExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ICastExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ICompletionExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IComposedExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIfElseExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIndexAccessExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ITypeCheckExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IUnaryExpression;
import cc.kave.commons.model.ssts.expressions.loopheader.ILoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.expressions.simple.INullExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.expressions.simple.IUnknownExpression;
import cc.kave.commons.model.ssts.impl.visitor.AbstractThrowingNodeVisitor;
import cc.kave.commons.model.ssts.references.IEventReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IIndexAccessReference;
import cc.kave.commons.model.ssts.references.IMethodReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IUnknownReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IBreakStatement;
import cc.kave.commons.model.ssts.statements.IContinueStatement;
import cc.kave.commons.model.ssts.statements.IEventSubscriptionStatement;
import cc.kave.commons.model.ssts.statements.IExpressionStatement;
import cc.kave.commons.model.ssts.statements.IGotoStatement;
import cc.kave.commons.model.ssts.statements.ILabelledStatement;
import cc.kave.commons.model.ssts.statements.IReturnStatement;
import cc.kave.commons.model.ssts.statements.IThrowStatement;
import cc.kave.commons.model.ssts.statements.IUnknownStatement;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
import cc.kave.commons.model.ssts.visitor.ISSTNode;

public class VisitorAssertion {
	private ISSTNode _node;
	private int _context;

	public VisitorAssertion(ISSTNode node, int context) {
		_node = node;
		_context = context;
	}

	public void verify(Object obj) {
		AbstractThrowingNodeVisitor<Integer, Void> visitor = mock(AbstractThrowingNodeVisitor.class);
		_node.accept(visitor, _context);
		if (obj instanceof ISST)
			Mockito.verify(visitor).visit((ISST) obj, _context);
		else if (obj instanceof IDelegateDeclaration)
			Mockito.verify(visitor).visit((IDelegateDeclaration) obj, _context);
		else if (obj instanceof IEventDeclaration)
			Mockito.verify(visitor).visit((IEventDeclaration) obj, _context);
		else if (obj instanceof IFieldDeclaration)
			Mockito.verify(visitor).visit((IFieldDeclaration) obj, _context);
		else if (obj instanceof IMethodDeclaration)
			Mockito.verify(visitor).visit((IMethodDeclaration) obj, _context);
		else if (obj instanceof IPropertyDeclaration)
			Mockito.verify(visitor).visit((IPropertyDeclaration) obj, _context);
		else if (obj instanceof IVariableDeclaration)
			Mockito.verify(visitor).visit((IVariableDeclaration) obj, _context);
		else if (obj instanceof IAssignment)
			Mockito.verify(visitor).visit((IAssignment) obj, _context);
		else if (obj instanceof IBreakStatement)
			Mockito.verify(visitor).visit((IBreakStatement) obj, _context);
		else if (obj instanceof IContinueStatement)
			Mockito.verify(visitor).visit((IContinueStatement) obj, _context);
		else if (obj instanceof IExpressionStatement)
			Mockito.verify(visitor).visit((IExpressionStatement) obj, _context);
		else if (obj instanceof IGotoStatement)
			Mockito.verify(visitor).visit((IGotoStatement) obj, _context);
		else if (obj instanceof ILabelledStatement)
			Mockito.verify(visitor).visit((ILabelledStatement) obj, _context);
		else if (obj instanceof IReturnStatement)
			Mockito.verify(visitor).visit((IReturnStatement) obj, _context);
		else if (obj instanceof IThrowStatement)
			Mockito.verify(visitor).visit((IThrowStatement) obj, _context);
		else if (obj instanceof IDoLoop)
			Mockito.verify(visitor).visit((IDoLoop) obj, _context);
		else if (obj instanceof IForEachLoop)
			Mockito.verify(visitor).visit((IForEachLoop) obj, _context);
		else if (obj instanceof IForLoop)
			Mockito.verify(visitor).visit((IForLoop) obj, _context);
		else if (obj instanceof IIfElseBlock)
			Mockito.verify(visitor).visit((IIfElseBlock) obj, _context);
		else if (obj instanceof ILockBlock)
			Mockito.verify(visitor).visit((ILockBlock) obj, _context);
		else if (obj instanceof ISwitchBlock)
			Mockito.verify(visitor).visit((ISwitchBlock) obj, _context);
		else if (obj instanceof ITryBlock)
			Mockito.verify(visitor).visit((ITryBlock) obj, _context);
		else if (obj instanceof IUncheckedBlock)
			Mockito.verify(visitor).visit((IUncheckedBlock) obj, _context);
		else if (obj instanceof IUnsafeBlock)
			Mockito.verify(visitor).visit((IUnsafeBlock) obj, _context);
		else if (obj instanceof IUsingBlock)
			Mockito.verify(visitor).visit((IUsingBlock) obj, _context);
		else if (obj instanceof IWhileLoop)
			Mockito.verify(visitor).visit((IWhileLoop) obj, _context);
		else if (obj instanceof ICompletionExpression)
			Mockito.verify(visitor).visit((ICompletionExpression) obj, _context);
		else if (obj instanceof IComposedExpression)
			Mockito.verify(visitor).visit((IComposedExpression) obj, _context);
		else if (obj instanceof IIfElseExpression)
			Mockito.verify(visitor).visit((IIfElseExpression) obj, _context);
		else if (obj instanceof IInvocationExpression)
			Mockito.verify(visitor).visit((IInvocationExpression) obj, _context);
		else if (obj instanceof ILambdaExpression)
			Mockito.verify(visitor).visit((ILambdaExpression) obj, _context);
		else if (obj instanceof ILoopHeaderBlockExpression)
			Mockito.verify(visitor).visit((ILoopHeaderBlockExpression) obj, _context);
		else if (obj instanceof IConstantValueExpression)
			Mockito.verify(visitor).visit((IConstantValueExpression) obj, _context);
		else if (obj instanceof INullExpression)
			Mockito.verify(visitor).visit((INullExpression) obj, _context);
		else if (obj instanceof IReferenceExpression)
			Mockito.verify(visitor).visit((IReferenceExpression) obj, _context);
		else if (obj instanceof IEventReference)
			Mockito.verify(visitor).visit((IEventReference) obj, _context);
		else if (obj instanceof IFieldReference)
			Mockito.verify(visitor).visit((IFieldReference) obj, _context);
		else if (obj instanceof IMethodReference)
			Mockito.verify(visitor).visit((IMethodReference) obj, _context);
		else if (obj instanceof IPropertyReference)
			Mockito.verify(visitor).visit((IPropertyReference) obj, _context);
		else if (obj instanceof IVariableReference)
			Mockito.verify(visitor).visit((IVariableReference) obj, _context);
		else if (obj instanceof IUnknownReference)
			Mockito.verify(visitor).visit((IUnknownReference) obj, _context);
		else if (obj instanceof IUnknownExpression)
			Mockito.verify(visitor).visit((IUnknownExpression) obj, _context);
		else if (obj instanceof IUnknownStatement)
			Mockito.verify(visitor).visit((IUnknownStatement) obj, _context);
		else if (obj instanceof ICastExpression)
			Mockito.verify(visitor).visit((ICastExpression) obj, _context);
		else if (obj instanceof IIndexAccessExpression)
			Mockito.verify(visitor).visit((IIndexAccessExpression) obj, _context);
		else if (obj instanceof ITypeCheckExpression)
			Mockito.verify(visitor).visit((ITypeCheckExpression) obj, _context);
		else if (obj instanceof IIndexAccessReference)
			Mockito.verify(visitor).visit((IIndexAccessReference) obj, _context);
		else if (obj instanceof IEventSubscriptionStatement)
			Mockito.verify(visitor).visit((IEventSubscriptionStatement) obj, _context);
		else if (obj instanceof IBinaryExpression)
			Mockito.verify(visitor).visit((IBinaryExpression) obj, _context);
		else if (obj instanceof IUnaryExpression)
			Mockito.verify(visitor).visit((IUnaryExpression) obj, _context);
		else
			throw new RuntimeException();

	}
}
