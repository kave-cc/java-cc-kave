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
package cc.kave.commons.model.naming.impl.v0.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;

import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.testcommons.ParameterData;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class TypeUtilsTest {

	@Test
	public void ShouldRecognizeUnknownType() {
		for (String id : new String[] { null, "", "?" }) {
			assertTrue(TypeUtils.isUnknownTypeIdentifier(id));
			assertFalse(TypeName.isTypeNameIdentifier(id));
			assertFalse(ArrayTypeName.isArrayTypeNameIdentifier(id));
			assertFalse(DelegateTypeName.isDelegateTypeNameIdentifier(id));
			assertFalse(TypeParameterName.isTypeParameterNameIdentifier(id));
			assertFalse(PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
		}
	}

	public static Object[][] provideTypes() {
		ParameterData pd = new ParameterData();
		pd.add("T[],P", "ArrayTypeName");
		pd.add("d:[?] [?].()", "DelegateTypeName");
		pd.add("T", "TypeParameterName");
		pd.add("p:int", "PredefinedTypeName");
		pd.add("T,P", "TypeName");
		return pd.toArray();

	}

	@Test
	@Parameters(method = "provideTypes")
	public void ShouldRecognizeNameTypes(String typeId, String typeOfName) {
		ITypeName sut = TypeUtils.createTypeName(typeId);
		assertEquals(typeOfName, sut.getClass().getSimpleName());
	}
}