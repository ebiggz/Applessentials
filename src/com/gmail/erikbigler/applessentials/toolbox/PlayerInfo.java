package com.gmail.erikbigler.applessentials.toolbox;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

import ru.tehkode.permissions.bukkit.PermissionsEx;

import com.dthielke.herochat.Herochat;
import com.gmail.erikbigler.applessentials.Applessentials;
import com.gmail.erikbigler.applessentials.Slice;
import com.gmail.erikbigler.applessentials.utils.Time;
import com.gmail.erikbigler.applessentials.utils.Utils;

public class PlayerInfo implements CommandExecutor {
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		if(!sender.hasPermission("applecraft.mod")) {
			sender.sendMessage(ChatColor.RED + "You don't have permission to run this command.");
			return true;
		}

		if(commandLabel.equalsIgnoreCase("playerinfo") || commandLabel.equalsIgnoreCase("pi")) {
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

				Player mod = (Player) sender;
				Inventory i = getPlayerInfoInv(playerName);
				boolean online;
				Player p = Bukkit.getPlayerExact(playerName);
				if(p == null) {
					online = false;
				} else {
					online = true;
				}

				PIMenuData menuData = new PIMenuData(i, mod, playerName, online);
				Applessentials.playerInfoMenus.put(mod, menuData);

				mod.openInventory(i);

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

	@SuppressWarnings("deprecation")
	public String getPermGroupStr(Player player) {
		String[] groups = PermissionsEx.getUser(player).getGroupsNames();
		String groupsStr = "";
		for(int i = 0; i < groups.length; i++) {
			groupsStr = groupsStr + groups[i];
			if(i+1 < groups.length) {
				groupsStr = groupsStr + ", ";
			}
		}
		return groupsStr;
	}

	@SuppressWarnings("deprecation")
	public String getPermGroupStr(String playername) {
		String[] groups = PermissionsEx.getUser(playername).getGroupsNames();
		String groupsStr = "";
		for(int i = 0; i < groups.length; i++) {
			groupsStr = groupsStr + groups[i];
			if(i+1 < groups.length) {
				groupsStr = groupsStr + ", ";
			}
		}
		return groupsStr;
	}

	@SuppressWarnings("deprecation")
	public Inventory getPlayerInfoInv(String playerName) {
		Inventory i = Bukkit.createInventory(null, 9*2, ChatColor.YELLOW + playerName + ChatColor.BLACK + " Info");

		Player p = Bukkit.getPlayerExact(playerName);
		if(p == null) {
			//offline menu
			OfflinePlayer offp = Bukkit.getOfflinePlayer(playerName);
			Slice slice = Applessentials.getSliceManager().getSlice(offp.getName());

			ItemStack details = new ItemStack(Material.NAME_TAG);
			ItemMeta detailsItemMeta = details.getItemMeta();
			detailsItemMeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.UNDERLINE + "Details");
			List<String> detailsLore = new ArrayList<String>();
			detailsLore.add(ChatColor.GOLD + "Online: " + ChatColor.WHITE + offp.isOnline());
			detailsLore.add(ChatColor.GOLD + "Perm Group: " + ChatColor.WHITE + getPermGroupStr(offp.getName()));
			Location loc = slice.getLastKnownLocation();
			if(loc != null) {
				detailsLore.add(ChatColor.GOLD + "Logoff Loc: " + ChatColor.WHITE +  + loc.getBlockX() + ", "+ loc.getBlockY() + ", " + loc.getBlockZ() + ", " + loc.getWorld().getName());
			}
			detailsItemMeta.setLore(detailsLore);
			details.setItemMeta(detailsItemMeta);
			i.setItem(3, details);

			ItemStack clock = new ItemStack(Material.WATCH);
			ItemMeta clockItemMeta = clock.getItemMeta();
			clockItemMeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.UNDERLINE + "Time");
			List<String> clockLore = new ArrayList<String>();
			clockLore.add(ChatColor.GOLD + "First Joined: " + ChatColor.WHITE + Time.dateFromMills(offp.getFirstPlayed()));
			clockLore.add(ChatColor.GOLD + "Last Played: " + ChatColor.WHITE + Time.dateFromMills(offp.getLastPlayed()));
			clockLore.add(ChatColor.GOLD + "Total Playtime: " + ChatColor.WHITE + Time.timeString(slice.getTotalTime()));
			clockItemMeta.setLore(clockLore);
			clock.setItemMeta(clockItemMeta);
			i.setItem(4, clock);

			ItemStack command = new ItemStack(Material.COMMAND);
			ItemMeta commandItemMeta = command.getItemMeta();
			commandItemMeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.UNDERLINE + "Recent Commands");
			List<String> commands = slice.getRecentCommands();
			for(int j = 0; j < commands.size(); j++) {
				commands.set(j, ChatColor.RESET + commands.get(j));
			}
			commandItemMeta.setLore(commands);
			command.setItemMeta(commandItemMeta);
			i.setItem(5, command);

