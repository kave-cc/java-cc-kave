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
import cc.kave.rsse.calls.model.usages.IUsageSite;
import cc.kave.rsse.calls.model.usages.UsageSiteType;
import cc.kave.rsse.calls.model.usages.impl.UsageSite;

public class UsageSiteTypeAdapter implements JsonSerializer<IUsageSite>, JsonDeserializer<IUsageSite> {

	@Override
	public JsonElement serialize(IUsageSite src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject o = new JsonObject();
		if (src.getType() != null) {
			o.add("Type", context.serialize(src.getType()));
		}
		IMemberName member = src.getMember();
		if (member != null) {
			o.add("Member", context.serialize(member));
		}
		if (src.getArgIndex() != -1) {
			o.addProperty("ArgIndex", src.getArgIndex());
		}
		return o;
	}

	@Override
	public IUsageSite deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {

		UsageSite us = new UsageSite();

		JsonObject obj = json.getAsJsonObject();
		if (obj.has("Type")) {
			us.type = context.deserialize(obj.get("Type"), UsageSiteType.class);
		}
		if (obj.has("Member")) {
			us.member = context.deserialize(obj.get("Member"), IMemberName.class);
		}
		if (obj.has("ArgIndex")) {
			us.argIndex = obj.get("ArgIndex").getAsInt();
		}

		return us;
	}
}