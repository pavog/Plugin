/*
 * StatisticsPlayerEvent.java
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

package com.wolvencraft.yasp.events;

import lombok.Getter;
import lombok.AccessLevel;

import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerEvent;

import com.wolvencraft.yasp.session.OnlineSession;
import com.wolvencraft.yasp.util.VariableManager.PlayerVariable;

@Getter(AccessLevel.PUBLIC)
public abstract class StatisticsPlayerEvent extends PlayerEvent {
    
    private OnlineSession session;
    private PlayerVariable actionType;
    
    public StatisticsPlayerEvent(OnlineSession session, PlayerVariable actionType) {
        super(Bukkit.getServer().getPlayer(session.getName()));
        this.session = session;
        this.actionType = actionType;
    }
    
    /**
     * Returns the data associated with the event as a parameter
     * @deprecated Unsafe and unreliable
     * @return Parameter String
     */
    public abstract String getParameterString();

}
