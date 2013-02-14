package com.wolvencraft.yasp.db.data.normal;

import java.util.Map;

public interface DataHolder {
	
	/**
	 * Returns the DataHolder label as String
	 * @return <b>String</b> DataHolder label
	 */
	public String getDataLabel();
	
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
	
	/**
	 * Checks if the DataHolder is a duplicate of another holder.<br />
	 * Does not check any stored data for equality. For example, two <b>PlayerDistances</b> will be equal if the stored player names are equal, even if the stored distances are not.
	 * @param holder DataHolder to check
	 * @return <b>true</b> if the argument is a duplicate, <b>false</b> otherwise
	 */
	public boolean equals(DataHolder holder);
	
	/**
	 * Checks if the DataHolder contains the specified arguments.<br />
	 * Does not check any stored data for equality. For example, two <b>PlayerDistances</b> will be equal if the stored player names are equal, even if the stored distances are not.<br />
	 * Arguments are case-sensitive. Exact match is required.
	 * @param arguments Arguments to check against
	 * @return <b>true</b> if the argument is a duplicate, <b>false</b> otherwise
	 */
	public boolean equals(String... arguments);
	
	/**
	 * Returns the name of the player
	 * @return <b>String</b> player name
	 */
	public String getPlayerName();
}
