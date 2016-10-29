package com.gmail.erikbigler.applessentials.toolbox;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.gmail.erikbigler.applessentials.utils.FancyMenu;


public class AdminHelpMenu implements CommandExecutor {

	String[] commandData = {
			"&7&o(Hover over a &a&ocommand &7&ofor info, click to run it)",
			"TELLRAW run>>/channels>>/channels>>See a list of all the channels.",
			"TELLRAW run>>/allchat>>/allchat>>Toggle seeing all chat.",
			"TELLRAW suggest>>/tp [player]>>/tp >>Teleport to a player.",
			"TELLRAW suggest>>/tp here [player]>>/tp here >>Teleport to a player to you.",
			"TELLRAW suggest>>/pi [player]>>/pi >>Info about a player.",
			"TELLRAW run>>/vanish>>/vanish>>Toggle vanish.",
			"TELLRAW suggest>>/warn [player] [reason]>>/warn>>Issue a player a warning.",
			"TELLRAW suggest>>/warninfo [player]>>/warninfo>>See warn information.",
			"TELLRAW suggest>>/tplastknown [player]>>/tplastknown>>Teleport to a player's last known (log off) location."
	};

	String[] channelData = {
			"&7&o(Hover over a &a&ocommand &7&ofor info, click to run it)",
			"TELLRAW run>>/ch g>>/ch g>>Global chat channel.",
			"TELLRAW run>>/ch l>>/ch l>>Local chat channel.",
			"TELLRAW run>>/ch sc>>/ch sc>>Staff chat channel.",
			"TELLRAW run>>/ch c>>/ch c>>Creative chat channel.",
			"TELLRAW run>>/ch ctf>>/ch ctf>>CTF chat channel.",
			"TELLRAW run>>/ch p>>/ch p>>Parkour chat channel.",
			"TELLRAW run>>/ch sb>>/ch sb>>Skyblock chat channel.",
			"TELLRAW run>>/ch s>>/ch s>>Survival chat channel.",
			"TELLRAW run>>/ch sg>>/ch sg>>SurvivalGames chat channel.",
			"TELLRAW run>>/ch w>>/ch w>>Walls chat channel."
	};

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		if(commandLabel.equalsIgnoreCase("channels")) {
			if(args.length == 0) {
				FancyMenu.showClickableCommandList(sender, commandLabel, "Chat Channels", channelData, 1);
			} else {
				try {
					FancyMenu.showClickableCommandList(sender, commandLabel, "Chat Channels", channelData, Integer.parseInt(args[0]));
				} catch (Exception e) {
					sender.sendMessage(ChatColor.RED + "Invalid number!");
				}
			}
		}

		if(commandLabel.equalsIgnoreCase("staff")) {
			if(!sender.hasPermission("applecraft.mod")) {
				sender.sendMessage(ChatColor.RED + "You don't have permission to run this command!");
				return true;
			}
			if(args.length == 0) {
				FancyMenu.showClickableCommandList(sender, commandLabel, "Staff Commands", commandData, 1);
			} else {
				try {
					FancyMenu.showClickableCommandList(sender, commandLabel, "Staff Commands", commandData, Integer.parseInt(args[0]));
				} catch (Exception e) {
					sender.sendMessage(ChatColor.RED + "Invalid number!");
				}
			}
		}

		if(commandLabel.equalsIgnoreCase("com")) {
			if(!sender.hasPermission("applecraft.mod")) {
				sender.sendMessage(ChatColor.RED + "You don't have permission to run this command!");
				return true;
			}
			if(args.length == 0) {
				FancyMenu.showClickableCommandList(sender, commandLabel, "Staff Commands", commandData, 1);
			} else {
				try {
					FancyMenu.showClickableCommandList(sender, commandLabel, "Staff Commands", commandData, Integer.parseInt(args[0]));
				} catch (Exception e) {
					sender.sendMessage(ChatColor.RED + "Invalid number!");
				}
			}
		}
		return true;
	}
}
