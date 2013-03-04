package com.wolvencraft.yasp.cmd;

import com.wolvencraft.yasp.CommandManager;
import com.wolvencraft.yasp.util.Message;

public class HelpCommand implements BaseCommand {

	@Override
	public boolean run(String[] args) {
		Message.formatHeader(20, "YASP Help");
		for(CommandManager cmd : CommandManager.values()) { cmd.getHelp(); }
		return true;
	}
	
	@Override
	public void getHelp() {}
}
