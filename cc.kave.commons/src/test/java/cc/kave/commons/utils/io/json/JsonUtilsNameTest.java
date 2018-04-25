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
package cc.kave.commons.utils.io.json;

import static cc.kave.commons.utils.StringUtils.f;
import static cc.kave.commons.utils.ssts.SSTUtils.ACTION;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.collect.Sets;

import cc.kave.commons.model.naming.IName;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMemberName;
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
import cc.kave.commons.model.naming.impl.v0.idecomponents.SolutionName;
import cc.kave.commons.model.naming.impl.v0.idecomponents.WindowName;
import cc.kave.commons.model.naming.impl.v0.others.ReSharperLiveTemplateName;
import cc.kave.commons.model.naming.impl.v0.types.ArrayTypeName;
import cc.kave.commons.model.naming.impl.v0.types.DelegateTypeName;
import cc.kave.commons.model.naming.impl.v0.types.PredefinedTypeName;
import cc.kave.commons.model.naming.impl.v0.types.TypeName;
import cc.kave.commons.model.naming.impl.v0.types.TypeParameterName;
import cc.kave.commons.model.naming.impl.v0.types.organization.AssemblyName;
import cc.kave.commons.model.naming.impl.v0.types.organization.AssemblyVersion;
import cc.kave.commons.model.naming.impl.v0.types.organization.NamespaceName;
import cc.kave.commons.testing.ParameterData;
import cc.kave.commons.utils.naming.serialization.NameSerialization;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;

@RunWith(JUnitParamsRunner.class)
public class JsonUtilsNameTest {

	private static IName[] names = new IName[] { new GeneralName(),

			// v0

			// code elements
			new AliasName(), new EventName(), new FieldName(), new LambdaName(), new LocalVariableName(),
			new MethodName(), new ParameterName(), new PropertyName(),
			// ide components
			new CommandBarControlName(), new CommandName(), new DocumentName(), new ProjectItemName(),
			new PropertyName(), new SolutionName(), new WindowName(),
			// others
			new ReSharperLiveTemplateName(),
			// types
			new AssemblyName(), new AssemblyVersion(), new NamespaceName(),
			//
			new ArrayTypeName("T[],P"), new DelegateTypeName(), new PredefinedTypeName("p:int"), new TypeName(),
			new TypeParameterName("T")

			// v1

			// yet to come... :D
	};

	@Test
	@Parameters(method = "provideSerializationExamples")
	@TestCaseName("{2}")
	public void deserialization(IName expected, Class<?> type, String tcn) {
		String str = NameSerialization.serialize(expected);
		String json = f("\"%s\"", str);
		Object actual = JsonUtils.fromJson(json, type);
		assertEquals(expected, actual);
	}

	@Test
	@Parameters(method = "provideSerializationExamples")
	@TestCaseName("{2}")
	public void serialization(IName n, Class<?> type, String tcn) {
		String str = NameSerialization.serialize(n);
		String expected = f("\"%s\"", str);
		String actual = JsonUtils.toJson(n, type);
		assertEquals(expected, actual);
	}

	@Test
	public void memberNames() {
		String eid = String.format("[%s] [T, P].E", ACTION.getIdentifier());
		assertMember(Names.newEvent(eid), "\"0E:" + eid + "\"");

		String fid = "[p:int] [T, P]._f";
		assertMember(Names.newField(fid), "\"0F:" + fid + "\"");

		String mid = "[p:int] [T, P].m()";
		assertMember(Names.newMethod(mid), "\"0M:" + mid + "\"");

		String pid = "get set [p:int] [T, P].P()";
		assertMember(Names.newProperty(pid), "\"0P:" + pid + "\"");
	}

	private void assertMember(IMemberName m, String mid) {
		String json = JsonUtils.toJson(m, IMemberName.class);
		assertEquals(mid, json);
		IMemberName m2 = JsonUtils.fromJson(json, IMemberName.class);
		assertEquals(m, m2);

		Set<IMemberName> s = new HashSet<IMemberName>();
		s.add(m);
		json = JsonUtils.toJson(s);
		assertEquals("[" + mid + "]", json);

		Map<IMemberName, Double> map = new HashMap<IMemberName, Double>();
		map.put(m, 0.123);
		json = JsonUtils.toJson(map);
		assertEquals("{" + mid + ":0.123}", json);

	}

	public static Object[][] provideSerializationExamples() {
		ParameterData d = new ParameterData();
		for (IName n : names) {
			for (Class<?> c : getHierarchy(n.getClass())) {
				String testCaseName = f("%s as %s", n.getClass().getSimpleName(), c.getSimpleName());
				d.add(n, c, testCaseName);
			}
		}
		return d.toArray();
	}

	private static Set<Class<?>> getHierarchy(Class<?> elem) {
		Set<Class<?>> hierarchy = Sets.newHashSet();
		if (elem == null || elem.equals(Object.class)) {
			return hierarchy;
		}

		hierarchy.add(elem);

		for (Class<?> i : elem.getInterfaces()) {
			hierarchy.addAll(getHierarchy(i));
		}
		hierarchy.addAll(getHierarchy(elem.getSuperclass()));

		return hierarchy;
	}
}