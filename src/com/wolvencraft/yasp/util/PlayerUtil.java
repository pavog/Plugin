/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wolvencraft.yasp.util;


import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.tables.Normal;
import java.util.UUID;


public class PlayerUtil {
    
/**
 *
 * @author Mario
 * @param uuid Player uuid
 * @return TRUE on success
 */
    public static boolean remove(UUID uuid){
             
           return Query.table(Normal.PlayerStats.TableName)
                        .condition(Normal.PlayerStats.UUID, uuid.toString())
                        .delete();  
        
    }
    
}
