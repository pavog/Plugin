/*
 * Copyright (C) 2014 Mario
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.wolvencraft.yasp.util.tasks;

import org.bukkit.Bukkit;

import com.wolvencraft.yasp.db.data.DataStore;
import com.wolvencraft.yasp.db.data.hooks.vault;
import com.wolvencraft.yasp.settings.Constants.StatPerms;
import com.wolvencraft.yasp.settings.Module;
import com.wolvencraft.yasp.session.OnlineSession;
import com.wolvencraft.yasp.util.cache.OnlineSessionCache;

/**
 * Saves all Hook data from plugins which don't provide any events where I can listen on periodically 
 * This task runs every 10 minuts
 * @author Mario
 */
public class HookRefreshTask implements Runnable {
    
     /**
     * <b>Default constructor</b>
     */
    public HookRefreshTask() { }
    
    @Override
    public void run() {
        for(OnlineSession session : OnlineSessionCache.getSessions()) {
            if(session.isOnline()){
                if(Module.Vault.isActive() && StatPerms.HookVault.has(Bukkit.getPlayer(session.getName()))){
                    ((VaultData) session.getDataStore(DataStore.DataStoreType.Hook_Vault)).VaultPlayerEntry(session.getName(), session.getId());
                    ((VaultData) session.getDataStore(DataStore.DataStoreType.Hook_Vault)).DetailedVaultEntry(session.getName(), session.getId()); 
                }         
            }
        }
    }
    
}
