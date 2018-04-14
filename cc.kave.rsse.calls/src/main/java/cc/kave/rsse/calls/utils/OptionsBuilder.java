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
import static java.lang.String.format;

import java.util.LinkedHashMap;
import java.util.Map;

import cc.kave.commons.assertions.Asserts;
import cc.kave.rsse.calls.mining.MiningOptions.DistanceMeasure;

public class OptionsBuilder {
	// be aware that existing evaluations might depend on these defaults!!

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
		assertNotNull(approachName);
		assertFalse(approachName.isEmpty());
		assertFalse(approachName.contains("]"));
		this.approachName = approachName;
	}

	public static OptionsBuilder bmn() {
		OptionsBuilder b = new OptionsBuilder("bmn");
		b.option("dist", DistanceMeasure.MANHATTAN.toString());
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
		OptionsBuilder b = new OptionsBuilder("canopy");
		b.option("dist", DistanceMeasure.COSINE.toString());
		b.option("t1", t1);
		b.option("t2", t2);
		b.option("prec", "double");
		return b;
	}

	public String get() {
		StringBuilder sb = new StringBuilder();
		sb.append("APP[").append(approachName).append("]");

		appendWeight(sb, "CCTX", weightClassCtx);
		appendWeight(sb, "MCTX", weightMethodCtx);
		appendWeight(sb, "DEF", weightDef);
		appendWeight(sb, "CALLS", weightCalls);
		appendWeight(sb, "PARAMS", weightParams);
		appendWeight(sb, "MEMBERS", weightMembers);

		if (minOccurrences > 1) {
			sb.append("+ATLEAST(" + minOccurrences + ")");
		}
		if (minProbability != 0) {
			sb.append("+P_MIN(").append(format("%.2f", minProbability)).append(")");
		}
		if (opts.keySet().size() > 0) {
			sb.append("+OPTS[");
			boolean isFirst = true;
			for (String k : opts.keySet()) {
				if (!isFirst) {
					sb.append(';');
				}
				isFirst = false;
				sb.append(k).append(':').append(opts.get(k));
			}
			sb.append("]");
		}
		return sb.toString();
	}

	private static void appendWeight(StringBuilder sb, String key, double weight) {
		// real precision is 1 digit, but this mimics rounding
		if (weight >= 0.05) {
			sb.append('+').append(key);
			if (weight < 0.95) {
				sb.append("(").append(format("%.1f", weight)).append(")");
			}
		}
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
		Asserts.assertNotNull(k);
		Asserts.assertNotNull(v);
		Asserts.assertFalse(k.isEmpty());
		Asserts.assertFalse(v.isEmpty());
		char[] notAllowed = new char[] { ';', ':', '\n', '\t', '[', ']', ' ' };
		for (char c : notAllowed) {
			assertNotExists(k, c);
			assertNotExists(v, c);
		}
		opts.put(k, v);
		return this;
	}

	private void assertNotExists(String haystack, char needle) {
		if (haystack.indexOf(needle) != -1) {
			Asserts.fail("Unexpected, '%s' contains forbidden char '%c'.", haystack, needle);
		}
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