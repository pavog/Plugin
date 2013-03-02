package com.wolvencraft.yasp.cmd;

import org.bukkit.Bukkit;

import com.wolvencraft.yasp.CommandManager;
import com.wolvencraft.yasp.StatsPlugin;
import com.wolvencraft.yasp.db.Database;
import com.wolvencraft.yasp.db.exceptions.DatabaseConnectionException;
import com.wolvencraft.yasp.util.Message;

public class RepatchCommand implements BaseCommand {

	@Override
	public boolean run(String[] args) {
		Bukkit.getScheduler().runTaskAsynchronously(StatsPlugin.getInstance(), new Runnable() {

			@Override
			public void run() {
				Message.sendFormattedSuccess(CommandManager.getSender(), "Attempting to patch the database...");
				try {
					Database.getInstance().patch(true);
				} catch (DatabaseConnectionException e) {
					Message.sendFormattedError(CommandManager.getSender(), "Patch failed");
				}
				Message.sendFormattedSuccess(CommandManager.getSender(), "Patching complete");
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
