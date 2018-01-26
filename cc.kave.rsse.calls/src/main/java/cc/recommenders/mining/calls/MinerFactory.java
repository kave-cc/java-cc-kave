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

import cc.kave.commons.exceptions.AssertionException;
import cc.recommenders.mining.calls.bmn.BMNMiner;
import cc.recommenders.mining.calls.pbn.PBNMiner;
import cc.recommenders.usages.Query;
import cc.recommenders.usages.Usage;

import com.google.inject.Inject;

public class MinerFactory {

	private MiningOptions mOpts;
	private BMNMiner bmnMiner;
	private PBNMiner pbnMiner;

	@Inject
	public MinerFactory(MiningOptions mOpts, BMNMiner bmnMiner, PBNMiner pbnMiner) {
		this.mOpts = mOpts;
		this.bmnMiner = bmnMiner;
		this.pbnMiner = pbnMiner;
	}

	public Miner<Usage, Query> get() {
		switch (mOpts.getAlgorithm()) {
		case BMN:
			return bmnMiner;
		case CANOPY:
			return pbnMiner;
		default:
			throw new AssertionException("unsupported options: " + mOpts);
		}
	}
}