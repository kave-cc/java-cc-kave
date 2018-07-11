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
package cc.kave.rsse.calls.recs.pbn;

import static cc.kave.rsse.calls.utils.OptionsBuilder.bmn;
import static cc.kave.rsse.calls.utils.OptionsBuilder.pbn;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.exceptions.AssertionException;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.rsse.calls.model.usages.impl.Usage;

public class PBNRecommenderInstanceTest {

	private PBNModel m;
	private PBNRecommenderInstance sut;

	@Before
	public void setup() {
		m = new PBNModel();
		sut = new PBNRecommenderInstance(m, pbn(1).get());
	}

	@Test(expected = AssertionException.class)
	public void cannotUseNonPBNOptions() {
		new PBNRecommenderInstance(m, bmn().get());
	}

	@Test(expected = AssertionException.class)
	public void fail_queryIsNull() {
		sut.query(null);
	}

	@Test(expected = AssertionException.class)
	public void fail_queryForOtherType() {
		m.type = mock(ITypeName.class);
		sut = new PBNRecommenderInstance(m, pbn(1).get());

		Usage u = new Usage();
		u.type = mock(ITypeName.class);
		sut.query(u);
	}
}