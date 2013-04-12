/*
 * ScoreboardCommand.java
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

package com.wolvencraft.yasp.cmd;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.wolvencraft.yasp.DatabaseTask;
import com.wolvencraft.yasp.CommandManager;
import com.wolvencraft.yasp.Statistics;
import com.wolvencraft.yasp.util.Message;

/**
 * Score board command.<br />
 * Displays a score board-style statistics for the player.
 * @author bitWolfy
 *
 */
public class ScoreboardCommand implements BaseCommand {

    @Override
    public boolean run(String[] args) {
        if(!Statistics.isCraftBukkitCompatible()) {
            Message.sendFormattedError("This command is not compatible with the current version of CraftBukkit");
            return false;
        }
        if(!(CommandManager.getSender() instanceof Player)) {
            Message.sendFormattedError(CommandManager.getSender(), "This command can only be executed by a living player");
            return false;
        }
        
        Player player = (Player) CommandManager.getSender();
        Map<String, Object> stats = DatabaseTask.getSession(player.getName()).getTotals().getValues();
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        Objective objective = board.registerNewObjective("Blocks broken", "blocksBroken");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("Statistics");
        Score score = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Broken:"));
        score.setScore((Integer) stats.get("blocksBroken"));
        
        Message.sendFormattedSuccess("Displaying a scoreboard");
        return true;
    }

    @Override
    public void getHelp() {
        Message.formatHelp("book", "", "Displays some of your stats on a scoreboard");
    }

}
