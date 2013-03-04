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
	
	public TotalPVPEntry get(int victimId, ItemStack weapon) {
		for(TotalPVPEntry entry : data) {
			if(entry.equals(victimId, weapon)) return entry;
		}
		TotalPVPEntry entry = new TotalPVPEntry(victimId, weapon);
		data.add(entry);
		return entry;
	}
	
	public void clear() {
		data.clear();
	}
	
	public void sync(int killerId) {
		for(TotalPVPEntry entry : data) entry.pushData(killerId);
	}
	
}
