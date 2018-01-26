/*
 * Copyright 2014 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.recommenders.datastructures;

import cc.kave.commons.assertions.Asserts;

public class Histogram {

	private double[] bins;

	public Histogram(int numBins) {
		bins = new double[numBins];
	}

	// bin is zero based, maxBin is arraysize
	public void add(int binOneBased, int maxBin) {
		Asserts.assertGreaterThan(binOneBased, 0);
		int bin = binOneBased - 1;
		double length = 1 / (double) maxBin;
		double start = bin * length;
		double end = (bin + 1) * length;

		for (int curBin = 0; curBin < bins.length; curBin++) {
			double curLength = 1 / (double) bins.length;
			double curStart = curBin * curLength;
			double curEnd = (curBin + 1) * curLength;

			boolean isOutside = start > curEnd || end < curStart;
			if (!isOutside) {

				boolean hasOverlap = end <= curEnd || start >= curStart;
				if (hasOverlap) {

					double maxStart = Math.max(start, curStart);
					double maxEnd = Math.min(end, curEnd);
					double absoluteOverlap = maxEnd - maxStart;
					Asserts.assertGreaterOrEqual(absoluteOverlap, 0);
					double overlapRatio = absoluteOverlap / curLength;

					// max amount a single bin is assigned
					double binValue = curLength / length;
					double curValue = binValue * overlapRatio;

					bins[curBin] += curValue;
				}

			}
		}
	}

	public double[] getBins() {
		return bins;
	}

	public String toString() {
		return null;
	}
}