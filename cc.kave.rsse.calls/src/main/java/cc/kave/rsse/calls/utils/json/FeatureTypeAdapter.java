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

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.rsse.calls.model.features.CallParameterFeature;
import cc.kave.rsse.calls.model.features.ClassContextFeature;
import cc.kave.rsse.calls.model.features.DefinitionFeature;
import cc.kave.rsse.calls.model.features.IFeature;
import cc.kave.rsse.calls.model.features.MemberAccessFeature;
import cc.kave.rsse.calls.model.features.MethodContextFeature;
import cc.kave.rsse.calls.model.features.TypeFeature;
import cc.kave.rsse.calls.model.usages.ICallParameter;
import cc.kave.rsse.calls.model.usages.IDefinition;
import cc.kave.rsse.calls.model.usages.IMemberAccess;

public class FeatureTypeAdapter implements JsonSerializer<IFeature>, JsonDeserializer<IFeature> {

	public static final String TYPE_MARKER = "$type";
	public static final String TYPE = "Type";
	public static final String METHOD = "Method";
	public static final String DEFINITION = "Definition";
	public static final String CALL_PARAMETER = "CallParameter";
	public static final String MEMBER_ACCESS = "MemberAccess";

	@Override
	public JsonElement serialize(IFeature src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject obj = new JsonObject();
		String className = src.getClass().getSimpleName();
		obj.addProperty(TYPE_MARKER, className);
		if (src instanceof TypeFeature) {
			serialize((TypeFeature) src, obj, context);
		} else if (src instanceof ClassContextFeature) {
			serialize((ClassContextFeature) src, obj, context);
		} else if (src instanceof MethodContextFeature) {
			serialize((MethodContextFeature) src, obj, context);
		} else if (src instanceof DefinitionFeature) {
			serialize((DefinitionFeature) src, obj, context);
		} else if (src instanceof CallParameterFeature) {
			serialize((CallParameterFeature) src, obj, context);
		} else if (src instanceof MemberAccessFeature) {
			serialize((MemberAccessFeature) src, obj, context);
		} else {
			throw new IllegalArgumentException("Unexpected feature type: " + className);
		}
		return obj;
	}

	@Override
	public IFeature deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		String className = obj.get(TYPE_MARKER).getAsString();
		if (TypeFeature.class.getSimpleName().equals(className)) {
			return deserializeType(obj, context);
		}
		if (ClassContextFeature.class.getSimpleName().equals(className)) {
			return deserializeClassCtx(obj, context);
		}
		if (MethodContextFeature.class.getSimpleName().equals(className)) {
			return deserializeMethodCtx(obj, context);
		}
		if (DefinitionFeature.class.getSimpleName().equals(className)) {
			return deserializeDefinition(obj, context);
		}
		if (CallParameterFeature.class.getSimpleName().equals(className)) {
			return deserializeCallParameter(obj, context);
		}
		if (MemberAccessFeature.class.getSimpleName().equals(className)) {
			return deserializeUsageSite(obj, context);
		}
		throw new IllegalArgumentException("Unexpected feature type: " + className);
	}

	private void serialize(TypeFeature src, JsonObject obj, JsonSerializationContext context) {
		obj.add(TYPE, context.serialize(src.type));
	}

	private TypeFeature deserializeType(JsonObject obj, JsonDeserializationContext context) {
		ITypeName t = context.deserialize(obj.get(TYPE), ITypeName.class);
		return new TypeFeature(t);
	}

	private void serialize(ClassContextFeature src, JsonObject obj, JsonSerializationContext context) {
		obj.add(TYPE, context.serialize(src.type));
	}

	private IFeature deserializeClassCtx(JsonObject obj, JsonDeserializationContext context) {
		ITypeName t = context.deserialize(obj.get(TYPE), ITypeName.class);
		return new ClassContextFeature(t);
	}

	private void serialize(MethodContextFeature src, JsonObject obj, JsonSerializationContext context) {
		obj.add(METHOD, context.serialize(src.method));
	}

	private IFeature deserializeMethodCtx(JsonObject obj, JsonDeserializationContext context) {
		IMethodName m = context.deserialize(obj.get(METHOD), IMethodName.class);
		return new MethodContextFeature(m);
	}

	private void serialize(DefinitionFeature src, JsonObject obj, JsonSerializationContext context) {
		obj.add(DEFINITION, context.serialize(src.definition));
	}

	private IFeature deserializeDefinition(JsonObject obj, JsonDeserializationContext context) {
		IDefinition d = context.deserialize(obj.get(DEFINITION), IDefinition.class);
		return new DefinitionFeature(d);
	}

	private void serialize(CallParameterFeature src, JsonObject obj, JsonSerializationContext context) {
		obj.add(CALL_PARAMETER, context.serialize(src.callParameter));
	}

	private IFeature deserializeCallParameter(JsonObject obj, JsonDeserializationContext context) {
		ICallParameter d = context.deserialize(obj.get(CALL_PARAMETER), ICallParameter.class);
		return new CallParameterFeature(d);
	}

	private IFeature deserializeUsageSite(JsonObject obj, JsonDeserializationContext context) {
		IMemberAccess us = context.deserialize(obj.get(MEMBER_ACCESS), IMemberAccess.class);
		return new MemberAccessFeature(us);
	}

	private void serialize(MemberAccessFeature src, JsonObject obj, JsonSerializationContext context) {
		obj.add(MEMBER_ACCESS, context.serialize(src.memberAccess));
	}
}