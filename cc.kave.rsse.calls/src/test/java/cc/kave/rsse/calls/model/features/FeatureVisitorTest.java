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
package cc.kave.rsse.calls.model.features;

import org.junit.Test;

public class FeatureVisitorTest {

	@Test
	public void visitorMethodsArePreimplemented() {
		IFeatureVisitor visitor = new FeatureVisitor();
		visitor.visit((TypeFeature) null);
		visitor.visit((ClassContextFeature) null);
		visitor.visit((MethodContextFeature) null);
		visitor.visit((DefinitionFeature) null);
		visitor.visit((UsageSiteFeature) null);
	}
}