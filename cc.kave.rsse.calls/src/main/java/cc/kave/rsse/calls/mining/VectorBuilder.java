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

import static cc.kave.rsse.calls.model.Constants.UNKNOWN_CCF;
import static cc.kave.rsse.calls.model.Constants.UNKNOWN_DF;
import static cc.kave.rsse.calls.model.Constants.UNKNOWN_MCF;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import cc.kave.rsse.calls.model.Constants;
import cc.kave.rsse.calls.model.Dictionary;
import cc.kave.rsse.calls.model.features.CallParameterFeature;
import cc.kave.rsse.calls.model.features.ClassContextFeature;
import cc.kave.rsse.calls.model.features.DefinitionFeature;
import cc.kave.rsse.calls.model.features.IFeature;
import cc.kave.rsse.calls.model.features.MemberAccessFeature;
import cc.kave.rsse.calls.model.features.MethodContextFeature;
import cc.kave.rsse.calls.model.features.Pattern;
import cc.kave.rsse.calls.model.features.TypeFeature;

public class VectorBuilder {

	private Options opts;

	public VectorBuilder(Options opts) {
		if (!(opts.useCalls() || opts.useParams() || opts.useMembers())) {
			throw new IllegalArgumentException("All usages will be filtered, if no UsageSiteType is enabled.");
		}
		this.opts = opts;
	}

	public List<double[]> toDoubleArrays(List<List<IFeature>> usages, Dictionary<IFeature> dict) {
		assertInput(usages, dict);
		List<double[]> vectors = new ArrayList<>(usages.size());
		for (List<IFeature> usage : usages) {
			Optional<double[]> arr = toDoubleArray(usage, dict);
			if (arr.isPresent()) {
				vectors.add(arr.get());
			}
		}
		return vectors;
	}

	public List<boolean[]> toBoolArrays(List<List<IFeature>> usages, Dictionary<IFeature> dict) {
		assertInput(usages, dict);
		List<boolean[]> vectors = new ArrayList<>(usages.size());
		for (List<IFeature> usage : usages) {
			Optional<boolean[]> arr = toBoolArray(usage, dict);
			if (arr.isPresent()) {
				vectors.add(arr.get());
			}
		}
		return vectors;
	}

	public Optional<double[]> toDoubleArray(List<IFeature> usage, Dictionary<IFeature> dict) {
		assertInput(usage, dict);

		double[] arr = new double[dict.size()];
		for (int i = 0; i < dict.size(); i++) {
			IFeature f = dict.getEntry(i);
			arr[i] = usage.contains(f) ? weight(f) : 0;
		}

		int idxUnknownCctx = dict.getId(UNKNOWN_CCF);
		int idxUnknownMctx = dict.getId(UNKNOWN_MCF);
		int idxUnknownDef = dict.getId(UNKNOWN_DF);

		int numSites = 0;
		for (IFeature f : usage) {
			if (dict.contains(f)) {
				if (f instanceof MemberAccessFeature) {
					numSites++;
				}
			} else {
				// assert every usage gets cCtx/mCtx/def set!
				if (f instanceof ClassContextFeature) {
					arr[idxUnknownCctx] = opts.weightClassCtx;
				} else if (f instanceof MethodContextFeature) {
					arr[idxUnknownMctx] = opts.weightMethodCtx;
				} else if (f instanceof DefinitionFeature) {
					arr[idxUnknownDef] = opts.weightDef;
				}
			}
		}
		if (numSites == 0) {
			return Optional.empty();
		} else {
			return Optional.of(arr);
		}
	}

