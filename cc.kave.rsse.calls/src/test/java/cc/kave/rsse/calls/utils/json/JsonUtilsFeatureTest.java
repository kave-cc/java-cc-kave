/**
 * Copyright 2018 University of Zurich
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.kave.rsse.calls.utils.json;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.utils.io.json.JsonUtils;
import cc.kave.rsse.calls.model.features.ClassContextFeature;
import cc.kave.rsse.calls.model.features.DefinitionFeature;
import cc.kave.rsse.calls.model.features.IFeature;
import cc.kave.rsse.calls.model.features.MethodContextFeature;
import cc.kave.rsse.calls.model.features.TypeFeature;
import cc.kave.rsse.calls.model.features.UsageSiteFeature;
import cc.kave.rsse.calls.model.usages.impl.Definitions;
import cc.kave.rsse.calls.utils.json.JsonUtilsCcKaveRsseCalls;

public class JsonUtilsFeatureTest {

	private static final ITypeName SOME_TYPE = Names.newType("T,P");
	private static final IMethodName SOME_METHOD = Names.newMethod("[p:void] [T,P].m()");

	@Before
	public void setup() {
		JsonUtilsCcKaveRsseCalls.registerJsonAdapters();
	}

	@After
	public void teardown() {
		JsonUtils.resetAllConfiguration();
	}

	@Test
	public void callFeature() {
		assertRoundtrip(new UsageSiteFeature(SOME_METHOD));
	}

	@Test
	public void classFeature() {
		assertRoundtrip(new ClassContextFeature(SOME_TYPE));
	}

	@Test
	public void definitionFeature() {
		assertRoundtrip(new DefinitionFeature(Definitions.definedByConstant()));
	}

	@Test
	public void FirstMethodFeature() {
		assertRoundtrip(new MethodContextFeature(SOME_METHOD));
	}

	@Test
	public void TypeFeature() {
		assertRoundtrip(new TypeFeature(SOME_TYPE));
	}

	private void assertRoundtrip(IFeature f) {
		String j1 = JsonUtils.toJson(f, IFeature.class);
		Assert.assertTrue(j1.contains("$type"));
		IFeature f1 = JsonUtils.fromJson(j1, IFeature.class);
		Assert.assertEquals(f, f1);

		String j2 = JsonUtils.toJson(f, f.getClass());
		Assert.assertTrue(j2.contains("$type"));
		IFeature f2 = JsonUtils.fromJson(j2, f.getClass());
		Assert.assertEquals(f, f2);
	}
}