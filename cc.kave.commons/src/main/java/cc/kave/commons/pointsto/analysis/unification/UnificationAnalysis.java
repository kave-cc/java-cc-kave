/**
 * Copyright 2016 Simon Reuß
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
package cc.kave.commons.pointsto.analysis.unification;

import java.util.List;
import java.util.Map;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.pointsto.analysis.AbstractLocation;
import cc.kave.commons.pointsto.analysis.AbstractPointsToAnalysis;
import cc.kave.commons.pointsto.analysis.FieldSensitivity;
import cc.kave.commons.pointsto.analysis.PointsToContext;
import cc.kave.commons.pointsto.analysis.PointsToQuery;
import cc.kave.commons.pointsto.analysis.references.DistinctReference;
import cc.kave.commons.pointsto.analysis.references.conversion.DistinctReferenceContextCollector;
import cc.kave.commons.pointsto.analysis.references.conversion.DistinctReferenceContextCollectorVisitor;
import cc.kave.commons.pointsto.analysis.references.conversion.QueryKeyTransformer;
import cc.kave.commons.pointsto.analysis.unification.identifiers.LocationIdentifierFactory;
import cc.kave.commons.pointsto.analysis.unification.identifiers.MemberLocationIdentifierFactory;
import cc.kave.commons.pointsto.analysis.unification.identifiers.SteensgaardLocationIdentifierFactory;
import cc.kave.commons.pointsto.analysis.unification.identifiers.TypeLocationIdentifierFactory;
import cc.kave.commons.pointsto.analysis.visitors.ThisReferenceOption;

public class UnificationAnalysis extends AbstractPointsToAnalysis {

	private final FieldSensitivity fieldSensitivity;

	public UnificationAnalysis(FieldSensitivity fieldSensitivity) {
		this.fieldSensitivity = fieldSensitivity;
	}

	private LocationIdentifierFactory getIdentifierFactory() {
		switch (fieldSensitivity) {
		case NONE:
			return new SteensgaardLocationIdentifierFactory();
		case TYPE_BASED:
			return new TypeLocationIdentifierFactory();
		case FULL:
			return new MemberLocationIdentifierFactory();
		default:
			throw new IllegalArgumentException("Unknown field sensitivity: " + fieldSensitivity.toString());
		}
	}

	@Override
	public PointsToContext compute(Context context) {
		checkContextBinding();

		UnificationAnalysisVisitor visitor = new UnificationAnalysisVisitor();
		UnificationAnalysisVisitorContext visitorContext = new UnificationAnalysisVisitorContext(context,
				getIdentifierFactory());

		visitor.visit(context.getSST(), visitorContext);
		Map<DistinctReference, AbstractLocation> referenceToLocation = visitorContext.getReferenceLocations();

		// collect information needed to create the query keys
		DistinctReferenceContextCollector contextCollector = new DistinctReferenceContextCollector(context,
				ThisReferenceOption.PER_CONTEXT);
		new DistinctReferenceContextCollectorVisitor().visit(context.getSST(), contextCollector);

		QueryKeyTransformer transformer = new QueryKeyTransformer(true);
		for (Map.Entry<DistinctReference, AbstractLocation> refLoc : referenceToLocation.entrySet()) {
			List<PointsToQuery> queryKeys = refLoc.getKey().accept(transformer, contextCollector);

			for (PointsToQuery queryKey : queryKeys) {
				contextToLocations.put(queryKey, refLoc.getValue());
			}
		}

		return new PointsToContext(context, this);
	}

}
