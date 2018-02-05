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

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.impl.references.MethodReference;
import cc.kave.commons.model.ssts.references.IMethodReference;
import cc.kave.commons.utils.io.json.JsonUtils;
import cc.kave.commons.utils.io.json.TypeUtil;

// this is a regression test for the serialization of KaVE classes in names
// please keep in sync with the equivalent test that exists in C# 
public class NamingSerializationTest {
	@Test
	public void MethodsCanBeSafelyDeSerialized_Regular() {
		String id = "[p:void] [C,P]..ctor()";
		AssertMethod(id, id);
	}

	private static void AssertMethod(String id, String serId) {
		IMethodName obj = Names.newMethod(id);
		String json = JsonUtils.toJson(obj);
		String expected = String.format("\"0M:%s\"", serId);
		Assert.assertEquals(expected, json);
		IMethodName obj2 = JsonUtils.fromJson(json, IMethodName.class);
		Assert.assertEquals(obj, obj2);
	}

	private static void AssertType(String id, String serId) {
		ITypeName obj = Names.newType(id);
		String json = JsonUtils.toJson(obj);
		String expected = String.format("\"0T:%s\"", serId);
		Assert.assertEquals(expected, json);
		ITypeName obj2 = JsonUtils.fromJson(json, ITypeName.class);
		Assert.assertEquals(obj, obj2);
	}

	@Test
	public void MethodsCanBeSafelyDeSerialized_KaVEType() {
		String id = "[p:void] [KaVE.Commons.Model.SSTs.Impl.References.MethodReference, KaVE.Commons].M()";
		String serId = "[p:void] [[SST:References.MethodReference]].M()";
		AssertMethod(id, serId);
	}

	@Test
	public void TypesCanBeSafelyDeSerialized_Regular() {
		String id = "C,P";
		AssertType(id, id);
	}

	@Test
	public void TypesCanBeSafelyDeSerialized_KaVEType() {
		String id = "KaVE.Commons.Model.SSTs.Impl.References.MethodReference, KaVE.Commons";
		String serId = "[SST:References.MethodReference]";
		AssertType(id, serId);
	}

	@Test
	public void RealTypeAnnotationsShouldBeSimplified() {
		MethodReference obj = new MethodReference();
		obj.setMethodName(Names.newMethod(
				"[KaVE.Commons.Model.SSTs.Impl.Foo.Bar, KaVE.Commons] [KaVE.Commons.Model.SSTs.Impl.Bla.Blubb, KaVE.Commons].M()"));

		String json = JsonUtils.toJson(obj);

		String expectedJson = "{\"$type\":\"[SST:References.MethodReference]\","
				+ "\"Reference\":{\"$type\":\"[SST:References.VariableReference]\",\"Identifier\":\"\"},"
				+ "\"MethodName\":\"0M:[[SST:Foo.Bar]] [[SST:Bla.Blubb]].M()\"}";

		Assert.assertEquals(expectedJson, json);

		IMethodReference obj2 = JsonUtils.fromJson(json, IMethodReference.class);
		Assert.assertEquals(obj, obj2);
	}

	// Java specific

	@Test
	public void Compact_to() {
		String id = "...\"$type\":\"cc.kave.commons.model.ssts.impl.foo.Bar\"...";
		String expected = "...\"$type\":\"[SST:Foo.Bar]\"...";
		String actual = TypeUtil.toSerializedNames(id);
		assertEquals(expected, actual);
	}

	@Test
	public void FormattedMeansWhitespace_to() {
		String id = "...\"$type\": \"cc.kave.commons.model.ssts.impl.foo.Bar\"...";
		String expected = "...\"$type\": \"[SST:Foo.Bar]\"...";
		String actual = TypeUtil.toSerializedNames(id);
		assertEquals(expected, actual);
	}

	@Test
	public void Compact_from() {
		String id = "...\"$type\":\"[SST:Foo.Bar]\"...";
		String expected = "...\"$type\":\"cc.kave.commons.model.ssts.impl.foo.Bar\"...";
		String actual = TypeUtil.fromSerializedNames(id);
		assertEquals(expected, actual);
	}

	@Test
	public void FormattedMeansWhitespace_from() {
		String id = "...\"$type\": \"[SST:Foo.Bar]\"...";
		String expected = "...\"$type\": \"cc.kave.commons.model.ssts.impl.foo.Bar\"...";
		String actual = TypeUtil.fromSerializedNames(id);
		assertEquals(expected, actual);
	}
}