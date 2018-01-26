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

public class CoReFieldName implements ICoReFieldName {
	private static final long serialVersionUID = 5067244907255465328L;

	private static Map<String /* vmTypeName */, CoReFieldName> index = new MapMaker().weakValues().makeMap();

	/**
	 * Format: DeclaringType'.'fieldName;FieldType, i.e.,
	 * &lt;VmTypeName&gt;.&lt;String&gt;;&lt;VmTypeName&gt;
	 * 
	 * @param fieldName
	 * @return
	 */
	public static CoReFieldName get(final String fieldName) {
		// typeName = removeGenerics(typeName);
		CoReFieldName res = index.get(fieldName);
		if (res == null) {
			res = new CoReFieldName(fieldName);
			index.put(fieldName, res);
		}
		return res;
	}

	private String identifier;

	protected CoReFieldName() {
		// no-one should instantiate this class. O
	}

	/**
	 * @see #get(String)
	 */
	protected CoReFieldName(final String vmFieldName) {
		identifier = vmFieldName;
		Asserts.assertNotNull(identifier);
		Asserts.assertNotNull(getDeclaringType());
		Asserts.assertNotNull(getFieldName());
		Asserts.assertNotNull(getFieldType());
	}

	@Override
	public ICoReTypeName getDeclaringType() {
		final String declaringType = StringUtils.substringBeforeLast(identifier, ".");
		return CoReTypeName.get(declaringType);
	}

	@Override
	public String getFieldName() {
		final String fieldName = StringUtils.substringBetween(identifier, ".", ";");
		return fieldName;
	}

	@Override
	public ICoReTypeName getFieldType() {
		final String fieldType = StringUtils.substringAfter(identifier, ";");
		return CoReTypeName.get(fieldType);
	}

	@Override
	public String getIdentifier() {
		return identifier;
	}

	@Override
	public int compareTo(final ICoReFieldName other) {
		return identifier.compareTo(other.getIdentifier());
	}

	@Override
	public String toString() {
		return getIdentifier();
	}
}
