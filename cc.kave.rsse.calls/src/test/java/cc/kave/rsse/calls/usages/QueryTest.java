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
package cc.kave.rsse.calls.usages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.testing.ToStringAsserts;

public class QueryTest {

	private Usage sut;

	@Before
	public void sut() {
		sut = new Usage();
	}

	@Test
	public void typeCanBeSet() {
		ITypeName expected = mock(ITypeName.class);
		sut.setType(expected);
		ITypeName actual = sut.getType();
		assertEquals(expected, actual);
	}

	@Test
	public void classContextCanBeSet() {
		ITypeName expected = mock(ITypeName.class);
		sut.setClassContext(expected);
		ITypeName actual = sut.getClassContext();
		assertEquals(expected, actual);
	}

	@Test
	public void methodContextCanBeSet() {
		IMethodName expected = mock(IMethodName.class);
		sut.setMethodContext(expected);
		IMethodName actual = sut.getMethodContext();
		assertEquals(expected, actual);
	}

	@Test
	public void definitionCanBeSet() {
		DefinitionSite expected = mock(DefinitionSite.class);
		sut.setDefinition(expected);
		DefinitionSite actual = sut.getDefinitionSite();
		assertEquals(expected, actual);
	}

	@Test
	public void callSitesAreEmptyByDefault() {
		Set<UsageSite> actuals = sut.getAllUsageSites();
		assertEquals(Sets.newLinkedHashSet(), actuals);
	}

	@Test
	public void callSitesCanBeAdded() {
		UsageSite site = createReceiverCallSite();
		Set<UsageSite> expecteds = Sets.newHashSet(site);

		sut.addCallSite(site);
		Set<UsageSite> actuals = sut.getAllUsageSites();

		assertEquals(expecteds, actuals);
	}

	@Test
	public void successfullyAddingCallSitesReturnsTrue() {
		boolean success = sut.addCallSite(createReceiverCallSite());
		assertTrue(success);
	}

	@Test
	public void equalCallSitesCannotBeAddedTwice() {
		UsageSite site = createReceiverCallSite();
		sut.addCallSite(site);
		boolean success = sut.addCallSite(site);
		assertFalse(success);

		Set<UsageSite> actuals = sut.getAllUsageSites();
		assertEquals(1, actuals.size());
	}

	@Test
	public void receiverCallSitesAreCorrectlyFiltered() {
		UsageSite r1 = createReceiverCallSite();
		UsageSite r2 = createReceiverCallSite();
		UsageSite p1 = createParameterCallSite();
		UsageSite p2 = createParameterCallSite();

		sut.accesses.addAll(Sets.newHashSet(r1, p1, r2, p2));
		Set<UsageSite> actuals = sut.getCallSites();

		Set<UsageSite> expecteds = Sets.newHashSet(r1, r2);
		assertEquals(expecteds, actuals);
	}

	@Test
	public void paramCallSitesAreCorrectlyFiltered() {
		UsageSite r1 = createReceiverCallSite();
		UsageSite r2 = createReceiverCallSite();
		UsageSite p1 = createParameterCallSite();
		UsageSite p2 = createParameterCallSite();

		sut.accesses.addAll(Sets.newHashSet(r1, p1, r2, p2));
		Set<UsageSite> actuals = sut.getParameterSites();

		Set<UsageSite> expecteds = Sets.newHashSet(p1, p2);
		assertEquals(expecteds, actuals);
	}

	@Test
	public void creationOfUsageCopyCreatesClone() {
		IUsage expected = createUsage();
		Usage actual = Usage.createAsCopyFrom(expected);

		assertNotSame(expected, actual);
		assertNotSame(expected.getAllUsageSites(), actual.getAllUsageSites());
	}

	@Test
	public void creationOfUsageCopiesWorks() {
		IUsage expected = createUsage();
		Usage actual = Usage.createAsCopyFrom(expected);

		assertEquals(expected.getType(), actual.getType());
		assertEquals(expected.getClassContext(), actual.getClassContext());
		assertEquals(expected.getMethodContext(), actual.getMethodContext());
		assertEquals(expected.getDefinitionSite(), actual.getDefinitionSite());
		assertEquals(expected.getAllUsageSites(), actual.getAllUsageSites());
	}

	@Test
	public void equality_default() {
		assertEquals(new Usage(), new Usage());
	}

	@Test
	@SuppressWarnings("deprecation")
	public void equality_notEqualToNoUsage() {
		assertNotEquals(new Usage(), new NoUsage());
	}

	@Test
	public void toStringIsImplemented() {
		ToStringAsserts.assertToStringUtils(new Usage());
	}

	private static UsageSite createReceiverCallSite() {
		IMethodName m = Names.newMethod("[p:void] [Type, P].receiverMethod()");
		UsageSite site = UsageSites.methodCall(m);
		return site;
	}

	private static UsageSite createParameterCallSite() {
		IMethodName m = Names.newMethod("[p:void] [Type, P].paramMethod([Param, P] p)");
		UsageSite site = UsageSites.methodParameter(m, 1);
		return site;
	}

	private static IUsage createUsage() {
		Usage q = new Usage();
		q.setType(mock(ITypeName.class));
		q.setClassContext(mock(ITypeName.class));
		q.setMethodContext(mock(IMethodName.class));
		q.setDefinition(mock(DefinitionSite.class));
		q.addCallSite(mock(UsageSite.class));
		q.addCallSite(mock(UsageSite.class));
		return q;
	}
}