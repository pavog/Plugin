package com.wolvencraft.yasp.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.wolvencraft.yasp.DataCollector;
import com.wolvencraft.yasp.StatsPlugin;
import com.wolvencraft.yasp.util.Util;

public class BlockListener implements Listener {
	
	public BlockListener(StatsPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {
		if(StatsPlugin.getPaused()) return;
		Player player = event.getPlayer();
		if(Util.isExempt(player, "block.break")) return;
		DataCollector
			.get(player)
			.blocks()
			.blockBreak(event.getBlock().getLocation(), event.getBlock().getType(), event.getBlock().getData());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event) {
		if(StatsPlugin.getPaused()) return;
		Player player = event.getPlayer();
		if(Util.isExempt(player, "block.place")) return;
		DataCollector
			.get(player)
			.blocks()
			.blockPlace(event.getBlock().getLocation(), event.getBlock().getType(), event.getBlock().getData());
	}
}
