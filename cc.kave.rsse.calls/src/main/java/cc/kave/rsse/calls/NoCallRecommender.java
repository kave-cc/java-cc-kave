/*
 * Copyright 2014 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.kave.rsse.calls;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Sets;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.IName;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.rsse.calls.model.usages.IUsage;

public class NoCallRecommender implements IMemberRecommender<IUsage> {

	@Override
	public Set<Pair<IMemberName, Double>> query(IUsage query) {
		return Sets.newHashSet();
	}

	@Override
	public int getLastModelSize() {
		return 0;
	}

	@Override
	public Set<Pair<IMemberName, Double>> query(Context ctx) {
		return Sets.newHashSet();
	}

	@Override
	public Set<Pair<IMemberName, Double>> query(Context ctx, List<IName> ideProposals) {
		return Sets.newHashSet();
	}
}