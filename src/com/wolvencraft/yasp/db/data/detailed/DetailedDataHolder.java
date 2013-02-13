package com.wolvencraft.yasp.db.data.detailed;

import java.util.Map;

/**
 * Represents data stored in a log format, i.e. new data is appended to the end of the table. No existing data can be changed.<br />
 * Multiple instances of this type can (and should) exist.
 * @author bitWolfy
 *
 */
public interface DetailedDataHolder {
	
	/**
	 * Explicitly pushes data to the remote database.<br />
	 * If the data holder is marked as <i>on hold</i>, skips the holder
	 * @return <b>true</b> if the holder has been synched and can be removed, <b>false</b> if it is on hold
	 */
	public boolean pushData();
	
	/**
	 * Returns the data holder as a map of column names and corresponding values
	 * @return Map of column names and values
	 */
	public Map<String, Object> getValues();
	
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