			ItemStack chest = new ItemStack(Material.CHEST);
			ItemMeta chestItemMeta = chest.getItemMeta();
			chestItemMeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.ITALIC + "Player Inventory");
			List<String> chestLore = new ArrayList<String>();
			chestLore.add("*Click To See*");
			chestItemMeta.setLore(chestLore);
			chest.setItemMeta(chestItemMeta);
			i.setItem(12, chest);

			ItemStack enderchest = new ItemStack(Material.ENDER_CHEST);
			ItemMeta enderchestItemMeta = enderchest.getItemMeta();
			enderchestItemMeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.ITALIC + "Player Ender Chest");
			List<String> enderchestLore = new ArrayList<String>();
			enderchestLore.add("*Click To See*");
			enderchestItemMeta.setLore(enderchestLore);
			enderchest.setItemMeta(enderchestItemMeta);
			i.setItem(13, enderchest);

			Wool wool3 = new Wool(DyeColor.RED);
			String banPlayer = "Ban Player";
			String clickToBan = "*Click To Ban*";
			if(offp.isBanned()) {
				wool3 = new Wool(DyeColor.LIME);
				banPlayer = "Unban Player";
				clickToBan = "*Click To Unban*";
			}
			ItemStack ban = wool3.toItemStack(1);
			ItemMeta banItemMeta = ban.getItemMeta();
			ban.setData(wool3);
			banItemMeta.setDisplayName(ChatColor.YELLOW + banPlayer);
			List<String> banLore = new ArrayList<String>();
			banLore.add(clickToBan);
			banItemMeta.setLore(banLore);
			ban.setItemMeta(banItemMeta);
			i.setItem(14, ban);

			return i;
		}

		//player online menu

		Slice slice = Applessentials.getSliceManager().getSlice(p.getName());

		ItemStack details = new ItemStack(Material.NAME_TAG);
		ItemMeta detailsItemMeta = details.getItemMeta();
		detailsItemMeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.UNDERLINE + "Details");
		List<String> detailsLore = new ArrayList<String>();
		detailsLore.add(ChatColor.GOLD + "Online: " + ChatColor.WHITE + p.isOnline());
		String ip = p.getAddress().toString().replace("/", "");
		if(ip.contains(":")) {
			int index = ip.indexOf(":");
			ip = ip.substring(0, index - 1);
		}
		detailsLore.add(ChatColor.GOLD + "IP: " + ChatColor.WHITE + ip);
		detailsLore.add(ChatColor.GOLD + "Chat Channel: " + ChatColor.WHITE + Herochat.getChatterManager().getChatter(p).getActiveChannel().getName().replace(p.getName(), " with "));
		detailsLore.add(ChatColor.GOLD + "Perm Group: " + ChatColor.WHITE + getPermGroupStr(p.getName()));
		detailsItemMeta.setLore(detailsLore);
		details.setItemMeta(detailsItemMeta);
		i.setItem(2, details);

		ItemStack clock = new ItemStack(Material.WATCH);
		ItemMeta clockItemMeta = clock.getItemMeta();
		clockItemMeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.UNDERLINE + "Time");
		List<String> clockLore = new ArrayList<String>();
		clockLore.add(ChatColor.GOLD + "First Joined: " + ChatColor.WHITE + Time.dateFromMills(p.getFirstPlayed()));
		clockLore.add(ChatColor.GOLD + "Last Played: " + ChatColor.WHITE + Time.dateFromMills(p.getLastPlayed()));
		clockLore.add(ChatColor.GOLD + "Online For: " + ChatColor.WHITE + Time.timeString(slice.getPlayTime()));
		clockLore.add(ChatColor.GOLD + "Total Playtime: " + ChatColor.WHITE + Time.timeString(slice.getPlayTime() + slice.getTotalTime()));
		clockItemMeta.setLore(clockLore);
		clock.setItemMeta(clockItemMeta);
		i.setItem(3, clock);

		ItemStack compass = new ItemStack(Material.COMPASS);
		ItemMeta compassItemMeta = compass.getItemMeta();
		compassItemMeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.UNDERLINE + "Location");
		List<String> compassLore = new ArrayList<String>();
		compassLore.add(ChatColor.GOLD + "Coords: " + ChatColor.WHITE + "X:" + (int)(p.getLocation().getX()) + " Y:" + (int)(p.getLocation().getY()) + " Z:" + (int)(p.getLocation().getZ()));
		compassLore.add(ChatColor.GOLD + "World: " + ChatColor.WHITE + p.getWorld().getName());
		compassItemMeta.setLore(compassLore);
		compass.setItemMeta(compassItemMeta);
		i.setItem(4, compass);

		ItemStack apple = new ItemStack(Material.APPLE);
		ItemMeta appleItemMeta = apple.getItemMeta();
		appleItemMeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.UNDERLINE + "Stats");
		List<String> appleLore = new ArrayList<String>();
		appleLore.add(ChatColor.GOLD + "Health: " + ChatColor.WHITE + p.getHealth()/2 + "/10");
		appleLore.add(ChatColor.GOLD + "Food: " + ChatColor.WHITE + p.getFoodLevel()/2 + "/10");
		appleLore.add(ChatColor.GOLD + "XP Levels: " + ChatColor.WHITE + p.getLevel());
		appleLore.add(ChatColor.GOLD + "Balance: " + ChatColor.WHITE + "$" + Applessentials.economy.getBalance(p.getName()));
		appleItemMeta.setLore(appleLore);
		apple.setItemMeta(appleItemMeta);
		i.setItem(5, apple);

		ItemStack command = new ItemStack(Material.COMMAND);
		ItemMeta commandItemMeta = command.getItemMeta();
		commandItemMeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.UNDERLINE + "Recent Commands");
		List<String> commands = slice.getRecentCommands();
		for(int j = 0; j < commands.size(); j++) {
			commands.set(j, ChatColor.RESET + commands.get(j));
		}
		commandItemMeta.setLore(commands);
		command.setItemMeta(commandItemMeta);
		i.setItem(6, command);

		ItemStack pearl = new ItemStack(Material.ENDER_PEARL);
		ItemMeta pearlItemMeta = pearl.getItemMeta();
		pearlItemMeta.setDisplayName(ChatColor.YELLOW + "Teleport To Player");
		List<String> pearlLore = new ArrayList<String>();
		pearlLore.add("*Click To TP*");
		pearlLore.add(ChatColor.GRAY + "(and auto vanish)");
		pearlItemMeta.setLore(pearlLore);
		pearl.setItemMeta(pearlItemMeta);
		i.setItem(10, pearl);

		ItemStack eye = new ItemStack(Material.EYE_OF_ENDER);
		ItemMeta eyeItemMeta = eye.getItemMeta();
		eyeItemMeta.setDisplayName(ChatColor.YELLOW + "Teleport Player Here");
		List<String> eyeLore = new ArrayList<String>();
		eyeLore.add("*Click To TP*");
		eyeItemMeta.setLore(eyeLore);
		eye.setItemMeta(eyeItemMeta);
		i.setItem(11, eye);

		ItemStack chest = new ItemStack(Material.CHEST);
		ItemMeta chestItemMeta = chest.getItemMeta();
		chestItemMeta.setDisplayName(ChatColor.YELLOW + "Player Inventory");
		List<String> chestLore = new ArrayList<String>();
		chestLore.add("*Click To See*");
		chestItemMeta.setLore(chestLore);
		chest.setItemMeta(chestItemMeta);
		i.setItem(12, chest);

		ItemStack enderchest = new ItemStack(Material.ENDER_CHEST);
		ItemMeta enderchestItemMeta = enderchest.getItemMeta();
		enderchestItemMeta.setDisplayName(ChatColor.YELLOW + "Player Ender Chest");
		List<String> enderchestLore = new ArrayList<String>();
		enderchestLore.add("*Click To See*");
		enderchestItemMeta.setLore(enderchestLore);
		enderchest.setItemMeta(enderchestItemMeta);
		i.setItem(13, enderchest);

		Wool wool = new Wool(DyeColor.GRAY);
		String mutePlayer = "Mute Player";
		String clickToMute = "*Click To Mute*";
		if(Herochat.getChatterManager().getChatter(p).isMuted()) {
			wool = new Wool(DyeColor.SILVER);
			mutePlayer = "Unmute Player";
			clickToMute = "*Click To Unmute*";
		}
		ItemStack mute = wool.toItemStack(1);
		ItemMeta muteItemMeta = mute.getItemMeta();
		mute.setData(wool);
		muteItemMeta.setDisplayName(ChatColor.YELLOW + mutePlayer);
		List<String> muteLore = new ArrayList<String>();
		muteLore.add(clickToMute);
		muteItemMeta.setLore(muteLore);
		mute.setItemMeta(muteItemMeta);
		i.setItem(14, mute);

		Wool wool2 = new Wool(DyeColor.YELLOW);
		ItemStack kick = wool2.toItemStack(1);
		ItemMeta kickItemMeta = kick.getItemMeta();
		kickItemMeta.setDisplayName(ChatColor.YELLOW + "Kick Player");
		List<String> kickLore = new ArrayList<String>();
		kickLore.add("*Click To Kick*");
		kickItemMeta.setLore(kickLore);
		kick.setItemMeta(kickItemMeta);
		i.setItem(15, kick);



		Wool wool3 = new Wool(DyeColor.RED);
		String banPlayer = "Ban Player";
		String clickToBan = "*Click To Ban*";
		if(p.isBanned()) {
			wool3 = new Wool(DyeColor.LIME);
			banPlayer = "Unban Player";
			clickToBan = "*Click To Unban*";
		}
		ItemStack ban = wool3.toItemStack(1);
		ItemMeta banItemMeta = ban.getItemMeta();
		ban.setData(wool3);
		banItemMeta.setDisplayName(ChatColor.YELLOW + banPlayer);
		List<String> banLore = new ArrayList<String>();
		banLore.add(clickToBan);
		banItemMeta.setLore(banLore);
		ban.setItemMeta(banItemMeta);
		i.setItem(16, ban);

		return i;
	}
}
