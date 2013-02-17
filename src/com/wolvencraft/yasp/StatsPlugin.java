package com.wolvencraft.yasp;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.wolvencraft.yasp.db.Database;
import com.wolvencraft.yasp.db.data.Dynamic.Settings;
import com.wolvencraft.yasp.db.exceptions.DatabaseConnectionException;
import com.wolvencraft.yasp.events.listeners.*;
import com.wolvencraft.yasp.stats.DataCollector;
import com.wolvencraft.yasp.stats.DatabaseSync;
import com.wolvencraft.yasp.util.Message;

public class StatsPlugin extends JavaPlugin {
	private static StatsPlugin plugin;
	private static Settings settings;
	private Database database;
	
	@Override
	public void onEnable() {
		plugin = this;

		getConfig().options().copyDefaults(true);
		saveConfig();
		settings = new Settings(this);
		
		try { database = new Database(); }
		catch (ClassNotFoundException e) {
			Message.log(Level.SEVERE, "MySQL Driver not found");
			if (settings.getDebug()) e.printStackTrace();
			this.setEnabled(false);
			return;
		} catch (DatabaseConnectionException e) {
			Message.log(Level.SEVERE, "Could not connect to the database. Is the plugin configured correctly?");
			if (settings.getDebug()) e.printStackTrace();
			this.setEnabled(false);
			return;
		}
		
		if (database == null) {
			StatsPlugin.plugin = null;
			this.setEnabled(false);
			return;
		}
		
		Message.log("Established database connection");
		
		new DataCollector();
		
		new PlayerListener(this);
		new BlockListener(this);
		new EntityListener(this);
		
		Bukkit.getScheduler().runTaskTimerAsynchronously(this, new DatabaseSync(), 0L, settings.getPing());
	}

	@Override
	public void onDisable() {
		Bukkit.getScheduler().cancelAllTasks();
	}
	
	public static StatsPlugin getInstance() 		{ return plugin; }
	public static Settings getSettings()			{ return settings; }
}
