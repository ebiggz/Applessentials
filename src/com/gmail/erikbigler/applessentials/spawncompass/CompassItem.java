package com.gmail.erikbigler.applessentials.spawncompass;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.erikbigler.applessentials.Applessentials;


public class CompassItem {

	private ItemStack item;
	private int slot;
	private boolean hasTeleport = false;
	private Location tpLoc;
	private boolean hasCommands = false;
	private List<String> commands;
	private String permission;
	private boolean hasPermission;
	private boolean isValid = false;
	private boolean isEnabled = true;

	@SuppressWarnings("deprecation")
	public CompassItem(ConfigurationSection itemData, int slot) {
		this.slot = slot;
		//get item data
		if(itemData.contains("item")) {
			String itemValueData = itemData.getString("item.id");
			try {
				if(itemValueData.contains(":")) {
					String[] split = itemValueData.split(":");
					item = new ItemStack(Material.getMaterial(Integer.parseInt(split[0])), 1, Short.parseShort(split[1]));
				} else {
					item = new ItemStack(Material.getMaterial(Integer.parseInt(itemValueData)));
				}
				isValid = true;
			} catch (Exception e) {
				Applessentials.getPlugin().getLogger().warning("There was an issue making the item for slot: " + slot);
				return;
			}
			ItemMeta im = item.getItemMeta();
			if(itemData.contains("item.name")) {
				im.setDisplayName(ChatColor.translateAlternateColorCodes('&', itemData.getString("item.name")));
			}
			if(itemData.contains("item.lore")) {
				List<String> lore = itemData.getStringList("item.lore");
				for(int i = 0; i < lore.size(); i++) {
					lore.set(i, ChatColor.translateAlternateColorCodes('&', lore.get(i)));
				}
				im.setLore(lore);
			}
			item.setItemMeta(im);
		}
		//check for click actions
		if(itemData.contains("teleport")) {
			try {
				hasTeleport = true;
				tpLoc = new Location(Bukkit.getWorld(itemData.getString("teleport.world")), itemData.getDouble("teleport.x"), itemData.getDouble("teleport.y"), itemData.getDouble("teleport.z"), Float.parseFloat(itemData.getString("teleport.yaw")), Float.parseFloat(itemData.getString("teleport.pitch")));
			} catch (Exception e) {
				Applessentials.getPlugin().getLogger().warning("There was an issue making the tp location for slot: " + slot);
			}
		}
		if(itemData.contains("commands")) {
			hasCommands = true;
			commands = itemData.getStringList("commands");
		}
		if(itemData.contains("permission")) {
			permission = itemData.getString("permission");
			hasPermission = true;
		}
		this.isEnabled = itemData.getBoolean("enabled", true);
	}

	public ItemStack getItem() {
		return item;
	}

	public int getSlot() {
		return slot;
	}

	public boolean hasTeleport() {
		return hasTeleport;
	}

	public Location getTpLoc() {
		return tpLoc;
	}

	public boolean hasCommands() {
		return hasCommands;
	}

	public List<String> getCommands() {
		return commands;
	}

	public String getPermission() {
		return permission;
	}

	public boolean hasPermission() {
		return hasPermission;
	}

	public boolean isValid() {
		return isValid;
	}

	public boolean isEnabled() {
		return isEnabled;
	}
}
