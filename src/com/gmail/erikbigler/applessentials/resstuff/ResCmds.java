package com.gmail.erikbigler.applessentials.resstuff;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.bekvon.bukkit.residence.Residence;


public class ResCmds implements CommandExecutor  {

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		if(commandLabel.equalsIgnoreCase("resmax")) {
			//get some vars
			Player player = (Player) sender;
			Location loc = player.getLocation();
			int maxX = Residence.getPermissionManager().getGroup(player).getMaxX()/2-1;
			int maxZ = Residence.getPermissionManager().getGroup(player).getMaxZ()/2-1;

			//make selection
			Residence.getSelectionManager().placeLoc1(player, new Location(player.getWorld(), loc.getX()+maxX+1, 256.0, loc.getZ()+maxZ+1));
			Residence.getSelectionManager().placeLoc2(player, new Location(player.getWorld(), loc.getX()-maxX, 1.0, loc.getZ()-maxZ));
			Residence.getSelectionManager().showSelectionInfo(player);
		}

		else if(commandLabel.equalsIgnoreCase("restool")) {
			if(sender.hasPermission("applecraft.restool")) {
				giveTool((Player) sender);
			} else {
				sender.sendMessage(ChatColor.RED + "Sorry, you don't have permission to use the res tool command.");
			}
		}
		return true;
	}

	public boolean giveTool(Player player)
	{
		ItemStack handitem = player.getItemInHand();
		Inventory inv = player.getInventory();

		if (!handitem.getType().equals(Material.WOOD_HOE))
		{
			if (!handitem.getType().equals(Material.AIR))
			{
				if (inv.firstEmpty() == -1)
				{
					player.sendMessage(ChatColor.RED + "No space in your inventory.");
					return false;
				}

				inv.setItem(inv.firstEmpty(), handitem);
			}

			if (inv.contains(Material.WOOD_HOE))
			{
				Integer slotId = inv.first(Material.WOOD_HOE);
				ItemStack stack = inv.getItem(slotId);
				Integer stackAmount = stack.getAmount();

				if (stackAmount > 1)
				{
					stack.setAmount(stackAmount - 1);
				}
				else
				{
					inv.clear(slotId);
				}
			}

			player.setItemInHand(new ItemStack(Material.WOOD_HOE, 1));
			player.sendMessage(ChatColor.GOLD + "Here's your res tool.");
		}
		else
		{
			player.sendMessage(ChatColor.RED + "You already have the res tool in your hand!");
			return false;
		}

		return true;
	}

}
