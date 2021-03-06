package com.project.terminkalender.calendar;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.project.terminkalender.AppMain;
import com.project.terminkalender.userdata.Task;

public class Tasktable {
	private Array<Slot> slots;
	private Table table;

	public Tasktable() {
		table = new Table();
		Array<Task> tasks = AppMain.user.getGame().getTasks();
		slots = new Array<Slot>(tasks.size);
		
		for (Task task : tasks) {
			slots.add(new Slot(new TaskCalendar(task.getName(), task.getLimit(), task.getWhatArray(), task.getWhereArray())));
		}
	}
	
	public Array<Slot> getSlots() {
		return slots;
	}
	public Table getTable() {
		return table;
	}
}
