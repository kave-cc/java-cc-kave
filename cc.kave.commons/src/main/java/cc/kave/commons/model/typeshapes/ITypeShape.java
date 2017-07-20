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

import java.util.Set;

import javax.annotation.Nonnull;

public interface ITypeShape {
	/// <summary>
	/// A description of the enclosing class, including its parent class and
	/// implemented interfaces.
	/// </summary>
	@Nonnull
	ITypeHierarchy getTypeHierarchy();

	void setTypeHierarchy(ITypeHierarchy typeHierarchy);

	/// <summary>
	/// All Methods that are overridden in the class under edit (including
	/// information about the first and super
	/// declaration).
	/// </summary>
	@Nonnull
	Set<IMethodHierarchy> getMethodHierarchies();

	void setMethodHierarchies(Set<IMethodHierarchy> methodHierarchies);

}
