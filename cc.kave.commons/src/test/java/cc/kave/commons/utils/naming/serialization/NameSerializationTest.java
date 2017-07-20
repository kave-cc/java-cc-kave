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
package cc.kave.commons.utils.naming.serialization;

import static cc.kave.commons.utils.StringUtils.f;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;

import cc.kave.commons.model.naming.IName;
import cc.kave.commons.model.naming.impl.v0.GeneralName;
import cc.kave.commons.model.naming.impl.v0.codeelements.AliasName;
import cc.kave.commons.model.naming.impl.v0.codeelements.EventName;
import cc.kave.commons.model.naming.impl.v0.codeelements.FieldName;
import cc.kave.commons.model.naming.impl.v0.codeelements.LambdaName;
import cc.kave.commons.model.naming.impl.v0.codeelements.LocalVariableName;
import cc.kave.commons.model.naming.impl.v0.codeelements.MethodName;
import cc.kave.commons.model.naming.impl.v0.codeelements.ParameterName;
import cc.kave.commons.model.naming.impl.v0.codeelements.PropertyName;
import cc.kave.commons.model.naming.impl.v0.idecomponents.CommandBarControlName;
import cc.kave.commons.model.naming.impl.v0.idecomponents.CommandName;
import cc.kave.commons.model.naming.impl.v0.idecomponents.DocumentName;
import cc.kave.commons.model.naming.impl.v0.idecomponents.ProjectItemName;
import cc.kave.commons.model.naming.impl.v0.idecomponents.ProjectName;
import cc.kave.commons.model.naming.impl.v0.idecomponents.SolutionName;
import cc.kave.commons.model.naming.impl.v0.idecomponents.WindowName;
import cc.kave.commons.model.naming.impl.v0.others.ReSharperLiveTemplateName;
import cc.kave.commons.model.naming.impl.v0.types.ArrayTypeName;
import cc.kave.commons.model.naming.impl.v0.types.DelegateTypeName;
import cc.kave.commons.model.naming.impl.v0.types.PredefinedTypeName;
import cc.kave.commons.model.naming.impl.v0.types.TypeName;
import cc.kave.commons.model.naming.impl.v0.types.TypeParameterName;
import cc.kave.commons.model.naming.impl.v0.types.TypeUtils;
import cc.kave.commons.model.naming.impl.v0.types.organization.AssemblyName;
import cc.kave.commons.model.naming.impl.v0.types.organization.AssemblyVersion;
import cc.kave.commons.model.naming.impl.v0.types.organization.NamespaceName;
import cc.kave.commons.testing.ParameterData;
import cc.kave.commons.utils.naming.serialization.NameSerialization;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class NameSerializationTest {

	@Test(expected = IllegalArgumentException.class)
	public void ShouldFailForUnknownPrefixes() {
		NameSerialization.deserialize("x:...");
	}

	@Test(expected = IllegalArgumentException.class)
	public void ShouldFailForUnknownTypes() {
		NameSerialization.serialize(new TestName());
	}

	public static Object[][] provideV0Types() {
		ParameterData d = new ParameterData();
		d.add("T,P", "CSharp.TypeName", "0T", TypeName.class);
		d.add("T[],P", "CSharp.TypeName", "0T", ArrayTypeName.class);
		d.add("p:int", "CSharp.PredefinedTypeName", "0T", PredefinedTypeName.class);
		d.add("p:int[]", "CSharp.PredefinedTypeName", "0T", PredefinedTypeName.class);
		d.add("T", "CSharp.TypeName", "0T", TypeParameterName.class);
		d.add("T", "CSharp.TypeParameterName", "0T", TypeParameterName.class);
		d.add("T -> T,P", "CSharp.TypeName", "0T", TypeParameterName.class);
		d.add("e:n.E,P", "CSharp.EnumTypeName", "0T", TypeName.class);
		d.add("i:n.I,P", "CSharp.InterfaceTypeName", "0T", TypeName.class);
		d.add("s:n.S,P", "CSharp.StructTypeName", "0T", TypeName.class);
		d.add("d:[?] [T,P].()", "CSharp.TypeName", "0T", DelegateTypeName.class);
		d.add("d:[?] [T,P].()", "CSharp.DelegateTypeName", "0T", DelegateTypeName.class);

		return d.toArray();
	}

	@Test
	@Parameters(method = "provideV0Types")
	public void ShouldDeserializeV0Types(String id, String oldPrefix, String newPrefix, Class<?> expectedType) {
		for (String prefix : new String[] { oldPrefix, newPrefix }) {
			String input = f("%s:%s", prefix, id);

			IName objExpected = TypeUtils.createTypeName(id);
			IName objActual = NameSerialization.deserialize(input);
			assertEquals(objExpected, objActual);
			assertTrue(expectedType.isAssignableFrom(objActual.getClass()));

			String outputActual = NameSerialization.serialize(objActual);
			String outputExpected = f("%s:%s", newPrefix, id);
			assertEquals(outputExpected, outputActual);
		}
	}

	@Test
	public void ShouldHandleOldUnknownType() {
		IName objExpected = new TypeName();
		IName objActual = NameSerialization.deserialize("CSharp.UnknownTypeName:?");

		assertEquals(objExpected, objActual);
		assertTrue(objActual instanceof TypeName);

		String outputActual = NameSerialization.serialize(objActual);
		String outputExpected = "0T:?";
		assertEquals(outputExpected, outputActual);
	}

	public static Object[][] provideV0Names() {
		ParameterData d = new ParameterData();
		d.add("xyz", "CSharp.Name", "0General", GeneralName.class);
		// code elements
		d.add("A -> ?", "CSharp.AliasName", "0Alias", AliasName.class);
		d.add("[VT,P] [DT,P]._e", "CSharp.EventName", "0E", EventName.class);
		d.add("[VT,P] [DT,P]._f", "CSharp.FieldName", "0F", FieldName.class);
		d.add("[RT,P] ()", "CSharp.LambdaName", "0L", LambdaName.class);
		d.add("[T,P] v", "CSharp.LocalVariableName", "0LocalVar", LocalVariableName.class);
		d.add("[RT,P] [DT,P].M()", "CSharp.MethodName", "0M", MethodName.class);
		d.add("[PT,P] p", "CSharp.ParameterName", "0Param", ParameterName.class);
		d.add("get [VT,P] [DT,P].P()", "CSharp.PropertyName", "0P", PropertyName.class);
		// ide components
		d.add("a|b|c", "VisualStudio.CommandBarControlName", "0Ctrl", CommandBarControlName.class);
		d.add("a:1:abc", "VisualStudio.CommandName", "0Cmd", CommandName.class);
		d.add("CSharp C:\\File.cs", "VisualStudio.DocumentName", "0Doc", DocumentName.class);
		d.add("File C:\\Project\\File.txt", "VisualStudio.ProjectItemName", "0Itm", ProjectItemName.class);
		d.add("ProjectType C:\\Project.csproj", "VisualStudio.ProjectName", "0Prj", ProjectName.class);
		d.add("C:\\File\\To\\S.sln", "VisualStudio.SolutionName", "0Sln", SolutionName.class);
		d.add("someType someCaption", "VisualStudio.WindowName", "0Win", WindowName.class);
		// other
		d.add("someType:someCaption", "ReSharper.LiveTemplateName", "0RSTpl", ReSharperLiveTemplateName.class);
		// types/organization
		d.add("A, 1.2.3.4", "CSharp.AssemblyName", "0A", AssemblyName.class);
		d.add("1.2.3.4", "CSharp.AssemblyVersion", "0V", AssemblyVersion.class);
		d.add("a.b.c", "CSharp.NamespaceName", "0N", NamespaceName.class);
		// types
		d.add("T,P", "CSharp.TypeName", "0T", TypeName.class);
		return d.toArray();
	}

	@Test
	@Parameters(method = "provideV0Names")
	public void ShouldDeserializeV0Names(String id, String oldPrefix, String newPrefix, Class<?> expectedType) {
		for (String prefix : new String[] { oldPrefix, newPrefix }) {
			String input = f("%s:%s", prefix, id);
			IName obj = NameSerialization.deserialize(input);

			assertTrue(expectedType.isAssignableFrom(obj.getClass()));

			String actual = NameSerialization.serialize(obj);
			String expected = f("%s:%s", newPrefix, id);
			assertEquals(expected, actual);
		}
	}

	private class TestName implements IName {
		public String getIdentifier() {
			return "";
		}

		@Override
		public boolean isUnknown() {
			return false;
		}

		@Override
		public boolean isHashed() {
			return false;
		}
	}
}