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

package com.wolvencraft.yasp;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import com.wolvencraft.yasp.cmd.*;
import com.wolvencraft.yasp.util.Message;

/**
 * Manages commands and command senders in the plugin
 * @author bitWolfy
 *
 */
public enum CommandManager {
	Book (BookCommand.class, "stats.cmd.book", "book"),
	Help (HelpCommand.class, "stats.cmd.help", "help"),
	Scoreboard(ScoreboardCommand.class, "stats.cmd.scoreboard", "scoreboard"),
	
	Dump (DumpCommand.class, null, "dump"),
	Pause(PauseCommand.class, null, "pause"),
	Sync (SyncCommand.class, null, "sync"),
	Reconnect (ReconnectCommand.class, null, "reconnect"),
	Repatch (RepatchCommand.class, null, "repatch");
	
	CommandManager(Class<?> clazz, String permission, String... args) {
		try {
			this.clazz = (BaseCommand) clazz.newInstance();
			this.permission = permission;
			alias = new ArrayList<String>();
			for(String arg : args) {
				alias.add(arg);
			}
		}
		catch (InstantiationException e) 	{ Message.log(Level.SEVERE, "Error while instantiating a command! (InstantiationException)"); return; }
		catch (IllegalAccessException e) 	{ Message.log(Level.SEVERE, "Error while instantiating a command! (IllegalAccessException)"); return; }
		catch (Exception e) 				{ Message.log(Level.SEVERE, "Error while instantiating a command! (Exception)"); return; }
	}
	
	private static CommandSender sender = null;
	
	private BaseCommand clazz;
	private String permission;
	private List<String> alias;
	
	/**
	 * Checks if the command contains the specified alias
	 * @param arg Alias to search for
	 * @return <b>true</b> if the command contains the alias, <b>false</b> otherwise.
	 */
	public boolean isCommand(String arg) {
		return alias.contains(arg);
	}
	
	/**
	 * Returns the help message for the command
	 */
	public void getHelp() {
		clazz.getHelp();
	}
	
	/**
	 * Performs permissions checks and runs the command.
	 * @param args Arguments to be passed on to the command
	 * @return Command result
	 */
	public boolean run(String[] args) {
		if(permission == null) {
			if(sender instanceof ConsoleCommandSender || sender.isOp()) return clazz.run(args);
			Message.sendFormattedError(sender, "You are not allowed to perform this task");
		} else {
			if(sender instanceof ConsoleCommandSender || sender.hasPermission(permission)) return clazz.run(args);
			Message.sendFormattedError(sender, "You are not allowed to perform this task");
		}
		return false;
	}
	
	/**
	 * Wraps around <i>run(String[] args)<i> to accommodate for commands with a single argument.<br />
	 * Performs permissions checks and runs the command.
	 * @param arg Argument to be passed on to the command
	 * @return Command result
	 */
	public boolean run(String arg) {
		String[] args = {"", arg};
		return run(args);
	}
	
	/**
	 * Wraps around <i>run(String[] args)<i> to accommodate for commands without arguments.<br />
	 * Performs permissions checks and runs the command.
	 * @return Command result
	 */
	public boolean run() {
		String[] args = {"", ""};
		return run(args);
	}
	
	/**
	 * Returns the CommandSender for the current command.<br />
	 * Unsafe; should only be used inside command classes.
	 * @return CommandSender
	 */
	public static CommandSender getSender() {
		return sender;
	}
	
	/**
	 * Sets the CommandSender to the one specified.
	 * @param sender CommandSender to be set as the current one
	 */
	public static void setSender(CommandSender sender) { 
		CommandManager.sender = sender;
	}
	
	/**
	 * Resets the CommandSender to null to prevent memory leaks
	 */
	public static void resetSender() {
		sender = null;
	}
}