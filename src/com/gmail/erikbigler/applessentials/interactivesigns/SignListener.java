package com.gmail.erikbigler.applessentials.interactivesigns;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.gmail.erikbigler.applessentials.Applessentials;
import com.gmail.erikbigler.applessentials.interactivesigns.ISCmdData.CommandType;


public class SignListener implements Listener {


	@EventHandler (priority = EventPriority.MONITOR)
	public void onSignBreak(BlockBreakEvent event) {
		if(event.getBlock().getType() != Material.SIGN_POST && event.getBlock().getType() != Material.WALL_SIGN) return;
		InteractiveSign is = Applessentials.getSignManager().getSignAtLocation(event.getBlock().getLocation());
		if(is == null) return;
		if(event.getPlayer().hasPermission("applecraft.mod")) {
			event.getPlayer().sendMessage(ChatColor.RED + "Interactive Sign break blocked. Please deregister this sign by typing \"/isign remove\" first.");
		} else {
			event.getPlayer().sendMessage(ChatColor.RED + "You are not allowed to break this Interactive Sign!");
		}
		event.setCancelled(true);
	}


	@EventHandler (priority = EventPriority.MONITOR)
	public void onSignInteract(PlayerInteractEvent event) {
		if(event.getClickedBlock() == null) return;
		if(event.getClickedBlock().getType() != Material.SIGN_POST && event.getClickedBlock().getType() != Material.WALL_SIGN) return;
		InteractiveSign is = Applessentials.getSignManager().getSignAtLocation(event.getClickedBlock().getLocation());
		if(Applessentials.signSelectors.containsKey(event.getPlayer())) {
			ISCmdData iscd = Applessentials.signSelectors.get(event.getPlayer());
			Applessentials.signSelectors.remove(event.getPlayer());
			if(iscd.getType() == CommandType.REMOVE) {
				if(is != null) {
					Applessentials.getSignManager().removeSign(is);
					event.getPlayer().sendMessage(ChatColor.GREEN + "You have unregistered this Interactive Sign.");
				} else {
					event.getPlayer().sendMessage(ChatColor.RED + "That is not a registered Interactive Sign!");
				}
			}
			else if(iscd.getType() == CommandType.ADD) {
				if(is != null) {
					event.getPlayer().sendMessage(ChatColor.RED + "That is already an InteractiveSign!");
				} else {
					Applessentials.getSignManager().addSign(iscd.getName(), event.getClickedBlock().getLocation(), iscd.getCommand());
					event.getPlayer().sendMessage(ChatColor.GREEN + "You have registered this as an Interactive Sign with the command: " + ChatColor.GRAY + iscd.getCommand());
				}
			}
		} else {
			if(is == null) return;
			event.getPlayer().performCommand(is.getCommand());
		}
	}
}
