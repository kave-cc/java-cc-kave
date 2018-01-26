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

import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;;

public class DistinctLambdaParameterReference implements DistinctReference {

	private final IParameterName parameter;
	private final ILambdaExpression lambda;

	public DistinctLambdaParameterReference(IParameterName parameter, ILambdaExpression lambda) {
		this.parameter = parameter;
		this.lambda = lambda;
	}

	@Override
	public IReference getReference() {
		return variableReference(parameter.getName());
	}

	@Override
	public ITypeName getType() {
		return parameter.getValueType();
	}

	public ILambdaExpression getLambda() {
		return lambda;
	}

	@Override
	public <TReturn, TContext> TReturn accept(DistinctReferenceVisitor<TReturn, TContext> visitor, TContext context) {
		return visitor.visit(this, context);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lambda == null) ? 0 : lambda.hashCode());
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
		DistinctLambdaParameterReference other = (DistinctLambdaParameterReference) obj;
		if (lambda == null) {
			if (other.lambda != null)
				return false;
		} else if (lambda != other.lambda) // reference equality
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
		return MoreObjects.toStringHelper(DistinctLambdaParameterReference.class).add("name", parameter.getName())
				.add("type", parameter.getValueType().getName()).toString();
	}
}