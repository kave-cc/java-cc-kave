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
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.rsse.calls.usages.features.CallFeature;
import cc.kave.rsse.calls.usages.features.UsageFeature.ObjectUsageFeatureVisitor;

public class CallFeatureTest {

	private IMethodName method;
	private CallFeature sut;

	@Before
	public void setup() {
		method = mock(IMethodName.class);
		sut = new CallFeature(method);
	}

	@Test(expected = RuntimeException.class)
	public void methodMustNotBeNull() {
		new CallFeature(null);
	}

	@Test
	public void assignedMethodIsReturned() {
		IMethodName actual = sut.getMethodName();
		IMethodName expected = method;

		assertEquals(expected, actual);
	}

	@Test
	public void visitorIsImplemented() {
		final boolean[] res = new boolean[] { false };
		sut.accept(new ObjectUsageFeatureVisitor() {
			@Override
			public void visit(CallFeature f) {
				res[0] = true;
			}
		});
		assertTrue(res[0]);
	}

	@Test
	public void equality() {
		IMethodName mA = Names.newMethod("[?] [?].mA()");
		IMethodName mB = Names.newMethod("[?] [?].mB()");

		CallFeature cfA1 = new CallFeature(mA);
		CallFeature cfA2 = new CallFeature(mA);
		CallFeature cfB = new CallFeature(mB);

		Assert.assertEquals(cfA1, cfA2);
		Assert.assertEquals(cfA1.hashCode(), cfA2.hashCode());

		Assert.assertNotEquals(cfA1, cfB);
		Assert.assertNotEquals(cfA1.hashCode(), cfB.hashCode());
	}
}