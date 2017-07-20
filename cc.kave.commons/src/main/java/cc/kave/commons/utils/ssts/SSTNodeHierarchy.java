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
package cc.kave.commons.utils.ssts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import cc.kave.commons.model.ssts.ISST;
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

public class SSTNodeHierarchy {

	private Map<Integer, List<ISSTNode>> childrenMap;

	private Map<Integer, ISSTNode> parentMap;

	public SSTNodeHierarchy(ISSTNode sst) {
		this.childrenMap = new HashMap<>();
		this.parentMap = new HashMap<>();
		ParentChildrenVisitor visitor = new ParentChildrenVisitor();
		sst.accept(visitor, this);
	}

	public ISSTNode getParent(ISSTNode n) {
		return parentMap.get(System.identityHashCode(n));
	}

	public Iterable<ISSTNode> getChildren(ISSTNode n) {
		return childrenMap.get(System.identityHashCode(n));
	}

	private void addChildren(ISSTNode n, List<ISSTNode> children) {
		int identityHashCode = System.identityHashCode(n);
		for (ISSTNode child : children) {
			addParent(child, n);
		}
		childrenMap.put(identityHashCode, children);
	}

	private void addParent(ISSTNode n, ISSTNode parent) {
		parentMap.put(System.identityHashCode(n), parent);
	}

	private class ParentChildrenVisitor extends AbstractThrowingNodeVisitor<SSTNodeHierarchy, Void> {

		@Override
		public Void visit(ISST sst, SSTNodeHierarchy context) {
			List<ISSTNode> children = Lists.newArrayList();
			children.addAll(visit(sst.getDelegates(), context));
			children.addAll(visit(sst.getEvents(), context));
			children.addAll(visit(sst.getFields(), context));
			children.addAll(visit(sst.getMethods(), context));
			children.addAll(visit(sst.getProperties(), context));
			context.addChildren(sst, children);
			return null;
		}

		@Override
		public Void visit(IDelegateDeclaration stmt, SSTNodeHierarchy context) {
			List<ISSTNode> children = Lists.newArrayList();
			context.addChildren(stmt, children);
			return null;
		}

		@Override
		public Void visit(IEventDeclaration stmt, SSTNodeHierarchy context) {
			List<ISSTNode> children = Lists.newArrayList();
			context.addChildren(stmt, children);
			return null;
		}

		@Override
		public Void visit(IFieldDeclaration stmt, SSTNodeHierarchy context) {
			List<ISSTNode> children = Lists.newArrayList();
			context.addChildren(stmt, children);
			return null;
		}

		@Override
		public Void visit(IMethodDeclaration stmt, SSTNodeHierarchy context) {
			context.addChildren(stmt, visit(stmt.getBody(), context));
			return null;
		}

		@Override
		public Void visit(IPropertyDeclaration stmt, SSTNodeHierarchy context) {
			List<ISSTNode> children = Lists.newArrayList();
			children.addAll(visit(stmt.getSet(), context));
			children.addAll(visit(stmt.getGet(), context));
			context.addChildren(stmt, children);
			return null;
		}

		@Override
		public Void visit(IContinueStatement stmt, SSTNodeHierarchy context) {
			context.addChildren(stmt, Lists.newArrayList());
			return null;
		}

		@Override
		public Void visit(IBreakStatement stmt, SSTNodeHierarchy context) {
			context.addChildren(stmt, Lists.newArrayList());
			return null;
		}

		@Override
		public Void visit(IAssignment stmt, SSTNodeHierarchy context) {
			context.addChildren(stmt, Lists.newArrayList(stmt.getExpression(), stmt.getReference()));
			stmt.getExpression().accept(this, context);
			stmt.getReference().accept(this, context);
			return null;
		}

		@Override
		public Void visit(IVariableDeclaration stmt, SSTNodeHierarchy context) {
			context.addChildren(stmt, Lists.newArrayList(stmt.getReference()));
			stmt.getReference().accept(this, context);
			return null;
		}

		@Override
		public Void visit(IExpressionStatement stmt, SSTNodeHierarchy context) {
			context.addChildren(stmt, Lists.newArrayList(stmt.getExpression()));
			stmt.getExpression().accept(this, context);
			return null;
		}

		@Override
		public Void visit(IGotoStatement stmt, SSTNodeHierarchy context) {
			context.addChildren(stmt, Lists.newArrayList());
			return null;
		}

		@Override
		public Void visit(ILabelledStatement stmt, SSTNodeHierarchy context) {
			context.addChildren(stmt, Lists.newArrayList(stmt.getStatement()));
			stmt.getStatement().accept(this, context);
			return null;
		}

		@Override
		public Void visit(IReturnStatement stmt, SSTNodeHierarchy context) {
			context.addChildren(stmt, Lists.newArrayList(stmt.getExpression()));
			stmt.getExpression().accept(this, context);
			return null;
		}

		@Override
		public Void visit(IThrowStatement stmt, SSTNodeHierarchy context) {
			context.addChildren(stmt, Lists.newArrayList(stmt.getReference()));
			stmt.getReference().accept(this, context);
			return null;
		}

