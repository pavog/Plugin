package com.wolvencraft.yasp;

import java.util.logging.Level;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;

import com.wolvencraft.yasp.db.data.Settings;
import com.wolvencraft.yasp.util.Message;

public class VaultHook {
	
	public VaultHook(StatsPlugin plugin) {
		ServicesManager svm = plugin.getServer().getServicesManager();
		boolean broken = false;
		
		try { economy = ((RegisteredServiceProvider<Economy>)(svm.getRegistration(Economy.class))).getProvider();}
		catch(NullPointerException ex) { Message.log(Level.SEVERE, "An error occurred while initializing economy"); broken = true; }
		
		try { permissions = ((RegisteredServiceProvider<Permission>)(svm.getRegistration(Permission.class))).getProvider(); }
		catch(NullPointerException ex) { Message.log(Level.SEVERE, "An error occurred while initializing permissions"); broken = true; }
		
		if(!broken) Settings.setUsingVault(true);
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
	
	/**
	 * Manually disables the Vault economy and permissions hooks
	 */
	public static void disable() {
		economy = null;
		permissions = null;
	}
}
