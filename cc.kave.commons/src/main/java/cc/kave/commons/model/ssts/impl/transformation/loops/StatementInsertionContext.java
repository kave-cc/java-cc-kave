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
import cc.kave.commons.model.ssts.visitor.ISSTNode;

public abstract class StatementInsertionContext {
	protected List<IStatement> statements;

	public StatementInsertionContext(List<IStatement> statements) {
		this.statements = statements;
	}

	public boolean insertBefore(IStatement stmt) {
		return false;
	}

	public boolean insertAfter(IStatement stmt) {
		return false;
	}
	
	public boolean skip(ISSTNode node) {
		return false;
	}
}
