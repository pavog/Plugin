package com.wolvencraft.yasp.stats.data;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.wolvencraft.yasp.db.data.Dynamic.TotalDeathsEntry;

public class TotalDeaths {
	
	private List<TotalDeathsEntry> data;
	
	public TotalDeaths() {
		data = new ArrayList<TotalDeathsEntry>();
	}
	
	public void add(TotalDeathsEntry newData) {
		data.add(newData);
	}
	
	public List<TotalDeathsEntry> get() {
		List<TotalDeathsEntry> temp = new ArrayList<TotalDeathsEntry>();
		for(TotalDeathsEntry value : data) temp.add(value);
		return temp;
	}
	
	public TotalDeathsEntry get(int playerId, DamageCause cause) {
		for(TotalDeathsEntry entry : data) {
			if(entry.equals(playerId, cause)) return entry;
		}
		TotalDeathsEntry entry = new TotalDeathsEntry(playerId, cause);
		data.add(entry);
		return entry;
	}
	
	public void clear() {
		data.clear();
	}
	
	public void sync() {
		for(TotalDeathsEntry blocks : data) blocks.pushData();
	}
	
}
