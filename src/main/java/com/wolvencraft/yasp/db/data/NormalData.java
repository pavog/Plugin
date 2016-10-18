package com.wolvencraft.yasp.db.data;

/**
 * Represents the "totals" of statistical data. These entries are changing every time their corresponding data type changes.<br />
 * No duplicate entries are allowed.
 * @author bitWolfy
 *
 */
public abstract class NormalData {
    
    /**
     * Performs a database operation to fetch the data from the remote database.<br />
     * If no data is found in the database, the default values are inserted.
     * @param playerId Player ID
     */
    public abstract void fetchData(int playerId);
    
    /**
     * Performs a database operation to push the local data to the remote database.<br />
     * If no data is found in the database, the default values are inserted instead.
     * @param playerId Player ID
     * @return <b>true</b> if the insertion was successful, <b>false</b> otherwise
     */
    public abstract boolean pushData(int playerId);
    
    /**
     * Resets the values of the data store to the default ones
     * @param playerId Player ID
     */
    public abstract void clearData(int playerId);
}
