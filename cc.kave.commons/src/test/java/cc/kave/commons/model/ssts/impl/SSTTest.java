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
package cc.kave.commons.model.ssts.impl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;

import org.junit.Test;

import com.google.common.collect.Sets;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.ssts.declarations.IDelegateDeclaration;
import cc.kave.commons.model.ssts.declarations.IEventDeclaration;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.DelegateDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.EventDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.FieldDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.PropertyDeclaration;
import cc.kave.testcommons.ToStringAssert;

public class SSTTest {

	@Test
	public void testDefaultValues() {
		SST sut = new SST();

		assertThat(Names.getUnknownType(), equalTo(sut.getEnclosingType()));
		assertThat(null, equalTo(sut.getPartialClassIdentifier()));
		assertFalse(sut.isPartialClass());
		assertThat(new HashSet<IDelegateDeclaration>(), equalTo(sut.getDelegates()));
		assertThat(new HashSet<IEventDeclaration>(), equalTo(sut.getEvents()));
		assertThat(new HashSet<IFieldDeclaration>(), equalTo(sut.getFields()));
		assertThat(new HashSet<IMethodDeclaration>(), equalTo(sut.getEntryPoints()));
		assertThat(new HashSet<IPropertyDeclaration>(), equalTo(sut.getProperties()));
		assertThat(new HashSet<IMethodDeclaration>(), equalTo(sut.getMethods()));
		assertThat(new HashSet<IMethodDeclaration>(), equalTo(sut.getNonEntryPoints()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		SST sut = new SST();
		sut.setEnclosingType(Names.newType("T1, P1"));
		sut.getDelegates().add(new DelegateDeclaration());
		sut.getFields().add(new FieldDeclaration());
		sut.getEvents().add(new EventDeclaration());
		sut.getMethods().add(new MethodDeclaration());
		sut.getProperties().add(new PropertyDeclaration());
		sut.setPartialClassIdentifier("abc");

		assertThat(Names.newType("T1, P1"), equalTo(sut.getEnclosingType()));
		assertThat("abc", equalTo(sut.getPartialClassIdentifier()));
		assertTrue(sut.isPartialClass());
		assertThat(Sets.newHashSet(new DelegateDeclaration()), equalTo(sut.getDelegates()));
		assertThat(Sets.newHashSet(new FieldDeclaration()), equalTo(sut.getFields()));
		assertThat(Sets.newHashSet(new EventDeclaration()), equalTo(sut.getEvents()));
		assertThat(Sets.newHashSet(new MethodDeclaration()), equalTo(sut.getMethods()));
		assertThat(Sets.newHashSet(new PropertyDeclaration()), equalTo(sut.getProperties()));
	}

	@Test
	public void testEqualityDefault() {
		SST a = new SST();
		SST b = new SST();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityOfLegacyPartialClassIdentifier() {
		SST a = new SST();
		a.setPartialClassIdentifier(null);
		SST b = new SST();
		a.setPartialClassIdentifier("");

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));

		assertThat(b, equalTo(a));
		assertThat(b.hashCode(), equalTo(a.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		SST a = new SST();
		SST b = new SST();
		a.setEnclosingType(Names.newType("T1, P1"));
		a.setPartialClassIdentifier("abc");
		a.getDelegates().add(new DelegateDeclaration());
		a.getFields().add(new FieldDeclaration());
		a.getEvents().add(new EventDeclaration());
		a.getMethods().add(new MethodDeclaration());
		a.getProperties().add(new PropertyDeclaration());
		b.setEnclosingType(Names.newType("T1, P1"));
		b.setPartialClassIdentifier("abc");
		b.getDelegates().add(new DelegateDeclaration());
		b.getFields().add(new FieldDeclaration());
		b.getEvents().add(new EventDeclaration());
		b.getMethods().add(new MethodDeclaration());
		b.getProperties().add(new PropertyDeclaration());

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentType() {
		SST a = new SST();
		SST b = new SST();
		a.setEnclosingType(Names.newType("T1, P1"));

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityPartialClassIdentifier() {
		SST a = new SST();
		SST b = new SST();
		a.setPartialClassIdentifier("abc");

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentDelegates() {
		SST a = new SST();
		SST b = new SST();
		a.getDelegates().add(new DelegateDeclaration());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentEvents() {
		SST a = new SST();
		SST b = new SST();
		a.getEvents().add(new EventDeclaration());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentFields() {
		SST a = new SST();
		SST b = new SST();
		a.getFields().add(new FieldDeclaration());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentMethods() {
		SST a = new SST();
		SST b = new SST();
		a.getMethods().add(new MethodDeclaration());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentProperties() {
		SST a = new SST();
		SST b = new SST();
		a.getProperties().add(new PropertyDeclaration());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEntryPointFiltering() {
		MethodDeclaration ep = new MethodDeclaration();
		ep.setEntryPoint(true);
		MethodDeclaration nep = new MethodDeclaration();
		nep.setEntryPoint(false);
		SST sut = new SST();
		sut.getMethods().add(ep);
		sut.getMethods().add(nep);

		assertThat(Sets.newHashSet(ep), equalTo(sut.getEntryPoints()));
		assertThat(Sets.newHashSet(nep), equalTo(sut.getNonEntryPoints()));
	}

	@Test
	public void testVisitorIsImplemented() {
		SST sut = new SST();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testWithReturnIsImplemented() {
		// TODO : Visitor Test
	}

	@Test
	public void toStringIsImplemented() {
		ToStringAssert.assertToStringUtils(new SST());
	}
}