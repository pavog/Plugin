package com.wolvencraft.yasp.hooks;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;

import com.wolvencraft.yasp.StatsPlugin;
import com.wolvencraft.yasp.db.Database;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.data.sync.Settings;
import com.wolvencraft.yasp.exceptions.DatabaseConnectionException;
import com.wolvencraft.yasp.hooks._HookTables.VaultTable;
import com.wolvencraft.yasp.util.Message;

public class VaultHook implements _PluginHook {
	
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
		
		public VaultHookEntry(Player player) {
			fetchData(player);
		}
		
		private String groupName;
		private double balance;
		
		@Override
		public void fetchData(Player player) { 
			if(permissions != null) groupName = permissions.getPrimaryGroup(player);
			else groupName = null;
			if(economy != null) balance = economy.getBalance(player.getPlayerListName());
			else balance = -1;
		}
		
		@Override
		public boolean pushData(int playerId) {
			return QueryUtils.update(
				VaultTable.TableName.toString(),
				getValues(playerId),
				new String[] { VaultTable.PlayerId.toString(), playerId + ""}
			);
		}

		@Override
		public Map<String, Object> getValues(int playerId) {
			Map<String, Object> values = new HashMap<String, Object>();
			values.put(VaultTable.PlayerId.toString(), playerId);
			values.put(VaultTable.Balance.toString(), balance);
			values.put(VaultTable.GroupName.toString(), groupName);
			return values;
		}
		
	}
	
	@Override
	public boolean patch() {
		try { Database.getInstance().runCustomPatch("vault_v1"); }
		catch (DatabaseConnectionException ex) {
			Message.log(Level.SEVERE, ex.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public void cleanup() {
		economy = null;
		permissions = null;
	}
}
