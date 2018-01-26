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

import cc.kave.commons.assertions.Asserts;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.references.IIndexAccessReference;

public class DistinctIndexAccessReference implements DistinctReference {

	private final IIndexAccessReference reference;
	private final DistinctReference baseReference;

	public DistinctIndexAccessReference(IIndexAccessReference reference, DistinctReference baseReference) {
		this.reference = reference;
		this.baseReference = baseReference;
		Asserts.assertTrue(reference.getExpression().getReference().equals(baseReference.getReference()));
	}

	@Override
	public IIndexAccessReference getReference() {
		return reference;
	}

	public DistinctReference getBaseReference() {
		return baseReference;
	}

	@Override
	public ITypeName getType() {
		ITypeName baseType = baseReference.getType();
		return baseType.isArray() ? baseType.asArrayTypeName().getArrayBaseType() : Names.getUnknownType();
	}

	@Override
	public <TReturn, TContext> TReturn accept(DistinctReferenceVisitor<TReturn, TContext> visitor, TContext context) {
		return visitor.visit(this, context);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((baseReference == null) ? 0 : baseReference.hashCode());
		result = prime * result + ((reference == null) ? 0 : reference.hashCode());
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
		DistinctIndexAccessReference other = (DistinctIndexAccessReference) obj;
		if (baseReference == null) {
			if (other.baseReference != null)
				return false;
		} else if (!baseReference.equals(other.baseReference))
			return false;
		if (reference == null) {
			if (other.reference != null)
				return false;
		} else if (!reference.equals(other.reference))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(DistinctIndexAccessReference.class).add("base", getBaseReference())
				.toString();
	}

}
