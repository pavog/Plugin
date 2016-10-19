/*
 * Copyright (C) 2014 Mario
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.wolvencraft.yasp.db.totals;

import com.wolvencraft.yasp.Statistics;
import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.tables.Hook;
import com.wolvencraft.yasp.settings.Module;
import com.wolvencraft.yasp.util.VariableManager;
import com.wolvencraft.yasp.util.VariableManager.HookVariable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.HashMap;
import java.util.Map;

/**
 * Generic Hook information used on DisplaySigns and books.
 *
 * @author Mario
 */
public class HookTotals {

    private int playerId;
    private Map<HookVariable, Object> values;

    /**
     * <b>Default Constructor</b><br />
     * Sets up the default values for the data holder.
     */
    public HookTotals(int playerId) {
        this.playerId = playerId;

        values = new HashMap<HookVariable, Object>();

        fetchData();
    }

    /**
     * Fetches the data from the remote database.<br />
     * Automatically calculates values from the contents of corresponding tables.
     */
    public void fetchData() {

        if (!Statistics.getInstance().isEnabled()) return;
        if (Module.Vault.isActive()) {
            try {
                values.put(HookVariable.MONEY, Query.table(Hook.VaultTable.TableName).column(Hook.VaultTable.Balance).condition(Hook.VaultTable.PlayerId, playerId).select().asDouble(Hook.VaultTable.Balance));
                try {
                    JSONArray JSONarray = (JSONArray) new JSONParser().parse(Query.table(Hook.VaultTable.TableName).column(Hook.VaultTable.GroupName).condition(Hook.VaultTable.PlayerId, playerId).select().asString(Hook.VaultTable.GroupName));
                    JSONObject group = (JSONObject) JSONarray.get(0);
                    values.put(HookVariable.GROUP, group.get("group"));
                } catch (ParseException e) {
                    values.put(HookVariable.GROUP, "Error!");
                }
            } catch (NullPointerException e) {
                values.put(HookVariable.GROUP, "Not tracked!");
                values.put(HookVariable.MONEY, (double) 0);
            }

        } else {
            values.put(HookVariable.MONEY, (double) 0);
            values.put(HookVariable.GROUP, "Hook disabled!");
        }
    }

    /**
     * Safely returns the value of the specified variable
     *
     * @param type Variable to return
     * @return Variable value
     */
    public Object getValue(VariableManager.HookVariable type) {
        if (values.containsKey(type)) return values.get(type);
        values.put(type, 0);
        return 0;
    }

}
