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
package cc.kave.rsse.calls.pbn;

import static cc.kave.rsse.calls.pbn.PBNModelConstants.DUMMY_DEFINITION;
import static cc.kave.rsse.calls.pbn.PBNModelConstants.DUMMY_METHOD;
import static cc.kave.rsse.calls.pbn.PBNModelConstants.DUMMY_TYPE;
import static cc.kave.rsse.calls.pbn.PBNModelConstants.UNKNOWN_DEFINITION;
import static cc.kave.rsse.calls.pbn.PBNModelConstants.UNKNOWN_METHOD;
import static cc.kave.rsse.calls.pbn.PBNModelConstants.UNKNOWN_TYPE;
import static com.google.common.collect.Sets.newLinkedHashSet;

import java.util.Set;

import com.google.common.collect.Sets;

import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.rsse.calls.datastructures.Dictionary;
import cc.kave.rsse.calls.datastructures.IMatcher;
import cc.kave.rsse.calls.usages.features.CallFeature;
import cc.kave.rsse.calls.usages.features.ClassFeature;
import cc.kave.rsse.calls.usages.features.DefinitionFeature;
import cc.kave.rsse.calls.usages.features.FirstMethodFeature;
import cc.kave.rsse.calls.usages.features.ParameterFeature;
import cc.kave.rsse.calls.usages.features.TypeFeature;
import cc.kave.rsse.calls.usages.features.UsageFeature;

public class DictionaryHelper {

	public static final DefinitionFeature UNKNOWN_IN_DEFINITION = new DefinitionFeature(UNKNOWN_DEFINITION);
	public static final FirstMethodFeature UNKNOWN_IN_METHOD = new FirstMethodFeature(UNKNOWN_METHOD);
	public static final ClassFeature UNKNOWN_IN_CLASS = new ClassFeature(UNKNOWN_TYPE);

	private final Dictionary<UsageFeature> dictionary;

	public DictionaryHelper(Dictionary<UsageFeature> dictionary) {
		this.dictionary = dictionary;
	}

	public void addDummyStatesToEnsureAtLeastTwoStatesPerNode() {
		// ensure two entries for types with very little input data
		dictionary.add(new ClassFeature(DUMMY_TYPE));
		dictionary.add(new FirstMethodFeature(DUMMY_METHOD));
		dictionary.add(new DefinitionFeature(DUMMY_DEFINITION));
		// add to support feature dropping
		dictionary.add(UNKNOWN_IN_CLASS);
		dictionary.add(UNKNOWN_IN_METHOD);
		dictionary.add(UNKNOWN_IN_DEFINITION);
	}

	public Set<UsageFeature> getClassContexts() {
		IMatcher<UsageFeature> matcher = new IMatcher<UsageFeature>() {
			@Override
			public boolean matches(UsageFeature entry) {
				return entry instanceof ClassFeature;
			}
		};
		return dictionary.getAllMatchings(matcher);
	}

	public Set<UsageFeature> getMethodContexts() {
		IMatcher<UsageFeature> matcher = new IMatcher<UsageFeature>() {
			@Override
			public boolean matches(UsageFeature entry) {
				return entry instanceof FirstMethodFeature;
			}
		};
		return dictionary.getAllMatchings(matcher);
	}

	public Set<UsageFeature> getDefinitions() {
		IMatcher<UsageFeature> matcher = new IMatcher<UsageFeature>() {
			@Override
			public boolean matches(UsageFeature entry) {
				return entry instanceof DefinitionFeature;
			}
		};
		return dictionary.getAllMatchings(matcher);
	}

	public Set<CallFeature> getCallSites() {
		IMatcher<UsageFeature> matcher = new IMatcher<UsageFeature>() {
			@Override
			public boolean matches(UsageFeature entry) {
				return entry instanceof CallFeature;
			}
		};
		Set<CallFeature> calls = newLinkedHashSet();
		for (UsageFeature feature : dictionary.getAllMatchings(matcher)) {
			calls.add((CallFeature) feature);
		}
		return calls;
	}

	public Set<ParameterFeature> getParameterSites() {
		IMatcher<UsageFeature> matcher = new IMatcher<UsageFeature>() {
			@Override
			public boolean matches(UsageFeature entry) {
				return entry instanceof ParameterFeature;
			}
		};
		Set<ParameterFeature> params = newLinkedHashSet();
		for (UsageFeature feature : dictionary.getAllMatchings(matcher)) {
			params.add((ParameterFeature) feature);
		}
		return params;
	}

	public ITypeName getType() {
		IMatcher<UsageFeature> matcher = new IMatcher<UsageFeature>() {
			@Override
			public boolean matches(UsageFeature entry) {
				return entry instanceof TypeFeature;
			}
		};
		Set<UsageFeature> matches = dictionary.getAllMatchings(matcher);
		TypeFeature typeFeature = (TypeFeature) matches.iterator().next();
		return typeFeature.getType();
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