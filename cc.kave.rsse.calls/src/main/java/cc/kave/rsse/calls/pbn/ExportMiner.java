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
package cc.kave.rsse.calls.pbn;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.utils.io.Directory;
import cc.kave.rsse.calls.datastructures.Dictionary;
import cc.kave.rsse.calls.extraction.features.FeatureExtractor;
import cc.kave.rsse.calls.extraction.features.OptionAwareFeaturePredicate;
import cc.kave.rsse.calls.extraction.features.RareFeatureDropper;
import cc.kave.rsse.calls.mining.DictionaryBuilder;
import cc.kave.rsse.calls.mining.ModelBuilder;
import cc.kave.rsse.calls.options.MiningOptions;
import cc.kave.rsse.calls.options.QueryOptions;
import cc.kave.rsse.calls.pbn.clustering.PatternFinderFactory;
import cc.kave.rsse.calls.pbn.model.BayesianNetwork;
import cc.kave.rsse.calls.usages.Usage;
import cc.kave.rsse.calls.usages.features.UsageFeature;

public class ExportMiner extends PBNMiner {

	private static boolean GENERATE_FREQUENCIES = true;

	private Directory exportDir;

	private MiningOptions mOpts;

	@Inject
	public ExportMiner(FeatureExtractor<Usage, UsageFeature> featureExtractor,
			DictionaryBuilder<Usage, UsageFeature> dictionaryBuilder,
			PatternFinderFactory<UsageFeature> patternFinderFactory,
			ModelBuilder<UsageFeature, BayesianNetwork> modelBuilder, QueryOptions queryOptions, MiningOptions mOpts,
			RareFeatureDropper<UsageFeature> dropper, @Named("export") Directory exportDir,
			OptionAwareFeaturePredicate featurePred) {
		super(featureExtractor, dictionaryBuilder, patternFinderFactory, modelBuilder, queryOptions, mOpts, dropper,
				featurePred);
		this.mOpts = mOpts;
		this.exportDir = exportDir.createDirectory("BMN" + (GENERATE_FREQUENCIES ? "+F" : "-F"));
	}

	@Override
	public BayesianNetwork learnModel(List<Usage> usages) {
		throw new RuntimeException("not implemented");
	}

	public void export(ITypeName type, List<Usage> usages) throws IOException {

		List<List<UsageFeature>> features = extractFeatures(usages);
		Dictionary<UsageFeature> dictionary = createDictionary(usages, features);

		Map<Set<UsageFeature>, Integer> counts = count(features);

		String out = createOutput(counts, dictionary);

		String fileName = type.toString().replaceAll("[^a-zA-Z0-9.]", "_") + ".txt";
		getDir().writeContent(out, fileName);
	}

	private Directory getDir() {
		return exportDir.createDirectory(mOpts.isFeatureDropping() ? "+DROP" : "-DROP");
	}

	private static Map<Set<UsageFeature>, Integer> count(List<List<UsageFeature>> features) {
		Map<Set<UsageFeature>, Integer> counts = Maps.newLinkedHashMap();

		for (List<UsageFeature> usage : features) {
			Set<UsageFeature> su = Sets.newLinkedHashSet();
			su.addAll(usage);

			int newVal;
			if (counts.containsKey(su)) {
				newVal = counts.get(su) + 1;
			} else {
				newVal = 1;
			}

			counts.put(su, newVal);
		}
		return counts;
	}

	private static String createOutput(Map<Set<UsageFeature>, Integer> counts, Dictionary<UsageFeature> dictionary) {
		StringBuilder sb = new StringBuilder();

		for (UsageFeature f : dictionary.getAllEntries()) {
			sb.append(f);
			sb.append('\t');
		}
		if (GENERATE_FREQUENCIES) {
			sb.append("frequency");
		}
		sb.append('\n');

		for (Set<UsageFeature> usage : counts.keySet()) {
			StringBuilder row = new StringBuilder();
			int count = counts.get(usage);

			for (UsageFeature f : dictionary.getAllEntries()) {
				int num = usage.contains(f) ? 1 : 0;
				row.append(num);
				row.append('\t');
			}
			if (GENERATE_FREQUENCIES) {
				row.append(count);
			}
			row.append('\n');

			if (GENERATE_FREQUENCIES) {
				sb.append(row.toString());
			} else {
				String rowString = row.toString();
				for (int i = 0; i < count; i++) {
					sb.append(rowString);
				}
			}
		}

		return sb.toString();
	}
}