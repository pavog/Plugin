package com.wolvencraft.yasp.hooks;

import org.bukkit.entity.Player;

public class WorldGuardHook implements PluginHook {
	
	public WorldGuardHook() {
		
	}
	
	public class WorldGuardHookEntry implements PluginHookEntry {

		@Override
		public void fetchData(Player player) {
			// TODO Auto-generated method stub
			
		}

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
		// TODO Auto-generated method stub
		
	}
	
}
