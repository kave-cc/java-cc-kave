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

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;

/**
 * Controls whether the {@link PointsToUsageExtractor} descends into a specific method or property call of the same
 * {@link Context} given the current {@link Callpath}.
 */
public interface DescentStrategy {

	boolean descent(IMethodDeclaration methodDecl, Callpath currentCallpath);

	boolean descent(IPropertyDeclaration propertyDecl, Callpath currentCallpath);

}
