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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.Set;

import com.google.common.collect.Sets;

public class ToStringUtils {
	public static String toString(boolean b) {
		return Boolean.toString(b);
	}

	public static String toString(int i) {
		return Integer.toString(i);
	}

	public static String toString(long l) {
		return Long.toString(l);
	}

	public static String toString(float f) {
		return Float.toString(f);
	}

	public static String toString(double d) {
		return Double.toString(d);
	}

	public static String toString(char c) {
		return String.format("'%c'", c);
	}

	public static String toString(String s) {
		return String.format("\"%s\"", s.replaceAll("\\\"", "\\\\\""));
	}

	public static String toString(Object o) {
		try {
			StringBuilder sb = new StringBuilder();
			toString(0, sb, o, Sets.newHashSet(o));
			return sb.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void toString(int depth, StringBuilder sb, Object o, Set<Object> visited)
			throws IllegalArgumentException, IllegalAccessException {

		if (handlePrimitives(o, sb)) {
			return;
		}

		visited.add(o);

		if (handleIterables(depth, o, sb, visited)) {
			return;
		}

		appendTypeAndHash(sb, o);

		sb.append(" {\n");

		Set<String> fieldNames = Sets.newHashSet();
		Class<? extends Object> c = o.getClass();
		while (c != null) {
			for (Field f : c.getDeclaredFields()) {
				indent(depth + 1, sb);
				boolean isStatic = Modifier.isStatic(f.getModifiers());
				if (isStatic) {
					sb.append("static ");
				}
				String fieldName = f.getName();
				if (fieldNames.contains(fieldName)) {
					fieldName = c.getSimpleName() + '.' + fieldName;
				}
				fieldNames.add(fieldName);

				sb.append(fieldName);
				sb.append(" = ");

				Object val = getValue(o, f);
				appendRefOrStepDown(depth, val, sb, visited);
				sb.append(",\n");
			}
			c = c.getSuperclass();
		}

		indent(depth, sb);
		sb.append("}");
	}

	private static void appendTypeAndHash(StringBuilder sb, Object o) {
		sb.append(o.getClass().getSimpleName());
		sb.append('@');
		sb.append(o.hashCode());
	}

	private static Object getValue(Object o, Field f) throws IllegalAccessException {
		Object val;
		if (!f.isAccessible()) {
			f.setAccessible(true);
			val = f.get(o);
			f.setAccessible(false);
		} else {
			val = f.get(o);
		}
		return val;
	}

	private static boolean hasCustomToString(Class<? extends Object> c) throws SecurityException {

		if (isBuiltInClass(c)) {
			return false;
		}

		Method m;
		try {
			m = c.getMethod("toString");
		} catch (NoSuchMethodException e) {
			m = null;
		}

		return !isBuiltInClass(m.getDeclaringClass());
	}

	private static boolean isBuiltInClass(Class<? extends Object> c) {
		return Object.class.equals(c) || Boolean.class.equals(c) || Integer.class.equals(c) || Long.class.equals(c)
				|| Float.class.equals(c) || Double.class.equals(c) || Character.class.equals(c)
				|| String.class.equals(c) || AbstractCollection.class.equals(c);
	}

	private static boolean handlePrimitives(Object o, StringBuilder sb) {

		if (o == null) {
			sb.append("null");
			return true;
		}

		if (o instanceof Boolean) {
			sb.append(toString((boolean) o));
			return true;
		}

		if (o instanceof Integer) {
			int i = (int) o;
			sb.append(toString(i));
			return true;
		}

		if (o instanceof Long) {
			long l = (long) o;
			sb.append(toString(l));
			return true;
		}

		if (o instanceof Float) {
			float f = (float) o;
			sb.append(toString(f));
			return true;
		}

		if (o instanceof Double) {
			double d = (double) o;
			sb.append(toString(d));
			return true;
		}

		if (o instanceof Character) {
			char c = (char) o;
			sb.append(toString(c));
			return true;
		}

		if (o instanceof String) {
			String s = (String) o;
			sb.append(toString(s));
			return true;
		}

		return false;
	}

	@SuppressWarnings("rawtypes")
	private static boolean handleIterables(int depth, Object o, StringBuilder sb, Set<Object> visited)
			throws IllegalArgumentException, IllegalAccessException {
		if (!(o instanceof Iterable)) {
			return false;
		}

		Iterator it = ((Iterable) o).iterator();

		appendTypeAndHash(sb, o);

		sb.append(" [\n");
		while (it.hasNext()) {
			Object next = it.next();
			indent(depth + 1, sb);
			appendRefOrStepDown(depth, next, sb, visited);
			sb.append(",\n");
		}
		indent(depth, sb);
		sb.append("]");

		return true;
	}

	private static void appendRefOrStepDown(int depth, Object o, StringBuilder sb, Set<Object> visited)
			throws IllegalArgumentException, IllegalAccessException {

		if (handlePrimitives(o, sb)) {
			return;
		}

		boolean hasCustomToString = hasCustomToString(o.getClass());
		if (hasCustomToString) {
			sb.append(indent(depth + 1, o.toString()));
			return;
		}

		if (visited.contains(o)) {
			sb.append(String.format("{--> %s@%d}", o.getClass().getSimpleName(), o.hashCode()));
		} else {
			visited.add(o);
			toString(depth + 1, sb, o, visited);
		}
	}

	private static String indent(int depth, String string) {
		StringBuilder sb = new StringBuilder();
		indent(depth, sb);
		String indent = sb.toString();
		String out = string.replaceAll("\n", "\n" + indent);
		return out;
	}

	private static void indent(int depth, StringBuilder sb) {
		for (int i = 0; i < depth; i++) {
			sb.append("   ");
		}
	}
}