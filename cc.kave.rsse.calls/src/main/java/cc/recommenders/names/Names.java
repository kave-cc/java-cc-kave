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

import static cc.kave.commons.assertions.Throws.throwUnreachable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import cc.kave.commons.assertions.Asserts;
import cc.kave.commons.model.naming.IName;

/**
 * 
 * Contains utility methods for parsing and converting plain vm strings (or
 * their corresponding {@link IName}s respectively) to source strings.
 * 
 */
@Deprecated // should no longer be used. No "primitive types" exist in the new
			// naming scheme.
public class Names {
	public static enum PrimitiveType {
		BOOLEAN('Z', "boolean"), VOID('V', "void"), CHAR('C', "char"), BYTE('B', "byte"), SHORT('S', "short"), INT('I',
				"int"), FLOAT('F', "float"), LONG('J', "long"), DOUBLE('D', "double");
		public static PrimitiveType fromSrc(final String src) {
			Asserts.assertNotNull(src, "src");
			//
			for (final PrimitiveType t : values()) {
				if (t.src.equals(src)) {
					return t;
				}
			}
			return null;
		}

		private final String src;

		private final char vm;

		private PrimitiveType(final char vm, final String src) {
			this.vm = vm;
			this.src = src;
		}

		public String src() {
			return src;
		}

		public char vm() {
			return vm;
		}
	}

	public static int STYLE_SHORT = 0;

	public static int STYLE_LONG = 1;

	public static final String DOUBLE = "double";

	public static final String LONG = "long";

	public static final String FLOAT = "float";

	public static final String INT = "int";

	public static final String SHORT = "short";

	public static final String BYTE = "byte";

	public static final String CHAR = "char";

	public static final String BOOLEAN = "boolean";

	public static final String VOID = "void";

	/**
	 * Given "X.method(LType1;[II)V"
	 * <ol>
	 * <li>return[0] = method name, i.e., "X.method"
	 * <li>return [1..n-2] = parameter types, i.e., ["Type1", "int[]","int"]
	 * <li>return[n-1] = return type, i.e., "void"
	 * </ol>
	 */
	public static String[] parseMethodSignature1(final String methodSignature) {
		Asserts.assertNotNull(methodSignature, "s");
		//
		// this is not high performance code but it might be ok for now.
		// final int lastDot = s.lastIndexOf('.');
		final int openingBracket = methodSignature.indexOf('(');
		final ArrayList<String> res = new ArrayList<String>();
		// res.add(getType(s.substring(0, lastDot).toCharArray(), 0));
		// res.add(s.substring(lastDot + 1, openingBracket));
		// method name
		res.add(methodSignature.substring(0, openingBracket));
		//
		final char[] desc = methodSignature.substring(openingBracket).toCharArray();
		int off = 1;
		while (true) {
			final char car = desc[off];
			if (car == ')') {
				break;
			}
			switch (car) {
			case 'V':
				// not possible - right ? ;)
			case 'Z':
			case 'C':
			case 'B':
			case 'S':
			case 'I':
			case 'F':
			case 'J':
			case 'D':
				res.add(internal_vm2srcTypeName(desc, off++));
				continue;
			case 'L':
				res.add(internal_vm2srcTypeName(desc, off));
				off = internal_findEndOfObjectType(desc, off);
				continue;
			case '[':
				res.add(internal_vm2srcTypeName(desc, off));
				while (desc[off] == '[') {
					off++;
				}
				if (desc[off] == 'L') {
					off = internal_findEndOfObjectType(desc, off);
				} else {
					off++;
				}
			}
		}
		res.add(internal_vm2srcTypeName(desc, off + 1));
		return res.toArray(new String[res.size()]);
	}

	private static int internal_findEndOfObjectType(final char[] desc, final int off) {
		Asserts.assertNotNull(desc, "desc");
		//
		for (int index = off; index < desc.length; index++) {
			if (desc[index] == ';') {
				return index + 1;
			}
		}
		return desc.length;
	}

