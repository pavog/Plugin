package com.wolvencraft.yasp;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.wolvencraft.yasp.db.data.Settings;
import com.wolvencraft.yasp.db.exceptions.VaultDependencyException;

public class VaultHook {
	
	public VaultHook(StatsPlugin plugin) throws VaultDependencyException {
		try {
		economy = ((RegisteredServiceProvider<Economy>)(plugin.getServer().getServicesManager().getRegistration(Economy.class))).getProvider();
        permissions = ((RegisteredServiceProvider<Permission>)(plugin.getServer().getServicesManager().getRegistration(Permission.class))).getProvider();
		} catch (Exception e) { throw new VaultDependencyException(e); }
		Settings.setUsingVault(true);
	}

	private static Economy economy;
	private static Permission permissions;
	
	/**
	 * Returns the balance of the specified player
	 * @param player Player
	 * @return double balance of the player
	 */
	public static double getBalance(Player player) {
		return economy.getBalance(player.getName());
	}
	
	/**
	 * Returns the rank that the specified player has
	 * @param player Player to check
	 * @return <b>String</b> name of the rank
	 */
	public static String getRank(Player player) {
		return permissions.getPlayerGroups(player.getWorld(), player.getPlayerListName())[0];
	}
}
