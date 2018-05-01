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

import static cc.kave.commons.assertions.Asserts.assertTrue;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.rsse.calls.model.usages.ICallParameter;
import cc.kave.rsse.calls.model.usages.impl.CallParameter;

public class CallParameterTypeAdapter implements JsonSerializer<ICallParameter>, JsonDeserializer<ICallParameter> {

	private static final String METHOD = "Method";
	private static final String ARG_INDEX = "ArgIndex";

	@Override
	public JsonElement serialize(ICallParameter src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject o = new JsonObject();
		o.add(METHOD, context.serialize(src.getMethod()));
		o.addProperty("ArgIndex", src.getArgIndex());
		return o;
	}

	@Override
	public ICallParameter deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {

		JsonObject obj = json.getAsJsonObject();
		assertTrue(obj.has(METHOD));
		assertTrue(obj.has(ARG_INDEX));

		IMethodName m = context.deserialize(obj.get(METHOD), IMethodName.class);
		int argIndex = obj.get(ARG_INDEX).getAsInt();

		return new CallParameter(m, argIndex);
	}
}