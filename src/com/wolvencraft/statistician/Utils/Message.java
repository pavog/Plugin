package com.wolvencraft.statistician.Utils;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolvencraft.statistician.StatisticianPlugin;

public class Message {
	private static Logger logger = StatisticianPlugin.getInstance().getLogger();
	
	public static void send(CommandSender sender, String message) {
		if(sender == null) sender = Bukkit.getServer().getConsoleSender();
		if(message == null) message = "";
		message = Util.parseChatColors(message);
		sender.sendMessage(message);
	}
	
	public static void sendFormatted(CommandSender sender, String title, String message) {
		message = title + " " + ChatColor.WHITE + message;
		send(sender, message);
	}
	
	public static void sendFormattedSuccess(CommandSender sender, String message) {
		sendFormatted(sender, ChatColor.DARK_GREEN + StatisticianPlugin.getSettings().LOG_PREFIX, message);
	}

	public static void sendFormattedError(CommandSender sender, String message) {
		sendFormatted(sender, ChatColor.DARK_RED + StatisticianPlugin.getSettings().LOG_PREFIX, message);
	}
	
    /**
     * Broadcasts a message to all players on the server
     * @param message Message to be sent
     */
    public static void broadcast(String message) {
        for (Player p : Bukkit.getServer().getOnlinePlayers())
        	sendFormatted(p, ChatColor.DARK_GREEN + StatisticianPlugin.getSettings().LOG_PREFIX, message);
        log(Util.parseChatColors(message));
    }
    
    /**
     * Sends a message into the server log if debug is enabled
     * @param message Message to be sent
     */
    public static void debug(String message) {
        if (StatisticianPlugin.getSettings().DEBUG) log(message);
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
}
