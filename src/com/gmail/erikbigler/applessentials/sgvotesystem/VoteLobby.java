package com.gmail.erikbigler.applessentials.sgvotesystem;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.gmail.erikbigler.applessentials.Applessentials;
import com.gmail.erikbigler.applessentials.bossbar.BarAPI;
import com.gmail.erikbigler.applessentials.utils.Time;

public class VoteLobby {

	private Scoreboard sb;
	private Objective objective;
	private ScoreboardManager manager = Bukkit.getScoreboardManager();
	private boolean voteInProgress = false;
	//private Game currentGame;
	private Queue<Player> queue = new LinkedList<Player>();
	private HashMap<String, ArenaTotal> voteTotals = new HashMap<String, ArenaTotal>();
	private Inventory voteBallot;
	private String voteStartTimeStamp;
	private BukkitRunnable voteTimer;
	private Plugin plugin = Bukkit.getPluginManager().getPlugin("Applessentials");

	enum VoteState {
		GAME_INPROGRESS, GAME_WAITING, VOTE_INPROGRESS, VOTE_WAITING
	}

	void startVote() {
		if(voteInProgress) return;
		voteTotals.clear();
		this.voteInProgress = true;
		List<String> availArenas = getAvailableArenas();
		plugin.reloadConfig();
		for(String arena : availArenas) {
			String nick;
			nick = plugin.getConfig().getString("sg-vote-lobbies.arena-nicknames." + arena);
			if(nick == null) {
				nick = "";
			}
			voteTotals.put(arena, new ArenaTotal(arena, nick));
		}
		this.voteBallot = this.getVoteBallot(availArenas);
		createScoreboard();
		for(Player player : queue) {
			player.setScoreboard(sb);
			player.openInventory(voteBallot);
			Applessentials.lookingVoteBallot.add(player);
			BarAPI.setMessage(player, ChatColor.YELLOW + "Vote on a Survival Games Map! Type " + ChatColor.GREEN + "/map vote", 30);
		}
		voteStartTimeStamp = Time.getTimeWithSecs();

		if(voteTimer != null) {
			voteTimer.cancel();
		}

		voteTimer = new VoteTimer();
		voteTimer.runTaskLater(Bukkit.getPluginManager().getPlugin("Applessentials"), 30*20);

		//create vote ballot
	}

	public void applyVoteTotal(String arenaID, String player) {
		for(String arena : this.voteTotals.keySet()) {
			voteTotals.get(arena).removeVoter(player);
		}
		voteTotals.get(arenaID).addVoter(player);
		this.updateScoreboard();
	}

	public void removePlayerVote(String player) {
		if(voteTotals != null && !voteTotals.isEmpty()) {
			for(String arena : this.voteTotals.keySet()) {
				voteTotals.get(arena).removeVoter(player);
			}
			this.updateScoreboard();
		}
	}

	void createScoreboard() {
		sb = manager.getNewScoreboard();
		sb.registerNewObjective("InfoPanel", "dummy");
		objective = sb.getObjective("InfoPanel");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName(ChatColor.GOLD + "SG Map Votes");
		for(String arena : this.voteTotals.keySet()) {
			ArenaTotal vt = this.voteTotals.get(arena);
			String name = vt.getNick();
			if(name == null || name.isEmpty()) {
				name = arena;
			}
			if(name.length() > 16) {
				name = name.substring(0, 12) + "...";
			}
			System.out.println(name);
			objective.getScore(name).setScore(0);
		}
	}

	public void updateScoreboard() {

		for(String arena : this.voteTotals.keySet()) {
			ArenaTotal vt = this.voteTotals.get(arena);
			String name = vt.getNick();
			if(name == null || name.isEmpty()) {
				name = arena;
			}
			if(name.length() > 16) {
				name = name.substring(0, 12) + "...";
			}
			objective.getScore(name).setScore(voteTotals.get(arena).getVotes());
		}
	}

