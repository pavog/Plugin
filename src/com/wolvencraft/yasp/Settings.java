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

package com.wolvencraft.yasp;

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
		LogPrefix("log-prefix");
		
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
		public String toString() { return asString(); }
		public String asString() { return value == null ? StatsPlugin.getInstance().getConfig().getString(node) : (String) value; }
		public Boolean asBoolean() { return value == null ? StatsPlugin.getInstance().getConfig().getBoolean(node) : (Boolean) value; }
		public Integer asInteger() { return value == null ? StatsPlugin.getInstance().getConfig().getInt(node) : (Integer) value; }
	}
	
	public enum Modules {
		Blocks("module.blocks"),
		Items("module.items"),
		Deaths("module.deaths"),
		
		HookVault("hook.vault"),
		HookWorldGuard("hook.worldguard"),
		HookJobs("hook.jobs"),
		HookMcMMO("hook.mcmmo");;
		
		Modules(String row) { this.row = row;}
		
		String row;
		
		public boolean getEnabled() {
			try {
				return Query.table(SettingsTable.TableName.toString())
					.column("value")
					.condition("key", row)
					.selectAll()
					.get(0)
					.getValueAsBoolean("value");
			} catch (Exception ex) { return true; }
		}
	}
	
	/**
	 * Represents modules that are currently active.
	 * @author bitWolfy
	 *
	 */
	public enum ActiveHooks {
		HookVault,
		HookWorldGuard,
		HookJobs,
		HookMcMMO;
		
		ActiveHooks() { active = false; }
		
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
		LogDelay("log_delay"),
		ShowWelcomeMessages("show_welcome_messages"),
		WelcomeMessage("welcome_message"),
		ShowFirstJoinMessages("show_first_join_messages"),
		FirstJoinMessage("first_join_message");
		
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
					.selectAll()
					.get(0)
					.getValue("value");
			} catch (Exception ex) { return ""; }
		}
		public Integer asInteger() { 
			try {
				return Query.table(SettingsTable.TableName.toString())
					.column("value")
					.condition("key", row)
					.selectAll()
					.get(0)
					.getValueAsInteger("value");
			} catch (Exception ex) { return 0; }
		}
		public Boolean asBoolean() {
			try {
				return Query.table(SettingsTable.TableName.toString())
					.column("value")
					.condition("key", row)
					.selectAll()
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
	
	/**
	 * A hard-coded list of items that have a metadata that should be tracked
	 * @author bitWolfy
	 *
	 */
	public enum ItemsWithMetadata {
		Plank(5),
		Sapling(6),
		Log(17),
		Leave(18),
		Sandstone(24),
		TallGrass(31),
		Wool(35),
		DoubleSlab(43),
		Slab(44),
		PlankDoubleSlab(125),
		PlankSlab(126),
		MobHeadBlock(144),
		Quartz(155),
		GoldenApple(322),
		Dye(351),
		Potion(373),
		MobEgg(383),
		MobHead(397);
		
		ItemsWithMetadata(int itemId){
			this.itemId = itemId;
		}
		
		int itemId;
		
		private int getId() { return itemId; }
		
		/**
		 * Checks if the specified ID is in the list
		 * @param id Item ID
		 * @return <b>true</b> if the item is in the list, <b>false</b> otherwise
		 */
		public static boolean checkAgainst(int id) {
			for(ItemsWithMetadata entry : ItemsWithMetadata.values()) {
				if(entry.getId() == id) return true;
			}
			return false;
		}
	}
	
}
