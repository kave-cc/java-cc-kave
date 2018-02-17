/**
 * Copyright (c) 2010 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Marcel Bruch - initial API and implementation.
 *    Sebastian Proksch - stripped down to a simple calculation helper
 */
package cc.recommenders.mining.calls;

import static cc.kave.commons.assertions.Throws.newIllegalArgumentException;
import static cc.kave.commons.assertions.Throws.throwIllegalStateException;
import static java.lang.Math.abs;

import java.math.BigDecimal;
import java.util.Arrays;

import org.apache.commons.math.stat.StatUtils;
import org.apache.commons.math.util.MathUtils;
import org.eclipse.recommenders.commons.bayesnet.Node;

import cc.kave.commons.assertions.Asserts;

public class NetworkMathUtils {

	// TODO check why this is so huge!
	public static final double MAX_PROBABILTY_DELTA = 0.1;
	public static final int P_ROUNDING_PRECISION = 6;
	public static final double P_ROUNDING_FACTOR = 1000000;
	public static final double P_MIN = 0.000001;
	public static final double P_MAX = 0.999999;

	/**
	 * Simple smoothing smoothing strategy that adds a minimum value to all values
	 * to guarantee that all values are &gt;0. <br>
	 * <b>Note:</b> This is not a scaling, if <b>a lot</b> of values are passed, the
	 * added minimum can add up to a considerable value! <br>
	 * <b>Note:</b> Changes the value array!
	 * 
	 * @param values
	 *            the values to be scaled
	 * @throws RuntimeException
	 *             if delta passes a max threshold
	 */
	public static void smoothValues(final double[] values) {

		for (int i = 0; i < values.length; i++) {
			values[i] += P_MIN;
		}
	}

	public static void ensureAllProbabilitiesInValidRange(final double[] values) {
		Asserts.assertNotNull(values);
		Asserts.assertGreaterOrEqual(values.length, 1);

		for (int i = values.length; i-- > 0;) {
			if (!isInMinMaxRange(values[i])) {
				throw newIllegalArgumentException("index '%d' has invalid value of '%1.6f'.", i, values[i]);
			}
		}
	}

	public static boolean isInMinMaxRange(final double value) {
		if (value < P_MIN) {
			return false;
		}
		if (value > P_MAX) {
			return false;
		}
		return true;
	}

	private static boolean isDeltaEqualToZero(final double delta) {
		return delta == 0d;
	}

	private static boolean isDeltaTooHigh(final double delta) {
		final boolean res = abs(delta) > MAX_PROBABILTY_DELTA;
		return res;
	}

	@Deprecated
	public static double round(final double value, final int precision) {
		return MathUtils.round(value, precision, BigDecimal.ROUND_HALF_EVEN);

	}

	public static double roundToDefaultPrecision(final double value) {
		return (long) (P_ROUNDING_FACTOR * value + 0.5) / P_ROUNDING_FACTOR;
	}

	private static void ensureSumIsOne(final double[] values) {
		final double sum = StatUtils.sum(values);
		final double delta = abs(1 - sum);
		if (delta > 0.0001) {
			throwIllegalStateException("failed to round double array properly");
		}
	}

	//
	// private static IMethodName findConstructorCall(final Set<IMethodName>
	// invokedMethods) {
	// for (final IMethodName m : invokedMethods) {
	// if (m.isInit()) {
	// return m;
	// }
	// }
	// return null;
	// }

	public static void ensureCorrectNumberOfProbabilities(final Node node) {
		int numberOfProbabilities = node.getStates().length;
		for (final Node parent : node.getParents()) {
			numberOfProbabilities *= parent.getStates().length;
		}
		Asserts.assertEquals(numberOfProbabilities, node.getProbabilities().length,
				"incomplete probability definition");
	}

	public static void ensureMinimumTwoStates(final Node node) {
		ensureMinimumTwoStates(node.getStates());
	}

	public static void ensureMinimumTwoStates(final String[] states) {
		Asserts.assertGreaterOrEqual(states.length, 2);
	}

	public static double[] createPriorProbabilitiesForContextNodeAssumingDummyStateAtFirstIndex(final int length) {
		final double[] res = new double[length];
		Arrays.fill(res, 1, res.length, P_MIN);
		final double sum = StatUtils.sum(res);
		res[0] = 1 - sum;
		return res;
	}

	/**
	 * Returns a/b. Returns P_MAX if b==0;
	 * 
	 * @param a enumerator
	 * @param b denominator
	 * @return division result in the range [P_MIN, P_MAX], P_MAX, for divByZero
	 */
	public static double safeDivMaxMin(final int a, final int b) {
		if (b == 0) {
			return P_MAX;
		}

		final double res = a / (double) b;
		if (res > P_MAX) {
			return P_MAX;
		}
		if (res < P_MIN) {
			return P_MIN;
		}
		return res;
	}

	public static double getProbabilityInMinMaxRange(double probability) {
		if (probability > P_MAX) {
			return P_MAX;
		}
		if (probability < P_MIN) {
			return P_MIN;
		}
		return probability;
	}
}
