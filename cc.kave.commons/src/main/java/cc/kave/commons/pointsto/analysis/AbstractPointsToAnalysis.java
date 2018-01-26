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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.utils.io.Logger;

public abstract class AbstractPointsToAnalysis implements PointsToAnalysis {

	protected Multimap<PointsToQuery, AbstractLocation> contextToLocations = HashMultimap.create();

	protected QueryStrategy queryStrategy;

	public AbstractPointsToAnalysis() {
		this.queryStrategy = QueryStrategy.MINIMIZE_USAGE_DEFECTS;
	}

	@Override
	public QueryStrategy getQueryStrategy() {
		return queryStrategy;
	}

	@Override
	public void setQueryStrategy(QueryStrategy strategy) {
		this.queryStrategy = strategy;
	}

	/**
	 * Checks whether this {@link PointsToAnalysis} has already been bound to a
	 * {@link Context} and throws an {@link IllegalStateException} accordingly.
	 */
	protected void checkContextBinding() {
		if (!contextToLocations.isEmpty()) {
			throw new IllegalStateException("Analysis has already been bound to a context");
		}
	}

	@Override
	public Set<AbstractLocation> query(PointsToQuery query) {
		IReference reference = query.getReference();
		ITypeName type = query.getType();
		IMemberName member = query.getMember();

		Collection<AbstractLocation> locations = contextToLocations
				.get(new PointsToQuery(reference, type, query.getStmt(), member));
		if (locations.isEmpty()) {
			// drop method
			locations = contextToLocations.get(new PointsToQuery(reference, type, query.getStmt(), null));
			if (!locations.isEmpty()) {
				return new HashSet<>(locations);
			}

			// drop statements
			locations = contextToLocations.get(new PointsToQuery(reference, type, null, member));
			if (!locations.isEmpty()) {
				return new HashSet<>(locations);
			}

			// drop statements & method
			locations = contextToLocations.get(new PointsToQuery(reference, type, null, null));
			if (locations.isEmpty()) {
				if (query.getStmt() != null && reference != null) {
					// statement + reference are unique enough for a last effort
					// exhaustive search
					locations = query(reference, query.getStmt());
					if (!locations.isEmpty()) {
						return new HashSet<>(locations);
					}
				}

				Logger.err("Failed to find a location after dropping both statement and method");
			}
		}

		if (locations.isEmpty() && queryStrategy == QueryStrategy.EXHAUSTIVE) {
			locations = contextToLocations.values();
		}

		return new HashSet<>(locations);
	}

	protected Collection<AbstractLocation> query(IReference reference, IStatement stmt) {
		Set<AbstractLocation> locations = new HashSet<>();
		for (PointsToQuery queryKey : contextToLocations.keySet()) {
			if (queryKey.getStmt() == stmt && reference.equals(queryKey.getReference())) {
				locations.addAll(contextToLocations.get(queryKey));
			}
		}

		if (locations.size() > 1 && queryStrategy == QueryStrategy.MINIMIZE_USAGE_DEFECTS) {
			return Collections.emptySet();
		} else {
			return locations;
		}
	}
}