/*
 * BanHammerHook.java
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
import java.util.List;

import name.richardson.james.bukkit.banhammer.BanHammer;
import name.richardson.james.bukkit.banhammer.persistence.BanRecord;

import com.wolvencraft.yasp.settings.Module;
import com.wolvencraft.yasp.util.serializable.BanRecordSerializable;

public class BanHammerHook extends PluginHook {
    
    private static BanHammer instance;
    
    public BanHammerHook() {
        super(Module.BanHammer, "BanHammer");
    }
    
    @Override
    protected void onEnable() {
        instance = (BanHammer) super.plugin;
    }
    
    @Override
    protected void onDisable() {
        instance = null;
    }
    
    /**
     * Returns the ban data as a Json array
     * @param playerName Player to look up
     * @return Ban data, or an empty string
     */
    public static String getBan(String playerName) {
        List<BanRecordSerializable> records = new ArrayList<BanRecordSerializable>();
        for(BanRecord record : instance.getHandler().getPlayerBans(playerName)) {
            records.add(new BanRecordSerializable(
                    record.getCreator().getName(),
                    record.getReason(),
                    record.getCreatedAt().getTime() / 1000,
                    record.getExpiresAt().getTime() / 1000));
        }
        return BanRecordSerializable.serialize(records);
    }
    
}
