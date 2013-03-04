package com.wolvencraft.yasp.hooks;

import org.bukkit.entity.Player;

import com.wolvencraft.yasp.StatsPlugin;

public class EconomyHook {
	
	/**
	 * Returns the balance of the specified player
	 * @param player Player
	 * @return double balance of the player
	 */
	public static double getBalance(Player player) {
		return StatsPlugin.getEconomy().getBalance(player.getName());
	}
}
