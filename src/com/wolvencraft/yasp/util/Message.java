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

public class Message {
	private static Logger logger = StatsPlugin.getInstance().getLogger();
	
	public static void send(CommandSender sender, String message) {
		if(sender == null) sender = Bukkit.getServer().getConsoleSender();
		if(message == null) return;
		message = Util.parseChatColors(message);
		sender.sendMessage(message);
	}
	
	public static void sendFormatted(CommandSender sender, String title, String message) {
		if(message == null) return;
		message = "[" + title + "] " + ChatColor.WHITE + message;
		send(sender, message);
	}
	
	public static void sendFormattedSuccess(CommandSender sender, String message) {
		sendFormatted(sender, ChatColor.DARK_GREEN + Settings.LocalConfiguration.LogPrefix.asString(), message);
	}

	public static void sendFormattedError(CommandSender sender, String message) {
		sendFormatted(sender, ChatColor.DARK_RED + Settings.LocalConfiguration.LogPrefix.asString(), message);
	}
	
    /**
     * Broadcasts a message to all players on the server
     * @param message Message to be sent
     */
    public static void broadcast(String message) {
        for (Player p : Bukkit.getServer().getOnlinePlayers())
        	sendFormatted(p, ChatColor.DARK_GREEN + Settings.LocalConfiguration.LogPrefix.asString(), message);
        log(Util.parseChatColors(message));
    }
    
    /**
     * Sends a message into the server log if debug is enabled
     * @param message Message to be sent
     */
    public static void debug(String message) {
        if (Settings.LocalConfiguration.Debug.asBoolean()) log(message);
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
	
	public static void formatHelp(String command, String arguments, String description, String node) {
		CommandSender sender = CommandManager.getSender();
		if(!arguments.equalsIgnoreCase("")) arguments = " " + arguments;
		if(sender.hasPermission(node) || node.equals(""))
			sender.sendMessage(ChatColor.GOLD + "/stats " + command + ChatColor.GRAY + arguments + ChatColor.WHITE + " " + description);
		return;
	}
	
	public static void formatHelp(String command, String arguments, String description) {
		formatHelp(command, arguments, description, "");
		return;
	}
	
	public static void formatHeader(int padding, String name) {
		CommandSender sender = CommandManager.getSender();
		String spaces = "";
		for(int i = 0; i < padding; i++) { spaces = spaces + " "; }
		sender.sendMessage(spaces + "-=[ " + ChatColor.BLUE + name + ChatColor.WHITE + " ]=-");
	}
}
