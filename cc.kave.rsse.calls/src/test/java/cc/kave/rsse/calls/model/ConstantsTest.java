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
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cc.kave.rsse.calls.model.features.ClassContextFeature;
import cc.kave.rsse.calls.model.features.DefinitionFeature;
import cc.kave.rsse.calls.model.features.MethodContextFeature;

public class ConstantsTest {

	@Test
	public void initForCoverage() {
		new Constants();
	}

	@Test
	public void unknownClassContextFeature() {
		assertEquals(new ClassContextFeature(getUnknownType()), Constants.UNKNOWN_CCF);
	}

	@Test
	public void unknownMethodContextFeature() {
		assertEquals(new MethodContextFeature(getUnknownMethod()), Constants.UNKNOWN_MCF);
	}

	@Test
	public void unknownDefinitionFeature() {
		assertEquals(new DefinitionFeature(definedByUnknown()), Constants.UNKNOWN_DF);
	}
}