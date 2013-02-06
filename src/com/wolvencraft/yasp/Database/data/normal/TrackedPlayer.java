package com.wolvencraft.yasp.Database.data.normal;

import org.bukkit.entity.Player;

public class TrackedPlayer implements DataHolder {
	
	public TrackedPlayer (Player player) {
		this.playerId = -1;
		this.playerName = player.getPlayerListName();
		this.online = true;
		this.expPercent = player.getExp();
		this.expTotal = player.getTotalExperience();
		this.expLevel = player.getLevel();
		this.foodLevel = player.getFoodLevel();
		this.healthLevel = player.getHealth();
		this.logins = 0;
		this.deaths = 0;
	}
	
	private int playerId;
	private String playerName;
	private boolean online;
	private double expPercent;
	private int expTotal;
	private int expLevel;
	private int foodLevel;
	private int healthLevel;
	private long firstJoin;
	private int logins;
	private int deaths;
	
	@Override
	public void fetchData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pushData() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String getDataHolderName() { return "TrackedPlayer:" + playerName; }
	

}
