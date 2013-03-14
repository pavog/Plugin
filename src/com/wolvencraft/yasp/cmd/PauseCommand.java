package com.wolvencraft.yasp.cmd;

import com.wolvencraft.yasp.CommandManager;
import com.wolvencraft.yasp.StatsPlugin;
import com.wolvencraft.yasp.util.Message;

public class PauseCommand implements BaseCommand {

	@Override
	public boolean run(String[] args) {
		if(StatsPlugin.getPaused()) {
			StatsPlugin.setPaused(false);
			Message.send(CommandManager.getSender(), "Data collection is unpaused");
		} else {
			StatsPlugin.setPaused(true);
			Message.send(CommandManager.getSender(), "Data collection is paused");
		}
		return true;
	}

	@Override
	public void getHelp() {
		Message.formatHelp("pause", "", "Toggles the data collection on and off");
	}

}
