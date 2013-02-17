package com.wolvencraft.yasp.stats.data;

import java.util.ArrayList;
import java.util.List;

import com.wolvencraft.yasp.db.data.Dynamic.TotalPVEEntry;

public class TotalPVE {
	
	private List<TotalPVEEntry> data;
	
	public TotalPVE() {
		data = new ArrayList<TotalPVEEntry>();
	}
	
	public void add(TotalPVEEntry newData) {
		data.add(newData);
	}
	
	public List<TotalPVEEntry> get() {
		List<TotalPVEEntry> temp = new ArrayList<TotalPVEEntry>();
		for(TotalPVEEntry value : data) temp.add(value);
		return temp;
	}
	
	public TotalPVEEntry get(int killerId, String victimId) {
		for(TotalPVEEntry entry : data) {
			if(entry.equals(killerId, victimId)) return entry;
		}
		TotalPVEEntry entry = new TotalPVEEntry(killerId, victimId);
		data.add(entry);
		return entry;
	}
	
	public void clear() {
		data.clear();
	}
	
	public void sync() {
		for(TotalPVEEntry blocks : data) blocks.pushData();
	}
	
}
