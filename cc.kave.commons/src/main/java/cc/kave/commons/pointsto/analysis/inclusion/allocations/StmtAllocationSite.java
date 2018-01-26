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
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;

public class StmtAllocationSite implements AllocationSite {

	private final IStatement stmt;
	private final ITypeName type;

	public StmtAllocationSite(IStatement stmt) {
		this(stmt, inferType(stmt));
	}

	public StmtAllocationSite(IStatement stmt, ITypeName type) {
		this.stmt = stmt;
		this.type = type;
	}

	private static ITypeName inferType(IStatement stmt) {
		if (stmt instanceof IAssignment) {
			IAssignableExpression expr = ((IAssignment) stmt).getExpression();
			if (expr instanceof IInvocationExpression) {
				IInvocationExpression invocationExpr = (IInvocationExpression) expr;
				IMethodName method = invocationExpr.getMethodName();
				if (method.isConstructor()) {
					return method.getDeclaringType();
				}
			}
		} else if (stmt instanceof IVariableDeclaration) {
			return ((IVariableDeclaration) stmt).getType();
		}
		return Names.getUnknownType();
	}

	@Override
	public ITypeName getType() {
		return type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((stmt == null) ? 0 : stmt.hashCode());
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
		StmtAllocationSite other = (StmtAllocationSite) obj;
		if (stmt != other.stmt)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else {
			if (!type.equals(other.type))
				return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(StmtAllocationSite.class).add("stmt", stmt).add("type", type).toString();
	}
}