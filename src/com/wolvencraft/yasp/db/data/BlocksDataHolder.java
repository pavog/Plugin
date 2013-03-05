package com.wolvencraft.yasp.db.data;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

import com.wolvencraft.yasp.db.data.normal.TotalBlocksEntry;

public class BlocksDataHolder {
	
	private List<TotalBlocksEntry> data;
	
	public BlocksDataHolder() {
		data = new ArrayList<TotalBlocksEntry>();
	}
	
	public void add(TotalBlocksEntry newData) {
		data.add(newData);
	}
	
	public List<TotalBlocksEntry> get() {
		List<TotalBlocksEntry> temp = new ArrayList<TotalBlocksEntry>();
		for(TotalBlocksEntry value : data) temp.add(value);
		return temp;
	}
	
	public TotalBlocksEntry get(Material type, byte blockData) {
		for(TotalBlocksEntry entry : data) {
			if(entry.equals(type, blockData)) return entry;
		}
		TotalBlocksEntry entry = new TotalBlocksEntry(type, blockData);
		data.add(entry);
		return entry;
	}
	
	public void clear() {
		data.clear();
	}
	
	public void sync(int playerId) {
		for(TotalBlocksEntry entry : data) entry.pushData(playerId);
	}
	
}
