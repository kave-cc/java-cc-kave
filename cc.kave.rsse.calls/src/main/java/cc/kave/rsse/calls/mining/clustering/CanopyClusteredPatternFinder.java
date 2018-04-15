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

import static cc.kave.commons.assertions.Asserts.assertGreaterThan;
import static cc.kave.commons.assertions.Asserts.assertNotNegative;
import static org.apache.mahout.clustering.canopy.CanopyClusterer.createCanopies;

import java.util.List;

import org.apache.mahout.clustering.DistanceMeasureCluster;
import org.apache.mahout.clustering.canopy.Canopy;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.math.Vector;

import cc.kave.commons.assertions.Asserts;
import cc.kave.rsse.calls.mining.Options;
import cc.kave.rsse.calls.mining.VectorBuilder;

public class CanopyClusteredPatternFinder extends PatternFinder {

	private DistanceMeasure distanceMeasure;

	private double t1;
	private double t2;

	public CanopyClusteredPatternFinder(VectorBuilder vb, Options opts) {
		super(vb);
		Asserts.fail("set fields (+ public/final)");
		assertNotNegative(t1);
		assertNotNegative(t2);
		assertGreaterThan(t1, t2);
	}

	public double getT1() {
		return t1;
	}

	public double getT2() {
		return t2;
	}

	@Override
	protected List<? extends DistanceMeasureCluster> cluster(List<Vector> vectors) {
		List<Canopy> canopies = createCanopies(vectors, distanceMeasure, t1, t2);
		return canopies;
	}
}