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

package com.wolvencraft.yasp.scoreboard;

import net.minecraft.server.v1_5_R2.Packet206SetScoreboardObjective;
import net.minecraft.server.v1_5_R2.Packet207SetScoreboardScore;
import net.minecraft.server.v1_5_R2.Packet208SetScoreboardDisplayObjective;
import net.minecraft.server.v1_5_R2.PlayerConnection;
import org.bukkit.craftbukkit.v1_5_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple wrapper around the Minecraft score boards.<br />
 * Until Bukkit pulls its shit together, this is a working solution
 * @author bitWolfy
 *
 */
public class Scoreboard {
	
	/**
	 * <b>Default constructor</b><br />
	 * Creates a new score board with the specified ID
	 * @param id Unique ID of the board
	 */
	Scoreboard(String id) {
		this.id = id;
	}
	
	/**
	 * <b>Constructor</b><br />
	 * Creates a new score board with the specified ID
	 * @param id Unique ID of the board
	 * @param Scoreboard type
	 */
	Scoreboard(String id, Type type) {
		this.id = id;
		this.type = type;
	}
	
	/**
	 * <b>Constructor</b><br />
	 * Creates a new score board with the specified ID and priority
	 * @param id Unique ID of the board
	 * @param priority Board priority
	 */
	Scoreboard(String id, int priority) {
		this.id = id;
		this.priority = priority;
	}
	
	/**
	 * <b>Constructor</b><br />
	 * Creates a new score board with the specified ID and priority
	 * @param id Unique ID of the board
	 * @param priority Board priority
	 * @param Scoreboard type
	 */
	Scoreboard(String id, int priority, Type type) {
		this.id = id;
		this.priority = priority;
		this.type = type;
	}
	
	private List<Player> players = new ArrayList<Player>();
	private HashMap<String, Integer> items = new HashMap<String, Integer>();
	private Type type = Type.SIDEBAR;
	private String id;
	private String name = "Not initialized";
	private int priority = 10;
	
	/**
	 * Returns the unique ID of the score board
	 * @return Board ID
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Returns the display name of the score board
	 * @return Board display name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the board priority
	 * @return Board priority
	 */
	public int getPriority() {
		return priority;
	}
	
	/**
	 * Returns the board type
	 * @return Board type
	 */
	public Type getType() {
		return type;
	}
	
	/**
	 * Sets the board display name to the one specified
	 * @param displayName New display name
	 */
	public void setName(String name) {
		this.name = name;
		Packet206SetScoreboardObjective pack = new Packet206SetScoreboardObjective();
		pack.a = id;
		pack.b = name;
		pack.c = 2;
		for (Player player : players) {
			if (!isOnTop(player)) continue;
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(pack);
		}
	}
	
	/**
	 * Adds a new item to the score board
	 * @param name Name of the item
	 * @param value Item value
	 */
	public void add(String name, int value) {
		items.put(name, value);
		Packet207SetScoreboardScore pack = new Packet207SetScoreboardScore();
		pack.a = name;
		pack.c = value;
		pack.d = 0;
		pack.b = id;
		for (Player player : players) {
			if (!isOnTop(player)) continue;
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(pack);
		}
	}
	
	/**
	 * Removes the specified item from the score board
	 * @param name Name of the item to be removed
	 */
	public void remove(String name) {
		if (items.remove(name) != null) {
			Packet207SetScoreboardScore pack = new Packet207SetScoreboardScore();
			pack.a = name;
			pack.c = 0;
			pack.d = 1;
			pack.b = id;
			for (Player player : players) {
				if (!isOnTop(player)) continue;
				((CraftPlayer) player).getHandle().playerConnection.sendPacket(pack);
			}
		}
	}
	
	/**
	 * Checks if the specified player is associated with the score board
	 * @param player Player to search for
	 * @return <b>true</b> if the player is in the list, <b>false</b> otherwise
	 */
	public boolean isVisible(Player player) {
		return players.contains(player);
	}
	
	/**
	 * Sets the score board visibility for all players
	 * @param show <b>true</b> if the board should be shown, <b>false</b> otherwise
	 */
	public void setVisible(boolean show) {
		for(Player player : players) {
			setVisible(player, false);
		}
	}
	
	/**
	 * Sets the score board visibility for the specific player
	 * @param player Player
	 * @param show <b>true</b> if the board should be shown, <b>false</b> otherwise
	 */
	public void setVisible(Player player, boolean show) {
		if (show) {
			if (!players.contains(player)) {
				players.add(player);
				ScoreboardAPI.refresh(player);
			}
		} else {
			if (players.remove(player)) {
				Packet206SetScoreboardObjective pack = new Packet206SetScoreboardObjective();
				pack.a = id;
				pack.b = "";
				pack.c = 1;
				((CraftPlayer) player).getHandle().playerConnection.sendPacket(pack);
				ScoreboardAPI.refresh(player);
			}
		}
	}
	
	private void updatePosition(Player p) {
		if (!isOnTop(p)) {
			return;
		}
		Packet208SetScoreboardDisplayObjective pack2 = new Packet208SetScoreboardDisplayObjective();
		pack2.a = type.ordinal();
		pack2.b = id;
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(pack2);
	}
	
	/**
	 * Checks the visibility of the board for the specified player
	 * @param player Player to check
	 */
	public void checkVisibility(Player player) {
		if (!players.contains(player)) {
			return;
		}
		
		PlayerConnection conn = ((CraftPlayer) player).getHandle().playerConnection;
		if (isOnTop(player)) {
			Packet206SetScoreboardObjective pack = new Packet206SetScoreboardObjective();
			
			// Check if the board should be enabled
			pack.a = id;
			pack.b = name;
			pack.c = 0;
			conn.sendPacket(pack);
			for (String name2 : items.keySet()) {
				Integer valObj = items.get(name2);
				if (valObj == null) {
					continue;
				}
				int val = valObj.intValue();
				Packet207SetScoreboardScore pack2 = new Packet207SetScoreboardScore();
				pack2.a = name2;
				pack2.c = val;
				pack2.d = 0;
				pack2.b = id;
				conn.sendPacket(pack2);
			}
			updatePosition(player);
			
			// Check if the board should be disabled
			pack.a = id;
			pack.b = name;
			pack.c = 1;
			conn.sendPacket(pack);
		}
	}
	
	/**
	 * Checks if the score board is the top one for the player
	 * @param player Player to check
	 * @return <b>false</b> if there are score boards above this one, <b>false</b> otherwise
	 */
	private boolean isOnTop(Player player) {
		for(Scoreboard board : ScoreboardAPI.get()) {
			if(board == this) continue;
			if(board.isVisible(player)
				&& board.getType() == type
				&& (board.getPriority() >= priority)) return false;
		}
		return true;
	}
	
	/**
	 * Represents the score board type
	 * @author bitWolfy
	 *
	 */
	public enum Type {
		SIDEBAR(0),
		PLAYER_LIST(1);
		
		Type(int id) {
			this.id = id;
		}
		
		int id;
		
		public int getId() { return id; }
	}
}
