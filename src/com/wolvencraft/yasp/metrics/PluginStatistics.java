package com.wolvencraft.yasp.metrics;
 
import java.io.IOException;

import com.wolvencraft.yasp.StatsPlugin;
import com.wolvencraft.yasp.exceptions.MetricsConnectionException;
 
public class PluginStatistics {
	 
	private Metrics metrics;
	 
	public PluginStatistics(StatsPlugin plugin) throws MetricsConnectionException {
		try {
			this.metrics = new Metrics(plugin);
			if(metrics.isOptOut()) throw new MetricsConnectionException("Cannot connect to PluginMetrics :: opt out");
			metrics.start();
		}
		catch (IOException e) { throw new MetricsConnectionException("An error occurred while connecting to PluginMetrics"); }
	}
	 
	public Metrics getMetrics() {
		return metrics;
	}
}