	private static String internal_vm2srcTypeName(final char[] buf, final int off) {
		Asserts.assertNotNull(buf, "buf");
		//
		int len;
		switch (buf[off]) {
		case 'V':
			return VOID;
		case 'Z':
			return BOOLEAN;
		case 'C':
			return CHAR;
		case 'B':
			return BYTE;
		case 'S':
			return SHORT;
		case 'I':
			return INT;
		case 'F':
			return FLOAT;
		case 'J':
			return LONG;
		case 'D':
			return DOUBLE;
		case '[':
			final StringBuilder sb = new StringBuilder();
			sb.append("[]");
			len = 1;
			while (buf[off + len] == '[') {
				sb.append("[]");
				++len;
			}
			sb.insert(0, internal_vm2srcTypeName(buf, off + len));
			return sb.toString();
		case 'L':
			len = 1;
			while (off + len < buf.length && buf[off + len] != ';') {
				++len;
			}
			final String s1 = new String(buf, off + 1, len - 1).replaceAll("/", ".");
			final String s2 = s1.replaceAll("\\$", ".");
			return s2;
		default:
			throw throwUnreachable("couldn't handle '%s'", buf);
		}
	}

	/**
	 * 
	 * @return new String[] { declaringType, methodNameAndDesciptor };
	 */
	public static String[] parseMethodSignature2(final String vmMethodSignature) {
		final int indexOfDot = vmMethodSignature.indexOf('.');
		final String declaringType = vmMethodSignature.substring(0, indexOfDot);
		final String methodNameAndDesciptor = vmMethodSignature.substring(indexOfDot + 1);
		final String[] res = new String[] { declaringType, methodNameAndDesciptor };
		return res;
	}

	/**
	 * @return new String[] { type, methodName, methodDescriptor }
	 */
	public static String[] parseMethodSignature3(final String vmMethodSignature) {
		final int lastDot = vmMethodSignature.lastIndexOf('.');
		final int firstBracket = vmMethodSignature.lastIndexOf('(');
		final String type = vmMethodSignature.substring(0, lastDot);
		final String methodName = vmMethodSignature.substring(lastDot + 1, firstBracket);
		final String methodDescriptor = vmMethodSignature.substring(firstBracket);
		return new String[] { type, methodName, methodDescriptor };
	}

	public static String src2vmMethod(final String srcDeclaringType, final String methodName,
			final String[] srcParameterTypes, final String srcReturnType) {
		final String vmDeclaringTypeName = src2vmType(srcDeclaringType);
		final String vmMethodName = src2vmMethod(methodName, srcParameterTypes, srcReturnType);
		return vmDeclaringTypeName + "." + vmMethodName;
	}

	public static String src2vmMethod(final String methodName, final String[] srcParameterTypes,
			final String srcReturnType) {
		// TODO this code is incomplete and does not work well with arrays!
		Asserts.assertNotNull(methodName, "methodName");
		Asserts.assertNotNull(srcParameterTypes, "srcParameterTypes");
		Asserts.assertNotNull(srcReturnType, "srcReturnType");
		//
		final StringBuilder sb = new StringBuilder();
		sb.append(methodName).append('(');
		for (final String srcType : srcParameterTypes) {
			final String vmType = src2vmType(srcType);
			sb.append(vmType);
			if (vmType.startsWith("L")) {
				sb.append(';');
			}
		}
		String vmReturnType = src2vmType(srcReturnType);
		if (vmReturnType.startsWith("L")) {
			vmReturnType += ";";
		}
		sb.append(')').append(vmReturnType);
		return sb.toString();
	}

	public static String src2vmType(String type) {
		Asserts.assertNotNull(type, "type");
		//
		final PrimitiveType p = PrimitiveType.fromSrc(type);
		if (p != null) {
			return String.valueOf(p.vm());
		}
		int dimensions = 0;
		if (type.endsWith("]")) {
			dimensions = StringUtils.countMatches(type, "[]");
			type = StringUtils.substringBefore(type, "[") + ";";
		}
		return StringUtils.repeat("[", dimensions) + "L" + type.replaceAll("\\.", "/");
	}

	public static List<String> src2vmType(final String[] srcTypes) {
		Asserts.assertNotNull(srcTypes, "srcTypes");
		//
		final LinkedList<String> res = new LinkedList<String>();
		for (final String srcName : srcTypes) {
			res.add(src2vmType(srcName));
		}
		return res;
	}

	// public static String vm2srcPackage(final IPackageName pkg) {
	// ensureIsNotNull(pkg, "pkg");
	// return pkg.getIdentifier().replace('/', '.');
	// }

