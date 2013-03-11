package com.wolvencraft.yasp.util;

/**
 * Measures the tick rate of the server.
 * @author bitWolfy
 *
 */
public class TPSTracker implements Runnable {
	
	public TPSTracker() {
		ticksPerSecond = 0;
	}
	
	private static int ticksPerSecond;
	private long sec;
	private long currentSec;
	private int ticks;
	private int delay;
	
	@Override
	public void run() {
		sec = (System.currentTimeMillis() / 1000);
		if(currentSec == sec) { ticks++; }
		else {
			currentSec = sec;
			ticksPerSecond = (ticksPerSecond == 0 ? ticks : ((ticksPerSecond + ticks) / 2));
			ticks = 0;
			if((++delay % 300) == 0) { delay = 0; }
		}
	}
	
	public static int getTicksPerSecond() { return ticksPerSecond; }
	
}
