/**
 * Copyright 2016 Technische Universität Darmstadt
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
package cc.kave.commons.utils.json;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import cc.kave.commons.model.events.IDEEvent;
import cc.kave.commons.testing.ParameterData;
import cc.kave.commons.utils.io.json.JsonUtils;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class DateInEventSerializationTest {

	@Test
	@Parameters(method = "provideOffsets")
	public void differentOffsetsCanBeParsed(int hours, String offset) {
		String json = GetCommandEvent(offset);
		IDEEvent obj = JsonUtils.fromJson(json, IDEEvent.class);

		int actualOffsetInSec = obj.TriggeredAt.getOffset().getTotalSeconds();
		int expectedOffsetInSec = hours * 60 * 60;
		Assert.assertEquals(expectedOffsetInSec, actualOffsetInSec);
	}

	@Test
	public void offsetsAreIncludedInSerialization() {
		String in = GetCommandEvent("-03:00");
		IDEEvent obj = JsonUtils.fromJson(in, IDEEvent.class);
		String out = JsonUtils.toJson(obj);
		Assert.assertEquals(in, out);
	}

	public static Object[][] provideOffsets() {
		ParameterData pd = new ParameterData();
		// id, language, filename
		pd.add(0, "Z");
		pd.add(1, "+01:00");
		pd.add(2, "+02:00");
		pd.add(3, "+03:00");
		pd.add(-1, "-01:00");
		pd.add(-3, "-03:00");
		return pd.toArray();
	}

	private String GetCommandEvent(String offset) {
		return "{\"$type\":\"KaVE.Commons.Model.Events.CommandEvent, KaVE.Commons\"," + "\"CommandId\":\"cmdX\","
				+ "\"TriggeredAt\":\"2054-03-21T00:30:00.1234567" + offset + "\"}";
	}
}