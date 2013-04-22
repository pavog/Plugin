/*
 * GroupsSerializable.java
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

package com.wolvencraft.yasp.util.serializable;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;

import com.wolvencraft.yasp.util.Util;
import com.wolvencraft.yasp.util.hooks.VaultHook;

/**
 * <b>Requires VaultHook</b><br />
 * Provides means to serialize player's groups into a Json array
 * @author bitWolfy
 *
 */
@SuppressWarnings("unused")
public class GroupsSerializable {
    
    private String group;
    private String world;
    
    /**
     * <b>Default constructor</b>
     * @param group Group name
     * @param world World name
     */
    private GroupsSerializable(String group, String world) {
        this.group = group;
        this.world = world;
    }
    
    /**
     * Compresses a List into a single-line json array.<br />
     * Wraps around <code>Util.toJsonArray(List&lt;?&gt; source);</code><br />
     * Stores world and group names
     * @param playerName Name of the player to serialize
     * @return String json array
     */
    public static String serialize(String playerName) {
        List<GroupsSerializable> groups = new ArrayList<GroupsSerializable>();
        for(World world : Bukkit.getServer().getWorlds()) {
            groups.add(new GroupsSerializable(VaultHook.getGroup(playerName, world.getName()), world.getName()));
        }
        return Util.toJsonArray(groups);
    }
    
}
