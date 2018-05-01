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
import java.util.Set;

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
import cc.kave.rsse.calls.model.usages.ICallParameter;
import cc.kave.rsse.calls.model.usages.IDefinition;
import cc.kave.rsse.calls.model.usages.IMemberAccess;
import cc.kave.rsse.calls.model.usages.IUsage;
import cc.kave.rsse.calls.model.usages.impl.NoUsage;
import cc.kave.rsse.calls.model.usages.impl.Usage;

@SuppressWarnings("deprecation")
public class UsageTypeAdapter implements JsonSerializer<IUsage>, JsonDeserializer<IUsage> {

	private static final Type T_LIST_OF_IMEMBERACCESS = new TypeToken<List<IMemberAccess>>() {
	}.getType();
	private static final Type T_SET_OF_ICALLPARAMETER = new TypeToken<Set<ICallParameter>>() {
	}.getType();

	private static final String DEFINITION = "Definition";
	private static final String PARAMS = "CallParameters";
	private static final String MEMBER_ACCESSES = "MemberAccesses";

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
			obj.add(DEFINITION, context.serialize(src.getDefinition()));
		}
		if (src.getCallParameters() != null && src.getCallParameters().size() > 0) {
			obj.add(PARAMS, context.serialize(src.getCallParameters()));
		}
		if (src.getMemberAccesses() != null && src.getMemberAccesses().size() > 0) {
			obj.add(MEMBER_ACCESSES, context.serialize(src.getMemberAccesses()));
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
		if (obj.has(DEFINITION)) {
			usage.definition = context.deserialize(obj.get(DEFINITION), IDefinition.class);
		}
		if (obj.has(PARAMS)) {
			Set<ICallParameter> callParameters = context.deserialize(obj.get(PARAMS), T_SET_OF_ICALLPARAMETER);
			usage.callParameters.addAll(callParameters);
		}
		if (obj.has(MEMBER_ACCESSES)) {
			List<IMemberAccess> sites = context.deserialize(obj.get(MEMBER_ACCESSES), T_LIST_OF_IMEMBERACCESS);
			usage.memberAccesses.addAll(sites);
		}
		if (obj.has("IsQuery")) {
			usage.isQuery = obj.get("IsQuery").getAsBoolean();
		}

		return usage;
	}
}