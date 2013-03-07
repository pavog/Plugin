package com.wolvencraft.yasp.hooks;

import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;

import com.wolvencraft.yasp.StatsPlugin;
import com.wolvencraft.yasp.db.data.Settings;
import com.wolvencraft.yasp.util.Message;

import me.zford.jobs.bukkit.JobsPlugin;
import me.zford.jobs.bukkit.PlayerManager;
import me.zford.jobs.container.JobsPlayer;

public class JobsHook implements PluginHook {
	
	public JobsHook() {
		ServicesManager svm = StatsPlugin.getInstance().getServer().getServicesManager();
		boolean broken = false;
		
		try { jobs = ((RegisteredServiceProvider<JobsPlugin>)(svm.getRegistration(JobsPlugin.class))).getProvider();}
		catch(Exception ex) { Message.log(Level.SEVERE, "An error occurred while initializing Jobs"); broken = true; }
		
		if(!broken) {
			Settings.setUsingVault(true);
			playerManager = jobs.getPlayerManager();
		}
	}
	
	private static JobsPlugin jobs;
	private static PlayerManager playerManager;
	
	public String getJobProgression(Player player, String jobName) {
		JobsPlayer jPlayer = playerManager.getJobsPlayer(player.getPlayerListName());
		
		return jPlayer.getDisplayHonorific();
	}
	
	@Override
	public Object getPluginInstance() {
		return jobs;
	}

	@Override
	public void disable() {
		jobs = null;
	}

}
