/*
 * Statistics
 * Copyright (C) 2013 bitWolfy <http://www.wolvencraft.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

package com.wolvencraft.yasp.db.data.hooks;

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
import com.wolvencraft.yasp.Settings;
import com.wolvencraft.yasp.StatsPlugin;
import com.wolvencraft.yasp.db.Database;
import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.tables.Hook.WorldGuardTable;
import com.wolvencraft.yasp.exceptions.DatabaseConnectionException;
import com.wolvencraft.yasp.util.Message;

public class WorldGuardHook implements _PluginHook {
	
	public WorldGuardHook() {
		Plugin plugin = StatsPlugin.getInstance().getServer().getPluginManager().getPlugin("WorldGuard");
		if (plugin == null || !(plugin instanceof WorldGuardPlugin)) { worldGuard = null; }
		else {
			Message.log("WorldGuard hook enabled!");
			Settings.ActiveHooks.HookVault.setActive(true);
			worldGuard = (WorldGuardPlugin) plugin;
			instance = this;
		}
	}
	
	private static WorldGuardHook instance;
	private static WorldGuardPlugin worldGuard;
	
	public class WorldGuardHookEntry implements PluginHookEntry {
		
		public WorldGuardHookEntry(Player player, int playerId) {
			this.playerId = playerId;
			regions = new ArrayList<String>();
			fetchData(player);
		}
		
		private int playerId;
		private List<String> regions;
		
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
		public boolean pushData() {
			return Query.table(WorldGuardTable.TableName.toString())
				.value(getValues())
				.condition(WorldGuardTable.PlayerId.toString(), playerId)
				.update(true);
		}

		@Override
		public Map<String, Object> getValues() {
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
	
	/**
	 * Returns the hook instance
	 * @return <b>WorldGuardHook</b> instance
	 */
	public static WorldGuardHook getInstance() {
		return instance;
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
