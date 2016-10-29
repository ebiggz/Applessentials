package com.gmail.erikbigler.applessentials.sgvotesystem;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import com.gmail.erikbigler.applessentials.Applessentials;


public class LobbyListener implements Listener {


	/*@EventHandler
	public void onRegionEnter(RegionEnterEvent e) {
		if(e.getRegion().getId().equalsIgnoreCase("VoteLobby")) {
			Applessentials.getVoteLobby().addPlayerToLobby(e.getPlayer());
		}
	}


	@EventHandler
	public void onRegionLeave(RegionLeaveEvent e) {
		if(e.getRegion().getId().equalsIgnoreCase("VoteLobby")) {
			Applessentials.getVoteLobby().removePlayerFromLobby(e.getPlayer());
		}
	}*/

	@EventHandler(priority=EventPriority.MONITOR)
	public void invClick(InventoryClickEvent e) {
		if (!(e.getWhoClicked() instanceof Player)) return;
		Player p = (Player) e.getWhoClicked();
		if (Applessentials.lookingVoteBallot.contains(p)) {
			e.setCancelled(true);
			int slot = e.getSlot();
			ItemStack is = e.getInventory().getItem(slot);
			if(is == null) return;
			String arenaID = is.getItemMeta().getDisplayName().substring(0, 1);
			Applessentials.getVoteLobby().applyVoteTotal(arenaID, p.getName());
			p.closeInventory();
		}
	}

	@EventHandler(priority=EventPriority.HIGHEST)
	public void invClose(InventoryCloseEvent e) {
		if (!(e.getPlayer() instanceof Player)) return;
		Player p = (Player) e.getPlayer();
		if(Applessentials.lookingVoteBallot.contains(p)) {
			Applessentials.lookingVoteBallot.remove(p);
		}
	}

	@EventHandler (priority = EventPriority.MONITOR)
	public void onQuit(PlayerQuitEvent event) {
		if(Applessentials.lookingVoteBallot.contains(event.getPlayer())) {
			Applessentials.lookingVoteBallot.remove(event.getPlayer());
		}
	}
}
