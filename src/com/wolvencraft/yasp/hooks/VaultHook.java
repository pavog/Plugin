package com.wolvencraft.yasp.hooks;

import java.util.logging.Level;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;

import com.wolvencraft.yasp.StatsPlugin;
import com.wolvencraft.yasp.db.data.Settings;
import com.wolvencraft.yasp.util.Message;

public class VaultHook implements PluginHook {
	
	public VaultHook() {
		ServicesManager svm = StatsPlugin.getInstance().getServer().getServicesManager();
		boolean broken = false;
		
		try { economy = ((RegisteredServiceProvider<Economy>)(svm.getRegistration(Economy.class))).getProvider();}
		catch(Exception ex) { Message.log(Level.SEVERE, "An error occurred while initializing economy"); broken = true; }
		
		try { permissions = ((RegisteredServiceProvider<Permission>)(svm.getRegistration(Permission.class))).getProvider(); }
		catch(Exception ex) { Message.log(Level.SEVERE, "An error occurred while initializing permissions"); broken = true; }
		
		if(!broken) Settings.setUsingVault(true);
	}

	private static Economy economy;
	private static Permission permissions;
	
	public class VaultHookEntry implements PluginHookEntry {

		@Override
		public void fetchData(Player player) {
			if(permissions != null) groupName = permissions.getPrimaryGroup(player);
			else groupName = null;
			if(economy != null) balance = economy.getBalance(player.getPlayerListName());
			else balance = -1;
		}
		
		private String groupName;
		private double balance;
		
		@Override
		public boolean pushData(Player player) {
			// TODO Auto-generated method stub
			return false;
		}
		
	}
	
	@Override
	public boolean patch() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void cleanup() {
		economy = null;
		permissions = null;
	}
}
