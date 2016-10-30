package com.gmail.erikbigler.applessentials.chatutils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitScheduler;

import com.dthielke.herochat.ChannelChatEvent;
import com.dthielke.herochat.Chatter;
import com.dthielke.herochat.ConversationChannel;
import com.dthielke.herochat.Herochat;
import com.gmail.erikbigler.applessentials.Applessentials;
import com.gmail.erikbigler.applessentials.Slice;
import com.gmail.erikbigler.applessentials.utils.Utils;


public class NSA implements Listener {

	private Applessentials plugin;

	public NSA (Applessentials plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority= EventPriority.MONITOR)
	public void onChannelChat(ChannelChatEvent event) {

		if(event.getChannel() instanceof ConversationChannel) return;

		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		scheduler.runTaskAsynchronously(plugin, new Runnable() {

			private ChannelChatEvent event;
			@Override
			public void run() {
				Player[] onlinePlayers = Utils.getOnlinePlayers();
				for(Player player : onlinePlayers) {
					Slice slice = Applessentials.getSliceManager().getSlice(player.getName());
					if(slice.isSeeingAllChat()) {
						Chatter chatter = Herochat.getChatterManager().getChatter(player);
						if(chatter.getActiveChannel() != event.getChannel()) {
							if(!event.getChannel().hasWorld(player.getWorld())) {
								player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "(In " + event.getChannel().getName() + ") " + event.getSender().getName() + ": " + event.getMessage());
							}
							else if(event.getChannel().getDistance() > 0) {
								if(!event.getSender().isInRange(chatter, event.getChannel().getDistance())) {
									player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "(In " + event.getChannel().getName() + ") " + event.getSender().getName() + ": " + event.getMessage());
								}
							}

						} else {
							if(event.getChannel().getDistance() > 0) {
								if(!event.getSender().isInRange(chatter, event.getChannel().getDistance())) {
									player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "(In " + event.getChannel().getName() + ") " + event.getSender().getName() + ": " + event.getMessage());
								}
							}
						}
					}
				}
			}
			private Runnable init(ChannelChatEvent event){
				this.event = event;
				return this;
			}
		}.init(event));
	}
}