	public void voteEnd() {

 /*
		this.voteInProgress = false;

		//clear scoreboard
		for(Player player : queue) {
			player.setScoreboard(manager.getNewScoreboard());
			player.closeInventory();
		}

		//get winner map, if tied, get random
		List<ArenaTotal> totals = new ArrayList<ArenaTotal>();
		for(String arena : this.voteTotals.keySet()) {
			totals.add(voteTotals.get(arena));
		}

		//sort list by most votes
		Collections.sort(totals, new Comparator<ArenaTotal>() {
			public int compare(ArenaTotal at1, ArenaTotal at2) {
				return at2.getVotes() - at1.getVotes();
			}
		});

		ArenaTotal winner;
		ArenaTotal[] tied = getTiedTotals(totals);
		if(tied.length > 1) {
			Random r = new Random();
			winner = tied[r.nextInt(tied.length-1)];
		} else {
			winner = tied[0];
		}

		Game first = GameManager.getInstance().getGame(Integer.parseInt(winner.getID()));

		this.currentGame = first;

		while(first.getActivePlayers() < SettingsManager.getInstance().getSpawnCount(first.getID())) {
			if(queue.isEmpty()) break;
			Player p = queue.remove();
			if(p == null) break;
			if(currentGame.hasPlayer(p)) continue;
			currentGame.addPlayer(p);
		}

		if(!queue.isEmpty()) {
			int index = 1;
			for(Player p : queue) {
				p.sendMessage(ChatColor.GREEN + "[SG Vote Lobby] The voted upon arena is now full. You are #" + index + " in the queue for the next game.");
				index++;
			}
		}
		Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("Applessentials"), new Runnable() {
			@Override
			public void run() {
				checkForEmptyMap();
			}
		}, 20*60);
		*/
	}

	void checkForEmptyMap() {
		/*
		if(currentGame != null) {
			if(currentGame.getGameMode() == GameMode.WAITING) {
				if(currentGame.getActivePlayers() < 1) {
					currentGame = null;
				} else {
					Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("Applessentials"), new Runnable() {
						@Override
						public void run() {
							checkForEmptyMap();
						}
					}, 20*60);
				}
			}
		}
		*/
	}

	void cancelVote() {
		voteTimer.cancel();
	}

	private ArenaTotal[] getTiedTotals(List<ArenaTotal> totals) {
		List<ArenaTotal> tied = new ArrayList<ArenaTotal>();
		int first = totals.get(0).getVotes();
		for(ArenaTotal total: totals) {
			if(total.getVotes() == first) {
				tied.add(total);
			}
		}
		ArenaTotal[] tiedA = new ArenaTotal[tied.size()];
		tied.toArray(tiedA);
		return tiedA;
	}

	private Inventory getVoteBallot(List<String> availArenas) {

		int multOf9 = 9;
		int req = availArenas.size();
		while(req > multOf9) {
			multOf9 += 9;
		}

		Inventory i = Bukkit.createInventory(null, multOf9, ChatColor.GREEN + "Vote On A Map!");

		int index = 0;
		for(String arena : availArenas) {
			ItemStack details = new ItemStack(Material.PAPER);
			ItemMeta detailsItemMeta = details.getItemMeta();
			String name = arena;
			String nick = plugin.getConfig().getString("sg-vote-lobbies.arena-nicknames." + arena);
			if(nick != null && !nick.isEmpty()) {
				name += ": " + nick;
			}
			detailsItemMeta.setDisplayName(name);
			List<String> detailsLore = new ArrayList<String>();
			detailsLore.add("*click to vote*");
			detailsItemMeta.setLore(detailsLore);
			details.setItemMeta(detailsItemMeta);
			i.setItem(index, details);
			index++;
		}
		return i;
	}

	private List<String> getAvailableArenas() {
		List<String> arenas = new ArrayList<String>();
		/*
		for(Game game : GameManager.getInstance().getGames()) {
			GameMode gm = game.getGameMode();
			if(gm == GameMode.WAITING) {
				arenas.add(Integer.toString(game.getID()));
			}
		}
		*/
		return arenas;
	}


