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
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.pointsto.analysis.utils.LanguageOptions;

public class DistinctPropertyParameterReference implements DistinctReference {

	private final IVariableReference reference;
	private final ITypeName type;
	private final IPropertyName property;

	public DistinctPropertyParameterReference(LanguageOptions languageOptions, IPropertyName property) {
		this.reference = variableReference(languageOptions.getPropertyParameterName());
		this.type = property.getValueType();
		this.property = property;
	}

	public DistinctPropertyParameterReference(IParameterName parameter, IPropertyName property) {
		this(parameter.getName(), parameter.getValueType(), property);
	}

	public DistinctPropertyParameterReference(String name, ITypeName type, IPropertyName property) {
		this.reference = variableReference(name);
		this.type = type;
		this.property = property;
	}

	@Override
	public IVariableReference getReference() {
		return reference;
	}

	@Override
	public ITypeName getType() {
		return type;
	}

	public IPropertyName getProperty() {
		return property;
	}

	@Override
	public <TReturn, TContext> TReturn accept(DistinctReferenceVisitor<TReturn, TContext> visitor, TContext context) {
		return visitor.visit(this, context);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((property == null) ? 0 : property.hashCode());
		result = prime * result + ((reference == null) ? 0 : reference.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		DistinctPropertyParameterReference other = (DistinctPropertyParameterReference) obj;
		if (property == null) {
			if (other.property != null)
				return false;
		} else if (!property.equals(other.property))
			return false;
		if (reference == null) {
			if (other.reference != null)
				return false;
		} else if (!reference.equals(other.reference))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(DistinctPropertyParameterReference.class)
				.add("name", reference.getIdentifier()).add("type", type).add("property", property).toString();
	}
}