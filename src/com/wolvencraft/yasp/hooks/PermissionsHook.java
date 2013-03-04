package com.wolvencraft.yasp.hooks;

import org.bukkit.entity.Player;

import com.wolvencraft.yasp.StatsPlugin;

public class PermissionsHook {
	
	/**
	 * Returns the rank that the specified player has
	 * @param player Player to check
	 * @return <b>String</b> name of the rank
	 */
	public static String getRank(Player player) {
		return StatsPlugin.getPermissions().getPlayerGroups(player.getWorld(), player.getPlayerListName())[0];
	}
}
