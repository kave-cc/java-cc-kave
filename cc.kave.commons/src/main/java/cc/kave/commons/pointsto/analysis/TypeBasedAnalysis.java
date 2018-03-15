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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.pointsto.analysis.types.TypeCollector;
import cc.kave.commons.pointsto.analysis.utils.EnclosingNodeHelper;
import cc.kave.commons.utils.io.Logger;
import cc.kave.commons.utils.ssts.SSTNodeHierarchy;

/**
 * A {@link PointsToAnalysis} that assumes that all variables of a specific type
 * point to one {@link AbstractLocation}.
 *
 */
public class TypeBasedAnalysis extends AbstractPointsToAnalysis {

	@Override
	public PointsToContext compute(Context context) {
		checkContextBinding();

		SSTNodeHierarchy hierarchy = new SSTNodeHierarchy(context.getSST());
		EnclosingNodeHelper encNodes = new EnclosingNodeHelper(hierarchy);

		TypeCollector typeCollector = new TypeCollector(context);
		for (IReference ref : typeCollector.getReferences()) {
			ITypeName type = typeCollector.getType(ref);
			IStatement stmt = encNodes.getEnclosingStatement(ref);
			PointsToQuery key = new PointsToQuery(ref, type, stmt, encNodes.getEnclosingMember(stmt));
			contextToLocations.put(key, new AbstractLocation());
		}

		return new PointsToContext(context, this);
	}

	@Override
	public Set<AbstractLocation> query(PointsToQuery query) {
		ITypeName type = query.getType();
		if (type != null) {
			// querying for void types does not make any sense...
			if (type.isVoidType()) {
				return Collections.emptySet();
			}

			if (type.isUnknown() || type.isTypeParameter()) {
				Logger.debug("Queried for an unknown/generic type");

				if (queryStrategy == QueryStrategy.EXHAUSTIVE) {
					// assume that unknown types may point to any known location
					return new HashSet<>(contextToLocations.values());
				} else {
					return Collections.emptySet();
				}
			}
		}

		// lower query to used format
		return super.query(new PointsToQuery(null, query.getType(), null, null));
	}
}