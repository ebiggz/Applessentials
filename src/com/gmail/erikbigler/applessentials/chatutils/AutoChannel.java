package com.gmail.erikbigler.applessentials.chatutils;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import com.dthielke.herochat.Channel;
import com.dthielke.herochat.ConversationChannel;
import com.dthielke.herochat.Herochat;




public class AutoChannel implements Listener {



	@EventHandler(priority= EventPriority.MONITOR)
	public void onWorldChange(PlayerChangedWorldEvent event) {

		Player p = event.getPlayer();

		if(Herochat.getChatterManager().getChatter(p).getActiveChannel() instanceof ConversationChannel) return;

		List<Channel> channels = Herochat.getChannelManager().getChannels();
		for(Channel channel : channels) {
			if(channel.getName().equals("Global") || channel.getName().equals("Local") || channel.getName().equals("StaffChat")) continue;
			if(channel.hasWorld(p.getWorld())) {
				if(channel instanceof ConversationChannel) continue;
				Herochat.getChatterManager().getChatter(p).addChannel(channel, false, true);
				Herochat.getChatterManager().getChatter(p).setActiveChannel(channel, true, true);
				return;
			}
		}
	}
}
