/**
 * Copyright 2015 Simon Reuß
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

/**
 * A {@link DescentStrategy} that descends only into non-entry point methods that are not yet contained in the current
 * {@link Callpath}.
 */
public class SimpleDescentStrategy implements DescentStrategy {

	private static final int MAX_CALL_DEPTH = 10;

	@Override
	public boolean descent(IMethodDeclaration methodDecl, Callpath currentCallpath) {
		int currentCallDepth = currentCallpath.size();
		return currentCallDepth < MAX_CALL_DEPTH && !methodDecl.isEntryPoint()
				&& !currentCallpath.contains(methodDecl.getName());
	}

	@Override
	public boolean descent(IPropertyDeclaration propertyDecl, Callpath currentCallpath) {
		return false;
	}

}
