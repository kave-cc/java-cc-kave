/**
 * Copyright 2016 Technische Universität Darmstadt
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
package cc.kave.commons.utils.sstprinter;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.types.organization.INamespaceName;

public class SSTPrintingUtilsTest {

	@Test
	public void testUsingListFormattedCorrectly() {
		Set<INamespaceName> namespaces = new HashSet<>();
		namespaces.add(Names.newNamespace("Z"));
		namespaces.add(Names.newNamespace("System"));
		namespaces.add(Names.newNamespace("System"));
		namespaces.add(Names.newNamespace("System.Collections.Generic"));
		namespaces.add(Names.newNamespace("A"));
		namespaces.add(Names.newNamespace("")); // global

		SSTPrintingContext context = new SSTPrintingContext();
		SSTPrintingUtils.formatAsUsingList(namespaces, context);
		String expected = String.join("\n", "using A;", "using System;", "using System.Collections.Generic;",
				"using Z;");
		Assert.assertEquals(expected, context.toString());
	}

	@Test
	public void testUnknownNameIsNotAddedToList() {
		Set<INamespaceName> namespaces = new HashSet<>();
		namespaces.add(Names.getUnknownNamespace());
		namespaces.add(Names.newNamespace(""));

		SSTPrintingContext context = new SSTPrintingContext();
		SSTPrintingUtils.formatAsUsingList(namespaces, context);
		String expected = "";
		Assert.assertEquals(expected, context.toString());
	}
}