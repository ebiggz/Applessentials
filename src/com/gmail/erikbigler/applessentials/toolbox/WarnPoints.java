package com.gmail.erikbigler.applessentials.toolbox;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.erikbigler.applessentials.Applessentials;
import com.gmail.erikbigler.applessentials.Slice;
import com.gmail.erikbigler.applessentials.WarnEntry;
import com.gmail.erikbigler.applessentials.WarnPunishment;
import com.gmail.erikbigler.applessentials.utils.Time;


public class WarnPoints implements CommandExecutor {

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {


		if(commandLabel.equalsIgnoreCase("warn")) {
			if(!sender.hasPermission("applecraft.warn")) {
				sender.sendMessage(ChatColor.RED + "You don't have permission to run this command.");
				return true;
			}
			if(args.length == 0) {
				sender.sendMessage(ChatColor.RED + "Please type in a player's name.");
				return true;
			}
			if(args.length >= 1) {
				String playerName = completeName(args[0]);
				if(playerName == null) {
					sender.sendMessage(ChatColor.RED + "Not a known player.");
					return true;
				}
				Slice slice = Applessentials.getSliceManager().getSlice(playerName);
				slice.addWarnPoints(1);
				slice.setLastWarnPointsChange(Time.getTime());
				Player p = Bukkit.getPlayer(playerName);
				if(p != null) {
					p.sendMessage(ChatColor.RED + "[AppleCraft] You have been issued a warning by staff. You now have a total of " + ChatColor.GOLD + slice.getWarnPoints() + ChatColor.RED + " warn points. Type " + ChatColor.GOLD + "/warninfo");
				}
				sender.sendMessage(ChatColor.GREEN + "[Applessentials] " + ChatColor.GOLD + "You have issued a warning to " + ChatColor.WHITE + playerName + ChatColor.GOLD+ ". You can review their warn points and staff entries by typing " + ChatColor.WHITE + "/warninfo " + playerName);
				String message = "";
				if(args.length > 1) {
					for(int i = 1; i < args.length; i++){ //Combine arguments into one string
						message += " " + args[i];
					}
					message = message.substring(1).trim();
					List<WarnEntry> entries = slice.getWarnEntries();
					entries.add(new WarnEntry(sender.getName(), message, Time.getTime()));
					slice.saveWarnEntries(entries);
				} else {
					List<WarnEntry> entries = slice.getWarnEntries();
					entries.add(new WarnEntry(sender.getName(), "Warned issued by", Time.getTime()));
					slice.saveWarnEntries(entries);
				}
				for(WarnPunishment punishment : Applessentials.warnPunishments) {
					if(slice.getWarnPoints() == punishment.getPoints()) {
						String justice = punishment.getTime().toLowerCase();
						justice = justice.replace("ban", "");
						justice = justice.trim();
						if(justice.equalsIgnoreCase("kick")) {
							Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "kick " + playerName + " You have been kicked because you were issued another warning by staff: " + message);
						}
						else if(justice.equalsIgnoreCase("perma")) {
							Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "ban " + playerName + " You have accured too many warnings and have been permanently banned. You can challenge the ban on the AppleCraft forums. " + ChatColor.GREEN + ChatColor.UNDERLINE + "www.heyaapl.com/forum");
							slice.setIsBannedByWarn(true);
						}
						else {
							Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "tempban " + playerName + " " + punishment.getTime());
							slice.setIsBannedByWarn(true);
						}
						break;
					}
				}
			}
		}
		else if(commandLabel.equalsIgnoreCase("setwarn")) {
			if(!sender.hasPermission("applecraft.setwarn")) {
				sender.sendMessage(ChatColor.RED + "You don't have permission to run this command.");
				return true;
			}
			if(args.length == 0) {
				sender.sendMessage(ChatColor.RED + "Please type in a player's name.");
				return true;
			}
			if(args.length >= 1) {
				String playerName = completeName(args[0]);
				if(playerName == null) {
					sender.sendMessage(ChatColor.RED + "Not a known player.");
					return true;
				}
				Slice slice = Applessentials.getSliceManager().getSlice(playerName);

				String message = "";
				if(args.length == 2) {
					try {
						int points = Integer.parseInt(args[1]);

						slice.setLastWarnPointsChange(Time.getTime());
						slice.addWarnPoints(points);

						Player p = Bukkit.getPlayer(playerName);
						if(p != null) {
							p.sendMessage(ChatColor.RED + "[AppleCraft] You have been issued a warning by staff. You now have a total of " + ChatColor.GOLD + slice.getWarnPoints() + ChatColor.RED + " warn points. Type " + ChatColor.GOLD + "/warninfo");
						}
						sender.sendMessage(ChatColor.GREEN + "[AppleCraft] " + ChatColor.GOLD + "You have set the a warning points of " + ChatColor.WHITE + playerName + ChatColor.GOLD+ ". You can review their warn points and staff entries by typing " + ChatColor.WHITE + "/warninfo " + playerName);

						for(WarnPunishment punishment : Applessentials.warnPunishments) {
							if(slice.getWarnPoints() == punishment.getPoints()) {
								String justice = punishment.getTime().toLowerCase();
								justice = justice.replace("ban", "");
								justice = justice.trim();
								if(justice.equalsIgnoreCase("kick")) {
									Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "kick " + playerName + " You have been kicked because you were issued another warning by staff: " + message);
								}
								else if(justice.equalsIgnoreCase("perma")) {
									Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "ban " + playerName + " You have accured too many warnings and have been permanently banned. You can challenge the ban on the AppleCraft forums. " + ChatColor.GREEN + ChatColor.UNDERLINE + "www.heyaapl.com/forum");
									slice.setIsBannedByWarn(true);
								}
								else {
									Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "tempban " + playerName + " " + punishment.getTime());
									slice.setIsBannedByWarn(true);
								}
								break;
							}
						}

						List<WarnEntry> entries = slice.getWarnEntries();
						entries.add(new WarnEntry(sender.getName(), "Warned by " + sender.getName(), Time.getTime()));
						slice.saveWarnEntries(entries);

					} catch(Exception e) {
						sender.sendMessage(ChatColor.RED + "[AppleCraft] Invalid number!");
						return true;
					}
				} else {
					sender.sendMessage(ChatColor.RED + "Invalid command. /setwarn [player] [#ofpoints]");
				}
			}
		}
		else if(commandLabel.equalsIgnoreCase("unwarn")) {

			if(!sender.hasPermission("applecraft.unwarn")) {
				sender.sendMessage(ChatColor.RED + "You don't have permission to run this command.");
				return true;
			}

			if(args.length == 0) {
				sender.sendMessage(ChatColor.RED + "Please type in a player's name.");
				return true;
			}

			if(args.length >= 1) {
				String playerName = completeName(args[0]);
				if(playerName == null) {
					sender.sendMessage(ChatColor.RED + "Not a known player.");
					return true;
				}
				Slice slice = Applessentials.getSliceManager().getSlice(playerName);

				int newPoints = slice.getWarnPoints() - 1;
				if(newPoints < 0) {
					newPoints = 0;
				}
				slice.setWarnPoints(newPoints);
				if(slice.isBannedByWarn()) {
					Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "unban " + playerName);
					slice.setIsBannedByWarn(false);
				}
				Player p = Bukkit.getPlayer(playerName);
				if(p != null) {
					p.sendMessage(ChatColor.GREEN + "[AppleCraft] A warning point has been removed from your total by staff. You now have " + ChatColor.GOLD + slice.getWarnPoints() + ChatColor.GREEN + " warn points. Type " + ChatColor.GOLD + "/warninfo" + ChatColor.GREEN + " for more.");
				}
				sender.sendMessage(ChatColor.GREEN + "[Applessentials] " + ChatColor.GOLD + "You have removed a warn point from " + ChatColor.WHITE + playerName);
			}
		}
		else if(commandLabel.equalsIgnoreCase("warninfo")) {

			if(args.length == 0) {
				Slice slice = Applessentials.getSliceManager().getSlice(sender.getName());
				sender.sendMessage(ChatColor.GREEN + "[AppleCraft] " + ChatColor.YELLOW + "You have " + ChatColor.GOLD + slice.getWarnPoints() + ChatColor.YELLOW + " warn points.");
				sender.sendMessage(ChatColor.GREEN + "[AppleCraft] " + ChatColor.YELLOW + "Type " + ChatColor.WHITE + "/warninfo ?" + ChatColor.YELLOW + " to learn about warn points.");
			}
			else if(args.length >= 1) {
				if(args[0].equalsIgnoreCase("?")) {
					if(args.length == 1) {
						sender.sendMessage(ChatColor.GREEN + "[AppleCraft] " + ChatColor.YELLOW + "Warn Point Information" + ChatColor.WHITE + " Page 1/2");
						sender.sendMessage(ChatColor.WHITE + "-" + ChatColor.GOLD + " Staff members will issue you warnings if you are causing issues on the server. ");
						sender.sendMessage(ChatColor.WHITE + "-" + ChatColor.GOLD + " Each warning gets you 1 warn point.");
						sender.sendMessage(ChatColor.WHITE + "-" + ChatColor.GOLD + " Reaching certain amounts of warn points will get you banned for certain time periods.");
						sender.sendMessage(ChatColor.WHITE + "-" + ChatColor.GOLD + " A warn point goes away after a clean week (without any other warnings)");
						sender.sendMessage(ChatColor.WHITE + "-" + ChatColor.GOLD + " Type " + ChatColor.WHITE + "/warninfo ? 2" + ChatColor.GOLD + " to see the warn punishments page.");
					} else {
						if(args[1].equalsIgnoreCase("2")) {
							sender.sendMessage(ChatColor.GREEN + "[AppleCraft] " + ChatColor.YELLOW + "Warn Punishment Information" + ChatColor.WHITE + " Page 2/2");
							sender.sendMessage(ChatColor.RED + "Warn Points" + ChatColor.WHITE + " | " + ChatColor.RED + "Punishment");
							for(WarnPunishment punishment : Applessentials.warnPunishments) {
								sender.sendMessage(ChatColor.GOLD + "" + punishment.getPoints() +  ChatColor.WHITE + " : " + ChatColor.GOLD + punishment.getTime());
							}
						}
					}
				}
				else {
					if(!sender.hasPermission("applecraft.mod")) {
						sender.sendMessage(ChatColor.RED + "You don't have permission to run this command.");
						return true;
					}

					String playerName = completeName(args[0]);
					if(playerName == null) {
						sender.sendMessage(ChatColor.RED + "Not a known player.");
						return true;
					}
					Slice slice = Applessentials.getSliceManager().getSlice(playerName);
					sender.sendMessage(ChatColor.GREEN + "----[" + ChatColor.AQUA + playerName + ChatColor.YELLOW + " Warn Info" + ChatColor.GREEN + "]----");
					sender.sendMessage(ChatColor.YELLOW + "Warn Points: " + ChatColor.GOLD + slice.getWarnPoints());
					List<WarnEntry> entries = slice.getWarnEntries();
					if(!entries.isEmpty()) {
						sender.sendMessage(ChatColor.YELLOW + "Staff Entries:");
						int index = 1;
						for(WarnEntry entry : entries) {
							sender.sendMessage(ChatColor.GOLD + "" + index + ") " + ChatColor.RED + ChatColor.ITALIC + entry.getMessage() + ChatColor.RESET + ChatColor.DARK_AQUA + " - " + entry.getMod() + ChatColor.GRAY + " (" + entry.getTimeStamp() + ")");
							index++;
						}
					}
				}
			}
		}

		else if(commandLabel.equalsIgnoreCase("resetwarn")) {
			if(!sender.hasPermission("applecraft.resetwarn")) {
				sender.sendMessage(ChatColor.RED + "You don't have permission to run this command.");
				return true;
			}

			if(args.length == 0) {
				sender.sendMessage(ChatColor.RED + "Please type in a player's name.");
				return true;
			}

			if(args.length >= 1) {
				String playerName = completeName(args[0]);
				if(playerName == null) {
					sender.sendMessage(ChatColor.RED + "Not a known player.");
					return true;
				}
				Slice slice = Applessentials.getSliceManager().getSlice(playerName);
				List<WarnEntry> noEntries = new ArrayList<WarnEntry>();
				if(args.length == 2) {
					if(args[1].equalsIgnoreCase("points")) {
						slice.setWarnPoints(0);
						sender.sendMessage(ChatColor.GREEN + "[AppleCraft] You have reset " + playerName + "'s warn points.");
						if(slice.isBannedByWarn()) {
							Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "unban " + playerName);
							slice.setIsBannedByWarn(false);
						}
					}
					else if(args[1].equalsIgnoreCase("entries")) {
						slice.saveWarnEntries(noEntries);
						sender.sendMessage(ChatColor.GREEN + "[AppleCraft] You have reset " + playerName + "'s warn staff entries.");
					} else {
						sender.sendMessage(ChatColor.RED + "[AppleCraft] Not a recongized warn data type. /resetwarn [player] [entries/points]");
					}
				} else {
					slice.setWarnPoints(0);
					slice.saveWarnEntries(noEntries);
					sender.sendMessage(ChatColor.GREEN + "[AppleCraft] You have reset all of " + playerName + "'s warn data.");
					if(slice.isBannedByWarn()) {
						Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "unban " + playerName);
						slice.setIsBannedByWarn(false);
					}
				}
			}
		}
		return true;
	}

	public String completeName(String playername) {
		Player[] onlinePlayers = Bukkit.getOnlinePlayers();
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
}
