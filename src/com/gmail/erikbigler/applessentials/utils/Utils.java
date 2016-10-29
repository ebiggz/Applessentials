package com.gmail.erikbigler.applessentials.utils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.gmail.erikbigler.applessentials.Applessentials;
import com.gmail.erikbigler.applessentials.Slice;


public class Utils {

	public static Location stringToLocation(String locString) {
		String[] locData = locString.split(",");
		return new Location(Bukkit.getWorld(locData[0]), Double.parseDouble(locData[1]), Double.parseDouble(locData[2]), Double.parseDouble(locData[3]));
	}

	public static String locationToString(Location location) {
		return location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ();
	}

	public static boolean playerIsHideExcempt(Player player) {
		if(player.hasPermission("applecraft.mod") || player.hasPermission("applecraft.youtuber")) {
			return true;
		}
		return false;
	}

	public static Inventory getServerInfoInv() {

		Inventory i = Bukkit.createInventory(null, 9, ChatColor.BLACK + "Server Info");

		ItemStack command = new ItemStack(Material.COMMAND);
		ItemMeta commandItemMeta = command.getItemMeta();
		commandItemMeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.UNDERLINE + "Server Stats");
		List<String> ramStats = new ArrayList<String>();
		long totalRam = Runtime.getRuntime().totalMemory()/1000000000;
		long freeRam = Runtime.getRuntime().freeMemory()/1000000000;
		long usedRam = totalRam - freeRam;
		ramStats.add(ChatColor.GOLD + "TPS: " + ChatColor.WHITE + Applessentials.getTPS());
		ramStats.add(ChatColor.GOLD + "Total RAM: " + ChatColor.WHITE + totalRam + "GB");
		ramStats.add(ChatColor.GOLD + "Free RAM: " + ChatColor.WHITE + freeRam + " GB");
		ramStats.add(ChatColor.GOLD + "Used RAM: " + ChatColor.WHITE + usedRam + " GB");
		/*try {
			double cpuUsage = getProcessCpuLoad();
			ramStats.add(ChatColor.GOLD + "CPU Usage: " + ChatColor.WHITE + cpuUsage + "%");
		} catch (Exception e) {

		}*/
		commandItemMeta.setLore(ramStats);
		command.setItemMeta(commandItemMeta);
		i.setItem(2, command);

