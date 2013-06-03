package com.wolvencraft.yasp.cmd;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolvencraft.yasp.CommandManager;
import com.wolvencraft.yasp.Statistics;
import com.wolvencraft.yasp.CommandManager.Command;
import com.wolvencraft.yasp.db.Database;
import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.Query.QueryResult;
import com.wolvencraft.yasp.db.tables.Normal.PlayersTable;
import com.wolvencraft.yasp.settings.Constants.StatPerms;
import com.wolvencraft.yasp.util.Message;
import com.wolvencraft.yasp.util.cache.OnlineSessionCache;
import com.wolvencraft.yasp.util.tasks.DatabaseTask;
import com.wolvencraft.yasp.util.tasks.SignRefreshTask;

public class DatabaseCommands {
    
    @Command(
            alias = "sync",
            minArgs = 0,
            maxArgs = 0,
            permission = "stats.cmd.sync",
            allowConsole = true,
            usage = "/stats sync",
            description = "Forces the plugin to push data to the database"
            )
    public static boolean sync(List<String> args) {
        final CommandSender sender = CommandManager.getSender();
        Bukkit.getScheduler().runTaskAsynchronously(Statistics.getInstance(), new Runnable() {

            @Override
            public void run() {
                DatabaseTask.commit();
                
                List<QueryResult> results= Query.table(PlayersTable.TableName).column(PlayersTable.Name).condition(PlayersTable.Online, true).selectAll();
                for(QueryResult result : results) {
                    String playerName = result.asString(PlayersTable.Name);
                    if(Bukkit.getPlayerExact(playerName) == null)
                        Query.table(PlayersTable.TableName).value(PlayersTable.Online, false).condition(PlayersTable.Name, playerName).update();
                }
                
                Bukkit.getScheduler().runTask(Statistics.getInstance(), new Runnable() {
                    
                    @Override
                    public void run() {
                        SignRefreshTask.updateAll();
                    }
                    
                });
                
                Message.sendFormattedSuccess(sender, "Synchronization complete");
            }
            
        });
        return true;
    }
    
    @Command(
            alias = "patch",
            minArgs = 0,
            maxArgs = 1,
            permission = "stats.cmd.patch",
            allowConsole = true,
            usage = "/stats patch [id]",
            description = "Exectutes a database patch with the specified ID"
            )
    public static boolean patch(List<String> args) {
        final CommandSender sender = CommandManager.getSender();
        final String patchId = args.get(0);
        
        Message.sendFormattedSuccess("Attempting to patch the database (" + patchId + ")");
        Statistics.setPaused(true);
        Bukkit.getScheduler().runTaskAsynchronously(Statistics.getInstance(), new Runnable() {

            @Override
            public void run() {
                try { Database.executePatch(patchId); }
                catch (Exception ex) { Message.sendFormattedError(sender, "Patch failed!"); }
                finally {
                    for(Player player : Bukkit.getServer().getOnlinePlayers()) {
                        if(StatPerms.Statistics.has(player)) OnlineSessionCache.fetch(player);
                    }
                    Statistics.getServerStatistics().pushStaticData();
                    Message.sendFormattedSuccess(sender, "Patching finished.");
                    Statistics.setPaused(false);
                }
            }
            
        });
        
        return true;
    }
    
    @Command(
            alias = "repatch",
            minArgs = 0,
            maxArgs = 0,
            permission = "stats.cmd.repatch",
            allowConsole = true,
            usage = "/stats repatch",
            description = "Attempts to re-patch the database"
            )
    public static boolean repatch(List<String> args) {
        final CommandSender sender = CommandManager.getSender();
        Message.sendFormattedSuccess(sender, "Attempting to patch the database...");
        OnlineSessionCache.dumpSessions();
        Statistics.setPaused(true);
        Bukkit.getScheduler().runTaskAsynchronously(Statistics.getInstance(), new Runnable() {

            @Override
            public void run() {
                try { Database.patchDatabase(true); }
                catch (Exception ex) { Message.sendFormattedError(sender, "Patch failed!"); }
                finally {
                    for(Player player : Bukkit.getServer().getOnlinePlayers()) {
                        if(StatPerms.Statistics.has(player)) OnlineSessionCache.fetch(player);
                    }
                    Statistics.getServerStatistics().pushStaticData();
                    Message.sendFormattedSuccess(sender, "Patching finished.");
                    Statistics.setPaused(false);
                }
            }
            
        });
        return true;
    }
    
    @Command(
            alias = "reconnect",
            minArgs = 0,
            maxArgs = 0,
            permission = "stats.cmd.reconnect",
            allowConsole = true,
            usage = "/stats reconnect",
            description = "Attempts to reconnect to the database"
            )
    public static boolean reconnect(List<String> args) {
        try {
            Database.reconnect();
            Message.sendFormattedSuccess(CommandManager.getSender(), "Re-established the database connection");
            return true;
        } catch (Exception ex) {
            Message.sendFormattedError(CommandManager.getSender(), "An error occurred while reconnecting to the database");
            return false;
        }
    }
    
    @Command(
            alias = "dump",
            minArgs = 0,
            maxArgs = 0,
            permission = "stats.cmd.dump",
            allowConsole = true,
            usage = "/stats dump",
            description = "Dumps the locally stored data"
            )
    public static boolean dump(List<String> args) {
        OnlineSessionCache.dumpSessions();
        Message.sendFormattedSuccess(CommandManager.getSender(), "The local data has been dumped");
        return true;
    }

}
