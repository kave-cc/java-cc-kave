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
package cc.kave.commons.model.ssts;

import java.util.Set;

import javax.annotation.Nonnull;

import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.declarations.IDelegateDeclaration;
import cc.kave.commons.model.ssts.declarations.IEventDeclaration;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.visitor.ISSTNode;

public interface ISST extends ISSTNode {

	@Nonnull
	ITypeName getEnclosingType();

	@Nonnull
	Set<IFieldDeclaration> getFields();

	@Nonnull
	Set<IPropertyDeclaration> getProperties();

	@Nonnull
	Set<IMethodDeclaration> getMethods();

	@Nonnull
	Set<IEventDeclaration> getEvents();

	@Nonnull
	Set<IDelegateDeclaration> getDelegates();

	@Nonnull
	Set<IMethodDeclaration> getEntryPoints();

	@Nonnull
	Set<IMethodDeclaration> getNonEntryPoints();

	@Nonnull
	String getPartialClassIdentifier();

	boolean isPartialClass();
}