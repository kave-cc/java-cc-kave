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

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.rsse.calls.model.usages.IDefinition;
import cc.kave.rsse.calls.model.usages.impl.Definitions;

public class PBNModelConstants {

	public static final ITypeName DUMMY_CCTX = Names.newType("__DUMMY__, ???");
	public static final IMethodName DUMMY_MCTX = Names.newMethod("[?] [__DUMMY__, ???].???()");
	public static final IDefinition DUMMY_DEFINITION = Definitions.definedByReturnValue(DUMMY_MCTX);

	public static final String STATE_TRUE = "t";
	public static final String STATE_FALSE = "f";
}