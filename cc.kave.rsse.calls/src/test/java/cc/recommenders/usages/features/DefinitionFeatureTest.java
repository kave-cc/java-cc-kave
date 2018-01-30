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
package cc.recommenders.usages.features;

import static cc.recommenders.usages.DefinitionSites.createDefinitionByField;
import static cc.recommenders.usages.DefinitionSites.createDefinitionByParam;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import cc.recommenders.usages.DefinitionSite;
import cc.recommenders.usages.DefinitionSites;
import cc.recommenders.usages.features.UsageFeature.ObjectUsageFeatureVisitor;

public class DefinitionFeatureTest {

	private DefinitionSite definitionSite;
	private DefinitionFeature sut;

	@Before
	public void setup() {
		definitionSite = DefinitionSites.createDefinitionByConstant();
		sut = new DefinitionFeature(definitionSite);
	}

	@Test(expected = RuntimeException.class)
	public void definitionMustNotBeNull() {
		new DefinitionFeature(null);
	}

	@Test
	public void assignedDefinitionIsReturned() {
		DefinitionSite actual = sut.getDefinitionSite();
		DefinitionSite expected = definitionSite;

		assertSame(expected, actual);
	}

	@Test
	public void visitorIsImplemented() {
		final boolean[] res = new boolean[] { false };
		sut.accept(new ObjectUsageFeatureVisitor() {
			@Override
			public void visit(DefinitionFeature f) {
				res[0] = true;
			}
		});
		assertTrue(res[0]);
	}

	@Test
	public void equality() {

		DefinitionFeature df1 = feature(createDefinitionByParam("[?] [?].m()", 1));
		DefinitionFeature df2 = feature(createDefinitionByParam("[?] [?].m()", 1));

		assertTrue(df1.equals(df2));
		assertTrue(df1.hashCode() == df2.hashCode());
	}

	@Test
	public void equality_diffField() {

		DefinitionFeature df1 = feature(createDefinitionByField("[?] [?]._f"));
		DefinitionFeature df2 = feature(createDefinitionByField("[?] [?]._g"));

		assertFalse(df1.equals(df2));
		assertFalse(df1.hashCode() == df2.hashCode());
	}

	@Test
	public void equality_diffMethod() {

		DefinitionFeature df1 = feature(createDefinitionByParam("[?] [?].m1()", 1));
		DefinitionFeature df2 = feature(createDefinitionByParam("[?] [?].m2()", 1));

		assertFalse(df1.equals(df2));
		assertFalse(df1.hashCode() == df2.hashCode());
	}

	@Test
	public void equality_diffProperty() {

		DefinitionFeature df1 = feature(DefinitionSites.createDefinitionByProperty("get set [?] [?].P()"));
		DefinitionFeature df2 = feature(DefinitionSites.createDefinitionByProperty("get set [?] [?].Q()"));

		assertFalse(df1.equals(df2));
		assertFalse(df1.hashCode() == df2.hashCode());
	}

	@Test
	public void equality_diffParam() {

		DefinitionFeature df1 = feature(createDefinitionByParam("[?] [?].m()", 1));
		DefinitionFeature df2 = feature(createDefinitionByParam("[?] [?].m()", 2));

		assertFalse(df1.equals(df2));
		assertFalse(df1.hashCode() == df2.hashCode());
	}

	private static DefinitionFeature feature(DefinitionSite site) {
		return new DefinitionFeature(site);
	}
}