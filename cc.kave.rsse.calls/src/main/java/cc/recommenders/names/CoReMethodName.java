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

import static cc.kave.commons.assertions.Throws.newIllegalArgumentException;
import static cc.kave.commons.assertions.Throws.throwNotImplemented;
import static cc.recommenders.names.CoReTypeName.BOOLEAN;
import static cc.recommenders.names.CoReTypeName.BYTE;
import static cc.recommenders.names.CoReTypeName.CHAR;
import static cc.recommenders.names.CoReTypeName.DOUBLE;
import static cc.recommenders.names.CoReTypeName.FLOAT;
import static cc.recommenders.names.CoReTypeName.INT;
import static cc.recommenders.names.CoReTypeName.LONG;
import static cc.recommenders.names.CoReTypeName.SHORT;
import static cc.recommenders.names.CoReTypeName.VOID;

import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.MapMaker;

import cc.kave.commons.assertions.Asserts;

public class CoReMethodName implements ICoReMethodName {
	private static final long serialVersionUID = 688964238062226061L;

	private static Map<String /* name */, CoReMethodName> index = new MapMaker().weakValues().makeMap();

	public static CoReMethodName get(final String vmFullQualifiedTypeName, final String vmMethodSignature) {
		return get(vmFullQualifiedTypeName + "." + vmMethodSignature);
	}

	/**
	 * Creates a new {@link CoReMethodName} from the given method argument but
	 * replaces the declaring type by the given new base type.
	 * <p>
	 * Example: vmMethodName = "Ljava/lang/String.wait()V", vmBaseTypeName =
	 * "Ljava/lang/Object" --&gt; res = "Ljava/lang/Object.wait()".
	 * 
	 * @param vmBaseTypeName
	 * @param vmMethodName
	 * @return
	 */
	public static CoReMethodName rebase(final ICoReTypeName vmBaseTypeName, final ICoReMethodName vmMethodName) {
		Asserts.assertInstanceOf(vmBaseTypeName, CoReTypeName.class);
		Asserts.assertInstanceOf(vmMethodName, CoReMethodName.class);
		return get(vmBaseTypeName.getIdentifier(), vmMethodName.getSignature());
	}

	public static synchronized CoReMethodName get(final String vmFullQualifiedMethodName) {
		CoReMethodName res = index.get(vmFullQualifiedMethodName);
		if (res == null) {
			if (vmFullQualifiedMethodName.startsWith("< ")) {
				throw newIllegalArgumentException("invalid input: %s", vmFullQualifiedMethodName);
			}
			res = new CoReMethodName(vmFullQualifiedMethodName);
			index.put(vmFullQualifiedMethodName, res);
		}
		return res;
	}

	public static final ICoReMethodName NULL = CoReMethodName.get("L_null.null()V");

	// public static String removeGenerics(final String typeName) {
	// return StringUtils.substringBefore(typeName, "<");
	// }
	private String identifier;

	protected CoReMethodName() {
		// no-one should instantiate this class. But maybe we need subclasses
		// later...
	}

	/**
	 * @see #get(String)
	 */
	protected CoReMethodName(final String vmFullQualifiedMethodName) {
		identifier = vmFullQualifiedMethodName;
		// // perform syntax check by creating every possible element from this
		// string. If no exception is thrown everything should be ok...
		getDeclaringType();
		getParameterTypes();
		getReturnType();
	}

	@Override
	public ICoReTypeName getDeclaringType() {
		final int bracket = identifier.lastIndexOf('(');
		final int methodSeperator = identifier.lastIndexOf('.', bracket);
		return CoReTypeName.get(identifier.substring(0, methodSeperator));
	}

	@Override
	public String getDescriptor() {
		final int bracket = identifier.lastIndexOf('(');
		return identifier.substring(bracket);
	}

	@Override
	public String getIdentifier() {
		return identifier;
	}

	@Override
	public String getName() {
		final int methodSeperator = identifier.lastIndexOf('.');
		final int argumentsSeperator = identifier.lastIndexOf('(');
		return identifier.substring(methodSeperator + 1, argumentsSeperator);
	}

