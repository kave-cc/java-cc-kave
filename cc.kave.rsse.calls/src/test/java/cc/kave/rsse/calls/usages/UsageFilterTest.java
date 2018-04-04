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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;

public class UsageFilterTest {

	private UsageFilter sut;

	@Before
	public void setup() {
		sut = new UsageFilter();
	}

	@Test
	public void normalUsagesPass() {
		assertTrue(sut.apply(createValidUsage()));
	}

	@Test
	public void arraysAreFiltered() {
		Usage q = createValidUsage();
		q.setType(Names.newType("p:int[]"));
		assertFalse(sut.apply(q));
	}

	@Test
	@Ignore("no support for annonymous classes so far")
	public void anonymqsClassesAreFiltered() {
		Usage q = createValidUsage();
		q.setType(Names.newType("N.T+$1, P"));
		assertFalse(sut.apply(q));
	}

	@Test
	public void nestedClassesAreNotFiltered() {
		Usage q = createValidUsage();
		q.setType(Names.newType("N.T+U, P"));
		assertTrue(sut.apply(q));
	}

	@Test
	public void usageWithUnknownDefIsFiltered() {
		Usage q = createValidUsage();
		q.getDefinitionSite().setKind(DefinitionSiteKind.UNKNOWN);
		assertFalse(sut.apply(q));
	}

	@Test
	public void usageWithqtCallsIsFiltered() {
		Usage q = createValidUsage();
		q.getAllAccesses().clear();
		assertFalse(sut.apply(q));
	}

	private static Usage createValidUsage() {
		Usage q = new Usage();

		q.setType(Names.newType("T,P"));
		DefinitionSite definition = DefinitionSites.createDefinitionByReturn(Names.newMethod("LType.get()V"));
		q.setDefinition(definition);

		q.setClassContext(Names.newType("LSuperType"));
		q.setMethodContext(mock(IMethodName.class));

		q.accesses.add(createParameterCallSite());
		q.accesses.add(createReceiverCallSite());

		return q;
	}

	private static UsageAccess createParameterCallSite() {
		UsageAccess site = new UsageAccess();
		site.setKind(UsageAccessType.CALL_PARAMETER);
		site.setMethod(mock(IMethodName.class));
		site.setArgIndex(12);
		return site;
	}

	private static UsageAccess createReceiverCallSite() {
		UsageAccess site = new UsageAccess();
		site.setKind(UsageAccessType.CALL_RECEIVER);
		site.setMethod(mock(IMethodName.class));
		return site;
	}
}