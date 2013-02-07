package com.wolvencraft.yasp.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.wolvencraft.yasp.StatsPlugin;
import com.wolvencraft.yasp.db.data.detailed.BlockDestroyed;
import com.wolvencraft.yasp.db.data.detailed.BlockPlaced;
import com.wolvencraft.yasp.stats.CollectedData;

public class BlockListener implements Listener {
	
	public BlockListener(StatsPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {
		CollectedData.addDetailedData(new BlockDestroyed(event.getPlayer(), event.getBlock()));
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event) {
		CollectedData.addDetailedData(new BlockPlaced(event.getPlayer(), event.getBlock()));
	}
}