	@Override
	public ICoReTypeName[] getParameterTypes() {
		final ArrayList<CoReTypeName> argTypes = new ArrayList<CoReTypeName>();
		final int openingBracket = identifier.lastIndexOf('(');
		final char[] desc = identifier.substring(openingBracket + 1).toCharArray();
		int off = 0;
		while (true) {
			if (desc[off] == ')') {
				break;
			}
			switch (desc[off]) {
			case 'V':
				argTypes.add(VOID);
				break;
			case 'Z':
				argTypes.add(BOOLEAN);
				break;
			case 'C':
				argTypes.add(CHAR);
				break;
			case 'B':
				argTypes.add(BYTE);
				break;
			case 'S':
				argTypes.add(SHORT);
				break;
			case 'I':
				argTypes.add(INT);
				break;
			case 'F':
				argTypes.add(FLOAT);
				break;
			case 'J':
				argTypes.add(LONG);
				break;
			case 'D':
				argTypes.add(DOUBLE);
				break;
			case 'L': {
				final int start = off;
				do {
					off++;
					// TODO Marcel: Generics 'handling' is a bit strange... need
					// to fix that here when fully supporting generics later on.
					if (desc[off] == '<') {
						off++;
						int numberOfOpenGenerics = 1;
						while (numberOfOpenGenerics != 0) {
							switch (desc[off]) {
							case '>':
								numberOfOpenGenerics -= 1;
								break;
							case '<':
								numberOfOpenGenerics += 1;
								break;
							}
							off++;
						}
					}
				} while (desc[off] != ';');
				// off points to the ';' now
				final String argumentTypeName = new String(desc, start, off - start);
				argTypes.add(CoReTypeName.get(argumentTypeName));
				break;
			}
			case '[': {
				final int start = off;
				off++;
				while (desc[off] == '[') {
					// do we have an array? -> increase offset if we have
					// multidimensional arrays:
					// jump over all array counters
					off++;
				}
				// now, off is guaranteed to point to the first letter of the
				// type: either 'L' or a primitive letter.
				// if we have an object type:
				if (desc[off] == 'L') {
					off++;
					while (desc[off] != ';') {
						// go forward until the next semicolon
						off++;
					}
					// off points directly on the ';' Thus
					final String typeName = new String(desc, start, off - start);
					argTypes.add(CoReTypeName.get(typeName));
				} else {
					// if it is not a declared type, off points directly on the
					// primitive letter
					final String typeName = new String(desc, start, off + 1 - start);
					argTypes.add(CoReTypeName.get(typeName));
				}
				break;
			}
			}
			off++;
		}
		return argTypes.toArray(new CoReTypeName[argTypes.size()]);
	}

	@Override
	public ICoReTypeName getReturnType() {
		String returnType = StringUtils.substringAfterLast(identifier, ")");
		// strip off throws type from method return
		returnType = StringUtils.substringBefore(returnType, "|");
		if (!returnType.endsWith(";")) {

			// be sure that if it does not end with a ';' is MUST be a primitive
			// or an array of primitives:
			final ICoReTypeName res = CoReTypeName.get(returnType);
			Asserts.assertTrue(res.isPrimitiveType() || res.isArrayType() && res.getArrayBaseType().isPrimitiveType());
			return res;
		} else {
			returnType = StringUtils.substring(returnType, 0, -1);
			return CoReTypeName.get(returnType);
		}
	}

	@Override
	public String getSignature() {
		final int methodSeparator = identifier.lastIndexOf('.');
		return identifier.substring(methodSeparator + 1);
	}

	@Override
	public boolean isInit() {
		final String name = getName();
		return "<init>".equals(name) || "<subtype-init>".equals(name);
	}

	@Override
	public boolean isStaticInit() {
		return "<clinit>".equals(getName());
	}

	@Override
	public boolean isSynthetic() {
		return getName().contains("$");
	}

	@Override
	public boolean similar(final ICoReMethodName other) {
		throw throwNotImplemented();
	}

	@Override
	public String toString() {
		return getIdentifier();
	}

	@Override
	public boolean isVoid() {
		return getReturnType().isVoid();
	}

	@Override
	public int compareTo(final ICoReMethodName o) {
		return getIdentifier().compareTo(o.getIdentifier());
	}

	@Override
	public boolean hasParameters() {
		return getParameterTypes().length > 0;
	}
}
