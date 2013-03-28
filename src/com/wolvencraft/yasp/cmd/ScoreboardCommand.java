package com.wolvencraft.yasp.cmd;

import java.util.Map;

import org.bukkit.entity.Player;

import com.wolvencraft.yasp.DataCollector;
import com.wolvencraft.yasp.CommandManager;
import com.wolvencraft.yasp.StatsPlugin;
import com.wolvencraft.yasp.scoreboard.Scoreboard;
import com.wolvencraft.yasp.scoreboard.ScoreboardAPI;
import com.wolvencraft.yasp.util.Message;

public class ScoreboardCommand implements BaseCommand {

	@Override
	public boolean run(String[] args) {
		if(!StatsPlugin.isCraftBukkitCompatible()) {
			Message.sendFormattedError("This command is not compatible with the current version of CraftBukkit");
			return false;
		}
		if(!(CommandManager.getSender() instanceof Player)) {
			Message.sendFormattedError(CommandManager.getSender(), "This command can only be executed by a living player");
			return false;
		}
		Player player = (Player) CommandManager.getSender();
        Map<String, Object> stats = DataCollector.get(player.getName()).playerTotals().getValues();
        
		Scoreboard board = ScoreboardAPI.get(player.getName());
		if(board == null) {
			board = ScoreboardAPI.add(player.getName());
			board.setName("Statistics");
			board.add("Kills", (Integer) stats.get("pvpKills"));
			board.add("Deaths", (Integer) stats.get("pvpDeaths"));
			board.setVisible(true);
		} else {
			if(board.isVisible(player)) board.setVisible(false);
			else board.setVisible(true);
			ScoreboardAPI.refresh(player);
		}
		Message.sendFormattedSuccess("Displaying a scoreboard");
		return true;
	}

	@Override
	public void getHelp() {
		Message.formatHelp("book", "", "Displays some of your stats on a scoreboard");
	}

}
