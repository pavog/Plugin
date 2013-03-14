package com.wolvencraft.yasp;

import java.io.File;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import com.wolvencraft.yasp.StatsSignFactory.StatsSign;
import com.wolvencraft.yasp.db.Database;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.data.sync.Settings;
import com.wolvencraft.yasp.exceptions.MetricsConnectionException;
import com.wolvencraft.yasp.hooks.*;
import com.wolvencraft.yasp.listeners.*;
import com.wolvencraft.yasp.metrics.Statistics;
import com.wolvencraft.yasp.util.Message;
import com.wolvencraft.yasp.util.TPSTracker;

public class StatsPlugin extends JavaPlugin {
	private static StatsPlugin instance;
	private static boolean paused;
	
	private DataCollector dataCollector;
	private StatsSignFactory displaySignFactory;
	private TPSTracker tpsTracker;
	
	private static VaultHook vaultHook;
	private static WorldGuardHook worldGuardHook;
	
	@Override
	public void onEnable() {
		if(!new File(getDataFolder(), "config.yml").exists()) {
			Message.log("Local configuration not found. Creating a default one for you.");
			getConfig().options().copyDefaults(true);
			saveConfig();
			this.setEnabled(false);
			return;
		}
		
		instance = this;
		paused = true;
		
		new QueryUtils();
		new Settings(this);
		
		try { new Database(); }
		catch (Exception e) {
			Message.log(Level.SEVERE, "Cannot establish a connection with the target database!");
			if (Settings.LocalConfiguration.Debug.asBoolean()) e.printStackTrace();
			this.setEnabled(false);
			return;
		}
		
		Message.log("Database connection established");

		ConfigurationSerialization.registerClass(StatsSign.class, "StatsSign");
		
		dataCollector = new DataCollector();
		displaySignFactory = new StatsSignFactory();
		tpsTracker = new TPSTracker();
		
		if (getServer().getPluginManager().getPlugin("Vault") != null) {
			Message.log("Vault found! Advanced player statistics are available.");
			vaultHook = new VaultHook();
		} else Settings.setUsingVault(false);
		
		if (getServer().getPluginManager().getPlugin("WorldGuard") != null) {
			Message.log("WorldGuard found! Using it to track player's location.");
			worldGuardHook = new WorldGuardHook();
		} else Settings.setUsingWorldGuard(false);
		
		new ServerListener(this);
		new PlayerListener(this);
		new BlockListener(this);
		new ItemListener(this);
		new DeathListener(this);
		new FeedbackListener(this);
		
		Settings.fetchData();
		
		try { new Statistics(this); }
		catch (MetricsConnectionException e) { Message.log(e.getMessage()); }
		
		Bukkit.getScheduler().runTaskTimerAsynchronously(this, dataCollector, Settings.getPing(), Settings.getPing());
		Bukkit.getScheduler().runTaskTimerAsynchronously(this, displaySignFactory, (Settings.getPing() / 2), Settings.getPing());
		Bukkit.getScheduler().runTaskTimer(this, tpsTracker, 0, 1);
	}

	@Override
	public void onDisable() {
		DataCollector.dumpAll();
		Bukkit.getScheduler().cancelTasks(this);
		
		try {
			DataCollector.global().pluginShutdown();
			DataCollector.clear();
			
			if(Settings.getUsingVault()) { vaultHook.cleanup(); }
			if(Settings.getUsingWorldGuard()) { worldGuardHook.cleanup(); }

			Database.cleanup();
			instance = null;
		} catch (Exception e) { 
			Message.log(Level.SEVERE, e.getMessage());
			if(Settings.LocalConfiguration.Debug.asBoolean()) e.printStackTrace();
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(command.getName().equalsIgnoreCase("stats"))) return false;
		CommandManager.setSender(sender);
		
		if(args.length == 0) {
			CommandManager.Help.run();
			CommandManager.resetSender();
			return true;
		}
		
		for(CommandManager cmd : CommandManager.values()) {
			if(cmd.isCommand(args[0])) {
				if(Settings.LocalConfiguration.Debug.asBoolean()) {
					String argString = "/stats";
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
	
	/**
	 * Checks if the database synchronization is paused or not
	 * @return <b>true</b> if the database synchronization is paused, <b>false</b> otherwise
	 */
	public static boolean getPaused() {
		return paused;
	}
	
	/**
	 * Sets the synchronization status
	 * @param paused <b>true</b> to pause the database synchronization, <b>false</b> to unpause it.
	 */
	public static void setPaused(boolean paused) {
		StatsPlugin.paused = paused;
	}
}
