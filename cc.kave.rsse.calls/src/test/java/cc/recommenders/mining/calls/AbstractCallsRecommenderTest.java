/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.recommenders.mining.calls;

import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.exceptions.AssertionException;
import cc.kave.commons.model.ssts.ISST;

public class AbstractCallsRecommenderTest {

	private TestRecommender sut;

	@Before
	public void setup() {
		sut = new TestRecommender();
	}

	@Test(expected = AssertionException.class)
	public void queryingFails() {
		sut.query((ISST) null);
	}

	// TODO: what about the other query calls? check for null there too

	@Test(expected = AssertionException.class)
	public void gettingSizeFails() {
		sut.getSize();
	}

	private class TestRecommender extends AbstractCallsRecommender<String> {
	}
}