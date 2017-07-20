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
package cc.kave.commons.model.ssts.impl.declarations;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IEventName;
import cc.kave.commons.model.ssts.impl.SSTTestHelper;
import cc.kave.testcommons.ToStringAssert;

public class EventDeclarationTest {

	@Test
	public void testDefaultValues() {
		EventDeclaration sut = new EventDeclaration();

		assertThat(Names.getUnknownEvent(), equalTo(sut.getName()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		EventDeclaration sut = new EventDeclaration();
		sut.setName(someEvent());

		assertThat(someEvent(), equalTo(sut.getName()));
	}

	@Test
	public void testEqualityDefault() {
		EventDeclaration a = new EventDeclaration();
		EventDeclaration b = new EventDeclaration();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		EventDeclaration a = new EventDeclaration();
		EventDeclaration b = new EventDeclaration();
		a.setName(someEvent());
		b.setName(someEvent());

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentType() {
		EventDeclaration a = new EventDeclaration();
		EventDeclaration b = new EventDeclaration();
		a.setName(someEvent());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		EventDeclaration sut = new EventDeclaration();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testWithReturnIsImplemented() {
		// TODO : Visitor Test
	}

	private IEventName someEvent() {
		return Names.newEvent("[T1,P1] [T2,P2].Event");
	}

	@Test
	public void toStringIsImplemented() {
		ToStringAssert.assertToStringUtils(new EventDeclaration());
	}
}