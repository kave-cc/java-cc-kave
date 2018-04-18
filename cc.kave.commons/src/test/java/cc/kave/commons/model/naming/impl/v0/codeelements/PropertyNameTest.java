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

import static cc.kave.commons.model.naming.Names.newMethod;
import static cc.kave.commons.model.naming.Names.newProperty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.collect.Lists;

import cc.kave.commons.exceptions.AssertionException;
import cc.kave.commons.exceptions.ValidationException;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

/* Please note: the primary test source for member names should be the generated
 * test suite. If you encounter a bug, please fix it on the C# side first, then
 * regenerate the test suite to create a failing test, and only then fix the
 * bug in the Java implementation of the names.
 * 
 * The sole purpose of this class is to test the "infrastructure", including
 * Java specific checks, implementation details, instantiation checks, or alike.
 */
@RunWith(JUnitParamsRunner.class)
public class PropertyNameTest {

	@Test(expected = ValidationException.class)
	@Parameters({ //
			"get set [?] [?].P", // no paranthesis
			"[?] [?].P()" // neither get nor set
	})

	public void ShouldRejectInvalidProperties(String invalidId) {
		// ReSharper disable once ObjectCreationAsStatement
		new PropertyName(invalidId);
	}

	@Test
	public void getExplicitGetter_regular() {
		IMethodName actual = Names.newProperty("set get [p:int] [p:object].P()").getExplicitGetterName();
		IMethodName expected = Names.newMethod("[p:int] [p:object].P__get__()");
		assertEquals(expected, actual);
	}

	@Test
	public void getExplicitGetter_indexer() {
		IMethodName actual = Names.newProperty("set get [p:int] [p:object].P([p:bool] p)").getExplicitGetterName();
		IMethodName expected = Names.newMethod("[p:int] [p:object].P__get__([p:bool] p)");
		assertEquals(expected, actual);
	}

	@Test
	public void getExplicitSetter_regular() {
		IMethodName actual = newProperty("set get [p:int] [p:object].P()").getExplicitSetterName();
		IMethodName expected = newMethod("[p:void] [p:object].P__set__([p:int] value)");
		assertEquals(expected, actual);
	}

	@Test
	public void getExplicitSetter_indexer() {
		IMethodName actual = newProperty("set get [p:int] [p:object].P([p:bool] p)").getExplicitSetterName();
		IMethodName expected = newMethod("[p:void] [p:object].P__set__([p:bool] p, [p:int] value)");
		assertEquals(expected, actual);
	}

	@Test(expected = AssertionException.class)
	public void getExplicitGetter_notImplemented() {
		newProperty("set [p:int] [p:object].P()").getExplicitGetterName();
	}

	@Test(expected = AssertionException.class)
	public void getExplicitSetter_notImplemented() {
		newProperty("get [p:int] [p:object].P()").getExplicitSetterName();
	}

	@Test
	public void canRequestParametersFromIndexer() {
		List<IParameterName> actuals = newProperty("set get [p:int] [p:object].P([p:bool] idx)").getParameters();
		List<IParameterName> expecteds = Lists.newArrayList(Names.newParameter("[p:bool] idx"));
		assertEquals(expecteds, actuals);
	}

	@Test(expected = AssertionException.class)
	public void cannotRequestParametersFromNonIndexer() {
		Names.newProperty("set get [p:int] [p:object].P()").getParameters();

	}

	@Test
	public void getExplicitGetterIsStable() {
		IPropertyName n = Names.newProperty("set get [p:int] [p:object].P()");
		IMethodName a = n.getExplicitGetterName();
		IMethodName b = n.getExplicitGetterName();
		assertSame(a, b);
	}

	@Test
	public void getExplicitSetterIsStable() {
		IPropertyName n = Names.newProperty("set get [p:int] [p:object].P()");
		IMethodName a = n.getExplicitSetterName();
		IMethodName b = n.getExplicitSetterName();
		assertSame(a, b);
	}

	@Test
	public void getExplicitSetterParametersAreStable() {
		IPropertyName n = Names.newProperty("set get [p:int] [p:object].P()");
		List<IParameterName> pA = n.getExplicitSetterName().getParameters();
		List<IParameterName> pB = n.getExplicitSetterName().getParameters();
		assertSame(pA, pB);
		assertSame(pA.get(0), pB.get(0));
	}

	@Test
	public void getExplicitGetterParametersAreStable() {
		IPropertyName n = Names.newProperty("set get [p:int] [p:object].P([p:bool] p)");
		List<IParameterName> pA = n.getExplicitSetterName().getParameters();
		List<IParameterName> pB = n.getExplicitSetterName().getParameters();
		assertSame(pA, pB);
		assertSame(pA.get(0), pB.get(0));
	}

	@Test
	public void explicitParameterNamesAreStable_indexerSet() {
		IPropertyName n = Names.newProperty("set [p:int] [p:object].P([p:bool] p)");
		List<IParameterName> pA = n.getParameters();
		List<IParameterName> pB = n.getExplicitSetterName().getParameters();
		assertEquals(pA.size(), pB.size() - 1);
		assertSame(pA.get(0), pB.get(0));
	}

	@Test
	public void explicitParameterNamesAreStable_indexerGet() {
		IPropertyName n = Names.newProperty("get [p:int] [p:object].P([p:bool] p)");
		List<IParameterName> pA = n.getParameters();
		List<IParameterName> pB = n.getExplicitGetterName().getParameters();
		assertSame(pA, pB);
		assertSame(pA.get(0), pB.get(0));
	}
}