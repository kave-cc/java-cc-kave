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

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.math.util.MathUtils.round;

public class Boxplot {

	public static final double DATA_PRECISION = 0.00001;
	public static final int DATA_SCALE = 5;

	private final int numValues;
	private final double mean;

	private final double lowerWhisker;
	private final double lowerQuartil;
	private final double median;
	private final double upperQuartil;
	private final double upperWhisker;

	public Boxplot(int numValues, double mean, double lowerWhisker, double lowerQuartil, double median,
			double upperQuartil, double upperWhisker) {
		this.numValues = numValues;
		this.mean = round(mean, DATA_SCALE);
		this.lowerWhisker = round(lowerWhisker, DATA_SCALE);
		this.lowerQuartil = round(lowerQuartil, DATA_SCALE);
		this.median = round(median, DATA_SCALE);
		this.upperQuartil = round(upperQuartil, DATA_SCALE);
		this.upperWhisker = round(upperWhisker, DATA_SCALE);
	}

	public int getNumValues() {
		return numValues;
	}

	public double getMean() {
		return mean;
	}

	public double getLowerWhisker() {
		return lowerWhisker;
	}

	public double getLowerQuartil() {
		return lowerQuartil;
	}

	public double getMedian() {
		return median;
	}

	public double getUpperQuartil() {
		return upperQuartil;
	}

	public double getUpperWhisker() {
		return upperWhisker;
	}

	@Override
	public boolean equals(Object obj) {
		return reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return reflectionHashCode(this);
	}

	@Override
	public String toString() {
		return String.format("[%d values (avg: %.3f) - %.2f; %.2f; %.2f; %.2f; %.2f]", numValues, mean, lowerWhisker,
				lowerQuartil, median, upperQuartil, upperWhisker);
	}
}