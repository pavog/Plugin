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

package com.wolvencraft.yasp.settings;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Data store that contains both local and remote plugin configurations
 * @author bitWolfy
 *
 */
public class Constants {
    
    /**
     * A hard-coded list of items that have a metadata that should be tracked.<br />
     * This is really bogus, but until we have a better solution, it will have to do.
     * @author bitWolfy
     *
     */
    public enum ItemsWithMetadata {
        Plank           (5, 0, 3),
        Sapling         (6, 0, 3),
        Log             (17, 0, 3),
        Leave           (18, 0, 3),
        Sandstone       (24, 0, 2),
        TallGrass       (31, 0, 2),
        Wool            (35, 0, 15),
        DoubleSlab      (43, 0, 7),
        Slab            (44, 0, 7),
        SilverfishBlock (97, 0, 2),
        StoneBricks     (98, 0, 3),
        PlankDoubleSlab (125, 0, 3),
        PlankSlab       (126, 0, 3),
        CobblestoneWall (139, 0, 1),
        MobHeadBlock    (144, 0, 4),
        Quartz          (155, 0, 2),
        Coal            (263, 0, 1),
        GoldenApple     (322, 0, 1),
        Dye             (351, 0, 15),
        Potion          (373, 0, 16489),
        MobEgg          (383, 50, 120),
        MobHead         (397, 0, 4);
        
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
        
        Statistics      ("stats.track"),
        Block           ("stats.track.block"),
        BlockPlace      ("stats.track.block.place", Block),
        BlockBreak      ("stats.track.block.break", Block),
        Item            ("stats.track.item"),
        ItemDrop        ("stats.track.item.drop", Item),
        ItemPickUp      ("stats.track.item.pickup", Item),
        ItemUse         ("stats.track.item.use", Item),
        ItemBreak       ("stats.track.item.break", Item),
        ItemCraft       ("stats.track.item.craft", Item),
        ItemAnvil       ("stats.track.item.anvil", Item),
        ItemMisc        ("stats.track.item.misc", Item),
        Player          ("stats.track.player"),
        PlayerDistances ("stats.track.player.distances", Player),
        PlayerInventory ("stats.track.player.inventory", Player),
        PlayerMisc      ("stats.track.player.misc", Player),
        Death           ("stats.track.death"),
        DeathPVP        ("stats.track.death.pvp", Death),
        DeathPVE        ("stats.track.death.pve", Death),
        DeathOther      ("stats.track.death.other", Death);
        
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
    
    /**
     * Describes the relationships between Projectile types and their item IDs.<br />
     * Used to extract an ItemStack from an EntityType.
     * @author bitWolfy
     *
     */
    public enum ProjectileToItem {
        
        Arrow(EntityType.ARROW, 262),
        Egg(EntityType.EGG, 344),
        EnderPearl(EntityType.ENDER_PEARL, 368),
        FishingHook(EntityType.FISHING_HOOK, 346),
        LargeFireball(EntityType.FIREBALL, 385),
        SmallFireball(EntityType.SMALL_FIREBALL, 51),
        Snowball(EntityType.SNOWBALL, 332),
        ThrownExpBottle(EntityType.THROWN_EXP_BOTTLE, 384),
        ThrownPotion(EntityType.SPLASH_POTION, 373),
        WitherSkull(EntityType.WITHER_SKULL, 397, (short) 1);
        
        private EntityType type;
        private int itemId;
        private short data;
        
        /**
         * <b>Default constructor</b><br />
         * Damage value defaults to 0.
         * @param type Entity type
         * @param itemId Item ID
         */
        ProjectileToItem(EntityType type, int itemId) {
            this.type = type;
            this.itemId = itemId;
            this.data = 0;
        }
        
        /**
         * <b>Constructor</b>
         * @param type Entity type
         * @param itemId Item ID
         * @param data Damage value
         */
        ProjectileToItem(EntityType type, int itemId, short data) {
            this.type = type;
            this.itemId = itemId;
            this.data = data;
        }
        
        /**
         * Parses an EntityType and returns a corresponding ItemStack
         * @param type Projectile type
         * @return Item tack
         */
        public static ItemStack parse(EntityType type) {
            for(ProjectileToItem entry : ProjectileToItem.values()) {
                if(type.equals(entry.type)) {
                    return new ItemStack(entry.itemId, 1, entry.data);
                }
            }
            return new ItemStack(Material.ARROW);
        }
        
    }
    
}
