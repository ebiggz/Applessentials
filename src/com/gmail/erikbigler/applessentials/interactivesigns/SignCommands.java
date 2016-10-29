package com.gmail.erikbigler.applessentials.interactivesigns;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import com.gmail.erikbigler.applessentials.Applessentials;
import com.gmail.erikbigler.applessentials.interactivesigns.ISCmdData.CommandType;

public class SignCommands implements CommandExecutor {

	private Applessentials plugin;

	public SignCommands(Applessentials plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		if(commandLabel.equalsIgnoreCase("isign")) {
			if(!sender.hasPermission("applecraft.mod")) {
				sender.sendMessage(ChatColor.RED + "You don't have permission to run this command!");
				return true;
			}
			if(args.length == 0) {
				sender.sendMessage(ChatColor.AQUA + "------Interactive Sign Commands------");
				sender.sendMessage(ChatColor.YELLOW + "/isign add [signName] [command]");
				sender.sendMessage(ChatColor.YELLOW + "/isign remove");
			}
			else if(args.length > 0) {
				if(args[0].equalsIgnoreCase("add")) {
					if(args.length <= 2) {
						sender.sendMessage(ChatColor.RED + "Please include the name of the sign and the command to be run. /isign add [name] [command]");
					}
					else {
						String name = args[1];
						if(Applessentials.getSignManager().nameIsTaken(name)) {
							sender.sendMessage(ChatColor.RED + "This name is already taken, please try again with a different name.");
						} else {
							String command = "";
							for(int i = 2; i < args.length; i++){ //Combine arguments into one string
								command += " " + args[i];
							}
							command = command.substring(1).trim();
							if(command.startsWith("/")) {
								command.replaceFirst("/", "");
							}

							final Player p = (Player) sender;
							Applessentials.signSelectors.put(p, new ISCmdData(name, command, CommandType.ADD));

							p.sendMessage(ChatColor.GREEN + "Please click the sign that you want to make into an Interactive Sign.");

							//cancel button selection after 5 seconds
							BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
							scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
								@Override
								public void run() {
									if(Applessentials.signSelectors.containsKey(p)) {
										Applessentials.signSelectors.remove(p);
										p.sendMessage(ChatColor.GREEN + "Sign selection canceled.");
									}
								}
							}, 100L);
						}
					}
				}
				else if(args[0].equalsIgnoreCase("remove")) {
					final Player p = (Player) sender;
					Applessentials.signSelectors.put(p, new ISCmdData("", "", CommandType.REMOVE));

					p.sendMessage(ChatColor.GREEN + "Please click the sign that you want to remove as an Interactive Sign.");

					//cancel button selection after 5 seconds
					BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
					scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
						@Override
						public void run() {
							if(Applessentials.signSelectors.containsKey(p)) {
								Applessentials.signSelectors.remove(p);
								p.sendMessage(ChatColor.GREEN + "Sign selection canceled.");
							}
						}
					}, 100L);
				}
			}
		}
		return true;
	}
}
