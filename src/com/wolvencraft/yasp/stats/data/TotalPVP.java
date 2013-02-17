package com.wolvencraft.yasp.stats.data;

import java.util.ArrayList;
import java.util.List;

import com.wolvencraft.yasp.db.data.Dynamic.TotalPVPEntry;

public class TotalPVP {
	
	private List<TotalPVPEntry> data;
	
	public TotalPVP() {
		data = new ArrayList<TotalPVPEntry>();
	}
	
	public void add(TotalPVPEntry newData) {
		data.add(newData);
	}
	
	public List<TotalPVPEntry> get() {
		List<TotalPVPEntry> temp = new ArrayList<TotalPVPEntry>();
		for(TotalPVPEntry value : data) temp.add(value);
		return temp;
	}
	
	public TotalPVPEntry get(int killerId, int victimId) {
		for(TotalPVPEntry entry : data) {
			if(entry.equals(killerId, victimId)) return entry;
		}
		TotalPVPEntry entry = new TotalPVPEntry(killerId, victimId);
		data.add(entry);
		return entry;
	}
	
	public void clear() {
		data.clear();
	}
	
	public void sync() {
		for(TotalPVPEntry blocks : data) blocks.pushData();
	}
	
}
