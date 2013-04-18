/*
 * CommandManager.java
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

package com.wolvencraft.yasp;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import com.wolvencraft.yasp.cmd.*;
import com.wolvencraft.yasp.util.Message;

/**
 * A sturdy implementation of a CommandManager focused on handling sub-commands.<br />
 * Additionally, stores the CommandSender for the duration of the command execution.
 * @author bitWolfy
 *
 */
public enum CommandManager {
    Book(BookCommand.class, "stats.cmd.book", "book"),
    Help(HelpCommand.class, "stats.cmd.help", "help"),
    Scoreboard(ScoreboardCommand.class, "stats.cmd.scoreboard", "scoreboard"),
    
    Dump(DumpCommand.class, "stats.cmd.debug", "dump"),
    Fetch(FetchCommand.class, "stats.cmd.debug", "fetch"),
    Pause(PauseCommand.class, "stats.cmd.debug", "pause"),
    Sync(SyncCommand.class, "stats.cmd.debug", "sync"),
    Patch(PatchCommand.class, "stats.cmd.debug", "patch"),
    Reconnect(ReconnectCommand.class, "stats.cmd.debug", "reconnect"),
    Repatch(RepatchCommand.class, "stats.cmd.debug", "repatch");
    
    private static CommandSender sender = null;
    
    private BaseCommand command;
    private String permission;
    private List<String> alias;
    
    /**
     * <b>Constructor</b><br />
     * @param command Command base class. Must implement <code>BaseCommand</code>.
     * @param permission Permission node, or <b>null</b> for ops-only permission
     * @param args An array of arguments that the command can handle
     */
    CommandManager(Class<?> command, String permission, String... args) {
        try {
            this.command = (BaseCommand) command.newInstance();
            this.permission = permission;
            alias = new ArrayList<String>();
            for (String arg : args) {
                alias.add(arg);
            }
        }
        catch (InstantiationException e)     { Message.log(Level.SEVERE, "Error while instantiating a command! (InstantiationException)"); return; }
        catch (IllegalAccessException e)     { Message.log(Level.SEVERE, "Error while instantiating a command! (IllegalAccessException)"); return; }
        catch (Exception e)             { Message.log(Level.SEVERE, "Error while instantiating a command! (Exception)"); return; }
    }
    
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
        command.getHelp();
    }
    
    /**
     * Performs permissions checks and runs the command.
     * @param args Arguments to be passed on to the command
     * @return Command result
     */
    public boolean run(String[] args) {
        if(permission == null) {
            if(sender instanceof ConsoleCommandSender || sender.isOp()) {
                return command.run(args);
            }
            
            Message.sendFormattedError(sender, "You are not allowed to perform this task");
        } else {
            if(sender instanceof ConsoleCommandSender || sender.hasPermission(permission)) {
                return command.run(args);
            }
            
            Message.sendFormattedError(sender, "You are not allowed to perform this task");
        }
        return false;
    }
    
    /**
     * Wraps around <code>run(String[] args)</code> to accommodate for commands with a single argument.<br />
     * Performs permissions checks and runs the command.
     * @param arg Argument to be passed on to the command
     * @return Command result
     */
    public boolean run(String arg) {
        String[] args = {"", arg};
        return run(args);
    }
    
    /**
     * Wraps around <code>run(String[] args)</code> to accommodate for commands without arguments.<br />
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
     * Resets the CommandSender to null to prevent memory leaks.<br />
     * This method is to be run at the end of <code>onCommand()</code>
     */
    public static void resetSender() {
        sender = null;
    }
}
