/* 
 *    Copyright 2009-2011 The MyBatis Team
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *    
 *    Changelog:
 *    
 *    Cut down on unused code and generally optimized for desired tasks.
 *    - bitWolfy
 *    
 *    Added the ability to change the delimiter so you can run scripts that 
 *    contain stored procedures.
 *    - ChaseHQ
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
	Dump (DumpCommand.class, null, "dump"),
	Pause(PauseCommand.class, null, "pause"),
	Sync (SyncCommand.class, null, "sync"),
	Reconnect (ReconnectCommand.class, null, "reconnect"),
	Repatch (RepatchCommand.class, null, "repatch"),
	Help (HelpCommand.class, null, "help");
	
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