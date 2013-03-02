package com.wolvencraft.yasp.cmd;

import com.wolvencraft.yasp.CommandManager;

public class HelpCommand implements BaseCommand {

	@Override
	public boolean run(String[] args) {
		for(CommandManager cmd : CommandManager.values()) { cmd.getHelpLine(); }
		return true;
	}
	
	public void getHelp() {}
	public void getHelpLine() {};
}
