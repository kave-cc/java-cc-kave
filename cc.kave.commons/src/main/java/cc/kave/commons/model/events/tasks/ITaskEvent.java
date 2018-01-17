package cc.kave.commons.model.events.tasks;

import cc.kave.commons.model.events.IIDEEvent;
import cc.kave.commons.model.events.userprofiles.Likert5Point;

public interface ITaskEvent extends IIDEEvent {

	String getVersion();

	String getTaskId();

	TaskAction getAction();

	String getNewParentId();

	Likert5Point getAnnoyance();

	Likert5Point getImportance();

	Likert5Point getUrgency();
}