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
package cc.kave.rsse.calls.recs.pbn;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.rsse.calls.model.usages.IDefinition;
import cc.kave.rsse.calls.model.usages.impl.Definitions;

public class PBNModelConstants {

	public static final ITypeName DUMMY_CCTX = Names.newType("__DUMMY__, ???");
	public static final IMethodName DUMMY_MCTX = Names.newMethod("[?] [__DUMMY__, ???].???()");
	public static final IDefinition DUMMY_DEFINITION = Definitions.definedByReturnValue(DUMMY_MCTX);

	public static final String STATE_TRUE = "t";
	public static final String STATE_FALSE = "f";
}