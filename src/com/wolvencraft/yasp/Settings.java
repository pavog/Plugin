/*
 * Settings.java
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

package com.wolvencraft.yasp;

import org.bukkit.entity.Player;

import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.Query.QueryResult;
import com.wolvencraft.yasp.db.tables.Normal.SettingsTable;

/**
 * Data store that contains both local and remote plugin configurations
 * @author bitWolfy
 *
 */
public class Settings {
    
    /**
     * Clears the settings cache.<br />
     * Entries will have to be pulled from the database next time they are requested
     */
    public static void clearCache() {
        Modules.clearCache();
        RemoteConfiguration.clearCache();
    }
    
    /**
     * Represents the local configuration, stored in <i>config.yml</i>
     * @author bitWolfy
     *
     */
    public enum LocalConfiguration {
        Debug("debug", true),
        DBHost("database.host", true),
        DBPort("database.port", true),
        DBName("database.name", true),
        DBUser("database.user", true),
        DBPass("database.pass", true),
        DBPrefix("database.prefix", true),
        DBConnect("jdbc:mysql://" + DBHost.asString() + ":" + DBPort.asInteger() + "/" + DBName.asString()),
        LogPrefix("log-prefix", true),
        Cloud(true);
        
        Object entry;
        
        /**
         * <b>Default constructor</b><br />
         * Creates a new LocalConfiguration with the specified value
         * @param entry Configuration value
         */
        LocalConfiguration(Object entry) {
            this.entry = entry;
        }
        
        /**
         * <b>Constructor</b><br />
         * Creates a new LocalConfiguration entry with the value that can be pulled from <code>config.yml</code>.
         * @param entry Configuration value or <code>config.yml</code> node
         * @param fromFile If <b>true</b>, configuration values will be pulled from file, otherwise, the <code>entry</code> will be used
         */
        LocalConfiguration(Object entry, boolean fromFile) {
            if(fromFile) this.entry = Statistics.getInstance().getConfig().getString((String) entry);
            else this.entry = entry;
        }
        
        /**
         * Returns the configuration value as String
         * @deprecated <code>asString();</code> should be used instead
         * @return Configuration value
         */
        @Override
        public String toString() {
            return asString();
        }
        
        /**
         * Returns the configuration value as String
         * @return Configuration value
         */
        public String asString() {
            return (String) entry;
        }
        
        /**
         * Returns the configuration value as a boolean
         * @return Configuration value
         */
        public boolean asBoolean() {
            try { return Boolean.parseBoolean((String) entry); }
            catch (Throwable t) { return false; }
        }
        
        /**
         * Returns the configuration value as an integer
         * @return Configuration value
         */
        public int asInteger() {
            try { return Integer.parseInt((String) entry); }
            catch (Throwable t) { return 0; }
        }
    }
    
    /**
     * Checks for the modules activated via the portal admin interface.
     * @author bitWolfy
     *
     */
    public enum Modules {
        Server("module.server"),
        Blocks("module.blocks"),
        Items("module.items"),
        Deaths("module.deaths"),
        Inventory("module.inventory"),
        
        HookVault("hook.vault"),
        HookWorldGuard("hook.worldguard"),
        HookJobs("hook.jobs"),
        HookMcMMO("hook.mcmmo");
        
        private String key;
        private boolean active;
        private boolean refresh;
        
        /**
         * <b>Default constructor</b><br />
         * Creates a new Module entry with the specified key
         * @param key Entry key
         */
        Modules(String key) {
            this.key = key;
            try { active = Query.table(SettingsTable.TableName).column("value").condition("key", key).select().asBoolean("value"); }
            catch (Throwable t) { active = true; }
            refresh = false;
        }
        
        /**
         * Returns the status of the module according to the remote configuration
         * @return <b>true</b> if the module is enabled, <b>false</b> if it is not
         */
        public boolean getEnabled() {
            if(refresh) {
                try { active = Query.table(SettingsTable.TableName).column("value").condition("key", key).select().asBoolean("value"); }
                catch (Throwable t) { active = true; }
                refresh = false;
            }
            return active;
        }
        
        /**
         * Signals the plugin to pull the stored value from the database next time it is called
         */
        private static void clearCache() {
            for(Modules module : Modules.values()) module.refresh = true;
        }
    }
    
