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
package cc.recommenders.usages;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import cc.recommenders.names.ICoReMethodName;
import cc.recommenders.usages.CallSite;
import cc.recommenders.usages.CallSiteKind;
import cc.recommenders.usages.DefinitionSite;
import cc.recommenders.usages.DefinitionSiteKind;
import cc.recommenders.usages.DefinitionSites;
import cc.recommenders.usages.Query;
import cc.recommenders.usages.UsageFilter;
import cc.recommenders.names.CoReMethodName;
import cc.recommenders.names.CoReTypeName;

import com.google.common.collect.Sets;

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
		q.setType(CoReTypeName.get("[LSomeType"));
		assertFalse(sut.apply(q));
	}

	@Test
	public void anonymqsClassesAreFiltered() {
		Query q = createValidUsage();
		q.setType(CoReTypeName.get("Lorg/eclipse/swt/widgets/Button$1"));
		assertFalse(sut.apply(q));
	}

	@Test
	public void nestedClassesAreNotFiltered() {
		Query q = createValidUsage();
		q.setType(CoReTypeName.get("Lorg/eclipse/swt/widgets/Button$Nested"));
		assertTrue(sut.apply(q));
	}

	@Test
	public void nonSwtTypesAreFiltered() {
		Query q = createValidUsage();
		q.setType(CoReTypeName.get("LType"));
		assertFalse(sut.apply(q));
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

		q.setType(CoReTypeName.get("Lorg/eclipse/swt/widgets/Button"));
		DefinitionSite definition = DefinitionSites.createDefinitionByReturn(CoReMethodName.get("LType.get()V"));
		q.setDefinition(definition);

		q.setClassContext(CoReTypeName.get("LSuperType"));
		q.setMethodContext(mock(ICoReMethodName.class));

		Set<CallSite> calls = Sets.newHashSet();
		calls.add(createParameterCallSite());
		calls.add(createReceiverCallSite());
		q.setAllCallsites(calls);

		return q;
	}

	private static CallSite createParameterCallSite() {
		CallSite site = new CallSite();
		site.setKind(CallSiteKind.PARAMETER);
		site.setMethod(mock(ICoReMethodName.class));
		site.setArgIndex(12);
		return site;
	}

	private static CallSite createReceiverCallSite() {
		CallSite site = new CallSite();
		site.setKind(CallSiteKind.RECEIVER);
		site.setMethod(mock(ICoReMethodName.class));
		return site;
	}
}