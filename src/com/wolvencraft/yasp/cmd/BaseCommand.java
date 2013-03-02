package com.wolvencraft.yasp.cmd;

public interface BaseCommand {
	public boolean run(String[] args);
	public void getHelp();
	public void getHelpLine();
}
