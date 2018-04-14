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

import java.util.List;

import cc.kave.commons.exceptions.AssertionException;
import cc.kave.rsse.calls.model.Dictionary;
import cc.kave.rsse.calls.model.features.ClassContextFeature;
import cc.kave.rsse.calls.model.features.DefinitionFeature;
import cc.kave.rsse.calls.model.features.IFeature;
import cc.kave.rsse.calls.model.features.MethodContextFeature;
import cc.kave.rsse.calls.model.features.TypeFeature;
import cc.kave.rsse.calls.model.features.UsageSiteFeature;

public class DictionaryBuilder {

	private Options opts;

	public DictionaryBuilder(Options opts) {
		this.opts = opts;
	}

	public Dictionary<IFeature> newDictionary(List<List<IFeature>> llf) {
		Dictionary<IFeature> dictionary = new Dictionary<IFeature>();

		for (List<IFeature> lf : llf) {
			for (IFeature f : lf) {
				if (isUsedInQuery(f)) {
					dictionary.add(f);
				}
			}
		}

		return dictionary;
	}

	public boolean isUsedInQuery(IFeature f) {
		if (f instanceof TypeFeature) {
			return true;
		}
		if (f instanceof ClassContextFeature) {
			return opts.useClassCtx();
		}
		if (f instanceof MethodContextFeature) {
			return opts.useMethodCtx();
		}
		if (f instanceof DefinitionFeature) {
			return opts.useDef();
		}
		if (f instanceof UsageSiteFeature) {
			UsageSiteFeature usf = (UsageSiteFeature) f;
			switch (usf.site.getType()) {
			case CALL_RECEIVER:
				return opts.useCalls();
			case CALL_PARAMETER:
				return opts.useParams();
			case FIELD_ACCESS:
				return opts.useMembers();
			case PROPERTY_ACCESS:
				return opts.useMembers();
			}
		}
		throw new AssertionException(String.format("unknown feature class: %s", f.getClass()));
	}
}