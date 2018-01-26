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
package cc.kave.commons.pointsto;

import cc.kave.commons.pointsto.analysis.PointsToAnalysis;

/**
 * A {@link PointsToAnalysisFactory} which uses reflection to get the name of the analysis and to create a new instance.
 */
public class SimplePointsToAnalysisFactory<T extends PointsToAnalysis> implements PointsToAnalysisFactory {

	private Class<T> analysisClass;

	public SimplePointsToAnalysisFactory(Class<T> analysisClass) {
		this.analysisClass = analysisClass;
	}

	@Override
	public String getName() {
		return analysisClass.getSimpleName();
	}

	@Override
	public PointsToAnalysis create() {
		try {
			return analysisClass.getConstructor().newInstance();
		} catch (Exception e) {
			// forward exception as there is no proper way to handle this here
			throw new RuntimeException(e);
		}
	}

}
