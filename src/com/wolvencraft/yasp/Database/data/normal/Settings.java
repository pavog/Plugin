package com.wolvencraft.yasp.Database.data.normal;

import java.util.List;

import com.wolvencraft.yasp.StatsPlugin;
import com.wolvencraft.yasp.Database.DBEntry;
import com.wolvencraft.yasp.Database.QueryUtils;
import com.wolvencraft.yasp.Database.tables.normal.SettingsTable;

public class Settings implements NormalData {

	public Settings(StatsPlugin plugin) {
		debug = plugin.getConfig().getBoolean("debug");
		
		dbhost = plugin.getConfig().getString("database.host");
		dbport = plugin.getConfig().getInt("database.port");
		dbname = plugin.getConfig().getString("database.name");
		
		dbconnect = "jdbc:mysql://" + dbhost + ":" + dbport + "/" + dbname;
		
		dbuser = plugin.getConfig().getString("database.user");
		dbpass = plugin.getConfig().getString("database.pass");
		
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
	
	private int remoteVersion;
	private int ping;
	private boolean showWelcomeMessages;
	private String welcomeMessage;
	private boolean showFirstJoinMessages;
	private String firstJoinMessage;
	
	@Override
	public void fetchData() {
		List<DBEntry> entries = QueryUtils.fetchData("SELECT * FROM " + SettingsTable.TableName);
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
	public void pushData() {
		// We do not need to sync settings back to the database
	}
	
	public boolean getDebug() { return debug; }
	public int getLatestVersion() { return latestVersion; }
	public String getConnectionPath() { return dbconnect; }
	public String getDatabaseName() { return dbname; }
	public String getDatabaseUsername() { return dbuser; }
	public String getDatabasePassword() { return dbpass; }
	
	public int getRemoteVersion() { return remoteVersion; }
	public int getPing() { return ping; }
	public String getWelcomeMessage() { if(showWelcomeMessages) return welcomeMessage; else return null; }
	public String getFirstJoinMessage() { if(showFirstJoinMessages) return firstJoinMessage; else return null; }
}
