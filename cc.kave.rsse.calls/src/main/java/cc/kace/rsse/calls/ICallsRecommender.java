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
package cc.kace.rsse.calls;

import java.util.List;
import java.util.Set;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.IName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.ssts.ISST;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.names.ICoReMethodName;

public interface ICallsRecommender<Query> {

	/**
	 * use the recommender-specific query format to query proposals
	 */
	Set<Tuple<IMethodName, Double>> query2(Query query);

	/**
	 * query proposals by providing an SST
	 */
	Set<Tuple<IMethodName, Double>> query(ISST sst);

	/**
	 * query proposals by providing an SST and the proposals given by the IDE
	 */
	Set<Tuple<IMethodName, Double>> query(ISST sst, List<IName> ideProposals);

	/**
	 * query proposals by providing a context
	 */
	Set<Tuple<IMethodName, Double>> query(Context ctx);

	/**
	 * query proposals by providing a context and the proposals given by the IDE
	 */
	Set<Tuple<IMethodName, Double>> query(Context ctx, List<IName> ideProposals);

	// TODO get rid of this
	Set<Tuple<ICoReMethodName, Double>> query(Query query);

	/**
	 * @return the size of the underlying model in bytes
	 */
	int getSize();
}
