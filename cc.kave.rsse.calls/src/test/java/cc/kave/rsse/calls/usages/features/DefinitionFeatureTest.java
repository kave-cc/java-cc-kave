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
package cc.kave.rsse.calls.usages.features;

import static cc.kave.commons.model.naming.Names.newProperty;
import static cc.kave.rsse.calls.usages.model.impl.Definitions.definedByMemberAccess;
import static cc.kave.rsse.calls.usages.model.impl.Definitions.definedByMemberAccessToField;
import static cc.kave.rsse.calls.usages.model.impl.Definitions.definedByMemberAccessToProperty;
import static cc.kave.rsse.calls.usages.model.impl.Definitions.definedByMethodParameter;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import cc.kave.rsse.calls.usages.features.UsageFeature.ObjectUsageFeatureVisitor;
import cc.kave.rsse.calls.usages.model.IDefinition;
import cc.kave.rsse.calls.usages.model.impl.Definitions;

public class DefinitionFeatureTest {

	private IDefinition definitionSite;
	private DefinitionFeature sut;

	@Before
	public void setup() {
		definitionSite = Definitions.definedByConstant();
		sut = new DefinitionFeature(definitionSite);
	}

	@Test(expected = RuntimeException.class)
	public void definitionMustNotBeNull() {
		new DefinitionFeature(null);
	}

	@Test
	public void assignedDefinitionIsReturned() {
		IDefinition actual = sut.getDefinitionSite();
		IDefinition expected = definitionSite;

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

		DefinitionFeature df1 = feature(definedByMethodParameter("[?] [?].m()", 1));
		DefinitionFeature df2 = feature(definedByMethodParameter("[?] [?].m()", 1));

		assertTrue(df1.equals(df2));
		assertTrue(df1.hashCode() == df2.hashCode());
	}

	@Test
	public void equality_diffField() {

		DefinitionFeature df1 = feature(definedByMemberAccessToField("[?] [?]._f"));
		DefinitionFeature df2 = feature(definedByMemberAccessToField("[?] [?]._g"));

		assertFalse(df1.equals(df2));
		assertFalse(df1.hashCode() == df2.hashCode());
	}

	@Test
	public void equality_diffMethod() {

		DefinitionFeature df1 = feature(definedByMethodParameter("[?] [?].m1()", 1));
		DefinitionFeature df2 = feature(definedByMethodParameter("[?] [?].m2()", 1));

		assertFalse(df1.equals(df2));
		assertFalse(df1.hashCode() == df2.hashCode());
	}

	@Test
	public void equality_diffProperty() {

		DefinitionFeature df1 = feature(definedByMemberAccess(newProperty("get set [?] [?].P()")));
		DefinitionFeature df2 = feature(definedByMemberAccessToProperty("get set [?] [?].Q()"));

		assertFalse(df1.equals(df2));
		assertFalse(df1.hashCode() == df2.hashCode());
	}

	@Test
	public void equality_diffParam() {

		DefinitionFeature df1 = feature(definedByMethodParameter("[?] [?].m()", 1));
		DefinitionFeature df2 = feature(definedByMethodParameter("[?] [?].m()", 2));

		assertFalse(df1.equals(df2));
		assertFalse(df1.hashCode() == df2.hashCode());
	}

	private static DefinitionFeature feature(IDefinition site) {
		return new DefinitionFeature(site);
	}
}