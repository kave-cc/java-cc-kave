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
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import cc.kave.testcommons.ToStringAssert;

public class SwitchBlockTest extends SSTBaseTest {

	@Test
	public void testDefaultValues() {
		SwitchBlock sut = new SwitchBlock();

		assertThat(new VariableReference(), equalTo(sut.getReference()));
		assertThat(new ArrayList<IStatement>(), equalTo(sut.getSections()));
		assertThat(new ArrayList<IStatement>(), equalTo(sut.getDefaultSection()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		SwitchBlock sut = new SwitchBlock();
		sut.setReference(someVarRef("a"));
		sut.getSections().add(new CaseBlock());
		sut.getDefaultSection().add(new ReturnStatement());

		assertThat(someVarRef("a"), equalTo(sut.getReference()));
		assertThat(Lists.newArrayList(new CaseBlock()), equalTo(sut.getSections()));
		assertThat(Lists.newArrayList(new ReturnStatement()), equalTo(sut.getDefaultSection()));
	}

	@Test
	public void testChildrenIdentity() {
		SwitchBlock sut = new SwitchBlock();
		sut.setReference(someVarRef("a"));
		sut.getSections().add(new CaseBlock());
		sut.getDefaultSection().add(new ReturnStatement());

		assertChildren(sut, sut.getReference(), sut.getDefaultSection().get(0));
	}

	@Test
	public void testEqualityDefault() {
		SwitchBlock a = new SwitchBlock();
		SwitchBlock b = new SwitchBlock();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		SwitchBlock a = new SwitchBlock();
		SwitchBlock b = new SwitchBlock();
		a.setReference(someVarRef("a"));
		a.getSections().add(new CaseBlock());
		a.getDefaultSection().add(new ReturnStatement());
		b.setReference(someVarRef("a"));
		b.getSections().add(new CaseBlock());
		b.getDefaultSection().add(new ReturnStatement());

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentReference() {
		SwitchBlock a = new SwitchBlock();
		SwitchBlock b = new SwitchBlock();
		a.setReference(someVarRef("a"));

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentSections() {
		SwitchBlock a = new SwitchBlock();
		SwitchBlock b = new SwitchBlock();
		a.getSections().add(new CaseBlock());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentDefaultSection() {
		SwitchBlock a = new SwitchBlock();
		SwitchBlock b = new SwitchBlock();
		a.getDefaultSection().add(new ReturnStatement());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		SwitchBlock sut = new SwitchBlock();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testVisitorWithReturnIsImplemented() {
		// TODO: Visitor Test
	}

	@Test
	public void toStringIsImplemented() {
		ToStringAssert.assertToStringUtils(new SwitchBlock());
	}
}