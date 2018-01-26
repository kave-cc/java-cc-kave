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

import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.references.IPropertyReference;

public class DistinctPropertyReference extends DistinctMemberReference {

	public DistinctPropertyReference(IPropertyReference propertyRef, DistinctReference baseReference) {
		super(propertyRef, baseReference);
	}

	@Override
	public boolean isStaticMember() {
		return getReference().getPropertyName().isStatic();
	}

	@Override
	public IPropertyName getMemberName() {
		return getReference().getPropertyName();
	}

	@Override
	public IPropertyReference getReference() {
		return (IPropertyReference) super.getReference();
	}

	@Override
	public ITypeName getType() {
		return ((IPropertyReference) memberReference).getPropertyName().getValueType();
	}

	@Override
	public <TReturn, TContext> TReturn accept(DistinctReferenceVisitor<TReturn, TContext> visitor, TContext context) {
		return visitor.visit(this, context);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(DistinctPropertyReference.class).add("base", getBaseReference())
				.add("name", getReference().getPropertyName().getName()).toString();
	}
}