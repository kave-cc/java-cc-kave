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
import cc.kave.commons.testing.DataStructureEqualityAsserts;
import cc.kave.rsse.calls.model.usages.IMemberAccess;

public class MemberAccessFeatureTest {

	@Test
	public void defaultValues() {
		IMemberAccess site = mock(IMemberAccess.class);
		MemberAccessFeature sut = new MemberAccessFeature(site);
		assertSame(site, sut.memberAccess);
	}

	@Test
	public void visitorIsImplemented() {
		final boolean[] res = new boolean[] { false };
		new MemberAccessFeature(mock(IMemberAccess.class)).accept(new FeatureVisitor() {
			@Override
			public void visit(MemberAccessFeature f) {
				res[0] = true;
			}
		});
		assertTrue(res[0]);
	}

	@Test
	public void toStringIsImplemented() {
		assertToStringUtils(new MemberAccessFeature(mock(IMemberAccess.class)));
	}

	@Test
	public void equality() {
		IMemberAccess s = mock(IMemberAccess.class);
		MemberAccessFeature a = new MemberAccessFeature(s);
		MemberAccessFeature b = new MemberAccessFeature(s);
		DataStructureEqualityAsserts.assertEqualDataStructures(a, b);
	}

	@Test
	public void equality_diff() {
		MemberAccessFeature a = new MemberAccessFeature(mock(IMemberAccess.class));
		MemberAccessFeature b = new MemberAccessFeature(mock(IMemberAccess.class));
		DataStructureEqualityAsserts.assertNotEqualDataStructures(a, b);
	}

	@Test(expected = AssertionException.class)
	public void fail_siteIsNull() {
		new MemberAccessFeature(null);
	}
}