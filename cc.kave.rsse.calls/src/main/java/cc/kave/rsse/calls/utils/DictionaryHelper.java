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
package cc.kave.rsse.calls.utils;

import static cc.kave.rsse.calls.recs.pbn.PBNModelConstants.DUMMY_DEFINITION;
import static cc.kave.rsse.calls.recs.pbn.PBNModelConstants.DUMMY_METHOD;
import static cc.kave.rsse.calls.recs.pbn.PBNModelConstants.DUMMY_TYPE;
import static cc.kave.rsse.calls.recs.pbn.PBNModelConstants.UNKNOWN_DEFINITION;
import static cc.kave.rsse.calls.recs.pbn.PBNModelConstants.UNKNOWN_METHOD;
import static cc.kave.rsse.calls.recs.pbn.PBNModelConstants.UNKNOWN_TYPE;
import static com.google.common.collect.Sets.newLinkedHashSet;

import java.util.Set;

import com.google.common.collect.Sets;

import cc.kave.commons.assertions.Asserts;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.rsse.calls.model.Dictionary;
import cc.kave.rsse.calls.model.IMatcher;
import cc.kave.rsse.calls.model.features.ClassContextFeature;
import cc.kave.rsse.calls.model.features.DefinitionFeature;
import cc.kave.rsse.calls.model.features.IFeature;
import cc.kave.rsse.calls.model.features.MethodContextFeature;
import cc.kave.rsse.calls.model.features.TypeFeature;
import cc.kave.rsse.calls.model.features.UsageSiteFeature;

public class DictionaryHelper {

	public static final DefinitionFeature UNKNOWN_IN_DEFINITION = new DefinitionFeature(UNKNOWN_DEFINITION);
	public static final MethodContextFeature UNKNOWN_IN_METHOD = new MethodContextFeature(UNKNOWN_METHOD);
	public static final ClassContextFeature UNKNOWN_IN_CLASS = new ClassContextFeature(UNKNOWN_TYPE);

	private final Dictionary<IFeature> dictionary;

	public DictionaryHelper(Dictionary<IFeature> dictionary) {
		this.dictionary = dictionary;
	}

	public void addDummyStatesToEnsureAtLeastTwoStatesPerNode() {
		// ensure two entries for types with very little input data
		dictionary.add(new ClassContextFeature(DUMMY_TYPE));
		dictionary.add(new MethodContextFeature(DUMMY_METHOD));
		dictionary.add(new DefinitionFeature(DUMMY_DEFINITION));
		// add to support feature dropping
		dictionary.add(UNKNOWN_IN_CLASS);
		dictionary.add(UNKNOWN_IN_METHOD);
		dictionary.add(UNKNOWN_IN_DEFINITION);
	}

	public Set<IFeature> getClassContexts() {
		IMatcher<IFeature> matcher = new IMatcher<IFeature>() {
			@Override
			public boolean matches(IFeature entry) {
				return entry instanceof ClassContextFeature;
			}
		};
		return dictionary.getAllMatchings(matcher);
	}

	public Set<IFeature> getMethodContexts() {
		IMatcher<IFeature> matcher = new IMatcher<IFeature>() {
			@Override
			public boolean matches(IFeature entry) {
				return entry instanceof MethodContextFeature;
			}
		};
		return dictionary.getAllMatchings(matcher);
	}

	public Set<IFeature> getDefinitions() {
		IMatcher<IFeature> matcher = new IMatcher<IFeature>() {
			@Override
			public boolean matches(IFeature entry) {
				return entry instanceof DefinitionFeature;
			}
		};
		return dictionary.getAllMatchings(matcher);
	}

	public Set<UsageSiteFeature> getUsageSites() {
		IMatcher<IFeature> matcher = new IMatcher<IFeature>() {
			@Override
			public boolean matches(IFeature entry) {
				return entry instanceof UsageSiteFeature;
			}
		};
		Set<UsageSiteFeature> calls = newLinkedHashSet();
		for (IFeature feature : dictionary.getAllMatchings(matcher)) {
			calls.add((UsageSiteFeature) feature);
		}
		return calls;
	}

	public ITypeName getType() {
		Asserts.fail("avoid multipel iterations... just iterate through values once (and also handle absence!)");
		IMatcher<IFeature> matcher = new IMatcher<IFeature>() {
			@Override
			public boolean matches(IFeature entry) {
				return entry instanceof TypeFeature;
			}
		};
		Set<IFeature> matches = dictionary.getAllMatchings(matcher);
		TypeFeature typeFeature = (TypeFeature) matches.iterator().next();
		return typeFeature.type;
	}

	public static <T> Set<String> diff(Dictionary<T> a, Dictionary<T> b) {
		Set<String> diff = Sets.newLinkedHashSet();
		for (T t : a.getAllEntries()) {
			if (!b.contains(t)) {
				diff.add("-" + t);
			}
		}
		for (T t : b.getAllEntries()) {
			if (!a.contains(t)) {
				diff.add("+" + t);
			}
		}
		return diff;
	}
}