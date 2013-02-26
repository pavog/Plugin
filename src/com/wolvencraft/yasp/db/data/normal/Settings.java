package com.wolvencraft.yasp.db.data.normal;

import java.util.List;
import java.util.Map;

import com.wolvencraft.yasp.StatsPlugin;
import com.wolvencraft.yasp.db.QueryResult;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.normal._Settings;

public class Settings implements _NormalData {
	
	/**
	 * Default constructor. Takes in the plugin instance as argument.
	 * @param plugin Plugin instance
	 */
	public Settings(StatsPlugin plugin) {
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
		ping = 120;
		showWelcomeMessages = false;
		welcomeMessage = "The server owner did not configure plugin's database connection properly.";
		showFirstJoinMessages = false;
		firstJoinMessage = "The server owner did not configure plugin's database connection properly.";
		
		fetchData();
	}
	
	private static boolean debug;
	
	private static String dbhost;
	private static int dbport;
	private static String dbname;
	private static String dbuser;
	private static String dbpass;
	private static String dbconnect;
	private static String tablePrefix;
	
	private static String logPrefix;
	
	private static int databaseVersion;
	
	private static int ping;
	private static boolean showWelcomeMessages;
	private static String welcomeMessage;
	private static boolean showFirstJoinMessages;
	private static String firstJoinMessage;
	
	@Override
	public void fetchData() {
		List<QueryResult> entries = QueryUtils.select(_Settings.TableName.toString(), "*");
		for(QueryResult entry : entries) {
			if(entry.getValue("key").equalsIgnoreCase("version")) databaseVersion = entry.getValueAsInteger("value");
			else if(entry.getValue("key").equalsIgnoreCase("ping")) ping = entry.getValueAsInteger("value");
			else if(entry.getValue("key").equalsIgnoreCase("show_welcome_messages")) showWelcomeMessages = entry.getValueAsBoolean("value");
			else if(entry.getValue("key").equalsIgnoreCase("welcome_message")) welcomeMessage = entry.getValue("value");
			else if(entry.getValue("key").equalsIgnoreCase("show_first_join_messages")) showFirstJoinMessages = entry.getValueAsBoolean("value");
			else if(entry.getValue("key").equalsIgnoreCase("first_join_message")) firstJoinMessage = entry.getValue("value");
		}
	}
	
	@Override
	public boolean pushData() {
		return QueryUtils.update(_Settings.TableName.toString(), "value", databaseVersion + "", "key = version");
	}
	
	public static boolean updateVersion(int version) {
		return QueryUtils.update(_Settings.TableName.toString(), "value", version + "", "key = version");
	}
	
	@Override
	public Map<String, Object> getValues() { return null; }
	
	public static boolean getDebug() { return debug; }
	public static String getConnectionPath() { return dbconnect; }
	public static String getDatabaseName() { return dbname; }
	public static String getDatabaseUsername() { return dbuser; }
	public static String getDatabasePassword() { return dbpass; }
	public static String getTablePrefix() { return tablePrefix; }
	public static String getLogPrefix() { return logPrefix; }
	
	public static int getDatabaseVersion() { return databaseVersion; }
	
	public static int getPing() { return ping; }
	public static String getWelcomeMessage() { if(showWelcomeMessages) return welcomeMessage; else return null; }
	public static String getFirstJoinMessage() { if(showFirstJoinMessages) return firstJoinMessage; else return null; }
	
}
