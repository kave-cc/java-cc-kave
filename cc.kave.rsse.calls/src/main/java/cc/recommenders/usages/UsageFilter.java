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
package cc.recommenders.usages;

import static cc.recommenders.usages.DefinitionSiteKind.UNKNOWN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Predicate;

public class UsageFilter implements Predicate<Usage> {

	private Pattern anonymousClassPattern = Pattern.compile(".*\\$[1-9]+[0-9]*$");

	@Override
	public boolean apply(Usage usage) {

		if (isArray(usage)) {
			log("is array");
			return false;
		}

		if (isAnonymousClass(usage)) {
			log("is anonymous");
			return false;
		}

		if (!hasReceiverCallSites(usage)) {
			log("no calls");
			return false;
		}

		// TODO discuss whether UNKNOWN definitions should be ignored or not!
		if (UNKNOWN.equals(usage.getDefinitionSite().getKind())) {
			log("unknown def");
			return false;
		}

		boolean isSwtWidget = usage.getType().getIdentifier().startsWith("Lorg/eclipse/swt/widgets/");
		if (!isSwtWidget) {
			log("no swt widget");
			return false;
		}

		return true;
	}

	private void log(String msg) {
		// System.err.println(msg);
	}

	private boolean isArray(Usage usage) {
		return usage.getType().getIdentifier().startsWith("[");
	}

	private boolean isAnonymousClass(Usage usage) {
		Matcher matcher = anonymousClassPattern.matcher(usage.getType().getIdentifier());
		return matcher.matches();
	}

	private static boolean hasReceiverCallSites(Usage usage) {
		return usage.getReceiverCallsites().size() > 0;
	}
}
