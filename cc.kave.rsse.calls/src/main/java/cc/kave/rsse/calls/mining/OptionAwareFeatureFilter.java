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
package cc.kave.rsse.calls.mining;

import com.google.common.base.Predicate;
import com.google.inject.Inject;

import cc.kave.commons.exceptions.AssertionException;
import cc.kave.rsse.calls.model.features.ClassContextFeature;
import cc.kave.rsse.calls.model.features.DefinitionFeature;
import cc.kave.rsse.calls.model.features.IFeature;
import cc.kave.rsse.calls.model.features.MethodContextFeature;
import cc.kave.rsse.calls.model.features.TypeFeature;
import cc.kave.rsse.calls.model.features.UsageSiteFeature;

public class OptionAwareFeatureFilter implements Predicate<IFeature> {

	private QueryOptions qOpts;

	@Inject
	public OptionAwareFeatureFilter(QueryOptions qOpts) {
		this.qOpts = qOpts;
	}

	@Override
	public boolean apply(IFeature f) {
		if (f instanceof TypeFeature) {
			return true;
		}
		if (f instanceof ClassContextFeature) {
			return qOpts.useClassContext;
		}
		if (f instanceof MethodContextFeature) {
			return qOpts.useMethodContext;
		}
		if (f instanceof DefinitionFeature) {
			return qOpts.useDefinition;
		}
		if (f instanceof UsageSiteFeature) {
			return true;
		}
		throw new AssertionException(String.format("unknown feature class: %s", f.getClass()));
	}
}
