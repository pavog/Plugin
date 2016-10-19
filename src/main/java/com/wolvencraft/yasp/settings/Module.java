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

package com.wolvencraft.yasp.settings;

import com.google.common.collect.Lists;
import com.wolvencraft.yasp.Statistics;
import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.Query.QueryResult;
import com.wolvencraft.yasp.db.data.DataStore;
import com.wolvencraft.yasp.db.data.blocks.BlockData;
import com.wolvencraft.yasp.db.data.deaths.DeathData;
import com.wolvencraft.yasp.db.data.hooks.admincmd.AdminCmdData;
import com.wolvencraft.yasp.db.data.hooks.banhammer.BanHammerData;
import com.wolvencraft.yasp.db.data.hooks.commandbook.CommandBookData;
import com.wolvencraft.yasp.db.data.hooks.factions.FactionsData;
import com.wolvencraft.yasp.db.data.hooks.jail.JailData;
import com.wolvencraft.yasp.db.data.hooks.mcmmo.McMMOData;
import com.wolvencraft.yasp.db.data.hooks.mobarena.MobArenaData;
import com.wolvencraft.yasp.db.data.hooks.pvparena.PvpArenaData;
import com.wolvencraft.yasp.db.data.hooks.towny.TownyData;
import com.wolvencraft.yasp.db.data.hooks.vanish.VanishData;
import com.wolvencraft.yasp.db.data.hooks.vault.VaultData;
import com.wolvencraft.yasp.db.data.hooks.votifier.VotifierData;
import com.wolvencraft.yasp.db.data.hooks.worldguard.WorldGuardData;
import com.wolvencraft.yasp.db.data.items.ItemData;
import com.wolvencraft.yasp.db.data.pve.PVEData;
import com.wolvencraft.yasp.db.data.pvp.PVPData;
import com.wolvencraft.yasp.db.tables.Miscellaneous.SettingsTable;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.List;

/**
 * Represents the different plugin modules
 *
 * @author bitWolfy
 */
@SuppressWarnings("unchecked")
public enum Module {

    Server("server", false),
    Blocks("blocks", false, BlockData.class),
    DetailedBlocks("detailed_blocks", false, BlockData.class),
    Items("items", false, ItemData.class),
    DetailedItems("detailed_items", false, ItemData.class),
    Deaths("deaths", false, DeathData.class, PVEData.class, PVPData.class),
    DetailedPVEDeaths("detailed_pve", false, PVEData.class),
    DetailedPVPDeaths("detailed_pvp", false, PVPData.class),
    Inventory("inventory", false),

    AdminCmd("admincmd", true, AdminCmdData.class),
    BanHammer("banhammer", true, BanHammerData.class),
    CommandBook("commandbook", true, CommandBookData.class),
    Factions("factions", true, FactionsData.class),
    Jail("jail", true, JailData.class),
    McBans("mcbans", true),
    McMMO("mcmmo", true, McMMOData.class),
    MobArena("mobarena", true, MobArenaData.class),
    PvpArena("pvparena", true, PvpArenaData.class),
    Towny("towny", true, TownyData.class),
    Vanish("vanishnopacket", true, VanishData.class),
    Vault("vault", true, VaultData.class),
    Votifier("votifier", true, VotifierData.class),
    WorldGuard("worldguard", true, WorldGuardData.class),

    Unknown("unknown", false);

    public final String KEY;

    @Getter(AccessLevel.PUBLIC)
    private boolean hook;

    @Getter(AccessLevel.PUBLIC)
    private List<Class<? extends DataStore<?, ?>>> dataStores;

    private boolean refreshScheduled;

    private boolean enabled;
    private boolean active;
    private int version;

    Module(String key, boolean isHook, Class<? extends DataStore<?, ?>>... dataStores) {
        this.hook = isHook;
        this.KEY = key;

        try {
            updateCache();
        } catch (Throwable t) {
        }

        if (dataStores.length == 0) this.dataStores = Lists.newArrayList();
        else this.dataStores = Arrays.asList(dataStores);

        if (!isHook) active = true;
        refreshScheduled = false;
    }

    /**
     * Schedules all the modules to refresh
     */
    public static void clearCache() {
        for (Module module : Module.values()) module.refreshScheduled = true;
    }

    /**
     * Returns the state of the module specified in the configuration.
     *
     * @return <b>true</b> if the module is enabled, <b>false</b> if it is not
     */
    public boolean isEnabled() {
        if (refreshScheduled) updateCacheAsynchronously();
        return enabled;
    }

    /**
     * Checks if the module is actually active.
     *
     * @return <b>true</b> if the module is both enabled and active, <b>false</b> otherwise
     */
    public boolean isActive() {
        return isEnabled() && active;
    }

    /**
     * Sets the active status of the module.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Returns the version of the module.<br />
     * Only relevant if the module is a plugin hook.
     *
     * @return Module version
     */
    public int getVersion() {
        if (refreshScheduled) updateCacheAsynchronously();
        return version;
    }

    /**
     * Sets the new version of the module.<br />
     * Updates the version in the database if the module is a hook
     *
     * @param version New version
     */
    public void setVersion(int version) {
        if (refreshScheduled) updateCacheAsynchronously();
        this.version = version;
        if (!hook) return;
        String versionKey = "version." + KEY;
        Query.table(SettingsTable.TableName)
                .value("value", version)
                .condition("key", versionKey)
                .update();
    }

    /**
     * Fetches the module variables from the database
     */
    private void updateCache() {
        String stateKey = "";
        if (hook) {
            stateKey = "hook." + KEY;

            String versionKey = "version." + KEY;
            QueryResult versionResult = Query.table(SettingsTable.TableName).column("value").condition("key", versionKey).select();
            if (versionResult == null) {
                Query.table(SettingsTable.TableName).value("key", versionKey).value("value", 0).insert();
                version = 0;
            } else {
                version = versionResult.asInt("value");
            }
        } else {
            stateKey = "module." + KEY;
            version = -1;
        }

        QueryResult enabledResult = Query.table(SettingsTable.TableName).column("value").condition("key", stateKey).select();
        if (enabledResult == null) {
            Query.table(SettingsTable.TableName).value("key", stateKey).value("value", false).insert();
            enabled = true;
        } else {
            enabled = enabledResult.asBoolean("value");
        }

        refreshScheduled = false;
    }

    /**
     * Fetches the module variables from the database asynchronously
     */
    private void updateCacheAsynchronously() {
        if (!Statistics.getInstance().isEnabled()) return;
        Bukkit.getScheduler().runTaskAsynchronously(Statistics.getInstance(), new Runnable() {
            @Override
            public void run() {
                updateCache();
            }
        });
    }

}
