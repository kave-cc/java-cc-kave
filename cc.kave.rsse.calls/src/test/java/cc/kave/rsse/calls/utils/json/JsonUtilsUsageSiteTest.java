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
import cc.kave.rsse.calls.model.usages.IUsageSite;
import cc.kave.rsse.calls.model.usages.UsageSiteType;
import cc.kave.rsse.calls.model.usages.impl.UsageSite;

public class JsonUtilsUsageSiteTest extends JsonUtilsBaseTest {

	@Test
	public void defaultValues() {
		assertJson(new UsageSite(), new JsonObject());
		assertRoundtrip(new UsageSite(), IUsageSite.class);
		assertRoundtrip(new UsageSite(), UsageSite.class);
	}

	@Test
	public void withValues() {
		JsonObject o = new JsonObject();
		o.addProperty("Type", "CALL_PARAMETER");
		o.addProperty("Member", "0M:[p:void] [T, P].m([p:int] p)");
		o.addProperty("ArgIndex", 1);
		assertJson(getFullExample(), o);
	}

	@Test
	public void roundtrip() {
		assertRoundtrip(getFullExample(), UsageSite.class);
	}

	@Test
	public void roundtrip_interface() {
		assertRoundtrip(getFullExample(), IUsageSite.class);
	}

	private UsageSite getFullExample() {
		UsageSite us = new UsageSite();
		us.type = UsageSiteType.CALL_PARAMETER;
		us.member = Names.newMethod("[p:void] [T, P].m([p:int] p)");
		us.argIndex = 1;
		return us;
	}
}