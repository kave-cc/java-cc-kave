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
package cc.kave.commons.pointsto.extraction;

import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;

/***
 * A {@link DescentStrategy} that does not descent into any method or property.
 */
public class NopDescentStrategy implements DescentStrategy {

	@Override
	public boolean descent(IMethodDeclaration methodDecl, Callpath currentCallpath) {
		return false;
	}

	@Override
	public boolean descent(IPropertyDeclaration propertyDecl, Callpath currentCallpath) {
		return false;
	}

}