		ItemStack compass = new ItemStack(Material.COMPASS);
		ItemMeta compassItemMeta = compass.getItemMeta();
		compassItemMeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.UNDERLINE + "World Stats");
		List<String> compassLore = new ArrayList<String>();
		compassLore.add(ChatColor.GOLD + "World Count: " + ChatColor.WHITE + Bukkit.getWorlds().size());
		int totalChunks = 0;
		int totalEnts = 0;
		for(World world : Bukkit.getWorlds()) {
			totalChunks += world.getLoadedChunks().length;
			totalEnts += world.getEntities().size();
		}
		compassLore.add(ChatColor.GOLD + "Chunks (combined): " + ChatColor.WHITE + totalChunks);
		compassLore.add(ChatColor.GOLD + "Entities (combined): " + ChatColor.WHITE + totalEnts);
		compassItemMeta.setLore(compassLore);
		compass.setItemMeta(compassItemMeta);
		i.setItem(3, compass);

		ItemStack clock = new ItemStack(Material.WATCH);
		ItemMeta clockItemMeta = clock.getItemMeta();
		clockItemMeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.UNDERLINE + "Time Stats");
		List<String> clockLore = new ArrayList<String>();
		ConfigAccessor statsData = new ConfigAccessor("stats.yml");
		String startTime = statsData.getConfig().getString("server-start-timestamp", "");
		if(startTime.isEmpty()) {
			clockLore.add(ChatColor.GOLD + "Runtime: " + ChatColor.WHITE + "Error getting runtime :(");
		} else {
			String timeString;
			try {
				timeString = Time.timeString(Time.compareTimeSecs(startTime)*1000);
				clockLore.add(ChatColor.GOLD + "Runtime: " + ChatColor.WHITE + timeString);

			} catch (ParseException e) {
				clockLore.add(ChatColor.GOLD + "Runtime: " + ChatColor.WHITE + "Error getting runtime :(");
			}
		}
		clockItemMeta.setLore(clockLore);
		clock.setItemMeta(clockItemMeta);
		i.setItem(4, clock);

		ItemStack skull = new ItemStack(Material.SKULL_ITEM);
		skull.setDurability((short) 3);
		ItemMeta skullItemMeta = skull.getItemMeta();
		skullItemMeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.UNDERLINE + "Player Stats");
		List<String> playerStats = new ArrayList<String>();
		playerStats.add(ChatColor.GOLD + "Currently Online: " + ChatColor.WHITE + Bukkit.getOnlinePlayers().length + "/" + Bukkit.getMaxPlayers());
		playerStats.add(ChatColor.GOLD + "Total Joined: " + ChatColor.WHITE + Bukkit.getOfflinePlayers().length);
		skullItemMeta.setLore(playerStats);
		skull.setItemMeta(skullItemMeta);
		i.setItem(5, skull);

		ItemStack sign = new ItemStack(Material.SIGN);
		ItemMeta signItemMeta = sign.getItemMeta();
		signItemMeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.UNDERLINE + "About");
		List<String> signInfo = new ArrayList<String>();
		signInfo.add(ChatColor.GOLD + "Version: " + ChatColor.WHITE + Bukkit.getVersion());
		signItemMeta.setLore(signInfo);
		sign.setItemMeta(signItemMeta);
		i.setItem(6, sign);
		return i;
	}

	/*public static double getProcessCpuLoad() throws MalformedObjectNameException, ReflectionException, InstanceNotFoundException {

		MBeanServer mbs    = ManagementFactory.getPlatformMBeanServer();
		ObjectName name    = ObjectName.getInstance("java.lang:type=OperatingSystem");
		AttributeList list = mbs.getAttributes(name, new String[]{ "ProcessCpuLoad" });

		if (list.isEmpty())     return Double.NaN;

		Attribute att = (Attribute)list.get(0);
		Double value  = (Double)att.getValue();

		if (value == -1.0)      return Double.NaN;  // usually takes a couple of seconds before we get real values

		return ((int)(value * 1000) / 10.0);        // returns a percentage value with 1 decimal point precision
	}*/

	@SuppressWarnings("deprecation")
	public static Inventory getFriendListGUI(String playerName) {
		Slice slice = Applessentials.getSliceManager().getSlice(playerName);
		List<String> friends = slice.getFriendList();
		int multOf9 = 9;
		int req = friends.size()+1;
		while(req > multOf9) {
			multOf9 += 9;
		}

		Inventory i = Bukkit.createInventory(null, multOf9, ChatColor.YELLOW + "Friends List");

		List<String> onlinePlayers = new ArrayList<String>();
		List<String> offlinePlayers = new ArrayList<String>();

		for(String friendName : friends) {
			Player friendPlayer = Bukkit.getPlayer(friendName);
			if(friendPlayer != null && friendPlayer.isOnline()) {
				onlinePlayers.add(friendName);
			} else {
				offlinePlayers.add(friendName);
			}
		}

		Collections.sort(onlinePlayers, String.CASE_INSENSITIVE_ORDER);
		Collections.sort(offlinePlayers, String.CASE_INSENSITIVE_ORDER);

		ItemStack infoSign = new ItemStack(Material.SIGN);
		ItemMeta ism = infoSign.getItemMeta();
		ism.setDisplayName(ChatColor.YELLOW + "" + ChatColor.UNDERLINE + "Details");
		ism.setLore(Arrays.asList(ChatColor.GOLD + "" + onlinePlayers.size() + "/" + friends.size() + " Online", ChatColor.GRAY + "Friends are always", ChatColor.GRAY + "visible, regardless of", ChatColor.GRAY + "your player settings.", "*Click for command help*"));
		infoSign.setItemMeta(ism);
		i.addItem(infoSign);

		ItemStack playerHead = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		for(String friendName : onlinePlayers) {
			ItemStack friendHead = playerHead.clone();
			SkullMeta sim = (SkullMeta) friendHead.getItemMeta();
			sim.setOwner(friendName);
			List<String> lore = new ArrayList<String>();
			Player friendPlayer = Bukkit.getPlayer(friendName);
			if(friendPlayer != null && friendPlayer.isOnline()) {
				sim.setDisplayName(ChatColor.GREEN + friendName);
				lore.add(ChatColor.GRAY + "Online");
				String gameType = Applessentials.getGameTypeForWorld(friendPlayer.getWorld().getName());
				if(gameType != null) {
					lore.add(ChatColor.YELLOW + "Currently playing in " + gameType);
				}
				lore.add("*Click to private chat*");
			} else {
				sim.setDisplayName(ChatColor.RED + friendName);
				lore.add(ChatColor.GRAY + "Offline");
			}
			sim.setLore(lore);
			friendHead.setItemMeta(sim);
			i.addItem(friendHead);
		}

		for(String friendName : offlinePlayers) {
			ItemStack friendHead = playerHead.clone();
			SkullMeta sim = (SkullMeta) friendHead.getItemMeta();
			sim.setOwner(friendName);
			List<String> lore = new ArrayList<String>();
			Player friendPlayer = Bukkit.getPlayer(friendName);
			if(friendPlayer != null && friendPlayer.isOnline()) {
				sim.setDisplayName(ChatColor.GREEN + friendName);
				lore.add(ChatColor.GRAY + "Online");
				String gameType = Applessentials.getGameTypeForWorld(friendPlayer.getWorld().getName());
				if(gameType != null) {
					lore.add(ChatColor.YELLOW + "Currently playing in " + gameType);
				}
				lore.add("*Click to private chat*");
			} else {
				sim.setDisplayName(ChatColor.RED + friendName);
				lore.add(ChatColor.GRAY + "Offline");
			}
			sim.setLore(lore);
			friendHead.setItemMeta(sim);
			i.addItem(friendHead);
		}

		return i;
	}

	@SuppressWarnings("deprecation")
	public static Inventory getPlayerSettingsInventory(String playerName) {
		Inventory i = Bukkit.createInventory(null, 9*2, ChatColor.YELLOW + "Player Settings");
		Slice slice = Applessentials.getSliceManager().getSlice(playerName);
		boolean isHidingChat, isHidingPMs, isHidingPlayers, isAcceptingFriendRequests;

		isHidingPlayers = slice.isHidingPlayers();
		isHidingChat = slice.isHidingChat();
		isHidingPMs = slice.isHidingPMs();
		isAcceptingFriendRequests = slice.isAcceptingFriendRequests();

		ItemStack off = new ItemStack(Material.getMaterial(160), 1, (short) 14);
		ItemStack on = new ItemStack(Material.getMaterial(160), 1, (short) 5);

		//show players
		ItemStack hidingPlayers = new ItemStack(Material.EYE_OF_ENDER, 1);
		ItemStack hidingPlayersValue = isHidingPlayers ? off.clone() : on.clone();
		ItemMeta imPlayers = hidingPlayers.getItemMeta();
		imPlayers.setDisplayName(isHidingPlayers ? ChatColor.RED + "Show Spawn Players" : ChatColor.GREEN + "Show Spawn Players");
		imPlayers.setLore(Arrays.asList(isHidingPlayers ? ChatColor.GRAY + "Players are hidden in spawn." : ChatColor.GRAY + "Players are visable in spawn.", "*Click to toggle*"));
		hidingPlayers.setItemMeta(imPlayers);
		hidingPlayersValue.setItemMeta(imPlayers);
		i.setItem(1, hidingPlayers);
		i.setItem(10, hidingPlayersValue);

		//show chat
		ItemStack hidingChat = new ItemStack(Material.PAPER, 1);
		ItemStack hidingChatValue = isHidingChat ? off.clone() : on.clone();
		ItemMeta imChat = hidingChat.getItemMeta();
		imChat.setDisplayName(isHidingChat ? ChatColor.RED + "Show Chat" : ChatColor.GREEN + "Show Chat");
		imChat.setLore(Arrays.asList(isHidingChat ? ChatColor.GRAY + "Chat is hidden." : ChatColor.GRAY + "Chat is visable.", "*Click to toggle*"));
		hidingChat.setItemMeta(imChat);
		hidingChatValue.setItemMeta(imChat);
		i.setItem(3, hidingChat);
		i.setItem(12, hidingChatValue);

		//show PMs
		ItemStack hidingPMs = new ItemStack(Material.EMPTY_MAP, 1);
		ItemStack hidingPMsValue = isHidingPMs ? off.clone() : on.clone();
		ItemMeta imPMs = hidingPMs.getItemMeta();
		imPMs.setDisplayName(isHidingPMs ? ChatColor.RED + "Show Private Messages" : ChatColor.GREEN + "Show Private Messages");
		imPMs.setLore(Arrays.asList(isHidingPMs ? ChatColor.GRAY + "PMs are hidden." : ChatColor.GRAY + "PMs are visable.", "*Click to toggle*"));
		hidingPMs.setItemMeta(imPMs);
		hidingPMsValue.setItemMeta(imPMs);
		i.setItem(5, hidingPMs);
		i.setItem(14, hidingPMsValue);

		//accept friend requests
		ItemStack acceptingFRs = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		ItemStack acceptingFRsValue = isAcceptingFriendRequests ? on.clone() : off.clone();
		ItemMeta imFRs = off.getItemMeta();
		imFRs.setDisplayName(isAcceptingFriendRequests ? ChatColor.GREEN + "Enable Friend Requests" : ChatColor.RED + "Enable Friend Requests");
		imFRs.setLore(Arrays.asList(isAcceptingFriendRequests ? ChatColor.GRAY + "Friend requests are enabled." : ChatColor.GRAY + "Friend requests are disabled.", "*Click to toggle*"));
		acceptingFRs.setItemMeta(imFRs);
		acceptingFRsValue.setItemMeta(imFRs);
		i.setItem(7, acceptingFRs);
		i.setItem(16, acceptingFRsValue);

		return i;
	}
}
