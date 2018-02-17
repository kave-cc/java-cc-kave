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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.rsse.calls.usages.features.TypeFeature;
import cc.kave.rsse.calls.usages.features.UsageFeature.ObjectUsageFeatureVisitor;

public class TypeFeatureTest {

	private ITypeName type;
	private TypeFeature sut;

	@Before
	public void setup() {
		type = mock(ITypeName.class);
		sut = new TypeFeature(type);
	}

	@Test(expected = RuntimeException.class)
	public void typeMustNotBeNull() {
		new TypeFeature(null);
	}

	@Test
	public void assignedMethodIsReturned() {
		ITypeName actual = sut.getType();
		ITypeName expected = type;

		assertEquals(expected, actual);
	}

	@Test
	public void visitorIsImplemented() {
		final boolean[] res = new boolean[] { false };
		sut.accept(new ObjectUsageFeatureVisitor() {
			@Override
			public void visit(TypeFeature f) {
				res[0] = true;
			}
		});
		assertTrue(res[0]);
	}

	@Test
	public void equality() {
		ITypeName mA = Names.newType("T,P");
		ITypeName mB = Names.newType("U,P");

		TypeFeature cfA1 = new TypeFeature(mA);
		TypeFeature cfA2 = new TypeFeature(mA);
		TypeFeature cfB = new TypeFeature(mB);

		Assert.assertEquals(cfA1, cfA2);
		Assert.assertEquals(cfA1.hashCode(), cfA2.hashCode());

		Assert.assertNotEquals(cfA1, cfB);
		Assert.assertNotEquals(cfA1.hashCode(), cfB.hashCode());
	}
}