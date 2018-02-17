/**
 * Copyright (c) 2010, 2011 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Sebastian Proksch - initial API and implementation
 */
package cc.kave.commons.evaluation;

import static cc.kave.commons.assertions.Asserts.assertFalse;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.math.stat.StatUtils;
import org.apache.commons.math.stat.descriptive.rank.Percentile;

import com.google.common.collect.Lists;

// TODO rename class to something more generic that also reflects mean/variance calculation
public class BoxplotData {

	private List<Double> data = Lists.newLinkedList();
	private double[] dataAsCachedArray;

	public void add(Double d) {
		data.add(d);
	}

	public boolean hasData() {
		return !data.isEmpty();
	}

	public Boxplot getBoxplot() {
		assertFalse(data.isEmpty());
		double mean = getMean();
		double p5 = getPercentil(5);
		double p25 = getPercentil(25);
		double p50 = getPercentil(50);
		double p75 = getPercentil(75);
		double p95 = getPercentil(95);
		return new Boxplot(data.size(), mean, p5, p25, p50, p75, p95);
	}

	public double getMean() {
		refreshCache();
		return StatUtils.mean(dataAsCachedArray);
	}

	public double getVarianceRaw() {
		refreshCache();
		return StatUtils.variance(dataAsCachedArray);
	}

	public Variance getVariance() {
		double mean = getMean();
		double variance = getVarianceRaw();
		return new Variance(data.size(), mean, variance);
	}

	public double getPercentil(int percent) {
		refreshCache();
		Percentile p = new Percentile(percent);
		// sorting is implicitly done in the evaluate method
		return p.evaluate(dataAsCachedArray);
	}

	private void refreshCache() {
		if (dataAsCachedArray == null || data.size() != dataAsCachedArray.length) {
			dataAsCachedArray = new double[data.size()];
			int i = 0;
			for (Double d : data) {
				dataAsCachedArray[i++] = d;
			}
		}
	}

	public void addAll(BoxplotData other) {
		for (double v : other.data) {
			add(v);
		}
	}

	public double[] getRawValues() {
		refreshCache();
		return dataAsCachedArray;
	}

	public void addAll(double[] values) {
		for (double v : values) {
			add(v);
		}
	}

	public static BoxplotData from(double[] vs) {
		BoxplotData bd = new BoxplotData();
		bd.addAll(vs);
		return bd;
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
}