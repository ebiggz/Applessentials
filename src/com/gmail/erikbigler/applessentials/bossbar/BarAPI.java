package com.gmail.erikbigler.applessentials.bossbar;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.gmail.erikbigler.applessentials.Applessentials;

public class BarAPI implements Listener {
	
	private static HashMap<UUID, BossBar> players = new HashMap<UUID, BossBar>();
	private static HashMap<UUID, Integer> timers = new HashMap<UUID, Integer>();
	
	private static BarAPI instance;
	
	public static BarAPI getInstance() {
		if(instance == null) {
			instance = new BarAPI();
		}
		return instance;
	}
	
	public BarAPI() {
		if(instance != null) {
			throw new IllegalStateException("BarAPI already exists, can't construct a second one.");			
		}
		instance = this;
	}
	public static void setMessage(Player player, String message) {
		if(hasBar(player)) {
			removeBar(player);
		}
		    
	    BossBar bar = Bukkit.createBossBar(message, BarColor.BLUE, BarStyle.SOLID);
	    players.put(player.getUniqueId(), bar);
	    
	    //show to player
	    bar.addPlayer(player);
	}
	public static void setMessage(Player player, String message, int seconds) {
		
		setMessage(player, message);
	    
	    //create a timer to remove bar after x seconds
	    timers.put(player.getUniqueId(), 
	    		Integer.valueOf(Bukkit.getScheduler().runTaskLaterAsynchronously(
		    		Applessentials.getPlugin(), 
		    		new Runnable() {
		    			
		    			private Player player;
		    			
		    			private Runnable init(Player player){
		    				this.player = player;
		    				return this;
		    			}
		    			
				        public void run() {
				        	removeBar(player);
				        }
					      
					}.init(player), seconds * 20L).getTaskId()
	    		)
	    	);	   
	  }
	
	public static void removeBar(Player player) {
		BossBar bar = players.remove(player.getUniqueId());
		if(bar != null) {
			bar.removePlayer(player);
		}
		cancelTimer(player);
	}
	
	public static void removeAllBars() {
		for(UUID id: players.keySet()) {
			//remove bar and remove players from bar
			players.remove(id).removeAll();
			
			//remove and cancel timer
			Bukkit.getScheduler().cancelTask(timers.remove(id));
		}
	}
	
	public static BossBar getBar(Player player) {
		return players.get(player.getUniqueId());
	}
	
	public static boolean hasBar(Player player) {
		return players.containsKey(player.getUniqueId());
	}
	
	public static void cancelTimer(Player player) {
		Integer taskId = timers.remove(player.getUniqueId());
		if(taskId != null) {
			Bukkit.getScheduler().cancelTask(taskId);
		}
	}
}
