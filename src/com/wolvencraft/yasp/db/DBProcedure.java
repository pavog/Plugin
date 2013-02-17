package com.wolvencraft.yasp.db;

public enum DBProcedure {
	
	PLUGIN_STARTUP("pluginStartup"),
	PLUGIN_SHUTDOWN("pluginShutdown"),
	UPDATE_MOST_EVER_ONLINE("updateMostEverOnline");
	
	DBProcedure(String alias) {
		this.alias = alias;
	}
	
	String alias;
	
	public String getName() { return alias; }
}
