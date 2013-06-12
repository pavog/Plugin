/*
 * PlayerCommands.java
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

import java.util.List;

import org.bukkit.entity.Player;

import com.wolvencraft.yasp.CommandManager;
import com.wolvencraft.yasp.CommandManager.Command;
import com.wolvencraft.yasp.util.Message;
import com.wolvencraft.yasp.util.cache.OnlineSessionCache;

public class PlayerCommands {
    
    @Command(
            alias = "book",
            minArgs = 0,
            maxArgs = 0,
            permission = "stats.cmd.book",
            allowConsole = false,
            usage = "/stats book",
            description = "Get a book with all your statistical information"
            )
    public static boolean book(List<String> args) {
        Player player = (Player) CommandManager.getSender();
        player.getInventory().addItem(com.wolvencraft.yasp.util.BookUtil.compileStatsBook(player));
        return false;
    }
    
    @Command(
            alias = "scoreboard",
            minArgs = 0,
            maxArgs = 0,
            permission = "stats.cmd.board",
            allowConsole = false,
            usage = "/stats scoreboard",
            description = "Displays stats on a scoreboard"
            )
    public static boolean scoreboard(List<String> args) {
        Player player = (Player) CommandManager.getSender();
        if(OnlineSessionCache.fetch(player).toggleScoreboard()) Message.sendFormattedSuccess("Displaying a scoreboard");
        else Message.sendFormattedSuccess("Scoreboard disabled");
        return true;
    }
    
}
