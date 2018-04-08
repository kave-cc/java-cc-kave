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
package cc.kave.commons.model.naming.impl.v0;

import org.junit.Assert;
import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;

public class NamingUtilsTest {

	@Test
	public void toValueType_Func1() {
		String m = "[RT, P] [DT, P].m()";
		String vt = "d:[TResult] [System.Func`1[[TResult -> RT, P]], mscorlib, 4.0.0.0].()";
		assertMethodValueType(m, vt);
	}

	@Test
	public void toValueType_Func2() {
		String m = "[RT, P] [DT, P].m([TA, P] p1)";
		String vt = "d:[TResult] [System.Func`2[[T -> TA, P],[TResult -> RT, P]], mscorlib, 4.0.0.0].([T] arg)";
		assertMethodValueType(m, vt);
	}

	@Test
	public void toValueType_Func3() {
		String m = "[RT, P] [DT, P].m([TA, P] p1, [TB, P] p2)";
		String vt = "d:[TResult] [System.Func`3[[T1 -> TA, P],[T2 -> TB, P],[TResult -> RT, P]], mscorlib, 4.0.0.0].([T1] arg1, [T2] arg2)";
		assertMethodValueType(m, vt);
	}

	@Test
	public void toValueType_Action0() {
		String m = "[p:void] [DT, P].m()";
		String vt = "d:[p:void] [System.Action, mscorlib, 4.0.0.0].()";
		assertMethodValueType(m, vt);

	}

	@Test
	public void toValueType_Action1() {
		String m = "[p:void] [DT, P].m([TA, P] p1)";
		String vt = "d:[p:void] [System.Action`1[[T -> TA, P]], mscorlib, 4.0.0.0].([T] obj)";
		assertMethodValueType(m, vt);

	}

	@Test
	public void toValueType_Action2() {
		String m = "[p:void] [DT, P].m([TA, P] p1, [TB, P] p2)";
		String vt = "d:[p:void] [System.Action`2[[T1 -> TA, P],[T2 -> TB, P]], mscorlib, 4.0.0.0].([T1] arg1, [T2] arg2)";
		assertMethodValueType(m, vt);

	}

	@Test
	public void toValueType_Action2_NoBindings() {
		String m = "[p:void] [DT, P].m([TA, P] p1, [TB, P] p2)";
		String vt = "d:[p:void] [System.Action`2[[T1],[T2]], mscorlib, 4.0.0.0].([T1] arg1, [T2] arg2)";

		IMethodName in = Names.newMethod(m);
		ITypeName actual = NameUtils.toValueType(in, false);
		ITypeName expected = Names.newType(vt);
		Assert.assertEquals(expected, actual);
	}

	private static void assertMethodValueType(String m, String vt) {
		IMethodName in = Names.newMethod(m);
		ITypeName actual = NameUtils.toValueType(in, true);
		ITypeName expected = Names.newType(vt);
		Assert.assertEquals(expected, actual);
	}
}