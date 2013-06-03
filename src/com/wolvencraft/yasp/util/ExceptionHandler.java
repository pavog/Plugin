package com.wolvencraft.yasp.util;

import me.zford.jobs.util.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;

import com.wolvencraft.yasp.Statistics;
import com.wolvencraft.yasp.CommandManager.Command;

public class ExceptionHandler {
    
    public static void handle(Throwable t) {
        PluginDescriptionFile description = Statistics.getInstance().getDescription();
        Message.log(
                "+------------ [ Statistics ] ------------+",
                "| The plugin \"Statistics\" has caused an error.",
                "| Please, create a new ticket with this error at",
                "| " + description.getWebsite(),
                "| ",
                "| Bukkit  : " + Bukkit.getVersion(),
                "| Plugin  : " + description.getFullName(),
                "| Version : " + description.getVersion(),
                "| Error   : " + t.getLocalizedMessage(),
                "+----------------------------------------+",
                "| The stack trace of the error follows: ",
                ""
                );
        for(StackTraceElement element : t.getStackTrace()) {
            Message.log("| " + element.toString());
        }
        Message.log("+----------------------------------------+");
    }
    
    public static void handle(Throwable t, CommandSender sender, Command command) {
        Message.send(sender, ChatColor.RED + "An internal error occurred while executing the command");
        
        PluginDescriptionFile description = Statistics.getInstance().getDescription();
        String alias = "";
        for(String str : command.alias()) {
            alias += str + " ";
        }
        
        Message.log(
                "+------------ [ Statistics ] ------------+",
                "| The plugin \"Statistics\" has caused an error.",
                "| Please, create a new ticket with this error at",
                "| " + description.getWebsite(),
                "| ",
                "| Bukkit  : " + Bukkit.getVersion(),
                "| Plugin  : " + description.getFullName(),
                "| Version : " + description.getVersion(),
                "| Error   : " + t.getLocalizedMessage(),
                "+----------------------------------------+",
                "| Command alias     : " + alias,
                "| Unstable          : " + command.unstable(),
                "| Bukkit compatible : " + Statistics.isCraftBukkitCompatible(),
                "+----------------------------------------+",
                "| The stack trace of the error follows: ",
                ""
                );
        for(StackTraceElement element : t.getStackTrace()) {
            Message.log("| " + element.toString());
        }
        Message.log("+----------------------------------------+");
    }
    
}
