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
package cc.kave.rsse.calls.model;

import static cc.kave.commons.model.naming.Names.getUnknownMethod;
import static cc.kave.commons.model.naming.Names.getUnknownType;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByUnknown;

import cc.kave.rsse.calls.model.features.ClassContextFeature;
import cc.kave.rsse.calls.model.features.DefinitionFeature;
import cc.kave.rsse.calls.model.features.MethodContextFeature;
import cc.kave.rsse.calls.model.features.TypeFeature;

public class Constants {

	public static final TypeFeature UNKNOWN_TF = new TypeFeature(getUnknownType());
	public static final ClassContextFeature UNKNOWN_CCF = new ClassContextFeature(getUnknownType());
	public static final MethodContextFeature UNKNOWN_MCF = new MethodContextFeature(getUnknownMethod());
	public static final DefinitionFeature UNKNOWN_DF = new DefinitionFeature(definedByUnknown());
}