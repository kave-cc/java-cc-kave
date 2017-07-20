/**
 * Copyright 2016 Technische Universität Darmstadt
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
package cc.kave.commons.model.events.userprofiles;

import cc.kave.commons.model.events.IDEEvent;

public class UserProfileEvent extends IDEEvent {

	public String ProfileId;

	public Educations Education;

	public Positions Position;

	public boolean ProjectsCourses;
	public boolean ProjectsPersonal;
	public boolean ProjectsSharedSmall;
	public boolean ProjectsSharedMedium;
	public boolean ProjectsSharedLarge;

	public boolean TeamsSolo;
	public boolean TeamsSmall;
	public boolean TeamsMedium;
	public boolean TeamsLarge;

	public YesNoUnknown CodeReviews;

	public Likert7Point ProgrammingGeneral;

	public Likert7Point ProgrammingCSharp;
	
	public String Comment;
}