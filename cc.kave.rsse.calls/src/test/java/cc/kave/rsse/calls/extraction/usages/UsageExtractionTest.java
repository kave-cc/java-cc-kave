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
import cc.kave.rsse.calls.extraction.usages.PointsToUsageExtractor;
import cc.kave.rsse.calls.usages.CallSite;
import cc.kave.rsse.calls.usages.CallSites;
import cc.kave.rsse.calls.usages.DefinitionSite;
import cc.kave.rsse.calls.usages.DefinitionSites;
import cc.kave.rsse.calls.usages.Query;
import cc.kave.rsse.calls.usages.Usage;

public abstract class UsageExtractionTest extends TestBuilder {

	protected PointsToUsageExtractor createExtractor() {
		return new PointsToUsageExtractor();
	}

	protected abstract PointsToAnalysis createAnalysis();

	public List<Usage> extract(Context context) {
		PointsToUsageExtractor extractor = createExtractor();
		return extractor.extract(createAnalysis().compute(context));
	}

	public Usage usage(ITypeName type, IMethodName methodContext, ITypeName classContext, DefinitionSite definitionSite,
			Set<CallSite> callSites) {
		Query query = new Query();
		query.setType(type);
		query.setMethodContext(methodContext);
		query.setClassContext(classContext);
		query.setDefinition(definitionSite);
		query.setAllCallsites(callSites);
		return query;
	}

	public CallSite callSite(IMethodName method) {
		return CallSites.createReceiverCallSite(method);
	}

	public CallSite callSite(IMethodName method, int index) {
		return CallSites.createParameterCallSite(method, index);
	}

	public DefinitionSite parameterDefinitionSite(IMethodName method, int index) {
		return DefinitionSites.createDefinitionByParam(method, index);
	}

	public DefinitionSite fieldDefinitionSite(IFieldName field) {
		return DefinitionSites.createDefinitionByField(field);
	}

	public DefinitionSite propertyDefinitionSite(IPropertyName property) {
		return DefinitionSites.createDefinitionByProperty(property);
	}

	public DefinitionSite returnDefinitionSite(IMethodName method) {
		return DefinitionSites.createDefinitionByReturn(method);
	}
}