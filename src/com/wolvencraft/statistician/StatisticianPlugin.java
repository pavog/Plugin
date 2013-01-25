package com.wolvencraft.statistician;

import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.wolvencraft.statistician.Database.DBConnectFail;
import com.wolvencraft.statistician.Database.Database;
import com.wolvencraft.statistician.Database.DataValues.DataValues_Config;
import com.wolvencraft.statistician.EventDataHandlers.EDHPlayer;
import com.wolvencraft.statistician.Listeners.BlockListener;
import com.wolvencraft.statistician.Listeners.EntityListener;
import com.wolvencraft.statistician.Listeners.PlayerListener;
import com.wolvencraft.statistician.Stats.PlayerData;
import com.wolvencraft.statistician.Utils.Message;
import com.wolvencraft.statistician.Utils.Settings;

public class StatisticianPlugin extends JavaPlugin {
	private static StatisticianPlugin plugin;
	private static Settings settings;
	
	private Database database;
	private ExecutorService executorService;
	private DataProcessor dataProcessor;
	private Timer dataProcessorTimer;
	private PlayerData playerData;
	private EDHPlayer edhPlayer;

	@Override
	public void onEnable() {
		plugin = this;
		
		this.setNaggable(false);

		getConfig().options().copyDefaults(true);
		saveConfig();
		settings = new Settings(this);
		
		if (database == null) {
			try { database = new Database(); }
			catch (ClassNotFoundException e) {
				Message.log(Level.SEVERE, "MySQL Driver not found");
				if (settings.DEBUG) e.printStackTrace();
			} catch (DBConnectFail e) {
				Message.log(Level.SEVERE, "Critical Error, could not connect to mySQL. Is the database Available? Check config file and try again. (" + e.getMessage() + ")");
				if (settings.DEBUG) e.printStackTrace();
			}
		}
		
		if (database == null) {
			StatisticianPlugin.plugin = null;
			this.getPluginLoader().disablePlugin(this);
			return;
		}

		DataValues_Config.refresh();

		this.database.callStoredProcedure("pluginStartup", null);
		this.executorService = Executors.newCachedThreadPool();
		this.edhPlayer = new EDHPlayer();
		this.playerData = new PlayerData();
		this.dataProcessor = new DataProcessor();
		this.dataProcessor.addProcessable(this.playerData);

		this.dataProcessorTimer = new Timer(true);
		this.dataProcessorTimer.scheduleAtFixedRate(this.dataProcessor, 0, settings.PING);

		// Setup Listeners
		new PlayerListener(this, this.edhPlayer);
		new BlockListener(this, this.edhPlayer);
		new EntityListener(this, this.edhPlayer);

		// This could be a reload so see if people are logged in
		for (Player player : this.getServer().getOnlinePlayers()) {
			this.edhPlayer.PlayerJoin(player);
		}
	}

	@Override
	public void onDisable() {
		if (StatisticianPlugin.plugin == null || !StatisticianPlugin.plugin.equals(this)) return;

		this.dataProcessorTimer.cancel();

		if (this.edhPlayer != null) {
			for (Player player : this.getServer().getOnlinePlayers()) {
				this.edhPlayer.PlayerQuit(player);
			}
		}

		if (this.playerData != null) {
			this.playerData._processData();
		}

		if (this.database != null) {
			this.database.callStoredProcedure("pluginShutdown", null);
			this.database = null;
		}

		plugin = null;

		if (this.executorService != null) {
			this.executorService.shutdown();
		}
	}
	
	public static StatisticianPlugin getInstance() 	{ return plugin; }
	public static Settings getSettings()			{ return settings; }
	public static void reloadSettings() {
		plugin.getConfig().options().copyDefaults(true);
		plugin.saveConfig();
		settings = null;
		settings = new Settings(plugin);
		
	}
	public Database getDB() 						{ return this.database; }
	public ExecutorService getExecutor()			{ return this.executorService; }
	public PlayerData getPlayerData()				{ return this.playerData; }
	public boolean statExempt(Player player)		{ return !player.hasPermission("statistician.ignore"); }
}
