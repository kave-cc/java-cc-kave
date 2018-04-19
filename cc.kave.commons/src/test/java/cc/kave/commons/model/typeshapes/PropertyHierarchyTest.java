/**
 * Copyright 2018 University of Zurich
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
package cc.kave.commons.model.typeshapes;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IPropertyName;

public class PropertyHierarchyTest {

	@Test
	public void testDefaultValues() {
		PropertyHierarchy sut = new PropertyHierarchy();
		assertThat(Names.getUnknownProperty(), equalTo(sut.getElement()));
		assertNull(sut.getSuper());
		assertNull(sut.getFirst());
		assertFalse(sut.isDeclaredInParentHierarchy());
	}

	@Test
	public void builderSetters() {
		IPropertyName e = mock(IPropertyName.class);
		IPropertyName s = mock(IPropertyName.class);
		IPropertyName f = mock(IPropertyName.class);

		IMemberHierarchy<IPropertyName> sut = new PropertyHierarchy(e);
		IMemberHierarchy<IPropertyName> sut2 = sut.setSuper(s);
		IMemberHierarchy<IPropertyName> sut3 = sut2.setFirst(f);
		assertSame(sut, sut2);
		assertSame(sut2, sut3);

		assertSame(e, sut.getElement());
		assertSame(s, sut.getSuper());
		assertSame(f, sut.getFirst());
	}

	@Ignore
	@Test
	public void testMe() {
		Assert.fail();
	}
}