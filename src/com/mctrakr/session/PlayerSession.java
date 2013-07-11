/*
 * PlayerSession.java
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

package com.mctrakr.session;

import org.bukkit.Bukkit;

import lombok.AccessLevel;
import lombok.Getter;

import com.mctrakr.modules.totals.PlayerTotals;

@Getter(AccessLevel.PUBLIC)
public abstract class PlayerSession {
    
    protected final int id;
    protected final String name;
    protected PlayerTotals playerTotals;
    
    public PlayerSession(int id, String name) {
        this.id = id;
        this.name = name;
        this.playerTotals = new PlayerTotals(id);
    }
    
    public boolean isOnline() {
        return Bukkit.getPlayerExact(name) != null;
    }
    
}
