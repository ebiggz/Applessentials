package com.gmail.erikbigler.applessentials.bossbar;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.gmail.erikbigler.applessentials.utils.Time;


public class BarMessageData {

	private String worldName;
	private int currentMessage = -1;
	private List<String> messages = new ArrayList<String>();
	private int interval;
	private String lastTimeStamp;

	public BarMessageData(String worldName, List<String> messages, int interval) {
		this.worldName = worldName;
		this.messages = messages;
		this.interval = interval;
		for(int i = 0; i < messages.size(); i++) {
			messages.set(i, ChatColor.translateAlternateColorCodes('&', messages.get(i)));
		}
		start();
	}

	private void start() {
		if(messages.size() > 1 && interval > 0) {
			Bukkit.getScheduler().scheduleSyncRepeatingTask(Bukkit.getPluginManager().getPlugin("Applessentials"), new Runnable() {
				@Override
				public void run() {
					World w = Bukkit.getWorld(worldName);
					if(w == null) return;
					currentMessage++;
					if(currentMessage >= messages.size()) {
						currentMessage = 0;
					}
					lastTimeStamp = Time.getTimeWithSecs();
					for(Player player : w.getPlayers()) {
						break;
						//if(Applessentials.getVoteLobby().playerIsInLobby(player)) continue;					
						//BarAPI.setMessage(player, messages.get(currentMessage).replace("%player%", player.getName()), interval);
					}
				}
			}, 0, interval * 20);
		} else {
			currentMessage = 0;
			World w = Bukkit.getWorld(worldName);
			if(w == null) return;
			for(Player player : w.getPlayers()) {
				BarAPI.setMessage(player, messages.get(currentMessage).replace("%player%", player.getName()));
			}
		}
	}

	public int getCurrentMessageIndex() {
		return currentMessage;
	}

	public List<String> getMessages() {
		return messages;
	}

	public int getInterval() {
		return interval;
	}

	public String getWorldName() {
		return worldName;
	}

	public String getLastTimeStamp() {
		return lastTimeStamp;
	}
}
