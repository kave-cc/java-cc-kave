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

import static cc.kave.rsse.calls.model.usages.impl.UsageSites.call;
import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import com.google.common.collect.Lists;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.rsse.calls.model.features.ClassContextFeature;
import cc.kave.rsse.calls.model.features.DefinitionFeature;
import cc.kave.rsse.calls.model.features.IFeature;
import cc.kave.rsse.calls.model.features.MethodContextFeature;
import cc.kave.rsse.calls.model.features.TypeFeature;
import cc.kave.rsse.calls.model.features.UsageSiteFeature;
import cc.kave.rsse.calls.model.usages.DefinitionType;
import cc.kave.rsse.calls.model.usages.IUsage;
import cc.kave.rsse.calls.model.usages.IUsageSite;

public class FeatureExtractor {

	private MiningOptions opts;

	public FeatureExtractor(MiningOptions opts) {
		this.opts = opts;
	}

	public List<List<IFeature>> extract(List<IUsage> usages) {
		List<List<IFeature>> features = newArrayList();
		for (IUsage usage : usages) {
			features.add(extract(usage));
		}
		return features;
	}

	public List<IFeature> extract(IUsage usage) {

		List<IFeature> features = Lists.newArrayList();

		features.add(new TypeFeature(usage.getType()));
		features.add(new ClassContextFeature(usage.getClassContext()));
		features.add(new MethodContextFeature(usage.getMethodContext()));
		features.add(new DefinitionFeature(usage.getDefinition()));

		if (shouldDefMethodBeAddedAsCall(usage)) {
			IMethodName init = usage.getDefinition().getMember(IMethodName.class);
			features.add(new UsageSiteFeature(call(init)));
		}

		for (IUsageSite site : usage.getUsageSites()) {
			features.add(new UsageSiteFeature(site));
		}

		return features;
	}

	private boolean shouldDefMethodBeAddedAsCall(IUsage usage) {
		boolean isNew = DefinitionType.NEW.equals(usage.getDefinition().getType());
		boolean useInitAsCall = opts.isInitUsedAsCall();
		return isNew && useInitAsCall;
	}
}