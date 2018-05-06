/**
 * Copyright (c) 2010, 2011 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Sebastian Proksch - initial API and implementation
 */
package cc.kave.rsse.calls.recs.pbn;

import static cc.kave.commons.model.naming.Names.newMethod;
import static cc.kave.commons.model.naming.Names.newType;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByReturnValue;
import static cc.kave.rsse.calls.recs.pbn.PBNModelConstants.DUMMY_CCTX;
import static cc.kave.rsse.calls.recs.pbn.PBNModelConstants.DUMMY_DEFINITION;
import static cc.kave.rsse.calls.recs.pbn.PBNModelConstants.DUMMY_MCTX;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PBNModelConstantsTest {

	@Test
	public void dummyCCtx() {
		assertEquals(newType("__DUMMY__, ???"), DUMMY_CCTX);
	}

	@Test
	public void dummyMCtx() {
		assertEquals(newMethod("[?] [__DUMMY__, ???].???()"), DUMMY_MCTX);
	}

	@Test
	public void dummyDef() {
		assertEquals(definedByReturnValue(DUMMY_MCTX), DUMMY_DEFINITION);
	}

	@Test
	public void stateTrue() {
		assertEquals("t", PBNModelConstants.STATE_TRUE);
	}

	@Test
	public void stateFalse() {
		assertEquals("f", PBNModelConstants.STATE_FALSE);
	}
}