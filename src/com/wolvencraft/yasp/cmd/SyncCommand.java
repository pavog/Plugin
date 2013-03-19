/* 
 *    Copyright 2009-2011 The MyBatis Team
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *    
 *    Changelog:
 *    
 *    Cut down on unused code and generally optimized for desired tasks.
 *    - bitWolfy
 *    
 *    Added the ability to change the delimiter so you can run scripts that 
 *    contain stored procedures.
 *    - ChaseHQ
 */

package com.wolvencraft.yasp.cmd;

import org.bukkit.Bukkit;

import com.wolvencraft.yasp.CommandManager;
import com.wolvencraft.yasp.DataCollector;
import com.wolvencraft.yasp.StatsPlugin;
import com.wolvencraft.yasp.StatsSignFactory;
import com.wolvencraft.yasp.util.Message;

public class SyncCommand implements BaseCommand {

	@Override
	public boolean run(String[] args) {
		Bukkit.getScheduler().runTaskAsynchronously(StatsPlugin.getInstance(), new Runnable() {

			@Override
			public void run() {
				DataCollector.pushAllData();
			}
			
		});
		StatsSignFactory.updateAll();
		Message.sendFormattedSuccess(CommandManager.getSender(), "Synchronization complete");
		return true;
	}

	@Override
	public void getHelp() { Message.formatHelp("sync", "", "Forces the plugin to push data to the database"); }

}
