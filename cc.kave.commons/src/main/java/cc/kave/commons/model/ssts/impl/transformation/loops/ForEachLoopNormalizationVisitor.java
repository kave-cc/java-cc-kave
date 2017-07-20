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
package cc.kave.commons.model.ssts.impl.transformation.loops;

import static cc.kave.commons.model.ssts.impl.SSTUtil.assign;
import static cc.kave.commons.model.ssts.impl.SSTUtil.declare;
import static cc.kave.commons.model.ssts.impl.SSTUtil.declareVar;
import static cc.kave.commons.model.ssts.impl.SSTUtil.loopHeader;
import static cc.kave.commons.model.ssts.impl.SSTUtil.refExpr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.returnStatement;
import static cc.kave.commons.model.ssts.impl.SSTUtil.variableReference;
import static cc.kave.commons.model.ssts.impl.transformation.loops.IteratorUtil.getNext;
import static cc.kave.commons.model.ssts.impl.transformation.loops.IteratorUtil.hasNext;
import static cc.kave.commons.model.ssts.impl.transformation.loops.IteratorUtil.iteratorInvocation;
import static cc.kave.commons.model.ssts.impl.transformation.loops.IteratorUtil.iteratorType;

import java.util.ArrayList;
import java.util.List;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.IForEachLoop;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ILoopHeaderExpression;
import cc.kave.commons.model.ssts.impl.blocks.WhileLoop;
import cc.kave.commons.model.ssts.impl.transformation.AbstractStatementNormalizationVisitor;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;

public class ForEachLoopNormalizationVisitor extends AbstractStatementNormalizationVisitor<Void> {
	private int iteratorCount = 0;
	private boolean isJava;

	public ForEachLoopNormalizationVisitor() {
		this.isJava = true;
	}

	// distinction between Java / C#
	public ForEachLoopNormalizationVisitor(boolean java) {
		this.isJava = java;
	}

	@Override
	public List<IStatement> visit(IForEachLoop block, Void context) {
		// normalize inner loops
		super.visit(block, context);

		IVariableReference loopedRef = block.getLoopedReference();
		IVariableDeclaration varDec = block.getDeclaration();

		// declare & initialize iterator
		IVariableDeclaration iteratorDec = iteratorDeclaration(varDec.getType());
		IVariableReference iterator = iteratorDec.getReference();
		IStatement iteratorInit = assign(iterator, iteratorInvocation(loopedRef, isJava));

		// create condition
		IVariableReference hasNext = variableReference("hasNext");
		IVariableDeclaration hasNextDec = declare(hasNext.getIdentifier(), Names.newType("p:bool"));
		ILoopHeaderExpression condition = loopHeader(hasNextDec, assign(hasNext, hasNext(iterator)),
				returnStatement(refExpr(hasNext)));

		// assign next element
		IAssignableExpression invokeNext = getNext(iterator, isJava);
		IStatement varAssign = assign(varDec.getReference(), invokeNext);

		// assemble while loop
		List<IStatement> whileBody = new ArrayList<IStatement>();
		whileBody.add(varDec);
		whileBody.add(varAssign);
		whileBody.addAll(block.getBody());
		WhileLoop whileLoop = new WhileLoop();
		whileLoop.setBody(whileBody);
		whileLoop.setCondition(condition);

		List<IStatement> normalized = new ArrayList<IStatement>();
		normalized.add(iteratorDec);
		normalized.add(iteratorInit);
		normalized.add(whileLoop);
		return normalized;
	}

	// ---------------------------- helpers -----------------------------------

	private String iteratorVariableName() {
		return "$it_" + iteratorCount++;
	}

	private IVariableDeclaration iteratorDeclaration(ITypeName paramType) {
		return declareVar(iteratorVariableName(), iteratorType(paramType));
	}
}