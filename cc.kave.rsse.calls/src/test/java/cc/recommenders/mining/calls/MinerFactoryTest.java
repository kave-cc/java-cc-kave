/**
 * Copyright (c) 2011-2013 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Sebastian Proksch - initial API and implementation
 */
package cc.recommenders.mining.calls;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.exceptions.AssertionException;
import cc.recommenders.mining.calls.MiningOptions.Algorithm;
import cc.recommenders.mining.calls.bmn.BMNMiner;
import cc.recommenders.mining.calls.pbn.PBNMiner;
import cc.recommenders.usages.Query;
import cc.recommenders.usages.Usage;

public class MinerFactoryTest {

	private BMNMiner bmnMiner;
	private PBNMiner pbnMiner;
	private MiningOptions mOpts;
	private MinerFactory sut;

	@Before
	public void set() {
		bmnMiner = mock(BMNMiner.class);
		pbnMiner = mock(PBNMiner.class);
		mOpts = new MiningOptions();

		sut = new MinerFactory(mOpts, bmnMiner, pbnMiner);
	}

	@Test
	public void bmn() {
		mOpts.setAlgorithm(Algorithm.BMN);
		Miner<Usage, Query> actual = sut.get();
		Miner<Usage, Query> expected = bmnMiner;
		assertEquals(expected, actual);
	}

	@Test
	public void pbn() {
		mOpts.setAlgorithm(Algorithm.CANOPY);
		Miner<Usage, Query> actual = sut.get();
		Miner<Usage, Query> expected = pbnMiner;
		assertEquals(expected, actual);
	}

	@Test(expected = AssertionException.class)
	public void elsewise() {
		mOpts.setAlgorithm(Algorithm.CALLGROUP);
		sut.get();
	}
}