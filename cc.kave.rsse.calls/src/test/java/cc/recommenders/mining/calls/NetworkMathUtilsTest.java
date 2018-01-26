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
package cc.recommenders.mining.calls;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NetworkMathUtilsTest {
	@Test
	public void roundToDefaultPrecision() {
		double actual = NetworkMathUtils.roundToDefaultPrecision(0.123456789);
		double expected = 0.123457;
		assertEquals(expected, actual, 0.0000001);
	}
}