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
import cc.kave.rsse.calls.model.usages.MemberAccessType;

public class MemberAccesses {

	public static MemberAccess methodCall(String id) {
		Asserts.assertNotNull(id);
		return methodCall(Names.newMethod(id));
	}

	public static MemberAccess methodCall(IMethodName m) {
		Asserts.assertNotNull(m);
		MemberAccess site = new MemberAccess();
		site.type = MemberAccessType.METHOD_CALL;
		site.member = m;
		return site;
	}

	public static MemberAccess memberRef(IMemberName m) {
		Asserts.assertNotNull(m);
		MemberAccess site = new MemberAccess();
		site.type = MemberAccessType.MEMBER_REFERENCE;
		site.member = m;
		return site;
	}

	public static MemberAccess memberRefToEvent(String id) {
		Asserts.assertNotNull(id);
		return memberRef(Names.newEvent(id));
	}

	public static MemberAccess memberRefToField(String id) {
		Asserts.assertNotNull(id);
		return memberRef(Names.newField(id));
	}

	public static MemberAccess memberRefToMethod(String id) {
		Asserts.assertNotNull(id);
		return memberRef(Names.newMethod(id));
	}

	public static MemberAccess memberRefToProperty(String id) {
		Asserts.assertNotNull(id);
		return memberRef(Names.newProperty(id));
	}
}