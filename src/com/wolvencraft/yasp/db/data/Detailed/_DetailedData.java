package com.wolvencraft.yasp.db.data.detailed;

import java.util.Map;

/**
 * Represents data stored in a log format, i.e. new data is appended to the end of the table. No existing data can be changed.<br />
 * Multiple instances of this type can (and should) exist.
 * @author bitWolfy
 *
 */
public interface _DetailedData {
	
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
