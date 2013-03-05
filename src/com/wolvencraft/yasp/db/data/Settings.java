package com.wolvencraft.yasp.db.data;

import java.util.List;

import org.bukkit.entity.Player;

import com.wolvencraft.yasp.StatsPlugin;
import com.wolvencraft.yasp.db.QueryResult;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.normal._Settings;

public class Settings {
	
	/**
	 * Default constructor. Takes in the plugin instance as argument.
	 * @param plugin Plugin instance
	 */
	public Settings(StatsPlugin plugin) {
		instance = this;
		
		debug = plugin.getConfig().getBoolean("debug");
		
		dbhost = plugin.getConfig().getString("database.host");
		dbport = plugin.getConfig().getInt("database.port");
		dbname = plugin.getConfig().getString("database.name");
		dbconnect = "jdbc:mysql://" + dbhost + ":" + dbport + "/" + dbname;
		dbuser = plugin.getConfig().getString("database.user");
		dbpass = plugin.getConfig().getString("database.pass");
		tablePrefix = plugin.getConfig().getString("database.prefix");
		
		logPrefix = plugin.getDescription().getName();
		
		databaseVersion = 0;
		usingVault = false;
		
		ping = 2400;
		showWelcomeMessages = false;
		welcomeMessage = "Welcome, <PLAYER>!";
		showFirstJoinMessages = false;
		firstJoinMessage = "Welcome, <PLAYER>! Your statistics on this server are now being tracked.";
	}
	
	private static Settings instance;
	
	private boolean debug;
	
	private String dbhost;
	private int dbport;
	private String dbname;
	private String dbuser;
	private String dbpass;
	private String dbconnect;
	private String tablePrefix;
	
	private String logPrefix;
	
	private int databaseVersion;
	private boolean usingVault;
	
	private long ping;
	private boolean showWelcomeMessages;
	private String welcomeMessage;
	private boolean showFirstJoinMessages;
	private String firstJoinMessage;
	
	public void fetchData() {
		List<QueryResult> entries = QueryUtils.select(_Settings.TableName.toString(), new String[] {"key", "value"});
		for(QueryResult entry : entries) {
			if(entry.getValue("key").equalsIgnoreCase("version")) databaseVersion = entry.getValueAsInteger("value");
			else if(entry.getValue("key").equalsIgnoreCase("ping")) ping = entry.getValueAsInteger("value") * 20;
			else if(entry.getValue("key").equalsIgnoreCase("show_welcome_messages")) showWelcomeMessages = entry.getValueAsBoolean("value");
			else if(entry.getValue("key").equalsIgnoreCase("welcome_message")) welcomeMessage = entry.getValue("value");
			else if(entry.getValue("key").equalsIgnoreCase("show_first_join_messages")) showFirstJoinMessages = entry.getValueAsBoolean("value");
			else if(entry.getValue("key").equalsIgnoreCase("first_join_message")) firstJoinMessage = entry.getValue("value");
		}
	}
	
	/**
	 * Performs a database operation to fetch the data from the remote database.<br />
	 * If no data is found in the database, the default values are inserted.
	 */
	public static void fetchSettings() {
		instance.fetchData();
	}
	
	public boolean pushData() {
		QueryUtils.update(_Settings.TableName.toString(), "value", databaseVersion + "", new String[] {"key", "version"});
		if(usingVault) QueryUtils.update(_Settings.TableName.toString(), "value", 1 + "", new String[] {"key", "vault"});
		else QueryUtils.update(_Settings.TableName.toString(), "value", 0 + "", new String[] {"key", "vault"});
		return true;
	}

	/**
	 * Performs a database operation to push the local data to the remote database.<br />
	 * If no data is found in the database, the default values are inserted instead.
	 * @return <b>true</b> if the insertion was successful, <b>false</b> otherwise
	 */
	public static boolean pushSettings() {
		return instance.pushData();
	}
	
	public static void setDatabaseVersion(int version) { 
		instance.databaseVersion = version;
		QueryUtils.update(_Settings.TableName.toString(), "value", version + "", new String[] {"key", "version"});
	}
	
	public static int getDatabaseVersion() { 
		List<QueryResult> entries = QueryUtils.select(_Settings.TableName.toString(), new String[] {"key", "value"});
		for(QueryResult entry : entries) {
			if(entry.getValue("key").equalsIgnoreCase("version")) instance.databaseVersion = entry.getValueAsInteger("value");
		}
		return instance.databaseVersion;
	}
	
	public static void setUsingVault(boolean usingVault) {
		instance.usingVault = usingVault;
		if(usingVault) QueryUtils.update(_Settings.TableName.toString(), "value", 1 + "", new String[] {"key", "vault"});
		else QueryUtils.update(_Settings.TableName.toString(), "value", 0 + "", new String[] {"key", "vault"});
	}
	
	public static boolean getUsingVault() {
		List<QueryResult> entries = QueryUtils.select(_Settings.TableName.toString(), new String[] {"key", "value"});
		for(QueryResult entry : entries) {
			if(entry.getValue("key").equalsIgnoreCase("vault")) instance.usingVault = entry.getValueAsBoolean("value");
		}
		return instance.usingVault;
	}
	
	public static boolean getDebug() { return instance.debug; }
	public static String getConnectionPath() { return instance.dbconnect; }
	public static String getDatabaseName() { return instance.dbname; }
	public static String getDatabaseUsername() { return instance.dbuser; }
	public static String getDatabasePassword() { return instance.dbpass; }
	public static String getTablePrefix() { return instance.tablePrefix; }
	public static String getLogPrefix() { return instance.logPrefix; }
	
	public static long getPing() { return instance.ping; }
	
	public static String getWelcomeMessage(Player player) {
		if(instance.showWelcomeMessages) return instance.welcomeMessage.replace("<PLAYER>", player.getPlayerListName());
		else return null;
	}
	
	public static String getFirstJoinMessage(Player player) {
		if(instance.showFirstJoinMessages) return instance.firstJoinMessage.replace("<PLAYER>", player.getPlayerListName());
		else return null;
	}
	
}
