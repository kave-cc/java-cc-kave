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
package cc.kave.rsse.calls.model.features;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.rsse.calls.model.features.ClassContextFeature;
import cc.kave.rsse.calls.model.features.FeatureVisitor;

public class ClassContextFeatureTest {

	private ITypeName type;
	private ClassContextFeature sut;

	@Before
	public void setup() {
		type = mock(ITypeName.class);
		sut = new ClassContextFeature(type);
	}

	@Test(expected = RuntimeException.class)
	public void typeMustNotBeNull() {
		new ClassContextFeature(null);
	}

	@Test
	public void assignedMethodIsReturned() {
		ITypeName actual = sut.getTypeName();
		ITypeName expected = type;

		assertEquals(expected, actual);
	}

	@Test
	public void visitorIsImplemented() {
		final boolean[] res = new boolean[] { false };
		sut.accept(new FeatureVisitor() {
			@Override
			public void visit(ClassContextFeature f) {
				res[0] = true;
			}
		});
		assertTrue(res[0]);
	}

	@Test
	public void equality() {
		ITypeName mA = Names.newType("TA, P");
		ITypeName mB = Names.newType("TB, P");

		ClassContextFeature cfA1 = new ClassContextFeature(mA);
		ClassContextFeature cfA2 = new ClassContextFeature(mA);
		ClassContextFeature cfB = new ClassContextFeature(mB);

		Assert.assertEquals(cfA1, cfA2);
		Assert.assertEquals(cfA1.hashCode(), cfA2.hashCode());

		Assert.assertNotEquals(cfA1, cfB);
		Assert.assertNotEquals(cfA1.hashCode(), cfB.hashCode());
	}
}