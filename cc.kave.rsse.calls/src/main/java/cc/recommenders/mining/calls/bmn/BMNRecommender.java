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
package cc.recommenders.mining.calls.bmn;

import static cc.kave.commons.assertions.Asserts.assertEquals;
import static cc.recommenders.mining.calls.bmn.QueryState.FALSE;
import static cc.recommenders.mining.calls.bmn.QueryState.TRUE;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cc.recommenders.datastructures.Dictionary;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.mining.calls.AbstractCallsRecommender;
import cc.recommenders.mining.calls.ProposalHelper;
import cc.recommenders.mining.calls.QueryOptions;
import cc.recommenders.mining.features.FeatureExtractor;
import cc.recommenders.names.ICoReMethodName;
import cc.recommenders.usages.Query;
import cc.recommenders.usages.Usage;
import cc.recommenders.usages.features.CallFeature;
import cc.recommenders.usages.features.ClassFeature;
import cc.recommenders.usages.features.DefinitionFeature;
import cc.recommenders.usages.features.FirstMethodFeature;
import cc.recommenders.usages.features.ParameterFeature;
import cc.recommenders.usages.features.TypeFeature;
import cc.recommenders.usages.features.UsageFeature;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class BMNRecommender extends AbstractCallsRecommender<Query> {

	private FeatureExtractor<Usage, UsageFeature> featureExtractor;
	private Dictionary<UsageFeature> dictionary;
	private Table table;
	private QueryOptions qOpts;

	public BMNRecommender(FeatureExtractor<Usage, UsageFeature> featureExtractor, BMNModel model, QueryOptions qOpts) {
		this.featureExtractor = featureExtractor;
		this.qOpts = qOpts;
		this.table = model.table;
		this.dictionary = model.dictionary;
	}

	@Override
	public Set<Tuple<ICoReMethodName, Double>> query(Query query) {
		Set<Tuple<ICoReMethodName, Double>> res = ProposalHelper.createSortedSet();

		List<UsageFeature> fs = featureExtractor.extract(query);
		QueryState[] states = convert(fs);

		Set<Tuple<Integer, Double>> proposals = query(states);
		for (Tuple<Integer, Double> proposal : proposals) {
			int idx = proposal.getFirst();
			CallFeature feature = (CallFeature) dictionary.getEntry(idx);
			ICoReMethodName methodName = feature.getMethodName();
			double probability = proposal.getSecond();
			if (probability > qOpts.minProbability) {
				Tuple<ICoReMethodName, Double> tuple = Tuple.newTuple(methodName, probability);
				res.add(tuple);
			}
		}

		return res;
	}

	private QueryState[] convert(List<UsageFeature> fs) {
		QueryState[] qss = new QueryState[dictionary.size()];

		for (int i = 0; i < dictionary.size(); i++) {
			UsageFeature f = dictionary.getEntry(i);
			final boolean isContained = fs.contains(f);
			qss[i] = getState(f, isContained);
		}

		return qss;
	}

	private QueryState getState(UsageFeature f, boolean isContained) {
		if (f instanceof TypeFeature) {
			return isContained ? TRUE : FALSE;
		}
		if (f instanceof CallFeature) {
			return isContained ? TRUE : QueryState.CREATE_PROPOSAL;
		}
		if (f instanceof ClassFeature) {
			if (qOpts.useClassContext) {
				return isContained ? TRUE : FALSE;
			}
		}
		if (f instanceof FirstMethodFeature) {
			if (qOpts.useMethodContext) {
				return isContained ? TRUE : FALSE;
			}
		}
		if (f instanceof DefinitionFeature) {
			if (qOpts.useDefinition) {
				return isContained ? TRUE : FALSE;
			}
		}
		if (f instanceof ParameterFeature) {
			if (qOpts.useParameterSites) {
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