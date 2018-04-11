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

import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByConstructor;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByMemberAccessToField;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByMemberAccessToProperty;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByMethodParameter;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.utils.io.json.JsonUtils;
import cc.kave.rsse.calls.model.usages.IUsage;
import cc.kave.rsse.calls.model.usages.impl.Definitions;
import cc.kave.rsse.calls.model.usages.impl.NoUsage;
import cc.kave.rsse.calls.model.usages.impl.Usage;
import cc.kave.rsse.calls.model.usages.impl.UsageSites;

@SuppressWarnings("deprecation")
public class JsonUtilsUsageTest {

	@Before
	public void setup() {
		JsonUtilsCcKaveRsseCalls.registerJsonAdapters();
	}

	@After
	public void teardown() {
		JsonUtils.resetAllConfiguration();
	}

	@Test
	public void differentDefinitionSites_constant() {
		Usage q = q();
		q.definition = Definitions.definedByConstant();
		Assert.assertEquals(q, JsonUtils.fromJson(JsonUtils.toJson(q), IUsage.class));
	}

	@Test
	public void differentDefinitionSites_init() {
		Usage q = q();
		q.definition = definedByConstructor("[p:void] [T, P]..ctor()");
		Assert.assertEquals(q, JsonUtils.fromJson(JsonUtils.toJson(q), IUsage.class));
	}

	@Test
	public void differentDefinitionSites_field() {
		Usage q = q();
		q.definition = definedByMemberAccessToField("[p:int] [T, P]._f");
		Assert.assertEquals(q, JsonUtils.fromJson(JsonUtils.toJson(q), IUsage.class));
	}

	@Test
	public void differentDefinitionSites_param() {
		Usage q = q();
		q.definition = definedByMethodParameter("[p:int] [T3, P].M()", 2);
		Assert.assertEquals(q, JsonUtils.fromJson(JsonUtils.toJson(q), IUsage.class));
	}

	@Test
	public void differentDefinitionSites_property() {
		Usage q = q();
		q.definition = definedByMemberAccessToProperty("get [p:int] [T4, P].P()");
		String json = JsonUtils.toJson(q);
		Assert.assertEquals(q, JsonUtils.fromJson(json, IUsage.class));
	}

	@Test
	public void differentDefinitionSites_return() {
		Usage q = q();
		q.definition = Definitions.definedByReturnValue("[p:int] [T5, P].P()");
		Assert.assertEquals(q, JsonUtils.fromJson(JsonUtils.toJson(q), IUsage.class));
	}

	@Test
	public void differentDefinitionSites_this() {
		Usage q = q();
		q.definition = Definitions.definedByThis();
		Assert.assertEquals(q, JsonUtils.fromJson(JsonUtils.toJson(q), IUsage.class));
	}

	@Test
	public void queryToUsage() {
		IUsage q = q();
		String json = JsonUtils.toJson(q);
		IUsage u = JsonUtils.fromJson(json, IUsage.class);
		Assert.assertEquals(q, u);
	}

	@Test
	public void queryToJson() {
		String json = JsonUtils.toJson(q());
		Assert.assertEquals("{\"type\":\"T, P\"," + "\"classCtx\":\"S, P\"," + "\"methodCtx\":\"[p:int] [T, P].M()\","
				+ "\"definition\":{\"kind\":\"CONSTANT\"}," + "\"sites\":["
				+ "{\"kind\":\"PARAMETER\",\"method\":\"[p:int] [T2, P].M()\",\"argIndex\":1},"
				+ "{\"kind\":\"RECEIVER\",\"method\":\"[p:int] [T3, P].M()\"}" + "]}", json);
	}

	@Test
	public void queryToQuery() {
		IUsage q = q();
		String json = JsonUtils.toJson(q);
		Usage u = JsonUtils.fromJson(json, Usage.class);
		Assert.assertEquals(q, u);
	}

	@Test
	public void noUsageToUsage() {
		IUsage q = new NoUsage();
		String json = JsonUtils.toJson(q);
		IUsage u = JsonUtils.fromJson(json, IUsage.class);
		Assert.assertEquals(q, u);
	}

	@Test
	public void noUsageToNoUsage() {
		IUsage q = new NoUsage();
		String json = JsonUtils.toJson(q);
		NoUsage u = JsonUtils.fromJson(json, NoUsage.class);
		Assert.assertEquals(q, u);
	}

	private static Usage q() {
		Usage q = new Usage();
		q.type = Names.newType("T, P");
		q.classCtx = Names.newType("S, P");
		q.methodCtx = Names.newMethod("[p:int] [T, P].M()");
		q.definition = Definitions.definedByConstant();
		q.getUsageSites().add(UsageSites.callParameter("[p:int] [T2, P].M()", 1));
		q.getUsageSites().add(UsageSites.call("[p:int] [T3, P].M()"));
		return q;
	}
}