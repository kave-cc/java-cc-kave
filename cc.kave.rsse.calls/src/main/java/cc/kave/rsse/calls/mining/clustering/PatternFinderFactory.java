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
package cc.kave.rsse.calls.mining.clustering;

import com.google.inject.Inject;

import cc.kave.rsse.calls.mining.Options;
import cc.kave.rsse.calls.mining.VectorBuilder;

public class PatternFinderFactory {

	private final VectorBuilder vectorBuilder;
	private final Options options;

	@Inject
	public PatternFinderFactory(VectorBuilder vectorBuilder, Options options) {
		this.vectorBuilder = vectorBuilder;
		this.options = options;
	}

	public PatternFinder createPatternFinder() {

		switch (options.getOptAsEnum("algo", ClusteringAlgorithm.class)) {
		case CANOPY:
			return createCanopyClusterer();

		case KMEANS:
			return createKmeansClusterer();

		case KMEANS_AND_CANOPY:
			return createCombinedClusterer();
		}

		throw new IllegalStateException();

	}

	private CanopyClusteredPatternFinder createCanopyClusterer() {
		return new CanopyClusteredPatternFinder(vectorBuilder, options);
	}

	private PatternFinder createKmeansClusterer() {
		return new KMeansClusteredPatternFinder(vectorBuilder, options);
	}

	private PatternFinder createCombinedClusterer() {
		return new CombinedKmeansAndCanopyClusteredPatternFinder(vectorBuilder, options);
	}
}