/**
 * Copyright (c) 2011-2013 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Sebastian Proksch - initial API and implementation
 */
package cc.kave.rsse.calls.mining.clustering;

import org.apache.mahout.common.distance.CosineDistanceMeasure;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.common.distance.ManhattanDistanceMeasure;

import cc.kave.rsse.calls.mining.Options;

public class DistanceMeasureFactory {

	private final Options options;

	public DistanceMeasureFactory(Options options) {
		this.options = options;
	}

	public DistanceMeasure get() {
		switch (options.getOptAsEnum("dist", cc.kave.rsse.calls.mining.clustering.DistanceMeasure.class)) {
		case COSINE:
			return new CosineDistanceMeasure();
		case MANHATTAN:
			return new ManhattanDistanceMeasure();
		}
		throw new RuntimeException("unknown distance measure");
	}
}