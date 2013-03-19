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

package com.wolvencraft.yasp.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolvencraft.yasp.CommandManager;
import com.wolvencraft.yasp.StatsPlugin;
import com.wolvencraft.yasp.db.data.sync.Settings;

/**
 * Utility class used for sending different types of messages to players.
 * @author bitWolfy
 *
 */
public class Message {
	private static Logger logger = StatsPlugin.getInstance().getLogger();
	
	/**
	 * Send a message to the specified CommandSender.<br />
	 * Using CommandSender allows to easily send messages both to Player and ConsoleCommandSender.
	 * @param sender CommandSender to forward the message to
	 * @param message Message to be sent
	 */
	public static void send(CommandSender sender, String message) {
		if(sender == null) sender = Bukkit.getServer().getConsoleSender();
		if(message == null) return;
		message = Util.parseChatColors(message);
		sender.sendMessage(message);
	}
	
	/**
	 * Sends a message to the latest CommandSender.<br />
	 * Wraps around <i>send(CommandSender sender, String message)</i>, substituting the latest CommandSender for sender.
	 * Therefore, it is only safe to use this method in command classes.
	 * @param message Message to be sent
	 */
	public static void send(String message) {
		CommandSender sender = CommandManager.getSender();
		send(sender, message);
	}
	
	/**
	 * Builds and sends a message with an attached title.<br />
	 * Using CommandSender allows to easily send messages both to Player and ConsoleCommandSender.
	 * @param sender CommandSender to forward the message to
	 * @param titleColor Color of the title
	 * @param title Title to attach to the message
	 * @param message Message to be sent
	 */
	public static void sendFormatted(CommandSender sender, ChatColor titleColor, String title, String message) {
		if(message == null) return;
		message = titleColor + "[" + title + "] " + ChatColor.WHITE + message;
		send(sender, message);
	}
	
	/**
	 * Builds and sends a message with a green-colored title.
	 * @param sender CommandSender to forward the message to
	 * @param message Message to be sent
	 */
	public static void sendFormattedSuccess(CommandSender sender, String message) {
		sendFormatted(sender, ChatColor.DARK_GREEN, Settings.LocalConfiguration.LogPrefix.asString(), message);
	}
	
	/**
	 * Builds and sends a message with a green-colored title.<br />
	 * Wraps around <i>send(CommandSender sender, String message)</i>, substituting the latest CommandSender for sender.
	 * Therefore, it is only safe to use this method in command classes.
	 * @param message Message to be sent
	 */
	public static void sendFormattedSuccess(String message) {
		CommandSender sender = CommandManager.getSender();
		sendFormatted(sender, ChatColor.DARK_GREEN, Settings.LocalConfiguration.LogPrefix.asString(), message);
	}
	
	/**
	 * Builds and sends a message with a red-colored title.
	 * @param sender CommandSender to forward the message to
	 * @param message Message to be sent
	 */
	public static void sendFormattedError(CommandSender sender, String message) {
		sendFormatted(sender, ChatColor.DARK_RED, Settings.LocalConfiguration.LogPrefix.asString(), message);
	}
	
	/**
	 * Builds and sends a message with a red-colored title.<br />
	 * Wraps around <i>send(CommandSender sender, String message)</i>, substituting the latest CommandSender for sender.
	 * Therefore, it is only safe to use this method in command classes.
	 * @param message Message to be sent
	 */
	public static void sendFormattedError(String message) {
		CommandSender sender = CommandManager.getSender();
		sendFormatted(sender, ChatColor.DARK_RED, Settings.LocalConfiguration.LogPrefix.asString(), message);
	}
	
    /**
     * Broadcasts a message to all players on the server
     * @param message Message to be sent
     */
    public static void broadcast(String message) {
        for (Player p : Bukkit.getServer().getOnlinePlayers())
        	sendFormatted(p, ChatColor.DARK_GREEN, Settings.LocalConfiguration.LogPrefix.asString(), message);
        log(Util.parseChatColors(message));
    }
    
	/**
	 * Sends a message into the server log
	 * @param message Message to be sent
	 */
	public static void log(String message) {
		logger.info(message);
	}
	
	/**
	 * Sends a message into the server log
	 * @param level Severity level
	 * @param message Message to be sent
	 */
	public static void log(Level level, String message) {
		logger.log(level, message);
	}
    
    /**
     * Sends a message into the server log if debug is enabled in the configuration.<br />
     * Should not be used if there is more then one line to be sent to the console.
     * @param message Message to be sent
     */
    public static void debug(String message) {
        if (Settings.LocalConfiguration.Debug.asBoolean()) log(message);
    }
    
    /**
     * Sends a message into the server log if debug is enabled in the configuration.<br />
     * Should not be used if there is more then one line to be sent to the console.
	 * @param level Severity level
     * @param message Message to be sent
     */
    public static void debug(Level level, String message) {
        if (Settings.LocalConfiguration.Debug.asBoolean()) log(level, message);
    }
	
    /**
     * Formats the help string for the specified sub-command and arguments.<br />
     * The CommandSender will not be able to see the message if he lacks the permissions node specified.<br />
     * Borrows the CommandSender from the CommandManager; therefore, it is not safe to use this method outside of command classes.
     * @param command Sub-command name
     * @param arguments Command arguments
     * @param description Command description
     * @param node Permissions node needed to see the help message
     */
	public static void formatHelp(String command, String arguments, String description, String node) {
		CommandSender sender = CommandManager.getSender();
		if(!arguments.equalsIgnoreCase("")) arguments = " " + arguments;
		if(sender.hasPermission(node) || node.equals(""))
			sender.sendMessage(ChatColor.GOLD + "/stats " + command + ChatColor.GRAY + arguments + ChatColor.WHITE + " " + description);
		return;
	}
	
	/**
     * Formats the help string for the specified sub-command and arguments.<br />
     * Wraps around <i>formatHelp(String command, String arguments, String description, String node)</i> with an empty permissions node.<br />
     * Borrows the CommandSender from the CommandManager; therefore, it is not safe to use this method outside of command classes.
     * @param command Sub-command name
     * @param arguments Command arguments
     * @param description Command description
	 */
	public static void formatHelp(String command, String arguments, String description) {
		formatHelp(command, arguments, description, "");
		return;
	}
	
	/**
	 * Formats the specified string and shifts it according to the padding provided.<br />
     * Borrows the CommandSender from the CommandManager; therefore, it is not safe to use this method outside of command classes.
	 * @param padding Number of spaces to shift the string
	 * @param str String to format and shift
	 */
	public static void formatHeader(int padding, String str) {
		CommandSender sender = CommandManager.getSender();
		String spaces = "";
		for(int i = 0; i < padding; i++) { spaces = spaces + " "; }
		sender.sendMessage(spaces + "-=[ " + ChatColor.BLUE + str + ChatColor.WHITE + " ]=-");
	}
}
