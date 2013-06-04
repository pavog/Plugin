/*
 * PluginCommands.java
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

import org.bukkit.ChatColor;

import com.wolvencraft.yasp.CommandManager;
import com.wolvencraft.yasp.CommandManager.Command;
import com.wolvencraft.yasp.CommandManager.CommandPair;
import com.wolvencraft.yasp.Statistics;
import com.wolvencraft.yasp.util.Message;

public class PluginCommands {
    
    @Command(
            alias = "help",
            minArgs = 0,
            maxArgs = 0,
            permission = "stats.cmd.help",
            allowConsole = true,
            usage = "/stats help",
            description = "Full command help listing"
            )
    public static boolean help(List<String> args) {
        Message.formatHeader(20, "Statistics Help");
        for(CommandPair command : CommandManager.getCommands()) {
            Command cmd = command.getProperties();
            Message.send(ChatColor.GREEN + cmd.usage() + " " + ChatColor.GRAY + cmd.description());
        }
        return true;
    }
    
    @Command(
            alias = "pause",
            minArgs = 2,
            maxArgs = 3,
            permission = "stats.cmd.pause",
            allowConsole = true,
            usage = "/stats pause",
            description = "Temporarily pauses all data collection"
            )
    public static boolean pause(List<String> args) {
        if(Statistics.isPaused()) {
            Statistics.setPaused(false);
            Message.send("Data collection is unpaused");
        } else {
            Statistics.setPaused(true);
            Message.send("Data collection is paused");
        }
        return true;
    }
    
}
