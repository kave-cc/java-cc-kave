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
import java.util.Optional;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.ssts.expressions.assignable.ICompletionExpression;
import cc.kave.commons.pointsto.SimplePointsToAnalysisFactory;
import cc.kave.commons.pointsto.analysis.PointsToContext;
import cc.kave.commons.pointsto.analysis.PointsToQueryBuilder;
import cc.kave.commons.pointsto.analysis.TypeBasedAnalysis;
import cc.kave.commons.utils.ssts.SSTNodeHierarchy;
import cc.kave.commons.utils.ssts.completioninfo.CompletionInfo;
import cc.kave.rsse.calls.extraction.usages.PointsToUsageExtractor;
import cc.kave.rsse.calls.usages.Usage;

public class UsageExtractor {

	private final SimplePointsToAnalysisFactory<TypeBasedAnalysis> paFactory;
	private final PointsToUsageExtractor usageExtractor;
	private final PointsToContext p2ctx;
	private final PointsToQueryBuilder queryBuilder;
	private final List<Usage> usages;
	private final List<Usage> queries;
	private SSTNodeHierarchy hierarchy;

	public UsageExtractor(Context ctx) {

		hierarchy = new SSTNodeHierarchy(ctx.getSST());
		paFactory = new SimplePointsToAnalysisFactory<>(TypeBasedAnalysis.class);
		p2ctx = paFactory.create().compute(ctx);
		queryBuilder = new PointsToQueryBuilder(ctx);

		usageExtractor = new PointsToUsageExtractor();
		usages = usageExtractor.extract(p2ctx);

		extractQueries(ctx);
		queries = extractQueries(ctx);
	}

	private List<Usage> extractQueries(Context ctx) {
		Optional<CompletionInfo> info = CompletionInfo.extractCompletionInfoFrom(ctx);
		if (info.isPresent()) {
			ICompletionExpression ce = info.get().getCompletionExpr();
			List<Usage> qs = usageExtractor.extractQueries(ce, p2ctx, queryBuilder, hierarchy);
			if (!qs.isEmpty()) {
				return qs;
			}
		}
		return null;
	}

	public List<Usage> getUsages() {
		return usages;
	}

	public boolean hasCallQuery() {
		return queries != null;
	}

	public Usage getQuery() {
		return queries.iterator().next();
	}
}