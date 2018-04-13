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

import org.junit.Assert;

import cc.kave.rsse.calls.model.Dictionary;
import cc.kave.rsse.calls.model.features.IFeature;

public class DictionaryBuilder {


	public DictionaryBuilder(MiningOptions mOpts, QueryOptions qOpts) {
	}

	public Dictionary<IFeature> newDictionary(List<List<IFeature>> llf) {
		Assert.fail("+ drop rare");

		Dictionary<IFeature> dictionary = new Dictionary<IFeature>();

		for (List<IFeature> lf : llf) {
			for (IFeature f : lf) {
				if (filter.apply(f)) {
					dictionary.add(f);
				}
			}
		}

		return dictionary;
	}
}