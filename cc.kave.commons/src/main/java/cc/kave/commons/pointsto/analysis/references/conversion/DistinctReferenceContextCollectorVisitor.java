/**
 * Copyright 2016 Simon Reuß
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package cc.kave.commons.pointsto.analysis.references.conversion;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.ssts.blocks.IDoLoop;
import cc.kave.commons.model.ssts.blocks.IForEachLoop;
import cc.kave.commons.model.ssts.blocks.IForLoop;
import cc.kave.commons.model.ssts.blocks.IIfElseBlock;
import cc.kave.commons.model.ssts.blocks.ILockBlock;
import cc.kave.commons.model.ssts.blocks.ISwitchBlock;
import cc.kave.commons.model.ssts.blocks.ITryBlock;
import cc.kave.commons.model.ssts.blocks.IUnsafeBlock;
import cc.kave.commons.model.ssts.blocks.IUsingBlock;
import cc.kave.commons.model.ssts.blocks.IWhileLoop;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.IIndexAccessExpression;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IIndexAccessReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IEventSubscriptionStatement;
import cc.kave.commons.model.ssts.statements.IExpressionStatement;
import cc.kave.commons.model.ssts.statements.ILabelledStatement;
import cc.kave.commons.model.ssts.statements.IReturnStatement;
import cc.kave.commons.model.ssts.statements.IThrowStatement;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
import cc.kave.commons.pointsto.analysis.visitors.ScopingVisitor;

public class DistinctReferenceContextCollectorVisitor extends ScopingVisitor<DistinctReferenceContextCollector, Void> {

	@Override
	public Void visit(IMethodDeclaration stmt, DistinctReferenceContextCollector context) {
		IMethodName method = stmt.getName();
		context.setCurrentMember(method);
		context.enterMember(method);
		try {
			super.visit(stmt, context);
		} finally {
			context.leaveMember();
		}
		context.setCurrentMember(null);
		return null;
	}

	@Override
	public Void visit(IPropertyDeclaration stmt, DistinctReferenceContextCollector context) {
		IPropertyName property = stmt.getName();
		context.setCurrentMember(property);
		context.enterMember(property);
		try {
			super.visit(stmt, context);
		} finally {
			context.leaveMember();
		}
		return null;
	}

	@Override
	public Void visit(IAssignment stmt, DistinctReferenceContextCollector context) {
		context.setCurrentStatement(stmt);
		super.visit(stmt, context);
		context.setCurrentStatement(null);
		return null;
	}

	@Override
	public Void visit(IDoLoop block, DistinctReferenceContextCollector context) {
		context.setCurrentStatement(block);
		super.visit(block, context);
		context.setCurrentStatement(null);
		return null;
	}

	@Override
	public Void visit(IEventSubscriptionStatement stmt, DistinctReferenceContextCollector context) {
		context.setCurrentStatement(stmt);
		super.visit(stmt, context);
		context.setCurrentStatement(null);
		return null;
	}

	@Override
	public Void visit(IExpressionStatement stmt, DistinctReferenceContextCollector context) {
		context.setCurrentStatement(stmt);
		super.visit(stmt, context);
		context.setCurrentStatement(null);
		return null;
	}

	@Override
	public Void visit(IForEachLoop block, DistinctReferenceContextCollector context) {
		context.setCurrentStatement(block);
		super.visit(block, context);
		context.setCurrentStatement(null);
		return null;
	}

	@Override
	public Void visit(IForLoop block, DistinctReferenceContextCollector context) {
		context.setCurrentStatement(block);
		super.visit(block, context);
		context.setCurrentStatement(null);
		return null;
	}

	@Override
	public Void visit(IIfElseBlock block, DistinctReferenceContextCollector context) {
		context.setCurrentStatement(block);
		super.visit(block, context);
		context.setCurrentStatement(null);
		return null;
	}

	@Override
	public Void visit(ILabelledStatement stmt, DistinctReferenceContextCollector context) {
		context.setCurrentStatement(stmt);
		super.visit(stmt, context);
		context.setCurrentStatement(stmt);
		return null;
	}

	@Override
	public Void visit(ILockBlock stmt, DistinctReferenceContextCollector context) {
		context.setCurrentStatement(stmt);
		super.visit(stmt, context);
		context.setCurrentStatement(null);
		return null;
	}

	@Override
	public Void visit(IReturnStatement stmt, DistinctReferenceContextCollector context) {
		context.setCurrentStatement(stmt);
		super.visit(stmt, context);
		context.setCurrentStatement(null);
		return null;
	}

	@Override
	public Void visit(ISwitchBlock block, DistinctReferenceContextCollector context) {
		context.setCurrentStatement(block);
		super.visit(block, context);
		context.setCurrentStatement(null);
		return null;
	}

	@Override
	public Void visit(IThrowStatement stmt, DistinctReferenceContextCollector context) {
		context.setCurrentStatement(stmt);
		super.visit(stmt, context);
		context.setCurrentStatement(null);
		return null;
	}

	@Override
	public Void visit(ITryBlock block, DistinctReferenceContextCollector context) {
		context.setCurrentStatement(block);
		super.visit(block, context);
		context.setCurrentStatement(null);
		return null;
	}

	@Override
	public Void visit(IUnsafeBlock block, DistinctReferenceContextCollector context) {
		context.setCurrentStatement(block);
		super.visit(block, context);
		context.setCurrentStatement(null);
		return null;
	}

	@Override
	public Void visit(IUsingBlock block, DistinctReferenceContextCollector context) {
		context.setCurrentStatement(block);
		super.visit(block, context);
		context.setCurrentStatement(null);
		return null;
	}

	@Override
	public Void visit(IVariableDeclaration stmt, DistinctReferenceContextCollector context) {
		context.setCurrentStatement(stmt);
		super.visit(stmt, context);
		context.setCurrentStatement(null);
		return null;
	}

	@Override
	public Void visit(IWhileLoop block, DistinctReferenceContextCollector context) {
		context.setCurrentStatement(block);
		super.visit(block, context);
		context.setCurrentStatement(null);
		return null;
	}

	@Override
	public Void visit(IIndexAccessReference indexAccessRef, DistinctReferenceContextCollector context) {
		context.useReference(indexAccessRef);
		return super.visit(indexAccessRef, context);
	}

	@Override
	public Void visit(IIndexAccessExpression expr, DistinctReferenceContextCollector context) {
		context.useReference(expr.getReference());
		return null;
	}

	@Override
	public Void visit(IFieldReference fieldRef, DistinctReferenceContextCollector context) {
		context.useReference(fieldRef);
		return null;
	}

	@Override
	public Void visit(IPropertyReference propertyRef, DistinctReferenceContextCollector context) {
		context.useReference(propertyRef);
		return null;
	}

	@Override
	public Void visit(IVariableReference varRef, DistinctReferenceContextCollector context) {
		context.useReference(varRef);
		return null;
	}
}