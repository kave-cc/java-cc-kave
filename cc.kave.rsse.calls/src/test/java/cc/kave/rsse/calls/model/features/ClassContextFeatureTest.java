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
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.testing.DataStructureEqualityAsserts;

public class ClassContextFeatureTest {

	private static final ITypeName SOME_TYPE = mock(ITypeName.class);
	private static final ITypeName OTHER_TYPE = mock(ITypeName.class);

	@Test
	public void defaultValues() {
		ClassContextFeature sut = new ClassContextFeature(SOME_TYPE);
		assertSame(SOME_TYPE, sut.type);
	}

	@Test
	public void visitorIsImplemented() {
		final boolean[] res = new boolean[] { false };
		new ClassContextFeature(SOME_TYPE).accept(new FeatureVisitor() {
			@Override
			public void visit(ClassContextFeature f) {
				res[0] = true;
			}
		});
		assertTrue(res[0]);
	}

	@Test
	public void toStringIsImplemented() {
		assertToStringUtils(new ClassContextFeature(SOME_TYPE));
	}

	@Test
	public void equality() {
		ClassContextFeature a = new ClassContextFeature(SOME_TYPE);
		ClassContextFeature b = new ClassContextFeature(SOME_TYPE);
		DataStructureEqualityAsserts.assertEqualDataStructures(a, b);
	}

	@Test
	public void equality_diffValues() {
		ClassContextFeature a = new ClassContextFeature(SOME_TYPE);
		ClassContextFeature b = new ClassContextFeature(OTHER_TYPE);
		DataStructureEqualityAsserts.assertNotEqualDataStructures(a, b);
	}

	@Test(expected = AssertionException.class)
	public void fail_siteIsNull() {
		new ClassContextFeature(null);
	}
}