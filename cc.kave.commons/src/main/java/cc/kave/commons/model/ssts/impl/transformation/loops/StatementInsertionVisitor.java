/**
 * Copyright 2015 Carina Oberle
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.kave.commons.model.ssts.impl.transformation.loops;

import java.util.ArrayList;
import java.util.List;

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
import cc.kave.commons.model.ssts.blocks.IUnsafeBlock;
import cc.kave.commons.model.ssts.blocks.IUsingBlock;
import cc.kave.commons.model.ssts.blocks.IWhileLoop;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.impl.visitor.AbstractThrowingNodeVisitor;
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

public class StatementInsertionVisitor
		extends AbstractThrowingNodeVisitor<StatementInsertionContext, List<IStatement>> {

	@Override
	public List<IStatement> visit(ISST sst, StatementInsertionContext context) {
		if (context.skip(sst))
			return null;

		for (IPropertyDeclaration property : sst.getProperties()) {
			property.accept(this, context);
		}
		for (IMethodDeclaration method : sst.getMethods()) {
			method.accept(this, context);
		}
		return null;
	}

	@Override
	public List<IStatement> visit(IMethodDeclaration method, StatementInsertionContext context) {
		if (context.skip(method))
			return null;
		List<IStatement> body = method.getBody();
		List<IStatement> bodyNormalized = visit(body, context);
		update(method.getBody(), bodyNormalized);
		return null;
	}

	@Override
	public List<IStatement> visit(IVariableDeclaration stmt, StatementInsertionContext context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IAssignment stmt, StatementInsertionContext context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IBreakStatement stmt, StatementInsertionContext context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IContinueStatement stmt, StatementInsertionContext context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IExpressionStatement stmt, StatementInsertionContext context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IGotoStatement stmt, StatementInsertionContext context) {
		return null;
	}

	@Override
	public List<IStatement> visit(ILabelledStatement stmt, StatementInsertionContext context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IReturnStatement stmt, StatementInsertionContext context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IThrowStatement stmt, StatementInsertionContext context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IDoLoop block, StatementInsertionContext context) {
		if (context.skip(block))
			return null;
		List<IStatement> body = block.getBody();
		List<IStatement> bodyNormalized = visit(body, context);
		update(block.getBody(), bodyNormalized);
		return null;
	}

	@Override
	public List<IStatement> visit(IForEachLoop block, StatementInsertionContext context) {
		if (context.skip(block))
			return null;
		List<IStatement> body = block.getBody();
		List<IStatement> bodyNormalized = visit(body, context);
		update(block.getBody(), bodyNormalized);
		return null;
	}

	@Override
	public List<IStatement> visit(IForLoop block, StatementInsertionContext context) {
		if (context.skip(block))
			return null;
		List<IStatement> initNormalized = visit(block.getInit(), context);
		List<IStatement> stepNormalized = visit(block.getStep(), context);
		List<IStatement> bodyNormalized = visit(block.getBody(), context);
		update(block.getInit(), initNormalized);
		update(block.getStep(), stepNormalized);
		update(block.getBody(), bodyNormalized);
		return null;
	}

	@Override
	public List<IStatement> visit(IIfElseBlock block, StatementInsertionContext context) {
		if (context.skip(block))
			return null;
		List<IStatement> thenNormalized = visit(block.getThen(), context);
		List<IStatement> elseNormalized = visit(block.getElse(), context);
		update(block.getThen(), thenNormalized);
		update(block.getElse(), elseNormalized);
		return null;
	}

	@Override
	public List<IStatement> visit(ILockBlock block, StatementInsertionContext context) {
		if (context.skip(block))
			return null;
		List<IStatement> bodyNormalized = visit(block.getBody(), context);
		update(block.getBody(), bodyNormalized);
		return null;
	}

	@Override
	public List<IStatement> visit(ISwitchBlock block, StatementInsertionContext context) {
		if (context.skip(block))
			return null;
		for (ICaseBlock caseBlock : block.getSections()) {
			List<IStatement> body = caseBlock.getBody();
			List<IStatement> bodyNormalized = visit(body, context);
			update(caseBlock.getBody(), bodyNormalized);
		}

		List<IStatement> defaultSection = block.getDefaultSection();
		List<IStatement> defaultSectionNormalized = visit(defaultSection, context);
		update(block.getDefaultSection(), defaultSectionNormalized);
		return null;
	}

	@Override
	public List<IStatement> visit(ITryBlock block, StatementInsertionContext context) {
		if (context.skip(block))
			return null;
		List<IStatement> body = block.getBody();
		List<IStatement> bodyNormalized = visit(body, context);
		update(block.getBody(), bodyNormalized);

		for (ICatchBlock catchBlock : block.getCatchBlocks()) {
			List<IStatement> catchBody = catchBlock.getBody();
			List<IStatement> catchBodyNormalized = visit(catchBody, context);
			update(catchBlock.getBody(), catchBodyNormalized);
		}

		List<IStatement> finallyStmt = block.getFinally();
		List<IStatement> finallyStmtNormalized = visit(finallyStmt, context);
		update(block.getFinally(), finallyStmtNormalized);
		return null;
	}

	@Override
	public List<IStatement> visit(IUncheckedBlock block, StatementInsertionContext context) {
		if (context.skip(block))
			return null;
		List<IStatement> body = block.getBody();
		List<IStatement> bodyNormalized = visit(body, context);
		update(block.getBody(), bodyNormalized);
		return null;
	}

	@Override
	public List<IStatement> visit(IUnsafeBlock block, StatementInsertionContext context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IUsingBlock block, StatementInsertionContext context) {
		if (context.skip(block))
			return null;
		List<IStatement> body = block.getBody();
		List<IStatement> bodyNormalized = visit(body, context);
		update(block.getBody(), bodyNormalized);
		return null;
	}

	@Override
	public List<IStatement> visit(IWhileLoop block, StatementInsertionContext context) {
		if (context.skip(block))
			return null;
		List<IStatement> body = block.getBody();
		List<IStatement> bodyNormalized = visit(body, context);
		update(block.getBody(), bodyNormalized);
		return null;
	}

	@Override
	public List<IStatement> visit(IUnknownStatement unknownStmt, StatementInsertionContext context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IEventSubscriptionStatement stmt, StatementInsertionContext context) {
		return null;
	}

	/**
	 * Helper method to visit list of statements.
	 */
	public List<IStatement> visit(List<IStatement> statements, StatementInsertionContext context) {
		List<IStatement> normalized = new ArrayList<IStatement>();
		for (IStatement stmt : statements) {
			List<IStatement> stmtNormalized = stmt.accept(this, context);
			if (context.insertBefore(stmt)) {
				normalized.addAll(context.statements);
			}
			if (stmtNormalized == null) {
				normalized.add(stmt);
			} else {
				normalized.addAll(stmtNormalized);
			}
			if (context.insertAfter(stmt)) {
				normalized.addAll(context.statements);
			}
		}
		return normalized;
	}

	public List<IStatement> update(List<IStatement> statements, List<IStatement> normalized) {
		if (normalized != null) {
			statements.clear();
			statements.addAll(normalized);
		}
		return statements;
	}

}
