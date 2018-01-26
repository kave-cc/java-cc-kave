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
package cc.kave.commons.pointsto.analysis.names;

import static cc.kave.commons.pointsto.analysis.utils.GenericNameUtils.eraseGenericInstantiations;

import com.google.common.base.MoreObjects;

import cc.kave.commons.model.naming.types.ITypeName;

public abstract class DistinctMemberName {

	private final ITypeName valueType;
	private final ITypeName declaringType;
	private final String name;

	protected DistinctMemberName(ITypeName valueType, ITypeName declaringType, String name) {
		this.valueType = eraseGenericInstantiations(valueType);
		this.declaringType = eraseGenericInstantiations(declaringType);
		this.name = name;
	}

	public ITypeName getValueType() {
		return valueType;
	}

	public ITypeName getDeclaringType() {
		return declaringType;
	}

	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((declaringType == null) ? 0 : declaringType.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((valueType == null) ? 0 : valueType.hashCode());
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
		DistinctMemberName other = (DistinctMemberName) obj;
		if (declaringType == null) {
			if (other.declaringType != null)
				return false;
		} else if (!declaringType.equals(other.declaringType))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (valueType == null) {
			if (other.valueType != null)
				return false;
		} else if (!valueType.equals(other.valueType))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("declaringType", declaringType).add("name", name).toString();
	}
}