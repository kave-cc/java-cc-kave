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

import com.google.common.collect.Sets;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.IName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.rsse.calls.model.Tuple;
import cc.kave.rsse.calls.model.usages.impl.Usage;

public class NoCallRecommender implements ICallsRecommender<Usage> {

	@Override
	public Set<Tuple<IMethodName, Double>> query(Usage query) {
		return Sets.newHashSet();
	}

	@Override
	public int getSize() {
		return 0;
	}

	@Override
	public Set<Tuple<IMethodName, Double>> query(Context ctx) {
		return Sets.newHashSet();
	}

	@Override
	public Set<Tuple<IMethodName, Double>> query(Context ctx, List<IName> ideProposals) {
		return Sets.newHashSet();
	}
}