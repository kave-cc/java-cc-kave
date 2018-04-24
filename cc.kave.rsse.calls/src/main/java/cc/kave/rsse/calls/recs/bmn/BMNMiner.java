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
package cc.kave.rsse.calls.recs.bmn;

import java.util.List;

import cc.kave.rsse.calls.mining.DictionaryBuilder;
import cc.kave.rsse.calls.mining.FeatureExtractor;
import cc.kave.rsse.calls.mining.VectorBuilder;
import cc.kave.rsse.calls.model.features.IFeature;
import cc.kave.rsse.calls.model.usages.IUsage;

public class BMNMiner {

	private final DictionaryBuilder dictBuilder;
	private final FeatureExtractor extractor;
	private final VectorBuilder vb;

	public BMNMiner(FeatureExtractor extractor, DictionaryBuilder dictBuilder, VectorBuilder vb) {
		this.dictBuilder = dictBuilder;
		this.extractor = extractor;
		this.vb = vb;
	}

	public BMNModel learnModel(List<IUsage> in) {
		List<List<IFeature>> fs = extractor.extract(in);

		BMNModel bmnModel = new BMNModel();
		bmnModel.dictionary = dictBuilder.build(fs);
		bmnModel.table = new Table(bmnModel.dictionary.size());

		for (boolean[] arr : vb.toBoolArrays(fs, bmnModel.dictionary)) {
			bmnModel.table.add(arr);
		}

		return bmnModel;
	}
}