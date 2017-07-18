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

import cc.kave.commons.exceptions.ValidationException;
import cc.kave.commons.model.naming.types.organization.IAssemblyVersion;

public class AssemblyVersionTest {

	@Test
	public void DefaultValues() {
		IAssemblyVersion sut = new AssemblyVersion();
		assertEquals(-1, sut.getMajor());
		assertEquals(-1, sut.getMinor());
		assertEquals(-1, sut.getBuild());
		assertEquals(-1, sut.getRevision());
		assertTrue(sut.isUnknown());
	}

	@Test
	public void ShouldParseNumbers() {
		IAssemblyVersion sut = new AssemblyVersion("1.2.3.4");
		assertEquals(1, sut.getMajor());
		assertEquals(2, sut.getMinor());
		assertEquals(3, sut.getBuild());
		assertEquals(4, sut.getRevision());
		assertFalse(sut.isUnknown());
	}

	@Test
	public void ShouldParseNumbers_Zeros() {
		IAssemblyVersion sut = new AssemblyVersion("0.0.0.0");
		assertEquals(0, sut.getMajor());
		assertEquals(0, sut.getMinor());
		assertEquals(0, sut.getBuild());
		assertEquals(0, sut.getRevision());
		assertFalse(sut.isUnknown());
	}

	@Test
	public void ShouldParseNumbersMultiDigit() {
		IAssemblyVersion sut = new AssemblyVersion("11.22.33.44");
		assertEquals(11, sut.getMajor());
		assertEquals(22, sut.getMinor());
		assertEquals(33, sut.getBuild());
		assertEquals(44, sut.getRevision());
		assertFalse(sut.isUnknown());
	}

	@Test
	public void ShouldRecognizeUnknownName() {
		assertTrue(new AssemblyVersion().isUnknown());
		assertTrue(new AssemblyVersion("???").isUnknown());
		assertFalse(new AssemblyVersion("1.2.3.4").isUnknown());
	}

	@Test(expected = ValidationException.class)
	public void ShouldAvoidNullParameters() {
		// ReSharper disable once ObjectCreationAsStatement
		// ReSharper disable once AssignNullToNotNullAttribute
		new AssemblyVersion(null);
	}

	@Test(expected = ValidationException.class)
	public void ShouldRejectIdentifiersWithInsufficientNumberOfDots() {
		// ReSharper disable once ObjectCreationAsStatement
		new AssemblyVersion("1.2.3");
	}

	@Test(expected = ValidationException.class)
	public void ShouldRejectIdentifiers_WithNonNumbers() {
		// ReSharper disable once ObjectCreationAsStatement
		new AssemblyVersion("1.2.3.a");
	}

	@Test
	public void ShouldBeGreaterThenPreviousVersions() {
		assertTrue(V("0.2.3.4").compareTo(V("1.2.3.4")) < 0);
		assertTrue(V("1.1.3.4").compareTo(V("1.2.3.4")) < 0);
		assertTrue(V("1.2.2.4").compareTo(V("1.2.3.4")) < 0);
		assertTrue(V("1.2.3.3").compareTo(V("1.2.3.4")) < 0);
	}

	@Test
	public void ShouldEqualsSameVersion() {
		IAssemblyVersion v1 = V("1.2.3.4");
		IAssemblyVersion v2 = V("1.2.3.4");

		assertEquals(v1, v2);
		assertTrue(v1.compareTo(v2) <= 0);
		assertTrue(v1.compareTo(v2) == 0);
		assertTrue(v1.compareTo(v2) >= 0);
	}

	@Test
	public void ShouldBeSmallerThenLaterVersions() {
		assertTrue(V("2.2.3.4").compareTo(V("1.2.3.4")) > 0);
		assertTrue(V("1.3.3.4").compareTo(V("1.2.3.4")) > 0);
		assertTrue(V("1.2.4.4").compareTo(V("1.2.3.4")) > 0);
		assertTrue(V("1.2.3.5").compareTo(V("1.2.3.4")) > 0);
	}

	private static AssemblyVersion V(String id) {
		return new AssemblyVersion(id);
	}
}