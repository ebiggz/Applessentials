package com.gmail.erikbigler.applessentials.bossbar;

import me.confuser.barapi.BarAPI;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import com.gmail.erikbigler.applessentials.Applessentials;
import com.gmail.erikbigler.applessentials.utils.Time;


public class BarMessagePlayerListener implements Listener {

	@EventHandler (priority = EventPriority.MONITOR)
	public void onJoin(PlayerJoinEvent event) {
		String worldName = event.getPlayer().getWorld().getName();
		if(Applessentials.getBarMessageHandler().getWorldMessages().containsKey(worldName)) {
			BarMessageData bmd = Applessentials.getBarMessageHandler().getWorldMessages().get(worldName);
			if(bmd.getLastTimeStamp() == null) {
				BarAPI.setMessage(event.getPlayer(), bmd.getMessages().get(bmd.getCurrentMessageIndex()).replace("%player%", event.getPlayer().getName()));
			} else {
				try {
					int timeSince = Time.compareTimeSecs(bmd.getLastTimeStamp());
					int newInterval = bmd.getInterval() - timeSince;
					BarAPI.setMessage(event.getPlayer(), bmd.getMessages().get(bmd.getCurrentMessageIndex()).replace("%player%", event.getPlayer().getName()), newInterval);
				} catch (Exception e) {
					BarAPI.setMessage(event.getPlayer(), bmd.getMessages().get(bmd.getCurrentMessageIndex()).replace("%player%", event.getPlayer().getName()));
				}
			}
		}
	}

	@EventHandler(priority= EventPriority.MONITOR)
	public void onWorldChange(PlayerChangedWorldEvent event) {
		String worldName = event.getPlayer().getWorld().getName();
		if(Applessentials.getBarMessageHandler().getWorldMessages().containsKey(worldName)) {
			BarMessageData bmd = Applessentials.getBarMessageHandler().getWorldMessages().get(worldName);
			if(bmd.getLastTimeStamp() == null) {
				BarAPI.setMessage(event.getPlayer(), bmd.getMessages().get(bmd.getCurrentMessageIndex()).replace("%player%", event.getPlayer().getName()));
			} else {
				try {
					int timeSince = Time.compareTimeSecs(bmd.getLastTimeStamp());
					int newInterval = bmd.getInterval() - timeSince;
					BarAPI.setMessage(event.getPlayer(), bmd.getMessages().get(bmd.getCurrentMessageIndex()).replace("%player%", event.getPlayer().getName()), newInterval);
				} catch (Exception e) {
					BarAPI.setMessage(event.getPlayer(), bmd.getMessages().get(bmd.getCurrentMessageIndex()).replace("%player%", event.getPlayer().getName()));
				}
			}
		} else {
			if(BarAPI.hasBar(event.getPlayer())) {
				BarAPI.removeBar(event.getPlayer());
			}
		}
	}
}