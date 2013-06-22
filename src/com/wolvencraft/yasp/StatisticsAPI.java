/*
 * StatisticsAPI.java
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.wolvencraft.yasp.db.Database;
import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.ScriptRunner;
import com.wolvencraft.yasp.db.tables.Miscellaneous.SettingsTable;
import com.wolvencraft.yasp.events.plugin.DatabasePatchEvent;
import com.wolvencraft.yasp.exceptions.DatabaseConnectionException;
import com.wolvencraft.yasp.exceptions.RuntimeSQLException;
import com.wolvencraft.yasp.listeners.handlers.HandlerManager;
import com.wolvencraft.yasp.session.OfflineSession;
import com.wolvencraft.yasp.session.OnlineSession;
import com.wolvencraft.yasp.settings.Constants.StatPerms;
import com.wolvencraft.yasp.util.Message;
import com.wolvencraft.yasp.util.VariableManager.ServerVariable;
import com.wolvencraft.yasp.util.cache.OfflineSessionCache;
import com.wolvencraft.yasp.util.cache.OnlineSessionCache;

/**
 * Simple API for servers statistics
 * @author bitWolfy
 *
 */
public class StatisticsAPI {
    
    /**
     * Returns the online player session
     * @param player Player object
     * @return Player session
     */
    public static OnlineSession getSession(Player player) {
        if(!isTracked(player)) return null;
        return OnlineSessionCache.fetch(player);
    }
    
    /**
     * Returns the OfflineSession for the player with the specified username.<br />
     * The player might not be online, or not exist at all.
     * @param username Player's username
     * @return DataSession with player's totals
     */
    public static OfflineSession getSession(String username) {
        return OfflineSessionCache.fetch(username);
    }
    
    /**
     * Returns the OfflineSession for the player with the specified username.<br />
     * The player might not be online, or not exist at all.
     * @param username Player's username
     * @param cached <b>true</b> if you want the plugin to cache this session
     * @return DataSession with player's totals
     */
    public static OfflineSession getSession(String username, boolean cached) {
        if(cached) return getSession(username);
        else return new OfflineSession(username);
    }
    
    /**
     * Checks if the player is tracked by the plugin
     * @param player Player object
     * @return <b>true</b> if the player is tracked, <b>false</b> otherwise
     */
    public static boolean isTracked(Player player) {
        return HandlerManager.playerLookup(player, StatPerms.Statistics);
    }
    
    /**
     * Returns the value of the specified variable
     * @param type Variable type
     * @return Variable value
     */
    public static Object getValue(ServerVariable type) {
        return Statistics.getServerTotals().getValues().get(type);
    }
    
    /**
     * Executes an external patch
     * @param plugin Plugin instance
     * @param path Path to the patch file (relative to your plugin's data folder), without the extension
     * @return <b>true</b> if the patch was executed, <b>false</b> if the patching was cancelled
     * @throws DatabaseConnectionException If an error occurred while executing a database patch
     * @throws FileNotFoundException If the patch file could not be found
     */
    public static boolean executeExternalPatch(Plugin plugin, String path) throws DatabaseConnectionException, FileNotFoundException {
        DatabasePatchEvent event = new DatabasePatchEvent(path);
        Bukkit.getServer().getPluginManager().callEvent(event);
        
        if(event.isCancelled()) return false;
        
        InputStream is = new FileInputStream(plugin.getDataFolder() + "/" + path + ".sql");
        Message.log(Level.FINE, "Executing external database patch: " + path + ".sql");
        
        ScriptRunner scriptRunner = new ScriptRunner(Database.getConnection());
        try {scriptRunner.runScript(new InputStreamReader(is)); }
        catch (RuntimeSQLException e) { throw new DatabaseConnectionException("An error occured while executing database patch: " + path + ".sql", e); }
        finally {
            if(!Query.table(SettingsTable.TableName).condition("key", "patched").exists()) {
                Query.table(SettingsTable.TableName).value("key", "patched").value("value", 1).insert();
            }
            Query.table(SettingsTable.TableName).value("value", 1).condition("key", "patched").update();
        }
        return true;
    }
}
