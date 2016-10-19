/*
 * JailPlayerEntry.java
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

package com.wolvencraft.yasp.db.data.hooks.jail;

import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.data.NormalData;
import com.wolvencraft.yasp.db.tables.Hook.JailTable;
import com.wolvencraft.yasp.util.hooks.JailHook;
import org.bukkit.entity.Player;

public class JailPlayerEntry extends NormalData {

    private String playerName;

    public JailPlayerEntry(Player player, int playerId) {
        this.playerName = player.getName();

        fetchData(playerId);
    }

    @Override
    public void fetchData(int playerId) {
        if (Query.table(JailTable.TableName)
                .condition(JailTable.PlayerId, playerId)
                .exists()) return;

        Query.table(JailTable.TableName)
                .value(JailTable.PlayerId, playerId)
                .value(JailTable.IsJailed, JailHook.isJailed(playerName))
                .value(JailTable.Jailer, JailHook.getJailer(playerName))
                .value(JailTable.RemainingTime, JailHook.getRemainingTime(playerName))
                .insert();
    }

    @Override
    public boolean pushData(int playerId) {
        return Query.table(JailTable.TableName)
                .value(JailTable.IsJailed, JailHook.isJailed(playerName))
                .value(JailTable.Jailer, JailHook.getJailer(playerName))
                .value(JailTable.RemainingTime, JailHook.getRemainingTime(playerName))
                .condition(JailTable.PlayerId, playerId)
                .update();
    }

    @Override
    @Deprecated
    public void clearData(int playerId) {
    }

}
