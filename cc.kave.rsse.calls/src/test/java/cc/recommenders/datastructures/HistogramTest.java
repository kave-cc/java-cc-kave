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

import static org.junit.Assert.assertArrayEquals;

import org.junit.Before;
import org.junit.Test;

import cc.kave.rsse.calls.datastructures.Histogram;

public class HistogramTest {

	private Histogram sut;

	@Before
	public void setup() {
		sut = new Histogram(3);
	}

	@Test
	public void a() {
		sut.add(1, 3);
		assertBins(1.0, 0.0, 0.0);
	}

	@Test
	public void b() {
		sut.add(2, 3);
		assertBins(0.0, 1.0, 0.0);
	}

	@Test
	public void c() {
		sut.add(1, 2);
		assertBins(0.666, 0.333, 0.0);
	}

	@Test
	public void d() {
		sut.add(2, 4);
		assertBins(0.333, 0.666, 0.0);
	}

	private void assertBins(double... expecteds) {
		double[] actuals = sut.getBins();
		assertArrayEquals(expecteds, actuals, 0.001);
	}
}