/*
 * Statistics Bukkit Plugin
 *
 * V2 Copyright (c) 2016 Paul <pavog> Vogel <http://www.paulvogel.me> and contributors.
 * V1 Copyright (c) 2016 bitWolfy <http://www.wolvencraft.com> and contributors.
 * Contributors are: Mario <MarioG1> Gallaun, Christian <Dazzl> Swan, Cory <Coryf88> Finnestad, Crimsonfoxy
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.wolvencraft.yasp.events;

import com.wolvencraft.yasp.session.OnlineSession;
import com.wolvencraft.yasp.util.VariableManager.PlayerVariable;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerEvent;

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
     *
     * @return Parameter String
     * @deprecated Unsafe and unreliable
     */
    public abstract String getParameterString();

}
