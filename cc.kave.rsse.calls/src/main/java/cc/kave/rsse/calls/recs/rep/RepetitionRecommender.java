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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.IName;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.rsse.calls.IMemberRecommender;
import cc.kave.rsse.calls.UsageExtractor;
import cc.kave.rsse.calls.model.usages.IUsage;
import cc.kave.rsse.calls.utils.ProposalHelper;

public class RepetitionRecommender implements IMemberRecommender<IUsage> {

	private RepetitionModelStore modelStore;

	public RepetitionRecommender(RepetitionModelStore modelStore) {
		this.modelStore = modelStore;
	}

	@Override
	public Set<Pair<IMemberName, Double>> query(Context ctx) {
		UsageExtractor ue = new UsageExtractor(ctx);
		if (ue.hasQuery()) {
			return query(ue.getQuery());
		}
		return new HashSet<>();
	}

	@Override
	public Set<Pair<IMemberName, Double>> query(IUsage u) {
		if (!modelStore.hasModel(u.getType())) {
			return new HashSet<>();
		}
		RepetitionModel model = modelStore.getModel(u.getType());

		Set<Pair<IMemberName, Double>> res = ProposalHelper.createSortedSet();
		Set<IMemberName> ms = u.getUsageSites().stream().map(us -> us.getMember()).collect(Collectors.toSet());
		for (IMemberName m : ms) {
			if (model.hasRepetitionProbability(m)) {
				res.add(Pair.of(m, model.getRepetitionProbability(m)));
			}
		}
		return res;
	}

	@Override
	public Set<Pair<IMemberName, Double>> query(Context ctx, List<IName> ideProposals) {
		return query(ctx);
	}

	@Override
	public int getLastModelSize() {
		return 0;
	}
}