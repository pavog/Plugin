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

package com.wolvencraft.yasp.session;

import java.util.UUID;


/**
 * Player session interface
 *
 * @author bitWolfy
 */
public interface PlayerSession {

    /**
     * Returns the stored player ID
     *
     * @return Player ID
     */
    public int getId();

    /**
     * Returns the player name
     *
     * @return Player name
     */
    public String getName();

    /**
     * Returns the player uuid
     *
     * @return UUID uuid
     */
    public UUID getUUID();

    /**
     * Checks if the player is online
     *
     * @return <b>true</b> if the player is online, <b>false</b> otherwise
     */
    public boolean isOnline();

}
