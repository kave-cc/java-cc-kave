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

import cc.kave.commons.pointsto.analysis.FieldSensitivity;
import cc.kave.commons.pointsto.analysis.PointsToAnalysis;

/**
 * A {@link PointsToAnalysisFactory} for context aware {@link PointsToAnalysis} implementations that require additional
 * constructor arguments.
 */
public class AdvancedPointsToAnalysisFactory<T extends PointsToAnalysis> implements PointsToAnalysisFactory {

	private Class<T> analysisClass;
	private FieldSensitivity fieldSensitivity;

	public AdvancedPointsToAnalysisFactory(Class<T> analysisClass, FieldSensitivity fieldSensitivity) {
		this.analysisClass = analysisClass;
		this.fieldSensitivity = fieldSensitivity;
	}

	@Override
	public String getName() {
		return analysisClass.getSimpleName() + "_" + fieldSensitivity.toString();
	}

	@Override
	public PointsToAnalysis create() {
		try {
			return analysisClass.getConstructor(fieldSensitivity.getClass()).newInstance(fieldSensitivity);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

}
