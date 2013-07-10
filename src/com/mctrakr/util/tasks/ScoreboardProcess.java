/*
 * ScoreboardProcess.java
 * 
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

package com.mctrakr.util.tasks;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import com.mctrakr.session.OnlineSession;
import com.mctrakr.settings.ConfigLock;
import com.mctrakr.settings.ConfigLock.PropertyType;
import com.mctrakr.util.NamedInteger;

public class ScoreboardProcess implements Runnable {
    
    private static ConfigLock lock = new ConfigLock(PropertyType.Scoreboards);
    private static List<OnlineSession> sessions = new ArrayList<OnlineSession>();
    
    public ScoreboardProcess() { }
    
    @Override
    public void run() {
        if(!lock.isEnabled()) return;
        
        for(OnlineSession session : new ArrayList<OnlineSession>(sessions)) {
            if(!session.isOnline()) {
                sessions.remove(session);
                continue;
            }
            
            Scoreboard board = session.getScoreboard();
            if(board == null) continue;
            
            Objective objective = session.getObjective();
            if(objective == null) continue;
            
            for(NamedInteger value : session.getPlayerTotals().getNamedValues()) {
                for(String name : value.getPossibleNames()) board.resetScores(Bukkit.getOfflinePlayer(name));
                objective.getScore(Bukkit.getOfflinePlayer(value.getName())).setScore(value.getValue());
            }
        }
    }
    
    /**
     * Adds a session to the list
     * @param session Session to add
     */
    public static void addPlayer(OnlineSession session) {
        if(sessions.contains(session)) return;
        sessions.add(session);
        
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        session.setScoreboard(board);
        board.clearSlot(DisplaySlot.SIDEBAR);
        
        Objective sidebar = board.getObjective("player_stats");
        session.setObjective(sidebar);
        if (sidebar == null) sidebar = board.registerNewObjective("player_stats", "dummy");
        sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
        sidebar.setDisplayName(session.getName());
    }
    
    /**
     * Removes the session from the list
     * @param session Session to remove
     */
    public static void removePlayer(OnlineSession session) {
        if(!sessions.contains(session)) return;
        sessions.remove(session);
        
        if(!session.isOnline()) return;
        session.getBukkitPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }
    
    /**
     * Checks whether the player is shown the statistics
     * @param session Session to look for
     * @return <b>true</b> if the stats are displayed on a scoreboard, <b>false</b> otherwise
     */
    public static boolean isDisplayed(OnlineSession session) {
        return sessions.contains(session);
    }
    
    @Override
    public void finalize() {
        sessions.clear();
    }
    
}
