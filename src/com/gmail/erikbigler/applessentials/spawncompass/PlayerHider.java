package com.gmail.erikbigler.applessentials.spawncompass;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import com.gmail.erikbigler.applessentials.Applessentials;
import com.gmail.erikbigler.applessentials.Slice;
import com.gmail.erikbigler.applessentials.utils.Utils;


public class PlayerHider implements CommandExecutor, Listener {

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		if(commandLabel.equalsIgnoreCase("hideplayers")) {
			final Player player = (Player) sender;
			Slice slice = Applessentials.getSliceManager().getSlice(player.getName());
			if(slice.isHidingPlayers()) {
				if(Applessentials.playersHidingPlayers.contains(player)) {
					Applessentials.playersHidingPlayers.remove(player);
				}
				slice.setHidePlayers(false);
				sender.sendMessage(ChatColor.GREEN + "[AppleCraft] Showing players in spawn!");
				Bukkit.getScheduler().runTaskAsynchronously(Applessentials.getPlugin(), new Runnable(){
					@Override
					public void run() {
						showSpawnPlayersFor(player);
					}
				});
			} else {
				Applessentials.playersHidingPlayers.add(player);
				slice.setHidePlayers(true);
				sender.sendMessage(ChatColor.GREEN + "[AppleCraft] Hiding players in spawn!");
				Bukkit.getScheduler().runTaskAsynchronously(Applessentials.getPlugin(), new Runnable(){
					@Override
					public void run() {
						hideSpawnPlayersFor(player);
					}
				});
			}
		}
		return true;
	}

	@EventHandler (priority = EventPriority.MONITOR)
	public void onJoin(PlayerJoinEvent event) {
		if(event.getPlayer().getWorld() == Applessentials.getSpawnWorld()) {
			for(Player player : Applessentials.playersHidingPlayers) {
				if(event.getPlayer() == player) continue;
				player.hidePlayer(event.getPlayer());
			}
		}
	}

	@EventHandler (priority = EventPriority.MONITOR)
	public void onWorldChange(PlayerChangedWorldEvent event) {
		if(event.getPlayer().getWorld() == Applessentials.getSpawnWorld()) {
			for(Player player : Applessentials.playersHidingPlayers) {
				if(event.getPlayer() == player) continue;
				player.hidePlayer(event.getPlayer());
			}
		}
		else if(event.getFrom() == Applessentials.getSpawnWorld()) {
			for(Player player : Applessentials.playersHidingPlayers) {
				if(event.getPlayer() == player) continue;
				player.showPlayer(event.getPlayer());
			}
		}
	}

	public static void hideSpawnPlayersFor(Player player) {
		System.out.println("Hiding players in spawn from: " + player.getName());
		World spawn = Applessentials.getSpawnWorld();
		Slice slice = Applessentials.getSliceManager().getSlice(player.getName());
		List<Player> players = spawn.getPlayers();
		for(Player p : players) {
			System.out.println("Doing hide check for: " + p.getName());
			if(p == player) continue;
			System.out.println("Player isnt the target player");
			if(slice.isFriendsWith(p.getName())) continue;
			System.out.println("Player isnt friends with the target player");
			if(Utils.playerIsHideExcempt(p)) continue;
			System.out.println("Player isnt hide exempt, hiding player");
			Bukkit.getScheduler().runTask(Applessentials.getPlugin(), new Runnable(){
				private Player main;
				private Player hide;
				@Override
				public void run() {
					main.hidePlayer(hide);
				}

				private Runnable init(Player main, Player hide){
					this.main = main;
					this.hide = hide;
					return this;
				}
			}.init(player, p));
		}
	}

	public void showSpawnPlayersFor(Player player) {
		World spawn = Applessentials.getSpawnWorld();
		List<Player> players = spawn.getPlayers();
		for(Player p : players) {
			if(p == player) continue;
			Bukkit.getScheduler().runTask(Applessentials.getPlugin(), new Runnable(){
				private Player main;
				private Player hide;
				@Override
				public void run() {
					main.showPlayer(hide);
				}

				private Runnable init(Player main, Player hide){
					this.main = main;
					this.hide = hide;
					return this;
				}
			}.init(player, p));
		}
	}
}
