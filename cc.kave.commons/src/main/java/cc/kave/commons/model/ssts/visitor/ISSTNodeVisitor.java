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
package cc.kave.commons.model.ssts.visitor;

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

public interface ISSTNodeVisitor<TContext, TReturn> {

	TReturn visit(ISST sst, TContext context);

	// declarations
	TReturn visit(IDelegateDeclaration stmt, TContext context);

	TReturn visit(IEventDeclaration stmt, TContext context);

	TReturn visit(IFieldDeclaration stmt, TContext context);

	TReturn visit(IMethodDeclaration stmt, TContext context);

	TReturn visit(IPropertyDeclaration stmt, TContext context);

	TReturn visit(IVariableDeclaration stmt, TContext context);

	// statements
	TReturn visit(IAssignment stmt, TContext context);

	TReturn visit(IBreakStatement stmt, TContext context);

	TReturn visit(IContinueStatement stmt, TContext context);

	TReturn visit(IExpressionStatement stmt, TContext context);

	TReturn visit(IGotoStatement stmt, TContext context);

	TReturn visit(ILabelledStatement stmt, TContext context);

	TReturn visit(IReturnStatement stmt, TContext context);

	TReturn visit(IThrowStatement stmt, TContext context);

	TReturn visit(IEventSubscriptionStatement stmt, TContext context);

	// blocks
	TReturn visit(IDoLoop block, TContext context);

	TReturn visit(IForEachLoop block, TContext context);

	TReturn visit(IForLoop block, TContext context);

	TReturn visit(IIfElseBlock block, TContext context);

	TReturn visit(ILockBlock block, TContext context);

	TReturn visit(ISwitchBlock block, TContext context);

	TReturn visit(ITryBlock block, TContext context);

	TReturn visit(IUncheckedBlock block, TContext context);

	TReturn visit(IUnsafeBlock block, TContext context);

	TReturn visit(IUsingBlock block, TContext context);

	TReturn visit(IWhileLoop block, TContext context);

	// Expressions
	TReturn visit(IBinaryExpression expr, TContext context);

	TReturn visit(ICastExpression expr, TContext context);

	TReturn visit(ICompletionExpression expr, TContext context);

	TReturn visit(IComposedExpression expr, TContext context);

	TReturn visit(IIfElseExpression expr, TContext context);

	TReturn visit(IIndexAccessExpression expr, TContext context);

	TReturn visit(IInvocationExpression entity, TContext context);

	TReturn visit(ILambdaExpression expr, TContext context);

	TReturn visit(ITypeCheckExpression expr, TContext context);

	TReturn visit(IUnaryExpression expr, TContext context);

	TReturn visit(ILoopHeaderBlockExpression expr, TContext context);

	TReturn visit(IConstantValueExpression expr, TContext context);

	TReturn visit(INullExpression expr, TContext context);

	TReturn visit(IReferenceExpression expr, TContext context);

	// References
	TReturn visit(IEventReference ref, TContext context);

	TReturn visit(IFieldReference ref, TContext context);

	TReturn visit(IIndexAccessReference ref, TContext context);

	TReturn visit(IMethodReference ref, TContext context);

	TReturn visit(IPropertyReference ref, TContext context);

	TReturn visit(IVariableReference ref, TContext context);

	// unknowns
	TReturn visit(IUnknownReference ref, TContext context);

	TReturn visit(IUnknownExpression expr, TContext context);

	TReturn visit(IUnknownStatement stmt, TContext context);
}