/*
 * Statistics
 * Copyright (C) 2013 bitWolfy <http://www.wolvencraft.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

package com.wolvencraft.yasp.session;

import java.util.List;

import org.bukkit.OfflinePlayer;

import com.wolvencraft.yasp.db.data.sync._DataStoreFactory.*;

/**
 * Generic interface for player sessions
 * @author bitWolfy
 *
 */
public interface PlayerSession extends Runnable {
	
	/**
	 * Executes a database synchronization on schedule.
	 */
	public void run();
	
	/**
	 * Returns a player object associated with the session.<br />
	 * Player may be offline.
	 * @return Player object
	 */
	public OfflinePlayer getPlayer();
	
	/**
	 * Returns all applicable data stores for the current user session.<br />
	 * Usefulness pending.
	 * @deprecated
	 * @return
	 */
	public List<_DataStore> getDataStores();
	
	/**
	 * Dumps all the local data in all data stores for the current session
	 */
	public void dumpData();
	
	/**
	 * Returns a data store with the specified DataStoreType
	 * @param type Type of the data store to return
	 * @return DataStore
	 */
	public _DataStore getDataStore(DataStoreType type);
	
	/**
	 * Checks if the player is online
	 * @return <b>true</b> if the player is online, <b>false</b> otherwise
	 */
	public boolean isOnline();
	
}
