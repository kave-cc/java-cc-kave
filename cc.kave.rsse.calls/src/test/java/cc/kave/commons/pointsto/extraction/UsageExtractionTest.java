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
package cc.kave.commons.pointsto.extraction;

import java.util.List;
import java.util.Set;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.pointsto.analysis.PointsToAnalysis;
import cc.recommenders.usages.CallSite;
import cc.recommenders.usages.CallSites;
import cc.recommenders.usages.DefinitionSite;
import cc.recommenders.usages.DefinitionSites;
import cc.recommenders.usages.Query;
import cc.recommenders.usages.Usage;

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
		query.setType(CoReNameConverter.convert(type));
		query.setMethodContext(CoReNameConverter.convert(methodContext));
		query.setClassContext(CoReNameConverter.convert(classContext));
		query.setDefinition(definitionSite);
		query.setAllCallsites(callSites);
		return query;
	}

	public CallSite callSite(IMethodName method) {
		return CallSites.createReceiverCallSite(CoReNameConverter.convert(method));
	}

	public CallSite callSite(IMethodName method, int index) {
		return CallSites.createParameterCallSite(CoReNameConverter.convert(method), index);
	}

	public DefinitionSite parameterDefinitionSite(IMethodName method, int index) {
		return DefinitionSites.createDefinitionByParam(CoReNameConverter.convert(method), index);
	}

	public DefinitionSite fieldDefinitionSite(IFieldName field) {
		return DefinitionSites.createDefinitionByField(CoReNameConverter.convert(field));
	}

	public DefinitionSite propertyDefinitionSite(IPropertyName property) {
		return DefinitionSites.createDefinitionByField(CoReNameConverter.convert(property));
	}

	public DefinitionSite returnDefinitionSite(IMethodName method) {
		return DefinitionSites.createDefinitionByReturn(CoReNameConverter.convert(method));
	}
}