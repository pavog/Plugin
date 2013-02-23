package com.wolvencraft.yasp.db.data.Dynamic;

import java.util.List;
import java.util.Map;

import com.wolvencraft.yasp.StatsPlugin;
import com.wolvencraft.yasp.db.DBEntry;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.Dynamic._Settings;

/**
 * Settings is a unique DataHolder. Unlike other holders, Settins is never synched back to the database.<br />
 * Any changes made to it will remain local. Additionally, the configuration pulled from config.yml is also stored in this DataHolder.<br />
 * If there is no connection to the database, or the plugin is running for the first time, the default settings will be used.
 * @author bitWolfy
 *
 */
public class Settings implements DynamicData {
	
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
		
		logPrefix = plugin.getDescription().getName();
		
		this.remoteVersion = 1;
		this.ping = 120;
		this.showWelcomeMessages = false;
		this.welcomeMessage = "The server owner did not configure plugin's database connection properly.";
		this.showFirstJoinMessages = false;
		this.firstJoinMessage = "The server owner did not configure plugin's database connection properly.";
	}
	
	private boolean debug;
	
	private int latestVersion;
	private String dbhost;
	private int dbport;
	private String dbname;
	private String dbuser;
	private String dbpass;
	
	private String dbconnect;
	private String logPrefix;
	
	private int remoteVersion;
	private int ping;
	private boolean showWelcomeMessages;
	private String welcomeMessage;
	private boolean showFirstJoinMessages;
	private String firstJoinMessage;
	
	@Override
	public void fetchData() {
		List<DBEntry> entries = QueryUtils.select(_Settings.TableName.toString(), "*");
		for(DBEntry entry : entries) {
			if(entry.getValue("key").equalsIgnoreCase("version")) remoteVersion = entry.getValueAsInteger("value");
			else if(entry.getValue("key").equalsIgnoreCase("ping")) ping = entry.getValueAsInteger("value");
			else if(entry.getValue("key").equalsIgnoreCase("show_welcome_messages")) showWelcomeMessages = entry.getValueAsBoolean("value");
			else if(entry.getValue("key").equalsIgnoreCase("welcome_message")) welcomeMessage = entry.getValue("value");
			else if(entry.getValue("key").equalsIgnoreCase("show_first_join_messages")) showFirstJoinMessages = entry.getValueAsBoolean("value");
			else if(entry.getValue("key").equalsIgnoreCase("first_join_message")) firstJoinMessage = entry.getValue("value");
		}
	}
	
	@Override
	public boolean pushData() { return true; }
	
	@Override
	public Map<String, Object> getValues() { return null; }
	
	public boolean getDebug() { return debug; }
	public int getLatestVersion() { return latestVersion; }
	public String getConnectionPath() { return dbconnect; }
	public String getDatabaseName() { return dbname; }
	public String getDatabaseUsername() { return dbuser; }
	public String getDatabasePassword() { return dbpass; }
	public String getLogPrefix() { return logPrefix; }
	
	public int getRemoteVersion() { return remoteVersion; }
	public int getPing() { return ping; }
	public String getWelcomeMessage() { if(showWelcomeMessages) return welcomeMessage; else return null; }
	public String getFirstJoinMessage() { if(showFirstJoinMessages) return firstJoinMessage; else return null; }
	
}
