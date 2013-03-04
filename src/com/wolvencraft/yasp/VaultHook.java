package com.wolvencraft.yasp;

import org.bukkit.entity.Player;

public class VaultHook {
	
	/**
	 * Returns the balance of the specified player
	 * @param player Player
	 * @return double balance of the player
	 */
	public static double getBalance(Player player) {
		return StatsPlugin.getEconomy().getBalance(player.getName());
	}
	
	/**
	 * Returns the rank that the specified player has
	 * @param player Player to check
	 * @return <b>String</b> name of the rank
	 */
	public static String getRank(Player player) {
		return StatsPlugin.getPermissions().getPlayerGroups(player.getWorld(), player.getPlayerListName())[0];
	}
}
