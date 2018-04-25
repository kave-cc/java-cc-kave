/**
 * Copyright 2018 University of Zurich
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
package cc.kave.rsse.calls.model.usages.impl;

import cc.kave.commons.assertions.Asserts;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.rsse.calls.model.usages.UsageSiteType;

public class UsageSites {

	public static UsageSite callParameter(String id, int argIndex) {
		Asserts.assertNotNull(id);
		Asserts.assertGreaterOrEqual(argIndex, 0);
		return callParameter(Names.newMethod(id), argIndex);
	}

	public static UsageSite callParameter(IMethodName m, int argIndex) {
		Asserts.assertNotNull(m);
		Asserts.assertGreaterOrEqual(argIndex, 0);
		Asserts.assertGreaterThan(m.getParameters().size(), argIndex);
		UsageSite site = new UsageSite();
		site.type = UsageSiteType.CALL_PARAMETER;
		site.member = m;
		site.argIndex = argIndex;
		return site;
	}

	public static UsageSite call(String id) {
		Asserts.assertNotNull(id);
		return call(Names.newMethod(id));
	}

	public static UsageSite call(IMethodName m) {
		Asserts.assertNotNull(m);
		UsageSite site = new UsageSite();
		site.type = UsageSiteType.CALL_RECEIVER;
		site.member = m;
		return site;
	}

	public static UsageSite memberAccess(IMemberName m) {
		Asserts.assertNotNull(m);
		UsageSite site = new UsageSite();
		site.type = UsageSiteType.MEMBER_ACCESS;
		site.member = m;
		return site;
	}

	public static UsageSite memberAccessToEvent(String id) {
		Asserts.assertNotNull(id);
		return memberAccess(Names.newEvent(id));
	}

	public static UsageSite memberAccessToField(String id) {
		Asserts.assertNotNull(id);
		return memberAccess(Names.newField(id));
	}

	public static UsageSite memberAccessToMethod(String id) {
		Asserts.assertNotNull(id);
		return memberAccess(Names.newMethod(id));
	}

	public static UsageSite memberAccessToProperty(String id) {
		Asserts.assertNotNull(id);
		return memberAccess(Names.newProperty(id));
	}
}