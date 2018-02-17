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
package cc.kave.commons.evaluation;

import static org.apache.commons.math.util.MathUtils.round;

import cc.kave.commons.assertions.Asserts;

public class Variance {

	public static final double DATA_PRECISION = 0.00001;
	public static final int DATA_SCALE = 5;

	private final int num;
	private final double mean;
	private final double variance;

	public Variance(int num, double mean, double variance) {
		Asserts.assertGreaterThan(num, 0);
		Asserts.assertGreaterOrEqual(variance, 0);

		this.num = num;
		this.mean = round(mean, DATA_SCALE);
		this.variance = round(variance, DATA_SCALE);
	}

	public double getVariance() {
		return variance;
	}

	public double getMean() {
		return mean;
	}

	public int getNumberOfInput() {
		return num;
	}
}