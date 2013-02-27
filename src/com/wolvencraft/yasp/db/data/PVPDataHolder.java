package com.wolvencraft.yasp.db.data;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.db.data.normal.TotalPVPEntry;

public class PVPDataHolder {
	
	private List<TotalPVPEntry> data;
	
	public PVPDataHolder() {
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
	
	public TotalPVPEntry get(int killerId, int victimId, ItemStack weapon) {
		for(TotalPVPEntry entry : data) {
			if(entry.equals(killerId, victimId, weapon)) return entry;
		}
		TotalPVPEntry entry = new TotalPVPEntry(killerId, victimId, weapon);
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
