package com.wolvencraft.yasp.hooks;

import java.util.Map;
import java.util.logging.Level;

import org.bukkit.entity.Player;

import com.wolvencraft.yasp.db.Database;
import com.wolvencraft.yasp.exceptions.DatabaseConnectionException;
import com.wolvencraft.yasp.util.Message;

public class WorldGuardHook implements _PluginHook {
	
	public WorldGuardHook() {
		
	}
	
	public class WorldGuardHookEntry implements PluginHookEntry {

		@Override
		public void fetchData(Player player) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean pushData(int playerId) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Map<String, Object> getValues(int playerId) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	@Override
	public boolean patch() {
		try { Database.getInstance().patch("worldguard_v1"); }
		catch (DatabaseConnectionException ex) {
			Message.log(Level.SEVERE, ex.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub
		
	}
	
}
