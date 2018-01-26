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
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import cc.recommenders.names.ICoReMethodName;
import cc.recommenders.usages.features.ParameterFeature;
import cc.recommenders.usages.features.UsageFeature.ObjectUsageFeatureVisitor;

public class ParameterFeatureTest {

	private ICoReMethodName method;
	private int argNum;
	private ParameterFeature sut;

	@Before
	public void setup() {
		method = mock(ICoReMethodName.class);
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
		ICoReMethodName actual = sut.getMethodName();
		ICoReMethodName expected = method;

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

	// TODO write tests for hashCode + equals
}