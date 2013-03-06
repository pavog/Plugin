package com.wolvencraft.yasp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

import com.wolvencraft.yasp.DataCollector;
import com.wolvencraft.yasp.StatsPlugin;

public class ServerListener implements Listener {
	
	public ServerListener(StatsPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	public void onWeatherChange(WeatherChangeEvent event) {
		if(!Bukkit.getWorlds().get(0).equals(event.getWorld())) return;
		DataCollector.global().weatherChange(event.toWeatherState(), event.getWorld().getWeatherDuration());
	}
}
