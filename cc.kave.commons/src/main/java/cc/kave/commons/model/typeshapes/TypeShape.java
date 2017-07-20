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
package cc.kave.commons.model.typeshapes;

import java.util.HashSet;
import java.util.Set;

public class TypeShape implements ITypeShape {

	private ITypeHierarchy typeHierarchy;
	private Set<IMethodHierarchy> methodHierarchies;

	public TypeShape() {
		this.typeHierarchy = new TypeHierarchy();
		this.methodHierarchies = new HashSet<>();
	}

	@Override
	public ITypeHierarchy getTypeHierarchy() {
		return this.typeHierarchy;
	}

	@Override
	public void setTypeHierarchy(ITypeHierarchy typeHierarchy) {
		this.typeHierarchy = typeHierarchy;
	}

	@Override
	public Set<IMethodHierarchy> getMethodHierarchies() {
		return this.methodHierarchies;
	}

	@Override
	public void setMethodHierarchies(Set<IMethodHierarchy> methodHierarchies) {
		this.methodHierarchies = methodHierarchies;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((methodHierarchies == null) ? 0 : methodHierarchies.hashCode());
		result = prime * result + ((typeHierarchy == null) ? 0 : typeHierarchy.hashCode());
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
		TypeShape other = (TypeShape) obj;
		if (methodHierarchies == null) {
			if (other.methodHierarchies != null)
				return false;
		} else if (!methodHierarchies.equals(other.methodHierarchies))
			return false;
		if (typeHierarchy == null) {
			if (other.typeHierarchy != null)
				return false;
		} else if (!typeHierarchy.equals(other.typeHierarchy))
			return false;
		return true;
	}

}
