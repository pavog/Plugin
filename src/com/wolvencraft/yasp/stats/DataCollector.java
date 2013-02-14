package com.wolvencraft.yasp.stats;

import java.util.ArrayList;
import java.util.List;

import com.wolvencraft.yasp.db.data.detailed.DetailedDataHolder;
import com.wolvencraft.yasp.db.data.normal.DataHolder;
import com.wolvencraft.yasp.db.data.normal.DataLabel;

public class DataCollector {

	private static List<DataHolder> simpleData;
	private static List<DetailedDataHolder> detailedData;
	
	/**
	 * <b>Default constructor</b><br />
	 * Stores statistical data until it is synched with the database
	 */
	public DataCollector() {
		simpleData = new ArrayList<DataHolder>();
		detailedData = new ArrayList<DetailedDataHolder>();
	}
	
	/**
	 * Returns all re-writable data.<br />
	 * The resulting list is not synchronized; adding or removing new objects will be ineffective
	 * @return List of re-writable DataHolders
	 */
	public static List<DataHolder> getNormalData() {
		List<DataHolder> data = new ArrayList<DataHolder>();
		for(DataHolder entry : simpleData) data.add(entry);
		return data;
	}
	
	/**
	 * Clears the re-writable data of all local data
	 * @return <b>true</b> if the data was cleared, <b>false</b> if the data bank was already empty
	 */
	public static boolean flushNormalData() {
		if(simpleData.isEmpty()) return false;
		simpleData.clear();
		return true;
	}
	
	/**
	 * Returns a list of all DataHolders by type
	 * @param label DataLabel
	 * @return List of DataHolders
	 */
	public static List<DataHolder> getNormalDataByType(DataLabel label) {
		List<DataHolder> data = new ArrayList<DataHolder>();
		for(DataHolder holder : simpleData) {
			if(holder.getDataLabel().startsWith(label.toString())) data.add(holder);
		}
		return data;
	}
	
	/**
	 * Returns a list of all DataHolders by type
	 * @param label DataLabel
	 * @return List of DataHolders
	 */
	public static List<DataHolder> getNormalDataByType(String label) {
		List<DataHolder> data = new ArrayList<DataHolder>();
		for(DataHolder holder : simpleData) {
			if(holder.getDataLabel().equals(label)) data.add(holder);
		}
		return data;
	}
	
	/**
	 * Adds a new DataHolder to be stored and synchronized
	 * @param newHolder DataHolder to add
	 * @return <b>true</b> if the DataHolder was added, <b>false</b> if a holder with this name already exists
	 */
	public static boolean addNormalData(DataHolder newHolder) {
		for(DataHolder holder : simpleData) {
			if(holder.getDataLabel().equals(newHolder.getDataLabel())) return false;
		}
		simpleData.add(newHolder);
		return true;
	}
	
	/**
	 * Returns all read-only data.<Br />
	 * The resulting list is not synchronized; adding or removing new objects will be ineffective
	 * @return List of read-only DataHolders
	 */
	public static List<DetailedDataHolder> getDetailedData() {
		List<DetailedDataHolder> data = new ArrayList<DetailedDataHolder>();
		for(DetailedDataHolder entry : detailedData) data.add(entry);
		return data;
	}
	
	/**
	 * Clears the re-writable data of all local data
	 * @param force Force remove all (even <i>on-hold</i>) data
	 * @return <b>true</b> if the data was cleared, <b>false</b> if the data bank was already empty
	 */
	public static boolean flushDetailedData(boolean force) {
		if(detailedData.isEmpty()) return false;
		for(DetailedDataHolder entry : getDetailedData()) {
			if(force || !entry.isOnHold()) detailedData.remove(entry);
		}
		return true;
	}
	
	/**
	 * Adds a new DataHolder to be stored and synchronized
	 * @param newHolder DataHolder to add
	 * @return <b>true</b> if the DataHolder was added, <b>false</b> if a holder with this name already exists
	 */
	public static boolean addDetailedData(DetailedDataHolder newHolder) {
		detailedData.add(newHolder);
		return true;
	}
}