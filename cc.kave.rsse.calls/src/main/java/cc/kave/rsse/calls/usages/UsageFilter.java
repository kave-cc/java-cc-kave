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
package cc.kave.rsse.calls.usages;

import static cc.kave.rsse.calls.usages.DefinitionSiteKind.UNKNOWN;

import com.google.common.base.Predicate;

public class UsageFilter implements Predicate<Usage> {

	@Override
	public boolean apply(Usage usage) {

		if (usage.getType().isArray()) {
			log("is array");
			return false;
		}

		if (!hasReceiverCallSites(usage)) {
			log("no calls");
			return false;
		}

		// TODO discuss whether UNKNOWN definitions should be ignored or not!
		// in general yes, but make sure this does not filter actual queries
		if (UNKNOWN.equals(usage.getDefinitionSite().getKind())) {
			log("unknown def");
			return false;
		}

		return true;
	}

	private void log(String msg) {
		// System.err.println(msg);
	}

	private static boolean hasReceiverCallSites(Usage usage) {
		return usage.getReceiverCallsites().size() > 0;
	}
}
