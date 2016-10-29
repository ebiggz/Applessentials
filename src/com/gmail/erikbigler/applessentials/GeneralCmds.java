package com.gmail.erikbigler.applessentials;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class GeneralCmds implements CommandExecutor {

	private Applessentials plugin;

	public GeneralCmds(Applessentials plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		if(commandLabel.equalsIgnoreCase("colors")) {
			sender.sendMessage(ChatColor.WHITE + "Colors:" + ChatColor.BLACK + " &0"  + ChatColor.DARK_BLUE + " &1" + ChatColor.DARK_GREEN + " &2" + ChatColor.DARK_AQUA + " &3" + ChatColor.DARK_RED + " &4" + ChatColor.DARK_PURPLE + " &5" + ChatColor.GOLD + " &6" + ChatColor.GRAY + " &7" + ChatColor.DARK_GRAY + " &8" + ChatColor.BLUE + " &9" + ChatColor.GREEN + " &a" + ChatColor.AQUA + " &b" + ChatColor.RED + " &c" + ChatColor.LIGHT_PURPLE + " &d" + ChatColor.YELLOW + " &e" + ChatColor.WHITE + " &f");
			sender.sendMessage(ChatColor.WHITE + "Formats:" + ChatColor.BOLD + " &l" + ChatColor.RESET + " " + ChatColor.STRIKETHROUGH + "&m" + ChatColor.RESET + " " + ChatColor.UNDERLINE + "&n" + ChatColor.RESET + " " + ChatColor.ITALIC + "&o"  + ChatColor.RESET + " &r(reset)");
		}
		else if(commandLabel.equalsIgnoreCase("applecraft")) {
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("reload")) {
					if(!sender.hasPermission("applecraft.admin")) {
						sender.sendMessage(ChatColor.RED + "You don't have permission to run this command.");
					}
					plugin.loadFilesAndData();
					sender.sendMessage(ChatColor.GREEN + "[AppleCraft] Files reloaded!");
				}
			}
		}
		else if(commandLabel.equalsIgnoreCase("speedtest")) {
			Player p = (Player) sender;
			p.setWalkSpeed(1.0F);
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100000, 2, true));
		}
		return true;
	}
}
