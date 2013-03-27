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

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * A simple API that allows the plugin to display score boards
 * @author bitWolfy
 *
 */
public class ScoreboardAPI {
	
	private static List<Scoreboard> boards = new ArrayList<Scoreboard>();
	
	/**
	 * Returns all score boards stored in the plugin
	 * @return List of score boards
	 */
	public static List<Scoreboard> get() {
		return boards;
	}
	
	/**
	 * Returns the score board with the specified name
	 * @param id Board name 
	 * @return Returns the new score board, or <b>null</b> if the is no board with such name
	 */
	public static Scoreboard get(String id) {
		for (Scoreboard s : boards) {
			if (s.getId() == id) return s;
		}
		return null;
	}
	
	/**
	 * Adds a new score board
	 * @param id Board name
	 * @param priority Board priority
	 * @return Returns the new score board, or <b>null</b> if the score board with this name already exists
	 */
	public static Scoreboard add(String id, int priority) {
		for (Scoreboard board : boards) {
			if (board.getId() == id) return null;
		}
		Scoreboard board = new Scoreboard(id, priority);
		boards.add(board);
		return board;
	}
	
	/**
	 * Refreshes all score boards for the player
	 * @param player Player to refresh the board for
	 */
	public static void refresh(Player player) {
		for (Scoreboard board : boards) {
			board.checkVisibility(player);
		}
	}
	
	/**
	 * Refreshes all score boards for all online players
	 */
	public static void refresh() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			refresh(player);
		}
	}
}
