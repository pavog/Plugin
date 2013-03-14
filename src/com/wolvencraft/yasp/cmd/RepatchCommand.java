package com.wolvencraft.yasp.cmd;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.wolvencraft.yasp.CommandManager;
import com.wolvencraft.yasp.DataCollector;
import com.wolvencraft.yasp.StatsPlugin;
import com.wolvencraft.yasp.db.Database;
import com.wolvencraft.yasp.util.Message;
import com.wolvencraft.yasp.util.Util;

public class RepatchCommand implements BaseCommand {

	@Override
	public boolean run(String[] args) {
		Message.sendFormattedSuccess(CommandManager.getSender(), "Attempting to patch the database...");
		DataCollector.dumpAll();
		StatsPlugin.setPaused(true);
		Bukkit.getScheduler().runTaskAsynchronously(StatsPlugin.getInstance(), new Runnable() {

			@Override
			public void run() {
				try { Database.getInstance().runSyncPatch(true); }
				catch (Exception ex) { Message.sendFormattedError(CommandManager.getSender(), "Patch failed"); }
				finally {
					for(Player player : Bukkit.getServer().getOnlinePlayers()) {
						if(!Util.isExempt(player)) DataCollector.get(player);
					}
				}
			}
			
		});
		return true;
	}

	@Override
	public void getHelp() { Message.formatHelp("repatch", "", "Attempts to re-patch the database"); }

}
