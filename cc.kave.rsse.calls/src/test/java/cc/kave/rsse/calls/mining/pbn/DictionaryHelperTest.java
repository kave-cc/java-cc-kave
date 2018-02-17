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
package cc.kave.rsse.calls.mining.pbn;

import static com.google.common.collect.Sets.newHashSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import com.google.common.collect.Sets;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.rsse.calls.datastructures.Dictionary;
import cc.kave.rsse.calls.datastructures.IMatcher;
import cc.kave.rsse.calls.pbn.DictionaryHelper;
import cc.kave.rsse.calls.usages.features.CallFeature;
import cc.kave.rsse.calls.usages.features.ClassFeature;
import cc.kave.rsse.calls.usages.features.DefinitionFeature;
import cc.kave.rsse.calls.usages.features.FirstMethodFeature;
import cc.kave.rsse.calls.usages.features.ParameterFeature;
import cc.kave.rsse.calls.usages.features.TypeFeature;
import cc.kave.rsse.calls.usages.features.UsageFeature;

public class DictionaryHelperTest {

	private DictionaryHelper sut;

	@Mock
	Dictionary<UsageFeature> dictionary;

	@Captor
	ArgumentCaptor<IMatcher<UsageFeature>> matcherCaptor;

	@Before
	public void setup() {
		initMocks(this);
		Set<UsageFeature> features = newHashSet();
		when(dictionary.getAllMatchings(matcherCaptor.capture())).thenReturn(features);

		sut = new DictionaryHelper(dictionary);
	}

	@Test
	public void dummyStatesAreAdded() {
		sut.addDummyStatesToEnsureAtLeastTwoStatesPerNode();
		verify(dictionary, times(6)).add(any(UsageFeature.class));
		// TODO why does the following not work?
		// verify(dictionary).add(any(ClassFeature.class));
		// verify(dictionary).add(any(FirstMethodFeature.class));
		// verify(dictionary, times(2)).add(any(DefinitionFeature.class));
	}

	@Test
	public void getType() {
		TypeFeature typeFeature = createTypeFeature();
		ITypeName expected = typeFeature.getType();

		dictionary = new Dictionary<UsageFeature>();
		dictionary.add(typeFeature);
		dictionary.add(createClassFeature());
		dictionary.add(createMethodFeature());
		dictionary.add(createCallFeature());
		dictionary.add(createParameterFeature());
		sut = new DictionaryHelper(dictionary);
		ITypeName actual = sut.getType();

		assertEquals(expected, actual);
	}

	@Test
	public void getClassContexts() {
		sut.getClassContexts();
		IMatcher<UsageFeature> matcher = matcherCaptor.getValue();
		assertTrue(matcher.matches(createClassFeature()));
		assertFalse(matcher.matches(createTypeFeature()));
	}

	@Test
	public void getMethodContexts() {
		sut.getMethodContexts();
		IMatcher<UsageFeature> matcher = matcherCaptor.getValue();
		assertTrue(matcher.matches(createMethodFeature()));
		assertFalse(matcher.matches(createTypeFeature()));
	}

	@Test
	public void getDefinition() {
		sut.getDefinitions();
		IMatcher<UsageFeature> matcher = matcherCaptor.getValue();
		assertTrue(matcher.matches(createDefinitionFeature()));
		assertFalse(matcher.matches(createTypeFeature()));
	}

	@Test
	public void getCallSitesMatcherIsCorrect() {
		sut.getCallSites();
		IMatcher<UsageFeature> matcher = matcherCaptor.getValue();
		assertTrue(matcher.matches(createCallFeature()));
		assertFalse(matcher.matches(createTypeFeature()));
	}

	@Test
	public void allCallSitesAreFoundAndCasted() {
		CallFeature c1 = createCallFeature();
		CallFeature c2 = createCallFeature();

		dictionary = new Dictionary<UsageFeature>();
		dictionary.add(createTypeFeature());
		dictionary.add(c1);
		dictionary.add(c2);
		sut = new DictionaryHelper(dictionary);

		Set<CallFeature> actual = sut.getCallSites();
		Set<CallFeature> expected = newHashSet(c1, c2);
		assertEquals(expected, actual);
	}

	@Test
	public void getParametersMatcherIsCorrect() {
		sut.getParameterSites();
		IMatcher<UsageFeature> matcher = matcherCaptor.getValue();
		assertTrue(matcher.matches(createParameterFeature()));
		assertFalse(matcher.matches(createTypeFeature()));
	}

	@Test
	public void allParameterSitesAreFoundAndCasted() {
		ParameterFeature p1 = createParameterFeature();
		ParameterFeature p2 = createParameterFeature();

		dictionary = new Dictionary<UsageFeature>();
		dictionary.add(createTypeFeature());
		dictionary.add(p1);
		dictionary.add(p2);
		sut = new DictionaryHelper(dictionary);

		Set<ParameterFeature> actual = sut.getParameterSites();
		Set<ParameterFeature> expected = newHashSet(p1, p2);
		assertEquals(expected, actual);
	}

	@Test
	public void dictionaryDiff() {
		Dictionary<String> a = new Dictionary<String>();
		a.add("a");
		a.add("b");
		Dictionary<String> b = new Dictionary<String>();
		b.add("a");
		b.add("c");

		Set<String> actual = DictionaryHelper.diff(a, b);
		Set<String> expected = Sets.newHashSet();
		expected.add("-b");
		expected.add("+c");

		assertEquals(expected, actual);
	}

	private static TypeFeature createTypeFeature() {
		ITypeName typeName = Names.newType("org.blubb.Bla, P");
		TypeFeature f = new TypeFeature(typeName);
		return f;
	}

	private static ClassFeature createClassFeature() {
		ClassFeature f = mock(ClassFeature.class);
		return f;
	}

	private static FirstMethodFeature createMethodFeature() {
		FirstMethodFeature f = mock(FirstMethodFeature.class);
		return f;
	}

	private static DefinitionFeature createDefinitionFeature() {
		DefinitionFeature f = mock(DefinitionFeature.class);
		return f;
	}

	private static CallFeature createCallFeature() {
		CallFeature f = mock(CallFeature.class);
		return f;
	}

	private static ParameterFeature createParameterFeature() {
		ParameterFeature f = mock(ParameterFeature.class);
		return f;
	}
}