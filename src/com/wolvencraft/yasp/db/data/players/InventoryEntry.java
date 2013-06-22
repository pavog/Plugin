/*
 * InventoryEntry.java
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

package com.wolvencraft.yasp.db.data.players;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.data.NormalData;
import com.wolvencraft.yasp.db.tables.Normal.PlayerInv;
import com.wolvencraft.yasp.settings.Constants.StatPerms;
import com.wolvencraft.yasp.util.serializable.EffectsSerializable;
import com.wolvencraft.yasp.util.serializable.InventorySerializable;

/**
 * Represents player inventory and potion effects
 * @author bitWolfy
 *
 */
public class InventoryEntry extends NormalData {
    
    private final String playerName;
    
    /**
     * <b>Default constructor</b><br />
     * Creates a new InventoryData object based on arguments provided
     * @param playerId Player ID
     */
    public InventoryEntry(int playerId, Player player) {
        this.playerName = player.getName();
        fetchData(playerId);
    }
    
    @Override
    public void fetchData(int playerId) {
        if(Query.table(PlayerInv.TableName)
                .column(PlayerInv.PlayerId)
                .condition(PlayerInv.PlayerId, playerId)
                .exists()) return;
        Query.table(PlayerInv.TableName)
            .value(PlayerInv.PlayerId, playerId)
            .insert();
    }
    
    @Override
    @Deprecated
    public void clearData(int playerId) { }
    
    @Override
    public boolean pushData(int playerId) {
        Player player = Bukkit.getPlayerExact(playerName);
        if(player == null) return false;
        if(!StatPerms.PlayerInventory.has(player)) return false;
        
        PlayerInventory inv = player.getInventory();
        List<ItemStack> invRow = new ArrayList<ItemStack>();
        
        for(int i = 9; i < 18; i++) { invRow.add(inv.getItem(i)); }
        String rowOne = InventorySerializable.serialize(invRow);
        invRow.clear();

        for(int i = 18; i < 27; i++) { invRow.add(inv.getItem(i)); }
        String rowTwo = InventorySerializable.serialize(invRow);
        invRow.clear();
        
        for(int i = 27; i < 36; i++) { invRow.add(inv.getItem(i)); }
        String rowThree = InventorySerializable.serialize(invRow);
        invRow.clear();

        for(int i = 0; i < 9; i++) { invRow.add(inv.getItem(i)); }
        String hotbar = InventorySerializable.serialize(invRow);
        invRow.clear();
        
        invRow.add(inv.getHelmet());
        invRow.add(inv.getChestplate());
        invRow.add(inv.getLeggings());
        invRow.add(inv.getBoots());
        String armor = InventorySerializable.serialize(invRow);
        
        String potionEffects = EffectsSerializable.serialize(player.getActivePotionEffects());
        
        Query.table(PlayerInv.TableName)
            .value(PlayerInv.Armor, armor)
            .value(PlayerInv.RowOne, rowOne)
            .value(PlayerInv.RowTwo, rowTwo)
            .value(PlayerInv.RowThree, rowThree)
            .value(PlayerInv.Hotbar, hotbar)
//          .value(PlayerInv.SelectedItem, inv.getHeldItemSlot())
            .value(PlayerInv.PotionEffects, potionEffects)
            .condition(PlayerInv.PlayerId, playerId)
            .update();
        return false;
    }
}
