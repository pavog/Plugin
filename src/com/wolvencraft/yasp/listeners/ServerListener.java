package com.wolvencraft.yasp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import com.wolvencraft.yasp.DataCollector;
import com.wolvencraft.yasp.StatsPlugin;

public class ServerListener implements Listener {
	
	public ServerListener(StatsPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onWeatherChange(WeatherChangeEvent event) {
		if(!Bukkit.getWorlds().get(0).equals(event.getWorld())) return;
		DataCollector.global().weatherChange(event.toWeatherState(), event.getWorld().getWeatherDuration());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPluginEnable(PluginEnableEvent event) {
		DataCollector.global().pluginNumberChange();
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPluginDisable(PluginDisableEvent event) {
		DataCollector.global().pluginNumberChange();
	}
}
