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
package cc.kave.rsse.calls.mining;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.exceptions.AssertionException;
import cc.kave.rsse.calls.bmn.BMNMiner;
import cc.kave.rsse.calls.mining.Miner;
import cc.kave.rsse.calls.mining.MinerFactory;
import cc.kave.rsse.calls.options.MiningOptions;
import cc.kave.rsse.calls.options.MiningOptions.Algorithm;
import cc.kave.rsse.calls.pbn.PBNMiner;
import cc.kave.rsse.calls.usages.Usage;
import cc.kave.rsse.calls.usages.IUsage;

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
		Miner<IUsage, Usage> actual = sut.get();
		Miner<IUsage, Usage> expected = bmnMiner;
		assertEquals(expected, actual);
	}

	@Test
	public void pbn() {
		mOpts.setAlgorithm(Algorithm.CANOPY);
		Miner<IUsage, Usage> actual = sut.get();
		Miner<IUsage, Usage> expected = pbnMiner;
		assertEquals(expected, actual);
	}

	@Test(expected = AssertionException.class)
	public void elsewise() {
		mOpts.setAlgorithm(Algorithm.CALLGROUP);
		sut.get();
	}
}