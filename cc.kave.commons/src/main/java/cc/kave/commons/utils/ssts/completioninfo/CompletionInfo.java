/**
 * Copyright 2018 University of Zurich
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
package cc.kave.commons.utils.ssts.completioninfo;

import static cc.kave.commons.assertions.Asserts.assertNotNull;

import java.util.Optional;

import cc.kave.commons.assertions.Asserts;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.ISST;
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
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.ICompletionExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.impl.visitor.AbstractTraversingNodeVisitor;
import cc.kave.commons.model.ssts.references.IAssignableReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;

public class CompletionInfo implements ICompletionInfo {

	public static Optional<CompletionInfo> extractCompletionInfoFrom(ISST sst) {
		CompletionInfo info = new CompletionInfo(sst);
		return info.hasInformation() ? Optional.of(info) : Optional.empty();
	}

	private ICompletionExpression completionExpr = null;
	private ITypeName triggeredType = null;
	private ITypeName expectedType = null;

	private CompletionInfo(ISST sst) {
		assertNotNull(sst);
		sst.accept(new CompletionInfoVisitor(), null);
	}

	private boolean hasInformation() {
		return completionExpr != null;
	}

	@Override
	public ICompletionExpression getCompletionExpr() {
		return completionExpr;
	}

	@Override
	public ITypeName getTriggeredType() {
		return triggeredType;
	}

	@Override
	public boolean hasExpectedType() {
		return expectedType != null;
	}

	@Override
	public ITypeName getExpectedType() {
		Asserts.assertNotNull(expectedType, "Use hasExpectedType() before requesting it!");
		return expectedType;
	}

	private class CompletionInfoVisitor extends AbstractTraversingNodeVisitor<Void, Void> {

		private TypeOfAssignableReferenceVisitor refTypeVisitor = new TypeOfAssignableReferenceVisitor();
		private VariableScope<ITypeName> variables = new VariableScope<>();

		@Override
		public Void visit(ICompletionExpression expr, Void context) {
			completionExpr = expr;

			if (expr.getTypeReference() != null) {
				triggeredType = expr.getTypeReference();
			} else if (expr.getVariableReference() != null) {
				String id = expr.getVariableReference().getIdentifier();
				triggeredType = variables.isDeclared(id) ? variables.get(id) : Names.getUnknownType();
			}

			return null;
		}

		@Override
		public Void visit(IPropertyDeclaration decl, Void context) {
			variables.open();
			visit(decl.getGet(), context);
			variables.close();
			variables.open();
			visit(decl.getSet(), context);
			variables.close();
			return null;
		}

		@Override
		public Void visit(IMethodDeclaration decl, Void context) {
			variables.open();

			for (IParameterName p : decl.getName().getParameters()) {
				variables.declare(p.getName(), p.getValueType());
			}

			super.visit(decl, context);
			variables.close();
			return null;
		}

		@Override
		public Void visit(IAssignment stmt, Void context) {
			if (stmt.getExpression() instanceof ICompletionExpression) {
				IAssignableReference ref = stmt.getReference();
				expectedType = ref.accept(refTypeVisitor, variables);
			}
			return super.visit(stmt, context);
		}

		@Override
		public Void visit(IVariableDeclaration stmt, Void context) {
			variables.declare(stmt.getReference().getIdentifier(), stmt.getType());
			return super.visit(stmt, context);
		}

		@Override
		public Void visit(IDoLoop loop, Void context) {
			variables.open();
			super.visit(loop, context);
			variables.close();
			return null;
		}

		@Override
		public Void visit(IForLoop loop, Void context) {
			variables.open();
			super.visit(loop, context);
			variables.close();
			return null;
		}

		@Override
		public Void visit(IForEachLoop loop, Void context) {
			variables.open();
			super.visit(loop, context);
			variables.close();
			return null;
		}

		@Override
		public Void visit(IIfElseBlock block, Void context) {

			block.getCondition().accept(this, context);
			variables.open();
			visit(block.getThen(), context);
			variables.close();
			variables.open();
			visit(block.getElse(), context);
			variables.close();
			return null;
		}

		@Override
		public Void visit(ILockBlock loop, Void context) {
			variables.open();
			super.visit(loop, context);
			variables.close();
			return null;
		}

		@Override
		public Void visit(ISwitchBlock block, Void context) {
			variables.open();
			super.visit(block, context);
			variables.close();
			return null;
		}

		@Override
		public Void visit(ITryBlock block, Void context) {
			variables.open();
			visit(block.getBody(), context);
			variables.close();
			for (ICatchBlock cb : block.getCatchBlocks()) {
				variables.open();

				IParameterName p = cb.getParameter();
				variables.declare(p.getName(), p.getValueType());

				visit(cb.getBody(), context);
				variables.close();
			}
			variables.open();
			visit(block.getFinally(), context);
			variables.close();
			return null;
		}

		@Override
		public Void visit(IUncheckedBlock block, Void context) {
			variables.open();
			super.visit(block, context);
			variables.close();
			return null;
		}

		@Override
		public Void visit(IUsingBlock block, Void context) {
			variables.open();
			super.visit(block, context);
			variables.close();
			return null;
		}

		@Override
		public Void visit(IWhileLoop loop, Void context) {
			variables.open();
			super.visit(loop, context);
			variables.close();
			return null;
		}

		@Override
		public Void visit(ILambdaExpression expr, Void context) {
			variables.open();
			for (IParameterName p : expr.getName().getParameters()) {
				variables.declare(p.getName(), p.getValueType());
			}
			super.visit(expr, context);
			variables.close();
			return null;
		}
	}
}