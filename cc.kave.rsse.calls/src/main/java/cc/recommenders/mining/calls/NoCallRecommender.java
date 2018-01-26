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
package cc.recommenders.mining.calls;

import java.util.Set;

import com.google.common.collect.Sets;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.names.ICoReMethodName;
import cc.recommenders.usages.Query;

public class NoCallRecommender implements ICallsRecommender<Query> {

	@Override
	public Set<Tuple<ICoReMethodName, Double>> query(Query query) {
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
	public Set<Tuple<IMethodName, Double>> query2(Query query) {
		return Sets.newHashSet();
	}
}