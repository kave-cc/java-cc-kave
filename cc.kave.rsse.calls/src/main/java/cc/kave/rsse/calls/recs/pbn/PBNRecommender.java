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
package cc.kave.rsse.calls.recs.pbn;

import java.util.List;
import java.util.Set;

import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Sets;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.IName;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.rsse.calls.IMemberRecommender;
import cc.kave.rsse.calls.UsageExtractor;
import cc.kave.rsse.calls.mining.Options;
import cc.kave.rsse.calls.model.usages.IUsage;

public class PBNRecommender implements IMemberRecommender<IUsage> {

	private final Options options;
	private final PBNModelStore modelStore;
	private final LRUMap recInstances;

	private int lastModelSize = -1;

	public PBNRecommender(Options options, PBNModelStore modelStore) {
		this.options = options;
		this.modelStore = modelStore;
		recInstances = new LRUMap(10);
	}

	@Override
	public Set<Pair<IMemberName, Double>> query(IUsage u) {

		ITypeName t = u.getType();
		PBNRecommenderInstance rec;

		if (recInstances.containsKey(t)) {
			rec = (PBNRecommenderInstance) recInstances.get(t);
		} else if (modelStore.hasModel(t)) {
			PBNModel model = modelStore.getModel(t);
			rec = new PBNRecommenderInstance(model, options);
			recInstances.put(t, rec);
		} else {
			return Sets.newHashSet();
		}
		return rec.query(u);
	}

	@Override
	public Set<Pair<IMemberName, Double>> query(Context ctx) {
		UsageExtractor ue = new UsageExtractor(ctx);
		if (ue.hasQuery()) {
			return query(ue.getQuery());
		}
		return Sets.newHashSet();
	}

	@Override
	public Set<Pair<IMemberName, Double>> query(Context ctx, List<IName> ideProposals) {
		return query(ctx);
	}

	@Override
	public int getLastModelSize() {
		return lastModelSize;
	}
}