    /**
     * Represents modules that are currently active.
     * @author bitWolfy
     *
     */
    public enum ActiveHooks {
        HookVault,
        HookWorldGuard,
        HookJobs,
        HookMcMMO;
        
        boolean active;
        
        ActiveHooks() { active = false; }
        
        public boolean getActive() { return active; }
        public void setActive(boolean active) { this.active = active; }
    }
    
    /**
     * Represents the configuration pulled from the database.<br />
     * No data is stored locally; all information is pulled from the database during runtime.
     * @author bitWolfy
     *
     */
    public enum RemoteConfiguration {
        DatabaseVersion("version"),
        
        Ping("ping"),
        LogDelay("log_delay"),
        ShowWelcomeMessages("show_welcome_messages"),
        WelcomeMessage("welcome_message"),
        ShowFirstJoinMessages("show_first_join_message"),
        FirstJoinMessage("first_join_message");
        
        String key;
        QueryResult entry;
        boolean refresh;
        
        /**
         * <b>Default constructor</b><br />
         * Creates a new RemoteConfiguration entry based on the specified key
         * @param key Entry key
         */
        RemoteConfiguration(String key) {
            try { this.entry = Query.table(SettingsTable.TableName).column("value").condition("key", key).select(); }
            catch (Throwable t) { this.entry = null; }
            refresh = false;
            this.key = key;
        }
        
        /**
         * Returns the configuration value as String
         * @deprecated <code>asString();</code> should be used instead
         * @return Configuration value
         */
        @Override
        public String toString() {
            return asString();
        }
        
        /**
         * Returns the configuration value as String
         * @return Configuration value
         */
        public String asString() {
            if(refresh) {
                try { entry = Query.table(SettingsTable.TableName).column("value").condition("key", key).select(); }
                catch (Throwable t) { entry = null; }
                refresh = false;
            }
            try { return entry.asString("value"); }
            catch (Throwable t) { return ""; }
        }
        
        /**
         * Returns the configuration value as an integer
         * @return Configuration value
         */
        public int asInteger() { 
            if(refresh) {
                try { entry = Query.table(SettingsTable.TableName).column("value").condition("key", key).select(); }
                catch (Throwable t) { entry = null; }
                refresh = false;
            }
            try { return entry.asInt("value"); }
            catch (Throwable t) { return 0; }
        }
        
        /**
         * Returns the configuration value as a boolean
         * @return Configuration value
         */
        public boolean asBoolean() {
            if(refresh) {
                try { entry = Query.table(SettingsTable.TableName).column("value").condition("key", key).select(); }
                catch (Throwable t) { entry = null; }
                refresh = false;
            }
            try { return entry.asBoolean("value"); }
            catch (Throwable t) { return false; }
        }
        
        /**
         * Updates the configuration with the specified value
         * @param value New configuration value
         * @return <b>true</b> if the update was successful, <b>false</b> otherwise
         */
        public boolean update(Object value) {
            return Query.table(SettingsTable.TableName).value("value", value).condition("key", key).update();
        }
        
        /**
         * Signals the plugin to pull the stored value from the database next time it is called
         */
        private static void clearCache() {
            for(RemoteConfiguration configEntry : RemoteConfiguration.values()) {
                configEntry.refresh = true;
            }
        }
    }
    
    /**
     * A hard-coded list of items that have a metadata that should be tracked.<br />
     * This is really bogus, but until we have a better solution, it will have to do.
     * @author bitWolfy
     *
     */
    public enum ItemsWithMetadata {
        Plank(5, 0, 3),
        Sapling(6, 0, 3),
        Log(17, 0, 3),
        Leave(18, 0, 3),
        Sandstone(24, 0, 2),
        TallGrass(31, 0, 2),
        Wool(35, 0, 15),
        DoubleSlab(43, 0, 7),
        Slab(44, 0, 7),
        SilverfishBlock(97, 0, 2),
        StoneBricks(98, 0, 3),
        PlankDoubleSlab(125, 0, 3),
        PlankSlab(126, 0, 3),
        CobblestoneWall(139, 0, 1),
        MobHeadBlock(144, 0, 4),
        Quartz(155, 0, 2),
        Coal(263, 0, 1),
        GoldenApple(322, 0, 1),
        Dye(351, 0, 15),
        Potion(373, 0, 16489),
        MobEgg(383, 50, 120),
        MobHead(397, 0, 4);
        
