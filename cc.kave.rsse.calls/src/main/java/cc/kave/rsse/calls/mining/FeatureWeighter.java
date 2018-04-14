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
package cc.kave.rsse.calls.mining;

import static java.lang.Math.max;

import cc.kave.rsse.calls.model.features.ClassContextFeature;
import cc.kave.rsse.calls.model.features.DefinitionFeature;
import cc.kave.rsse.calls.model.features.FeatureVisitor;
import cc.kave.rsse.calls.model.features.IFeature;
import cc.kave.rsse.calls.model.features.MethodContextFeature;
import cc.kave.rsse.calls.model.features.UsageSiteFeature;

public class FeatureWeighter {

	private final Options options;

	public FeatureWeighter(Options options) {
		this.options = options;
	}

	public double getWeight(IFeature f) {
		final double[] w = new double[] { 1.0 };
		f.accept(new FeatureVisitor() {
			@Override
			public void visit(UsageSiteFeature f) {
				w[0] = 1.0;
			}

			@Override
			public void visit(ClassContextFeature f) {
				w[0] = options.weightClassCtx;
			}

			@Override
			public void visit(DefinitionFeature f) {
				w[0] = options.weightDef;
			}

			public void visit(MethodContextFeature f) {
				w[0] = options.weightMethodCtx;
			}
		});

		return max(w[0], 0.00001);
	}

	public double getUnweighted(IFeature f, final double value) {
		double weight = getWeight(f);
		return value / weight;
	}
}