package com.gmail.erikbigler.applessentials.listeners;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitScheduler;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import com.gmail.erikbigler.applessentials.Applessentials;
import com.gmail.erikbigler.applessentials.Slice;
import com.gmail.erikbigler.applessentials.bossbar.BarAPI;
import com.gmail.erikbigler.applessentials.utils.Time;
import com.gmail.erikbigler.applessentials.utils.Utils;



public class PlayerListener implements Listener {


	private Applessentials plugin;

	public PlayerListener(Applessentials plugin) {
		this.plugin = plugin;
	}

	@EventHandler (priority = EventPriority.MONITOR)
	public void onEarlyJoin(final AsyncPlayerPreLoginEvent event) {

		if(Utils.getOnlinePlayers().length < Bukkit.getServer().getMaxPlayers()) return;


		PermissionUser pu = PermissionsEx.getPermissionManager().getUser(event.getUniqueId());

		event.setKickMessage(ChatColor.GREEN + "AppleCraft is currently full. Please try back soon. Remember, VIP members get space made for them when they join a full server!");
		event.setLoginResult(Result.KICK_OTHER);

		if(!pu.inGroup("Emerald", true)) return;

		event.setKickMessage(ChatColor.GREEN + "Sorry, while you are a VIP member, there isn't a non-VIP player online to kick to make space for you. Please try back soon.");
		event.setLoginResult(Result.ALLOWED);
		event.allow();

		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		scheduler.runTask(plugin, new Runnable() {
			@Override
			public void run() {
				Random r = new Random();
				if(Applessentials.nonVIPs.isEmpty()) return;
				Player poorSoul = Applessentials.nonVIPs.get(r.nextInt(Applessentials.nonVIPs.size()));
				if(poorSoul != null) {
					poorSoul.kickPlayer(ChatColor.GREEN + "You have been kicked to make room for a VIP player. Please try back soon. Thanks for playing on AppleCraft!");
				}
			}
		});
	}

	/*@EventHandler
	public void onRegionEnter(RegionEnterEvent e) {
		if(e.getRegion().getId().equalsIgnoreCase("VoteLobby")) {
			e.getPlayer().performCommand("sg join");
		}
	}


	@EventHandler
	public void onRegionLeave(RegionLeaveEvent e) {
		if(e.getRegion().getId().equalsIgnoreCase("VoteLobby")) {
			e.getPlayer().performCommand("sg leave");
		}
	}*/


	@EventHandler (priority = EventPriority.MONITOR)
	public void onJoin(PlayerJoinEvent event) {
		final Player p = event.getPlayer();

		event.setJoinMessage("");

		Bukkit.getScheduler().runTaskAsynchronously(Applessentials.getPlugin(), new Runnable() {
			@Override
			public void run() {
				notifyFriendsOfPlayerStatusChange(p, "joined");
			}
		});

		String playerName = p.getName();
		Slice slice = Applessentials.getSliceManager().getSlice(playerName);

		slice.setJoinTime(Time.getTime());

		if(PermissionsEx.getUser(p).inGroup("Player", false) || PermissionsEx.getUser(p).inGroup("Trusted", false)) {
			Applessentials.nonVIPs.add(p);
		}

		Location newLoc = slice.getNewLoginLoc();
		if(newLoc != null) {
			p.teleport(newLoc);
			slice.setNewLoginLoc(null);
		}

		long totalTimeMills = slice.getTotalTime();

		int totalMins = (int) (totalTimeMills/60000);
		int totalHours = totalMins/60;
		int totalDays = totalHours/24;

		if(totalDays >= 7) {
			if(PermissionsEx.getUser(p).inGroup("Player", false)) {
				PermissionsEx.getUser(p).addGroup("Trusted");
				PermissionsEx.getUser(p).removeGroup("Player");
				Bukkit.broadcastMessage(ChatColor.GREEN + "[AppleCraft] " + ChatColor.BLUE + p.getName() + " has wasted 7 days of their life on AppleCraft and is now a Trusted player!");
			}
		}

		if(slice.isBannedByWarn()) {
			slice.setIsBannedByWarn(false);
			slice.setLastWarnPointsChange(Time.getTime());
		}

		int daysSinceWarn = slice.daysSinceLastWarnPoints();
		int weeksSince = daysSinceWarn/7;
		if(weeksSince >= 1) {
			int newWarnPoints = slice.getWarnPoints() - weeksSince;
			if(!(newWarnPoints < 0)) {
				slice.setWarnPoints(newWarnPoints);
			} else {
				slice.setWarnPoints(0);
			}
			slice.setLastWarnPointsChange(Time.getTime());
		}
	}

	private void notifyFriendsOfPlayerStatusChange(Player player, String joinOrLeft) {
		Player[] onlinePlayers = Utils.getOnlinePlayers();
		for(Player onlinePlayer : onlinePlayers) {
			Slice onlineSlice = Applessentials.getSliceManager().getSlice(onlinePlayer.getName());
			if(onlineSlice.isFriendsWith(player.getName())) {
				if(player.hasPermission("applecraft.owner") || player.hasPermission("applecraft.admin")) return;
				onlinePlayer.sendMessage(ChatColor.YELLOW + player.getName() + " " + joinOrLeft + " the game.");
			}
		}
	}

	@EventHandler (priority = EventPriority.MONITOR)
	public void onQuit(PlayerQuitEvent event) {
		final Player p = event.getPlayer();

		event.setQuitMessage("");
		
		BarAPI.removeBar(p);

		Bukkit.getScheduler().runTaskAsynchronously(Applessentials.getPlugin(), new Runnable() {
			@Override
			public void run() {
				notifyFriendsOfPlayerStatusChange(p, "left");
				String playerName = p.getName();
				Double balance = Applessentials.economy.getBalance(playerName);
				Slice slice = Applessentials.getSliceManager().getSlice(playerName);
				slice.setLogoffBalance(balance);
				slice.updateTotalTime();
				slice.saveLastKnownLocation(p.getLocation());
			}
		});

	}

	@EventHandler (priority = EventPriority.MONITOR)
	public void onDeath(PlayerDeathEvent event) {
		String playerName = event.getEntity().getName();
		Slice slice = Applessentials.getSliceManager().getSlice(playerName);

		slice.setLastDeathLoc(event.getEntity().getLocation());
	}

	@EventHandler(priority= EventPriority.HIGHEST)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {

		String command = event.getMessage();
		final Player p = event.getPlayer();

		if(command.equalsIgnoreCase("/hub")) {
			String newCommand = "/spawn";
			event.setMessage(newCommand);
		}
		else if(command.equalsIgnoreCase("/res tool")) {
			String newCommand = "/restool";
			event.setMessage(newCommand);
		}
		else if(command.equalsIgnoreCase("/res select max")) {
			String newCommand = "/resmax";
			event.setMessage(newCommand);
		}

		command = command.toLowerCase();

		if(command.startsWith("/?") || command.startsWith("/help")) {
			if(!p.hasPermission("applecraft.mod")) {
				event.setCancelled(true);
			}
		}

		/* if(command.startsWith("/sg join") || command.startsWith("/hg join")) {
			if(event.getPlayer().hasPermission("applecraft.mod")) return;
			event.setCancelled(true);
			return;
		} */

		if(command.contains("/tell")) return;
		if(command.contains("/msg")) return;
		if(command.contains("/r")) return;
		if(command.contains("/whisper")) return;

		if(p.hasPermission("applecraft.owner")) return;

		Slice slice = Applessentials.getSliceManager().getSlice(p.getName());
		slice.addRecentCommand(command);

	}
}
