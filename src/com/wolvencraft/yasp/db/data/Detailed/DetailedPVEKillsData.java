package com.wolvencraft.yasp.db.data.detailed;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.Detailed;
import com.wolvencraft.yasp.util.Util;

public class DetailedPVEKillsData implements _DetailedData {
	
	public DetailedPVEKillsData(Location location, EntityType creatureType, ItemStack weapon, boolean playerKilled) {
		this.creatureType = creatureType;
		this.weapon = weapon;
		this.location = location;
		this.playerKilled = playerKilled;
		this.timestamp = Util.getTimestamp();
	}
	
	private EntityType creatureType;
	private ItemStack weapon;
	private Location location;
	private boolean playerKilled;
	private long timestamp;
	
	@Override
	public boolean pushData(int playerId) {
		return QueryUtils.insert(
			Detailed.PVEKills.TableName.toString(),
			getValues(playerId)
		);
	}

	@Override
	public Map<String, Object> getValues(int playerId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Detailed.PVEKills.PlayerID.toString(), playerId);
		map.put(Detailed.PVEKills.CreatureId.toString(), creatureType.getTypeId());
		if(playerKilled) {
			map.put(Detailed.PVEKills.PlayerKilled.toString(), 1);
			map.put(Detailed.PVEKills.MaterialId.toString(), -1);
			map.put(Detailed.PVEKills.MaterialData.toString(), 0);
		} else {
			map.put(Detailed.PVEKills.PlayerKilled.toString(), 0);
			map.put(Detailed.PVEKills.MaterialId.toString(), weapon.getTypeId());
			map.put(Detailed.PVEKills.MaterialData.toString(), weapon.getData().getData());
		}
		map.put(Detailed.PVEKills.World.toString(), location.getWorld().getName());
		map.put(Detailed.PVEKills.XCoord.toString(), location.getBlockX());
		map.put(Detailed.PVEKills.YCoord.toString(), location.getBlockY());
		map.put(Detailed.PVEKills.ZCoord.toString(), location.getBlockZ());
		map.put(Detailed.PVEKills.Timestamp.toString(), timestamp);
		return map;
	}

}