        int itemId;
        int minMetaData;
        int maxMetaData;
        
        /**
         * <b>Default constructor</b><br />
         * Sets up a constraint on item's metadata with the specified values
         * @param itemId Item ID
         * @param minMetaData Minimum allowed metadata
         * @param maxMetaData Maximum allowed metadata
         */
        ItemsWithMetadata(int itemId, int minMetaData, int maxMetaData) {
            this.itemId = itemId;
            this.minMetaData = minMetaData;
            this.maxMetaData = maxMetaData;
        }
        
        /**
         * Returns the item ID associated with this constraint
         * @return Item ID
         */
        public int getId() {
            return itemId;
        }
        
        /**
         * Returns the minimum metadata constraint for this item ID
         * @return Minimum metadata
         */
        public int getMinData() {
            return minMetaData;
        }
        
        /**
         * Returns the maximum metadata constraint for this item ID
         * @return Maximum metadata
         */
        public int getMaxData() {
            return maxMetaData;
        }
        
        /**
         * Checks if the specified metadata is valid
         * @param data Metadata to check
         * @return <b>true</b> if the data is valid, <b>false</b> otherwise
         */
        public boolean isDataValid(int data) {
            if(data < minMetaData || data > maxMetaData) return false;
            return true;
        }
        
        /**
         * Checks if the specified metadata falls under the constraint.<br />
         * If it does, it is returned unchanged, otherwise, the minMetaData is returned
         * @param data Metadata to check
         * @return Resulting metadata
         */
        public int getData(int data) {
            if(data >= minMetaData && data <= maxMetaData) return data;
            return minMetaData;
        }
        
        /**
         * Checks if the specified ID is in the list
         * @param id Item ID
         * @return <b>true</b> if the item is in the list, <b>false</b> otherwise
         */
        public static boolean checkAgainst(int id) {
            for(ItemsWithMetadata entry : ItemsWithMetadata.values()) {
                if(entry.getId() == id) return true;
            }
            return false;
        }
        
        /**
         * Returns the ItemsWithMetadata object based on the item ID provided
         * @param id Item ID
         * @return ItemsWithMetadata object
         */
        public static ItemsWithMetadata get(int id) {
            for(ItemsWithMetadata entry : ItemsWithMetadata.values()) {
                if(entry.getId() == id) return entry;
            }
            return null;
        }
    }
    
    /**
     * Temporary solution to the permissions problem.<br />
     * <blockquote>Nothing is more permanent than the temporary.<br />- Greek proverb</blockquote>
     * @author bitWolfy
     *
     */
    public enum StatPerms {
        
        Statistics("stats.track"),
        Block("stats.track.block"),
        BlockPlace("stats.track.block.place", Block),
        BlockBreak("stats.track.block.break", Block),
        Item("stats.track.item"),
        ItemDrop("stats.track.item.drop", Item),
        ItemPickUp("stats.track.item.pickup", Item),
        ItemUse("stats.track.item.use", Item),
        ItemBreak("stats.track.item.break", Item),
        ItemCraft("stats.track.item.craft", Item),
        ItemMisc("stats.track.item.misc", Item),
        Player("stats.track.player"),
        PlayerDistances("stats.track.player.distances", Player),
        PlayerInventory("stats.track.player.inventory", Player),
        PlayerMisc("stats.track.player.misc", Player),
        Death("stats.track.death"),
        DeathPVP("stats.track.death.pvp", Death),
        DeathPVE("stats.track.death.pve", Death),
        DeathOther("stats.track.death.other", Death);
        
        StatPerms parent;
        String node;
        
        /**
         * <b>Default constructor</b><br />
         * Constructor for the parent nodes
         * @param node Permissions node
         */
        StatPerms(String node) {
            this.node = node;
            this.parent = null;
        }
        
        /**
         * <b>Constructor</b><br />
         * Constructor for child nodes
         * @param node Permissions node
         * @param parent Parent node
         */
        StatPerms(String node, StatPerms parent) {
            this.node = node;
            this.parent = parent;
        }
        
        /**
         * Checks if the player has the specified node
         * @param player Player to check
         * @return <b>true</b> if the player has the node, <b>false</b> otherwise
         */
        public boolean has(Player player) {
            return player.isOp() || player.hasPermission(node);
        }
        
    }
    
}
