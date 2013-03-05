package com.wolvencraft.yasp.cmd;

import com.wolvencraft.yasp.CommandManager;
import com.wolvencraft.yasp.DataCollector;
import com.wolvencraft.yasp.LocalSession;
import com.wolvencraft.yasp.util.Message;

public class DumpCommand implements BaseCommand {

	@Override
	public boolean run(String[] args) {
		for(LocalSession session : DataCollector.get())
			session.dump();
		Message.sendFormattedSuccess(CommandManager.getSender(), "The local data has been dumped");
		return true;
	}

	@Override
	public void getHelp() { Message.formatHelp("dump", "", "Dumps the locally stored data"); }

}
