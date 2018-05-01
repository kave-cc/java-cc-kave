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

import static cc.kave.commons.model.naming.Names.newMethod;
import static cc.kave.commons.model.naming.Names.newType;
import static cc.kave.rsse.calls.model.usages.impl.MemberAccesses.methodCall;

import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import cc.kave.rsse.calls.model.usages.IUsage;
import cc.kave.rsse.calls.model.usages.impl.CallParameter;
import cc.kave.rsse.calls.model.usages.impl.Definitions;
import cc.kave.rsse.calls.model.usages.impl.NoUsage;
import cc.kave.rsse.calls.model.usages.impl.Usage;

@SuppressWarnings("deprecation")
public class JsonUtilsUsageTest extends JsonUtilsBaseTest {

	@Test
	public void noUsages() {
		assertJson(new NoUsage(), new JsonPrimitive("NoUsage"));
		assertRoundtrip(new NoUsage(), NoUsage.class);
		assertRoundtrip(new NoUsage(), IUsage.class);
	}

	@Test
	public void defaults() {
		assertJson(new Usage(), new JsonObject());
		assertRoundtrip(new Usage(), Usage.class);
	}

	@Test
	public void withValues() {
		JsonObject d = new JsonObject();
		d.addProperty("Type", "CONSTANT");

		JsonObject p1 = new JsonObject();
		p1.addProperty("Method", "0M:[p:int] [T, P].M([p:int] p)");
		p1.addProperty("ArgIndex", 0);
		JsonArray params = new JsonArray();
		params.add(p1);

		JsonObject us = new JsonObject();
		us.addProperty("Type", "METHOD_CALL");
		us.addProperty("Member", "0M:[p:int] [T3, P].M()");
		JsonArray sites = new JsonArray();
		sites.add(us);

		JsonObject obj = new JsonObject();
		obj.addProperty("Type", "0T:T, P");
		obj.addProperty("ClassCtx", "0T:S, P");
		obj.addProperty("MethodCtx", "0M:[p:int] [T, P].M()");
		obj.add("Definition", d);
		obj.add("CallParameters", params);
		obj.add("MemberAccesses", sites);
		obj.addProperty("IsQuery", true);

		assertJson(getFullExample(), obj);
	}

	@Test
	public void roundtrip() {
		assertRoundtrip(getFullExample(), Usage.class);
	}

	@Test
	public void roundtrip_interface() {
		assertRoundtrip(getFullExample(), IUsage.class);
	}

	private static Usage getFullExample() {
		Usage u = new Usage();
		u.type = newType("T, P");
		u.classCtx = newType("S, P");
		u.methodCtx = newMethod("[p:int] [T, P].M()");
		u.definition = Definitions.definedByConstant();
		u.callParameters.add(new CallParameter(newMethod("[p:int] [T, P].M([p:int] p)"), 0));
		u.memberAccesses.add(methodCall("[p:int] [T3, P].M()"));
		u.isQuery = true;
		return u;
	}
}