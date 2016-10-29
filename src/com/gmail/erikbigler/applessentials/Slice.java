package com.gmail.erikbigler.applessentials;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import com.gmail.erikbigler.applessentials.utils.ConfigAccessor;
import com.gmail.erikbigler.applessentials.utils.Time;
import com.gmail.erikbigler.applessentials.utils.Utils;

public class Slice {

	private Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Applessentials");
	private String playerName;

	public Slice(String playerName) {
		this.playerName = playerName;
		createFile(plugin.getDataFolder().getAbsolutePath() + File.separator + "data" + File.separator + "players", playerName + ".yml");
	}

	public long getPlayTime() {
		ConfigAccessor playerData = new ConfigAccessor("players" + File.separator + playerName + ".yml");
		if(playerData.getConfig().contains("joinTime")) {
			String joinTime = playerData.getConfig().getString("joinTime");
			try {
				return Time.compareTimeMills(joinTime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	public List<String> getFriendList() {
		ConfigAccessor playerData = new ConfigAccessor("players" + File.separator + playerName + ".yml");
		List<String> friends = new ArrayList<String>();
		if(playerData.getConfig().contains("friends.list")) {
			friends = playerData.getConfig().getStringList("friends.list");
		}
		return friends;
	}

	public boolean isFriendsWith(String friend) {
		return getFriendList().contains(friend);
	}

	public void removeFriend(String friend) {
		ConfigAccessor playerData = new ConfigAccessor("players" + File.separator + playerName + ".yml");
		List<String> friends = this.getFriendList();
		friends.remove(friend);
		playerData.getConfig().set("friends.list", friends);
		playerData.saveConfig();
	}

	public void addFriend(String friend) {
		ConfigAccessor playerData = new ConfigAccessor("players" + File.separator + playerName + ".yml");
		List<String> friends = this.getFriendList();
		if(!friends.contains(friend)) {
			friends.add(friend);
		}
		playerData.getConfig().set("friends.list", friends);
		playerData.saveConfig();
	}

	public void addWarnPoints(int points) {
		ConfigAccessor playerData = new ConfigAccessor("players" + File.separator + playerName + ".yml");
		playerData.getConfig().set("warn.points", getWarnPoints() + points);
		playerData.saveConfig();
	}

	public void setHideChat(boolean shouldHide) {
		ConfigAccessor playerData = new ConfigAccessor("players" + File.separator + playerName + ".yml");
		playerData.getConfig().set("hide.chat", shouldHide);
		playerData.saveConfig();
	}
	public void setHidePMs(boolean shouldHide) {
		ConfigAccessor playerData = new ConfigAccessor("players" + File.separator + playerName + ".yml");
		playerData.getConfig().set("hide.pms", shouldHide);
		playerData.saveConfig();
	}

	public void setHidePlayers(boolean shouldHide) {
		ConfigAccessor playerData = new ConfigAccessor("players" + File.separator + playerName + ".yml");
		playerData.getConfig().set("hide.players", shouldHide);
		playerData.saveConfig();
	}

	public boolean isHidingChat() {
		ConfigAccessor playerData = new ConfigAccessor("players" + File.separator + playerName + ".yml");
		return playerData.getConfig().getBoolean("hide.chat", false);
	}

	public boolean isHidingPMs() {
		ConfigAccessor playerData = new ConfigAccessor("players" + File.separator + playerName + ".yml");
		return playerData.getConfig().getBoolean("hide.pms", false);
	}

	public boolean isHidingPlayers() {
		ConfigAccessor playerData = new ConfigAccessor("players" + File.separator + playerName + ".yml");
		return playerData.getConfig().getBoolean("hide.players", false);
	}

	public void setAcceptFriendRequests(boolean isAccepting) {
		ConfigAccessor playerData = new ConfigAccessor("players" + File.separator + playerName + ".yml");
		playerData.getConfig().set("friends.accept-requests", isAccepting);
		playerData.saveConfig();
	}

	public boolean isAcceptingFriendRequests() {
		ConfigAccessor playerData = new ConfigAccessor("players" + File.separator + playerName + ".yml");
		return playerData.getConfig().getBoolean("friends.accept-requests", true);
	}

	public void setIsBannedByWarn(boolean banned) {
		ConfigAccessor playerData = new ConfigAccessor("players" + File.separator + playerName + ".yml");
		playerData.getConfig().set("warn.isBanned", banned);
		playerData.saveConfig();
	}

	public void setLastWarnPointsChange(String timeStamp) {
		ConfigAccessor playerData = new ConfigAccessor("players" + File.separator + playerName + ".yml");
		playerData.getConfig().set("warn.lastPointChange", timeStamp);
		playerData.saveConfig();
	}

	public String getLastWarnPointsChange() {
		ConfigAccessor playerData = new ConfigAccessor("players" + File.separator + playerName + ".yml");
		return playerData.getConfig().getString("warn.lastPointChange", "");
	}

	public int daysSinceLastWarnPoints() {
		String last = this.getLastWarnPointsChange();
		if(last.isEmpty()) return 0;
		try {
			int totalMins = Time.compareTime(last);
			return totalMins/60/24;
		} catch (ParseException e) {
			return 0;
		}
	}

	public boolean isBannedByWarn() {
		ConfigAccessor playerData = new ConfigAccessor("players" + File.separator + playerName + ".yml");
		return playerData.getConfig().getBoolean("warn.isBanned", false);
	}

	public int getWarnPoints() {
		ConfigAccessor playerData = new ConfigAccessor("players" + File.separator + playerName + ".yml");
		return playerData.getConfig().getInt("warn.points", 0);
	}

	public void setWarnPoints(int points) {
		ConfigAccessor playerData = new ConfigAccessor("players" + File.separator + playerName + ".yml");
		playerData.getConfig().set("warn.points", points);
		playerData.saveConfig();
	}

	public void removeWarnPoint(int points) {
		ConfigAccessor playerData = new ConfigAccessor("players" + File.separator + playerName + ".yml");
		playerData.getConfig().set("warn.points", getWarnPoints() - points);
		playerData.saveConfig();
	}

	public void saveWarnEntries(List<WarnEntry> entries) {
		ConfigAccessor playerData = new ConfigAccessor("players" + File.separator + playerName + ".yml");

		playerData.getConfig().set("warn.entries", null);
		playerData.saveConfig();

		int index = 1;
		for(WarnEntry warnEntry : entries) {
			if(index > 10) break;
			playerData.getConfig().set("warn.entries." + index + ".mod", warnEntry.getMod());
			playerData.getConfig().set("warn.entries." + index + ".message", warnEntry.getMessage());
			playerData.getConfig().set("warn.entries." + index + ".timeStamp", warnEntry.getTimeStamp());
			index++;
		}
		playerData.saveConfig();
	}

	public List<WarnEntry> getWarnEntries() {
		ConfigAccessor playerData = new ConfigAccessor("players" + File.separator + playerName + ".yml");

		List<WarnEntry> warnEntries = new ArrayList<WarnEntry>();

		ConfigurationSection cs = playerData.getConfig().getConfigurationSection("warn.entries");
		if(cs != null) {
			for(String warnEntry : cs.getKeys(false)) {
				ConfigurationSection entryData = cs.getConfigurationSection(warnEntry);
				if (entryData != null) {
					warnEntries.add(new WarnEntry(entryData.getString("mod"), entryData.getString("message"), entryData.getString("timeStamp")));
				}
			}
		}
		return warnEntries;
	}

	public void setSeeAllChat(boolean allChat) {
		ConfigAccessor playerData = new ConfigAccessor("players" + File.separator + playerName + ".yml");
		playerData.getConfig().set("seeAllChat", allChat);
		playerData.saveConfig();
	}

	public boolean isSeeingAllChat() {
		ConfigAccessor playerData = new ConfigAccessor("players" + File.separator + playerName + ".yml");
		return playerData.getConfig().getBoolean("seeAllChat", false);
	}

	public void updateTotalTime() {
		ConfigAccessor playerData = new ConfigAccessor("players" + File.separator + playerName + ".yml");
		playerData.getConfig().set("totalTime", getTotalTime() + getPlayTime());
		playerData.saveConfig();
	}

	public long getTotalTime() {
		ConfigAccessor playerData = new ConfigAccessor("players" + File.separator + playerName + ".yml");
		return playerData.getConfig().getLong("totalTime", 0);
	}

	public boolean hasLogoffXP() {
		ConfigAccessor playerData = new ConfigAccessor("players" + File.separator + playerName + ".yml");
		return playerData.getConfig().contains("logoffXP");
	}

	public int getLogoffXP() {
		ConfigAccessor playerData = new ConfigAccessor("players" + File.separator + playerName + ".yml");
		return playerData.getConfig().getInt("logoffXP", 0);
	}

	public void setLogoffXP(int amount) {
		ConfigAccessor playerData = new ConfigAccessor("players" + File.separator + playerName + ".yml");
		playerData.getConfig().set("logoffXP", amount);
		playerData.saveConfig();
	}

	public void addRecentCommand(String command) {
		List<String> commands = getRecentCommands();
		commands.add(0, command);
		while(commands.size() > 10) {
			commands.remove(commands.size()-1);
		}
		setRecentCommands(commands);
	}

	public void setRecentCommands(List<String> commands) {
		ConfigAccessor playerData = new ConfigAccessor("players" + File.separator + playerName + ".yml");
		playerData.getConfig().set("recentCommands", commands);
		playerData.saveConfig();
	}

	public List<String> getRecentCommands() {
		List<String> commands = new ArrayList<String>();
		ConfigAccessor playerData = new ConfigAccessor("players" + File.separator + playerName + ".yml");
		commands = playerData.getConfig().getStringList("recentCommands");
		return commands;
	}

	public boolean getAutohideWeather() {
		ConfigAccessor playerData = new ConfigAccessor("players" + File.separator + playerName + ".yml");
		return playerData.getConfig().getBoolean("autohideWeather", false);
	}

	public void setAutohideWeather(boolean autohide) {
		ConfigAccessor playerData = new ConfigAccessor("players" + File.separator + playerName + ".yml");
		playerData.getConfig().set("autohideWeather", autohide);
		playerData.saveConfig();
	}

	public void setLastDeathLoc(Location death) {
		ConfigAccessor playerData = new ConfigAccessor("players" + File.separator + playerName + ".yml");
		String deathLoc = Integer.toString((int) death.getX()) + "," + Integer.toString((int) death.getY()) + "," + Integer.toString((int) death.getZ()) + "," + death.getWorld().getName();
		playerData.getConfig().set("lastDeathLoc", deathLoc);
		playerData.saveConfig();
	}

	public String getLastDeathLocStr() {
		ConfigAccessor playerData = new ConfigAccessor("players" + File.separator + playerName + ".yml");
		return playerData.getConfig().getString("lastDeathLoc", "");
	}

	public Location getLastDeathLoc() {
		String deathLoc = getLastDeathLocStr();
		if(deathLoc.isEmpty()) return null;
		String[] points = deathLoc.split(",");
		World deathWorld = Bukkit.getWorld(points[3]);
		Location death = new Location(deathWorld, Double.parseDouble(points[0]), Double.parseDouble(points[1]), Double.parseDouble(points[2]));
		return death;
	}

	public void setLogoffBalance(double balance) {
		ConfigAccessor playerData = new ConfigAccessor("players" + File.separator + playerName + ".yml");
		playerData.getConfig().set("logoffBalance", balance);
		playerData.saveConfig();
	}

	public double getLogoffBalance() {
		ConfigAccessor playerData = new ConfigAccessor("players" + File.separator + playerName + ".yml");
		if(playerData.getConfig().contains("logoffBalance")) {
			return playerData.getConfig().getDouble("logoffBalance", 0.0);
		} else {
			return -1;
		}
	}

	public void setJoinTime(String time) {
		ConfigAccessor playerData = new ConfigAccessor("players" + File.separator + playerName + ".yml");
		playerData.getConfig().set("joinTime", time);
		playerData.saveConfig();
	}

	public boolean hasNewLoginLoc() {
		ConfigAccessor playerData = new ConfigAccessor("players" + File.separator + playerName + ".yml");
		if(playerData.getConfig().contains("newLoginLoc")) return true;
		return false;
	}

	public void saveLastKnownLocation(Location loc) {
		ConfigAccessor playerData = new ConfigAccessor("players" + File.separator + playerName + ".yml");
		playerData.getConfig().set("lastKnownLoc", Utils.locationToString(loc));
		playerData.saveConfig();
	}

	public Location getLastKnownLocation() {
		ConfigAccessor playerData = new ConfigAccessor("players" + File.separator + playerName + ".yml");
		if(!playerData.getConfig().contains("lastKnownLoc")) {
			return null;
		}
		return Utils.stringToLocation(playerData.getConfig().getString("lastKnownLoc"));
	}

	public Location getNewLoginLoc() {
		ConfigAccessor playerData = new ConfigAccessor("players" + File.separator + playerName + ".yml");
		if(playerData.getConfig().contains("newLoginLoc")) {
			String[] points = playerData.getConfig().getString("newLoginLoc").split(",");
			World world = Bukkit.getWorld(points[3]);
			return new Location(world, Double.parseDouble(points[0]), Double.parseDouble(points[1]), Double.parseDouble(points[2]));
		}
		return null;
	}
	public void setNewLoginLoc(Location location) {
		ConfigAccessor playerData = new ConfigAccessor("players" + File.separator + playerName + ".yml");
		if(location != null) {
			String loginLocStr = Integer.toString((int) location.getX()) + "," + Integer.toString((int) location.getY()) + "," + Integer.toString((int) location.getZ()) + "," + location.getWorld().getName();
			playerData.getConfig().set("newLoginLoc", loginLocStr);
		} else {
			playerData.getConfig().set("newLoginLoc", null);
		}
		playerData.saveConfig();
	}

	private void createFile(String path, String fileName) {
		(new File(path)).mkdirs();
		File file = new File(path + File.separator + fileName);

		if (!file.exists()) {
			try {
				file.createNewFile();
				System.out.println("[Applessentials] Created \"" + fileName + "\"");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
