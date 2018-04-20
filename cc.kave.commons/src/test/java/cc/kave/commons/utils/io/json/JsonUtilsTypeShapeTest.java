/**
 * Copyright 2018 University of Zurich
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.kave.commons.utils.io.json;

import java.lang.reflect.Type;

import org.junit.Assert;
import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.typeshapes.EventHierarchy;
import cc.kave.commons.model.typeshapes.MethodHierarchy;
import cc.kave.commons.model.typeshapes.PropertyHierarchy;
import cc.kave.commons.model.typeshapes.TypeHierarchy;

public class JsonUtilsTypeShapeTest {

	@Test
	public void EventHierarchies() {
		assertRoundtrip(new EventHierarchy(), EventHierarchy.class);
	}

	@Test
	public void MethodHierarchies() {
		assertRoundtrip(new MethodHierarchy(), MethodHierarchy.class);
	}

	@Test
	public void PropertyHierarchies() {
		assertRoundtrip(new PropertyHierarchy(), PropertyHierarchy.class);
	}

	@Test
	public void TypeHierarchies() {
		assertRoundtrip(new TypeHierarchy(Names.newType("T, P")), TypeHierarchy.class);
	}

	private static void assertRoundtrip(Object in, Type classOfIn) {
		String json = JsonUtils.toJson(in);
		Object out = JsonUtils.fromJson(json, classOfIn);
		Assert.assertEquals(in, out);
	}
}