package com.wolvencraft.yasp.db.data.Detailed;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.DataCollector;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.Detailed.DetailedPVEKills;
import com.wolvencraft.yasp.util.Util;

public class DetailedPVEKillsData implements _DetailedData {
	
	private boolean onHold = false;
	
	public DetailedPVEKillsData(Player player, EntityType creatureType, ItemStack weapon, boolean playerKilled) {
		this.playerId = DataCollector.getCachedPlayerId(player.getPlayerListName());
		this.creatureType = creatureType;
		this.weapon = weapon;
		this.location = player.getLocation();
		this.playerKilled = playerKilled;
		this.timestamp = Util.getCurrentTime().getTime();
	}
	
	private int playerId;
	private EntityType creatureType;
	private ItemStack weapon;
	private Location location;
	private boolean playerKilled;
	private long timestamp;
	
	@Override
	public boolean pushData() {
		return QueryUtils.insert(DetailedPVEKills.TableName.toString(), getValues());
	}

	@Override
	public Map<String, Object> getValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(DetailedPVEKills.PlayerID.toString(), playerId);
		map.put(DetailedPVEKills.CreatureId.toString(), creatureType);
		map.put(DetailedPVEKills.MaterialId.toString(), weapon.getTypeId());
		if(playerKilled) map.put(DetailedPVEKills.PlayerKilled.toString(), "Y");
		else map.put(DetailedPVEKills.PlayerKilled.toString(), "N");
		map.put(DetailedPVEKills.World.toString(), location.getWorld().getName());
		map.put(DetailedPVEKills.XCoord.toString(), location.getBlockX());
		map.put(DetailedPVEKills.YCoord.toString(), location.getBlockY());
		map.put(DetailedPVEKills.ZCoord.toString(), location.getBlockZ());
		map.put(DetailedPVEKills.Timestamp.toString(), timestamp);
		return map;
	}

	@Override
	public boolean isOnHold() { return onHold; }

	@Override
	public void setOnHold(boolean onHold) { this.onHold = onHold; }

	@Override
	public boolean refresh() { return onHold; }

}
