package com.gmail.erikbigler.applessentials.interactivesigns;

import org.bukkit.Location;


public class InteractiveSign {

	private String command;
	private Location location;
	private String name;


	public InteractiveSign(String name, Location location, String command) {
		this.command = command;
		this.location = location;
		this.name = name;
	}

	public String getCommand() {
		return command;
	}

	public String getName() {
		return name;
	}

	public Location getLocation() {
		return location;
	}
}
