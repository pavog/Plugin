/*
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

package com.wolvencraft.yasp.db.data.sync;

import com.wolvencraft.yasp.StatsPlugin;
import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.tables.Normal.SettingsTable;

/**
 * Data store that contains both local and remote plugin configurations
 * @author bitWolfy
 *
 */
public class Settings {
	
	/**
	 * Represents the local configuration, stored in <i>config.yml</i>
	 * @author bitWolfy
	 *
	 */
	public enum LocalConfiguration {
		Debug("debug"),
		DBHost("database.host"),
		DBPort("database.port"),
		DBName("database.name"),
		DBUser("database.user"),
		DBPass("database.pass"),
		DBPrefix("database.prefix"),
		DBConnect("", "jdbc:mysql://" + DBHost.asString() + ":" + DBPort.asInteger() + "/" + DBName.asString()),
		LogPrefix("", StatsPlugin.getInstance().getDescription().getName());
		
		LocalConfiguration(String node) {
			this.node = node;
			this.value = null;
		}
		
		LocalConfiguration(String node, Object value) {
			this.node = node;
			this.value = value;
		}
		
		String node;
		Object value;
		
		@Override
		public String toString() { return value == null ? StatsPlugin.getInstance().getConfig().getString(node) : (String) value; }
		public String asString() { return value == null ? StatsPlugin.getInstance().getConfig().getString(node) : (String) value; }
		public Boolean asBoolean() { return value == null ? StatsPlugin.getInstance().getConfig().getBoolean(node) : (Boolean) value; }
		public Integer asInteger() { return value == null ? StatsPlugin.getInstance().getConfig().getInt(node) : (Integer) value; }
	}
	
	/**
	 * Represents modules that are currently active.
	 * @author bitWolfy
	 *
	 */
	public enum Modules {
		HookVault,
		HookWorldGuard,
		HookJobs,
		HookMcMMO;
		
		Modules() { active = false; }
		
		boolean active;
		
		public boolean getActive() { return active; }
		public void setActive(boolean active) { this.active = active; }
	}
	
	/**
	 * Represents the configuration pulled from the database.<br />
	 * No data is stored locally; all information is pulled from the database during runtime.
	 * @author bitWolfy
	 *
	 */
	public enum RemoteConfiguration {
		DatabaseVersion("version"),
		
		Ping("ping"),
		ShowWelcomeMessages("show_welcome_messages"),
		WelcomeMessage("welcome_message"),
		ShowFirstJoinMessages("show_first_join_messages"),
		FirstJoinMessage("first_join_message"),
		
		HookVault("hook_vault"),
		HookWorldGuard("hook_worldguard"),
		HookJobs("hook_jobs"),
		HookMcMMO("hook_mcmmo");
		
		RemoteConfiguration(String row) {
			this.row = row;
		}
		
		String row;
		
		public String toString() { return asString(); }
		public String asString() {
			try {
				return Query.table(SettingsTable.TableName.toString())
					.column("value")
					.condition("key", row)
					.select()
					.get(0)
					.getValue("value");
			} catch (Exception ex) { return ""; }
		}
		public Integer asInteger() { 
			try {
				return Query.table(SettingsTable.TableName.toString())
					.column("value")
					.condition("key", row)
					.select()
					.get(0)
					.getValueAsInteger("value");
			} catch (Exception ex) { return 0; }
		}
		public Boolean asBoolean() {
			try {
				return Query.table(SettingsTable.TableName.toString())
					.column("value")
					.condition("key", row)
					.select()
					.get(0)
					.getValueAsBoolean("value");
			} catch (Exception ex) { return false; }
		}
		
		public boolean update(Object value) {
			return Query.table(SettingsTable.TableName.toString())
				.value("value", value)
				.condition("key", row)
				.update();
		}
	}
	
}
