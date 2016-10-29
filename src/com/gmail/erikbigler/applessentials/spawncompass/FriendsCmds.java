package com.gmail.erikbigler.applessentials.spawncompass;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import com.gmail.erikbigler.applessentials.Applessentials;
import com.gmail.erikbigler.applessentials.Slice;
import com.gmail.erikbigler.applessentials.utils.FancyMenu;
import com.gmail.erikbigler.applessentials.utils.Utils;


public class FriendsCmds implements CommandExecutor, Listener {

	String[] commandData = {
			"&7&o(Hover over a &a&ocommand &7&ofor info, click to run it)",
			"TELLRAW run>>/friends>>/friends>>See your friends list.",
			"TELLRAW run>>/friends help>>/friends help>>See this help menu.",
			"TELLRAW run>>/friends requests>>/friends requests>>Toggle enabling/disabling of friend requests.",
			"TELLRAW suggest>>/friends add [player]>>/friends add >>Send a friend request",
			"TELLRAW suggest>>/friends remove [player]>>/friends remove >>Remove a player as a friend"
	};

	@EventHandler(priority=EventPriority.MONITOR)
	public void invClose(InventoryCloseEvent e) {
		if(e.getPlayer() == null) return;
		if(!(e.getPlayer() instanceof Player)) return;
		Player p = (Player) e.getPlayer();
		if(Applessentials.playerFriendListMenu.contains(p)) {
			Applessentials.playerFriendListMenu.remove(p);
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority=EventPriority.MONITOR)
	public void invClick(InventoryClickEvent e) {
		if(!(e.getWhoClicked() instanceof Player)) return;
		Player p = (Player) e.getWhoClicked();
		if(Applessentials.playerFriendListMenu.contains(p)) {
			e.setCancelled(true);
			ItemStack clickedHead = e.getInventory().getItem(e.getSlot());
			if(clickedHead != null && clickedHead.getType() == Material.SKULL_ITEM) {
				String friendName = ChatColor.stripColor(clickedHead.getItemMeta().getDisplayName());
				Player friendPlayer = Bukkit.getPlayer(friendName);
				if(friendPlayer == null || !friendPlayer.isOnline()) return;
				p.performCommand("tell " + friendName);
				p.sendMessage(ChatColor.YELLOW + "Type " + ChatColor.WHITE + "/ch g" + ChatColor.YELLOW + " to return to Global Chat.");
				p.closeInventory();
			}
			if(clickedHead != null && clickedHead.getType() == Material.SIGN) {
				p.performCommand("friends help");
				p.closeInventory();
			}
		}
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		if(commandLabel.equalsIgnoreCase("settings")) {
			Player p = (Player) sender;
			if(!Applessentials.playerSettingsMenu.contains(p)) {
				Applessentials.playerSettingsMenu.add(p);
			}
			p.openInventory(Utils.getPlayerSettingsInventory(p.getName()));
		}

		else if(commandLabel.equalsIgnoreCase("friends")) {
			if(args.length == 0) {
				Player p = (Player) sender;
				Applessentials.playerFriendListMenu.remove(p);
				Applessentials.playerFriendListMenu.add(p);
				p.openInventory(Utils.getFriendListGUI(p.getName()));
				//show friends list
			}
			else if(args.length > 0) {
				if(args[0].equalsIgnoreCase("help")) {
					FancyMenu.showClickableCommandList(sender, commandLabel, "Friends Commands", commandData, 1);
					//show help menu
				}
				else if(args[0].equalsIgnoreCase("accept")) {
					if(Applessentials.friendRequests.containsKey((Player) sender)) {
						Player requestorP = Applessentials.friendRequests.get((Player) sender);
						if(requestorP.isOnline()) {
							Slice accepter = Applessentials.getSliceManager().getSlice(sender.getName());
							Slice requester = Applessentials.getSliceManager().getSlice(requestorP.getName());
							accepter.addFriend(requestorP.getName());
							requester.addFriend(sender.getName());
							Player senderPlayer = (Player) sender;
							if(!requestorP.canSee(senderPlayer)) {
								requestorP.showPlayer(senderPlayer);
							}
							if(!senderPlayer.canSee(requestorP)) {
								senderPlayer.showPlayer(requestorP);
							}
							requestorP.sendMessage(ChatColor.GREEN + "[AppleCraft] You are now friends with " + ChatColor.YELLOW + sender.getName());
							sender.sendMessage(ChatColor.GREEN + "[AppleCraft] You are now friends with " + ChatColor.YELLOW + requestorP.getName());
						} else {
							sender.sendMessage(ChatColor.RED + "An error occurred attempting to add friend. Did they go offline?");
						}
						Applessentials.friendRequests.remove((Player) sender);
					}
				}
				if(args[0].equalsIgnoreCase("deny")) {
					if(Applessentials.friendRequests.containsKey((Player) sender)) {
						Player requestorP = Applessentials.friendRequests.get((Player) sender);
						sender.sendMessage(ChatColor.GREEN + "[AppleCraft] You denied the friend request from " + ChatColor.YELLOW + requestorP.getName());
						Applessentials.friendRequests.remove((Player) sender);
					}
				}
				else if(args[0].equalsIgnoreCase("requests")) {
					Slice slice = Applessentials.getSliceManager().getSlice(sender.getName());
					if(slice.isAcceptingFriendRequests()) {
						slice.setAcceptFriendRequests(false);
						sender.sendMessage(ChatColor.GREEN + "[AppleCraft] Disabled friend requests!");
						Player p = (Player) sender;
						if(Applessentials.friendRequests.containsKey(p)) {
							Applessentials.friendRequests.remove(p);
						}
					} else {
						slice.setAcceptFriendRequests(true);
						sender.sendMessage(ChatColor.GREEN + "[AppleCraft] Enabled friend requests!");
					}
				}
				else if(args[0].equalsIgnoreCase("add")) {
					if(args.length == 1) {
						sender.sendMessage(ChatColor.RED + "[AppleCraft] You must specify a player's name! /friends add [player]");
					}
					else {
						final Player newFriend = Bukkit.getPlayerExact(args[1]);
						if(newFriend != null && newFriend.isOnline()) {
							Slice newSlice = Applessentials.getSliceManager().getSlice(args[1]);
							if(newSlice.isFriendsWith(sender.getName())) {
								sender.sendMessage(ChatColor.RED + "[AppleCraft] You are already friends with that player!");
							}
							else if(sender.getName().equals(args[1])) {
								sender.sendMessage(ChatColor.RED + "[AppleCraft] You can't be friends with yourself...");
							}
							else if(!newSlice.isAcceptingFriendRequests()) {
								sender.sendMessage(ChatColor.RED + "[AppleCraft] This player is not taking friend requests at this time.");
							}
							else if(Applessentials.friendRequests.containsKey(newFriend) && Applessentials.friendRequests.get(newFriend) == (Player) sender) {
								sender.sendMessage(ChatColor.RED + "[AppleCraft] You've already sent a friend request to this player recently!");
							}
							else {
								sender.sendMessage(ChatColor.GREEN + "[AppleCraft] Sent a friend request to " + ChatColor.YELLOW + args[1]);
								Applessentials.friendRequests.put(newFriend, (Player) sender);
								String command = "tellraw %player% {\"text\":\"\",\"extra\":[{\"text\":\"[AppleCraft] \",\"color\":\"green\"},{\"text\":\"%requestor%\",\"color\":\"yellow\"},{\"text\":\" wants to be friends! \"},{\"text\":\"Click one: \",\"color\":\"gray\"},{\"text\":\"Accept\",\"color\":\"green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/friends accept\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"Click to accept request.\"}}},{\"text\":\", \"},{\"text\":\"Deny\",\"color\":\"red\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/friends deny\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"Click to deny request.\"}}},{\"text\":\", or \"},{\"text\":\"Disable Requests\",\"color\":\"gold\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/friends requests\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"Click to disable friend requests\"}}}]}";
								command = command.replace("%player%", newFriend.getName()).replace("%requestor%", sender.getName());
								Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
								Bukkit.getScheduler().scheduleSyncDelayedTask(Applessentials.getPlugin(), new Runnable() {
									@Override
									public void run() {
										if(Applessentials.friendRequests.containsKey(newFriend)) {
											Applessentials.friendRequests.remove(newFriend);
											newFriend.sendMessage(ChatColor.GREEN + "[AppleCraft] Friend request timed out.");
										}
									}
								}, 600L);
							}
						} else {
							sender.sendMessage(ChatColor.RED + "[AppleCraft] You can only send friend requests to online players.");
						}
						//send friend request
					}
				}
				else if(args[0].equalsIgnoreCase("remove")) {
					if(args.length == 1) {
						sender.sendMessage(ChatColor.RED + "[AppleCraft] You must specify a player's name! /friends remove [player]");
					}
					else {
						Slice slice = Applessentials.getSliceManager().getSlice(sender.getName());
						if(slice.isFriendsWith(args[1])) {
							slice.removeFriend(args[1]);
							Applessentials.getSliceManager().getSlice(args[1]).removeFriend(sender.getName());
							sender.sendMessage(ChatColor.GREEN + "[AppleCraft] You are no longer friends with " + ChatColor.YELLOW + args[1]);
						} else {
							sender.sendMessage(ChatColor.RED + "[AppleCraft] That player is not on your friends list.");
						}
					}
				}
			}
		}
		return true;
	}
}
