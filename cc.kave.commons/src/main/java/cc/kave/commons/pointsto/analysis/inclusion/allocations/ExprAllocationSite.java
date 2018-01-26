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
package cc.kave.commons.pointsto.analysis.inclusion.allocations;

import com.google.common.base.MoreObjects;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.IExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IBinaryExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IUnaryExpression;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;

/**
 * Used to represent allocations due to expressions like
 * {@link IConstantValueExpression}, {@link IUnaryExpression} and
 * {@link IBinaryExpression}.
 */
public class ExprAllocationSite implements AllocationSite {

	private final IExpression expr;

	public ExprAllocationSite(IExpression expr) {
		this.expr = expr;
	}

	public IExpression getExpr() {
		return expr;
	}

	@Override
	public ITypeName getType() {
		return Names.getUnknownType();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((expr == null) ? 0 : expr.hashCode());
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
		ExprAllocationSite other = (ExprAllocationSite) obj;
		if (expr != other.expr)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(ExprAllocationSite.class).add("expr", expr).toString();
	}
}