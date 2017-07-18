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
package cc.kave.commons.model.naming.impl.v0.idecomponents;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;

import cc.kave.commons.exceptions.ValidationException;
import cc.kave.commons.model.naming.idecomponents.IDocumentName;
import cc.kave.testcommons.ParameterData;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class DocumentNameTest {

	private IDocumentName sut;

	@Test
	public void DefaultValues() {
		sut = new DocumentName();
		assertEquals("???", sut.getLanguage());
		assertEquals("???", sut.getFileName());
		assertTrue(sut.isUnknown());
	}

	@Test
	public void ShouldImplementIsUnknown() {
		assertTrue(new DocumentName().isUnknown());
		assertTrue(new DocumentName("???").isUnknown());
		assertFalse(new DocumentName("C# f.cs").isUnknown());
	}

	@Test(expected = ValidationException.class)
	public void ShouldAvoidNullParameters() {
		new DocumentName(null);
	}

	public static Object[][] provideDocumentNames() {
		ParameterData pd = new ParameterData();
		// id, language, filename
		pd.add("CSharp C:\\File.cs", "CSharp", "C:\\File.cs");
		pd.add(" \\File.ext", "", "\\File.ext");
		pd.add("Basic Code.vb", "Basic", "Code.vb");
		pd.add("C/C++ Code.c", "C/C++", "Code.c");
		pd.add("Plain Text Readme.txt", "Plain Text", "Readme.txt");
		pd.add("Plain Text Path With Spaces\\Readme.txt", "Plain Text", "Path With Spaces\\Readme.txt");
		return pd.toArray();
	}

	@Test
	@Parameters(method = "provideDocumentNames")
	public void ParsesName(String identifier, String language, String fileName) {
		sut = new DocumentName(identifier);
		assertEquals(language, sut.getLanguage());
		assertEquals(fileName, sut.getFileName());
	}

	@Test(expected = ValidationException.class)
	public void ShouldRejectNameWithoutSpaces() {
		new DocumentName("C:\\No\\Type\\Only\\File.cs");
	}
}