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

import static cc.kave.commons.model.ssts.impl.transformation.BooleanDeclarationUtil.TRUE;

import java.util.ArrayList;
import java.util.List;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.IForLoop;
import cc.kave.commons.model.ssts.expressions.ILoopHeaderExpression;
import cc.kave.commons.model.ssts.expressions.loopheader.ILoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.impl.blocks.WhileLoop;
import cc.kave.commons.model.ssts.impl.transformation.AbstractStatementNormalizationVisitor;
import cc.kave.commons.model.ssts.statements.IContinueStatement;

public class ForLoopNormalizationVisitor extends AbstractStatementNormalizationVisitor<Void> {

	private StatementInsertionVisitor insertionVisitor;

	public ForLoopNormalizationVisitor() {
		this.insertionVisitor = new StatementInsertionVisitor();
	}

	@Override
	public List<IStatement> visit(IForLoop block, Void context) {
		// normalize inner loops
		super.visit(block, context);

		ILoopHeaderExpression forCondition = block.getCondition();
		ILoopHeaderExpression whileCondition = whileCondition(forCondition);
		List<IStatement> whileBody = whileBody(block);

		WhileLoop whileLoop = new WhileLoop();
		whileLoop.setCondition(whileCondition);
		whileLoop.setBody(whileBody);

		List<IStatement> normalized = new ArrayList<IStatement>();
		normalized.addAll(block.getInit());
		normalized.add(whileLoop);
		return normalized;
	}
	
	/**
	 * Empty condition of for loop is translated to 'true' for while loop.
	 */
	private ILoopHeaderExpression whileCondition(ILoopHeaderExpression forCondition) {
		boolean forConditionEmpty = (forCondition instanceof ILoopHeaderBlockExpression)
				&& ((ILoopHeaderBlockExpression) forCondition).getBody().isEmpty();

		return forConditionEmpty ? TRUE : forCondition;
	}

	private List<IStatement> whileBody(IForLoop block) {
		List<IStatement> whileBody = replicateLoopStep(block.getBody(), block.getStep());

		/* no need to append loop step after 'continue' (dead code) */
		if (!containsContinue(whileBody)) {
			whileBody.addAll(block.getStep());
		}
		return whileBody;
	}

	private boolean containsContinue(List<IStatement> statements) {
		return statements.stream().anyMatch(s -> s instanceof IContinueStatement);
	}

	/**
	 * Place loop step before continue statements.
	 */
	private List<IStatement> replicateLoopStep(List<IStatement> statements, List<IStatement> loopStep) {
		return insertionVisitor.visit(statements, new StepInsertionContext(loopStep));
	}
	
}
