/**
 * Copyright 2015 Simon Reuß
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package cc.kave.commons.pointsto.analysis;

import java.util.Set;

import cc.kave.commons.model.events.completionevents.Context;

public interface PointsToAnalysis {

	/**
	 * Computes the points-to information for the specified {@link Context} and stores a reference to this instance in
	 * the returned {@link PointsToContext}. This method may only be called once.
	 */
	PointsToContext compute(Context context);

	QueryStrategy getQueryStrategy();

	void setQueryStrategy(QueryStrategy strategy);

	Set<AbstractLocation> query(PointsToQuery query);
}
