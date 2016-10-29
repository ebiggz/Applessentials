package com.gmail.erikbigler.applessentials.sgvotesystem;

import java.util.ArrayList;
import java.util.List;


public class ArenaTotal {

	private List<String> votes = new ArrayList<String>();
	private String id;
	private String nick;

	public ArenaTotal(String arenaID, String nick) {
		id = arenaID;
		this.nick = nick;
	}

	public String getID() {
		return id;
	}

	public String getNick() {
		return nick;
	}

	public void addVoter(String name) {
		if(votes.contains(name)) return;
		votes.add(name);
	}

	public void removeVoter(String name) {
		if(votes.contains(name)) {
			votes.remove(name);
		}
	}

	public List<String> getVoters() {
		return votes;
	}

	public int getVotes() {
		return votes.size();
	}

	public void clearVoters() {
		votes.clear();
	}

}
