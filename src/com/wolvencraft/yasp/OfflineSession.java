package com.wolvencraft.yasp;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class OfflineSession extends LocalSession {
	
	public OfflineSession(String playerName) {
		super((Player) Bukkit.getServer().getOfflinePlayer(playerName));
	}

}
