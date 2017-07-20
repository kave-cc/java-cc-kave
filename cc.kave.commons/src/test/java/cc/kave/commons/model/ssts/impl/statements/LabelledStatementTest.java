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
package cc.kave.commons.model.ssts.impl.statements;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import cc.kave.commons.model.ssts.impl.SSTTestHelper;
import cc.kave.testcommons.ToStringAssert;

public class LabelledStatementTest {

	@Test
	public void testDefaultValues() {
		LabelledStatement sut = new LabelledStatement();

		assertThat("", equalTo(sut.getLabel()));
		assertThat(new UnknownStatement(), equalTo(sut.getStatement()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		LabelledStatement sut = new LabelledStatement();
		sut.setLabel("a");
		sut.setStatement(new BreakStatement());

		assertThat(new BreakStatement(), equalTo(sut.getStatement()));
		assertThat("a", equalTo(sut.getLabel()));
	}

	@Test
	public void testEqualityDefault() {
		LabelledStatement a = new LabelledStatement();
		LabelledStatement b = new LabelledStatement();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		LabelledStatement a = new LabelledStatement();
		LabelledStatement b = new LabelledStatement();
		a.setLabel("a");
		b.setLabel("a");
		a.setStatement(new BreakStatement());
		b.setStatement(new BreakStatement());

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentLabel() {
		LabelledStatement a = new LabelledStatement();
		LabelledStatement b = new LabelledStatement();
		a.setLabel("a");
		b.setLabel("b");

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentStatement() {
		LabelledStatement a = new LabelledStatement();
		LabelledStatement b = new LabelledStatement();
		a.setStatement(new BreakStatement());
		b.setStatement(new ContinueStatement());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		LabelledStatement sut = new LabelledStatement();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testWithReturnIsImplemented() {
		// TODO : Visitor Test
	}

	@Test
	public void toStringIsImplemented() {
		ToStringAssert.assertToStringUtils(new LabelledStatement());
	}
}