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
package cc.kave.rsse.calls.recs.pbn;

import java.util.List;
import java.util.Set;

import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.rsse.calls.mining.DictionaryBuilder;
import cc.kave.rsse.calls.mining.FeatureExtractor;
import cc.kave.rsse.calls.mining.clustering.PatternFinderFactory;
import cc.kave.rsse.calls.model.Dictionary;
import cc.kave.rsse.calls.model.features.CallParameterFeature;
import cc.kave.rsse.calls.model.features.ClassContextFeature;
import cc.kave.rsse.calls.model.features.DefinitionFeature;
import cc.kave.rsse.calls.model.features.IFeature;
import cc.kave.rsse.calls.model.features.MemberAccessFeature;
import cc.kave.rsse.calls.model.features.MethodContextFeature;
import cc.kave.rsse.calls.model.features.Pattern;
import cc.kave.rsse.calls.model.features.TypeFeature;
import cc.kave.rsse.calls.model.usages.IUsage;

public class PBNMiner {

	private final DictionaryBuilder dictBuilder;
	private final FeatureExtractor extractor;
	private final PatternFinderFactory patternFinders;

	public PBNMiner(FeatureExtractor extractor, DictionaryBuilder dictBuilder, PatternFinderFactory patternFinders) {
		this.dictBuilder = dictBuilder;
		this.extractor = extractor;
		this.patternFinders = patternFinders;
	}

	public PBNModel build(List<IUsage> in) {
		List<List<IFeature>> fs = extractor.extract(in);
		Dictionary<IFeature> dict = dictBuilder.build(fs);
		List<Pattern> patterns = patternFinders.createPatternFinder().find(fs, dict);
		return new PBNModelInstantiator(dict, patterns).model;
	}

	private class PBNModelInstantiator {

		private final Dictionary<IFeature> dict;
		private final List<Pattern> patterns;

		private Set<ClassContextFeature> cctxs;
		private Set<MethodContextFeature> mctxs;
		private Set<DefinitionFeature> defs;
		private Set<MemberAccessFeature> accs;
		private Set<CallParameterFeature> params;

		private int numPatterns;
		private int numCctx;
		private int numMctx;
		private int numDefs;
		private int numAccesses;
		private int numParams;

		private PBNModel model;

		public PBNModelInstantiator(Dictionary<IFeature> dict, List<Pattern> patterns) {
			this.dict = dict;
			this.patterns = patterns;
			model = new PBNModel();

			splitDict();
			count();

			fillPatterns();
			fillCCtxs();
			fillMCtxs();
			fillDefs();
			fillMembers();
			fillParams();

			model = PBNModelUtils.normalize(model);
		}

		private void splitDict() {
			model.type = dict.getAllEntries(TypeFeature.class).iterator().next().type;

			cctxs = dict.getAllEntries(ClassContextFeature.class);
			mctxs = dict.getAllEntries(MethodContextFeature.class);
			defs = dict.getAllEntries(DefinitionFeature.class);
			accs = dict.getAllEntries(MemberAccessFeature.class);
			params = dict.getAllEntries(CallParameterFeature.class);
		}

		private void count() {
			numPatterns = patterns.size();
			numCctx = cctxs.size();
			numMctx = mctxs.size();
			numDefs = defs.size();
			numAccesses = accs.size();
			numParams = params.size();
		}

		private void fillPatterns() {}

		private void fillCCtxs() {
			model.classContexts = new ITypeName[numCctx];
			model.classContextProbabilities = new double[numPatterns * numCctx];

			int idx = 0;
			for (ClassContextFeature f : cctxs) {
				model.classContexts[idx] = f.type;
				int id = dict.getId(f);

				idx++;
			}
		}

		private void fillMCtxs() {
			// TODO Auto-generated method stub

		}

		private void fillDefs() {
			// TODO Auto-generated method stub

		}

		private void fillMembers() {
			// TODO Auto-generated method stub

		}

		private void fillParams() {
			// TODO Auto-generated method stub

		}
	}
}