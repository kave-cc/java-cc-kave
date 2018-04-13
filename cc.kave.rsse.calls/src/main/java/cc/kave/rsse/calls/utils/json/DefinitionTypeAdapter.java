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

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.rsse.calls.model.usages.DefinitionType;
import cc.kave.rsse.calls.model.usages.IDefinition;
import cc.kave.rsse.calls.model.usages.impl.Definition;

public class DefinitionTypeAdapter implements JsonSerializer<IDefinition>, JsonDeserializer<IDefinition> {

	@Override
	public JsonElement serialize(IDefinition src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject o = new JsonObject();
		if (src.getType() != null) {
			o.add("Type", context.serialize(src.getType(), DefinitionType.class));
		}
		IMemberName member = src.getMember(IMemberName.class);
		if (member != null) {
			o.add("Member", context.serialize(member));
		}
		if (src.getArgIndex() != -1) {
			o.addProperty("ArgIndex", src.getArgIndex());
		}
		return o;
	}

	@Override
	public IDefinition deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		Definition d = new Definition();

		JsonObject obj = json.getAsJsonObject();

		if (obj.has("Type")) {
			d.type = context.deserialize(obj.get("Type"), DefinitionType.class);
		}
		if (obj.has("Member")) {
			d.member = context.deserialize(obj.get("Member"), IMemberName.class);
		}
		if (obj.has("ArgIndex")) {
			d.argIndex = obj.get("ArgIndex").getAsInt();
		}

		return d;
	}
}