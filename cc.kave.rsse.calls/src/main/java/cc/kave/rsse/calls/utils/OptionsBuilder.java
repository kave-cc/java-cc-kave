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
package cc.kave.rsse.calls.utils;

import static cc.kave.commons.assertions.Asserts.assertFalse;
import static cc.kave.commons.assertions.Asserts.assertNotNull;

import java.util.LinkedHashMap;
import java.util.Map;

import cc.kave.commons.assertions.Asserts;
import cc.kave.rsse.calls.mining.Options;
import cc.kave.rsse.calls.mining.clustering.ClusteringAlgorithm;
import cc.kave.rsse.calls.mining.clustering.DistanceMeasure;

public class OptionsBuilder {

	private final String approachName;

	private double weightClassCtx;
	private double weightMethodCtx = 1;
	private double weightDef;
	private double weightCalls = 1;
	private double weightParams;
	private double weightMembers;

	private int minOccurrences = 1;
	private double minProbability = 0;

	private final Map<String, String> opts = new LinkedHashMap<>();

	public OptionsBuilder(String approachName) {
		assertValidString(approachName);
		this.approachName = approachName;
	}

	public static OptionsBuilder bmn() {
		OptionsBuilder b = new OptionsBuilder("bmn");
		return b;
	}

	public static OptionsBuilder pbn(int i) {
		String t1, t2;
		if (i == 0) {
			t1 = "0.002";
			t2 = "0.001";
		} else {
			t1 = String.format("%.3f", i * 0.01 + 0.001);
			t2 = String.format("%.2f", i * 0.01);
		}
		OptionsBuilder b = new OptionsBuilder("pbn");
		b.option("algo", ClusteringAlgorithm.CANOPY.toString());
		b.option("dist", DistanceMeasure.COSINE.toString());
		b.option("t1", t1);
		b.option("t2", t2);
		b.option("prec", "double");
		return b;
	}

	public Options get() {
		return new Options(approachName, weightClassCtx, weightMethodCtx, weightDef, weightCalls, weightParams,
				weightMembers, minOccurrences, minProbability, opts);
	}

	public OptionsBuilder cCtx(boolean isEnabled) {
		cCtx(isEnabled ? 1 : 0);
		return this;
	}

	public OptionsBuilder cCtx(double weight) {
		assertValidWeight(weight);
		this.weightClassCtx = weight;
		return this;
	}

	public OptionsBuilder mCtx(boolean isEnabled) {
		mCtx(isEnabled ? 1 : 0);
		return this;
	}

	public OptionsBuilder mCtx(double weight) {
		assertValidWeight(weight);
		this.weightMethodCtx = weight;
		return this;
	}

	public OptionsBuilder def(boolean isEnabled) {
		def(isEnabled ? 1 : 0);
		return this;
	}

	public OptionsBuilder def(double weight) {
		assertValidWeight(weight);
		this.weightDef = weight;
		return this;
	}

	public OptionsBuilder calls(boolean isEnabled) {
		calls(isEnabled ? 1 : 0);
		return this;
	}

	public OptionsBuilder calls(double weight) {
		assertValidWeight(weight);
		this.weightCalls = weight;
		return this;
	}

	public OptionsBuilder params(boolean isEnabled) {
		params(isEnabled ? 1 : 0);
		return this;
	}

	public OptionsBuilder params(double weight) {
		assertValidWeight(weight);
		this.weightParams = weight;
		return this;
	}

	public OptionsBuilder members(boolean isEnabled) {
		members(isEnabled ? 1 : 0);
		return this;
	}

	public OptionsBuilder members(double weight) {
		assertValidWeight(weight);
		this.weightMembers = weight;
		return this;
	}

	public OptionsBuilder atLeast(int num) {
		Asserts.assertGreaterOrEqual(num, 1);
		minOccurrences = num;
		return this;
	}

	public OptionsBuilder minProbability(double probability) {

		Asserts.assertGreaterOrEqual(probability, 0);
		Asserts.assertGreaterThan(1, probability);
		this.minProbability = probability;
		return this;
	}

	public OptionsBuilder noMinProbability() {
		minProbability(0);
		return this;
	}

	public OptionsBuilder option(String k, String v) {
		assertValidString(k);
		assertValidString(v);
		opts.put(k, v);
		return this;
	}

	private static void assertValidString(String v) {
		assertNotNull(v);
		assertFalse(v.isEmpty());
		char[] notAllowed = new char[] { ';', ':', '\n', '\t', '[', ']', ' ' };
		for (char c : notAllowed) {
			if (v.indexOf(c) != -1) {
				Asserts.fail("Unexpected, '%s' contains forbidden char '%c'.", v, c);
			}
		}

		assertFalse(v.contains("]"));
	}

	public OptionsBuilder optionDel(String k) {
		opts.remove(k);
		return this;
	}

	private static void assertValidWeight(double weight) {
		Asserts.assertGreaterOrEqual(1, weight);
		Asserts.assertLessOrEqual(0, weight);
	}
}