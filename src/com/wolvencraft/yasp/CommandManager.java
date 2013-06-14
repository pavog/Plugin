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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import com.wolvencraft.yasp.cmd.DatabaseCommands;
import com.wolvencraft.yasp.cmd.PlayerCommands;
import com.wolvencraft.yasp.cmd.PluginCommands;
import com.wolvencraft.yasp.util.ExceptionHandler;
import com.wolvencraft.yasp.util.Message;

public class CommandManager {
    
    @Getter(AccessLevel.PUBLIC)
    private static CommandManager instance;
    
    /**
     * Registered command container classes
     * @author bitWolfy
     *
     */
    private enum CommandClass {
        
        Database (DatabaseCommands.class),
        Player   (PlayerCommands.class),
        Plugin   (PluginCommands.class)
        ;
        
        @Getter(AccessLevel.PRIVATE) private Class<?> command;
        
        CommandClass(Class<?> command) {
            this.command = command;
        }
        
    }
    
    @Getter(AccessLevel.PUBLIC) private static List<CommandPair> commands;
    @Getter(AccessLevel.PUBLIC) private static CommandSender sender = null;
    
    public CommandManager() {
        instance = this;
        
        Message.debug("Starting to register commands");
        commands = new ArrayList<CommandPair>();
        
        for(CommandClass command : CommandClass.values()) {
            load(command.getCommand());
        }
    }
    
    /**
     * Executes the command with the specified arguments
     * @param sender Command sender
     * @param args Command arguments
     * @return <b>true</b> if the command was executed successfully, <b>false</b> otherwise
     */
    public static boolean run(CommandSender sender, String... args) {
        CommandManager.sender = sender;
        if(args.length < 1) {
            return run(sender, "help");
        }
        List<String> arguments = new LinkedList<String>(Arrays.asList(args));
        CommandPair command = get(arguments.get(0));
        arguments.remove(0);
        
        // Command check
        if(command == null) {
            Message.sendFormattedError(sender, "Unknown command");
            CommandManager.sender = null;
            return false;
        }
        Command properties = command.getProperties();
        
        // Argument check
        if(arguments.size() < properties.minArgs()
                || (properties.maxArgs() != -1 && arguments.size() > properties.maxArgs())) {
            Message.sendFormattedError(sender, "Invalid argument count");
            CommandManager.sender = null;
            return false;
        }
        
        // Console check
        if(sender instanceof ConsoleCommandSender && !properties.allowConsole()) {
            Message.sendFormattedError(sender, "This command can only be run by a living player");
            CommandManager.sender = null;
            return false;
        }
        
        // Permission check
        if(!(sender instanceof ConsoleCommandSender)
                && !sender.isOp()
                && !properties.permission().equals("")
                && !sender.hasPermission(properties.permission())) {
            Message.sendFormattedError(sender, "You lack the permission to run this command");
            CommandManager.sender = null;
            return false;
        }
        
        // Attempting to execute a command
        boolean result;
        try { result = command.run(arguments); }
        catch(Throwable t) {
            ExceptionHandler.handle(t, sender, command);
            result = false;
        }
        CommandManager.sender = null;
        return result;
    }
    
    /**
     * Performs a lookup of a command method based on an alias
     * @param alias Alias to look for
     * @return Command method pair
     */
    private static CommandPair get(String alias) {
        for(CommandPair command : commands) {
            if(Arrays.asList(command.getProperties().alias()).contains(alias)) return command;
        }
        return null;
    }
    
    /**
     * Loads all methods with the {@link CommandManager.Command} annotation
     * @param commandClass Command class
     */
    private void load(Class<?> commandClass) {
        Message.debug("Scanning " + commandClass.getName() + " for command methods (" + commandClass.getMethods().length  + " total)");
        for(Method method : commandClass.getMethods()) {
            Command cmd = method.getAnnotation(Command.class);
            if(cmd != null) {
                Message.debug("Registering a command with alias: " + cmd.alias()[0]);
                commands.add(new CommandPair(commandClass, method, cmd));
            }
        }
        
    }
    
    /**
     * An annotation for commands
     * @author bitWolfy
     *
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Command {
        
        /**
         * Returns the alias associated with this command.
         * @return List of alias
         */
        public String[] alias();
        
        /**
         * Returns the minimum number of arguments this command accepts.
         * Requires {@link #maxArgs()}.<br />
         * Defaults to 0 (no arguments).
         * @return Minimum number of arguments
         */
        public int minArgs() default 0;
        
        /**
         * Returns the maximum number of arguments this command accepts.
         * Required {@link #minArgs()}.<br />
         * Defaults to -1 (any number of arguments)
         * @return Minimum number of arguments
         */
        public int maxArgs() default -1;
        
        /**
         * Returns the permission required to use this command.
         * Pass <code>"*"</code> for ops-only; empty string for no permission
         * @return Command permission
         */
        public String permission() default "";
        
        /**
         * Should the console sender be allowed to run this command
         * @return <b>true</b> if the console command sender should be allowed to run the command, <b>false</b> otherwise
         */
        public boolean allowConsole() default true;
        
        /**
         * Returns the usage of the command. Requires {@link #description()}.<br />
         * Example: <code>/command (requiredArgument) [optionalArgument]</code>
         * @return Command usage
         */
        public String usage() default "";
        
        /**
         * Returns the description string for the help page.
         * Requires {@link #usage()}.
         * @return Description string
         */
        public String description() default "";
        
    }
    
    /**
     * Storage unit for command properties
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter(AccessLevel.PUBLIC)
    public class CommandPair {
        
        Class<?> declaringClass;
        Method command;
        Command properties;
        
        public boolean run(List<String> args) throws Throwable {
            boolean result = false;
            result = (Boolean) command.invoke(declaringClass, args);
            return result;
        }
        
    }
    
}
