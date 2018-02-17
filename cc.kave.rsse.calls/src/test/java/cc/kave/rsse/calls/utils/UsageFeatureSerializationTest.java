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
package cc.kave.rsse.calls.utils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.utils.io.json.JsonUtils;
import cc.kave.rsse.calls.usages.DefinitionSites;
import cc.kave.rsse.calls.usages.features.CallFeature;
import cc.kave.rsse.calls.usages.features.ClassFeature;
import cc.kave.rsse.calls.usages.features.DefinitionFeature;
import cc.kave.rsse.calls.usages.features.FirstMethodFeature;
import cc.kave.rsse.calls.usages.features.ParameterFeature;
import cc.kave.rsse.calls.usages.features.SuperMethodFeature;
import cc.kave.rsse.calls.usages.features.TypeFeature;
import cc.kave.rsse.calls.usages.features.UsageFeature;

public class UsageFeatureSerializationTest {

	private static final ITypeName SOME_TYPE = Names.newType("T,P");
	private static final IMethodName SOME_METHOD = Names.newMethod("[p:void] [T,P].m()");

	@Before
	public void setup() {
		RsseCallsJsonUtils.registerJsonAdapters();
	}

	@After
	public void teardown() {
		JsonUtils.resetAllConfiguration();
	}

	@Test
	public void callFeature() {
		assertRoundtrip(new CallFeature(SOME_METHOD));
	}

	@Test
	public void classFeature() {
		assertRoundtrip(new ClassFeature(SOME_TYPE));
	}

	@Test
	public void definitionFeature() {
		assertRoundtrip(new DefinitionFeature(DefinitionSites.createDefinitionByConstant()));
	}

	@Test
	public void FirstMethodFeature() {
		assertRoundtrip(new FirstMethodFeature(SOME_METHOD));
	}

	@Test
	public void ParameterFeature() {
		assertRoundtrip(new ParameterFeature(SOME_METHOD, 13));
	}

	@Test
	public void SuperMethodFeature() {
		assertRoundtrip(new SuperMethodFeature(SOME_METHOD));
	}

	@Test
	public void TypeFeature() {
		assertRoundtrip(new TypeFeature(SOME_TYPE));
	}

	private void assertRoundtrip(UsageFeature f) {
		String j1 = JsonUtils.toJson(f, UsageFeature.class);
		Assert.assertTrue(j1.contains("$type"));
		UsageFeature f1 = JsonUtils.fromJson(j1, UsageFeature.class);
		Assert.assertEquals(f, f1);

		String j2 = JsonUtils.toJson(f, f.getClass());
		Assert.assertTrue(j2.contains("$type"));
		UsageFeature f2 = JsonUtils.fromJson(j2, f.getClass());
		Assert.assertEquals(f, f2);
	}
}