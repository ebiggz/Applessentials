package com.gmail.erikbigler.applessentials.bossbar;

import java.util.HashMap;
import java.util.List;


public class BarMessageHandler {

	private HashMap<String,BarMessageData> worldMessages = new HashMap<String,BarMessageData>();

	public void clearWorldMessages() {
		worldMessages.clear();
	}
	public void addWorldMessages(String worldName, List<String> messages, int interval) {
		worldMessages.put(worldName, new BarMessageData(worldName, messages, interval));
	}

	public HashMap<String,BarMessageData> getWorldMessages() {
		return worldMessages;
	}

}
