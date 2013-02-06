package com.wolvencraft.yasp.Database.data.detailed;

/**
 * Represents data stored in a log format, i.e. new data is appended to the end of the table. No existing data can be changed.<br />
 * Multiple instances of this type can (and should) exist.
 * @author bitWolfy
 *
 */
public interface DetailedDataHolder {
	
	/**
	 * Returns a SQL query that needs to be ran in order to push data to the database
	 * @return SQL query
	 */
	public String getQuery();
	
	/**
	 * Checks if the data point is held due to some of the fields being incomplete
	 * @return <b>boolean</b> Whether the data being held back
	 */
	public boolean isOnHold();
	
	/**
	 * Set the hold status of the data point
	 * @param onHold Should the data be held back during the database sync
	 */
	public void setOnHold(boolean onHold);
	
	/**
	 * Refreshes the data point to see if it should still be held back
	 * @return <b>boolean</b> Whether the data should be held back
	 */
	public boolean refresh();
}
