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
import cc.kave.rsse.calls.model.usages.IUsageSite;

public class UsageSiteFeatureTest {

	@Test
	public void defaultValues() {
		IUsageSite site = mock(IUsageSite.class);
		UsageSiteFeature sut = new UsageSiteFeature(site);
		assertSame(site, sut.site);
	}

	@Test
	public void visitorIsImplemented() {
		final boolean[] res = new boolean[] { false };
		new UsageSiteFeature(mock(IUsageSite.class)).accept(new FeatureVisitor() {
			@Override
			public void visit(UsageSiteFeature f) {
				res[0] = true;
			}
		});
		assertTrue(res[0]);
	}

	@Test
	public void toStringIsImplemented() {
		assertToStringUtils(new UsageSiteFeature(mock(IUsageSite.class)));
	}

	@Test
	public void equality() {
		IUsageSite s = mock(IUsageSite.class);
		UsageSiteFeature a = new UsageSiteFeature(s);
		UsageSiteFeature b = new UsageSiteFeature(s);
		DataStructureEqualityAsserts.assertEqualDataStructures(a, b);
	}

	@Test
	public void equality_diff() {
		UsageSiteFeature a = new UsageSiteFeature(mock(IUsageSite.class));
		UsageSiteFeature b = new UsageSiteFeature(mock(IUsageSite.class));
		DataStructureEqualityAsserts.assertNotEqualDataStructures(a, b);
	}

	@Test(expected = AssertionException.class)
	public void fail_siteIsNull() {
		new UsageSiteFeature(null);
	}
}