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

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import cc.recommenders.mining.calls.MiningOptions;
import cc.recommenders.names.ICoReMethodName;
import cc.recommenders.usages.CallSite;
import cc.recommenders.usages.CallSiteKind;
import cc.recommenders.usages.DefinitionSiteKind;
import cc.recommenders.usages.Usage;
import cc.recommenders.usages.features.CallFeature;
import cc.recommenders.usages.features.ClassFeature;
import cc.recommenders.usages.features.DefinitionFeature;
import cc.recommenders.usages.features.FirstMethodFeature;
import cc.recommenders.usages.features.ParameterFeature;
import cc.recommenders.usages.features.TypeFeature;
import cc.recommenders.usages.features.UsageFeature;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

public class UsageFeatureExtractor implements FeatureExtractor<Usage, UsageFeature> {

	private MiningOptions opts;

	@Inject
	public UsageFeatureExtractor(MiningOptions opts) {
		this.opts = opts;
	}
	
	@Override
	public List<List<UsageFeature>> extract(List<Usage> usages) {
		List<List<UsageFeature>> features = newArrayList();
		for (Usage usage : usages) {
			features.add(extract(usage));
		}
		return features;
	}

	@Override
	public List<UsageFeature> extract(Usage usage) {

		List<UsageFeature> features = Lists.newArrayList();

		features.add(new TypeFeature(usage.getType()));
		features.add(new ClassFeature(usage.getClassContext()));
		features.add(new FirstMethodFeature(usage.getMethodContext()));
		features.add(new DefinitionFeature(usage.getDefinitionSite()));

		if(shouldDefMethodBeAddedAsCall(usage)) {
			features.add(new CallFeature(usage.getDefinitionSite().getMethod()));
		}
		
		for (CallSite site : usage.getAllCallsites()) {
			features.add(getSiteFeature(site));
		}

		return features;
	}

	private boolean shouldDefMethodBeAddedAsCall(Usage usage) {
		boolean isNew = DefinitionSiteKind.NEW.equals(usage.getDefinitionSite().getKind());
		boolean useInitAsCall = opts.isInitUsedAsCall(); 
		return isNew && useInitAsCall;
	}

	private static UsageFeature getSiteFeature(CallSite site) {
		if (site.getKind() == CallSiteKind.PARAMETER) {
			ICoReMethodName param = site.getMethod();
			int argNum = site.getArgIndex();
			return new ParameterFeature(param, argNum);
		} else {
			ICoReMethodName call = site.getMethod();
			return new CallFeature(call);
		}
	}
}