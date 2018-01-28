/**
 * Copyright 2018 University of Zurich
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package cc.kave.commons.utils.ssts.completioninfo;

import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.expressions.assignable.ICompletionExpression;

public interface ICompletionInfo {

	/**
	 * get the completion expression that is contained in the SST.
	 */
	ICompletionExpression getCompletionExpr();

	/**
	 * type for which the completion is triggered.
	 * 
	 * @return either the type of a static member completion or the declared type of
	 *         the variable on which an instance member is being completed.
	 */
	ITypeName getTriggeredType();

	/**
	 * can be used to check whether the {@link ICompletionExpression} is used in a
	 * place, in which a specific target type is expected, i.e., on the right-hand
	 * side of an assignment.
	 */
	boolean hasExpectedType();

	/**
	 * returns the type that is expected from the {@link ICompletionExpression}.
	 * 
	 * @return {@link ITypeName} that is expected from the completion expression or
	 *         <code>null</code>, if nothing is being expected (e.g., method call
	 *         with no assignment).
	 */
	ITypeName getExpectedType();
}