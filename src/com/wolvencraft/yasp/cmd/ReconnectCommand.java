package com.wolvencraft.yasp.cmd;

import com.wolvencraft.yasp.CommandManager;
import com.wolvencraft.yasp.db.Database;
import com.wolvencraft.yasp.util.Message;

public class ReconnectCommand implements BaseCommand {

	@Override
	public boolean run(String[] args) {
		try {
			Database.getInstance().reconnect();
			Message.sendFormattedSuccess(CommandManager.getSender(), "Re-established the database connection");
			return true;
		} catch (Exception ex) {
			Message.sendFormattedError(CommandManager.getSender(), "An error occurred while reconnecting to the database");
			return false;
		}
	}

	@Override
	public void getHelp() { Message.formatHelp("reconnect", "", "Attempts to reconnect to the remote database"); }

}
