package com.wolvencraft.yasp.hooks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.wolvencraft.yasp.StatsPlugin;
import com.wolvencraft.yasp.db.Database;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.exceptions.DatabaseConnectionException;
import com.wolvencraft.yasp.hooks._HookTables.WorldGuardTable;
import com.wolvencraft.yasp.util.Message;

public class WorldGuardHook implements _PluginHook {
	
	public WorldGuardHook() {
		Plugin plugin = StatsPlugin.getInstance().getServer().getPluginManager().getPlugin("WorldGuard");
		if (plugin == null || !(plugin instanceof WorldGuardPlugin)) { worldGuard = null; }
		else { worldGuard = (WorldGuardPlugin) plugin; }
	}
	
	WorldGuardPlugin worldGuard;
	
	public class WorldGuardHookEntry implements PluginHookEntry {
		
		public WorldGuardHookEntry(Player player) {
			regions = new ArrayList<String>();
			fetchData(player);
		}
		
		List<String> regions;
		
		@Override
		public void fetchData(Player player) {
			regions.clear();
			ApplicableRegionSet set = worldGuard.getRegionManager(player.getWorld()).getApplicableRegions(player.getLocation());
			Iterator<ProtectedRegion> it = set.iterator();
			while(it.hasNext()) {
				ProtectedRegion region = it.next();
				regions.add(region.getId());
				it.remove();
			}
		}

		@Override
		public boolean pushData(int playerId) {
			return QueryUtils.update(
				WorldGuardTable.TableName.toString(),
				getValues(playerId),
				new String[] {WorldGuardTable.PlayerId.toString(), playerId + ""}
			);
		}

		@Override
		public Map<String, Object> getValues(int playerId) {
			Map<String, Object> values = new HashMap<String, Object>();
			values.put(WorldGuardTable.PlayerId.toString(), playerId);
			String regionString = "";
			for(String region : regions) {
				if(!regionString.endsWith("")) regionString += ",";
				regionString += region;
			}
			values.put(WorldGuardTable.RegionName.toString(), regionString);
			return values;
		}
		
	}
	
	@Override
	public boolean patch() {
		try { Database.getInstance().runCustomPatch("worldguard_v1"); }
		catch (DatabaseConnectionException ex) {
			Message.log(Level.SEVERE, ex.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public void cleanup() {
		worldGuard = null;
	}
	
}
