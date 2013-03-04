package com.wolvencraft.yasp.db.data.detailed;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.detailed.DetailedPVPKills;
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
			DetailedPVPKills.TableName.toString(),
			getValues(killerId)
		);
	}

	@Override
	public Map<String, Object> getValues(int killerId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(DetailedPVPKills.PlayerID.toString(), killerId);
		map.put(DetailedPVPKills.VictimID.toString(), victimId);
		map.put(DetailedPVPKills.MaterialID.toString(), weapon.getTypeId());
		map.put(DetailedPVPKills.MaterialData.toString(), weapon.getData().getData());
		map.put(DetailedPVPKills.World.toString(), location.getWorld().getName());
		map.put(DetailedPVPKills.XCoord.toString(), location.getBlockX());
		map.put(DetailedPVPKills.YCoord.toString(), location.getBlockY());
		map.put(DetailedPVPKills.ZCoord.toString(), location.getBlockZ());
		map.put(DetailedPVPKills.Timestamp.toString(), timestamp);
		return map;
	}

}
