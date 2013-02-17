package com.wolvencraft.yasp.stats;

public class DatabaseSync implements Runnable {

	@Override
	public void run() {
		for(LocalSession session : DataCollector.get()) {
			session.pushData();
			if(session.getOnline() == false) DataCollector.remove(session);
		}
	}

}
