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
package cc.kave.rsse.calls.usages;

import cc.kave.commons.assertions.Asserts;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;

public class UsageAccesses {

	public static UsageAccess createCallParameter(String methodName, int argIndex) {
		Asserts.assertNotNull(methodName);
		Asserts.assertGreaterOrEqual(argIndex, 0);
		return createCallParameter(Names.newMethod(methodName), argIndex);
	}

	public static UsageAccess createCallParameter(IMethodName method, int argIndex) {
		Asserts.assertNotNull(method);
		Asserts.assertGreaterOrEqual(argIndex, 0);
		UsageAccess site = new UsageAccess();
		site.setKind(UsageAccessType.CALL_PARAMETER);
		site.setMethod(method);
		site.setArgIndex(argIndex);
		return site;
	}

	public static UsageAccess createCallReceiver(String methodName) {
		Asserts.assertNotNull(methodName);
		return createCallReceiver(Names.newMethod(methodName));
	}

	public static UsageAccess createCallReceiver(IMethodName method) {
		Asserts.assertNotNull(method);
		UsageAccess site = new UsageAccess();
		site.setKind(UsageAccessType.CALL_RECEIVER);
		site.setMethod(method);
		return site;
	}

	public static UsageAccess createFieldAccess(IFieldName f) {
		Asserts.fail("implementMe");
		Asserts.assertNotNull(f);
		UsageAccess site = new UsageAccess();
		site.setKind(UsageAccessType.FIELD_ACCESS);
		site.setField(f);
		return site;
	}

	public static UsageAccess createPropertyAccess(IPropertyName p) {
		Asserts.fail("implementMe");
		Asserts.assertNotNull(p);
		UsageAccess site = new UsageAccess();
		site.setKind(UsageAccessType.PROPERTY_ACCESS);
		site.setProperty(p);
		return site;
	}
}