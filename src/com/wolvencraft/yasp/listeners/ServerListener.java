package com.wolvencraft.yasp.listeners;

import org.bukkit.event.Listener;

import com.wolvencraft.yasp.StatsPlugin;

public class ServerListener implements Listener {
	
	public ServerListener(StatsPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
}
