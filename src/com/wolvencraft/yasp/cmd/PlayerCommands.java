package com.wolvencraft.yasp.cmd;

import java.util.List;

import org.bukkit.entity.Player;

import com.wolvencraft.yasp.CommandManager;
import com.wolvencraft.yasp.CommandManager.Command;
import com.wolvencraft.yasp.util.Message;
import com.wolvencraft.yasp.util.cache.OnlineSessionCache;

public class PlayerCommands {
    
    @Command(
            alias = "book",
            minArgs = 0,
            maxArgs = 0,
            permission = "stats.cmd.book",
            allowConsole = false,
            unstable = true,
            usage = "/stats book",
            description = "Get a book with all your statistical information"
            )
    public static boolean book(List<String> args) {
        Player player = (Player) CommandManager.getSender();
        player.getInventory().addItem(com.wolvencraft.yasp.util.BookUtil.compileStatsBook(player));
        return false;
    }
    
    @Command(
            alias = "scoreboard",
            minArgs = 0,
            maxArgs = 0,
            permission = "stats.cmd.board",
            allowConsole = false,
            unstable = true,
            usage = "/stats scoreboard",
            description = "Displays some of your stats on a scoreboard"
            )
    public static boolean scoreboard(List<String> args) {
        Player player = (Player) CommandManager.getSender();
        if(OnlineSessionCache.fetch(player).toggleScoreboard()) Message.sendFormattedSuccess("Displaying a scoreboard");
        else Message.sendFormattedSuccess("Scoreboard disabled");
        return true;
    }
    
}
