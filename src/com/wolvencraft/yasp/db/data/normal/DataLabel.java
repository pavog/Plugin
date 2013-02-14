package com.wolvencraft.yasp.db.data.normal;

/**
 * Represents different types of DataHandlers.<br />
 * Used to easily fetch the necessary handlers.
 * @author bitWolfy
 *
 */
public enum DataLabel {
	Player("Player"),
	PlayerDistance("PlayerDistance"),
	Settings("Settings"),
	TotalBlocks("TotalBlocks"),
	TotalDeaths("TotalDeaths"),
	TotalItems("TotalItems"),
	TotalPVE("TotalPVE"),
	TotalPVP("TotalPVP");
	
	/**
	 * <b>Default constructor</b><br />
	 * Creates a new DataLabel with the specified alias
	 * @param alias DataLabel alias
	 */
	DataLabel(String alias) {
		this.alias = alias;
	}
	
	String alias;
	
	@Override
	public String toString() { return alias; }
	
	/**
	 * Safely appends parameters to the alias
	 * @param param Parameters to append
	 * @return <b>String</b> Resulting string
	 */
	public String toParameterizedString(String... param) {
		String result = alias;
		for(String str : param) alias += ":" + str;
		return result;
	}
	
	/**
	 * Searches for a DataLabel based on the alias provided
	 * @param alias Alias to search for
	 * @return <b>DataLabel</b> if it exists, <b>null</b> otherwise
	 */
	public static DataLabel getByAlias(String alias) {
		for(DataLabel label : DataLabel.values()) {
			if(alias.equals(label.toString())) return label;
		}
		return null;
	}
}
