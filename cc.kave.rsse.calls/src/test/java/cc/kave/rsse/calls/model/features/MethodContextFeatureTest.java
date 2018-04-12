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
package cc.kave.rsse.calls.model.features;

import static cc.kave.commons.testing.ToStringAsserts.assertToStringUtils;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import cc.kave.commons.exceptions.AssertionException;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.testing.DataStructureEqualityAsserts;

public class MethodContextFeatureTest {

	private static final IMethodName SOME_METHOD = mock(IMethodName.class);
	private static final IMethodName OTHER_METHOD = mock(IMethodName.class);

	@Test
	public void defaultValues() {
		MethodContextFeature sut = new MethodContextFeature(SOME_METHOD);
		assertSame(SOME_METHOD, sut.method);
	}

	@Test
	public void visitorIsImplemented() {
		final boolean[] res = new boolean[] { false };
		new MethodContextFeature(SOME_METHOD).accept(new FeatureVisitor() {
			@Override
			public void visit(MethodContextFeature f) {
				res[0] = true;
			}
		});
		assertTrue(res[0]);
	}

	@Test
	public void toStringIsImplemented() {
		assertToStringUtils(new MethodContextFeature(SOME_METHOD));
	}

	@Test
	public void equality() {
		MethodContextFeature a = new MethodContextFeature(SOME_METHOD);
		MethodContextFeature b = new MethodContextFeature(SOME_METHOD);
		DataStructureEqualityAsserts.assertEqualDataStructures(a, b);
	}

	@Test
	public void equality_diffValues() {
		MethodContextFeature a = new MethodContextFeature(SOME_METHOD);
		MethodContextFeature b = new MethodContextFeature(OTHER_METHOD);
		DataStructureEqualityAsserts.assertNotEqualDataStructures(a, b);
	}

	@Test(expected = AssertionException.class)
	public void fail_siteIsNull() {
		new MethodContextFeature(null);
	}
}