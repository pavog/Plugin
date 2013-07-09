package com.wolvencraft.yasp.db.data.inventory;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import com.wolvencraft.yasp.db.DBTable;

public class Tables {
    
    private Tables() { }
    
    /**
     * Represents the <i>player_inventories</i> table.
     * @author bitWolfy
     *
     */
    @AllArgsConstructor(access=AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    public enum PlayerInv implements DBTable {
        TableName       ("player_inventories"),
        PlayerId        ("player_id"),
        SelectedItem    ("selected_item"),
        Hotbar          ("hotbar"),
        RowOne          ("row_one"),
        RowTwo          ("row_two"),
        RowThree        ("row_three"),
        Armor           ("armor"),
        PotionEffects   ("potion_effects");
        
        private String columnName;
        
        @Override
        public String toString() { return columnName; }
    }

}
