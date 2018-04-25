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
import static cc.kave.rsse.calls.model.usages.UsageSiteType.CALL_PARAMETER;
import static cc.kave.rsse.calls.model.usages.UsageSiteType.CALL_RECEIVER;
import static cc.kave.rsse.calls.model.usages.UsageSiteType.MEMBER_ACCESS;
import static cc.kave.rsse.calls.recs.bmn.QueryState.IGNORE;
import static cc.kave.rsse.calls.recs.bmn.QueryState.SET;
import static cc.kave.rsse.calls.recs.bmn.QueryState.TO_PROPOSE;
import static cc.kave.rsse.calls.recs.bmn.QueryState.UNSET;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.rsse.calls.AbstractCallsRecommender;
import cc.kave.rsse.calls.IModelStore;
import cc.kave.rsse.calls.UsageExtractor;
import cc.kave.rsse.calls.mining.FeatureExtractor;
import cc.kave.rsse.calls.mining.Options;
import cc.kave.rsse.calls.model.features.ClassContextFeature;
import cc.kave.rsse.calls.model.features.DefinitionFeature;
import cc.kave.rsse.calls.model.features.IFeature;
import cc.kave.rsse.calls.model.features.MethodContextFeature;
import cc.kave.rsse.calls.model.features.UsageSiteFeature;
import cc.kave.rsse.calls.model.usages.IUsage;
import cc.kave.rsse.calls.model.usages.UsageSiteType;
import cc.kave.rsse.calls.utils.ProposalHelper;

public class BMNRecommender extends AbstractCallsRecommender<IUsage> {

	private final FeatureExtractor featureExtractor;
	private final IModelStore<BMNModel> modelStore;
	private final Options opts;

	private BMNModel model = null;

	public BMNRecommender(FeatureExtractor featureExtractor, IModelStore<BMNModel> modelStore, Options opts) {
		this.featureExtractor = featureExtractor;
		this.modelStore = modelStore;
		this.opts = opts;
	}

	@Override
	public int getLastModelSize() {
		if (model == null) {
			return -1;
		}
		return model.table.getSize();
	}

	@Override
	public Set<Pair<IMemberName, Double>> query(Context ctx) {
		UsageExtractor ue = new UsageExtractor(ctx);
		if (ue.hasQuery()) {
			return query(ue.getQuery());
		}
		return new HashSet<>();
	}

	@Override
	public Set<Pair<IMemberName, Double>> query(IUsage u) {
		if (!modelStore.hasModel(u.getType())) {
			return new HashSet<>();
		}
		model = modelStore.getModel(u.getType());

		List<IFeature> fs = featureExtractor.extract(u);
		QueryState[] queryRow = toQueryRow(fs);

		Set<Pair<IMemberName, Double>> res = ProposalHelper.createSortedSet();

		Map<IMemberName, Double> proposals = query(queryRow);
		for (IMemberName m : proposals.keySet()) {
			double probability = proposals.get(m);
			if (probability > opts.minProbability) {
				Pair<IMemberName, Double> tuple = Pair.of(m, probability);
				res.add(tuple);
			}
		}

		return res;
	}

	private QueryState[] toQueryRow(List<IFeature> fs) {
		QueryState[] qss = new QueryState[model.dictionary.size()];
		for (int i = 0; i < model.dictionary.size(); i++) {
			IFeature f = model.dictionary.getEntry(i);
			final boolean isFeaturePartOfQuery = fs.contains(f);
			qss[i] = getQueryState(f, isFeaturePartOfQuery, opts);
		}

		return qss;
	}

	private Map<IMemberName, Double> query(QueryState[] queryRow) {
		Map<IMemberName, Double> res = new HashMap<>();

		List<Integer> nearestRows = findIndicesOfNearestNeighborRows(queryRow);
		int[] frequencies = model.table.getFrequencies();

		// count the total number
		int totalNum = 0;
		for (int rowIdx : nearestRows) {
			totalNum += frequencies[rowIdx];
		}

		boolean[] markers = markColumnsForProposals(queryRow);
		for (int colIdx = 0; colIdx < markers.length; colIdx++) {

			// skip unmarked columns
			if (!markers[colIdx]) {
				continue;
			}

			// count column frequency
			int colCount = 0;
			for (int rowIdx : nearestRows) {
				boolean[] row = model.table.getBMNTable()[rowIdx];
				if (row[colIdx]) {
					colCount += frequencies[rowIdx];
				}
			}
			double probablity = colCount / (double) totalNum;

			UsageSiteFeature feature = (UsageSiteFeature) model.dictionary.getEntry(colIdx);
			IMemberName m = feature.site.getMember();

			res.put(m, probablity);
		}
		return res;
	}

	private List<Integer> findIndicesOfNearestNeighborRows(QueryState[] query) {
		int minDistance = Integer.MAX_VALUE;
		List<Integer> nearestNeighbors = Lists.newLinkedList();

		boolean[][] rows = model.table.getBMNTable();
		for (int rowIdx = 0; rowIdx < rows.length; rowIdx++) {
			boolean[] row = rows[rowIdx];

			int dist = calculateDistance(query, row);
			if (dist < minDistance) {
				minDistance = dist;
				nearestNeighbors.clear();
				nearestNeighbors.add(rowIdx);
			} else if (dist == minDistance) {
				nearestNeighbors.add(rowIdx);
			}
		}
		return nearestNeighbors;
	}

	// ###################################################################################################

	protected static boolean[] markColumnsForProposals(QueryState[] queryRow) {
		boolean[] res = new boolean[queryRow.length];
		for (int i = 0; i < queryRow.length; i++) {
			res[i] = queryRow[i] == TO_PROPOSE;
		}
		return res;
	}

	protected static QueryState getQueryState(IFeature f, boolean isFeaturePartOfQuery, Options opts) {
		if (f instanceof ClassContextFeature) {
			if (opts.useClassCtx()) {
				return isFeaturePartOfQuery ? SET : UNSET;
			}
		}
		if (f instanceof MethodContextFeature) {
			if (opts.useMethodCtx()) {
				return isFeaturePartOfQuery ? SET : UNSET;
			}
		}
		if (f instanceof DefinitionFeature) {
			if (opts.useDef()) {
				return isFeaturePartOfQuery ? SET : UNSET;
			}
		}
		if (f instanceof UsageSiteFeature) {
			UsageSiteType ust = ((UsageSiteFeature) f).site.getType();
			if (CALL_RECEIVER == ust && opts.useCalls()) {
				return isFeaturePartOfQuery ? SET : TO_PROPOSE;
			}
			if (CALL_PARAMETER == ust && opts.useParams()) {
				return isFeaturePartOfQuery ? SET : UNSET;
			}
			if (MEMBER_ACCESS == ust && opts.useMembers()) {
				return isFeaturePartOfQuery ? SET : TO_PROPOSE;
			}
		}
		return IGNORE;
	}

	protected static int calculateDistance(QueryState[] query, boolean[] row) {
		assertEquals(query.length, row.length);
		int distance = 0;

		for (int i = 0; i < query.length; i++) {
			boolean isFalsePositive = query[i] == UNSET && row[i];
			boolean isFalseNegative = query[i] == SET && !row[i];
			if (isFalseNegative || isFalsePositive) {
				distance++;
			}
		}

		return distance;
	}
}