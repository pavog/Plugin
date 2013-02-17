package com.wolvencraft.yasp.stats.data;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.material.MaterialData;

import com.wolvencraft.yasp.db.data.Dynamic.TotalBlocksEntry;

public class TotalBlocks {
	
	private List<TotalBlocksEntry> data;
	
	public TotalBlocks() {
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
	
	public TotalBlocksEntry get(int playerId, MaterialData material) {
		for(TotalBlocksEntry blocks : data) {
			if(blocks.getMaterial().equals(material)) return blocks;
		}
		TotalBlocksEntry entry = new TotalBlocksEntry(playerId, material);
		data.add(entry);
		return entry;
	}
	
	public void clear() {
		data.clear();
	}
	
	public void sync() {
		for(TotalBlocksEntry blocks : data) blocks.pushData();
	}
	
}
