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

package com.mctrakr.managers;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import com.mctrakr.modules.DataStore;
import com.mctrakr.modules.hooks.PluginHook;
import com.mctrakr.modules.hooks.admincmd.AdminCmdDataStore;
import com.mctrakr.modules.hooks.banhammer.BanHammerDataStore;
import com.mctrakr.modules.hooks.commandbook.CommandBookDataStore;
import com.mctrakr.modules.hooks.factions.FactionsDataStore;
import com.mctrakr.modules.hooks.jail.JailDataStore;
import com.mctrakr.modules.hooks.mcmmo.McMMODataStore;
import com.mctrakr.modules.hooks.mobarena.MobArenaDataStore;
import com.mctrakr.modules.hooks.pvparena.PvpArenaDataStore;
import com.mctrakr.modules.hooks.towny.TownyDataStore;
import com.mctrakr.modules.hooks.vanish.VanishDataStore;
import com.mctrakr.modules.hooks.vault.VaultDataStore;
import com.mctrakr.modules.hooks.votifier.VotifierDataStore;
import com.mctrakr.modules.hooks.worldguard.WorldGuardDataStore;
import com.mctrakr.modules.stats.blocks.BlocksDataStore;
import com.mctrakr.modules.stats.deaths.DeathsDataStore;
import com.mctrakr.modules.stats.distance.DistancesDataStore;
import com.mctrakr.modules.stats.inventory.InventoryDataStore;
import com.mctrakr.modules.stats.items.ItemsDataStore;
import com.mctrakr.modules.stats.misc.MiscDataStore;
import com.mctrakr.modules.stats.player.PlayerDataStore;
import com.mctrakr.modules.stats.pve.PveDataStore;
import com.mctrakr.modules.stats.pvp.PvpDataStore;
import com.mctrakr.session.OnlineSession;
import com.mctrakr.settings.ConfigLock.ModuleKind;
import com.mctrakr.util.ExceptionHandler;

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
            
            if(storeObj.getLock().getKind() == ModuleKind.Hook) {
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
