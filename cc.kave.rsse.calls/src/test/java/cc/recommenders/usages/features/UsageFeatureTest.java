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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Test;

import cc.recommenders.usages.features.UsageFeature.ObjectUsageFeatureVisitor;

public class UsageFeatureTest {

	@Test
	public void equalObjects() {
		UsageFeature a = createFeature("a");
		UsageFeature b = createFeature("a");
		assertEquals(a, b);
		assertTrue(a.hashCode() == b.hashCode());
	}

	@Test
	public void differentObjects() {
		UsageFeature a = createFeature("a");
		UsageFeature b = createFeature("b");
		assertFalse(a.equals(b));
		assertFalse(a.hashCode() == b.hashCode());
	}

	@Test
	public void visitorMethodsArePreimplemented() {
		ObjectUsageFeatureVisitor visitor = new UsageFeature.ObjectUsageFeatureVisitor();
		visitor.visit((TypeFeature) null);
		visitor.visit((ClassFeature) null);
		visitor.visit((SuperMethodFeature) null);
		visitor.visit((FirstMethodFeature) null);
		visitor.visit((DefinitionFeature) null);
		visitor.visit((CallFeature) null);
		visitor.visit((ParameterFeature) null);
	}

	private UsageFeature createFeature(final String _name) {
		return new UsageFeature() {

			@SuppressWarnings("unused")
			// needed for testing purpose
			private String name;

			{
				name = _name;
			}

			@Override
			public void accept(ObjectUsageFeatureVisitor v) {
			}

			@Override
			public int hashCode() {
				return HashCodeBuilder.reflectionHashCode(this);
			}

			@Override
			public boolean equals(Object obj) {
				return EqualsBuilder.reflectionEquals(this, obj);
			}
		};
	}
}