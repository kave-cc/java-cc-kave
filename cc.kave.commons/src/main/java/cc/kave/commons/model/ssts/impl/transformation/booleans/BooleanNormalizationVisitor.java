/**
 * Copyright 2016 Carina Oberle
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
package cc.kave.commons.model.ssts.impl.transformation.booleans;

import java.util.ArrayList;
import java.util.List;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.IDoLoop;
import cc.kave.commons.model.ssts.blocks.IForLoop;
import cc.kave.commons.model.ssts.blocks.IIfElseBlock;
import cc.kave.commons.model.ssts.blocks.ISwitchBlock;
import cc.kave.commons.model.ssts.blocks.IWhileLoop;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.impl.transformation.AbstractStatementNormalizationVisitor;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IEventSubscriptionStatement;
import cc.kave.commons.model.ssts.statements.IExpressionStatement;
import cc.kave.commons.model.ssts.statements.ILabelledStatement;
import cc.kave.commons.model.ssts.statements.IReturnStatement;

public class BooleanNormalizationVisitor extends AbstractStatementNormalizationVisitor<RefLookup> {
	private ExpressionNormalizationVisitor expressionVisitor;

	public BooleanNormalizationVisitor() {
		expressionVisitor = new ExpressionNormalizationVisitor();
	}

	@Override
	public List<IStatement> visit(IMethodDeclaration decl, RefLookup context) {
		RefLookup lookup = new ReferenceCollectorVisitor().visit(decl);
		visit(decl.getBody(), lookup);
		return null;
	}

	/*
	 * Normalize expressions and retrieve newly created statements.
	 */
	private List<IStatement> getNewDeclarations(IStatement stmt, RefLookup context) {
		stmt.accept(expressionVisitor, context);
		return expressionVisitor.clearCreatedStatements();
	}

	private List<IStatement> handleNewDeclarations(IStatement stmt, RefLookup context) {
		List<IStatement> newDeclarations = getNewDeclarations(stmt, context);
		if (newDeclarations.isEmpty())
			return null;
		List<IStatement> stmtNormalized = new ArrayList<IStatement>();
		stmtNormalized.addAll(newDeclarations);
		stmtNormalized.add(stmt);
		return stmtNormalized;
	}

	@Override
	public List<IStatement> visit(IAssignment stmt, RefLookup context) {
		super.visit(stmt, context);
		return handleNewDeclarations(stmt, context);
	}

	@Override
	public List<IStatement> visit(IEventSubscriptionStatement stmt, RefLookup context) {
		super.visit(stmt, context);
		return handleNewDeclarations(stmt, context);
	}

	@Override
	public List<IStatement> visit(IExpressionStatement stmt, RefLookup context) {
		List<IStatement> statements = super.visit(stmt, context);
		List<IStatement> normalized = getNewDeclarations(stmt, context);
		if (statements != null)
			normalized.addAll(statements);
		return normalized;
	}

	@Override
	public List<IStatement> visit(ILabelledStatement stmt, RefLookup context) {
		List<IStatement> statements = super.visit(stmt, context);
		List<IStatement> normalized = getNewDeclarations(stmt, context);
		if (statements != null)
			normalized.addAll(statements);
		return normalized;
	}

	@Override
	public List<IStatement> visit(IReturnStatement stmt, RefLookup context) {
		super.visit(stmt, context);
		return handleNewDeclarations(stmt, context);
	}

	@Override
	public List<IStatement> visit(IDoLoop block, RefLookup context) {
		super.visit(block, context);
		return handleNewDeclarations(block, context);
	}

	@Override
	public List<IStatement> visit(IForLoop block, RefLookup context) {
		super.visit(block, context);
		return handleNewDeclarations(block, context);
	}

	@Override
	public List<IStatement> visit(IIfElseBlock block, RefLookup context) {
		super.visit(block, context);
		return handleNewDeclarations(block, context);
	}

	@Override
	public List<IStatement> visit(ISwitchBlock block, RefLookup context) {
		super.visit(block, context);
		return handleNewDeclarations(block, context);
	}

	@Override
	public List<IStatement> visit(IWhileLoop block, RefLookup context) {
		super.visit(block, context);
		return handleNewDeclarations(block, context);
	}

}
