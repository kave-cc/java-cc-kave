/*
 * Copyright 2017 Nico Strebel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.kave.commons.model.events.tasks;

import cc.kave.commons.model.enums.Likert5Point;
import cc.kave.commons.model.events.IDEEvent;

public class TaskEvent extends IDEEvent
{
    public String Version;
    public String TaskId;
    public TaskAction Action;
    public String NewParentId;
    public Likert5Point Annoyance;
    public Likert5Point Importance;
    public Likert5Point Urgency;
}