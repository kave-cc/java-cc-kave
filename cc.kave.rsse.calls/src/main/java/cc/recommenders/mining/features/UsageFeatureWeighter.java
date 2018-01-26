/**
 * Copyright (c) 2010, 2011 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Sebastian Proksch - initial API and implementation
 */
package cc.recommenders.mining.features;

import static java.lang.Math.max;
import cc.recommenders.mining.calls.MiningOptions;
import cc.recommenders.mining.calls.clustering.FeatureWeighter;
import cc.recommenders.usages.features.CallFeature;
import cc.recommenders.usages.features.ClassFeature;
import cc.recommenders.usages.features.DefinitionFeature;
import cc.recommenders.usages.features.FirstMethodFeature;
import cc.recommenders.usages.features.UsageFeature;
import cc.recommenders.usages.features.ParameterFeature;
import cc.recommenders.usages.features.UsageFeature.ObjectUsageFeatureVisitor;

import com.google.inject.Inject;

public class UsageFeatureWeighter implements FeatureWeighter<UsageFeature> {

	private final MiningOptions options;

	@Inject
	public UsageFeatureWeighter(MiningOptions options) {
		this.options = options;
	}

	@Override
	public double getWeight(UsageFeature f) {
		final double[] w = new double[] { 1.0 };
		f.accept(new ObjectUsageFeatureVisitor() {
			@Override
			public void visit(CallFeature f) {
				w[0] = 1.0;
			}

			@Override
			public void visit(ClassFeature f) {
				w[0] = options.getWeightClassContext();
			}

			@Override
			public void visit(DefinitionFeature f) {
				w[0] = options.getWeightDefinition();
			}

			public void visit(FirstMethodFeature f) {
				w[0] = options.getWeightMethodContext();
			}

			public void visit(ParameterFeature f) {
				w[0] = options.getWeightParameterSites();
			}
		});

		return max(w[0], 0.00001);
	}

	@Override
	public double getUnweighted(UsageFeature f, final double value) {
		double weight = getWeight(f);
		return value / weight;
	}
}