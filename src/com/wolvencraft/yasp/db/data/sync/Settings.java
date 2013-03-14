package com.wolvencraft.yasp.db.data.sync;

import java.util.List;

import org.bukkit.entity.Player;

import com.wolvencraft.yasp.StatsPlugin;
import com.wolvencraft.yasp.db.QueryResult;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.Normal.SettingsTable;
import com.wolvencraft.yasp.util.Message;

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
		
		public String asString() { return (String) value; }
		public Boolean asBoolean() { return (Boolean) value; }
		public Integer asInteger() { return (Integer) value; }
	}
	
	/**
	 * Default constructor. Takes in the plugin instance as argument.
	 * @param plugin Plugin instance
	 */
	public Settings(StatsPlugin plugin) {		
		ping = 2400;
		showWelcomeMessages = false;
		welcomeMessage = "Welcome, <PLAYER>!";
		showFirstJoinMessages = false;
		firstJoinMessage = "Welcome, <PLAYER>! Your statistics on this server are now being tracked.";
	}
	
	private static long ping;
	private static boolean showWelcomeMessages;
	private static String welcomeMessage;
	private static boolean showFirstJoinMessages;
	private static String firstJoinMessage;
	
	public static void fetchData() {
		List<QueryResult> entries = QueryUtils.select(SettingsTable.TableName.toString(), new String[] {"key", "value"});
		for(QueryResult entry : entries) {
			if(entry.getValue("key").equalsIgnoreCase("ping")) ping = entry.getValueAsInteger("value") * 20;
			else if(entry.getValue("key").equalsIgnoreCase("show_welcome_messages")) showWelcomeMessages = entry.getValueAsBoolean("value");
			else if(entry.getValue("key").equalsIgnoreCase("welcome_message")) welcomeMessage = entry.getValue("value");
			else if(entry.getValue("key").equalsIgnoreCase("show_first_join_messages")) showFirstJoinMessages = entry.getValueAsBoolean("value");
			else if(entry.getValue("key").equalsIgnoreCase("first_join_message")) firstJoinMessage = entry.getValue("value");
		}
	}
	
	public static void setDatabaseVersion(int version) { 
		if(!QueryUtils.update(SettingsTable.TableName.toString(), "value", version + "", new String[] {"key", "version"}))
		Message.log(java.util.logging.Level.SEVERE, "Failed to update the database version!");
	}
	
	public static int getDatabaseVersion() { 
		List<QueryResult> entries = QueryUtils.select(SettingsTable.TableName.toString(), new String[] {"key", "value"}, new String[] {"key", "version"});
		for(QueryResult entry : entries) {
			if(entry.getValue("key").equalsIgnoreCase("version")) return entry.getValueAsInteger("value");
		}
		return 0;
	}
	
	public static void setUsingVault(boolean usingVault) {
		if(usingVault) QueryUtils.update(SettingsTable.TableName.toString(), "value", 1 + "", new String[] {"key", "hook_vault"});
		else QueryUtils.update(SettingsTable.TableName.toString(), "value", 0 + "", new String[] {"key", "hook_vault"});
	}
	
	public static boolean getUsingVault() {
		List<QueryResult> entries = QueryUtils.select(SettingsTable.TableName.toString(), new String[] {"key", "value"}, new String[] {"key", "hook_vault"});
		for(QueryResult entry : entries) {
			if(entry.getValue("key").equalsIgnoreCase("hook_vault")) return entry.getValueAsBoolean("value");
		}
		return false;
	}
	
	public static void setUsingWorldGuard(boolean usingWorldGuard) {
		if(usingWorldGuard) QueryUtils.update(SettingsTable.TableName.toString(), "value", 1 + "", new String[] {"key", "hook_worldguard"});
		else QueryUtils.update(SettingsTable.TableName.toString(), "value", 0 + "", new String[] {"key", "hook_worldguard"});
	}
	
	public static boolean getUsingWorldGuard() {
		List<QueryResult> entries = QueryUtils.select(SettingsTable.TableName.toString(), new String[] {"key", "value"}, new String[] {"key", "hook_worldguard"});
		for(QueryResult entry : entries) {
			if(entry.getValue("key").equalsIgnoreCase("hook_worldguard")) return entry.getValueAsBoolean("value");
		}
		return false;
	}
	
	public static void setUsingDynmap(boolean usingDynmap) {
		if(usingDynmap) QueryUtils.update(SettingsTable.TableName.toString(), "value", 1 + "", new String[] {"key", "hook_dynmap"});
		else QueryUtils.update(SettingsTable.TableName.toString(), "value", 0 + "", new String[] {"key", "hook_dynmap"});
	}
	
	public static boolean getUsingDynmap() {
		List<QueryResult> entries = QueryUtils.select(SettingsTable.TableName.toString(), new String[] {"key", "value"});
		for(QueryResult entry : entries) {
			if(entry.getValue("key").equalsIgnoreCase("hook_dynmap")) return entry.getValueAsBoolean("value");
		}
		return false;
	}
	
	public static void setUsingMcMMO(boolean usingMcMMO) {
		if(usingMcMMO) QueryUtils.update(SettingsTable.TableName.toString(), "value", 1 + "", new String[] {"key", "hook_mcmmo"});
		else QueryUtils.update(SettingsTable.TableName.toString(), "value", 0 + "", new String[] {"key", "hook_mcmmo"});
	}
	
	public static boolean getUsingMcMMO() {
		List<QueryResult> entries = QueryUtils.select(SettingsTable.TableName.toString(), new String[] {"key", "value"});
		for(QueryResult entry : entries) {
			if(entry.getValue("key").equalsIgnoreCase("hook_mcmmo")) return entry.getValueAsBoolean("value");
		}
		return false;
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
