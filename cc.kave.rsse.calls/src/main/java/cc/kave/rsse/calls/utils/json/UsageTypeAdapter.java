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
import java.util.List;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.rsse.calls.model.usages.IDefinition;
import cc.kave.rsse.calls.model.usages.IUsage;
import cc.kave.rsse.calls.model.usages.IUsageSite;
import cc.kave.rsse.calls.model.usages.impl.NoUsage;
import cc.kave.rsse.calls.model.usages.impl.Usage;

@SuppressWarnings("deprecation")
public class UsageTypeAdapter implements JsonSerializer<IUsage>, JsonDeserializer<IUsage> {

	@Override
	public JsonElement serialize(IUsage src, Type typeOfSrc, JsonSerializationContext context) {
		if (src instanceof NoUsage) {
			return new JsonPrimitive("NoUsage");
		}
		JsonObject obj = new JsonObject();

		if (src.getType() != null) {
			obj.add("Type", context.serialize(src.getType()));
		}
		if (src.getClassContext() != null) {
			obj.add("ClassCtx", context.serialize(src.getClassContext()));
		}
		if (src.getMethodContext() != null) {
			obj.add("MethodCtx", context.serialize(src.getMethodContext()));
		}
		if (src.getDefinition() != null) {
			obj.add("Definition", context.serialize(src.getDefinition()));
		}
		if (src.getUsageSites() != null && src.getUsageSites().size() > 0) {
			obj.add("UsageSites", context.serialize(src.getUsageSites()));
		}
		if (src.isQuery()) {
			obj.addProperty("IsQuery", true);
		}
		return obj;
	}

	@Override
	public IUsage deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		if (json.isJsonPrimitive()) {
			return new NoUsage();
		}
		JsonObject obj = json.getAsJsonObject();
		Usage usage = new Usage();

		if (obj.has("Type")) {
			usage.type = context.deserialize(obj.get("Type"), ITypeName.class);
		}
		if (obj.has("ClassCtx")) {
			usage.classCtx = context.deserialize(obj.get("ClassCtx"), ITypeName.class);
		}
		if (obj.has("MethodCtx")) {
			usage.methodCtx = context.deserialize(obj.get("MethodCtx"), IMethodName.class);
		}
		if (obj.has("Definition")) {
			usage.definition = context.deserialize(obj.get("Definition"), IDefinition.class);
		}
		if (obj.has("UsageSites")) {
			Type listOfIUsage = new TypeToken<List<IUsageSite>>() {
			}.getType();
			List<IUsageSite> sites = context.deserialize(obj.get("UsageSites"), listOfIUsage);
			for (IUsageSite site : sites) {
				usage.usageSites.add(site);
			}
		}
		if (obj.has("IsQuery")) {
			usage.isQuery = obj.get("IsQuery").getAsBoolean();
		}

		return usage;
	}
}