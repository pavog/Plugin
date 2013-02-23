package com.wolvencraft.yasp.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.material.MaterialData;

import com.wolvencraft.yasp.DataCollector;
import com.wolvencraft.yasp.StatsPlugin;
import com.wolvencraft.yasp.util.Util;

public class BlockListener implements Listener {
	
	public BlockListener(StatsPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {
		if(!Util.isExempt(event.getPlayer())) return;
		DataCollector
			.get(event.getPlayer())
			.blockBreak(new MaterialData(event.getBlock().getType(), event.getBlock().getData()));
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event) {
		if(!Util.isExempt(event.getPlayer())) return;
		DataCollector
			.get(event.getPlayer())
			.blockPlace(new MaterialData(event.getBlock().getType(), event.getBlock().getData()));
	}
}
