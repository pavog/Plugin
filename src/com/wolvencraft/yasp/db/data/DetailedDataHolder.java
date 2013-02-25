package com.wolvencraft.yasp.db.data;

import java.util.ArrayList;
import java.util.List;

import com.wolvencraft.yasp.db.data.detailed._DetailedData;

public class DetailedDataHolder {
	
	public List<_DetailedData> data;
	
	public DetailedDataHolder() {
		data = new ArrayList<_DetailedData>();
	}
	
	public void add(_DetailedData newData) {
		data.add(newData);
	}
	
	public List<_DetailedData> get() {
		List<_DetailedData> temp = new ArrayList<_DetailedData>();
		for(_DetailedData value : data) temp.add(value);
		return temp;
	}
	
	public void clear() {
		data.clear();
	}
	
	public void remove(_DetailedData oldData) {
		data.remove(oldData);
	}
	
	public void sync() {
		for(_DetailedData entry : get()) {
			if(entry.pushData() && entry.refresh()) remove(entry);
		}
	}
}
