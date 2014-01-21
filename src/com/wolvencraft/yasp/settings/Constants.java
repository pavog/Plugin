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

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

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
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum ItemsWithMetadata {
        DIRT            (3, 0, 2),
        Plank           (5, 0, 5),
        Sapling         (6, 0, 5),
        Log             (17, 0, 3),
        Leave           (18, 0, 3),
        Sandstone       (24, 0, 2),
        TallGrass       (31, 0, 2),
        Wool            (35, 0, 15),
        RED_ROSE        (38, 0, 8),
        DoubleSlab      (43, 0, 7),
        Slab            (44, 0, 7),
        STAINED_GLASS   (95, 0, 15),
        SilverfishBlock (97, 0, 2),
        StoneBricks     (98, 0, 3),
        PlankDoubleSlab (125, 0, 3),
        PlankSlab       (126, 0, 5),
        CobblestoneWall (139, 0, 1),
        MobHeadBlock    (144, 0, 4),
        ANVIL           (145, 0, 2),
        Quartz          (155, 0, 2),
        STAINED_CLAY    (159, 0, 15),
        STAINED_GLASS_PANE (160, 0, 15),
        Leaves_2        (161, 0, 1),
        LOG_2           (162, 0, 1),
        CARPET          (171, 0, 15),
        DOUBLE_PLANT    (175, 0, 5),
        Coal            (263, 0, 1),
        GoldenApple     (322, 0, 1),
        RAW_FISH        (349, 0, 3),
        COOKED_FISH     (350, 0, 1),
        Dye             (351, 0, 15),
        Potion          (373, 0, 16489),
        MobEgg          (383, 50, 120),
        MobHead         (397, 0, 4);
        
        private int id;
        private int minData;
        private int maxData;
        
        /**
         * Checks if the specified metadata is valid
         * @param data Metadata to check
         * @return <b>true</b> if the data is valid, <b>false</b> otherwise
         */
        public boolean isDataValid(int data) {
            if(data < minData || data > maxData) return false;
            return true;
        }
        
        /**
         * Checks if the specified metadata falls under the constraint.<br />
         * If it does, it is returned unchanged, otherwise, the minMetaData is returned
         * @param data Metadata to check
         * @return Resulting metadata
         */
        public int getValidData(int data) {
            if(data >= minData && data <= maxData) return data;
            return minData;
        }
        
        /**
         * Checks if the specified ID is in the list
         * @param id Item ID
         * @return <b>true</b> if the item is in the list, <b>false</b> otherwise
         */
        public static boolean contains(int id) {
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
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
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

        private String node;
        private StatPerms parent;
        
        StatPerms(String node) {
            this.node = node;
            this.parent = null;
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
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    public enum ProjectileToItem {
        
        Arrow           (EntityType.ARROW, 262, 0),
        Egg             (EntityType.EGG, 344, 0),
        EnderPearl      (EntityType.ENDER_PEARL, 368, 0),
        FishingHook     (EntityType.FISHING_HOOK, 346, 0),
        LargeFireball   (EntityType.FIREBALL, 385, 0),
        SmallFireball   (EntityType.SMALL_FIREBALL, 51, 0),
        Snowball        (EntityType.SNOWBALL, 332, 0),
        ThrownExpBottle (EntityType.THROWN_EXP_BOTTLE, 384, 0),
        ThrownPotion    (EntityType.SPLASH_POTION, 373, 0),
        WitherSkull     (EntityType.WITHER_SKULL, 397, 1);
        
        private EntityType type;
        private int itemId;
        private int data;
        
        /**
         * Parses an EntityType and returns a corresponding ItemStack
         * @param type Projectile type
         * @return Item tack
         */
        public static ItemStack parse(EntityType type) {
            for(ProjectileToItem entry : ProjectileToItem.values()) {
                if(type.equals(entry.type)) {
                    return new ItemStack(entry.itemId, 1, (short) entry.data);
                }
            }
            return new ItemStack(Material.ARROW);
        }
    }
}
