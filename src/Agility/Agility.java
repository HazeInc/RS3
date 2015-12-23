package Agility;

import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.ClientContext;

import Agility.Tasks.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Script.Manifest(name = "Agility AIO", description = "Runs Burthrope & Gnome Basic Agility course")
public class Agility extends PollingScript<ClientContext> {
	private List<Task> taskList = new ArrayList<Task>();
	
	@Override
	public void start() {
		taskList.addAll(Arrays.asList(
				new BurthorpeCourse(ctx),
				new GnomeBasic(ctx)
				));
	}
	
	@Override
	public void poll() {
		for (Task task : taskList) {
			if (task.activate()) {
				task.execute();
			}
		}
	}
}