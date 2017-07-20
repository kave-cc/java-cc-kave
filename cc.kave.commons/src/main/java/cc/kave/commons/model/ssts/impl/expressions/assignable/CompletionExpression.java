/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.kave.commons.model.ssts.impl.expressions.assignable;

import java.util.ArrayList;

import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.expressions.assignable.ICompletionExpression;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;
import cc.kave.commons.utils.ToStringUtils;

public class CompletionExpression implements ICompletionExpression {

	private ITypeName typeReference;
	private IVariableReference variableReference;
	private String token;

	public CompletionExpression() {
		this.token = "";
	}

	@Override
	public Iterable<ISSTNode> getChildren() {
		return new ArrayList<ISSTNode>();
	}

	@Override
	public ITypeName getTypeReference() {
		return this.typeReference;
	}

	@Override
	public IVariableReference getVariableReference() {
		return this.variableReference;
	}

	@Override
	public String getToken() {
		return token;
	}

	public void setTypeReference(ITypeName typeReference) {
		this.typeReference = typeReference;
	}

	public void setObjectReference(IVariableReference objectReference) {
		this.variableReference = objectReference;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public int hashCode() {
		int hcTypeRef = this.typeReference != null ? this.typeReference.hashCode() : 0;
		int hcObjRef = this.variableReference != null ? this.variableReference.hashCode() : 0;
		int hcToken = this.token.hashCode();
		return (3 + hcToken * 397 + hcTypeRef * 23846 + hcObjRef);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CompletionExpression other = (CompletionExpression) obj;
		if (variableReference == null) {
			if (other.variableReference != null)
				return false;
		} else if (!variableReference.equals(other.variableReference))
			return false;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		if (typeReference == null) {
			if (other.typeReference != null)
				return false;
		} else if (!typeReference.equals(other.typeReference))
			return false;
		return true;
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

	@Override
	public String toString() {
		return ToStringUtils.toString(this);
	}
}