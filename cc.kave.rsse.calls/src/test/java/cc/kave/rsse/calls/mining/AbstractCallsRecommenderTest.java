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
package cc.kave.rsse.calls.mining;

import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.exceptions.AssertionException;
import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.rsse.calls.AbstractCallsRecommender;

public class AbstractCallsRecommenderTest {

	private TestRecommender sut;

	@Before
	public void setup() {
		sut = new TestRecommender();
	}

	@Test(expected = AssertionException.class)
	public void assertThatDefaultCallConcatenationsActuallyMakeSense() {

	}

	// TODO: what about the other query calls? check for null there too

	private class TestRecommender extends AbstractCallsRecommender<String> {

		@Override
		public Set<Pair<IMethodName, Double>> query(Context ctx) {
			return null;
		}

		@Override
		public Set<Pair<IMethodName, Double>> query(String query) {
			return null;
		}

		@Override
		public int getSize() {
			return 0;
		}
	}
}