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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.types.organization.INamespaceName;
import cc.kave.commons.model.ssts.ISST;

public class SSTPrintingUtils {
	public static void formatAsUsingList(Set<INamespaceName> namespaces, SSTPrintingContext context) {
		List<String> filteredNamespaceStrings = new ArrayList();
		for (INamespaceName name : namespaces) {
			if (!name.equals(Names.getUnknownNamespace())) {
				String s = name.getIdentifier().trim();
				if (!s.isEmpty()) {
					filteredNamespaceStrings.add(s);
				}
			}
		}
		filteredNamespaceStrings.sort(null);
		for (String n : filteredNamespaceStrings) {
			context.keyword("using").space().text(n).text(";");

			if (!n.equals(filteredNamespaceStrings.get(filteredNamespaceStrings.size() - 1))) {
				context.newLine();
			}
		}
	}

	public static String printSST(ISST sst) {
		SSTPrintingContext context = new SSTPrintingContext();
		sst.accept(new SSTPrintingVisitor(), context);
		return context.toString();
	}
}
