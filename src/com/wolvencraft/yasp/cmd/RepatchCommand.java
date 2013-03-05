package com.wolvencraft.yasp.cmd;

import org.bukkit.Bukkit;

import com.wolvencraft.yasp.CommandManager;
import com.wolvencraft.yasp.StatsPlugin;
import com.wolvencraft.yasp.db.Database;
import com.wolvencraft.yasp.util.Message;

public class RepatchCommand implements BaseCommand {

	@Override
	public boolean run(String[] args) {
		Message.sendFormattedSuccess(CommandManager.getSender(), "Attempting to patch the database...");
		Bukkit.getScheduler().runTaskAsynchronously(StatsPlugin.getInstance(), new Runnable() {

			@Override
			public void run() {
				try { Database.getInstance().patch(true); }
				catch (Exception ex) { Message.sendFormattedError(CommandManager.getSender(), "Patch failed"); }
			}
			
		});
		Message.sendFormattedSuccess(CommandManager.getSender(), "Patching complete");
		return true;
	}

	@Override
	public void getHelp() { Message.formatHelp("repatch", "", "Attempts to re-patch the remote database"); }

}
