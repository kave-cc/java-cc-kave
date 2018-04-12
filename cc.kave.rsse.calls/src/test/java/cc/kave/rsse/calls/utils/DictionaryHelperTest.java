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

import static com.google.common.collect.Sets.newHashSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
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
import cc.kave.rsse.calls.model.Dictionary;
import cc.kave.rsse.calls.model.IMatcher;
import cc.kave.rsse.calls.model.features.ClassContextFeature;
import cc.kave.rsse.calls.model.features.DefinitionFeature;
import cc.kave.rsse.calls.model.features.IFeature;
import cc.kave.rsse.calls.model.features.MethodContextFeature;
import cc.kave.rsse.calls.model.features.TypeFeature;
import cc.kave.rsse.calls.model.features.UsageSiteFeature;

public class DictionaryHelperTest {

	private DictionaryHelper sut;

	@Mock
	Dictionary<IFeature> dictionary;

	@Captor
	ArgumentCaptor<IMatcher<IFeature>> matcherCaptor;

	@Before
	public void setup() {
		initMocks(this);
		Set<IFeature> features = newHashSet();
		when(dictionary.getAllMatchings(matcherCaptor.capture())).thenReturn(features);

		sut = new DictionaryHelper(dictionary);
	}

	@Test
	public void dummyStatesAreAdded() {
		sut.addDummyStatesToEnsureAtLeastTwoStatesPerNode();
		verify(dictionary, times(6)).add(any(IFeature.class));
		// TODO why does the following not work?
		// verify(dictionary).add(any(ClassFeature.class));
		// verify(dictionary).add(any(FirstMethodFeature.class));
		// verify(dictionary, times(2)).add(any(DefinitionFeature.class));
	}

	@Test
	public void getType() {
		TypeFeature typeFeature = createTypeFeature();
		fail();
		ITypeName expected = null;// typeFeature.getType();

		dictionary = new Dictionary<IFeature>();
		dictionary.add(typeFeature);
		dictionary.add(createClassFeature());
		dictionary.add(createMethodFeature());
		dictionary.add(createCallFeature());
		sut = new DictionaryHelper(dictionary);
		ITypeName actual = sut.getType();

		assertEquals(expected, actual);
	}

	@Test
	public void getClassContexts() {
		sut.getClassContexts();
		IMatcher<IFeature> matcher = matcherCaptor.getValue();
		assertTrue(matcher.matches(createClassFeature()));
		assertFalse(matcher.matches(createTypeFeature()));
	}

	@Test
	public void getMethodContexts() {
		sut.getMethodContexts();
		IMatcher<IFeature> matcher = matcherCaptor.getValue();
		assertTrue(matcher.matches(createMethodFeature()));
		assertFalse(matcher.matches(createTypeFeature()));
	}

	@Test
	public void getDefinition() {
		sut.getDefinitions();
		IMatcher<IFeature> matcher = matcherCaptor.getValue();
		assertTrue(matcher.matches(createDefinitionFeature()));
		assertFalse(matcher.matches(createTypeFeature()));
	}

	@Test
	public void getCallSitesMatcherIsCorrect() {
		sut.getUsageSites();
		IMatcher<IFeature> matcher = matcherCaptor.getValue();
		assertTrue(matcher.matches(createCallFeature()));
		assertFalse(matcher.matches(createTypeFeature()));
	}

	@Test
	public void allCallSitesAreFoundAndCasted() {
		UsageSiteFeature c1 = createCallFeature();
		UsageSiteFeature c2 = createCallFeature();

		dictionary = new Dictionary<IFeature>();
		dictionary.add(createTypeFeature());
		dictionary.add(c1);
		dictionary.add(c2);
		sut = new DictionaryHelper(dictionary);

		Set<UsageSiteFeature> actual = sut.getUsageSites();
		Set<UsageSiteFeature> expected = newHashSet(c1, c2);
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

	private static ClassContextFeature createClassFeature() {
		ClassContextFeature f = mock(ClassContextFeature.class);
		return f;
	}

	private static MethodContextFeature createMethodFeature() {
		MethodContextFeature f = mock(MethodContextFeature.class);
		return f;
	}

	private static DefinitionFeature createDefinitionFeature() {
		DefinitionFeature f = mock(DefinitionFeature.class);
		return f;
	}

	private static UsageSiteFeature createCallFeature() {
		UsageSiteFeature f = mock(UsageSiteFeature.class);
		return f;
	}
}