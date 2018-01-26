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

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.MapMaker;

import cc.kave.commons.assertions.Asserts;

public class CoReVariableName implements ICoReVariableName {

	private static final long serialVersionUID = 5067244907255465328L;

	private static Map<String /* vmTypeName */, CoReVariableName> index = new MapMaker().weakValues().makeMap();

	/**
	 * Format: DeclaringType'.'fieldName;FieldType, i.e.,
	 * &lt;VmTypeName&gt;.&lt;String&gt;;&lt;VmTypeName&gt;
	 * 
	 * @param variableName
	 * @return
	 */
	public static CoReVariableName get(final String variableName) {

		CoReVariableName res = index.get(variableName);
		if (res == null) {
			res = new CoReVariableName(variableName);
			index.put(variableName, res);
		}
		return res;
	}

	private String identifier;

	protected CoReVariableName() {
		// no-one should instantiate this class. O
	}

	/**
	 * @see #get(String)
	 */
	protected CoReVariableName(final String vmVariableName) {
		identifier = vmVariableName;
		Asserts.assertNotNull(identifier);
		Asserts.assertNotNull(getDeclaringMethod());

	}

	@Override
	public String getName() {
		return StringUtils.substringAfterLast(identifier, "#");
	}

	@Override
	public ICoReMethodName getDeclaringMethod() {
		final String declaringType = StringUtils.substringBeforeLast(identifier, "#");
		return CoReMethodName.get(declaringType);
	}

	@Override
	public String getIdentifier() {
		return identifier;
	}

	@Override
	public int compareTo(final ICoReVariableName other) {
		return identifier.compareTo(other.getIdentifier());
	}
}
