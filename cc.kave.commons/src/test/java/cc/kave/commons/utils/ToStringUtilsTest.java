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
package cc.kave.commons.utils;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.junit.Test;

@SuppressWarnings("unused")
public class ToStringUtilsTest {

	@Test
	public void primitiveBoolean() {
		assertEquals("false", ToStringUtils.toString(false));
	}

	@Test
	public void primitiveInt() {
		assertEquals("123", ToStringUtils.toString((int) 123));
	}

	@Test
	public void primitiveLong() {
		assertEquals("123", ToStringUtils.toString((long) 123));
	}

	@Test
	public void primitiveFloat() {
		assertEquals("0.123", ToStringUtils.toString((float) 0.123));
	}

	@Test
	public void primitiveDouble() {
		assertEquals("0.123", ToStringUtils.toString((double) 0.123));
	}

	@Test
	public void primitiveChar() {
		assertEquals("'a'", ToStringUtils.toString('a'));
	}

	@Test
	public void primitiveString() {
		assertEquals("\"a\"", ToStringUtils.toString("a"));
	}

	@Test
	public void primitiveStringWithQuotes() {
		assertEquals("\"a\\\"b\"", ToStringUtils.toString("a\"b"));
	}

	@Test
	public void objNull() {
		assertObj("null", null);
	}

	@Test
	public void objA() {
		assertObj("A@666 {\n   f = 1,\n   f2 = 2,\n   f3 = 3,\n}", new A());
	}

	@Test
	public void objSimpleNesting() {

		N b = new N();
		b.hc = 2;
		N a = new N();
		a.hc = 1;
		a.o = b;
		assertObj("N@1 {\n   hc = 1,\n   o = N@2 {\n      hc = 2,\n      o = null,\n   },\n}", a);
	}

	@Test
	public void objRecursion() {

		N a = new N();
		a.hc = 1;
		a.o = a;
		assertObj("N@1 {\n   hc = 1,\n   o = {--> N@1},\n}", a);
	}

	@Test
	public void objIterables() {

		I a = new I();
		a.hc = 1;
		a.a = new MySet<String>(2, "a", "b");
		a.b = new MyList<String>(3, "a", "b", "c");
		assertObj(
				"I@1 {\n" + //
						"   hc = 1,\n" + //
						"   a = MySet@2 [\n" + //
						"      \"a\",\n" + //
						"      \"b\",\n" + //
						"   ],\n" + //
						"   b = MyList@3 [\n" + //
						"      \"a\",\n" + //
						"      \"b\",\n" + //
						"      \"c\",\n" + //
						"   ],\n" + //
						"}", //
				a);
	}

	@Test
	public void objNestedIterables() {
		Object o = new MySet<Set<String>>(1, new MySet<String>(2, "a", "b"));
		assertObj("MySet@1 [\n   MySet@2 [\n      \"a\",\n      \"b\",\n   ],\n]", o);
	}

	@Test
	public void objStaticFields() {
		S s = new S();
		s.hc = 5;
		S.s = 6;
		assertObj("S@5 {\n   hc = 5,\n   static s = 6,\n}", s);
	}

	@Test
	public void objExtensionHierarchy() {
		H s = new H();
		assertObj("H@123 {\n   f = 1,\n   H2.f = 2,\n   g = 3,\n}", s);
	}

	@Test
	public void objStopsAtCustomToStringMethods() {
		N n = new N();
		n.hc = 12;
		n.o = new T();

		assertObj("N@12 {\n   hc = 12,\n   o = [\n   --my custom to string\n   ],\n}", n);
	}

	private static void assertObj(String expected, Object o) {
		String actual = ToStringUtils.toString(o);
		assertEquals(expected, actual);
	}

	private static class A {
		public int f = 1;
		protected int f2 = 2;
		private int f3 = 3;

		@Override
		public int hashCode() {
			return 666;
		}
	}

	private static class N {
		public int hc;
		public Object o;

		@Override
		public int hashCode() {
			return hc;
		}
	}

	private static class I {
		public int hc;
		public Object a;
		public Object b;

		@Override
		public int hashCode() {
			return hc;
		}
	}

	private static class S {
		public int hc;
		public static Object s;

		@Override
		public int hashCode() {
			return hc;
		}
	}

	private static class H extends H2 {
		public int f = 1;

		@Override
		public int hashCode() {
			return 123;
		}
	}

	private static class H2 {
		public int f = 2;
		public int g = 3;
	}

	private static class T {
		@Override
		public String toString() {
			return "[\n--my custom to string\n]";
		}
	}

	private static class MySet<U> extends HashSet<U> {
		private static final long serialVersionUID = 1L;
		private int hc;

		@SafeVarargs
		public MySet(int hc, U... us) {
			this.hc = hc;
			for (U u : us) {
				add(u);
			}
		}

		@Override
		public int hashCode() {
			return hc;
		}
	}

	private static class MyList<U> extends LinkedList<U> {
		private static final long serialVersionUID = 1L;
		private int hc;

		@SafeVarargs
		public MyList(int hc, U... us) {
			this.hc = hc;
			for (U u : us) {
				add(u);
			}
		}

		@Override
		public int hashCode() {
			return hc;
		}
	}
}