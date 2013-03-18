package com.wolvencraft.yasp.db.data.sync;

import com.wolvencraft.yasp.StatsPlugin;
import com.wolvencraft.yasp.db.Query;
import com.wolvencraft.yasp.db.tables.Normal.SettingsTable;

public class Settings {
	
	public enum LocalConfiguration {
		Debug("debug"),
		DBHost("database.host"),
		DBPort("database.port"),
		DBName("database.name"),
		DBUser("database.user"),
		DBPass("database.pass"),
		DBPrefix("database.prefix"),
		DBConnect("", "jdbc:mysql://" + DBHost.asString() + ":" + DBPort.asInteger() + "/" + DBName.asString()),
		LogPrefix("", StatsPlugin.getInstance().getDescription().getPrefix());
		
		LocalConfiguration(String node) {
			this.value = StatsPlugin.getInstance().getConfig().get(node);
		}
		
		LocalConfiguration(String node, Object value) {
			this.value = value;
		}
		
		Object value;
		
		@Override
		public String toString() { return (String) value; }
		public String asString() { return (String) value; }
		public Boolean asBoolean() { return (Boolean) value; }
		public Integer asInteger() { return (Integer) value; }
	}
	
	public enum Hooks {
		Vault,
		WorldGuard,
		Jobs,
		McMMO;
		
		Hooks() { active = false; }
		
		boolean active;
		
		public boolean getActive() { return active; }
		public void setActive(boolean active) { this.active = active; }
	}
	
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
