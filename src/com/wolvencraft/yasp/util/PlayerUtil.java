/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wolvencraft.yasp.util;


import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.tables.Normal;


public class PlayerUtil {
    
/**
 *
 * @author Mario
 * @param name Player name
 * @return TRUE on success
 */
    public static boolean remove(String name){
             
           return Query.table(Normal.PlayerStats.TableName)
                        .condition(Normal.PlayerStats.Name, name)
                        .delete();  
        
    }
    
}
