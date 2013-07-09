/*
 * ModuleManager.java
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

package com.wolvencraft.yasp.managers;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import com.wolvencraft.yasp.db.data.DataStore;
import com.wolvencraft.yasp.db.data.blocks.BlocksDataStore;
import com.wolvencraft.yasp.db.data.deaths.DeathsDataStore;
import com.wolvencraft.yasp.db.data.distance.DistancesDataStore;
import com.wolvencraft.yasp.db.data.inventory.InventoryDataStore;
import com.wolvencraft.yasp.db.data.items.ItemsDataStore;
import com.wolvencraft.yasp.db.data.misc.MiscDataStore;
import com.wolvencraft.yasp.db.data.player.PlayerDataStore;
import com.wolvencraft.yasp.db.data.pve.PveDataStore;
import com.wolvencraft.yasp.db.data.pvp.PvpDataStore;
import com.wolvencraft.yasp.db.hooks.PluginHook;
import com.wolvencraft.yasp.db.hooks.admincmd.AdminCmdDataStore;
import com.wolvencraft.yasp.db.hooks.banhammer.BanHammerDataStore;
import com.wolvencraft.yasp.db.hooks.commandbook.CommandBookDataStore;
import com.wolvencraft.yasp.db.hooks.factions.FactionsDataStore;
import com.wolvencraft.yasp.db.hooks.jail.JailDataStore;
import com.wolvencraft.yasp.db.hooks.mcmmo.McMMODataStore;
import com.wolvencraft.yasp.db.hooks.mobarena.MobArenaDataStore;
import com.wolvencraft.yasp.db.hooks.pvparena.PvpArenaDataStore;
import com.wolvencraft.yasp.db.hooks.towny.TownyDataStore;
import com.wolvencraft.yasp.db.hooks.vanish.VanishDataStore;
import com.wolvencraft.yasp.db.hooks.vault.VaultDataStore;
import com.wolvencraft.yasp.db.hooks.votifier.VotifierDataStore;
import com.wolvencraft.yasp.db.hooks.worldguard.WorldGuardDataStore;
import com.wolvencraft.yasp.session.OnlineSession;
import com.wolvencraft.yasp.util.ExceptionHandler;

@SuppressWarnings("rawtypes")
public class ModuleManager {
    
    private static List<Class<? extends DataStore>> modules;
    
    public ModuleManager() {
        modules = new ArrayList<Class<? extends DataStore>>();
        for(Module module : Module.values()) modules.add(module.getModule());
    }
    
    public static List<DataStore> getModules(OnlineSession session) {
        List<DataStore> dataStores = new ArrayList<DataStore>();
        for(Class<? extends DataStore> store : modules) {
            DataStore storeObj = null;
            try { storeObj = store.getDeclaredConstructor(OnlineSession.class).newInstance(session); }
            catch (Throwable t) { ExceptionHandler.handle(t); }
            if(storeObj == null) continue;
            if(!storeObj.getLock().isEnabled()) continue;
            
            if(storeObj.getLock().isHook()) {
                PluginHook hook = HookManager.getHook(storeObj.getType());
                if(hook == null || !hook.isEnabled()) continue;
            }
            dataStores.add(storeObj);
        }
        return dataStores;
    }
    
    @Getter(AccessLevel.PUBLIC)
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    private enum Module {
        
        Block           (BlocksDataStore.class),
        Death           (DeathsDataStore.class),
        Distance        (DistancesDataStore.class),
        Inventory       (InventoryDataStore.class),
        Item            (ItemsDataStore.class),
        Misc            (MiscDataStore.class),
        Player          (PlayerDataStore.class),
        PVE             (PveDataStore.class),
        PVP             (PvpDataStore.class),
        
        AdminCmd        (AdminCmdDataStore.class),
        BanHammer       (BanHammerDataStore.class),
        CommandBook     (CommandBookDataStore.class),
        Factions        (FactionsDataStore.class),
        Jail            (JailDataStore.class),
        McMMO           (McMMODataStore.class),
        MobArena        (MobArenaDataStore.class),
        PvpArena        (PvpArenaDataStore.class),
        Towny           (TownyDataStore.class),
        Vanish          (VanishDataStore.class),
        Vault           (VaultDataStore.class),
        Votifier        (VotifierDataStore.class),
        WorldGuard      (WorldGuardDataStore.class),
        ;
        
        Class<? extends DataStore> module;
        
    }
    
}
