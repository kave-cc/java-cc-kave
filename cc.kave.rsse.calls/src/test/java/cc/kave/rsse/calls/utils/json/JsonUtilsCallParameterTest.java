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

import static cc.kave.commons.model.naming.Names.newMethod;

import org.junit.Test;

import com.google.gson.JsonObject;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.rsse.calls.model.usages.ICallParameter;
import cc.kave.rsse.calls.model.usages.impl.CallParameter;

public class JsonUtilsCallParameterTest extends JsonUtilsBaseTest {

	private static final IMethodName METHOD = newMethod("[p:void] [T, P].m([p:int] p)");
	private static final int ARGINDEX = 0;

	@Test
	public void correctJson() {
		JsonObject o = new JsonObject();
		o.addProperty("Method", "0M:" + METHOD.getIdentifier());
		o.addProperty("ArgIndex", ARGINDEX);
		assertJson(getFullExample(), o);
	}

	@Test
	public void roundtrip() {
		assertRoundtrip(getFullExample(), CallParameter.class);
	}

	@Test
	public void roundtrip_interface() {
		assertRoundtrip(getFullExample(), ICallParameter.class);
	}

	private CallParameter getFullExample() {
		return new CallParameter(METHOD, ARGINDEX);
	}
}