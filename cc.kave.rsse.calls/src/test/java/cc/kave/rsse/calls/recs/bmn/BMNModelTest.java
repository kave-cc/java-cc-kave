/**
 * Copyright (c) 2010, 2011 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Ervina Cergani - initial API and implementation
 *     Sebastian Proksch - initial API and implementation
 */
package cc.kave.rsse.calls.recs.bmn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.testing.ToStringAsserts;
import cc.kave.rsse.calls.model.Dictionary;

@SuppressWarnings("unchecked")
public class BMNModelTest {

	private BMNModel a;
	private BMNModel b;

	@Before
	public void setup() {
		a = new BMNModel();
		b = new BMNModel();
	}

	@Test
	public void equality_no() {
		a.dictionary = mock(Dictionary.class);
		a.table = mock(Table.class);
		b.dictionary = mock(Dictionary.class);
		b.table = mock(Table.class);

		assertNotEquals(a, b);
		assertFalse(a.hashCode() == b.hashCode());
	}

	@Test
	public void equality_onlySameDict() {
		a.dictionary = mock(Dictionary.class);
		a.table = mock(Table.class);
		b.dictionary = a.dictionary;
		b.table = mock(Table.class);

		assertNotEquals(a, b);
		assertFalse(a.hashCode() == b.hashCode());
	}

	@Test
	public void equality_onlySameTable() {
		a.dictionary = mock(Dictionary.class);
		a.table = mock(Table.class);
		b.dictionary = mock(Dictionary.class);
		b.table = a.table;

		assertNotEquals(a, b);
		assertFalse(a.hashCode() == b.hashCode());
	}

	@Test
	public void equality_sameModel() {
		a.dictionary = mock(Dictionary.class);
		a.table = mock(Table.class);
		b.dictionary = a.dictionary;
		b.table = a.table;

		assertEquals(a, b);
		assertTrue(a.hashCode() == b.hashCode());
	}

	@Test
	public void toStringIsImplemented() {
		ToStringAsserts.assertToStringUtils(new BMNModel());
	}
}