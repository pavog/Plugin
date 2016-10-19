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

package com.wolvencraft.yasp.session;

import com.wolvencraft.yasp.db.totals.HookTotals;
import com.wolvencraft.yasp.db.totals.PlayerTotals;
import com.wolvencraft.yasp.util.cache.PlayerCache;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.UUID;

/**
 * A simplistic representation of a player's session.<br />
 * The player in question might be offline, or not exist at all.
 *
 * @author bitWolfy
 */
@Getter(AccessLevel.PUBLIC)
public class OfflineSession implements PlayerSession {

    private final int id;
    private final String name;
    private final UUID uuid;

    private PlayerTotals playerTotals;
    private HookTotals hookTotals;

    /**
     * <b>Default constructor</b><br />
     * Creates a new player data session based on his uuid
     *
     * @param uuid Player's uuid
     */
    public OfflineSession(UUID uuid) {
        this.uuid = uuid;
        this.id = PlayerCache.get(uuid);
        this.name = PlayerCache.getName(uuid);
        this.playerTotals = new PlayerTotals(id);
        this.hookTotals = new HookTotals(id);
    }

    @Override
    public boolean isOnline() {
        return Bukkit.getPlayer(uuid).isOnline();
    }

    @Override
    public UUID getUUID() {
        return this.uuid;
    }

}
