package com.wolvencraft.yasp.db.data.detailed;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.db.CachedData;
import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.tables.detailed.PVPTable;
import com.wolvencraft.yasp.util.Util;

public class DeathPVP implements DetailedDataHolder {
	
	private boolean onHold = false;
	
	public DeathPVP(Player killer, Player victim, ItemStack weapon) {
		this.killerName = killer.getPlayerListName();
		this.killerId = CachedData.getCachedPlayerId(killerName);
		this.victimName = victim.getPlayerListName();
		this.victimId = CachedData.getCachedPlayerId(victimName);
		this.weapon = weapon;
		this.location = victim.getLocation();
		this.timestamp = Util.getCurrentTime().getTime();
	}
	
	private String killerName;
	private int killerId;
	private String victimName;
	private int victimId;
	private ItemStack weapon;
	private Location location;
	private long timestamp;
	
	@Override
	public boolean pushData() {
		return QueryUtils.insert(PVPTable.TableName.toString(), getValues());
	}

	@Override
	public Map<String, Object> getValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(PVPTable.PlayerID.toString(), killerId);
		map.put(PVPTable.VictimID.toString(), victimId);
		map.put(PVPTable.MaterialID.toString(), weapon.getTypeId());
		map.put(PVPTable.World.toString(), location.getWorld().getName());
		map.put(PVPTable.XCoord.toString(), location.getBlockX());
		map.put(PVPTable.YCoord.toString(), location.getBlockY());
		map.put(PVPTable.ZCoord.toString(), location.getBlockZ());
		map.put(PVPTable.Timestamp.toString(), timestamp);
		return map;
	}

	@Override
	public boolean isOnHold() { return onHold; }

	@Override
	public void setOnHold(boolean onHold) { this.onHold = onHold; }

	@Override
	public boolean refresh() { return onHold; }

}
