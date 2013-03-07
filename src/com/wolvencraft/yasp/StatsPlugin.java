package com.wolvencraft.yasp;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.wolvencraft.yasp.db.Database;
import com.wolvencraft.yasp.db.data.Settings;
import com.wolvencraft.yasp.exceptions.MetricsConnectionException;
import com.wolvencraft.yasp.hooks.*;
import com.wolvencraft.yasp.listeners.*;
import com.wolvencraft.yasp.metrics.Statistics;
import com.wolvencraft.yasp.util.Message;

public class StatsPlugin extends JavaPlugin {
	private static StatsPlugin instance;
	
	private int databaseTaskId;
	private int signTaskId;
	
	@Override
	public void onEnable() {
		instance = this;
		
		getConfig().options().copyDefaults(true);
		saveConfig();
		new Settings(this);
		
		if(!Database.testConnection()) {
			Message.log(Level.SEVERE, "Could not establish a connection to the database.");
			this.setEnabled(false);
			return;
		}
		
		try { new Database(); }
		catch (Exception e) {
			Message.log(Level.SEVERE, e.getMessage());
			if (Settings.getDebug()) e.printStackTrace();
			this.setEnabled(false);
			return;
		}
		
		Message.log("Database connection established");
		
		if (Settings.getUsingVault() && getServer().getPluginManager().getPlugin("Vault") != null) {
            Message.log("Vault found! Advanced player statistics are available.");
            new VaultHook(this);
        }
		
		Settings.fetchSettings();
		
		new ServerListener(this);
		new PlayerListener(this);
		new BlockListener(this);
		new ItemListener(this);
		new EntityListener(this);
		
		try { new Statistics(this); }
		catch (MetricsConnectionException e) { Message.log(e.getMessage()); }
		
		databaseTaskId = Bukkit.getScheduler().runTaskTimerAsynchronously(this, new DataCollector(), 0L, Settings.getPing()).getTaskId();
		signTaskId = Bukkit.getScheduler().runTaskTimerAsynchronously(this, new DisplaySignFactory(), (Settings.getPing() / 2), Settings.getPing()).getTaskId();
	}

	@Override
	public void onDisable() {
		try {
			DataCollector.global().pluginShutdown();
			Bukkit.getScheduler().cancelTask(databaseTaskId);
			Bukkit.getScheduler().cancelTask(signTaskId);
			DataCollector.clear();
			VaultHook.disable();
			instance = null;
		} catch (Exception ex) { 
			Message.log(Level.SEVERE, ex.getMessage());
			if(Settings.getDebug()) ex.printStackTrace();
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(command.getName().equalsIgnoreCase("yasp"))) return false;
		CommandManager.setSender(sender);
		
		if(args.length == 0) {
			CommandManager.HELP.run();
			CommandManager.resetSender();
			return true;
		}
		
		for(CommandManager cmd : CommandManager.values()) {
			if(cmd.isCommand(args[0])) {
				if(Settings.getDebug()) {
					String argString = "/yasp";
			        for (String arg : args) { argString = argString + " " + arg; }
					Message.log(sender.getName() + ": " + argString);
				}
				
				boolean result = cmd.run(args);
				CommandManager.resetSender();
				return result;
			}
		}
		
		Message.sendFormattedError(sender, "Unknown command");
		CommandManager.resetSender();
		return false;
	}
	
	/**
	 * Returns a static plugin instance
	 * @return <b>YASP</b> plugin instance
	 */
	public static StatsPlugin getInstance() {
		return instance;
	}
}
