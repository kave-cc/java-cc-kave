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
package cc.kave.rsse.calls.mining;

import static cc.kave.commons.assertions.Asserts.assertGreaterOrEqual;
import static cc.kave.commons.assertions.Asserts.assertGreaterThan;
import static cc.kave.commons.assertions.Asserts.assertTrue;
import static java.lang.String.format;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.math.util.MathUtils;

import cc.kave.commons.assertions.Asserts;
import cc.kave.commons.utils.StringUtils;

public class Options {

	public final String approachName;

	public final double weightClassCtx;
	public final double weightMethodCtx;
	public final double weightDef;
	public final double weightCalls;
	public final double weightParams;
	public final double weightMembers;

	public final int keepOnlyFeaturesWithAtLeastOccurrences;
	public final double minProbability;

	public final Map<String, String> opts;

	public boolean useClassCtx() {
		return weightClassCtx > 0;
	}

	public boolean useMethodCtx() {
		return weightMethodCtx > 0;
	}

	public boolean useDef() {
		return weightDef > 0;
	}

	public boolean useCalls() {
		return weightCalls > 0;
	}

	public boolean useParams() {
		return weightParams > 0;
	}

	public boolean useMembers() {
		return weightMembers > 0;
	}

	public int getOptAsInt(String key) {
		return Integer.valueOf(opts.get(key));
	}

	public double getOptAsDouble(String key) {
		return Double.valueOf(opts.get(key));
	}

	public boolean getOptAsBool(String key) {
		return Boolean.valueOf(opts.get(key).toLowerCase());
	}

	public <T> T getOptAsEnum(String key, Class<T> classOfEnum) {
		assertTrue(classOfEnum.isEnum());
		String val = opts.get(key);
		for (T t : classOfEnum.getEnumConstants()) {
			if (val.equals(t.toString())) {
				return t;
			}
		}
		throw new IllegalArgumentException(
				format("Unable to find '%s' in enum '%s'.", val, classOfEnum.getSimpleName()));
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("APP[").append(approachName).append("]");

		appendWeight(sb, "CCTX", weightClassCtx);
		appendWeight(sb, "MCTX", weightMethodCtx);
		appendWeight(sb, "DEF", weightDef);
		appendWeight(sb, "CALLS", weightCalls);
		appendWeight(sb, "PARAMS", weightParams);
		appendWeight(sb, "MEMBERS", weightMembers);

		if (keepOnlyFeaturesWithAtLeastOccurrences > 1) {
			sb.append("+ATLEAST(" + keepOnlyFeaturesWithAtLeastOccurrences + ")");
		}
		if (minProbability > 0.005) {
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
		if (weight >= 0.005) {
			sb.append('+').append(key);
			if (weight < 0.995) {
				sb.append("(").append(format("%.2f", weight)).append(")");
			}
		}
	}

	public Options(String approachName, double weightClassCtx, double weightMethodCtx, double weightDef,
			double weightCalls, double weightParams, double weightMembers, int minOccurrences, double minProbability,
			Map<String, String> opts) {
		this.approachName = approachName;
		this.weightClassCtx = round(weightClassCtx);
		this.weightMethodCtx = round(weightMethodCtx);
		this.weightDef = round(weightDef);
		this.weightCalls = round(weightCalls);
		this.weightParams = round(weightParams);
		this.weightMembers = round(weightMembers);
		this.keepOnlyFeaturesWithAtLeastOccurrences = minOccurrences;
		this.minProbability = round(minProbability);
		this.opts = new LinkedHashMap<>(opts);
		validateState();
	}

	private double round(double d) {
		return MathUtils.round(d, 2);
	}

	public Options(String opts) {
		this.approachName = parseBody(opts, "APP[");
		this.weightClassCtx = round(parseWeight(opts, "CCTX"));
		this.weightMethodCtx = round(parseWeight(opts, "MCTX"));
		this.weightDef = round(parseWeight(opts, "DEF"));
		this.weightCalls = round(parseWeight(opts, "CALLS"));
		this.weightParams = round(parseWeight(opts, "PARAMS"));
		this.weightMembers = round(parseWeight(opts, "MEMBERS"));
		this.keepOnlyFeaturesWithAtLeastOccurrences = parseInt(opts, "ATLEAST", 1);
		this.minProbability = round(parseWeight(opts, "P_MIN"));
		this.opts = parseOpts(opts);
		validateState();
	}

	private Map<String, String> parseOpts(String opts) {
		Map<String, String> m = new LinkedHashMap<>();
		int start = opts.indexOf("+OPTS[");
		if (start == -1) {
			return m;
		}
		int content = start + 6;
		int close = StringUtils.FindCorrespondingCloseBracket(opts, content - 1);
		String body = opts.substring(content, close);
		for (String entry : body.split(";")) {
			String[] parts = entry.split(":");
			m.put(parts[0], parts[1]);
		}
		return m;
	}

	private void validateState() {
		// app name
		validateString(approachName);
		// weights
		assertGreaterOrEqual(weightClassCtx, 0);
		assertGreaterOrEqual(1, weightClassCtx);
		assertGreaterOrEqual(weightMethodCtx, 0);
		assertGreaterOrEqual(1, weightMethodCtx);
		assertGreaterOrEqual(weightDef, 0);
		assertGreaterOrEqual(1, weightDef);
		assertGreaterOrEqual(weightCalls, 0);
		assertGreaterOrEqual(1, weightCalls);
		assertGreaterOrEqual(weightParams, 0);
		assertGreaterOrEqual(1, weightParams);
		assertGreaterOrEqual(weightMembers, 0);
		assertGreaterOrEqual(1, weightMembers);
		assertGreaterOrEqual(minProbability, 0);
		assertGreaterThan(1, minProbability);
		// atLeast
		assertGreaterOrEqual(keepOnlyFeaturesWithAtLeastOccurrences, 1);
	}

	private static void validateString(String s) {
		Asserts.assertNotNull(s);
		Asserts.assertFalse(s.isEmpty());
		for (char c : new char[] { ',', ';', '[', ']', ':', '\n', '\t', ' ' }) {
			Asserts.assertTrue(s.indexOf(c) == -1);
		}
	}

	private int parseInt(String opts, String key, int defaultVal) {
		int start = opts.indexOf(key);
		if (start == -1) {
			return defaultVal;
		}
		int open = start + key.length();
		int close = StringUtils.FindCorrespondingCloseBracket(opts, open);
		String val = opts.substring(open + 1, close);
		return Integer.parseInt(val);
	}

	private static String parseBody(String opts, String key) {
		int start = opts.indexOf(key);
		int contentStart = start + key.length();
		int contentEnd = StringUtils.FindCorrespondingCloseBracket(opts, contentStart - 1);
		return opts.substring(contentStart, contentEnd);
	}

	private double parseWeight(String opts, String key) {
		int start = opts.indexOf(key);
		if (start == -1) {
			return 0;
		}
		int open = start + key.length();
		if (opts.length() <= open || opts.charAt(open) != '(') {
			return 1;
		}
		int close = StringUtils.FindCorrespondingCloseBracket(opts, open);
		String val = opts.substring(open + 1, close);
		return Double.parseDouble(val);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Options) {
			return toString().equals(obj.toString());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return 1 + toString().hashCode();
	}
}