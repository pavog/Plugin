package com.wolvencraft.yasp.db.data.normal;

public enum DataLabel {
	Player("Player"),
	PlayerDistance("PlayerDistance"),
	Settings("Settings"),
	TotalBlocks("TotalBlocks"),
	TotalDeaths("TotalDeaths"),
	TotalItems("TotalItems"),
	TotalPVE("TotalPVE"),
	TotalPVP("TotalPVP");
	
	DataLabel(String alias) {
		this.alias = alias;
	}
	
	String alias;
	
	public String getAlias() { return alias; }
	public String getAliasParameterized(String param) { return alias + ":" + param; }
	
	public static DataLabel getByAlias(String str) {
		for(DataLabel label : DataLabel.values()) {
			if(str.equals(label.getAlias())) return label;
		}
		return null;
	}
}
