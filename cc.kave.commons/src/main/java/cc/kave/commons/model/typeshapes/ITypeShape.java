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

import cc.kave.commons.model.naming.codeelements.IEventName;
import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.naming.types.IDelegateTypeName;
import cc.kave.commons.model.naming.types.ITypeName;

public interface ITypeShape {

	@Nonnull
	ITypeHierarchy getTypeHierarchy();

	void setTypeHierarchy(ITypeHierarchy th);

	@Nonnull
	Set<ITypeName> getNestedTypes();

	@Nonnull
	Set<IDelegateTypeName> getDelegates();

	@Nonnull
	Set<IMemberHierarchy<IEventName>> getEventHierarchies();

	@Nonnull
	Set<IFieldName> getFields();

	@Nonnull
	Set<IMemberHierarchy<IMethodName>> getMethodHierarchies();

	@Nonnull
	Set<IMemberHierarchy<IPropertyName>> getPropertyHierarchies();
}