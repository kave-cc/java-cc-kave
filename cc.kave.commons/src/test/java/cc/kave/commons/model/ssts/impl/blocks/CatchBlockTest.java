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

import org.junit.Test;

import com.google.common.collect.Lists;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.ssts.blocks.CatchBlockKind;
import cc.kave.commons.model.ssts.impl.SSTBaseTest;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import cc.kave.testcommons.ToStringAssert;

public class CatchBlockTest extends SSTBaseTest {
	@Test
	public void testDefaultValues() {
		CatchBlock sut = new CatchBlock();

		assertThat(Names.getUnknownParameter(), equalTo(sut.getParameter()));
		assertThat(Lists.newArrayList(), equalTo(sut.getBody()));
		assertThat(CatchBlockKind.Default, equalTo(sut.getKind()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		CatchBlock sut = new CatchBlock();
		sut.setParameter(someParameter());
		sut.getBody().add(new ReturnStatement());
		sut.setKind(CatchBlockKind.General);

		assertThat(Lists.newArrayList(new ReturnStatement()), equalTo(sut.getBody()));
		assertThat(someParameter(), equalTo(sut.getParameter()));
		assertThat(CatchBlockKind.General, equalTo(sut.getKind()));
	}

	@Test
	public void testEqualityDefault() {
		CatchBlock a = new CatchBlock();
		CatchBlock b = new CatchBlock();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		CatchBlock a = new CatchBlock();
		CatchBlock b = new CatchBlock();
		a.setParameter(someParameter());
		a.getBody().add(new ReturnStatement());
		a.setKind(CatchBlockKind.General);
		b.setParameter(someParameter());
		b.getBody().add(new ReturnStatement());
		b.setKind(CatchBlockKind.General);
		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentParameter() {
		CatchBlock a = new CatchBlock();
		CatchBlock b = new CatchBlock();
		a.setParameter(someParameter());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentBody() {
		CatchBlock a = new CatchBlock();
		CatchBlock b = new CatchBlock();
		a.getBody().add(new ReturnStatement());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentKind() {
		CatchBlock a = new CatchBlock();
		CatchBlock b = new CatchBlock();
		a.setKind(CatchBlockKind.General);

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEnumNumberingIsStable() {
		assertThat(0, equalTo(CatchBlockKind.Default.ordinal()));
		assertThat(1, equalTo(CatchBlockKind.Unnamed.ordinal()));
		assertThat(2, equalTo(CatchBlockKind.General.ordinal()));
	}

	@Test
	public void toStringIsImplemented() {
		ToStringAssert.assertToStringUtils(new CatchBlock());
	}
}