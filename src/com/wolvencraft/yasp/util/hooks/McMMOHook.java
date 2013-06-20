/*
 * mcMMOHook.java
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.craftbukkit.libs.com.google.gson.Gson;
import org.bukkit.entity.Player;

import com.gmail.nossr50.api.ExperienceAPI;
import com.gmail.nossr50.api.PartyAPI;
import com.gmail.nossr50.datatypes.skills.SkillType;
import com.gmail.nossr50.util.skills.SkillUtils;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.wolvencraft.yasp.Statistics;
import com.wolvencraft.yasp.settings.Module;
import com.wolvencraft.yasp.util.Util;
import com.wolvencraft.yasp.util.serializable.PartySerializable;
import com.wolvencraft.yasp.util.serializable.ValueSerializable;

public class McMMOHook extends PluginHook {
    
    private static List<String> SKILL_NAMES;
    
    public McMMOHook() {
        super(Module.McMMO, "mcMMO");
    }
    
    @Override
    public void onEnable() {
        ArrayList<String> names = Lists.newArrayList();
        
        for (SkillType skill : SkillType.values())
            names.add(SkillUtils.getSkillName(skill));
        
        Collections.sort(names);
        SKILL_NAMES = ImmutableList.copyOf(names);
    }
    
    /**
     * Returns player's experience
     * @param player Player to look up
     * @return Experience
     */
    public static String getExp(Player player) {
        List<ValueSerializable> values = Lists.newArrayList();
        for(String skillType : SKILL_NAMES) {
            values.add(new ValueSerializable(skillType, ExperienceAPI.getXP(player, skillType)));
        }
        return Util.toJsonArray(values);
    }
    
    /**
     * Returns player's skill levels
     * @param player Player to look up
     * @return Player's skill levels
     */
    public static String getLevel(Player player) {
        List<ValueSerializable> values = Lists.newArrayList();
        for(String skillType : SKILL_NAMES) {
            values.add(new ValueSerializable(skillType, ExperienceAPI.getLevel(player, skillType)));
        }
        return Util.toJsonArray(values);
    }
    
    /**
     * Returns the serialized party data
     * @param player Player to look up
     * @return Party data, or empty String
     */
    public static String getParty(Player player) {
        String party = PartyAPI.getPartyName(player);
        if(party == null) return "";
        List<Player> members = PartyAPI.getOnlineMembers(party);
        List<String> membersString = Lists.newArrayList();
        for(Player pl : members) membersString.add(pl.getName());
        PartySerializable value = new PartySerializable(party, membersString);
        Gson gson = Statistics.getGson();
        return gson.toJson(value);
    }
    
}
