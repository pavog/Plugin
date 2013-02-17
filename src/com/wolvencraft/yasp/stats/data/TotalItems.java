package com.wolvencraft.yasp.stats.data;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.wolvencraft.yasp.db.data.Dynamic.TotalItemsEntry;

public class TotalItems {
	
	private List<TotalItemsEntry> data;
	
	public TotalItems() {
		data = new ArrayList<TotalItemsEntry>();
	}
	
	public void add(TotalItemsEntry newData) {
		data.add(newData);
	}
	
	public List<TotalItemsEntry> get() {
		List<TotalItemsEntry> temp = new ArrayList<TotalItemsEntry>();
		for(TotalItemsEntry value : data) temp.add(value);
		return temp;
	}
	
	public TotalItemsEntry get(int playerId, ItemStack itemStack) {
		itemStack.setAmount(1);
		for(TotalItemsEntry blocks : data) {
			if(blocks.getItemStack().equals(itemStack)) return blocks;
		}
		TotalItemsEntry entry = new TotalItemsEntry(playerId, itemStack);
		data.add(entry);
		return entry;
	}
	
	public void clear() {
		data.clear();
	}
	
	public void sync() {
		for(TotalItemsEntry blocks : data) blocks.pushData();
	}
	
}
