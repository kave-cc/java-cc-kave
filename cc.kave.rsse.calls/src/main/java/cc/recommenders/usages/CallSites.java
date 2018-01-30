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
package cc.recommenders.usages;

import cc.kave.commons.assertions.Asserts;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;

public class CallSites {

	public static CallSite createParameterCallSite(String methodName, int argIndex) {
		Asserts.assertNotNull(methodName);
		Asserts.assertGreaterOrEqual(argIndex, 0);
		return createParameterCallSite(Names.newMethod(methodName), argIndex);
	}

	public static CallSite createParameterCallSite(IMethodName method, int argIndex) {
		Asserts.assertNotNull(method);
		Asserts.assertGreaterOrEqual(argIndex, 0);
		CallSite site = new CallSite();
		site.setKind(CallSiteKind.PARAMETER);
		site.setMethod(method);
		site.setArgIndex(argIndex);
		return site;
	}

	public static CallSite createReceiverCallSite(String methodName) {
		Asserts.assertNotNull(methodName);
		return createReceiverCallSite(Names.newMethod(methodName));
	}

	public static CallSite createReceiverCallSite(IMethodName method) {
		Asserts.assertNotNull(method);
		CallSite site = new CallSite();
		site.setKind(CallSiteKind.RECEIVER);
		site.setMethod(method);
		return site;
	}
}