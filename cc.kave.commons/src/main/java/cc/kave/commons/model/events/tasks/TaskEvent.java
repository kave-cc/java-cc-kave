package cc.kave.commons.model.events.tasks;

import cc.kave.commons.model.events.IDEEvent;
import cc.kave.commons.model.events.userprofiles.Likert5Point;

public class TaskEvent extends IDEEvent implements ITaskEvent {

	private String Version;

	private String TaskId;
	private TaskAction Action;
	private String NewParentId;

	private Likert5Point Annoyance;
	private Likert5Point Importance;
	private Likert5Point Urgency;

	@Override
	public String getVersion() {
		return Version;
	}

	@Override
	public String getTaskId() {
		return TaskId;
	}

	@Override
	public TaskAction getAction() {
		return Action;
	}

	@Override
	public String getNewParentId() {
		return NewParentId;
	}

	@Override
	public Likert5Point getAnnoyance() {
		return Annoyance;
	}

	@Override
	public Likert5Point getImportance() {
		return Importance;
	}

	@Override
	public Likert5Point getUrgency() {
		return Urgency;
	}
}