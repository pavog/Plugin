/*
 * CommandBookHook.java
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

import org.bukkit.entity.Player;

import com.sk89q.commandbook.CommandBook;
import com.sk89q.commandbook.GodComponent;
import com.sk89q.commandbook.session.SessionComponent;
import com.sk89q.commandbook.session.UserSession;
import com.wolvencraft.yasp.settings.Module;
import com.zachsthings.libcomponents.ComponentManager;
import com.zachsthings.libcomponents.bukkit.BukkitComponent;

public class CommandBookHook extends PluginHook {

    private static ComponentManager<BukkitComponent> componentManager;
    
    public CommandBookHook() {
        super(Module.CommandBook, "CommandBook");
    }
    
    @Override
    protected void onEnable() {
        componentManager = ((CommandBook) super.plugin).getComponentManager();
    }
    
    @Override
    protected void onDisable() {
        componentManager = null;
    }
    
    /**
     * Checks if the player is in the god mode
     * @param player Player to look up
     * @return <b>true</b> if the player has god mode on, <b>false</b> otherwise
     */
    public static boolean isGodMode(Player player) {
        GodComponent component = ((GodComponent) componentManager.getComponent(GodComponent.class));
        return component.isEnabled() && component.hasGodMode(player);
    }
    
    /**
     * Returns the player's AFK status
     * @param player Player to look up
     * @return <b>true</b> if the player is AFK, <b>false</b> otherwise
     */
    public static boolean isAFK(Player player) {
        SessionComponent sessions = componentManager.getComponent(SessionComponent.class);
        UserSession session = sessions.getSession(UserSession.class, player);
        return session.getIdleStatus() != null;
    }
    
}
