package com.wolvencraft.yasp.db.data;

/**
 * Represents data stored in a log format. New data is appended to the end of the table. No existing data can be changed.<br />
 * Multiple instances of this type could (and should) exist.
 * @author bitWolfy
 *
 */
public abstract class DetailedData {
    
    /**
     * Explicitly pushes data to the remote database.<br />
     * If the data holder is marked as <i>on hold</i>, skips the holder
     * @param playerId Player ID
     * @return <b>true</b> if the holder has been synchronized and can be removed, <b>false</b> if it is on hold
     */
    public abstract boolean pushData(int playerId);
}
