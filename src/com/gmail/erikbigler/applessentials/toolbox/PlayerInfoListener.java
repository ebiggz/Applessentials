package com.gmail.erikbigler.applessentials.toolbox;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.gmail.erikbigler.applessentials.Applessentials;


public class PlayerInfoListener implements Listener {

	@EventHandler(priority=EventPriority.MONITOR)
	public void invClick(InventoryClickEvent e) {
		if(!(e.getWhoClicked() instanceof Player)) return;
		Player p = (Player) e.getWhoClicked();
		if(Applessentials.serverInfoMenus.contains(p)) {
			e.setCancelled(true);
			return;
		}
		if(Applessentials.playerInfoMenus.containsKey(p)) {
			String primaryGroup = Applessentials.permission.getPrimaryGroup("", p.getName());
			PIMenuData data = Applessentials.playerInfoMenus.get(p);
			if(!data.getInventory().contains(e.getCurrentItem())) return;
			e.setCancelled(true);
			String playerTarget = data.getTarget();
			int slot = e.getSlot();
			if(data.isTargetIsOnline()) {
				if(slot == 10) {
					p.closeInventory();
					p.performCommand("v on");
					p.performCommand("tp " + playerTarget);
				}
				else if(slot == 11) {
					p.closeInventory();
					p.performCommand("tp here " + playerTarget);
				}
				else if(slot == 12) {
					p.closeInventory();
					p.performCommand("inv " + playerTarget);
				}
				else if(slot == 13) {
					p.closeInventory();
					p.performCommand("openender " + playerTarget);
				}
				else if(slot == 14) {
					p.closeInventory();
					p.performCommand("ch mute " + playerTarget);
				}
				else if(slot == 15) {
					p.closeInventory();
					p.performCommand("kick " + playerTarget);
				}
				else if(slot == 16) {
					p.closeInventory();
					if(e.getCurrentItem().getItemMeta().getDisplayName().toLowerCase().contains("unban")) {
						p.performCommand("unban " + playerTarget);
					} else {
						if(primaryGroup.equalsIgnoreCase("Mod")) {
							p.performCommand("tempban " + playerTarget + " 1h");
						} else {
							p.performCommand("ban " + playerTarget + " Make an issue to appeal ban");
						}
					}
				}
			} else {
				if(slot == 12) {
					p.closeInventory();
					p.performCommand("inv " + playerTarget);
				}
				else if(slot == 13) {
					p.closeInventory();
					p.performCommand("openender " + playerTarget);
				}
				else if(slot == 14) {
					p.closeInventory();
					if(e.getCurrentItem().getItemMeta().getDisplayName().toLowerCase().contains("unban")) {
						p.performCommand("unban " + playerTarget);
					} else {
						p.performCommand("ban " + playerTarget + " Make an issue to appeal ban");
					}
				}
			}
		}
	}

	@EventHandler(priority=EventPriority.HIGHEST)
	public void invClose(InventoryCloseEvent e) {
		if (!(e.getPlayer() instanceof Player)) return;
		Player p = (Player) e.getPlayer();
		if(Applessentials.playerInfoMenus.containsKey(p)) {
			Applessentials.playerInfoMenus.remove(p);
		}
		if(Applessentials.serverInfoMenus.contains(p)) {
			Applessentials.serverInfoMenus.remove(p);
		}
	}

	@EventHandler (priority = EventPriority.MONITOR)
	public void onQuit(PlayerQuitEvent event) {
		if(Applessentials.playerInfoMenus.containsKey(event.getPlayer())) {
			Applessentials.playerInfoMenus.remove(event.getPlayer());
		}
		if(Applessentials.serverInfoMenus.contains(event.getPlayer())) {
			Applessentials.serverInfoMenus.remove(event.getPlayer());
		}
	}
}