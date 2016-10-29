package com.gmail.erikbigler.applessentials.interactivesigns;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import com.gmail.erikbigler.applessentials.utils.ConfigAccessor;
import com.gmail.erikbigler.applessentials.utils.Utils;

public class SignManager {

	private ArrayList<InteractiveSign> signs = new ArrayList<InteractiveSign>();

	public void loadSign(String name, ConfigurationSection cs) {
		signs.add(new InteractiveSign(name, Utils.stringToLocation(cs.getString("location")), cs.getString("command")));
	}

	public void addSign(String name, Location loc, String command) {
		signs.add(new InteractiveSign(name, loc, command));
		updateSignStorage();
	}

	public InteractiveSign getSignAtLocation(Location loc) {
		for(InteractiveSign sign : signs) {
			if(sign.getLocation().equals(loc)) return sign;
		}
		return null;
	}

	public boolean nameIsTaken(String name) {
		for(InteractiveSign sign : signs) {
			if(sign.getName().equalsIgnoreCase(name)) return true;
		}
		return false;
	}

	public void removeSign(InteractiveSign is) {
		if(signs.contains(is)) {
			signs.remove(is);
			updateSignStorage();
		}
	}

	void updateSignStorage() {
		ConfigAccessor signData = new ConfigAccessor("signs.yml");
		signData.getConfig().set("Signs", null);
		signData.saveConfig();
		for(InteractiveSign sign : signs) {
			signData.getConfig().set("Signs." + sign.getName() + ".location", Utils.locationToString(sign.getLocation()));
			signData.getConfig().set("Signs." + sign.getName() + ".command", sign.getCommand());
		}
		signData.saveConfig();
	}

}
