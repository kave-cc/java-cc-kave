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

import static cc.kave.commons.model.ssts.impl.SSTUtil.*;
import static cc.kave.commons.model.ssts.impl.transformation.BooleanDeclarationUtil.TRUE;
import static cc.kave.commons.model.ssts.impl.transformation.BooleanDeclarationUtil.define;
import static cc.kave.commons.model.ssts.impl.transformation.BooleanDeclarationUtil.mainCondition;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.IDoLoop;
import cc.kave.commons.model.ssts.expressions.ILoopHeaderExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.loopheader.ILoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.impl.blocks.IfElseBlock;
import cc.kave.commons.model.ssts.impl.blocks.WhileLoop;
import cc.kave.commons.model.ssts.impl.statements.BreakStatement;
import cc.kave.commons.model.ssts.impl.transformation.AbstractStatementNormalizationVisitor;
import cc.kave.commons.model.ssts.statements.IReturnStatement;

public class DoLoopNormalizationVisitor extends AbstractStatementNormalizationVisitor<Void> {
	@Override
	public List<IStatement> visit(IDoLoop block, Void context) {
		// normalize inner loops
		super.visit(block, context);

		// extract do-loop condition
		ILoopHeaderExpression loopHeader = block.getCondition();
		if (!(loopHeader instanceof ILoopHeaderBlockExpression))
			return null;

		List<IStatement> loopHeaderBody = ((ILoopHeaderBlockExpression) loopHeader).getBody();
		List<IStatement> loopHeaderDeclarations = loopHeaderBody.subList(0, loopHeaderBody.size() - 1);

		IStatement loopHeaderReturn = loopHeaderBody.get(loopHeaderBody.size() - 1);
		if (!(loopHeaderReturn instanceof IReturnStatement))
			return null;

		ISimpleExpression doCondition = ((IReturnStatement) loopHeaderReturn).getExpression();

		// create break condition
		// (break loop when do-condition evaluates to false)
		List<IStatement> breakCondition = define("breakCondition", not(doCondition));

		IfElseBlock ifBlock = new IfElseBlock();
		ifBlock.setCondition(mainCondition(breakCondition));
		ifBlock.setThen(Lists.newArrayList(breakStatement()));

		// assemble while loop
		List<IStatement> whileBody = new ArrayList<IStatement>();
		whileBody.addAll(block.getBody());
		whileBody.addAll(loopHeaderDeclarations);
		whileBody.addAll(breakCondition);
		whileBody.add(ifBlock);

		WhileLoop whileLoop = new WhileLoop();
		whileLoop.setBody(whileBody);
		whileLoop.setCondition(TRUE);

		List<IStatement> normalized = new ArrayList<IStatement>();
		normalized.add(whileLoop);
		return normalized;
	}

	private ISimpleExpression getCondition(ILoopHeaderExpression loopHeader) {
		if (loopHeader instanceof ISimpleExpression)
			return (ISimpleExpression) loopHeader;
		if (loopHeader instanceof ILoopHeaderBlockExpression) {
			List<IStatement> loopHeaderBody = ((ILoopHeaderBlockExpression) loopHeader).getBody();

			IStatement loopHeaderReturn = loopHeaderBody.get(loopHeaderBody.size() - 1);
			if (!(loopHeaderReturn instanceof IReturnStatement))
				return null;
			return ((IReturnStatement) loopHeaderReturn).getExpression();
		}
		return null;
	}

}
