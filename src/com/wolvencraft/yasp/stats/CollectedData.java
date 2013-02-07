package com.wolvencraft.yasp.stats;

import java.util.ArrayList;
import java.util.List;

import com.wolvencraft.yasp.db.data.detailed.DetailedDataHolder;
import com.wolvencraft.yasp.db.data.normal.DataHolder;
import com.wolvencraft.yasp.db.data.normal.DataLabel;

public class CollectedData {

	private static List<DataHolder> simpleData;
	private static List<DetailedDataHolder> detailedData;
	
	public CollectedData() {
		simpleData = new ArrayList<DataHolder>();
		detailedData = new ArrayList<DetailedDataHolder>();
	}
	
	public static List<DataHolder> getNormalData() {
		List<DataHolder> data = new ArrayList<DataHolder>();
		for(DataHolder entry : simpleData) data.add(entry);
		return data;
	}
	
	public static boolean flushSimleData() {
		if(simpleData.isEmpty()) return false;
		simpleData.clear();
		return true;
	}

	public static DataHolder getNormalDataExact(DataLabel label) {
		for(DataHolder holder : simpleData) {
			if(holder.getDataLabel().equals(label.getAlias())) return holder;
		}
		return null;
	}
	
	public static List<DataHolder> matchNormalData(DataLabel label) {
		List<DataHolder> data = new ArrayList<DataHolder>();
		for(DataHolder holder : simpleData) {
			if(holder.getDataLabel().startsWith(label.getAlias())) data.add(holder);
		}
		return data;
	}
	
	public static List<DataHolder> matchNormalData(String label) {
		List<DataHolder> data = new ArrayList<DataHolder>();
		for(DataHolder holder : simpleData) {
			if(holder.getDataLabel().equals(label)) data.add(holder);
		}
		return data;
	}
	
	public static boolean addNormalData(DataHolder newHolder) {
		for(DataHolder holder : simpleData) {
			if(holder.getDataLabel().equals(newHolder.getDataLabel())) return false;
		}
		simpleData.add(newHolder);
		return true;
	}
	
	public static List<DetailedDataHolder> getDetailedData() {
		List<DetailedDataHolder> data = new ArrayList<DetailedDataHolder>();
		for(DetailedDataHolder entry : detailedData) data.add(entry);
		return data;
	}
	
	public static boolean flushDetailedData(boolean force) {
		if(detailedData.isEmpty()) return false;
		for(DetailedDataHolder entry : getDetailedData()) {
			if(force || !entry.isOnHold()) detailedData.remove(entry);
		}
		return true;
	}
	
	public static boolean addDetailedData(DetailedDataHolder newHolder) {
		detailedData.add(newHolder);
		return true;
	}
}