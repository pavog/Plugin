package com.wolvencraft.yasp.db;

public enum DBProcedure {
	
	PLUGIN_STARTUP("pluginStartup"),
	PLUGIN_SHUTDOWN("pluginShutdown"),
	UPDATE_MOST_EVER_ONLINE("updateMostEverOnline"),
	INCREMENT_BLOCK_PLACED("incrementBlockPlaced"),
	INCREMENT_BLOCK_DESTROY("incrementBlockDestroy"),
	INCREMENT_ITEM_PICKEDUP("incrementPickedup"),
	INCREMENT_ITEM_DROPPED("incrementDropped"),
	PLAYER_JOIN("newPlayer"),
	PLAYER_LOGIN("loginPlayer"),
	PLAYER_LOGOUT("logoutPlayer"),
	KILL("newKill");
	
	DBProcedure(String alias) {
		this.alias = alias;
	}
	
	String alias;
	
	public String getName() { return alias; }
}
