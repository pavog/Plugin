package com.wolvencraft.yasp.metrics;
 
import java.io.IOException;

import com.wolvencraft.yasp.StatsPlugin;
import com.wolvencraft.yasp.db.exceptions.MetricsConnectionException;
 
public class Statistics {
	 
	private Metrics metrics;
	 
	public Statistics(StatsPlugin plugin) throws MetricsConnectionException {
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