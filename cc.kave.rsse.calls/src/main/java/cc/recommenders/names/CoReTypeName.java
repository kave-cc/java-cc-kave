/**
 * Copyright (c) 2010 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Marcel Bruch - initial API and implementation.
 */
package cc.recommenders.names;

import static cc.kave.commons.assertions.Throws.throwIllegalArgumentException;
import static cc.kave.commons.assertions.Throws.throwUnreachable;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.MapMaker;

import cc.kave.commons.assertions.Asserts;

public class CoReTypeName implements ICoReTypeName {
	private static Map<String /* vmTypeName */, CoReTypeName> index = new MapMaker().weakValues().makeMap();

	public static final CoReTypeName OBJECT = CoReTypeName.get("Ljava/lang/Object");

	public static final CoReTypeName JavaLangNullPointerException = CoReTypeName.get("Ljava/lang/NullPointerException");

	public static final CoReTypeName JavaLangOutOfMemoryError = CoReTypeName.get("Ljava/lang/OutOfMemoryError");

	public static final CoReTypeName JavaLangString = CoReTypeName.get("Ljava/lang/String");

	public static final CoReTypeName JavaLangExceptionInInitializerError = CoReTypeName
			.get("Ljava/lang/ExceptionInInitializerError");

	public static final CoReTypeName STRING = CoReTypeName.get("Ljava/lang/String");

	public static final CoReTypeName NULL = get("Lnull");

	public static final CoReTypeName BYTE = get("B");

	public static final CoReTypeName BOOLEAN = get("Z");

	public static final CoReTypeName CHAR = get("C");

	public static final CoReTypeName DOUBLE = get("D");

	public static final CoReTypeName FLOAT = get("F");

	public static final CoReTypeName INT = get("I");

	public static final CoReTypeName LONG = get("J");

	public static final CoReTypeName SHORT = get("S");

	public static final CoReTypeName VOID = get("V");

	public static CoReTypeName get(String typeName) {
		typeName = removeGenerics(typeName);
		CoReTypeName res = index.get(typeName);
		if (res == null) {
			res = new CoReTypeName(typeName);
			index.put(typeName, res);
		}
		return res;
	}

	private static String removeGenerics(final String typeName) {
		return StringUtils.substringBefore(typeName, "<");
	}

	private String identifier;

	protected CoReTypeName() {
		// no-one should instantiate this class. O
	}

	/**
	 * @see #get(String)
	 */
	protected CoReTypeName(final String vmTypeName) {
		Asserts.assertNotNull(vmTypeName);
		Asserts.assertFalse(vmTypeName.length() == 0, "empty size for type name not permitted");
		if (vmTypeName.length() == 1) {
			switch (vmTypeName.charAt(0)) {
			case 'B':
			case 'C':
			case 'D':
			case 'F':
			case 'I':
			case 'J':
			case 'S':
			case 'V':
			case 'Z':
				break;
			default:
				throwUnreachable("Invalid type name: " + vmTypeName);
			}
		} else {
			switch (vmTypeName.charAt(0)) {
			case '[':
			case 'L':
				break;
			default:
				throwUnreachable("Invalid type name: " + vmTypeName);
			}
		}

		int off = 0;
		while (off < vmTypeName.length()) {
			final char c = vmTypeName.charAt(off);
			if (c == '[' || c == '/' || c == '-'/* as in 'package-info.class' */ || c == '<' || c == '>'
					|| Character.isJavaIdentifierPart(c)) {
				off++;
				continue;
			}
			throwIllegalArgumentException("Cannot parse '%s' as vm type name.", vmTypeName);
			break;
		}
		identifier = vmTypeName;
	}

	@Override
	public ICoReTypeName getArrayBaseType() {
		Asserts.assertTrue(isArrayType(), "only array-types have a base type!");
		int start = 0;
		while (identifier.charAt(++start) == '[') {
			// start counter gets increased
		}
		return get(identifier.substring(start));
	}

	@Override
	public int getArrayDimensions() {
		int count = 0;
		int start = 0;
		while (identifier.charAt(start++) == '[') {
			count++;
		}
		return count;
	}

	@Override
	public String getClassName() {
		final int indexOf = identifier.lastIndexOf('/');
		if (indexOf < 0 && !isPrimitiveType()) {
			return identifier.substring(1);
		}
		final String classname = identifier.substring(indexOf + 1);
		return classname;
	}

	@Override
	public String getIdentifier() {
		return identifier;
	}

	@Override
	public ICoRePackageName getPackage() {
		final int lastSlash = identifier.lastIndexOf('/');
		if (lastSlash == -1 || identifier.charAt(0) == '[') {
			return CoRePackageName.DEFAULT_PACKAGE;
		}
		return CoRePackageName.get(identifier.substring(1, lastSlash));
	}

	@Override
	public boolean isAnonymousType() {
		return identifier.matches(".*\\$\\d+");
	}

	@Override
	public boolean isArrayType() {
		return identifier.charAt(0) == '[';
	}

	@Override
	public boolean isDeclaredType() {
		return identifier.charAt(0) == 'L';
	}

	@Override
	public boolean isNestedType() {
		return identifier.contains("$");
	}

	@Override
	public boolean isPrimitiveType() {
		return !(isArrayType() || isDeclaredType());
	}

	@Override
	public boolean isVoid() {
		return this == VOID;
	}

	@Override
	public int compareTo(final ICoReTypeName o) {
		return getIdentifier().compareTo(o.getIdentifier());
	}

	@Override
	public String toString() {
		return getIdentifier();
	}

	@Override
	public ICoReMethodName getDeclaringMethod() {
		Asserts.assertTrue(isNestedType(), "only valid on nested types");
		final int lastPathSegmentSeparator = identifier.lastIndexOf('/');
		final String path = identifier.substring(0, lastPathSegmentSeparator);
		final int bracket = path.lastIndexOf('(');
		final int methodSeparator = path.lastIndexOf('/', bracket);
		final String newFQName = path.substring(0, methodSeparator) + "." + path.substring(methodSeparator + 1);
		return CoReMethodName.get(newFQName);
	}

	@Override
	public ICoReTypeName getDeclaringType() {
		Asserts.assertTrue(isNestedType(), "only valid on nested types");
		final int lastIndexOf = identifier.lastIndexOf('$');
		final String declaringTypeName = identifier.substring(0, lastIndexOf);
		return get(declaringTypeName);
	}
}
