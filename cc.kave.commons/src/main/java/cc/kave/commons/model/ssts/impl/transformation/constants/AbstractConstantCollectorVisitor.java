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
package cc.kave.commons.model.ssts.impl.transformation.constants;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.impl.visitor.AbstractThrowingNodeVisitor;
import cc.kave.commons.model.ssts.references.IEventReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
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

public abstract class AbstractConstantCollectorVisitor
		extends AbstractThrowingNodeVisitor<Set<IFieldDeclaration>, Set<IFieldDeclaration>> {
	@Override
	public Set<IFieldDeclaration> visit(ISST sst, Set<IFieldDeclaration> constants) {

		for (IFieldDeclaration field : sst.getFields()) {
			constants.add(field);
		}
		for (IPropertyDeclaration property : sst.getProperties()) {
			constants.addAll(property.accept(this, constants));
		}
		for (IMethodDeclaration method : sst.getMethods()) {
			constants.addAll(method.accept(this, constants));
		}
		return constants;
	}

	@Override
	public Set<IFieldDeclaration> visit(IMethodDeclaration stmt, Set<IFieldDeclaration> constants) {
		return visit(stmt.getBody(), constants);
	}

	@Override
	public Set<IFieldDeclaration> visit(IPropertyDeclaration stmt, Set<IFieldDeclaration> constants) {
		Set<IFieldDeclaration> foundConstants = new HashSet<IFieldDeclaration>();
		foundConstants.addAll(visit(stmt.getGet(), constants));
		foundConstants.addAll(visit(stmt.getSet(), constants));
		return foundConstants;
	}

	@Override
	public Set<IFieldDeclaration> visit(IAssignment stmt, Set<IFieldDeclaration> constants) {
		return stmt.getReference().accept(this, constants);
	}

	@Override
	public Set<IFieldDeclaration> visit(IVariableDeclaration stmt, Set<IFieldDeclaration> constants) {
		return new HashSet<IFieldDeclaration>();
	}

	@Override
	public Set<IFieldDeclaration> visit(IBreakStatement stmt, Set<IFieldDeclaration> constants) {
		return new HashSet<IFieldDeclaration>();
	}

	@Override
	public Set<IFieldDeclaration> visit(IContinueStatement stmt, Set<IFieldDeclaration> constants) {
		return new HashSet<IFieldDeclaration>();
	}

	@Override
	public Set<IFieldDeclaration> visit(IExpressionStatement stmt, Set<IFieldDeclaration> constants) {
		return new HashSet<IFieldDeclaration>();
	}

	@Override
	public Set<IFieldDeclaration> visit(IGotoStatement stmt, Set<IFieldDeclaration> constants) {
		return new HashSet<IFieldDeclaration>();
	}

	@Override
	public Set<IFieldDeclaration> visit(ILabelledStatement stmt, Set<IFieldDeclaration> constants) {
		return stmt.getStatement().accept(this, constants);
	}

	@Override
	public Set<IFieldDeclaration> visit(IReturnStatement stmt, Set<IFieldDeclaration> constants) {
		return new HashSet<IFieldDeclaration>();
	}

	@Override
	public Set<IFieldDeclaration> visit(IThrowStatement stmt, Set<IFieldDeclaration> constants) {
		return new HashSet<IFieldDeclaration>();
	}

	@Override
	public Set<IFieldDeclaration> visit(IDoLoop block, Set<IFieldDeclaration> constants) {
		return visit(block.getBody(), constants);
	}

	@Override
	public Set<IFieldDeclaration> visit(IForEachLoop block, Set<IFieldDeclaration> constants) {
		return visit(block.getBody(), constants);
	}

	@Override
	public Set<IFieldDeclaration> visit(IForLoop block, Set<IFieldDeclaration> constants) {
		Set<IFieldDeclaration> foundConstants = new HashSet<IFieldDeclaration>();
		foundConstants.addAll(visit(block.getInit(), constants));
		foundConstants.addAll(visit(block.getBody(), constants));
		return foundConstants;
	}

	@Override
	public Set<IFieldDeclaration> visit(IIfElseBlock block, Set<IFieldDeclaration> constants) {
		Set<IFieldDeclaration> foundConstants = new HashSet<IFieldDeclaration>();
		foundConstants.addAll(visit(block.getThen(), constants));
		foundConstants.addAll(visit(block.getElse(), constants));
		return foundConstants;
	}

	@Override
	public Set<IFieldDeclaration> visit(ILockBlock stmt, Set<IFieldDeclaration> constants) {
		return visit(stmt.getBody(), constants);
	}

	@Override
	public Set<IFieldDeclaration> visit(ISwitchBlock block, Set<IFieldDeclaration> constants) {
		Set<IFieldDeclaration> foundConstants = new HashSet<IFieldDeclaration>();
		foundConstants.addAll(visit(block.getDefaultSection(), constants));

		for (ICaseBlock caseBlock : block.getSections()) {
			foundConstants.addAll(visit(caseBlock.getBody(), constants));
		}
		return foundConstants;
	}

	@Override
	public Set<IFieldDeclaration> visit(ITryBlock block, Set<IFieldDeclaration> constants) {
		Set<IFieldDeclaration> foundConstants = new HashSet<IFieldDeclaration>();
		foundConstants.addAll(visit(block.getBody(), constants));

		for (ICatchBlock catchBlock : block.getCatchBlocks()) {
			foundConstants.addAll(visit(catchBlock.getBody(), constants));
		}
		return foundConstants;
	}

	@Override
	public Set<IFieldDeclaration> visit(IUncheckedBlock block, Set<IFieldDeclaration> constants) {
		return visit(block.getBody(), constants);
	}

	@Override
	public Set<IFieldDeclaration> visit(IUnsafeBlock block, Set<IFieldDeclaration> constants) {
		return new HashSet<IFieldDeclaration>();
	}

	@Override
	public Set<IFieldDeclaration> visit(IUsingBlock block, Set<IFieldDeclaration> constants) {
		return visit(block.getBody(), constants);
	}

	@Override
	public Set<IFieldDeclaration> visit(IWhileLoop block, Set<IFieldDeclaration> constants) {
		return visit(block.getBody(), constants);
	}

	@Override
	public Set<IFieldDeclaration> visit(IEventReference eventRef, Set<IFieldDeclaration> constants) {
		return new HashSet<IFieldDeclaration>();
	}

	@Override
	public Set<IFieldDeclaration> visit(IFieldReference fieldRef, Set<IFieldDeclaration> constants) {
		return new HashSet<IFieldDeclaration>();
	}

	@Override
	public Set<IFieldDeclaration> visit(IMethodReference methodRef, Set<IFieldDeclaration> constants) {
		return new HashSet<IFieldDeclaration>();
	}

	@Override
	public Set<IFieldDeclaration> visit(IPropertyReference propertyRef, Set<IFieldDeclaration> constants) {
		return new HashSet<IFieldDeclaration>();
	}

	@Override
	public Set<IFieldDeclaration> visit(IVariableReference varRef, Set<IFieldDeclaration> constants) {
		return new HashSet<IFieldDeclaration>();
	}

	@Override
	public Set<IFieldDeclaration> visit(IUnknownReference unknownRef, Set<IFieldDeclaration> constants) {
		return new HashSet<IFieldDeclaration>();
	}

	@Override
	public Set<IFieldDeclaration> visit(IUnknownStatement unknownStmt, Set<IFieldDeclaration> constants) {
		return new HashSet<IFieldDeclaration>();
	}

	@Override
	public Set<IFieldDeclaration> visit(IEventSubscriptionStatement stmt, Set<IFieldDeclaration> constants) {
		return stmt.getReference().accept(this, constants);
	}

	public Set<IFieldDeclaration> visit(List<IStatement> stmts, Set<IFieldDeclaration> constants) {
		Set<IFieldDeclaration> foundConstants = new HashSet<IFieldDeclaration>();
		for (IStatement stmt : stmts) {
			foundConstants.addAll(stmt.accept(this, constants));
		}
		return foundConstants;
	}

}
