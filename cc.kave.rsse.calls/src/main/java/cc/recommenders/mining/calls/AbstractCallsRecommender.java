/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.recommenders.mining.calls;

import java.util.List;
import java.util.Set;

import cc.kace.rsse.calls.ICallsRecommender;
import cc.kave.commons.assertions.Asserts;
import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.IName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.ssts.ISST;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.names.ICoReMethodName;

public abstract class AbstractCallsRecommender<T> implements ICallsRecommender<T> {

	@Override
	public Set<Tuple<IMethodName, Double>> query(Context ctx) {
		Asserts.fail("not implemented yet");
		return null;
	}

	@Override
	public Set<Tuple<IMethodName, Double>> query2(T query) {
		Asserts.fail("not implemented yet");
		return null;
	}

	@Override
	public Set<Tuple<ICoReMethodName, Double>> query(T query) {
		Asserts.fail("not implemented yet");
		return null;
	}

	@Override
	public int getSize() {
		Asserts.fail("not implemented yet");
		return -1;
	}

	@Override
	public Set<Tuple<IMethodName, Double>> query(ISST sst) {
		Asserts.fail("not implemented yet");
		return null;
	}

	@Override
	public Set<Tuple<IMethodName, Double>> query(ISST sst, List<IName> ideProposals) {
		Asserts.fail("not implemented yet");
		return null;
	}

	@Override
	public Set<Tuple<IMethodName, Double>> query(Context ctx, List<IName> ideProposals) {
		Asserts.fail("not implemented yet");
		return null;
	}
}