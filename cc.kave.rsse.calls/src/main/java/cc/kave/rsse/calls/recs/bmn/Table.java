/**
 * Copyright 2010 Technische Universität Darmstadt
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
package cc.kave.rsse.calls.recs.bmn;

import static cc.kave.commons.assertions.Asserts.assertEquals;

import java.util.Arrays;

public class Table {

	private int numOfCols;
	private boolean[][] bmnTable = new boolean[0][0];
	private int[] frequencies = new int[0];

	public Table(int bmnTableSize) {
		this.numOfCols = bmnTableSize;
	}

	public Table(boolean[][] bmnTable, int[] frequencies) {
		assertEquals(bmnTable.length, frequencies.length);
		this.numOfCols = bmnTable[0].length;
		this.bmnTable = bmnTable;
		this.frequencies = frequencies;// .clone();
	}

	public void add(boolean[] row) {
		assertEquals(numOfCols, row.length);

		int index = findRow(row);

		if (index == -1) {
			int totalNumOfRows = bmnTable.length;

			boolean[][] newTable = new boolean[totalNumOfRows + 1][numOfCols];
			System.arraycopy(bmnTable, 0, newTable, 0, bmnTable.length);
			newTable[totalNumOfRows] = row;

			bmnTable = newTable;

			int[] newFreq = new int[totalNumOfRows + 1];
			System.arraycopy(frequencies, 0, newFreq, 0, frequencies.length);
			newFreq[totalNumOfRows] = 1;

			frequencies = newFreq;
		} else {
			frequencies[index]++;
		}
	}

	private int findRow(boolean[] row) {
		for (int i = 0; i < bmnTable.length; i++) {
			boolean[] cur = bmnTable[i];
			if (Arrays.equals(cur, row)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * do not alter the array that is returned here! you have access to the internal
	 * version, for performance reasons
	 * 
	 * @return the internal values of the BMN model
	 */
	public boolean[][] getBMNTable() {
		return bmnTable;
	}

	public int[] getRowFrequencies() {
		return frequencies.clone();
	}

	public int[] getFreqs() {
		return frequencies;
	}

	/**
	 * Calculate the memory size of this table.
	 * 
	 * @return size of the table, in Byte
	 */
	public int getSize() {
		// if(bmnTable.length == 0 || bmnTable[0].length == 0) {
		// return 0;
		// }
		int numRows = bmnTable.length;
		int numCols = bmnTable[0].length;
		double rowSizeInByte = (numCols + 32) / 8d;
		return (int) Math.round(Math.ceil(numRows * rowSizeInByte));
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Table other = (Table) obj;
		if (!Arrays.deepEquals(bmnTable, other.bmnTable))
			return false;
		if (!Arrays.equals(frequencies, other.frequencies))
			return false;
		if (numOfCols != other.numOfCols)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		for (int rowIdx = 0; rowIdx < bmnTable.length; rowIdx++) {
			result = prime * result + Arrays.hashCode(bmnTable[rowIdx]);
		}
		result = prime * result + Arrays.hashCode(frequencies);
		result = prime * result + numOfCols;
		return result;
	}
}