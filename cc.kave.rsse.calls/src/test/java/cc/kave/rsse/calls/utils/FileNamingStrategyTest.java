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

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.types.ITypeName;

public class FileNamingStrategyTest {

	@Test
	public void localType() {
		assertPath("T, P", "P/local/T");
	}

	@Test
	public void frameworkType() {
		assertPath("T, F, 1.2.3.4", "F/1.2.3.4/T");
	}

	@Test
	public void typeWithNamespace() {
		assertPath("a.b.C, F, 1.2.3.4", "F/1.2.3.4/a/b/C");
	}

	@Test
	public void interfaces() {
		assertPath("i:n.I, F, 1.2.3.4", "F/1.2.3.4/n/i_I");
	}

	@Test
	public void structs() {
		assertPath("s:S, F, 1.2.3.4", "F/1.2.3.4/s_S");
	}

	@Test
	public void predefined() {
		assertPath("p:int", "predefined/int");
	}

	@Test
	public void array1() {
		String id = Names.newArrayType(1, Names.newType("p:int")).getIdentifier();
		assertPath(id, "array_1d/predefined/int");
	}

	@Test
	public void array3() {
		String id = Names.newArrayType(3, Names.newType("p:int")).getIdentifier();
		assertPath(id, "array_3d/predefined/int");
	}

	@Test
	public void delegate() {
		assertPath("d:[p:int] [n.T+D,P].([p:object] o)", "delegate/P/local/n/T+D/d_[p_int]_[n.T+D,P].([p_object]_o)");
	}

	@Test
	public void delegate_generic() {
		assertPath("d:[p:int] [T'1[[U]]+D,P].([p:object] o)", "delegate/P/local/T_1[[U]]+D/d_[p_int]_[T_1[[U]]+D,P].([p_object]_o)");
	}

	@Test
	public void delegate_unnested() {
		assertPath("d:[p:int] [D'1[[U]],P].([p:object] o)", "delegate/P/local/D_1[[U]]/d_[p_int]_[D_1[[U]],P].([p_object]_o)");
	}

	@Test
	public void unknown() {
		assertPath(Names.getUnknownType().getIdentifier(), "unknown");
	}

	@Test
	public void generic_unbound() {
		assertPath("T'1[[U]], P, 1.2.3.4", "P/1.2.3.4/T_1[[U]]");
	}

	@Test
	public void generic_bound() {
		assertPath("T'1[[U -> p:int]], P, 1.2.3.4", "P/1.2.3.4/T_1[[U_-__p_int]]");
	}

	@Test
	public void generic_interface() {
		assertPath("i:I'1[[U]], P, 1.2.3.4", "P/1.2.3.4/i_I_1[[U]]");
	}

	private void assertPath(String id, String relPath) {
		String expected = String.join(File.separator, relPath.split("/"));

		ITypeName t = Names.newType(id);
		String actual = new FileNamingStrategy().getRelativePath(t);
		assertEquals(expected, actual);
	}
}