/**
 * Copyright 2016 Simon Reuß
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
package cc.kave.commons.pointsto.analysis.references;

import static cc.kave.commons.pointsto.analysis.utils.SSTBuilder.variableReference;

import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.blocks.ICatchBlock;;

public class DistinctCatchBlockParameterReference implements DistinctReference {

	private final ICatchBlock catchBlock;

	public DistinctCatchBlockParameterReference(ICatchBlock catchBlock) {
		this.catchBlock = catchBlock;
	}

	@Override
	public IReference getReference() {
		return variableReference(catchBlock.getParameter().getName());
	}

	@Override
	public ITypeName getType() {
		return catchBlock.getParameter().getValueType();
	}

	public ICatchBlock getCatchBlock() {
		return catchBlock;
	}

	@Override
	public <TReturn, TContext> TReturn accept(DistinctReferenceVisitor<TReturn, TContext> visitor, TContext context) {
		return visitor.visit(this, context);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((catchBlock == null) ? 0 : catchBlock.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DistinctCatchBlockParameterReference other = (DistinctCatchBlockParameterReference) obj;
		if (catchBlock == null) {
			if (other.catchBlock != null)
				return false;
		} else if (catchBlock != other.catchBlock) // reference equality
			return false;

		return true;
	}
}