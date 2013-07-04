/*
 * TownyHook.java
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

package com.wolvencraft.yasp.util.hooks;

import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import com.wolvencraft.yasp.settings.Module;
import com.wolvencraft.yasp.util.serializable.NationSerializable;
import com.wolvencraft.yasp.util.serializable.TownSerializable;
import com.wolvencraft.yasp.util.serializable.TownyPlayerSerializable;

public class TownyHook extends PluginHook {
    
    public TownyHook() {
        super(Module.Towny, "Towny");
    }
    
    public static String[] getPlayerData(String playerName) {
        String[] data = new String[] {"", "", ""};
        
        Resident resident;
        try { resident = TownyUniverse.getDataSource().getResident(playerName); }
        catch (NotRegisteredException e) { return data; }
        if(resident == null) return data;
        
        data[0] = TownyPlayerSerializable.serialize(resident);
        
        Town town;
        try { town = resident.getTown(); }
        catch (NotRegisteredException e) { return data; }
        if(town == null) return data;
        
        data[1] = TownSerializable.serialize(town);
        
        Nation nation;
        try { nation = town.getNation(); }
        catch (NotRegisteredException e) { return data; }
        if(nation == null) return data;
        
        data[2] = NationSerializable.serialize(nation);
        
        return data;
    }
    
}
