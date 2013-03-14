package com.wolvencraft.yasp.db.data.sync;

import java.util.List;

import org.bukkit.entity.Player;

import com.wolvencraft.yasp.StatsPlugin;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.QueryUtils.QueryResult;
import com.wolvencraft.yasp.db.tables.Normal.SettingsTable;

public class Settings {
	
	public enum LocalConfiguration {
		Debug("debug"),
		DBHost("database.host"),
		DBPort("database.port"),
		DBName("database.name"),
		DBUser("database.user"),
		DBPass("database.pass"),
		DBPrefix("database.prefix"),
		DBConnect("", "jdbc:mysql://" + DBHost.asString() + ":" + DBPort.asInteger() + "/" + DBName.asString()),
		LogPrefix("", StatsPlugin.getInstance().getDescription().getPrefix());
		
		LocalConfiguration(String node) {
			this.value = StatsPlugin.getInstance().getConfig().get(node);
		}
		
		LocalConfiguration(String node, Object value) {
			this.value = value;
		}
		
		Object value;
		
		@Override
		public String toString() { return (String) value; }
		public String asString() { return (String) value; }
		public Boolean asBoolean() { return (Boolean) value; }
		public Integer asInteger() { return (Integer) value; }
	}
	
	/**
	 * Default constructor. Takes in the plugin instance as argument.
	 * @param plugin Plugin instance
	 */
	public Settings() {		
		ping = 2400;
		showWelcomeMessages = false;
		welcomeMessage = "Welcome, <PLAYER>!";
		showFirstJoinMessages = false;
		firstJoinMessage = "Welcome, <PLAYER>! Your statistics on this server are now being tracked.";
		
		List<QueryResult> entries = QueryUtils.select(SettingsTable.TableName.toString()).column("key", "value").select();
		for(QueryResult entry : entries) {
			if(entry.getValue("key").equalsIgnoreCase("ping")) ping = entry.getValueAsInteger("value") * 20;
			else if(entry.getValue("key").equalsIgnoreCase("show_welcome_messages")) showWelcomeMessages = entry.getValueAsBoolean("value");
			else if(entry.getValue("key").equalsIgnoreCase("welcome_message")) welcomeMessage = entry.getValue("value");
			else if(entry.getValue("key").equalsIgnoreCase("show_first_join_messages")) showFirstJoinMessages = entry.getValueAsBoolean("value");
			else if(entry.getValue("key").equalsIgnoreCase("first_join_message")) firstJoinMessage = entry.getValue("value");
		}
	}
	
	private static long ping;
	private static boolean showWelcomeMessages;
	private static String welcomeMessage;
	private static boolean showFirstJoinMessages;
	private static String firstJoinMessage;
	
	public static void setDatabaseVersion(String version) {
		QueryUtils.update(SettingsTable.TableName.toString())
			.value("value", version)
			.condition("key", "version")
			.update(true);
	}
	
	public static int getDatabaseVersion() { 
		return QueryUtils.select(SettingsTable.TableName.toString())
			.column("value")
			.condition("key", "version")
			.select()
			.get(0)
			.getValueAsInteger("value");
	}
	
	public static void setUsingVault(boolean usingVault) {
		QueryUtils.update(SettingsTable.TableName.toString())
			.value("value", usingVault)
			.condition("key", "hook_vault")
			.update(true);
	}
	
	public static boolean getUsingVault() {
		return QueryUtils.select(SettingsTable.TableName.toString())
			.column("value")
			.condition("key", "hook_vault")
			.select()
			.get(0)
			.getValueAsBoolean("value");
	}
	
	public static void setUsingWorldGuard(boolean usingWorldGuard) {
		QueryUtils.update(SettingsTable.TableName.toString())
			.value("value", usingWorldGuard)
			.condition("key", "hook_worldguard")
			.update(true);
	}
	
	public static boolean getUsingWorldGuard() {
		return QueryUtils.select(SettingsTable.TableName.toString())
			.column("value")
			.condition("key", "hook_worldguard")
			.select()
			.get(0)
			.getValueAsBoolean("value");
	}
	
	public static long getPing() { return Settings.ping; }
	
	public static String getWelcomeMessage(Player player) {
		if(Settings.showWelcomeMessages) return Settings.welcomeMessage.replace("<PLAYER>", player.getPlayerListName());
		else return null;
	}
	
	public static String getFirstJoinMessage(Player player) {
		if(Settings.showFirstJoinMessages) return Settings.firstJoinMessage.replace("<PLAYER>", player.getPlayerListName());
		else return null;
	}
	
}
