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
				Message.sendFormattedSuccess(CommandManager.getSender(), "Sending data to the remote database");
				DataCollector.pushAllData();
				Message.sendFormattedSuccess(CommandManager.getSender(), "Synchronization complete");
			}
			
		});
		return true;
	}

	@Override
	public void getHelp() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getHelpLine() {
		// TODO Auto-generated method stub
		
	}

}