		@Override
		public Void visit(IDoLoop block, SSTNodeHierarchy context) {
			List<ISSTNode> children = Lists.newArrayList();
			children.addAll(visit(block.getBody(), context));
			children.add(block.getCondition());
			block.getCondition().accept(this, context);
			context.addChildren(block, children);
			return null;
		}

		@Override
		public Void visit(IForEachLoop block, SSTNodeHierarchy context) {
			List<ISSTNode> children = Lists.newArrayList();
			children.addAll(visit(block.getBody(), context));
			children.add(block.getDeclaration());
			children.add(block.getLoopedReference());
			block.getDeclaration().accept(this, context);
			block.getLoopedReference().accept(this, context);
			context.addChildren(block, children);
			return null;
		}

		@Override
		public Void visit(IForLoop block, SSTNodeHierarchy context) {
			List<ISSTNode> children = Lists.newArrayList();
			children.addAll(visit(block.getBody(), context));
			children.add(block.getCondition());
			block.getCondition().accept(this, context);
			children.addAll(visit(block.getInit(), context));
			children.addAll(visit(block.getStep(), context));
			context.addChildren(block, children);
			return null;
		}

		@Override
		public Void visit(IIfElseBlock block, SSTNodeHierarchy context) {
			List<ISSTNode> children = Lists.newArrayList();
			children.add(block.getCondition());
			block.getCondition().accept(this, context);
			children.addAll(visit(block.getElse(), context));
			children.addAll(visit(block.getThen(), context));
			context.addChildren(block, children);
			return null;
		}

		@Override
		public Void visit(ILockBlock stmt, SSTNodeHierarchy context) {
			List<ISSTNode> children = Lists.newArrayList();
			children.addAll(visit(stmt.getBody(), context));
			children.add(stmt.getReference());
			stmt.getReference().accept(this, context);
			context.addChildren(stmt, children);
			return null;
		}

		@Override
		public Void visit(ISwitchBlock block, SSTNodeHierarchy context) {
			List<ISSTNode> children = Lists.newArrayList();
			children.addAll(visit(block.getDefaultSection(), context));
			children.add(block.getReference());
			for (ICaseBlock caseblock : block.getSections()) {
				children.addAll(visit(caseblock.getBody(), context));
			}
			context.addChildren(block, children);
			block.getReference().accept(this, context);
			return null;
		}

		@Override
		public Void visit(ITryBlock block, SSTNodeHierarchy context) {
			List<ISSTNode> children = Lists.newArrayList();
			children.addAll(visit(block.getBody(), context));
			for (ICatchBlock catchblock : block.getCatchBlocks()) {
				children.addAll(visit(catchblock.getBody(), context));
			}
			children.addAll(visit(block.getFinally(), context));
			context.addChildren(block, children);
			return null;
		}

		@Override
		public Void visit(IUncheckedBlock block, SSTNodeHierarchy context) {
			List<ISSTNode> children = Lists.newArrayList();
			children.addAll(visit(block.getBody(), context));
			context.addChildren(block, children);
			return null;
		}

		@Override
		public Void visit(IUnsafeBlock block, SSTNodeHierarchy context) {
			context.addChildren(block, Lists.newArrayList());
			return null;
		}

		@Override
		public Void visit(IUsingBlock block, SSTNodeHierarchy context) {
			List<ISSTNode> children = Lists.newArrayList();
			children.addAll(visit(block.getBody(), context));
			children.add(block.getReference());
			context.addChildren(block, children);
			block.getReference().accept(this, context);
			return null;
		}

		@Override
		public Void visit(IWhileLoop block, SSTNodeHierarchy context) {
			List<ISSTNode> children = Lists.newArrayList();
			children.addAll(visit(block.getBody(), context));
			children.add(block.getCondition());
			context.addChildren(block, children);
			block.getCondition().accept(this, context);
			return null;
		}

		@Override
		public Void visit(ICompletionExpression entity, SSTNodeHierarchy context) {
			if (entity.getVariableReference() != null) {
				context.addChildren(entity, Lists.newArrayList(entity.getVariableReference()));
				entity.getVariableReference().accept(this, context);
			}
			return null;
		}

		@Override
		public Void visit(IComposedExpression expr, SSTNodeHierarchy context) {
			context.addChildren(expr, visit(expr.getReferences(), context));
			return null;
		}

		@Override
		public Void visit(IIfElseExpression expr, SSTNodeHierarchy context) {
			context.addChildren(expr,
					Lists.newArrayList(expr.getCondition(), expr.getThenExpression(), expr.getElseExpression()));
			expr.getCondition().accept(this, context);
			expr.getElseExpression().accept(this, context);
			expr.getThenExpression().accept(this, context);
			return null;
		}

		@Override
		public Void visit(IInvocationExpression entity, SSTNodeHierarchy context) {
			List<ISSTNode> children = Lists.newArrayList();
			children.addAll(visit(entity.getParameters(), context));
			children.add(entity.getReference());
			entity.getReference().accept(this, context);
			context.addChildren(entity, children);
			return null;
		}

