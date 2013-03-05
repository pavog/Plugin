package com.wolvencraft.yasp;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import com.wolvencraft.yasp.cmd.*;
import com.wolvencraft.yasp.util.Message;

public enum CommandManager {
	Dump (DumpCommand.class, "dump"),
	Sync (SyncCommand.class, "sync"),
	Reconnect (ReconnectCommand.class, "reconnect"),
	Repatch (RepatchCommand.class, "repatch"),
	HELP (HelpCommand.class,  "help");
	
	CommandManager(Class<?> clazz, String... args) {
		try {
			this.clazz = (BaseCommand) clazz.newInstance();
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
	private List<String> alias;
	
	public boolean isCommand(String arg) 	{ return alias.contains(arg); }
	public void getHelp() 					{ clazz.getHelp(); }
	
	public boolean run(String[] args) {
		if(sender instanceof ConsoleCommandSender || sender.isOp()) return clazz.run(args);
		Message.sendFormattedError(sender, "You are not allowed to perform this task");
		return false;
	}

	public boolean run(String arg) {
		String[] args = {"", arg};
		return run(args);
	}
	
	public boolean run() {
		String[] args = {"", ""};
		return run(args);
	}
	
	public static CommandSender getSender() 	{ return sender; }
	public static void setSender(CommandSender sender) { CommandManager.sender = sender; }
	public static void resetSender() { sender = null; }
}