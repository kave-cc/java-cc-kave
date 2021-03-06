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

import static cc.kave.commons.testing.DataStructureEqualityAsserts.assertEqualDataStructures;
import static cc.kave.commons.testing.DataStructureEqualityAsserts.assertNotEqualDataStructures;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import cc.kave.commons.exceptions.AssertionException;

public class TableTest {

	private Table sut;

	@Test
	public void tableMatrixAndFrequenciesAreNotNull() {
		sut = new Table(10);
		assertNotNull(sut.getBMNTable());
		assertNotNull(sut.getFrequencies());
	}

	@Test
	public void aTableIsEmptyByDefault() {
		sut = new Table(10);
		assertEquals(0, sut.getBMNTable().length);
	}

	@Test
	public void rowsCanBeAdded() {
		sut = new Table(3);
		sut.add(q(1, 0, 1));

		assertRows(q(1, 0, 1));
		assertFreqs(1);
	}

	@Test
	public void frequenciesAreIncreased() {
		sut = new Table(3);
		sut.add(q(1, 0, 1));
		sut.add(q(1, 0, 1));
		sut.add(q(0, 0, 1));

		assertRows(q(1, 0, 1), q(0, 0, 1));
		assertFreqs(2, 1);
	}

	@Test(expected = AssertionException.class)
	public void rowsMustHaveTheRightNumberOfColumns() {
		sut = new Table(1);
		sut.add(q(1, 0));
	}

	@Test
	public void instantiationOfNewTable() {
		boolean[][] newTable = new boolean[][] { q(1, 0, 1) };
		int[] newFreqs = new int[] { 2 };

		sut = new Table(newTable, newFreqs);

		assertArrayEquals(new boolean[][] { q(1, 0, 1) }, sut.getBMNTable());
		assertArrayEquals(new int[] { 2 }, sut.getFrequencies());
	}

	@Test
	public void instantiationOfNewTableDoesNotCloneInput() {
		boolean[][] newTable = new boolean[][] { q(1, 0, 1) };
		int[] newFreqs = new int[] { 2 };

		sut = new Table(newTable, newFreqs);

		newTable[0][0] = false;
		newFreqs[0] = 13;

		assertArrayEquals(newTable, sut.getBMNTable());
		assertArrayEquals(newFreqs, sut.getFrequencies());
	}

	@Test(expected = AssertionException.class)
	public void instantiationOfNewTable_SameDimensions() {
		sut = new Table(new boolean[1][1], new int[2]);
	}

	@Test
	public void equality_equalObjects() {
		boolean[][] ta = new boolean[][] { q(1) };
		int[] fa = new int[] { 1 };
		Table a = new Table(ta, fa);

		boolean[][] tb = new boolean[][] { q(1) };
		int[] fb = new int[] { 1 };
		Table b = new Table(tb, fb);

		assertEqualDataStructures(a, b);
	}

	@Test
	public void equality_customConstructors() {
		boolean[][] table = new boolean[][] { q(1) };
		int[] freqs = new int[] { 1 };

		Table a = new Table(table, freqs);
		Table b = new Table(1);
		b.add(q(1));

		assertEqualDataStructures(a, b);
	}

	@Test
	public void equality_diffCols() {
		boolean[][] ta = new boolean[][] { q(1) };
		int[] fa = new int[] { 1 };
		Table a = new Table(ta, fa);

		boolean[][] tb = new boolean[][] { q(2) };
		int[] fb = new int[] { 1 };
		Table b = new Table(tb, fb);

		assertNotEqualDataStructures(a, b);
	}

	@Test
	public void equality_diffFreq() {
		boolean[][] ta = new boolean[][] { q(1) };
		int[] fa = new int[] { 1 };
		Table a = new Table(ta, fa);

		boolean[][] tb = new boolean[][] { q(1) };
		int[] fb = new int[] { 2 };
		Table b = new Table(tb, fb);

		assertNotEqualDataStructures(a, b);
	}

	@Test
	public void sizeCalculation_0() {
		int actual = createTable(0, 1).getSize();
		int expected = 0;
		assertEquals(expected, actual);
	}

	@Test
	public void sizeCalculation_0b() {
		int actual = createTable(1, 0).getSize();
		int expected = 4; // 4 freq + 0.0.. features
		assertEquals(expected, actual);
	}

	@Test
	public void sizeCalculation_1() {
		int actual = createTable(1, 1).getSize();
		int expected = 5; // 4 freq + 1 features
		assertEquals(expected, actual);
	}

	@Test
	public void sizeCalculation_2() {
		int actual = createTable(1, 8).getSize();
		int expected = 5; // 4 freq + 1 features
		assertEquals(expected, actual);
	}

	@Test
	public void sizeCalculation_3() {
		int actual = createTable(2, 13).getSize();
		int expected = 12; // 26bit~4Byte + 2*4Byte freq
		assertEquals(expected, actual);
	}

	@Test
	public void sizeCalculation_4() {
		int actual = createTable(10, 17).getSize();
		int expected = 40 + 22; // 10 freqs + 170bit/8 ~ 22Byte
		assertEquals(expected, actual);
	}

	private static Table createTable(int numRows, int numFeatures) {
		int[] freqs = new int[numRows];
		boolean[][] arr = new boolean[numRows][];
		for (int row = 0; row < numRows; row++) {
			arr[row] = new boolean[numFeatures];
		}
		return new Table(arr, freqs);
	}

	private static boolean[] q(int... values) {
		boolean[] res = new boolean[values.length];
		for (int i = 0; i < values.length; i++) {
			res[i] = values[i] == 1;
		}
		return res;
	}

	private void assertFreqs(int... expectedFreqs) {
		assertArrayEquals(expectedFreqs, sut.getFrequencies());
	}

	private void assertRows(boolean[]... expectedTable) {
		assertArrayEquals(expectedTable, sut.getBMNTable());
	}
}