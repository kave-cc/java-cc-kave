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

import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.Sets;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.rsse.calls.usages.CallSite;
import cc.kave.rsse.calls.usages.CallSiteKind;
import cc.kave.rsse.calls.usages.DefinitionSite;
import cc.kave.rsse.calls.usages.DefinitionSiteKind;
import cc.kave.rsse.calls.usages.DefinitionSites;
import cc.kave.rsse.calls.usages.Query;
import cc.kave.rsse.calls.usages.UsageFilter;

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
		Query q = createValidUsage();
		q.setType(Names.newType("p:int[]"));
		assertFalse(sut.apply(q));
	}

	@Test
	@Ignore("no support for annonymous classes so far")
	public void anonymqsClassesAreFiltered() {
		Query q = createValidUsage();
		q.setType(Names.newType("N.T+$1, P"));
		assertFalse(sut.apply(q));
	}

	@Test
	public void nestedClassesAreNotFiltered() {
		Query q = createValidUsage();
		q.setType(Names.newType("N.T+U, P"));
		assertTrue(sut.apply(q));
	}

	@Test
	public void usageWithUnknownDefIsFiltered() {
		Query q = createValidUsage();
		q.getDefinitionSite().setKind(DefinitionSiteKind.UNKNOWN);
		assertFalse(sut.apply(q));
	}

	@Test
	public void usageWithqtCallsIsFiltered() {
		Query q = createValidUsage();
		q.getAllCallsites().clear();
		assertFalse(sut.apply(q));
	}

	private static Query createValidUsage() {
		Query q = new Query();

		q.setType(Names.newType("T,P"));
		DefinitionSite definition = DefinitionSites.createDefinitionByReturn(Names.newMethod("LType.get()V"));
		q.setDefinition(definition);

		q.setClassContext(Names.newType("LSuperType"));
		q.setMethodContext(mock(IMethodName.class));

		Set<CallSite> calls = Sets.newHashSet();
		calls.add(createParameterCallSite());
		calls.add(createReceiverCallSite());
		q.setAllCallsites(calls);

		return q;
	}

	private static CallSite createParameterCallSite() {
		CallSite site = new CallSite();
		site.setKind(CallSiteKind.PARAMETER);
		site.setMethod(mock(IMethodName.class));
		site.setArgIndex(12);
		return site;
	}

	private static CallSite createReceiverCallSite() {
		CallSite site = new CallSite();
		site.setKind(CallSiteKind.RECEIVER);
		site.setMethod(mock(IMethodName.class));
		return site;
	}
}