	public static String vm2srcQualifiedMethod(final ICoReMethodName method) {
		final StringBuilder sb = new StringBuilder();
		final ICoReTypeName declaringType = method.getDeclaringType();
		sb.append(vm2srcQualifiedType(declaringType));
		sb.append('.');
		sb.append(method.getName());
		sb.append('(');
		for (final ICoReTypeName param : method.getParameterTypes()) {
			sb.append(vm2srcSimpleTypeName(param)).append(", ");
		}
		if (method.hasParameters()) {
			sb.setLength(sb.length() - 2);
		}
		sb.append(')');
		return sb.toString();
	}

	public static String vm2srcQualifiedType(final ICoReTypeName type) {
		if (type.isPrimitiveType()) {
			return Names.vm2srcSimpleTypeName(type);
		}
		if (type.isArrayType()) {
			return vm2srcQualifiedType(type.getArrayBaseType()) + StringUtils.repeat("[]", type.getArrayDimensions());
		}
		String s = type.getIdentifier();
		s = s.replace('/', '.');
		return s.substring(1);
	}

	/**
	 * @return the <b>&lt;method name&gt;(&lt;parameter simple types...&gt;)</b>
	 *         - no return value.
	 */
	public static String vm2srcSimpleMethod(final ICoReMethodName name) {
		Asserts.assertNotNull(name, "name");
		final StringBuilder sb = new StringBuilder();
		if (name.getName().equals("<subtype-init>")) {
			sb.append("ConstructorCallFromSubtype");
		} else {
			sb.append(name.isInit() ? vm2srcSimpleTypeName(name.getDeclaringType()) : name.getName());
		}
		//
		sb.append('(');
		for (final ICoReTypeName param : name.getParameterTypes()) {
			sb.append(vm2srcSimpleTypeName(param)).append(", ");
		}
		if (name.getParameterTypes().length > 0) {
			sb.delete(sb.length() - 2, sb.length());
		}
		//
		//
		sb.append(')');
		return sb.toString();
	}

	public static String vm2srcSimpleTypeName(final String CoReTypeName) {
		Asserts.assertNotNull(CoReTypeName, "CoReTypeName");
		//
		final String type = internal_vm2srcTypeName(CoReTypeName.toCharArray(), 0);
		final int lastDot = type.lastIndexOf('.');
		if (lastDot == -1) {
			return type;
		} else {
			return type.substring(lastDot + 1);
		}
	}

	public static String vm2srcSimpleTypeName(final ICoReTypeName type) {
		if (type.isArrayType()) {
			final int arrayDimensions = type.getArrayDimensions();
			final ICoReTypeName arrayBaseType = type.getArrayBaseType();
			final StringBuilder sb = new StringBuilder();
			final String simpleBaseType = vm2srcSimpleTypeName(arrayBaseType);
			sb.append(simpleBaseType);
			for (int i = arrayDimensions; i-- > 0;) {
				sb.append("[]");
			}
			return sb.toString();
		}
		if (type.isPrimitiveType()) {
			if (type == CoReTypeName.BOOLEAN) {
				return BOOLEAN;
			} else if (type == CoReTypeName.BYTE) {
				return BYTE;
			} else if (type == CoReTypeName.CHAR) {
				return CHAR;
			} else if (type == CoReTypeName.DOUBLE) {
				return DOUBLE;
			} else if (type == CoReTypeName.FLOAT) {
				return FLOAT;
			} else if (type == CoReTypeName.INT) {
				return INT;
			} else if (type == CoReTypeName.LONG) {
				return LONG;
			} else if (type == CoReTypeName.VOID) {
				return VOID;
			} else if (type == CoReTypeName.SHORT) {
				return SHORT;
			}
		}
		return type.getClassName();
	}

	/**
	 * converts:
	 * <ul>
	 * <li>Ljava/lang/String --&gt; java.lang.String
	 * <li>I --&gt; int
	 * </ul>
	 */
	public static String vm2srcTypeName(final String CoReTypeName) {
		Asserts.assertNotNull(CoReTypeName, "CoReTypeName");
		//
		return internal_vm2srcTypeName(CoReTypeName.toCharArray(), 0);
	}

	public static ICoReTypeName java2vmType(final Class<?> clazz) {
		final String vmName = src2vmType(clazz.getName());
		return CoReTypeName.get(vmName);
	}
}
