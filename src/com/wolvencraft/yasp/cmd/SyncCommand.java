package com.wolvencraft.yasp.cmd;

import org.bukkit.Bukkit;

import com.wolvencraft.yasp.CommandManager;
import com.wolvencraft.yasp.DataCollector;
import com.wolvencraft.yasp.StatsPlugin;
import com.wolvencraft.yasp.util.Message;

public class SyncCommand implements BaseCommand {

	@Override
	public boolean run(String[] args) {
		Bukkit.getScheduler().runTaskAsynchronously(StatsPlugin.getInstance(), new Runnable() {

			@Override
			public void run() {
				DataCollector.pushAllData();
			}
			
		});
		Message.sendFormattedSuccess(CommandManager.getSender(), "Synchronization complete");
		return true;
	}

	@Override
	public void getHelp() { Message.formatHelp("sync", "", "Forces the plugin to push data to the database"); }

}
