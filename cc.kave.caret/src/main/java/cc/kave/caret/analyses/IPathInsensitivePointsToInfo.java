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
package cc.kave.caret.analyses;

import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.ssts.IMemberDeclaration;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;

public interface IPathInsensitivePointsToInfo {

	boolean hasKey(ISST k);

	Object getAbstractObject(ISST k);

	boolean hasKey(IMemberDeclaration k);

	Object getAbstractObject(IMemberDeclaration k);

	boolean hasKey(ILambdaExpression k);

	Object getAbstractObject(ILambdaExpression k);

	boolean hasKey(IParameterName k);

	Object getAbstractObject(IParameterName k);

	boolean hasKey(IReference k);

	Object getAbstractObject(IReference k);
}