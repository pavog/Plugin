package com.wolvencraft.yasp.db.data;

import java.util.ArrayList;
import java.util.List;

import com.wolvencraft.yasp.db.data.Static.StaticData;

public class DetailedDataHolder {
	
	public List<StaticData> data;
	
	public DetailedDataHolder() {
		data = new ArrayList<StaticData>();
	}
	
	public void add(StaticData newData) {
		data.add(newData);
	}
	
	public List<StaticData> get() {
		List<StaticData> temp = new ArrayList<StaticData>();
		for(StaticData value : data) temp.add(value);
		return temp;
	}
	
	public void clear() {
		data.clear();
	}
	
	public void remove(StaticData oldData) {
		data.remove(oldData);
	}
	
	public void sync() {
		for(StaticData entry : get()) {
			if(entry.pushData() && entry.refresh()) remove(entry);
		}
	}
}
