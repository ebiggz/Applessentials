package com.gmail.erikbigler.applessentials.toolbox;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.gmail.erikbigler.applessentials.Applessentials;
import com.gmail.erikbigler.applessentials.Slice;
import com.gmail.erikbigler.applessentials.utils.Utils;


public class MiscCmds implements CommandExecutor {
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(commandLabel.equalsIgnoreCase("tplastknown")) {
			if(!sender.hasPermission("applecraft.mod")) {
				sender.sendMessage(ChatColor.RED + "You don't have permission to run this command.");
				return true;
			}
			if(args.length == 0) {
				sender.sendMessage(ChatColor.RED + "Please type in a player's name.");
				return true;
			}
			if(args.length == 1) {

				String playerName = completeName(args[0]);
				if(playerName == null) {
					sender.sendMessage(ChatColor.RED + "Not a known player.");
					return true;
				}

				Slice slice = Applessentials.getSliceManager().getSlice(playerName);
				Location loc = slice.getLastKnownLocation();
				if(loc == null) {
					sender.sendMessage(ChatColor.RED + "Could not find last known location for " + playerName + "!");
				} else {
					Player mod = (Player) sender;
					mod.teleport(loc);
					sender.sendMessage(ChatColor.GREEN + "[AppleCraft] " + " You teleported to " + playerName + "'s last known location.");
				}
			}
		}


		else if(commandLabel.equalsIgnoreCase("slap")) {
			if(!sender.hasPermission("applecraft.owner")) {
				sender.sendMessage(ChatColor.RED + "You don't have permission to run this command.");
				return true;
			}

			if(args.length == 0) {
				sender.sendMessage(ChatColor.RED + "Please type in a player's name.");
				return true;
			}
			if(args.length == 1) {

				String playerName = completeName(args[0]);
				if(playerName == null) {
					sender.sendMessage(ChatColor.RED + "Not a known player.");
					return true;
				}
				Player victim = (Player) Bukkit.getPlayerExact(playerName);

				victim.setVelocity(new Vector(1.5, 1, 0.0));
				victim.sendMessage(ChatColor.YELLOW + "*SLAP*");

			}
		}
		return true;
	}

	public String completeName(String playername) {
		Player[] onlinePlayers = Utils.getOnlinePlayers();
		for(int i = 0; i < onlinePlayers.length; i++) {
			if(onlinePlayers[i].getName().toLowerCase().startsWith(playername.toLowerCase())) {
				return onlinePlayers[i].getName();
			}
		}
		OfflinePlayer[] offlinePlayers = Bukkit.getOfflinePlayers();
		for(int i = 0; i < offlinePlayers.length; i++) {
			if(offlinePlayers[i].getName().toLowerCase().startsWith(playername.toLowerCase())) {
				return offlinePlayers[i].getName();
			}
		}
		return null;
	}

}
