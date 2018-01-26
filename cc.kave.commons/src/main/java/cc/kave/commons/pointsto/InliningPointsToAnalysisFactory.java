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
package cc.kave.commons.pointsto;

import java.util.Set;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.ssts.impl.visitor.inlining.InliningContext;
import cc.kave.commons.model.ssts.impl.visitor.inlining.InliningVisitor;
import cc.kave.commons.pointsto.analysis.AbstractLocation;
import cc.kave.commons.pointsto.analysis.PointsToAnalysis;
import cc.kave.commons.pointsto.analysis.PointsToContext;
import cc.kave.commons.pointsto.analysis.PointsToQuery;
import cc.kave.commons.pointsto.analysis.QueryStrategy;

public class InliningPointsToAnalysisFactory implements PointsToAnalysisFactory {

	private final PointsToAnalysisFactory baseFactory;

	public InliningPointsToAnalysisFactory(PointsToAnalysisFactory baseFactory) {
		this.baseFactory = baseFactory;
	}

	@Override
	public String getName() {
		return "Inlining_" + baseFactory.getName();
	}

	@Override
	public PointsToAnalysis create() {
		return new InliningPointsToAnalysis(baseFactory.create());
	}

	private static class InliningPointsToAnalysis implements PointsToAnalysis {

		private final PointsToAnalysis baseAnalysis;

		public InliningPointsToAnalysis(PointsToAnalysis baseAnalysis) {
			this.baseAnalysis = baseAnalysis;
		}

		@Override
		public PointsToContext compute(Context context) {
			InliningVisitor visitor = new InliningVisitor();
			InliningContext inliningContext = new InliningContext();
			context.getSST().accept(visitor, inliningContext);

			Context inlinedContext = new Context();
			inlinedContext.setSST(inliningContext.getSST());
			inlinedContext.setTypeShape(context.getTypeShape());

			return baseAnalysis.compute(inlinedContext);
		}

		@Override
		public QueryStrategy getQueryStrategy() {
			return baseAnalysis.getQueryStrategy();
		}

		@Override
		public void setQueryStrategy(QueryStrategy strategy) {
			baseAnalysis.setQueryStrategy(strategy);
		}

		@Override
		public Set<AbstractLocation> query(PointsToQuery query) {
			return baseAnalysis.query(query);
		}

	}
}
