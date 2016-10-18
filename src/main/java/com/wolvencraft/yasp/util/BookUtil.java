/*
 * BookUtil.java
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

package com.wolvencraft.yasp.util;

import com.wolvencraft.yasp.Statistics;
import com.wolvencraft.yasp.db.totals.HookTotals;
import com.wolvencraft.yasp.db.totals.PlayerTotals;
import com.wolvencraft.yasp.session.OfflineSession;
import com.wolvencraft.yasp.util.VariableSwap.HookData;
import com.wolvencraft.yasp.util.VariableSwap.PlayerData;
import com.wolvencraft.yasp.util.cache.OfflineSessionCache;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class BookUtil {
    
    private static String title;
    private static ArrayList<List<String>> Page = new ArrayList<>(); 
    
    public BookUtil() {
        int i = 1;
        File bookFile = new File(Statistics.getInstance().getDataFolder(), "resources/book.yml");
        if (!bookFile.exists()){
            Message.log("Book.yml not found. Creating one for you.");        
            Statistics.getInstance().saveResource("resources/book.yml",false);        
        }
            
        FileConfiguration bookconf = YamlConfiguration.loadConfiguration(bookFile);
        title = bookconf.getString("title");
            
        do{   
           Page.add(bookconf.getStringList("page."+i)); 
           i++;
        }while(bookconf.getList("page." +i) != null);       
         
    }
    
    /**
     * Creates a new statistics book based for the specified player.<br />
     * Version-specific method. Include methods to check for CraftBukkit version in the implementation.
     * @param player Player to use for statistics
     * @return Book with player's statistics
     */
    public static ItemStack compileStatsBook (Player player) {
        ItemStack bookStack = new ItemStack(387, 1);
        BookMeta book = (BookMeta) bookStack.getItemMeta();
        book.setTitle(title.replace("%PLAYERNAME%",player.getName()));
        book.setAuthor("Statistics");
        book.setPages(getBookPages(player.getUniqueId()));
        
        bookStack.setItemMeta(book);
        return bookStack;
    }
    
    /**
     * Returns the pages for the book with player's statistics. Could be used for offline players.<br />
     * It is safe to use this method with any version of CraftBukkit.
     * @param uuid Player's uuid to use for the statistics
     * @return Array of strings, each of them representing a new page in the book.
     */
    public static String[] getBookPages(UUID uuid) {
        OfflineSession session = OfflineSessionCache.fetch(uuid);
        PlayerTotals stats = session.getPlayerTotals();
        HookTotals hooks = session.getHookTotals();
        String[] Book = new String[50];
        String fixed_line;
        int i=0;     
        for(List<String> list : Page){
            Book[i] = "";
            for(String line : list){
                fixed_line = line.replace("%PLAYERNAME%",session.getName());
                fixed_line = PlayerData.swap(fixed_line,stats);
                fixed_line = HookData.swap(fixed_line,hooks);
                Book[i]=Book[i]+ ChatColor.translateAlternateColorCodes('&', fixed_line) + ChatColor.RESET + ChatColor.BLACK + "\n";
            }
            i++;
        }
        String[] finished_book = Arrays.copyOf(Book, i);
        return finished_book;
    }
}
