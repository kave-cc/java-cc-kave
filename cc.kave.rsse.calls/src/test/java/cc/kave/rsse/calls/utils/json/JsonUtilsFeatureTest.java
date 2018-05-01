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

import static cc.kave.rsse.calls.model.usages.impl.MemberAccesses.methodCall;

import org.junit.Test;

import com.google.gson.JsonObject;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.utils.io.json.JsonUtils;
import cc.kave.rsse.calls.model.features.CallParameterFeature;
import cc.kave.rsse.calls.model.features.ClassContextFeature;
import cc.kave.rsse.calls.model.features.DefinitionFeature;
import cc.kave.rsse.calls.model.features.IFeature;
import cc.kave.rsse.calls.model.features.IFeatureVisitor;
import cc.kave.rsse.calls.model.features.MemberAccessFeature;
import cc.kave.rsse.calls.model.features.MethodContextFeature;
import cc.kave.rsse.calls.model.features.TypeFeature;
import cc.kave.rsse.calls.model.usages.impl.CallParameter;
import cc.kave.rsse.calls.model.usages.impl.Definitions;

public class JsonUtilsFeatureTest extends JsonUtilsBaseTest {

	private static final ITypeName SOME_TYPE = Names.newType("T, P");
	private static final IMethodName SOME_METHOD = Names.newMethod("[p:void] [T, P].m()");

	@Test
	public void typeFeature() {
		TypeFeature f = new TypeFeature(SOME_TYPE);

		JsonObject json = jsonObject(TypeFeature.class);
		json.addProperty("Type", "0T:" + SOME_TYPE.getIdentifier());

		assertJson(f, json);
		assertRoundtrip(f, IFeature.class);
		assertRoundtrip(f, TypeFeature.class);
	}

	@Test
	public void classContextFeature() {
		ClassContextFeature f = new ClassContextFeature(SOME_TYPE);

		JsonObject json = jsonObject(ClassContextFeature.class);
		json.addProperty("Type", "0T:" + SOME_TYPE.getIdentifier());

		assertJson(f, json);
		assertRoundtrip(f, IFeature.class);
		assertRoundtrip(f, ClassContextFeature.class);
	}

	@Test
	public void methodContextFeature() {
		MethodContextFeature f = new MethodContextFeature(SOME_METHOD);

		JsonObject json = jsonObject(MethodContextFeature.class);
		json.addProperty("Method", "0M:" + SOME_METHOD.getIdentifier());

		assertJson(f, json);
		assertRoundtrip(f, IFeature.class);
		assertRoundtrip(f, MethodContextFeature.class);
	}

	@Test
	public void definitionFeature() {
		DefinitionFeature f = new DefinitionFeature(Definitions.definedByConstant());

		JsonObject def = new JsonObject();
		def.addProperty("Type", "CONSTANT");

		JsonObject json = jsonObject(DefinitionFeature.class);
		json.add("Definition", def);

		assertJson(f, json);
		assertRoundtrip(f, IFeature.class);
		assertRoundtrip(f, DefinitionFeature.class);
	}

	@Test
	public void callParameter() {
		CallParameter cp = new CallParameter(Names.newMethod("[p:int] [p:object].m([p:int] p0, [p:int] p1)"), 1);
		CallParameterFeature f = new CallParameterFeature(cp);

		JsonObject cpJson = new JsonObject();
		cpJson.addProperty("Method", "0M:" + cp.method.getIdentifier());
		cpJson.addProperty("ArgIndex", 1);

		JsonObject json = jsonObject(CallParameterFeature.class);
		json.add("CallParameter", cpJson);

		assertJson(f, json);
		assertRoundtrip(f, IFeature.class);
		assertRoundtrip(f, CallParameterFeature.class);
	}

	@Test
	public void memberAccessFeature() {
		MemberAccessFeature f = new MemberAccessFeature(methodCall(SOME_METHOD));

		JsonObject site = new JsonObject();
		site.addProperty("Type", "METHOD_CALL");
		site.addProperty("Member", "0M:" + SOME_METHOD.getIdentifier());

		JsonObject json = jsonObject(MemberAccessFeature.class);
		json.add("MemberAccess", site);

		assertJson(f, json);
		assertRoundtrip(f, IFeature.class);
		assertRoundtrip(f, MemberAccessFeature.class);
	}

	@Test(expected = IllegalArgumentException.class)
	public void fail_serializeUnknownFeatureType() {
		JsonUtils.toJson(new TestFeature(), IFeature.class);
	}

	@Test(expected = IllegalArgumentException.class)
	public void fail_deserializeUnknownFeatureType() {
		JsonUtils.fromJson("{\"$type\":\"TestFeature\"}", IFeature.class);
	}

	private static class TestFeature implements IFeature {
		@Override
		public void accept(IFeatureVisitor v) {
		}
	}
}