	public void addPlayerToLobby(Player p) {
		/*
		VoteState vs = this.getVoteState();
		p.sendMessage(ChatColor.GREEN + "[SG Vote Lobby] Joined the vote lobby!");
		if(vs == VoteState.GAME_WAITING) {
			GameMode currentGM = currentGame.getGameMode();
			if(currentGM == GameMode.WAITING || currentGM == GameMode.STARTING) {
				if (currentGame.getActivePlayers() < SettingsManager.getInstance().getSpawnCount(currentGame.getID())) {
					p.sendMessage(ChatColor.GREEN + "[SG Vote Lobby] A map has already been choosen. You have been autoadded to the game.");
					if(!currentGame.hasPlayer(p)) {
						currentGame.addPlayer(p);
					}
					return;
				}
			}
			currentGame = null;
			queue.add(p);
			p.sendMessage(ChatColor.GREEN + "[SG Vote Lobby] There is currently a game in progress. You have been added to the queue for the next game.");
		} else if(vs == VoteState.GAME_INPROGRESS) {
			currentGame = null;
			queue.add(p);
			p.sendMessage(ChatColor.GREEN + "[SG Vote Lobby] There is currently a game in progress. You have been added to the queue for the next game.");
		} else if(vs == VoteState.VOTE_WAITING) {
			currentGame = null;
			queue.add(p);
			if(queue.size() < 2) {
				p.sendMessage(ChatColor.GREEN + "[SG Vote Lobby] 1 more person is needed to start a map vote!");
			} else {
				this.startVote();
				p.sendMessage(ChatColor.GREEN + "[SG Vote Lobby] A vote has started! Please choose a map.");
			}
		} else if(vs == VoteState.VOTE_INPROGRESS) {
			currentGame = null;
			queue.add(p);
			p.sendMessage(ChatColor.GREEN + "[SG Vote Lobby] A vote is in progress, please choose a map!");
			p.setScoreboard(sb);
			p.openInventory(voteBallot);
			Applessentials.lookingVoteBallot.add(p);

			int timeSince = 0;
			try {
				timeSince = Time.compareTimeSecs(this.voteStartTimeStamp);
			} catch (ParseException e) {
			}

			int newInterval = 30 - timeSince;
			BarAPI.setMessage(p, ChatColor.YELLOW + "Vote on a Survival Games Map! Type " + ChatColor.GREEN + "/map vote", newInterval);

		}
		*/
	}

	public boolean playerIsInLobby(Player p) {
		return this.queue.contains(p);
	}

	public void removePlayerFromLobby(Player p) {
		if(this.queue.contains(p)) {
			this.queue.remove(p);
			p.setScoreboard(manager.getNewScoreboard());
			p.sendMessage(ChatColor.GREEN + "[SG Vote Lobby] Left the vote lobby!");
		}
		BarAPI.removeBar(p);
		this.removePlayerVote(p.getName());
		if(this.queue.isEmpty() && this.voteInProgress) {
			this.cancelVote();
			this.voteInProgress = false;
		}
	}

	public VoteState getVoteState() {
		if(aGameIsInProgress()) {
			return VoteState.GAME_INPROGRESS;
		}
		/*else if(currentGame != null) {
			return VoteState.GAME_WAITING;
		}*/
		else if (voteInProgress){
			return VoteState.VOTE_INPROGRESS;
		}
		else {
			return VoteState.VOTE_WAITING;
		}
	}

	private boolean aGameIsInProgress() {
		/*for(Game game : GameManager.getInstance().getGames()) {
			GameMode gm = game.getGameMode();
			if(gm == GameMode.INGAME || gm == GameMode.FINISHING) {
				return true;
			}
		}*/
		return false;
	}

	public Queue<Player> getVoteLobbyQueue() {
		return this.queue;
	}

	public Inventory getVoteBallot() {
		return this.voteBallot;
	}

	public HashMap<String, ArenaTotal> getVoteTotals() {
		return this.voteTotals;
	}

	private class VoteTimer extends BukkitRunnable {
		@Override
		public void run() {
			Applessentials.getVoteLobby().voteEnd();
		}
	}

	public boolean voteInProgress() {
		return this.voteInProgress;
	}
}
