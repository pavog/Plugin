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

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.wolvencraft.yasp.DataCollector;
import com.wolvencraft.yasp.db.data.hooks._HookFactory;
import com.wolvencraft.yasp.db.data.hooks._HookFactory.*;
import com.wolvencraft.yasp.db.data.receive.PlayerTotals;
import com.wolvencraft.yasp.db.data.sync.PlayersData;
import com.wolvencraft.yasp.db.data.sync._DataStoreFactory;
import com.wolvencraft.yasp.db.data.sync._DataStoreFactory.*;

/**
 * Represents a player session where the player is most likely online
 * @author bitWolfy
 *
 */
public class OnlineSession implements PlayerSession {
	
	public OnlineSession(Player player) {
		this.playerId = DataCollector.getPlayerId(player);
		this.playerName = player.getPlayerListName();
		
		this.dataStores = _DataStoreFactory.init(playerId);
		this.playerData = new PlayersData(player, playerId);
		this.pluginHooks = _HookFactory.init(player, playerId);
		this.playerTotals = new PlayerTotals(playerId);
	}
	
	private int playerId;
	private String playerName;
	private PlayersData playerData;
	private List<_DataStore> dataStores;
	private List<PluginHookData> pluginHooks;
	private PlayerTotals playerTotals;
	
	@Override
	public void run() {
		playerData.sync();
		for(_DataStore store : dataStores) store.sync();
		for(PluginHookData hookData : pluginHooks) hookData.pushData();
		playerTotals.fetchData();
	}

	@Override
	public OfflinePlayer getPlayer() {
		return Bukkit.getOfflinePlayer(playerName);
	}

	@Override
	public List<_DataStore> getDataStores() {
		
		return null;
	}

	@Override
	public void dumpData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public _DataStore getDataStore(DataStoreType type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isOnline() {
		// TODO Auto-generated method stub
		return false;
	}

}
