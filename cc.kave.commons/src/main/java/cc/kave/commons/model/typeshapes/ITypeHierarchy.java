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
import javax.annotation.Nullable;

import cc.kave.commons.model.naming.types.ITypeName;

public interface ITypeHierarchy {
	/// <summary>
	/// The type at this level in the type hierarchy.
	/// </summary>
	@Nonnull
	ITypeName getElement();

	/// <summary>
	/// The direct superclass of the type at this level.
	/// </summary>
	@Nullable
	ITypeHierarchy getExtends();

	/// <summary>
	/// The interfaces directly implemented by the type at this level.
	/// </summary>
	@Nonnull

	Set<ITypeHierarchy> getImplements();

	/// <summary>
	/// <returns>Whether this type extends some superclass or implements any
	/// interfaces</returns>
	/// </summary>
	boolean hasSupertypes();

	/// <summary>
	/// <returns>Whether this type extends some superclass</returns>
	/// </summary>
	boolean hasSuperclass();

	/// <summary>
	/// <returns>Whether this type implements any interfaces</returns>
	/// </summary>
	boolean isImplementingInterfaces();

}
