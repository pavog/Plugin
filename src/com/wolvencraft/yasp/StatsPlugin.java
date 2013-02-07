package com.wolvencraft.yasp;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.wolvencraft.yasp.db.Database;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.data.detailed.DetailedDataHolder;
import com.wolvencraft.yasp.db.data.normal.DataHolder;
import com.wolvencraft.yasp.db.data.normal.Settings;
import com.wolvencraft.yasp.db.exceptions.DatabaseConnectionException;
import com.wolvencraft.yasp.events.BlockListener;
import com.wolvencraft.yasp.events.EntityListener;
import com.wolvencraft.yasp.events.PlayerListener;
import com.wolvencraft.yasp.util.Message;

public class StatsPlugin extends JavaPlugin {
	private static StatsPlugin plugin;
	private static Settings settings;
	private Database database;
	
	private List<DataHolder> simpleData;
	private List<DetailedDataHolder> detailedData;
	
	@Override
	public void onEnable() {
		plugin = this;

		getConfig().options().copyDefaults(true);
		saveConfig();
		settings = new Settings(this);
		
		if (database == null) {
			try { database = new Database(); }
			catch (ClassNotFoundException e) {
				Message.log(Level.SEVERE, "MySQL Driver not found");
				if (settings.getDebug()) e.printStackTrace();
			} catch (DatabaseConnectionException e) {
				Message.log(Level.SEVERE, "Could not connect to the database. Is the plugin configured correctly?");
				if (settings.getDebug()) e.printStackTrace();
			}
		}
		
		if (database == null) {
			StatsPlugin.plugin = null;
			this.getPluginLoader().disablePlugin(this);
			return;
		}
		
		Message.log("Database connection established successfully");
		
		simpleData = new ArrayList<DataHolder>();
		detailedData = new ArrayList<DetailedDataHolder>();
		
		// Setup Listeners
		new PlayerListener(this);
		new BlockListener(this);
		new EntityListener(this);
		
		Bukkit.getScheduler().runTaskTimerAsynchronously(this, new Runnable() {

			@Override
			public void run() {
				for(DetailedDataHolder holder : detailedData) {
					if(holder.isOnHold()) { holder.refresh(); continue; }
					QueryUtils.pushData(holder.getQuery());
				}
				
				for(DataHolder holder : simpleData) holder.pushData();
				
			}
				
		}, settings.getPing(), settings.getPing());
	}

	@Override
	public void onDisable() {
		if (StatsPlugin.plugin == null || !StatsPlugin.plugin.equals(this)) return;

		
	}
	
	public static StatsPlugin getInstance() 		{ return plugin; }
	public static Settings getSettings()			{ return settings; }
	public Database getDB() 						{ return this.database; }
	public boolean statExempt(Player player)		{ return !player.hasPermission("statistician.ignore"); }
}
