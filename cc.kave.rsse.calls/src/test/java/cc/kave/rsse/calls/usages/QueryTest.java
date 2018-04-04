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
		Set<UsageAccess> actuals = sut.getAllAccesses();
		assertEquals(Sets.newLinkedHashSet(), actuals);
	}

	@Test
	public void callSitesCanBeAdded() {
		UsageAccess site = createReceiverCallSite();
		Set<UsageAccess> expecteds = Sets.newHashSet(site);

		sut.addCallSite(site);
		Set<UsageAccess> actuals = sut.getAllAccesses();

		assertEquals(expecteds, actuals);
	}

	@Test
	public void successfullyAddingCallSitesReturnsTrue() {
		boolean success = sut.addCallSite(createReceiverCallSite());
		assertTrue(success);
	}

	@Test
	public void equalCallSitesCannotBeAddedTwice() {
		UsageAccess site = createReceiverCallSite();
		sut.addCallSite(site);
		boolean success = sut.addCallSite(site);
		assertFalse(success);

		Set<UsageAccess> actuals = sut.getAllAccesses();
		assertEquals(1, actuals.size());
	}

	@Test
	public void receiverCallSitesAreCorrectlyFiltered() {
		UsageAccess r1 = createReceiverCallSite();
		UsageAccess r2 = createReceiverCallSite();
		UsageAccess p1 = createParameterCallSite();
		UsageAccess p2 = createParameterCallSite();

		sut.accesses.addAll(Sets.newHashSet(r1, p1, r2, p2));
		Set<UsageAccess> actuals = sut.getReceiverCallsites();

		Set<UsageAccess> expecteds = Sets.newHashSet(r1, r2);
		assertEquals(expecteds, actuals);
	}

	@Test
	public void paramCallSitesAreCorrectlyFiltered() {
		UsageAccess r1 = createReceiverCallSite();
		UsageAccess r2 = createReceiverCallSite();
		UsageAccess p1 = createParameterCallSite();
		UsageAccess p2 = createParameterCallSite();

		sut.accesses.addAll(Sets.newHashSet(r1, p1, r2, p2));
		Set<UsageAccess> actuals = sut.getParameterCallsites();

		Set<UsageAccess> expecteds = Sets.newHashSet(p1, p2);
		assertEquals(expecteds, actuals);
	}

	@Test
	public void creationOfUsageCopyCreatesClone() {
		IUsage expected = createUsage();
		Usage actual = Usage.createAsCopyFrom(expected);

		assertNotSame(expected, actual);
		assertNotSame(expected.getAllAccesses(), actual.getAllAccesses());
	}

	@Test
	public void creationOfUsageCopiesWorks() {
		IUsage expected = createUsage();
		Usage actual = Usage.createAsCopyFrom(expected);

		assertEquals(expected.getType(), actual.getType());
		assertEquals(expected.getClassContext(), actual.getClassContext());
		assertEquals(expected.getMethodContext(), actual.getMethodContext());
		assertEquals(expected.getDefinitionSite(), actual.getDefinitionSite());
		assertEquals(expected.getAllAccesses(), actual.getAllAccesses());
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

	private static UsageAccess createReceiverCallSite() {
		IMethodName m = Names.newMethod("[p:void] [Type, P].receiverMethod()");
		UsageAccess site = UsageAccesses.createCallReceiver(m);
		return site;
	}

	private static UsageAccess createParameterCallSite() {
		IMethodName m = Names.newMethod("[p:void] [Type, P].paramMethod([Param, P] p)");
		UsageAccess site = UsageAccesses.createCallParameter(m, 1);
		return site;
	}

	private static IUsage createUsage() {
		Usage q = new Usage();
		q.setType(mock(ITypeName.class));
		q.setClassContext(mock(ITypeName.class));
		q.setMethodContext(mock(IMethodName.class));
		q.setDefinition(mock(DefinitionSite.class));
		q.addCallSite(mock(UsageAccess.class));
		q.addCallSite(mock(UsageAccess.class));
		return q;
	}
}