/*
 * TickTask.java
 * 
 * Statistics
 * Copyright (C) 2013 bitWolfy <http://www.wolvencraft.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

package com.wolvencraft.yasp.util.tasks;

import com.wolvencraft.yasp.Statistics;

/**
 * Measures the tick rate of the server.<br />
 * There can only be one instance of this method running in the plugin.
 * @author bitWolfy
 *
 */
public class TickTask implements Runnable {
    
    /**
     * <b>Default constructor.</b><br />
     * Creates a new tracker to record ticks per second.
     */
    public TickTask() {
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
            Statistics.getServerStatistics().updateTPS(ticksPerSecond);
            ticks = 0;
            if((++delay % 300) == 0) { delay = 0; }
        }
    }
    
    /**
     * Returns the server's current tickrate.
     * @return Ticks per second
     */
    public static int getTicksPerSecond() {
        return ticksPerSecond;
    }
    
}
