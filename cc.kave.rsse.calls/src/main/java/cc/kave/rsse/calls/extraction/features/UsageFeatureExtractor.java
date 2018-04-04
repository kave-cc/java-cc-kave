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
package cc.kave.rsse.calls.extraction.features;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.rsse.calls.options.MiningOptions;
import cc.kave.rsse.calls.usages.UsageAccess;
import cc.kave.rsse.calls.usages.UsageAccessType;
import cc.kave.rsse.calls.usages.DefinitionSiteKind;
import cc.kave.rsse.calls.usages.IUsage;
import cc.kave.rsse.calls.usages.features.CallFeature;
import cc.kave.rsse.calls.usages.features.ClassFeature;
import cc.kave.rsse.calls.usages.features.DefinitionFeature;
import cc.kave.rsse.calls.usages.features.FirstMethodFeature;
import cc.kave.rsse.calls.usages.features.ParameterFeature;
import cc.kave.rsse.calls.usages.features.TypeFeature;
import cc.kave.rsse.calls.usages.features.UsageFeature;

public class UsageFeatureExtractor implements FeatureExtractor<IUsage, UsageFeature> {

	private MiningOptions opts;

	@Inject
	public UsageFeatureExtractor(MiningOptions opts) {
		this.opts = opts;
	}

	@Override
	public List<List<UsageFeature>> extract(List<IUsage> usages) {
		List<List<UsageFeature>> features = newArrayList();
		for (IUsage usage : usages) {
			features.add(extract(usage));
		}
		return features;
	}

	@Override
	public List<UsageFeature> extract(IUsage usage) {

		List<UsageFeature> features = Lists.newArrayList();

		features.add(new TypeFeature(usage.getType()));
		features.add(new ClassFeature(usage.getClassContext()));
		features.add(new FirstMethodFeature(usage.getMethodContext()));
		features.add(new DefinitionFeature(usage.getDefinitionSite()));

		if (shouldDefMethodBeAddedAsCall(usage)) {
			features.add(new CallFeature(usage.getDefinitionSite().getMethod()));
		}

		for (UsageAccess site : usage.getAllAccesses()) {
			features.add(getSiteFeature(site));
		}

		return features;
	}

	private boolean shouldDefMethodBeAddedAsCall(IUsage usage) {
		boolean isNew = DefinitionSiteKind.NEW.equals(usage.getDefinitionSite().getKind());
		boolean useInitAsCall = opts.isInitUsedAsCall();
		return isNew && useInitAsCall;
	}

	private static UsageFeature getSiteFeature(UsageAccess site) {
		if (site.getKind() == UsageAccessType.CALL_PARAMETER) {
			IMethodName param = site.getMethod();
			int argNum = site.getArgIndex();
			return new ParameterFeature(param, argNum);
		} else {
			IMethodName call = site.getMethod();
			return new CallFeature(call);
		}
	}
}