	public Optional<boolean[]> toBoolArray(List<IFeature> usage, Dictionary<IFeature> dict) {
		assertInput(usage, dict);

		boolean[] arr = new boolean[dict.size()];
		for (int i = 0; i < dict.size(); i++) {
			IFeature f = dict.getEntry(i);
			arr[i] = usage.contains(f) ? true : false;
		}

		int idxUnknownCctx = dict.getId(UNKNOWN_CCF);
		int idxUnknownMctx = dict.getId(UNKNOWN_MCF);
		int idxUnknownDef = dict.getId(UNKNOWN_DF);

		int numSites = 0;
		for (IFeature f : usage) {
			if (dict.contains(f)) {
				if (f instanceof MemberAccessFeature) {
					numSites++;
				}
			} else {
				// assert every usage gets cCtx/mCtx/def set!
				if (f instanceof ClassContextFeature) {
					arr[idxUnknownCctx] = opts.useClassCtx();
				} else if (f instanceof MethodContextFeature) {
					arr[idxUnknownMctx] = opts.useMethodCtx();
				} else if (f instanceof DefinitionFeature) {
					arr[idxUnknownDef] = opts.useDef();
				}
			}
		}
		if (numSites == 0) {
			return Optional.empty();
		} else {
			return Optional.of(arr);
		}
	}

	public Pattern toPattern(int count, double[] weightedArr, Dictionary<IFeature> dict) {
		assertPositive(count);
		assertDictionary(dict);
		assertArr(weightedArr, dict);

		double[] arr = new double[weightedArr.length];
		for (int i = 0; i < arr.length; i++) {
			IFeature f = dict.getEntry(i);
			arr[i] = unweight(f, weightedArr[i]);
		}

		return new Pattern(count, arr, dict);
	}

	// ####################################################################################################

	private double weight(IFeature f) {
		if (f instanceof TypeFeature) {
			return 1.0;
		}
		if (f instanceof ClassContextFeature) {
			return opts.weightClassCtx;
		}
		if (f instanceof MethodContextFeature) {
			return opts.weightMethodCtx;
		}
		if (f instanceof DefinitionFeature) {
			return opts.weightDef;
		}
		if (f instanceof CallParameterFeature) {
			return opts.weightParams;
		}
		MemberAccessFeature usf = (MemberAccessFeature) f;
		switch (usf.memberAccess.getType()) {
		case METHOD_CALL:
			return opts.weightCalls;
		default: // MEMBER_REFERENCE:
			return opts.weightMembers;
		}
	}

	private double unweight(IFeature f, double in) {
		double w = in / weight(f);
		assertWeight(w);
		return w;
	}

	// ####################################################################################################

	private void assertInput(Collection<?> c, Dictionary<IFeature> dict) {
		assertNotNull(c);
		assertNotEmpty(c);
		assertDictionary(dict);
	}

	private static void assertDictionary(Dictionary<IFeature> dict) {
		assertNotNull(dict);
		assertPositive(dict.size());
		if (!dict.contains(Constants.UNKNOWN_CCF) || !dict.contains(Constants.UNKNOWN_MCF)
				|| !dict.contains(Constants.UNKNOWN_DF)) {
			throw new IllegalArgumentException("Dictionary is missing at least one UNKNOWN entry.");
		}
	}

	private static void assertArr(double[] arr, Dictionary<IFeature> dict) {
		assertNotNull(arr);
		if (arr.length != dict.size()) {
			throw new IllegalArgumentException("Unexpected, array size and dictionary size do not match.");
		}
	}

	private static void assertNotNull(Object o) {
		if (o == null) {
			throw new IllegalArgumentException("Unexpected, object should not be null.");
		}
	}

	private static void assertNotEmpty(Collection<?> c) {
		if (c.isEmpty()) {
			throw new IllegalArgumentException("Unexpected, collection should not be empty.");
		}
	}

	private static void assertPositive(int num) {
		if (num < 1) {
			throw new IllegalArgumentException("Unexpected, number should be positive.");
		}
	}

	private static void assertWeight(double w) {
		if (w < 0 || w > 1) {
			throw new IllegalArgumentException("Unexpected, weight exceed the [0,1] interval.");
		}
	}
}