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

import com.google.common.base.MoreObjects;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.references.IVariableReference;;

public class DistinctMethodParameterReference implements DistinctReference {

	private final IParameterName parameter;
	private final IMethodName method;

	public DistinctMethodParameterReference(IParameterName parameter, IMethodName method) {
		this.parameter = parameter;
		this.method = method;
	}

	@Override
	public IVariableReference getReference() {
		return variableReference(parameter.getName());
	}

	@Override
	public ITypeName getType() {
		return parameter.getValueType();
	}

	public IMethodName getMethod() {
		return method;
	}

	@Override
	public <TReturn, TContext> TReturn accept(DistinctReferenceVisitor<TReturn, TContext> visitor, TContext context) {
		return visitor.visit(this, context);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((method == null) ? 0 : method.hashCode());
		result = prime * result + ((parameter == null) ? 0 : parameter.hashCode());
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
		DistinctMethodParameterReference other = (DistinctMethodParameterReference) obj;
		if (method == null) {
			if (other.method != null)
				return false;
		} else if (!method.equals(other.method))
			return false;
		if (parameter == null) {
			if (other.parameter != null)
				return false;
		} else if (!parameter.equals(other.parameter))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(DistinctMethodParameterReference.class)
				.add("method", method.getDeclaringType().getName() + "." + method.getName())
				.add("name", parameter.getName()).toString();
	}
}