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

import java.util.List;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.IForEachLoop;
import cc.kave.commons.model.ssts.blocks.IForLoop;
import cc.kave.commons.model.ssts.impl.transformation.AbstractStatementNormalizationVisitor;

public class LoopNormalizationVisitor extends AbstractStatementNormalizationVisitor<Void> {

	private ForLoopNormalizationVisitor forVisitor;
	private ForEachLoopNormalizationVisitor forEachVisitor;
	
	public LoopNormalizationVisitor() {
		this.forVisitor = new ForLoopNormalizationVisitor();
		this.forEachVisitor = new ForEachLoopNormalizationVisitor();
	}

	@Override
	public List<IStatement> visit(IForEachLoop block, Void context) {
		super.visit(block, context);
		return forEachVisitor.visit(block, context);
	}

	@Override
	public List<IStatement> visit(IForLoop block, Void context) {
		super.visit(block, context);
		return forVisitor.visit(block, context);
	}
	
}
