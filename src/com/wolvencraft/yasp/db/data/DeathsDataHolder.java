package com.wolvencraft.yasp.db.data;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.wolvencraft.yasp.db.data.normal.TotalDeathsEntry;

public class DeathsDataHolder {
	
	private List<TotalDeathsEntry> data;
	
	public DeathsDataHolder() {
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
	
	public TotalDeathsEntry get(DamageCause cause) {
		for(TotalDeathsEntry entry : data) {
			if(entry.equals(cause)) return entry;
		}
		TotalDeathsEntry entry = new TotalDeathsEntry(cause);
		data.add(entry);
		return entry;
	}
	
	public void clear() {
		data.clear();
	}
	
	public void sync(int playerId) {
		for(TotalDeathsEntry entry : data) entry.pushData(playerId);
	}
	
}
