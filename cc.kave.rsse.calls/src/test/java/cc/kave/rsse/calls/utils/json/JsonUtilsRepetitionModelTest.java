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

import static cc.kave.commons.model.naming.Names.newField;

import org.junit.Test;

import com.google.gson.JsonObject;

import cc.kave.commons.model.naming.Names;
import cc.kave.rsse.calls.recs.rep.RepetitionModel;

public class JsonUtilsRepetitionModelTest extends JsonUtilsBaseTest {

	@Test
	public void toJson() {
		RepetitionModel obj = new RepetitionModel(Names.newType("T, P"));
		obj.setRepetitionProbability(newField("[p:int] [T, P]._f"), 0.123);

		JsonObject probs = new JsonObject();
		probs.addProperty("0F:[p:int] [T, P]._f", 0.123);

		JsonObject json = new JsonObject();
		json.addProperty("Type", "0T:T, P");
		json.add("Probabilities", probs);

		assertJson(obj, json);
		assertRoundtrip(obj, RepetitionModel.class);
	}
}