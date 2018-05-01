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
package cc.kave.rsse.calls.recs.rep;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.rsse.calls.model.usages.IMemberAccess;
import cc.kave.rsse.calls.model.usages.IUsage;

public class RepetitionMiner {

	private Map<IMemberName, Integer> numSeen;
	private Map<IMemberName, Integer> numRepeated;

	public RepetitionModel learnModel(List<IUsage> in) {
		if (in == null || in.isEmpty()) {
			throw new IllegalArgumentException("Input is null or empty.");
		}
		numSeen = new HashMap<>();
		numRepeated = new HashMap<>();
		for (IUsage u : in) {
			register(u);
		}

		ITypeName type = in.get(0).getType();
		RepetitionModel model = new RepetitionModel(type);
		for (IMemberName m : numSeen.keySet()) {
			if (numRepeated.containsKey(m)) {
				double r = numRepeated.get(m);
				double s = numSeen.get(m);
				double probability = r / s;
				model.setRepetitionProbability(m, probability);
			}
		}
		return model;
	}

	private void register(IUsage u) {
		Set<IMemberName> seen = new HashSet<>();
		Set<IMemberName> ignore = new HashSet<>();

		for (IMemberAccess us : u.getMemberAccesses()) {
			IMemberName m = us.getMember();
			if (ignore.contains(m)) {
				continue;
			}

			if (!seen.contains(m)) {
				count(m, numSeen);
				seen.add(m);
				continue;
			}

			seen.remove(m);
			ignore.add(m);
			count(m, numRepeated);
		}
	}

	private void count(IMemberName m, Map<IMemberName, Integer> map) {
		if (map.containsKey(m)) {
			int i = map.get(m);
			map.put(m, i + 1);
		} else {
			map.put(m, 1);
		}
	}
}