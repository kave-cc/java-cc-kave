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
package cc.kave.rsse.calls;

import java.util.List;
import java.util.Set;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.IName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.rsse.calls.datastructures.Tuple;

public interface ICallsRecommender<TQuery> {

	/**
	 * use the recommender-specific query format to query proposals
	 * 
	 * @param query
	 *            the query in a format specfic to the recommender
	 * @return a sorted set of the proposed methods plus probability
	 */
	Set<Tuple<IMethodName, Double>> query(TQuery query);

	/**
	 * query proposals by providing a context
	 * 
	 * @param ctx
	 *            the query as a Context
	 * @return a sorted set of the proposed methods plus probability
	 */
	Set<Tuple<IMethodName, Double>> query(Context ctx);

	/**
	 * query proposals by providing a context and the proposals given by the IDE
	 * 
	 * @param ctx
	 *            the query as a Context
	 * @param ideProposals
	 *            the proposal given by the IDE
	 * @return a sorted set of the proposed methods plus probability
	 */
	Set<Tuple<IMethodName, Double>> query(Context ctx, List<IName> ideProposals);

	/**
	 * Request the size of the underlying model.
	 * 
	 * @return model size in bytes
	 */
	int getSize();
}
