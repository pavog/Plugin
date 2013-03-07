package com.wolvencraft.yasp.hooks;

import java.util.logging.Level;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;

import com.wolvencraft.yasp.StatsPlugin;
import com.wolvencraft.yasp.db.data.Settings;
import com.wolvencraft.yasp.util.Message;

public class VaultEconomyHook implements PluginHook {
	
	public VaultEconomyHook() {
		ServicesManager svm = StatsPlugin.getInstance().getServer().getServicesManager();
		boolean broken = false;
		
		try { economy = ((RegisteredServiceProvider<Economy>)(svm.getRegistration(Economy.class))).getProvider();}
		catch(Exception ex) { Message.log(Level.SEVERE, "An error occurred while initializing economy"); broken = true; }
		
		if(!broken) Settings.setUsingVault(true);
	}

	private static Economy economy;
	
	/**
	 * Returns the balance of the specified player
	 * @param player Player
	 * @return double balance of the player
	 */
	public static double getBalance(Player player) {
		return economy.getBalance(player.getName());
	}
	
	@Override
	public void disable() {
		economy = null;
	}

	@Override
	public Object getPluginInstance() {
		return economy;
	}
}
