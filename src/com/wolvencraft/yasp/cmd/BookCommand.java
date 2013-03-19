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

import org.bukkit.entity.Player;

import com.wolvencraft.yasp.CommandManager;
import com.wolvencraft.yasp.util.Message;
import com.wolvencraft.yasp.util.Util;

public class BookCommand implements BaseCommand {

	@Override
	public boolean run(String[] args) {
		if(!(CommandManager.getSender() instanceof Player)) {
			Message.sendFormattedError(CommandManager.getSender(), "This command can only be executed by a living player");
			return false;
		}
		Player player = (Player) CommandManager.getSender();
		player.getInventory().addItem(Util.compileStatsBook(player));
		return false;
	}

	@Override
	public void getHelp() {
		Message.formatHelp("book", "", "Get a book with all your statistical information");
	}

}
