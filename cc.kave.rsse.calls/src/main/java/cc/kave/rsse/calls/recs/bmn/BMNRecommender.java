/**
 * Copyright (c) 2010, 2011 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Ervina Cergani - initial API and implementation
 *     Sebastian Proksch - initial API and implementation
 */
package cc.kave.rsse.calls.recs.bmn;

import static cc.kave.commons.assertions.Asserts.assertEquals;
import static cc.kave.rsse.calls.recs.bmn.QueryState.FALSE;
import static cc.kave.rsse.calls.recs.bmn.QueryState.TRUE;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.rsse.calls.AbstractCallsRecommender;
import cc.kave.rsse.calls.UsageExtractor;
import cc.kave.rsse.calls.mining.FeatureExtractor;
import cc.kave.rsse.calls.mining.QueryOptions;
import cc.kave.rsse.calls.model.Dictionary;
import cc.kave.rsse.calls.model.Tuple;
import cc.kave.rsse.calls.model.features.ClassContextFeature;
import cc.kave.rsse.calls.model.features.DefinitionFeature;
import cc.kave.rsse.calls.model.features.IFeature;
import cc.kave.rsse.calls.model.features.MethodContextFeature;
import cc.kave.rsse.calls.model.features.TypeFeature;
import cc.kave.rsse.calls.model.features.UsageSiteFeature;
import cc.kave.rsse.calls.model.usages.impl.Usage;
import cc.kave.rsse.calls.utils.ProposalHelper;

public class BMNRecommender extends AbstractCallsRecommender<Usage> {

	private FeatureExtractor featureExtractor;
	private Dictionary<IFeature> dictionary;
	private Table table;
	private QueryOptions qOpts;

	public BMNRecommender(FeatureExtractor featureExtractor, BMNModel model, QueryOptions qOpts) {
		this.featureExtractor = featureExtractor;
		this.qOpts = qOpts;
		this.table = model.table;
		this.dictionary = model.dictionary;
	}

	@Override
	public Set<Tuple<IMethodName, Double>> query(Context ctx) {
		UsageExtractor ue = new UsageExtractor(ctx);
		if (ue.hasCallQuery()) {
			return query((Usage) ue.getQuery());
		}
		return new HashSet<>();
	}

	@Override
	public Set<Tuple<IMethodName, Double>> query(Usage query) {
		Set<Tuple<IMethodName, Double>> res = ProposalHelper.createSortedSet();

		List<IFeature> fs = featureExtractor.extract(query);
		QueryState[] states = convert(fs);

		Set<Tuple<Integer, Double>> proposals = query(states);
		for (Tuple<Integer, Double> proposal : proposals) {
			int idx = proposal.getFirst();
			UsageSiteFeature feature = (UsageSiteFeature) dictionary.getEntry(idx);
			IMethodName methodName = feature.getMethodName();
			double probability = proposal.getSecond();
			if (probability > qOpts.minProbability) {
				Tuple<IMethodName, Double> tuple = Tuple.newTuple(methodName, probability);
				res.add(tuple);
			}
		}

		return res;
	}

	private QueryState[] convert(List<IFeature> fs) {
		QueryState[] qss = new QueryState[dictionary.size()];

		for (int i = 0; i < dictionary.size(); i++) {
			IFeature f = dictionary.getEntry(i);
			final boolean isContained = fs.contains(f);
			qss[i] = getState(f, isContained);
		}

		return qss;
	}

	private QueryState getState(IFeature f, boolean isContained) {
		if (f instanceof TypeFeature) {
			return isContained ? TRUE : FALSE;
		}
		if (f instanceof UsageSiteFeature) {
			return isContained ? TRUE : QueryState.CREATE_PROPOSAL;
		}
		if (f instanceof ClassContextFeature) {
			if (qOpts.useClassContext) {
				return isContained ? TRUE : FALSE;
			}
		}
		if (f instanceof MethodContextFeature) {
			if (qOpts.useMethodContext) {
				return isContained ? TRUE : FALSE;
			}
		}
		if (f instanceof DefinitionFeature) {
			if (qOpts.useDefinition) {
				return isContained ? TRUE : FALSE;
			}
		}
		return QueryState.IGNORE_IN_DISTANCE_CALCULATION;
	}

	@Override
	public int getSize() {
		return table.getSize();
	}

	private Set<Tuple<Integer, Double>> query(QueryState[] query) {
		Set<Tuple<Integer, Double>> res = ProposalHelper.createSortedSet();

		int totalNum = 0;
		List<Integer> nns = findNearestNeighbors(query);
		Map<Integer, Integer> colCounts = findUnkownFeatures(query);
		int[] frequencies = table.getRowFrequencies();

		for (int nn : nns) {
			totalNum += frequencies[nn];
			boolean[] row = table.getBMNTable()[nn];
			for (int col : colCounts.keySet()) {
				if (row[col]) {
					int newVal = colCounts.get(col) + frequencies[nn];
					colCounts.put(col, newVal);
				}
			}
		}

		for (int col : colCounts.keySet()) {
			double probablity = colCounts.get(col) / (double) totalNum;
			Tuple<Integer, Double> tuple = Tuple.newTuple(col, probablity);
			res.add(tuple);
		}

		return res;
	}

	private Map<Integer, Integer> findUnkownFeatures(QueryState[] query) {
		Map<Integer, Integer> colCounts = Maps.newHashMap();

		for (int i = 0; i < query.length; i++) {
			if (query[i] == QueryState.CREATE_PROPOSAL) {
				colCounts.put(i, 0);
			}
		}

		return colCounts;
	}

	private List<Integer> findNearestNeighbors(QueryState[] query) {
		int minDistance = Integer.MAX_VALUE;
		List<Integer> nearestNeighbors = Lists.newLinkedList();

		for (int i = 0; i < table.getBMNTable().length; i++) {
			boolean[] row = table.getBMNTable()[i];
			int dist = calculateDistance(query, row);
			if (dist < minDistance) {
				nearestNeighbors.clear();
				nearestNeighbors.add(i);
				minDistance = dist;
			} else if (dist == minDistance) {
				nearestNeighbors.add(i);
			}
		}
		return nearestNeighbors;
	}

	public static int calculateDistance(QueryState[] query, boolean[] row) {
		assertEquals(query.length, row.length);
		int distance = 0;

		for (int i = 0; i < query.length; i++) {
			boolean isFalsePositive = query[i] == FALSE && row[i];
			boolean isFalseNegative = query[i] == TRUE && !row[i];
			if (isFalseNegative || isFalsePositive) {
				distance++;
			}
		}

		return distance;
	}
}