package com.wolvencraft.yasp.events;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.material.MaterialData;

import com.wolvencraft.yasp.StatsPlugin;
import com.wolvencraft.yasp.db.data.detailed.BlockDestroyed;
import com.wolvencraft.yasp.db.data.detailed.BlockPlaced;
import com.wolvencraft.yasp.db.data.normal.DataHolder;
import com.wolvencraft.yasp.db.data.normal.DataLabel;
import com.wolvencraft.yasp.db.data.normal.TotalBlocks;
import com.wolvencraft.yasp.stats.DataCollector;

public class BlockListener implements Listener {
	
	public BlockListener(StatsPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		
		DataCollector.addDetailedData(new BlockDestroyed(player, new MaterialData(event.getBlock().getType())));
		
		List<DataHolder> data = DataCollector.getNormalDataByType(DataLabel.TotalBlocks.toParameterizedString(player.getPlayerListName()));
		DataHolder totalBlocks;
		if(data.isEmpty()) {
			totalBlocks = new TotalBlocks(player, new MaterialData(event.getBlock().getType()));
			DataCollector.addNormalData(totalBlocks);
		} else totalBlocks = data.get(0);
		TotalBlocks.class.cast(totalBlocks).addBroken();
		
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		
		DataCollector.addDetailedData(new BlockPlaced(player, new MaterialData(event.getBlock().getType())));
		
		List<DataHolder> data = DataCollector.getNormalDataByType(DataLabel.TotalBlocks.toParameterizedString(player.getPlayerListName()));
		DataHolder totalBlocks;
		if(data.isEmpty()) {
			totalBlocks = new TotalBlocks(player, new MaterialData(event.getBlock().getType()));
			DataCollector.addNormalData(totalBlocks);
		} else totalBlocks = data.get(0);
		TotalBlocks.class.cast(totalBlocks).addPlaced();
	}
}
