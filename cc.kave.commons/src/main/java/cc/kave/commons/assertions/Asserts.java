/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package cc.kave.commons.assertions;

import static java.lang.String.format;

import cc.kave.commons.exceptions.AssertionException;

public class Asserts {
	public static void assertNotNull(Object o) {
		assertNotNull(o, format("reference must not be null"));
	}

	public static void assertNotNull(Object o, String message) {
		if (o == null) {
			throw new AssertionException(message);
		}
	}

	public static void assertNotNegative(int arg) {
		if (arg < 0) {
			throw new AssertionException(format("parameter %d must not be negative", arg));
		}
	}

	public static void assertNotNegative(double arg) {
		if (arg < 0) {
			throw new AssertionException(format("parameter %f must not be negative", arg));
		}
	}

	public static void assertGreaterThan(double a, double b) {
		if (a <= b) {
			throw new AssertionException(format("parameter %f must be greater than parameter %f", a, b));
		}
	}

	public static void assertGreaterThan(int a, int b) {
		if (a <= b) {
			throw new AssertionException(format("first parameter (%d) must be > second parameter (%d)", a, b));
		}
	}

	public static void assertGreaterOrEqual(int a, int b) {
		if (a < b) {
			throw new AssertionException(format("first parameter (%d) must be >= second parameter (%d)", a, b));
		}
	}

	public static void assertGreaterOrEqual(double a, double b) {
		if (a < b) {
			throw new AssertionException(format("first parameter (%f) must be >= second parameter (%f)", a, b));
		}
	}

	public static void assertEquals(int a, int b) {
		if (a != b) {
			throw new AssertionException(format("parameters %d and %d must be equal", a, b));
		}
	}

	public static void assertLessOrEqual(double a, double b) {
		if (a > b) {
			throw new AssertionException(format("parameter %f must be less or equal to %f", a, b));
		}
	}

	public static void assertLessOrEqual(int a, int b) {
		if (a > b) {
			throw new AssertionException(format("parameter %d must be less or equal to %d", a, b));
		}
	}

	public static void assertPositive(int a) {
		if (a < 1) {
		}
	}

	public static void fail(String cause) {
		throw new AssertionException(cause);
	}

	public static void assertEquals(double a, double b, double threshold) {
		if (Math.abs(a - b) > threshold) {
			throw new AssertionException(format("assertion failed: a > b (a: %f -- b:  %f)", a, b));
		}
	}

	public static void assertEquals(Object a, Object b, String message) {
		if (!a.equals(b)) {
			throw new AssertionException(message);
		}
	}

	public static void assertTrue(boolean condition, String message) {
		if (!condition) {
			throw new AssertionException(message);
		}
	}

	public static void assertTrue(boolean condition) {
		assertTrue(condition, "unexpected condition");
	}

	public static void assertFalse(boolean condition) {
		assertFalse(condition, "unexpected condition");
	}

	public static void assertFalse(boolean condition, String message) {
		if (condition) {
			throw new AssertionException(message);
		}
	}

	public static void assertNull(Object o) {
		if (o != null) {
			throw new AssertionException("expected null");
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T assertInstanceOf(final Object obj, final Class<T> clazz) {
		assertNotNull(clazz);
		assertNotNull(obj);
		assertTrue(clazz.isInstance(obj));
		return null;// (T) obj;
	}
}