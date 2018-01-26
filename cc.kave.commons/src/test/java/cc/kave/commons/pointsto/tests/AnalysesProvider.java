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
package cc.kave.commons.pointsto.tests;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import cc.kave.commons.pointsto.AdvancedPointsToAnalysisFactory;
import cc.kave.commons.pointsto.PointsToAnalysisFactory;
import cc.kave.commons.pointsto.SimplePointsToAnalysisFactory;
import cc.kave.commons.pointsto.analysis.FieldSensitivity;
import cc.kave.commons.pointsto.analysis.ReferenceBasedAnalysis;
import cc.kave.commons.pointsto.analysis.TypeBasedAnalysis;
import cc.kave.commons.pointsto.analysis.inclusion.InclusionAnalysis;
import cc.kave.commons.pointsto.analysis.unification.UnificationAnalysis;

public class AnalysesProvider {

	public static final Collection<PointsToAnalysisFactory> ANALYSES = Arrays.asList(
			new SimplePointsToAnalysisFactory<>(TypeBasedAnalysis.class),
			new SimplePointsToAnalysisFactory<>(ReferenceBasedAnalysis.class),
			new AdvancedPointsToAnalysisFactory<>(UnificationAnalysis.class, FieldSensitivity.FULL),
			new SimplePointsToAnalysisFactory<>(InclusionAnalysis.class));

	public static final Collection<Object[]> ANALYSES_AS_PARAMETERS = ANALYSES.stream().map(a -> new Object[] { a })
			.collect(Collectors.toList());
}
