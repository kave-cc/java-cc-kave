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

import static cc.kave.commons.utils.ssts.completioninfo.VariableScope.ErrorHandling.LOG;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import cc.kave.caret.analyses.TypeBasedPointsToAnalysis;
import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.rsse.calls.analysis.UsageExtraction;
import cc.kave.rsse.calls.model.usages.IUsage;

public class UsageExtractor {

	private final List<IUsage> usages;
	private final List<IUsage> queries = new LinkedList<>();

	public UsageExtractor(Context ctx) {

		TypeBasedPointsToAnalysis p2a = new TypeBasedPointsToAnalysis(LOG);
		UsageExtraction ue = new UsageExtraction(p2a);
		usages = ue.extract(ctx);
		Iterator<IUsage> it = usages.iterator();
		while (it.hasNext()) {
			IUsage u = it.next();
			if (u.isQuery()) {
				it.remove();
				queries.add(u);
			}
		}
	}

	public List<IUsage> getUsages() {
		return usages;
	}

	public boolean hasQuery() {
		return !queries.isEmpty();
	}

	public IUsage getQuery() {
		return queries.get(0);
	}
}