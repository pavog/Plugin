/*
 * ConfigLock.java
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

package com.mctrakr.settings;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import com.mctrakr.Statistics;
import com.mctrakr.db.ConfigTables.ModulesTable;
import com.mctrakr.db.Query;
import com.mctrakr.db.Query.QueryResult;

@Getter(AccessLevel.PUBLIC)
public class ConfigLock implements Runnable {
    
    @Getter(AccessLevel.NONE)
    private BukkitTask process;
    
    private ModuleType type;
    private boolean enabled;
    private boolean hook;
    private int loadOrder;
    private int version;
    
    public ConfigLock(ModuleType type) {
        this.type = type;
        this.hook = false;
        init();
    }
    
    public ConfigLock(ModuleType type, boolean hook) {
        this.type = type;
        this.hook = hook;
        init();
    }
    
    /**
     * Initializes the scheduled task
     */
    private void init() {
        run();
        
        if(process == null)
        process = Bukkit.getScheduler().runTaskTimerAsynchronously(
            Statistics.getInstance(),
            this,
            0L,
            RemoteConfiguration.Ping.asLong()
        );
    }
    
    @Override
    public void run() {
        QueryResult result = Query.table(ModulesTable.TableName)
            .column(ModulesTable.IsEnabled, ModulesTable.LoadOrder, ModulesTable.Version)
            .condition(ModulesTable.Name, type.getAlias())
            .select();
        
        if(result == null) {
            enabled = true;
            loadOrder = 0;
            version = 0;
            
            String moduleType = "module";
            if(type instanceof PrimaryType) moduleType = "module";
            else if(type instanceof HookType) moduleType = "hook";
            else moduleType = "property";
            
            Query.table(ModulesTable.TableName)
                .value(ModulesTable.Name, type.getAlias())
                .value(ModulesTable.IsEnabled, enabled)
                .value(ModulesTable.LoadOrder, loadOrder)
                .value(ModulesTable.Type, moduleType)
                .value(ModulesTable.Version, version)
                .insert();
        } else {
            enabled = result.asBoolean(ModulesTable.IsEnabled);
            loadOrder = result.asInt(ModulesTable.LoadOrder);
            version = result.asInt(ModulesTable.Version);
        }
    }
    
    public void setVersion(int version) {
        Query.table(ModulesTable.TableName)
            .value(ModulesTable.Version, version)
            .condition(ModulesTable.Name, type.getAlias())
            .update();
    }
    
    @Override
    public void finalize() {
        if(process != null) process.cancel();
    }
    
    /**
     * Generic data store type.
     * @author bitWolfy
     *
     */
    public static interface ModuleType {
        
        /**
         * Returns the type alias
         * @return Data store alias
         */
        public String getAlias();
        
    }
    
    /**
     * Represents different types of built-in modules.
     * Exists for convenience purposes only
     * @author bitWolfy
     *
     */
    @Getter(AccessLevel.PUBLIC)
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    public enum PrimaryType implements ModuleType {
        
        Blocks          ("blocks"),
        Deaths          ("deaths"),
        Distance        ("distance"),
        Inventory       ("inventory"),
        Items           ("items"),
        Misc            ("misc"),
        Player          ("player"),
        PVE             ("pve"),
        PVP             ("pvp"),
        ;
        
        private String alias;
    }
    
    /**
     * Represents different types of hook modules.
     * Exists for convenience purposes only
     * @author bitWolfy
     *
     */
    @Getter(AccessLevel.PUBLIC)
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    public enum HookType implements ModuleType {

        AdminCmd        ("admincmd"),
        BanHammer       ("banhammer"),
        CommandBook     ("commandbook"),
        Factions        ("factions"),
        Jail            ("jail"),
        McMMO           ("mcmmo"),
        MobArena        ("mobarena"),
        PvpArena        ("pvparena"),
        Towny           ("towny"),
        Vanish          ("vanish"),
        Vault           ("vault"),
        Votifier        ("votifier"),
        WorldGuard      ("worldguard"),
        ;
        
        private String alias;
    }
    
    /**
     * Represents different types of hook modules.
     * Exists for convenience purposes only
     * @author bitWolfy
     *
     */
    @Getter(AccessLevel.PUBLIC)
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    public enum PropertyType implements ModuleType {
        
        Scoreboards     ("scoreboards"),
        Signs           ("signs"),
        ;
        
        private String alias;
    }
    
}

