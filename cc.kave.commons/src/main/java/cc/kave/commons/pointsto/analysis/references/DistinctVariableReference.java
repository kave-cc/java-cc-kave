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

import com.google.common.base.MoreObjects;

import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;

public class DistinctVariableReference implements DistinctReference {

	private final IVariableDeclaration varDecl;

	public DistinctVariableReference(IVariableDeclaration varDecl) {
		this.varDecl = varDecl;
	}

	@Override
	public IVariableReference getReference() {
		return varDecl.getReference();
	}

	@Override
	public ITypeName getType() {
		return varDecl.getType();
	}

	public IVariableDeclaration getDeclaration() {
		return varDecl;
	}

	@Override
	public <TReturn, TContext> TReturn accept(DistinctReferenceVisitor<TReturn, TContext> visitor, TContext context) {
		return visitor.visit(this, context);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((varDecl == null) ? 0 : varDecl.hashCode());
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
		DistinctVariableReference other = (DistinctVariableReference) obj;
		if (varDecl == null) {
			if (other.varDecl != null)
				return false;
		} else if (varDecl != other.varDecl) // reference equality
			return false;
		return true;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(DistinctVariableReference.class)
				.add("name", varDecl.getReference().getIdentifier()).add("type", varDecl.getType()).toString();
	}
}