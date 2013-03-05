package com.wolvencraft.yasp.db.data.detailed;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.Detailed;
import com.wolvencraft.yasp.util.Util;

public class DetailedPVPKillsData implements _DetailedData {
	
	public DetailedPVPKillsData(Location location, int victimId, ItemStack weapon) {
		this.victimId = victimId;
		this.weapon = weapon;
		this.location = location;
		this.timestamp = Util.getTimestamp();
	}
	
	private int victimId;
	private ItemStack weapon;
	private Location location;
	private long timestamp;
	
	@Override
	public boolean pushData(int killerId) {
		return QueryUtils.insert(
			Detailed.PVPKills.TableName.toString(),
			getValues(killerId)
		);
	}

	@Override
	public Map<String, Object> getValues(int killerId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Detailed.PVPKills.KillerID.toString(), killerId);
		map.put(Detailed.PVPKills.VictimID.toString(), victimId);
		map.put(Detailed.PVPKills.MaterialID.toString(), weapon.getTypeId());
		map.put(Detailed.PVPKills.MaterialData.toString(), weapon.getData().getData());
		map.put(Detailed.PVPKills.World.toString(), location.getWorld().getName());
		map.put(Detailed.PVPKills.XCoord.toString(), location.getBlockX());
		map.put(Detailed.PVPKills.YCoord.toString(), location.getBlockY());
		map.put(Detailed.PVPKills.ZCoord.toString(), location.getBlockZ());
		map.put(Detailed.PVPKills.Timestamp.toString(), timestamp);
		return map;
	}

}
