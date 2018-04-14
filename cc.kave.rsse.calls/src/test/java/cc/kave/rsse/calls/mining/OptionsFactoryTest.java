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
package cc.kave.rsse.calls.mining;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

import org.junit.Test;

public class OptionsFactoryTest {

	@Test(expected = IllegalStateException.class)
	public void notInitializedByDefault() {
		new OptionsFactory().get();
	}

	@Test(expected = IllegalArgumentException.class)
	public void customCtorCannotBeInitializedWithNull() {
		new OptionsFactory(null);
	}

	@Test
	public void customCtor() {
		Options in = mock(Options.class);
		assertSame(in, new OptionsFactory(in).get());
	}

	@Test
	public void canBeSet() {
		Options in = mock(Options.class);

		OptionsFactory sut = new OptionsFactory();
		sut.set(in);

		assertSame(in, sut.get());
	}

	@Test(expected = IllegalArgumentException.class)
	public void cannotBeSetToNyll() {
		new OptionsFactory().set(null);
	}
}