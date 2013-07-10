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

package com.mctrakr.db.data;

import lombok.AccessLevel;
import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import com.mctrakr.Statistics;
import com.mctrakr.db.ConfigTables.ModulesTable;
import com.mctrakr.db.Query;
import com.mctrakr.db.Query.QueryResult;
import com.mctrakr.db.data.DataStore.DataStoreType;
import com.mctrakr.settings.RemoteConfiguration;

@Getter(AccessLevel.PUBLIC)
public class ConfigLock implements Runnable {
    
    @Getter(AccessLevel.NONE)
    private BukkitTask process;
    
    private DataStoreType type;
    private boolean enabled;
    private boolean hook;
    private int loadOrder;
    private int version;
    
    public ConfigLock(DataStoreType type) {
        this.type = type;
        this.hook = false;
        init();
    }
    
    public ConfigLock(DataStoreType type, boolean hook) {
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
            
            Query.table(ModulesTable.TableName)
                .value(ModulesTable.Name, type.getAlias())
                .value(ModulesTable.IsEnabled, enabled)
                .value(ModulesTable.LoadOrder, loadOrder)
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
    
}
