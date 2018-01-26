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
package cc.recommenders.mining.features;

import cc.kave.commons.exceptions.AssertionException;
import cc.recommenders.mining.calls.QueryOptions;
import cc.recommenders.usages.features.CallFeature;
import cc.recommenders.usages.features.ClassFeature;
import cc.recommenders.usages.features.DefinitionFeature;
import cc.recommenders.usages.features.FirstMethodFeature;
import cc.recommenders.usages.features.ParameterFeature;
import cc.recommenders.usages.features.SuperMethodFeature;
import cc.recommenders.usages.features.TypeFeature;
import cc.recommenders.usages.features.UsageFeature;

import com.google.common.base.Predicate;
import com.google.inject.Inject;

public class OptionAwareFeaturePredicate implements Predicate<UsageFeature> {

	private QueryOptions qOpts;

	@Inject
	public OptionAwareFeaturePredicate(QueryOptions qOpts) {
		this.qOpts = qOpts;
	}

	@Override
	public boolean apply(UsageFeature f) {
		if (f instanceof TypeFeature) {
			return true;
		}
		if (f instanceof CallFeature) {
			return true;
		}
		if (f instanceof ClassFeature) {
			return qOpts.useClassContext;
		}
		if (f instanceof FirstMethodFeature) {
			return qOpts.useMethodContext;
		}
		if (f instanceof ParameterFeature) {
			return qOpts.useParameterSites;
		}
		if (f instanceof DefinitionFeature) {
			return qOpts.useDefinition;
		}
		if (f instanceof SuperMethodFeature) {
			return false;
		}
		throw new AssertionException(String.format("unknown feature class: %s", f.getClass()));
	}
}
