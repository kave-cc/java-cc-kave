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
package cc.kave.commons.model.naming.impl.v0.types.organization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;

import cc.kave.commons.exceptions.ValidationException;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.types.organization.IAssemblyName;
import cc.kave.commons.model.naming.types.organization.IAssemblyVersion;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class AssemblyNameTest {

	@Test
	public void DefaultValues() {
		IAssemblyName sut = new AssemblyName();
		assertFalse(sut.isLocalProject());
		assertEquals("???", sut.getName());
		assertEquals(new AssemblyVersion(), sut.getVersion());
		assertTrue(sut.isUnknown());
	}

	@Test
	public void HappyPath_Assembly() {
		IAssemblyName sut = new AssemblyName("A, 1.2.3.4");
		assertFalse(sut.isLocalProject());
		assertEquals("A", sut.getName());
		assertEquals(new AssemblyVersion("1.2.3.4"), sut.getVersion());
		assertFalse(sut.isUnknown());
	}

	@Test
	public void HappyPath_LocalProject() {
		IAssemblyName sut = new AssemblyName("P");
		assertTrue(sut.isLocalProject());
		assertEquals("P", sut.getName());
		assertEquals(new AssemblyVersion(), sut.getVersion());
		assertFalse(sut.isUnknown());
	}

	@Test
	public void AnUnknownVersionDoesNotMakeALocalProject() {
		assertFalse(new AssemblyName("P, -1.-1.-1.-1").isLocalProject());
	}

	@Test(expected = ValidationException.class)
	@Parameters({ "(", ")", "[", "]", "{", "}", "\\,", ";", ":", "a b" })
	public void SpecialCharsInNameAreNotAllowed(String specialChar) {
		new AssemblyName("a" + specialChar + "b, 1.2.3.4");
	}

	@Test(expected = ValidationException.class)
	public void VersionNeedsToBeParseable() {
		// ReSharper disable once ObjectCreationAsStatement
		new AssemblyName("P, 1.2.3.4a");
	}

	@Test
	public void LotOfWhitespace() {
		IAssemblyName n = new AssemblyName(" A , 1.2.3.4");
		AssertName(n, "A");
		AssertVersion(n, "1.2.3.4");
	}

	@Test
	public void NoWhitespace() {
		IAssemblyName n = new AssemblyName("A,1.2.3.4");
		AssertName(n, "A");
		AssertVersion(n, "1.2.3.4");
	}

	@Test
	public void ShouldImplementIsUnknown() {
		assertTrue(new AssemblyName().isUnknown());
	}

	@Test
	public void ShouldBeMSCorLibAssembly() {
		String identifier = "mscorlib, 4.0.0.0";
		IAssemblyName mscoreAssembly = new AssemblyName(identifier);

		assertEquals("mscorlib", mscoreAssembly.getName());
		assertEquals("4.0.0.0", mscoreAssembly.getVersion().getIdentifier());
		assertEquals(identifier, mscoreAssembly.getIdentifier());
	}

	@Test
	public void mscorlibIsNotLocal() {
		IAssemblyName a = Names.newType("p:int").getAssembly();
		assertFalse(a.isLocalProject());
	}

	private static void AssertName(IAssemblyName assemblyName, String expected) {
		String actual = assemblyName.getName();
		assertEquals(expected, actual);
	}

	private static void AssertVersion(IAssemblyName assemblyName, String expectedVersion) {
		IAssemblyVersion actual = assemblyName.getVersion();
		assertEquals(new AssemblyVersion(expectedVersion), actual);
	}
}