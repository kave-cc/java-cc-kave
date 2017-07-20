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
package cc.kave.commons.model.ssts.impl.blocks;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;

import org.junit.Test;

import com.google.common.collect.Lists;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.impl.SSTBaseTest;
import cc.kave.commons.model.ssts.impl.SSTTestHelper;
import cc.kave.commons.model.ssts.impl.statements.BreakStatement;
import cc.kave.testcommons.ToStringAssert;

public class UncheckBlockTest extends SSTBaseTest {

	@Test
	public void testDefaultValues() {
		UncheckedBlock sut = new UncheckedBlock();

		assertThat(new ArrayList<IStatement>(), equalTo(sut.getBody()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		UncheckedBlock sut = new UncheckedBlock();
		sut.getBody().add(new BreakStatement());

		assertThat(Lists.newArrayList(new BreakStatement()), equalTo(sut.getBody()));
	}

	@Test
	public void testChildrenIdentity() throws Exception {
		UncheckedBlock sut = new UncheckedBlock();
		sut.getBody().add(new BreakStatement());

		assertChildren(sut, sut.getBody().get(0));
	}

	@Test
	public void testEqualityDefault() {
		UncheckedBlock a = new UncheckedBlock();
		UncheckedBlock b = new UncheckedBlock();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		UncheckedBlock a = new UncheckedBlock();
		UncheckedBlock b = new UncheckedBlock();
		a.getBody().add(new BreakStatement());
		b.getBody().add(new BreakStatement());
		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentBody() {
		UncheckedBlock a = new UncheckedBlock();
		UncheckedBlock b = new UncheckedBlock();
		a.getBody().add(new BreakStatement());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		UncheckedBlock sut = new UncheckedBlock();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testVisitorWithReturnIsImplemented() {
		// TODO: Visitor Test
	}

	@Test
	public void toStringIsImplemented() {
		ToStringAssert.assertToStringUtils(new UncheckedBlock());
	}
}