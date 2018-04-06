/**
 * Copyright 2016 Technische Universität Darmstadt
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
package cc.kave.commons.model.naming.impl.v0.codeelements;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.types.ITypeParameterName;

/* Please note: the primary test source for member names should be the generated
 * test suite. If you encounter a bug, please fix it on the C# side first, then
 * regenerate the test suite to create a failing test, and only then fix the
 * bug in the Java implementation of the names.
 * 
 * The sole purpose of this class is to test the "infrastructure", including
 * Java specific checks, implementation details, instantiation checks, or alike.
 */
public class MethodNameTest {

	@Test
	public void repeatedCallsToParameterNameAreIdentityEqual() {
		IMethodName m = Names.newMethod("[p:void] [T, P].M([A, P] a, [B, P] b)");

		List<IParameterName> p1 = m.getParameters();
		List<IParameterName> p2 = m.getParameters();
		Assert.assertTrue(p1 == p2);

		IParameterName a1 = p1.iterator().next();
		IParameterName a2 = p2.iterator().next();
		Assert.assertTrue(a1 == a2);
	}

	@Test
	public void repeatedCallsToTypeParameterNameAreIdentityEqual() {
		IMethodName m = Names.newMethod("[p:void] [T, P].M`1[[G]]([A, P] a, [B, P] b)");

		List<ITypeParameterName> p1 = m.getTypeParameters();
		List<ITypeParameterName> p2 = m.getTypeParameters();
		Assert.assertTrue(p1 == p2);

		ITypeParameterName g1 = p1.iterator().next();
		ITypeParameterName g2 = p2.iterator().next();
		Assert.assertTrue(g1 == g2);
	}
}