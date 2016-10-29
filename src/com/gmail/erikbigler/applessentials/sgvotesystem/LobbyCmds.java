package com.gmail.erikbigler.applessentials.sgvotesystem;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.erikbigler.applessentials.Applessentials;


public class LobbyCmds implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		if(commandLabel.equalsIgnoreCase("map")) {
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("vote") || args[0].equalsIgnoreCase("v")) {
					Player p = (Player) sender;
					if(Applessentials.getVoteLobby().playerIsInLobby(p)) {
						if(Applessentials.getVoteLobby().voteInProgress()) {
							p.openInventory(Applessentials.getVoteLobby().getVoteBallot());
							Applessentials.lookingVoteBallot.add(p);
						}
					}
				}
			}
		}
		return true;
	}
}
