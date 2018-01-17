/**
 * Copyright 2018 University of Zurich
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package cc.kave.commons.generated;

import java.lang.reflect.Type;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.reflect.TypeToken;

import cc.kave.commons.model.events.IDEEvent;
import cc.kave.commons.utils.io.json.JsonUtils;

public class GeneratedEventStreamTest {

	// Please do not manually edit this file! Generate the JSON in C# and just copy
	// it to this class.

	@Test
	public void DeSerializationRountrip() {
		Type collectionType = new TypeToken<Set<IDEEvent>>() {
		}.getType();
		Set<IDEEvent> obj = JsonUtils.fromJson(json, collectionType);
		String actual = JsonUtils.toJsonFormatted(obj);
		Assert.assertEquals(json, actual);
	}

	@Test
	public void RegularAndFormattedAreEqual() {
		Type collectionType = new TypeToken<Set<IDEEvent>>() {
		}.getType();
		Set<IDEEvent> obj = JsonUtils.fromJson(json, collectionType);

		String json1 = JsonUtils.toJson(obj);
		Set<IDEEvent> obj1 = JsonUtils.fromJson(json1, collectionType);

		String json2 = JsonUtils.toJsonFormatted(obj);
		Set<IDEEvent> obj2 = JsonUtils.fromJson(json2, collectionType);

		Assert.assertEquals(obj1, obj2);
	}

	private String json = "[\n" + //
			"    {\n" + //
			"        \"$type\": \"KaVE.Commons.Model.Events.CompletionEvents.CompletionEvent, KaVE.Commons\",\n" + //
			"        \"Context2\": {\n" + //
			"            \"$type\": \"KaVE.Commons.Model.Events.CompletionEvents.Context, KaVE.Commons\",\n" + //
			"            \"TypeShape\": {\n" + //
			"                \"$type\": \"KaVE.Commons.Model.TypeShapes.TypeShape, KaVE.Commons\",\n" + //
			"                \"TypeHierarchy\": {\n" + //
			"                    \"$type\": \"KaVE.Commons.Model.TypeShapes.TypeHierarchy, KaVE.Commons\",\n" + //
			"                    \"Element\": \"0T:?\",\n" + //
			"                    \"Implements\": []\n" + //
			"                },\n" + //
			"                \"NestedTypes\": [],\n" + //
			"                \"Delegates\": [],\n" + //
			"                \"EventHierarchies\": [],\n" + //
			"                \"Fields\": [],\n" + //
			"                \"MethodHierarchies\": [],\n" + //
			"                \"PropertyHierarchies\": []\n" + //
			"            },\n" + //
			"            \"SST\": {\n" + //
			"                \"$type\": \"[SST:SST]\",\n" + //
			"                \"EnclosingType\": \"0T:T,P\",\n" + //
			"                \"Fields\": [],\n" + //
			"                \"Properties\": [],\n" + //
			"                \"Methods\": [],\n" + //
			"                \"Events\": [],\n" + //
			"                \"Delegates\": []\n" + //
			"            }\n" + //
			"        },\n" + //
			"        \"ProposalCollection\": [\n" + //
			"            {\n" + //
			"                \"$type\": \"KaVE.Commons.Model.Events.CompletionEvents.Proposal, KaVE.Commons\",\n" + //
			"                \"Name\": \"0General:y\",\n" + //
			"                \"Relevance\": 2\n" + //
			"            }\n" + //
			"        ],\n" + //
			"        \"Selections\": [\n" + //
			"            {\n" + //
			"                \"$type\": \"KaVE.Commons.Model.Events.CompletionEvents.ProposalSelection, KaVE.Commons\",\n"
			+ //
			"                \"Proposal\": {\n" + //
			"                    \"$type\": \"KaVE.Commons.Model.Events.CompletionEvents.Proposal, KaVE.Commons\",\n" + //
			"                    \"Name\": \"0General:z\",\n" + //
			"                    \"Relevance\": 4\n" + //
			"                },\n" + //
			"                \"SelectedAfter\": \"00:00:01\",\n" + //
			"                \"Index\": -1\n" + //
			"            },\n" + //
			"            {\n" + //
			"                \"$type\": \"KaVE.Commons.Model.Events.CompletionEvents.ProposalSelection, KaVE.Commons\",\n"
			+ //
			"                \"Proposal\": {\n" + //
			"                    \"$type\": \"KaVE.Commons.Model.Events.CompletionEvents.Proposal, KaVE.Commons\"\n" + //
			"                },\n" + //
			"                \"SelectedAfter\": \"427.04:05:06.0070008\",\n" + //
			"                \"Index\": -1\n" + //
			"            }\n" + //
			"        ],\n" + //
			"        \"TerminatedBy\": 2,\n" + //
			"        \"TerminatedState\": 1,\n" + //
			"        \"ProposalCount\": 3,\n" + //
			"        \"IDESessionUUID\": \"sid\",\n" + //
			"        \"KaVEVersion\": \"vX\",\n" + //
			"        \"TriggeredAt\": \"2018-01-14T22:26:45.1238243+01:00\",\n" + //
			"        \"TriggeredBy\": 3,\n" + //
			"        \"ActiveWindow\": \"0Win:w w\",\n" + //
			"        \"ActiveDocument\": \"0Doc:d d\"\n" + //
			"    },\n" + //
			"    {\n" + //
			"        \"$type\": \"KaVE.Commons.Model.Events.TestRunEvents.TestRunEvent, KaVE.Commons\",\n" + //
			"        \"WasAborted\": true,\n" + //
			"        \"Tests\": [\n" + //
			"            {\n" + //
			"                \"$type\": \"KaVE.Commons.Model.Events.TestRunEvents.TestCaseResult, KaVE.Commons\",\n" + //
			"                \"TestMethod\": \"0M:[?] [?].M()\",\n" + //
			"                \"Parameters\": \"without start...\",\n" + //
			"                \"Duration\": \"00:00:01\",\n" + //
			"                \"Result\": 1\n" + //
			"            },\n" + //
			"            {\n" + //
			"                \"$type\": \"KaVE.Commons.Model.Events.TestRunEvents.TestCaseResult, KaVE.Commons\",\n" + //
			"                \"TestMethod\": \"0M:[?] [?].M()\",\n" + //
			"                \"Parameters\": \"with start...\",\n" + //
			"                \"StartTime\": \"2018-01-14T22:26:45.1377018+01:00\",\n" + //
			"                \"Duration\": \"00:00:02\",\n" + //
			"                \"Result\": 3\n" + //
			"            }\n" + //
			"        ],\n" + //
			"        \"IDESessionUUID\": \"sid\",\n" + //
			"        \"KaVEVersion\": \"vX\",\n" + //
			"        \"TriggeredAt\": \"2018-01-14T22:26:45.1377018+01:00\",\n" + //
			"        \"TriggeredBy\": 3,\n" + //
			"        \"ActiveWindow\": \"0Win:w w\",\n" + //
			"        \"ActiveDocument\": \"0Doc:d d\"\n" + //
			"    },\n" + //
			"    {\n" + //
			"        \"$type\": \"KaVE.Commons.Model.Events.UserProfiles.UserProfileEvent, KaVE.Commons\",\n" + //
			"        \"ProfileId\": \"p\",\n" + //
			"        \"Education\": 2,\n" + //
			"        \"Position\": 3,\n" + //
			"        \"ProjectsCourses\": true,\n" + //
			"        \"ProjectsPersonal\": true,\n" + //
			"        \"ProjectsSharedSmall\": true,\n" + //
			"        \"ProjectsSharedMedium\": true,\n" + //
			"        \"ProjectsSharedLarge\": true,\n" + //
			"        \"TeamsSolo\": true,\n" + //
			"        \"TeamsSmall\": true,\n" + //
			"        \"TeamsMedium\": true,\n" + //
			"        \"TeamsLarge\": true,\n" + //
			"        \"CodeReviews\": 1,\n" + //
			"        \"ProgrammingGeneral\": 5,\n" + //
			"        \"ProgrammingCSharp\": 3,\n" + //
			"        \"Comment\": \"c\",\n" + //
			"        \"IDESessionUUID\": \"sid\",\n" + //
			"        \"KaVEVersion\": \"vX\",\n" + //
			"        \"TriggeredAt\": \"2018-01-14T22:26:45.1390315+01:00\",\n" + //
			"        \"TriggeredBy\": 3,\n" + //
			"        \"ActiveWindow\": \"0Win:w w\",\n" + //
			"        \"ActiveDocument\": \"0Doc:d d\"\n" + //
			"    },\n" + //
			"    {\n" + //
			"        \"$type\": \"KaVE.Commons.Model.Events.VersionControlEvents.VersionControlEvent, KaVE.Commons\",\n"
			+ //
			"        \"Actions\": [\n" + //
			"            {\n" + //
			"                \"$type\": \"KaVE.Commons.Model.Events.VersionControlEvents.VersionControlAction, KaVE.Commons\",\n"
			+ //
			"                \"ExecutedAt\": \"2018-01-14T22:26:45.1408241+01:00\",\n" + //
			"                \"ActionType\": 4\n" + //
			"            }\n" + //
			"        ],\n" + //
			"        \"Solution\": \"0Sln:s\",\n" + //
			"        \"IDESessionUUID\": \"sid\",\n" + //
			"        \"KaVEVersion\": \"vX\",\n" + //
			"        \"TriggeredAt\": \"2018-01-14T22:26:45.1417373+01:00\",\n" + //
			"        \"TriggeredBy\": 3,\n" + //
			"        \"ActiveWindow\": \"0Win:w w\",\n" + //
			"        \"ActiveDocument\": \"0Doc:d d\"\n" + //
			"    },\n" + //
			"    {\n" + //
			"        \"$type\": \"KaVE.Commons.Model.Events.Tasks.TaskEvent, KaVE.Commons\",\n" + //
			"        \"Version\": \"1.2.3-Experimental\",\n" + //
			"        \"TaskId\": \"1234-5678-...\",\n" + //
			"        \"Action\": 4,\n" + //
			"        \"NewParentId\": \"2345-6789-...\",\n" + //
			"        \"Annoyance\": 1,\n" + //
			"        \"Importance\": 2,\n" + //
			"        \"Urgency\": 4,\n" + //
			"        \"IDESessionUUID\": \"sid\",\n" + //
			"        \"KaVEVersion\": \"vX\",\n" + //
			"        \"TriggeredAt\": \"2018-01-14T22:26:45.14274+01:00\",\n" + //
			"        \"TriggeredBy\": 3,\n" + //
			"        \"ActiveWindow\": \"0Win:w w\",\n" + //
			"        \"ActiveDocument\": \"0Doc:d d\"\n" + //
			"    },\n" + //
			"    {\n" + //
			"        \"$type\": \"KaVE.Commons.Model.Events.VisualStudio.BuildEvent, KaVE.Commons\",\n" + //
			"        \"Scope\": \"s\",\n" + //
			"        \"Action\": \"a\",\n" + //
			"        \"Targets\": [\n" + //
			"            {\n" + //
			"                \"$type\": \"KaVE.Commons.Model.Events.VisualStudio.BuildTarget, KaVE.Commons\",\n" + //
			"                \"Project\": \"p\",\n" + //
			"                \"ProjectConfiguration\": \"pcfg\",\n" + //
			"                \"Platform\": \"plt\",\n" + //
			"                \"SolutionConfiguration\": \"scfg\",\n" + //
			"                \"StartedAt\": \"2018-01-14T22:26:45.1447743+01:00\",\n" + //
			"                \"Duration\": \"00:00:12\",\n" + //
			"                \"Successful\": true\n" + //
			"            }\n" + //
			"        ],\n" + //
			"        \"IDESessionUUID\": \"sid\",\n" + //
			"        \"KaVEVersion\": \"vX\",\n" + //
			"        \"TriggeredAt\": \"2018-01-14T22:26:45.1447743+01:00\",\n" + //
			"        \"TriggeredBy\": 3,\n" + //
			"        \"ActiveWindow\": \"0Win:w w\",\n" + //
			"        \"ActiveDocument\": \"0Doc:d d\"\n" + //
			"    },\n" + //
			"    {\n" + //
			"        \"$type\": \"KaVE.Commons.Model.Events.VisualStudio.DebuggerEvent, KaVE.Commons\",\n" + //
			"        \"Mode\": 0,\n" + //
			"        \"Reason\": \"r\",\n" + //
			"        \"Action\": \"a\",\n" + //
			"        \"IDESessionUUID\": \"sid\",\n" + //
			"        \"KaVEVersion\": \"vX\",\n" + //
			"        \"TriggeredAt\": \"2018-01-14T22:26:45.1469313+01:00\",\n" + //
			"        \"TriggeredBy\": 3,\n" + //
			"        \"ActiveWindow\": \"0Win:w w\",\n" + //
			"        \"ActiveDocument\": \"0Doc:d d\"\n" + //
			"    },\n" + //
			"    {\n" + //
			"        \"$type\": \"KaVE.Commons.Model.Events.VisualStudio.DocumentEvent, KaVE.Commons\",\n" + //
			"        \"Document\": \"0Doc:type path\",\n" + //
			"        \"Action\": 0,\n" + //
			"        \"IDESessionUUID\": \"sid\",\n" + //
			"        \"KaVEVersion\": \"vX\",\n" + //
			"        \"TriggeredAt\": \"2018-01-14T22:26:45.1469313+01:00\",\n" + //
			"        \"TriggeredBy\": 3,\n" + //
			"        \"ActiveWindow\": \"0Win:w w\",\n" + //
			"        \"ActiveDocument\": \"0Doc:d d\"\n" + //
			"    },\n" + //
			"    {\n" + //
			"        \"$type\": \"KaVE.Commons.Model.Events.VisualStudio.EditEvent, KaVE.Commons\",\n" + //
			"        \"Context2\": {\n" + //
			"            \"$type\": \"KaVE.Commons.Model.Events.CompletionEvents.Context, KaVE.Commons\",\n" + //
			"            \"TypeShape\": {\n" + //
			"                \"$type\": \"KaVE.Commons.Model.TypeShapes.TypeShape, KaVE.Commons\",\n" + //
			"                \"TypeHierarchy\": {\n" + //
			"                    \"$type\": \"KaVE.Commons.Model.TypeShapes.TypeHierarchy, KaVE.Commons\",\n" + //
			"                    \"Element\": \"0T:?\",\n" + //
			"                    \"Implements\": []\n" + //
			"                },\n" + //
			"                \"NestedTypes\": [],\n" + //
			"                \"Delegates\": [],\n" + //
			"                \"EventHierarchies\": [],\n" + //
			"                \"Fields\": [],\n" + //
			"                \"MethodHierarchies\": [],\n" + //
			"                \"PropertyHierarchies\": []\n" + //
			"            },\n" + //
			"            \"SST\": {\n" + //
			"                \"$type\": \"[SST:SST]\",\n" + //
			"                \"EnclosingType\": \"0T:Edit, P\",\n" + //
			"                \"Fields\": [],\n" + //
			"                \"Properties\": [],\n" + //
			"                \"Methods\": [],\n" + //
			"                \"Events\": [],\n" + //
			"                \"Delegates\": []\n" + //
			"            }\n" + //
			"        },\n" + //
			"        \"NumberOfChanges\": 1,\n" + //
			"        \"SizeOfChanges\": 2,\n" + //
			"        \"IDESessionUUID\": \"sid\",\n" + //
			"        \"KaVEVersion\": \"vX\",\n" + //
			"        \"TriggeredAt\": \"2018-01-14T22:26:45.1479439+01:00\",\n" + //
			"        \"TriggeredBy\": 3,\n" + //
			"        \"ActiveWindow\": \"0Win:w w\",\n" + //
			"        \"ActiveDocument\": \"0Doc:d d\"\n" + //
			"    },\n" + //
			"    {\n" + //
			"        \"$type\": \"KaVE.Commons.Model.Events.VisualStudio.FindEvent, KaVE.Commons\",\n" + //
			"        \"Cancelled\": true,\n" + //
			"        \"IDESessionUUID\": \"sid\",\n" + //
			"        \"KaVEVersion\": \"vX\",\n" + //
			"        \"TriggeredAt\": \"2018-01-14T22:26:45.1479439+01:00\",\n" + //
			"        \"TriggeredBy\": 3,\n" + //
			"        \"ActiveWindow\": \"0Win:w w\",\n" + //
			"        \"ActiveDocument\": \"0Doc:d d\"\n" + //
			"    },\n" + //
			"    {\n" + //
			"        \"$type\": \"KaVE.Commons.Model.Events.VisualStudio.IDEStateEvent, KaVE.Commons\",\n" + //
			"        \"IDELifecyclePhase\": 1,\n" + //
			"        \"OpenWindows\": [\n" + //
			"            \"0Win:w w\"\n" + //
			"        ],\n" + //
			"        \"OpenDocuments\": [\n" + //
			"            \"0Doc:d d\"\n" + //
			"        ],\n" + //
			"        \"IDESessionUUID\": \"sid\",\n" + //
			"        \"KaVEVersion\": \"vX\",\n" + //
			"        \"TriggeredAt\": \"2018-01-14T22:26:45.1489489+01:00\",\n" + //
			"        \"TriggeredBy\": 3,\n" + //
			"        \"ActiveWindow\": \"0Win:w w\",\n" + //
			"        \"ActiveDocument\": \"0Doc:d d\"\n" + //
			"    },\n" + //
			"    {\n" + //
			"        \"$type\": \"KaVE.Commons.Model.Events.VisualStudio.InstallEvent, KaVE.Commons\",\n" + //
			"        \"PluginVersion\": \"pv\",\n" + //
			"        \"IDESessionUUID\": \"sid\",\n" + //
			"        \"KaVEVersion\": \"vX\",\n" + //
			"        \"TriggeredAt\": \"2018-01-14T22:26:45.1489489+01:00\",\n" + //
			"        \"TriggeredBy\": 3,\n" + //
			"        \"ActiveWindow\": \"0Win:w w\",\n" + //
			"        \"ActiveDocument\": \"0Doc:d d\"\n" + //
			"    },\n" + //
			"    {\n" + //
			"        \"$type\": \"KaVE.Commons.Model.Events.VisualStudio.SolutionEvent, KaVE.Commons\",\n" + //
			"        \"Action\": 3,\n" + //
			"        \"Target\": \"0Doc:d d\",\n" + //
			"        \"IDESessionUUID\": \"sid\",\n" + //
			"        \"KaVEVersion\": \"vX\",\n" + //
			"        \"TriggeredAt\": \"2018-01-14T22:26:45.1499697+01:00\",\n" + //
			"        \"TriggeredBy\": 3,\n" + //
			"        \"ActiveWindow\": \"0Win:w w\",\n" + //
			"        \"ActiveDocument\": \"0Doc:d d\"\n" + //
			"    },\n" + //
			"    {\n" + //
			"        \"$type\": \"KaVE.Commons.Model.Events.VisualStudio.UpdateEvent, KaVE.Commons\",\n" + //
			"        \"OldPluginVersion\": \"o\",\n" + //
			"        \"NewPluginVersion\": \"n\",\n" + //
			"        \"IDESessionUUID\": \"sid\",\n" + //
			"        \"KaVEVersion\": \"vX\",\n" + //
			"        \"TriggeredAt\": \"2018-01-14T22:26:45.1499697+01:00\",\n" + //
			"        \"TriggeredBy\": 3,\n" + //
			"        \"ActiveWindow\": \"0Win:w w\",\n" + //
			"        \"ActiveDocument\": \"0Doc:d d\"\n" + //
			"    },\n" + //
			"    {\n" + //
			"        \"$type\": \"KaVE.Commons.Model.Events.VisualStudio.WindowEvent, KaVE.Commons\",\n" + //
			"        \"Window\": \"0Win:w w\",\n" + //
			"        \"Action\": 3,\n" + //
			"        \"IDESessionUUID\": \"sid\",\n" + //
			"        \"KaVEVersion\": \"vX\",\n" + //
			"        \"TriggeredAt\": \"2018-01-14T22:26:45.1511287+01:00\",\n" + //
			"        \"TriggeredBy\": 3,\n" + //
			"        \"ActiveWindow\": \"0Win:w w\",\n" + //
			"        \"ActiveDocument\": \"0Doc:d d\"\n" + //
			"    },\n" + //
			"    {\n" + //
			"        \"$type\": \"KaVE.Commons.Model.Events.ActivityEvent, KaVE.Commons\",\n" + //
			"        \"IDESessionUUID\": \"sid\",\n" + //
			"        \"KaVEVersion\": \"vX\",\n" + //
			"        \"TriggeredAt\": \"2018-01-14T22:26:45.1511287+01:00\",\n" + //
			"        \"TriggeredBy\": 3,\n" + //
			"        \"ActiveWindow\": \"0Win:w w\",\n" + //
			"        \"ActiveDocument\": \"0Doc:d d\"\n" + //
			"    },\n" + //
			"    {\n" + //
			"        \"$type\": \"KaVE.Commons.Model.Events.CommandEvent, KaVE.Commons\",\n" + //
			"        \"CommandId\": \"cid\",\n" + //
			"        \"IDESessionUUID\": \"sid\",\n" + //
			"        \"KaVEVersion\": \"vX\",\n" + //
			"        \"TriggeredAt\": \"2018-01-14T22:26:45.1511287+01:00\",\n" + //
			"        \"TriggeredBy\": 3,\n" + //
			"        \"ActiveWindow\": \"0Win:w w\",\n" + //
			"        \"ActiveDocument\": \"0Doc:d d\"\n" + //
			"    },\n" + //
			"    {\n" + //
			"        \"$type\": \"KaVE.Commons.Model.Events.ErrorEvent, KaVE.Commons\",\n" + //
			"        \"Content\": \"c\",\n" + //
			"        \"StackTrace\": [\n" + //
			"            \"s1\",\n" + //
			"            \"s2\"\n" + //
			"        ],\n" + //
			"        \"IDESessionUUID\": \"sid\",\n" + //
			"        \"KaVEVersion\": \"vX\",\n" + //
			"        \"TriggeredAt\": \"2018-01-14T22:26:45.1516956+01:00\",\n" + //
			"        \"TriggeredBy\": 3,\n" + //
			"        \"ActiveWindow\": \"0Win:w w\",\n" + //
			"        \"ActiveDocument\": \"0Doc:d d\"\n" + //
			"    },\n" + //
			"    {\n" + //
			"        \"$type\": \"KaVE.Commons.Model.Events.InfoEvent, KaVE.Commons\",\n" + //
			"        \"Info\": \"info\",\n" + //
			"        \"IDESessionUUID\": \"sid\",\n" + //
			"        \"KaVEVersion\": \"vX\",\n" + //
			"        \"TriggeredAt\": \"2018-01-14T22:26:45.1516956+01:00\",\n" + //
			"        \"TriggeredBy\": 3,\n" + //
			"        \"ActiveWindow\": \"0Win:w w\",\n" + //
			"        \"ActiveDocument\": \"0Doc:d d\"\n" + //
			"    },\n" + //
			"    {\n" + //
			"        \"$type\": \"KaVE.Commons.Model.Events.NavigationEvent, KaVE.Commons\",\n" + //
			"        \"Target\": \"0General:t\",\n" + //
			"        \"Location\": \"0General:l\",\n" + //
			"        \"TypeOfNavigation\": 1,\n" + //
			"        \"IDESessionUUID\": \"sid\",\n" + //
			"        \"KaVEVersion\": \"vX\",\n" + //
			"        \"TriggeredAt\": \"2018-01-14T22:26:45.1527041+01:00\",\n" + //
			"        \"TriggeredBy\": 3,\n" + //
			"        \"ActiveWindow\": \"0Win:w w\",\n" + //
			"        \"ActiveDocument\": \"0Doc:d d\"\n" + //
			"    },\n" + //
			"    {\n" + //
			"        \"$type\": \"KaVE.Commons.Model.Events.SystemEvent, KaVE.Commons\",\n" + //
			"        \"Type\": 6,\n" + //
			"        \"IDESessionUUID\": \"sid\",\n" + //
			"        \"KaVEVersion\": \"vX\",\n" + //
			"        \"TriggeredAt\": \"2018-01-14T22:26:45.1537138+01:00\",\n" + //
			"        \"TriggeredBy\": 3,\n" + //
			"        \"ActiveWindow\": \"0Win:w w\",\n" + //
			"        \"ActiveDocument\": \"0Doc:d d\"\n" + //
			"    }\n" + //
			"]";
}