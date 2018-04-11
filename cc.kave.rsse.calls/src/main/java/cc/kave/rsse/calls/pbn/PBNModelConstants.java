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
package cc.kave.rsse.calls.pbn;

import static java.lang.String.format;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.rsse.calls.usages.features.CallFeature;
import cc.kave.rsse.calls.usages.features.ClassFeature;
import cc.kave.rsse.calls.usages.features.DefinitionFeature;
import cc.kave.rsse.calls.usages.features.FirstMethodFeature;
import cc.kave.rsse.calls.usages.features.ParameterFeature;
import cc.kave.rsse.calls.usages.features.UsageFeature;
import cc.kave.rsse.calls.usages.features.UsageFeature.ObjectUsageFeatureVisitor;
import cc.kave.rsse.calls.usages.model.IDefinition;
import cc.kave.rsse.calls.usages.model.impl.Definitions;

public class PBNModelConstants {

	public static final String PATTERN_TITLE = "patterns";
	public static final String CLASS_CONTEXT_TITLE = "inClass";
	public static final String METHOD_CONTEXT_TITLE = "inMethod";
	public static final String DEFINITION_TITLE = "definition";

	public static final String STATE_TRUE = "t";
	public static final String STATE_FALSE = "f";

	public static final ITypeName DUMMY_TYPE = Names.newType("__DUMMY__, ???");
	public static final IMethodName DUMMY_METHOD = Names.newMethod("[?] [__DUMMY__, ???].???()");
	public static final IFieldName DUMMY_FIELD = Names.newField("[?] [__DUMMY__, ???].???");
	public static final IDefinition DUMMY_DEFINITION = Definitions.definedByReturnValue(DUMMY_METHOD);

	public static final ITypeName UNKNOWN_TYPE = Names.getUnknownType();
	public static final IMethodName UNKNOWN_METHOD = Names.getUnknownMethod();
	public static final IDefinition UNKNOWN_DEFINITION = Definitions.definedByUnknown();

	public static final String CALL_PREFIX = "C:";
	public static final String PARAMETER_PREFIX = "P:";

	public static String newClassContext(ITypeName type) {
		return type.getIdentifier();
	}

	public static String newMethodContext(IMethodName method) {
		return method.getIdentifier();
	}

	public static String newDefinition(IDefinition definitionSite) {
		return definitionSite.toString();
	}

	public static String newParameterSite(IMethodName param, int argNum) {
		return format("%s%s#%d", PARAMETER_PREFIX, param.getIdentifier(), argNum);
	}

	public static String newCallSite(IMethodName site) {
		return format("%s%s", CALL_PREFIX, site.getIdentifier());
	}

	public static String getTitle(UsageFeature f) {
		final String[] title = new String[] { "title is unset" };
		f.accept(new ObjectUsageFeatureVisitor() {
			@Override
			public void visit(CallFeature f) {
				title[0] = newCallSite(f.getMethodName());
			}

			@Override
			public void visit(ClassFeature f) {
				title[0] = newClassContext(f.getTypeName());
			}

			@Override
			public void visit(DefinitionFeature f) {
				title[0] = newDefinition(f.getDefinitionSite());

			}

			@Override
			public void visit(FirstMethodFeature f) {
				title[0] = newMethodContext(f.getMethodName());
			}

			@Override
			public void visit(ParameterFeature f) {
				title[0] = newParameterSite(f.getMethodName(), f.getArgNum());
			}
		});
		return title[0];
	}
}