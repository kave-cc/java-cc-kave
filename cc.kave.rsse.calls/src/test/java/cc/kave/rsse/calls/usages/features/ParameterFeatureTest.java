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
import cc.kave.rsse.calls.usages.features.ParameterFeature;
import cc.kave.rsse.calls.usages.features.UsageFeature.ObjectUsageFeatureVisitor;

public class ParameterFeatureTest {

	private IMethodName method;
	private int argNum;
	private ParameterFeature sut;

	@Before
	public void setup() {
		method = mock(IMethodName.class);
		argNum = 12345;
		sut = new ParameterFeature(method, argNum);
	}

	@Test(expected = RuntimeException.class)
	public void methodMustNotBeNull() {
		new ParameterFeature(null, 1);
	}

	@Test(expected = RuntimeException.class)
	public void argCannotBeNegative() {
		new ParameterFeature(method, -1);
	}

	@Test
	public void assignedMethodIsReturned() {
		IMethodName actual = sut.getMethodName();
		IMethodName expected = method;

		assertEquals(expected, actual);
	}

	@Test
	public void assignedArgumentNumberIsReturned() {
		int actual = sut.getArgNum();
		int expected = argNum;

		assertEquals(expected, actual);
	}

	@Test
	public void visitorIsImplemented() {
		final boolean[] res = new boolean[] { false };
		sut.accept(new ObjectUsageFeatureVisitor() {
			@Override
			public void visit(ParameterFeature f) {
				res[0] = true;
			}
		});
		assertTrue(res[0]);
	}

	@Test
	public void equality() {
		IMethodName mA = Names.newMethod("[?] [?].mA()");
		IMethodName mB = Names.newMethod("[?] [?].mB()");

		ParameterFeature cfA1a = new ParameterFeature(mA, 1);
		ParameterFeature cfA1b = new ParameterFeature(mA, 1);
		ParameterFeature cfB1 = new ParameterFeature(mB, 1);
		ParameterFeature cfB2 = new ParameterFeature(mB, 2);

		Assert.assertEquals(cfA1a, cfA1b);
		Assert.assertEquals(cfA1a.hashCode(), cfA1b.hashCode());

		Assert.assertNotEquals(cfA1a, cfB1);
		Assert.assertNotEquals(cfA1a.hashCode(), cfB1.hashCode());

		Assert.assertNotEquals(cfB1, cfB2);
		Assert.assertNotEquals(cfB1.hashCode(), cfB2.hashCode());
	}
}