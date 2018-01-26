/**
 * Copyright 2015 Simon Reuß
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
package cc.kave.commons.pointsto.analysis.visitors;

import cc.kave.commons.model.naming.codeelements.ILambdaName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.ssts.blocks.CatchBlockKind;
import cc.kave.commons.model.ssts.blocks.ICaseBlock;
import cc.kave.commons.model.ssts.blocks.ICatchBlock;
import cc.kave.commons.model.ssts.blocks.IDoLoop;
import cc.kave.commons.model.ssts.blocks.IForEachLoop;
import cc.kave.commons.model.ssts.blocks.IForLoop;
import cc.kave.commons.model.ssts.blocks.IIfElseBlock;
import cc.kave.commons.model.ssts.blocks.ISwitchBlock;
import cc.kave.commons.model.ssts.blocks.ITryBlock;
import cc.kave.commons.model.ssts.blocks.IUncheckedBlock;
import cc.kave.commons.model.ssts.blocks.IUsingBlock;
import cc.kave.commons.model.ssts.blocks.IWhileLoop;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;

public class ScopingVisitor<TContext extends ScopingVisitorContext, TReturn>
		extends TraversingVisitor<TContext, TReturn> {

	@Override
	public TReturn visit(IMethodDeclaration stmt, TContext context) {
		context.enterScope();
		IMethodName method = stmt.getName();
		for (IParameterName parameter : method.getParameters()) {
			context.declareParameter(parameter, method);
		}
		super.visit(stmt, context);
		context.leaveScope();

		return null;
	}

	@Override
	public TReturn visit(ILambdaExpression expr, TContext context) {
		ILambdaName lambda = expr.getName();

		context.enterScope();
		for (IParameterName parameter : lambda.getParameters()) {
			context.declareParameter(parameter, expr);
		}
		visitStatements(expr.getBody(), context);
		context.leaveScope();

		return null;
	}

	@Override
	public TReturn visit(IPropertyDeclaration stmt, TContext context) {
		if (!stmt.getGet().isEmpty()) {
			context.enterScope();
			visitStatements(stmt.getGet(), context);
			context.leaveScope();
		}

		if (!stmt.getSet().isEmpty()) {
			context.enterScope();
			context.declarePropertySetParameter(stmt);
			visitStatements(stmt.getSet(), context);
			context.leaveScope();
		}

		return null;
	}

	@Override
	public TReturn visit(ITryBlock block, TContext context) {
		context.enterScope();
		visitStatements(block.getBody(), context);
		context.leaveScope();

		for (ICatchBlock catchBlock : block.getCatchBlocks()) {
			context.enterScope();

			// only default catch blocks have a usable parameter, the other
			// kinds do not bind the caught exception to a
			// usable name
			if (catchBlock.getKind() == CatchBlockKind.Default) {
				context.declareParameter(catchBlock.getParameter(), catchBlock);
			}

			visitStatements(catchBlock.getBody(), context);
			context.leaveScope();
		}

		context.enterScope();
		visitStatements(block.getFinally(), context);
		context.leaveScope();

		return null;
	}

	@Override
	public TReturn visit(ISwitchBlock block, TContext context) {
		block.getReference().accept(this, context);

		for (ICaseBlock caseBlock : block.getSections()) {
			context.enterScope();
			caseBlock.getLabel().accept(this, context);
			visitStatements(caseBlock.getBody(), context);
			context.leaveScope();
		}

		context.enterScope();
		visitStatements(block.getDefaultSection(), context);
		context.leaveScope();

		return null;
	}

	@Override
	public TReturn visit(IIfElseBlock block, TContext context) {
		block.getCondition().accept(this, context);

		context.enterScope();
		visitStatements(block.getThen(), context);
		context.leaveScope();

		context.enterScope();
		visitStatements(block.getElse(), context);
		context.leaveScope();

		return null;
	}

	@Override
	public TReturn visit(IForLoop block, TContext context) {
		context.enterScope();
		visitStatements(block.getInit(), context);
		block.getCondition().accept(this, context);
		visitStatements(block.getBody(), context);
		visitStatements(block.getStep(), context);
		context.leaveScope();

		return null;
	}

	@Override
	public TReturn visit(IForEachLoop block, TContext context) {
		context.enterScope();
		block.getLoopedReference().accept(this, context);
		block.getDeclaration().accept(this, context);
		visitStatements(block.getBody(), context);
		context.leaveScope();

		return null;
	}

	@Override
	public TReturn visit(IUsingBlock block, TContext context) {
		context.enterScope();
		block.getReference().accept(this, context);
		visitStatements(block.getBody(), context);
		context.leaveScope();

		return null;
	}

	@Override
	public TReturn visit(IDoLoop block, TContext context) {
		context.enterScope();
		visitStatements(block.getBody(), context);
		context.leaveScope();
		block.getCondition().accept(this, context);

		return null;
	}

	@Override
	public TReturn visit(IWhileLoop block, TContext context) {
		block.getCondition().accept(this, context);
		context.enterScope();
		visitStatements(block.getBody(), context);
		context.leaveScope();

		return null;
	}

	@Override
	public TReturn visit(IUncheckedBlock block, TContext context) {
		context.enterScope();
		visitStatements(block.getBody(), context);
		context.leaveScope();

		return null;
	}

	@Override
	public TReturn visit(IVariableDeclaration stmt, TContext context) {
		context.declareVariable(stmt);
		return null;
	}
}