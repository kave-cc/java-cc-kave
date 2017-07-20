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

import java.util.List;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.IDoLoop;
import cc.kave.commons.model.ssts.blocks.IForLoop;
import cc.kave.commons.model.ssts.blocks.IWhileLoop;
import cc.kave.commons.model.ssts.statements.IContinueStatement;
import cc.kave.commons.model.ssts.visitor.ISSTNode;

public class StepInsertionContext extends StatementInsertionContext {

	public StepInsertionContext(List<IStatement> statements) {
		super(statements);
	}

	/**
	 * Insert loop step before continue statements.
	 */
	@Override
	public boolean insertBefore(IStatement stmt) {
		return stmt instanceof IContinueStatement;
	}

	/**
	 * Skip loops (as we don't want to insert the current loop step before
	 * continue statements of inner loops)
	 */
	@Override
	public boolean skip(ISSTNode node) {
		return (node instanceof IForLoop) || (node instanceof IWhileLoop) || (node instanceof IDoLoop);
	}
}
