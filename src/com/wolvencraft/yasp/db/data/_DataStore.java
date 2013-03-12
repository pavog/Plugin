package com.wolvencraft.yasp.db.data;

import java.util.List;
import java.util.Map;

public interface _DataStore {
	
	/**
	 * Returns the dynamic entries in the data store.<br />
	 * Asynchronous method; changes to the returned List will not affect the data store.
	 * @return Dynamic entries in the data store
	 */
	public List<NormalData> getNormalData();
	
	/**
	 * Returns the static entries in the data store.
	 * @return Static entries in the data store
	 */
	public List<DetailedData> getDetailedData();
	
	/**
	 * Synchronizes the data from the data store to the database, then removes it from local storage<br />
	 * If an entry was not synchronized, it will not be removed.
	 */
	public void sync();
	
	/**
	 * Clears the data store of all locally stored data.
	 */
	public void dump();
	
	/**
	 * Represents the "totals" of statistical data. These entries are changing every time their corresponding data type changes.<br />
	 * No duplicate entries are allowed.
	 * @author bitWolfy
	 *
	 */
	public interface NormalData {
		
		/**
		 * Performs a database operation to fetch the data from the remote database.<br />
		 * If no data is found in the database, the default values are inserted.
		 */
		public void fetchData(int playerId);
		
		/**
		 * Performs a database operation to push the local data to the remote database.<br />
		 * If no data is found in the database, the default values are inserted instead.
		 * @return <b>true</b> if the insertion was successful, <b>false</b> otherwise
		 */
		public boolean pushData(int playerId);
		
		/**
		 * Returns the data values of the DataHolder in a Map form
		 * @return <b>Map</b> of column names and their corresponding values
		 */
		public Map<String, Object> getValues(int playerId);
	}
	
	
	/**
	 * Represents data stored in a log format, i.e. new data is appended to the end of the table. No existing data can be changed.<br />
	 * Multiple instances of this type could (and should) exist.
	 * @author bitWolfy
	 *
	 */
	public interface DetailedData {
		
		/**
		 * Explicitly pushes data to the remote database.<br />
		 * If the data holder is marked as <i>on hold</i>, skips the holder
		 * @return <b>true</b> if the holder has been synchronized and can be removed, <b>false</b> if it is on hold
		 */
		public boolean pushData(int playerId);
		
		/**
		 * Returns the data holder as a map of column names and corresponding values
		 * @return Map of column names and values
		 */
		public Map<String, Object> getValues(int playerId);
	}

}
