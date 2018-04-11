/**
 * Copyright 2016 Simon Reuß
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package cc.kave.rsse.calls.extraction.usages;

import java.util.List;
import java.util.Set;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.pointsto.analysis.PointsToAnalysis;
import cc.kave.rsse.calls.usages.model.IUsage;
import cc.kave.rsse.calls.usages.model.IUsageSite;
import cc.kave.rsse.calls.usages.model.impl.Definition;
import cc.kave.rsse.calls.usages.model.impl.Definitions;
import cc.kave.rsse.calls.usages.model.impl.Usage;
import cc.kave.rsse.calls.usages.model.impl.UsageSite;
import cc.kave.rsse.calls.usages.model.impl.UsageSites;

public abstract class UsageExtractionTest extends TestBuilder {

	protected PointsToUsageExtractor createExtractor() {
		return new PointsToUsageExtractor();
	}

	protected abstract PointsToAnalysis createAnalysis();

	public List<IUsage> extract(Context context) {
		PointsToUsageExtractor extractor = createExtractor();
		return extractor.extract(createAnalysis().compute(context));
	}

	public IUsage usage(ITypeName type, IMethodName methodContext, ITypeName classContext, Definition definitionSite,
			Set<UsageSite> callSites) {
		Usage query = new Usage();
		query.type = type;
		query.methodCtx = methodContext;
		query.classCtx = classContext;
		query.definition = definitionSite;
		query.usageSites.addAll(callSites);
		return query;
	}

	public IUsageSite callSite(IMethodName method) {
		return UsageSites.call(method);
	}

	public IUsageSite callSite(IMethodName method, int index) {
		return UsageSites.callParameter(method, index);
	}

	public Definition parameterDefinitionSite(IMethodName method, int index) {
		return Definitions.definedByMethodParameter(method, index);
	}

	public Definition fieldDefinitionSite(IFieldName field) {
		return Definitions.definedByMemberAccess(field);
	}

	public Definition propertyDefinitionSite(IPropertyName property) {
		return Definitions.definedByMemberAccess(property);
	}

	public Definition returnDefinitionSite(IMethodName method) {
		return Definitions.definedByReturnValue(method);
	}
}