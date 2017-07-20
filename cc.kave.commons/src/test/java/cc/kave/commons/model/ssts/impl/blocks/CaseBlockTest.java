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
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import cc.kave.testcommons.ToStringAssert;

public class CaseBlockTest extends SSTBaseTest {

	@Test
	public void testDefaultValues() {
		CaseBlock sut = new CaseBlock();

		assertThat(new UnknownExpression(), equalTo(sut.getLabel()));
		assertThat(new ArrayList<IStatement>(), equalTo(sut.getBody()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		CaseBlock sut = new CaseBlock();
		sut.setLabel(this.label("a"));
		sut.setBody(Lists.newArrayList(new ReturnStatement()));

		assertThat(this.label("a"), equalTo(sut.getLabel()));
		assertThat(Lists.newArrayList(new ReturnStatement()), equalTo(sut.getBody()));
	}

	@Test
	public void testEqualityDefault() {
		CaseBlock a = new CaseBlock();
		CaseBlock b = new CaseBlock();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		CaseBlock a = new CaseBlock();
		CaseBlock b = new CaseBlock();
		a.setLabel(this.label("a"));
		a.setBody(Lists.newArrayList(new ReturnStatement()));
		b.setLabel(this.label("a"));
		b.setBody(Lists.newArrayList(new ReturnStatement()));

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentLabel() {
		CaseBlock a = new CaseBlock();
		CaseBlock b = new CaseBlock();
		a.setLabel(this.label("a"));

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentBody() {
		CaseBlock a = new CaseBlock();
		CaseBlock b = new CaseBlock();
		a.setBody(Lists.newArrayList(new ReturnStatement()));

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void toStringIsImplemented() {
		ToStringAssert.assertToStringUtils(new CaseBlock());
	}
}