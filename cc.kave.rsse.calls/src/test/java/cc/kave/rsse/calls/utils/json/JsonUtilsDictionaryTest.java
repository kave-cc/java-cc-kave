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

import static cc.kave.commons.model.naming.Names.newType;
import static cc.kave.commons.utils.io.json.JsonUtils.fromJson;
import static cc.kave.commons.utils.io.json.JsonUtils.toJson;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Type;
import java.util.Iterator;

import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;

import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.rsse.calls.model.Dictionary;
import cc.kave.rsse.calls.model.features.IFeature;
import cc.kave.rsse.calls.model.features.TypeFeature;

public class JsonUtilsDictionaryTest extends JsonUtilsBaseTest {

	private static final ITypeName SOME_TYPE = newType("T, P");
	private static final Type DICT_OF_STRING = new TypeToken<Dictionary<String>>() {
	}.getType();

	@Test
	public void defaults() {

		assertJson(new Dictionary<String>(), new JsonArray());
		assertRoundtrip(new Dictionary<String>(), DICT_OF_STRING);
	}

	@Test
	public void withValuesString() {

		Dictionary<String> obj = new Dictionary<String>();
		obj.add("a");

		JsonArray jsonArr = new JsonArray();
		jsonArr.add(new JsonPrimitive("a"));

		assertJson(obj, jsonArr);
		assertRoundtrip(obj, DICT_OF_STRING);
	}

	@Test
	public void cacheIsRefilledAfterDeserialization_contains() {
		Dictionary<String> in = new Dictionary<String>();
		in.add("a");
		Dictionary<String> out = fromJson(toJson(in), DICT_OF_STRING);
		assertTrue(out.contains("a"));
	}

	@Test
	public void cacheIsRefilledAfterDeserialization_getId() {
		Dictionary<String> in = new Dictionary<String>();
		in.add("a");
		Dictionary<String> out = fromJson(toJson(in), DICT_OF_STRING);
		assertEquals(0, out.getId("a"));
	}

	@Test
	public void cacheIsRefilledAfterDeserialization_addReturnValue() {
		Dictionary<String> in = new Dictionary<String>();
		in.add("a");
		Dictionary<String> out = fromJson(toJson(in), DICT_OF_STRING);
		assertEquals(0, out.add("a"));
	}

	@Test
	public void stabilityWithBigNumbers() {
		Dictionary<String> in = new Dictionary<String>();
		for (int i = 0; i < 100; i++) {
			in.add("e" + i);
		}
		Dictionary<String> out = fromJson(toJson(in), DICT_OF_STRING);
		assertEquals(in.size(), out.size());
		Iterator<String> itIn = in.getAllEntries().iterator();
		Iterator<String> itOut = out.getAllEntries().iterator();
		while (itIn.hasNext()) {
			assertEquals(itIn.next(), itOut.next());
		}
	}

	@Test
	public void alsoWorksWithFeatures() {

		Type dictOfIFeature = new TypeToken<Dictionary<IFeature>>() {
		}.getType();

		Dictionary<IFeature> obj = new Dictionary<IFeature>();
		obj.add(new TypeFeature(SOME_TYPE));

		JsonObject jsonType = new JsonObject();
		jsonType.addProperty(FeatureTypeAdapter.TYPE_MARKER, TypeFeature.class.getSimpleName());
		jsonType.addProperty(FeatureTypeAdapter.TYPE, "0T:" + SOME_TYPE.getIdentifier());

		JsonArray jsonArr = new JsonArray();
		jsonArr.add(jsonType);

		assertJson(obj, jsonArr);
		assertRoundtrip(new Dictionary<IFeature>(), dictOfIFeature);
	}
}