		@Override
		public Void visit(ILambdaExpression expr, SSTNodeHierarchy context) {
			context.addChildren(expr, visit(expr.getBody(), context));
			return null;
		}

		@Override
		public Void visit(ILoopHeaderBlockExpression expr, SSTNodeHierarchy context) {
			context.addChildren(expr, visit(expr.getBody(), context));
			return null;
		}

		@Override
		public Void visit(IConstantValueExpression expr, SSTNodeHierarchy context) {
			context.addChildren(expr, Lists.newArrayList());
			return null;
		}

		@Override
		public Void visit(INullExpression expr, SSTNodeHierarchy context) {
			context.addChildren(expr, Lists.newArrayList());
			return null;
		}

		@Override
		public Void visit(IReferenceExpression expr, SSTNodeHierarchy context) {
			context.addChildren(expr, Lists.newArrayList(expr.getReference()));
			expr.getReference().accept(this, context);
			return null;
		}

		@Override
		public Void visit(IEventReference eventRef, SSTNodeHierarchy context) {
			context.addChildren(eventRef, Lists.newArrayList(eventRef.getReference()));
			eventRef.getReference().accept(this, context);
			return null;
		}

		@Override
		public Void visit(IFieldReference fieldRef, SSTNodeHierarchy context) {
			context.addChildren(fieldRef, Lists.newArrayList(fieldRef.getReference()));
			fieldRef.getReference().accept(this, context);
			return null;
		}

		@Override
		public Void visit(IMethodReference methodRef, SSTNodeHierarchy context) {
			context.addChildren(methodRef, Lists.newArrayList(methodRef.getReference()));
			methodRef.getReference().accept(this, context);
			return null;
		}

		@Override
		public Void visit(IPropertyReference methodRef, SSTNodeHierarchy context) {
			context.addChildren(methodRef, Lists.newArrayList(methodRef.getReference()));
			methodRef.getReference().accept(this, context);
			return null;
		}

		@Override
		public Void visit(IVariableReference varRef, SSTNodeHierarchy context) {
			context.addChildren(varRef, Lists.newArrayList());
			return null;
		}

		@Override
		public Void visit(IUnknownReference unknownRef, SSTNodeHierarchy context) {
			context.addChildren(unknownRef, Lists.newArrayList());
			return null;
		}

		@Override
		public Void visit(IUnknownExpression unknownExpr, SSTNodeHierarchy context) {
			context.addChildren(unknownExpr, Lists.newArrayList());
			return null;
		}

		@Override
		public Void visit(IUnknownStatement unknownStmt, SSTNodeHierarchy context) {
			context.addChildren(unknownStmt, Lists.newArrayList());
			return null;
		}

		@Override
		public Void visit(IEventSubscriptionStatement stmt, SSTNodeHierarchy context) {
			context.addChildren(stmt, Lists.newArrayList(stmt.getExpression(), stmt.getReference()));
			stmt.getExpression().accept(this, context);
			stmt.getReference().accept(this, context);
			return null;
		}

		@Override
		public Void visit(ICastExpression expr, SSTNodeHierarchy context) {
			context.addChildren(expr, Lists.newArrayList(expr.getReference()));
			expr.getReference().accept(this, context);
			return null;
		}

		@Override
		public Void visit(IIndexAccessExpression expr, SSTNodeHierarchy context) {
			List<ISSTNode> children = Lists.newArrayList();
			children.addAll(visit(expr.getIndices(), context));
			children.add(expr.getReference());
			expr.getReference().accept(this, context);
			context.addChildren(expr, children);
			return null;
		}

		@Override
		public Void visit(ITypeCheckExpression expr, SSTNodeHierarchy context) {
			context.addChildren(expr, Lists.newArrayList(expr.getReference()));
			expr.getReference().accept(this, context);
			return null;
		}

		@Override
		public Void visit(IIndexAccessReference indexAccessRef, SSTNodeHierarchy context) {
			context.addChildren(indexAccessRef, Lists.newArrayList(indexAccessRef.getExpression()));
			indexAccessRef.getExpression().accept(this, context);
			return null;
		}

		@Override
		public Void visit(IBinaryExpression expr, SSTNodeHierarchy context) {
			context.addChildren(expr, Lists.newArrayList(expr.getLeftOperand(), expr.getRightOperand()));
			expr.getLeftOperand().accept(this, context);
			expr.getRightOperand().accept(this, context);
			return null;
		}

		@Override
		public Void visit(IUnaryExpression expr, SSTNodeHierarchy context) {
			context.addChildren(expr, Lists.newArrayList(expr.getOperand()));
			expr.getOperand().accept(this, context);
			return null;
		}

		public List<ISSTNode> visit(Iterable<? extends ISSTNode> nodes, SSTNodeHierarchy context) {
			List<ISSTNode> children = Lists.newArrayList();
			for (ISSTNode node : nodes) {
				children.add(node);
				node.accept(this, context);
			}
			return children;
		}
	}
}
