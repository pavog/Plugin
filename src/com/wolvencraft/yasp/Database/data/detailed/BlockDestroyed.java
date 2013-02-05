package com.wolvencraft.yasp.Database.data.detailed;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.wolvencraft.yasp.Database.CachedData;
import com.wolvencraft.yasp.Database.tables.detailed.BlocksDestroyedTable;
import com.wolvencraft.yasp.Utils.Util;

public class BlockDestroyed implements DetailedData {
	
	private boolean onHold = false;
	
	public BlockDestroyed(Player player, Block block) {
		this.playerName = player.getPlayerListName();
		this.block = block;
		this.timestamp = Util.getCurrentTime().getTime();
	}
	
	private String playerName;
	private Block block;
	private long timestamp;

	@Override
	public String getQuery() {
		if(onHold) return null;
		String query = "INSERT INTO " + BlocksDestroyedTable.TableName + " (" + BlocksDestroyedTable.MaterialId + ", " + BlocksDestroyedTable.PlayerId + ", " + BlocksDestroyedTable.World + ", " + BlocksDestroyedTable.XCoord + ", " + BlocksDestroyedTable.YCoord + ", " + BlocksDestroyedTable.ZCoord + ", " + BlocksDestroyedTable.Timestamp + ") " 
				+ "VALUES (" + block.getTypeId() + ", " + CachedData.getCachedPlayerId(playerName) + ", " + block.getWorld().getName() + ", " + block.getLocation().getBlockX() + ", " + block.getLocation().getBlockY() + ", " + block.getLocation().getBlockZ() + ", " + timestamp + ")";
		return query;
	}

	@Override
	public boolean isOnHold() { return onHold; }

	@Override
	public void setOnHold(boolean onHold) { this.onHold = onHold; }

	@Override
	public boolean refresh() { return onHold; }

}
