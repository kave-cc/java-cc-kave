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
package cc.kave.commons.pointsto.analysis.utils;

import java.util.function.Predicate;

import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.pointsto.analysis.DeclarationMapper;

public class PropertyAsFieldPredicate implements Predicate<IPropertyName> {

	private final DeclarationMapper declarationMapper;

	private final LanguageOptions languageOptions = LanguageOptions.getInstance();

	public PropertyAsFieldPredicate(DeclarationMapper declarationMapper) {
		this.declarationMapper = declarationMapper;
	}

	/**
	 * Tests whether the specified property should be treated as a field ({@code true}) or method ({@code false}).
	 */
	@Override
	public boolean test(IPropertyName property) {
		// treat properties of other classes and auto-implemented properties as field accesses
		IPropertyDeclaration propertyDecl = declarationMapper.get(property);
		return propertyDecl == null || languageOptions.isAutoImplementedProperty(propertyDecl);
	}
}