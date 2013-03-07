package com.wolvencraft.yasp.hooks;

import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;

import com.wolvencraft.yasp.StatsPlugin;
import com.wolvencraft.yasp.db.data.Settings;
import com.wolvencraft.yasp.util.Message;

import net.milkbowl.vault.permission.Permission;

public class VaultPermissionsHook implements PluginHook {
	
	public VaultPermissionsHook() {
		ServicesManager svm = StatsPlugin.getInstance().getServer().getServicesManager();
		boolean broken = false;
		
		try { permissions = ((RegisteredServiceProvider<Permission>)(svm.getRegistration(Permission.class))).getProvider(); }
		catch(Exception ex) { Message.log(Level.SEVERE, "An error occurred while initializing permissions"); broken = true; }

		if(!broken) Settings.setUsingVault(true);
	}
	
	private static Permission permissions;
	
	/**
	 * Returns the rank that the specified player has
	 * @param player Player to check
	 * @return <b>String</b> name of the rank
	 */
	public static String getRank(Player player) {
		return permissions.getPlayerGroups(player.getWorld(), player.getPlayerListName())[0];
	}
	
	@Override
	public Object getPluginInstance() {
		return permissions;
	}

	@Override
	public void disable() {
		permissions = null;
	}

}
