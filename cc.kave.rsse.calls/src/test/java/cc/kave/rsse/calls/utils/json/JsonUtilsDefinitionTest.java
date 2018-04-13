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

import org.junit.Test;

import com.google.gson.JsonObject;

import cc.kave.commons.model.naming.Names;
import cc.kave.rsse.calls.model.usages.DefinitionType;
import cc.kave.rsse.calls.model.usages.IDefinition;
import cc.kave.rsse.calls.model.usages.impl.Definition;

public class JsonUtilsDefinitionTest extends JsonUtilsBaseTest {

	@Test
	public void defaultValues() {
		assertJson(new Definition(), new JsonObject());
		assertRoundtrip(new Definition(), IDefinition.class);
		assertRoundtrip(new Definition(), Definition.class);
	}

	@Test
	public void withValues() {
		JsonObject o = new JsonObject();
		o.addProperty("Type", "LOOP_HEADER");
		o.addProperty("Member", "0M:[p:void] [T, P].m([p:int] p)");
		o.addProperty("ArgIndex", 1);
		assertJson(getFullExample(), o);
	}

	@Test
	public void roundtrip() {
		assertRoundtrip(getFullExample(), Definition.class);
	}

	@Test
	public void roundtrip_interface() {
		assertRoundtrip(getFullExample(), IDefinition.class);
	}

	private Definition getFullExample() {
		Definition d = new Definition();
		d.type = DefinitionType.LOOP_HEADER;
		d.member = Names.newMethod("[p:void] [T, P].m([p:int] p)");
		d.argIndex = 1;
		return d;
	}
}