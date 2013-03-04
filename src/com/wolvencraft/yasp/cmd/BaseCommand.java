package com.wolvencraft.yasp.cmd;

public interface BaseCommand {
	
	/**
	 * Executes the command according to the arguments in the parameters
	 * @param args Command parameters
	 * @return <b>true</b> if the command was executed successfully, <b>false</b> if an error occurred
	 */
	public boolean run(String[] args);
	
	/**
	 * Returns the help line associated with the command.
	 */
	public void getHelp();
}
