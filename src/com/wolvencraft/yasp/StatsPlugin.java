package com.wolvencraft.yasp;

import java.util.logging.Level;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.wolvencraft.yasp.db.Database;
import com.wolvencraft.yasp.db.data.normal.Settings;
import com.wolvencraft.yasp.db.exceptions.DatabaseConnectionException;
import com.wolvencraft.yasp.listeners.*;
import com.wolvencraft.yasp.util.Message;

public class StatsPlugin extends JavaPlugin {
	private static StatsPlugin instance;
	private Database database;
	
	private static Economy economy;
	private static Permission permissions;
	
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
		
		try { database = new Database(); }
		catch (DatabaseConnectionException e) {
			Message.log(Level.SEVERE, e.getMessage());
			if (Settings.getDebug()) e.printStackTrace();
			this.setEnabled(false);
			return;
		} catch (Exception e) {
			Message.log(Level.SEVERE, e.getMessage());
			if (Settings.getDebug()) e.printStackTrace();
			this.setEnabled(false);
			return;
		}
		
		if (database == null) {
			instance = null;
			this.setEnabled(false);
			return;
		}
		
		if (getServer().getPluginManager().getPlugin("Vault") != null) {
            Message.log("Vault found! Using it to track advanced player statistics");

    		try {
    	        economy = ((RegisteredServiceProvider<Economy>)(getServer().getServicesManager().getRegistration(Economy.class))).getProvider();
    	        permissions = ((RegisteredServiceProvider<Permission>)(getServer().getServicesManager().getRegistration(Permission.class))).getProvider();
    		} catch (NullPointerException npe) {
    			Message.log(Level.SEVERE, "An error occurred while setting up Vault dependency");
    			Settings.setUsingVault(true);
    		}
        } else Settings.setUsingVault(false);
		
		Settings.fetchSettings();
		
		new PlayerListener(this);
		new BlockListener(this);
		new EntityListener(this);
		
		Bukkit.getScheduler().runTaskTimerAsynchronously(this, new DataCollector(), 0L, Settings.getPing());
	}

	@Override
	public void onDisable() {
		Message.log("Plugin shutting down");
		try {
			DataCollector.pluginShutdown();
			DataCollector.pushAllData();
			instance = null;
			Bukkit.getScheduler().cancelAllTasks();
			DataCollector.clear();
		} catch (Exception e) { }
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandManager.setSender(sender);
		if(!(command.getName().equalsIgnoreCase("yasp"))) return false;
		
		if(args.length == 0) {
			CommandManager.HELP.run("");
			CommandManager.resetSender();
			return true;
		}
		for(CommandManager cmd : CommandManager.values()) {
			if(cmd.isCommand(args[0])) {
				
				String argString = "/yasp";
		        for (String arg : args) { argString = argString + " " + arg; }
				Message.debug(sender.getName() + ": " + argString);
				
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
	 * @return <b>Promote</b> plugin instance
	 */
	public static StatsPlugin getInstance() {
		return instance;
	}
	
	/**
	 * Returns the current economy instance.<br />
	 * The <b>EconomyHook</b> should generally be used instead of this.
	 * @return <b>Economy</b> Economy instance
	 */
	public static Economy getEconomy() {
		return economy;
	}
	
	/**
	 * Returns the current permissions instance.<br />
	 * The <b>PermissionsHook</b> should generally be used instead of this.
	 * @return <b>Permission</b> Economy instance
	 */
	public static Permission getPermissions() {
		return permissions;
	}
}
