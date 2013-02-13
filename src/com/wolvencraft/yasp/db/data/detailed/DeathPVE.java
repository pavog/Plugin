package com.wolvencraft.yasp.db.data.detailed;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.db.CachedData;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.detailed.PVETable;
import com.wolvencraft.yasp.util.Util;

public class DeathPVE implements DetailedDataHolder {
	
	private boolean onHold = false;
	
	public DeathPVE(Player player, EntityType creatureType, ItemStack weapon, boolean playerKilled) {
		this.playerName = player.getPlayerListName();
		this.playerId = CachedData.getCachedPlayerId(playerName);
		this.creatureType = creatureType;
		this.weapon = weapon;
		this.location = player.getLocation();
		this.playerKilled = playerKilled;
		this.timestamp = Util.getCurrentTime().getTime();
	}
	
	private String playerName;
	private int playerId;
	private EntityType creatureType;
	private ItemStack weapon;
	private Location location;
	private boolean playerKilled;
	private long timestamp;
	
	@Override
	public boolean pushData() {
		return QueryUtils.insert(PVETable.TableName.toString(), getValues());
	}

	@Override
	public Map<String, Object> getValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(PVETable.PlayerID.toString(), playerId);
		map.put(PVETable.CreatureID.toString(), creatureType);
		map.put(PVETable.MaterialID.toString(), weapon.getTypeId());
		if(playerKilled) map.put(PVETable.PlayerKilled.toString(), "Y");
		else map.put(PVETable.PlayerKilled.toString(), "N");
		map.put(PVETable.World.toString(), location.getWorld().getName());
		map.put(PVETable.XCoord.toString(), location.getBlockX());
		map.put(PVETable.YCoord.toString(), location.getBlockY());
		map.put(PVETable.ZCoord.toString(), location.getBlockZ());
		map.put(PVETable.Timestamp.toString(), timestamp);
		return map;
	}

	@Override
	public boolean isOnHold() { return onHold; }

	@Override
	public void setOnHold(boolean onHold) { this.onHold = onHold; }

	@Override
	public boolean refresh() { return onHold; }

}
