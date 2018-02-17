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
package cc.kave.rsse.calls.utils;

import java.io.File;

import cc.kave.commons.model.naming.types.IArrayTypeName;
import cc.kave.commons.model.naming.types.IDelegateTypeName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.naming.types.organization.IAssemblyName;
import cc.kave.commons.model.naming.types.organization.IAssemblyVersion;

public class FileNamingStrategy implements INamingStrategy<ITypeName> {

	@Override
	public String getRelativePath(ITypeName t) {

		if (t.isUnknown()) {
			return "unknown";
		}

		if (t.isPredefined()) {
			return p("predefined", t.getName());
		}

		if (t.isArray()) {
			IArrayTypeName arrT = t.asArrayTypeName();
			return p(String.format("array_%dd", arrT.getRank()), getRelativePath(arrT.getArrayBaseType()));
		}

		if (t.isDelegateType()) {
			IDelegateTypeName dt = t.asDelegateTypeName();
			String filler = getRelativePath(dt.getDelegateType());
			return f("delegate", filler, t.getIdentifier());
		}

		String asm = toPart(t.getAssembly());
		String prefix = t.isInterfaceType() ? "i:" : t.isStructType() ? "s:" : "";
		String[] parts = t.getFullName().split("\\.");
		parts[parts.length - 1] = prefix + parts[parts.length - 1];
		return f(asm, String.join(File.separator, parts));
	}

	private String toPart(IAssemblyName asm) {
		String name = asm.getName();
		IAssemblyVersion v = asm.getVersion();
		String version = v.isUnknown() ? "local"
				: String.format("%d.%d.%d.%d", v.getMajor(), v.getMinor(), v.getBuild(), v.getRevision());
		return p(name, version);
	}

	private static String f(String... parts) {
		String relName = p(parts);
		// relName = relName.replace('.', '/');
		// relName = relName.replace("\\\"", "\""); // quotes inside json
		// relName = relName.replace("\"", ""); // surrounding quotes
		// relName = relName.replace('\\', '/');
		relName = relName.replaceAll("[^a-zA-Z0-9,\\-_/+$(){}\\[\\].]", "_");
		// relName = relName.replaceAll("\\/+", "/"); // clean up duplicate slashes
		return relName;
	}

	private static String p(String... parts) {
		return String.join(File.separator, parts);
	}
}