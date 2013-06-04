/*
 * ExceptionHandler.java
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

package com.wolvencraft.yasp.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;

import com.wolvencraft.yasp.CommandManager.CommandPair;
import com.wolvencraft.yasp.Statistics;

public class ExceptionHandler {
    
    public static void handle(Throwable t) {
        PluginDescriptionFile description = Statistics.getInstance().getDescription();
        Message.log(
                "+-------------- [ Statistics ] --------------+",
                "| The plugin 'Statistics' has caused an error.",
                "| Please, create a new ticket with this error at",
                "| " + description.getWebsite(),
                "| ",
                "| Bukkit   : " + Bukkit.getVersion(),
                "| Plugin   : " + description.getFullName(),
                "| Version  : " + description.getVersion(),
                "| Error    : " + t.getLocalizedMessage(),
                "+--------------------------------------------+",
                "| The stack trace of the error follows: ",
                "| "
                );
        for(StackTraceElement element : t.getStackTrace()) {
            Message.log("| " + element.toString());
        }
        Message.log("+--------------------------------------------+");
    }
    
    public static void handle(Throwable t, CommandSender sender, CommandPair command) {
        Message.send(sender, ChatColor.RED + "An internal error occurred while executing the command");
        
        PluginDescriptionFile description = Statistics.getInstance().getDescription();
        String alias = "";
        for(String str : command.getProperties().alias()) {
            alias += str + " ";
        }
        
        Message.log(
                "+-------------- [ Statistics ] --------------+",
                "| The plugin 'Statistics' has caused an error.",
                "| Please, create a new ticket with this error at",
                "| " + description.getWebsite(),
                "| ",
                "| Bukkit   : " + Bukkit.getVersion(),
                "| Plugin   : " + description.getFullName(),
                "| Version  : " + description.getVersion(),
                "| Error    : " + t.getLocalizedMessage(),
                "+--------------------------------------------+",
                "| Command  : " + command.getCommand().getName(),
                "| Alias    : " + alias,
                "| Volatile : " + !command.getProperties().unstable(),
                "| Bukkit-c : " + Statistics.isCraftBukkitCompatible(),
                "+--------------------------------------------+",
                "| The stack trace of the error follows: ",
                "| "
                );
        for(StackTraceElement element : t.getStackTrace()) {
            Message.log("| " + element.toString());
        }
        Message.log("+--------------------------------------------+");
    }
    
}