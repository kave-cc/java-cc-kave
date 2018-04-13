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
package cc.kave.rsse.calls.utils.json;

import java.lang.reflect.Type;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import cc.kave.commons.utils.io.json.JsonUtils;

public class JsonUtilsBaseTest {

	@BeforeClass
	public static void setupBaseClass() {
		JsonUtilsCcKaveRsseCalls.registerJsonAdapters();

		// just for code coverage
		new JsonUtilsCcKaveRsseCalls();
	}

	@AfterClass
	public static void teardown() {
		JsonUtils.resetAllConfiguration();
	}

	protected static void assertJson(Object obj, JsonElement jsonObj) {
		String expected = new GsonBuilder().setPrettyPrinting().create().toJson(jsonObj);
		String actual = JsonUtils.toJsonFormatted(obj);
		Assert.assertEquals(expected, actual);
	}

	protected static void assertRoundtrip(Object in, Type typeOfO) {
		String json = JsonUtils.toJson(in);
		Object out = JsonUtils.fromJson(json, typeOfO);
		Assert.assertEquals(in, out);
	}

	protected static JsonObject jsonObject(Class<?> c) {
		JsonObject obj = new JsonObject();
		obj.addProperty("$type", c.getSimpleName());
		return obj;
	}
}