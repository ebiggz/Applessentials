package com.gmail.erikbigler.applessentials.chatutils;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.gmail.erikbigler.applessentials.Applessentials;
import com.gmail.erikbigler.applessentials.Slice;


public class Obama implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		if(commandLabel.equalsIgnoreCase("allchat")) {
			if(!sender.hasPermission("applecraft.mod")) {
				sender.sendMessage(ChatColor.RED + "You don't have permission to run this command!");
				return true;
			}
			Slice modSlice = Applessentials.getSliceManager().getSlice(sender.getName());

			if(modSlice.isSeeingAllChat()) {
				modSlice.setSeeAllChat(false);
				sender.sendMessage(ChatColor.GREEN + "You've stopped seeing all chat!");
			} else {
				modSlice.setSeeAllChat(true);
				sender.sendMessage(ChatColor.GREEN + "You are now seeing all chat.");
			}
		}
		return true;
	}
}
