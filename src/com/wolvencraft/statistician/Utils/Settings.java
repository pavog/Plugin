package com.wolvencraft.statistician.Utils;

import com.wolvencraft.statistician.StatisticianPlugin;

public class Settings {
	
	public final int DB_VERSION = 10;
	public final String LOG_PREFIX = StatisticianPlugin.getInstance().getDescription().getName();
	
	public final boolean DEBUG;
	
	public final String DB_HOST;
	public final int DB_PORT;
	public final String DB_NAME;
	
	public final String DB_CONNECT;
	
	public final String DB_USER;
	public final String DB_PASS;
	public final int PING;
	
	public Settings(StatisticianPlugin plugin) {
		DEBUG = plugin.getConfig().getBoolean("debug");
		
		DB_HOST = plugin.getConfig().getString("database.host");
		DB_PORT = plugin.getConfig().getInt("database.port");
		DB_NAME = plugin.getConfig().getString("database.name");
		
		DB_CONNECT = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME;
		
		DB_USER = plugin.getConfig().getString("database.user");
		DB_PASS = plugin.getConfig().getString("database.pass");
		PING = plugin.getConfig().getInt("ping");
	}
}
