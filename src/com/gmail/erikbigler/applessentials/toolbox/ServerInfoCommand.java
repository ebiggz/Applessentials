package com.gmail.erikbigler.applessentials.toolbox;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.erikbigler.applessentials.Applessentials;
import com.gmail.erikbigler.applessentials.utils.Utils;


public class ServerInfoCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		if(commandLabel.equalsIgnoreCase("si") || commandLabel.equalsIgnoreCase("serverinfo")) {
			if(!sender.hasPermission("applecraft.admin")) {
				sender.sendMessage(ChatColor.RED + "[AppleCraft] You don't have permission to run this command.");
				return true;
			}

			Player mod = (Player) sender;
			Applessentials.serverInfoMenus.add(mod);
			mod.openInventory(Utils.getServerInfoInv());

		}
		return true;
	}
}
