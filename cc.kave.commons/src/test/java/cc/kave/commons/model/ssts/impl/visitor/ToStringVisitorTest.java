/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.kave.commons.model.ssts.impl.visitor;

import org.junit.Test;

public class ToStringVisitorTest {
	@Test
	public void testAsd() {
		/*
		 * EventDeclaration eventDeclaration = new EventDeclaration();
		 * eventDeclaration.setName(CsEventName.newEventName(
		 * "[MyEvent, IO, 1.2.3.4] [DeclaringType, GUI, 5.6.7.8].E"));
		 * FieldDeclaration fieldDeclaration = new FieldDeclaration();
		 * fieldDeclaration.setName(CsFieldName .newFieldName(
		 * "[MyField, mscore, 4.0.0.0] [DeclaringType, mscore, 4.0.0.0]._f"));
		 * 
		 * SST sst = new SST();
		 * sst.setEnclosingType(CsTypeName.newTypeName("MyType, mscore, 4.0.0.0"
		 * )); sst.getEvents().add(eventDeclaration);
		 * sst.getFields().add(fieldDeclaration);
		 * 
		 * StringBuilder context = new StringBuilder();
		 * 
		 * ToStringVisitor sut = new ToStringVisitor(); sst.accept(sut,
		 * context);
		 * 
		 * String actual = context.toString(); String expected =
		 * "class MyType {\n\tevent MyEvent E;\n\tMyField _f;\n}\n";
		 * assertThat(expected, equalTo(actual));
		 */
	}
}
