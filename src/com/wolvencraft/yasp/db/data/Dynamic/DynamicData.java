package com.wolvencraft.yasp.db.data.Dynamic;

import java.util.Map;

public interface DynamicData {
	
	/**
	 * Performs a database operation to fetch the data from the remote database.<br />
	 * If no data is found in the database, the default values are inserted.
	 */
	public void fetchData();
	
	/**
	 * Performs a database operation to push the local data to the remote database.<br />
	 * If no data is found in the database, the default values are inserted instead.
	 * @return <b>true</b> if the insertion was successful, <b>false</b> otherwise
	 */
	public boolean pushData();
	
	/**
	 * Returns the data values of the DataHolder in a Map form
	 * @return <b>Map</b> of column names and their corresponding values
	 */
	public Map<String, Object> getValues();
}
