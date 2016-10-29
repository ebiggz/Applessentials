package com.gmail.erikbigler.applessentials.spawncompass;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;


public class MenuManager {

	private HashMap<Integer, CompassItem> menuItems = new HashMap<Integer, CompassItem>();
	private int menuRows = 1;
	private String menuName;


	public void addItem(int slot, CompassItem item) {
		menuItems.put(slot, item);
	}

	public void clearMenuItems() {
		menuItems.clear();
	}

	public void setMenuData(int rows, String name) {
		this.menuRows = rows;
		this.menuName = ChatColor.translateAlternateColorCodes('&', name);
	}

	public Inventory getMenuForPlayer(Player player) {
		Inventory i = Bukkit.createInventory(null, 9*menuRows, menuName);
		for(int slot : menuItems.keySet()) {
			CompassItem item = menuItems.get(slot);
			if(!item.isValid()) continue;
			if(!item.isEnabled()) continue;
			if(item.hasPermission()) {
				if(!player.hasPermission(item.getPermission())) continue;
			}
			i.setItem(slot, item.getItem());
		}
		return i;
	}

	public CompassItem getClickedItemAtSlot(int slot, Player player) {
		if(menuItems.containsKey(slot)) {
			CompassItem item = menuItems.get(slot);
			if(!item.isValid()) return null;
			if(!item.isEnabled()) return null;
			if(item.hasPermission()) {
				if(player.hasPermission(item.getPermission())) {
					return item;
				} else {
					return null;
				}
			} else {
				return item;
			}
		} else {
			return null;
		}
	}
	public String getCompassMenuName() {
		return this.menuName;
	}
}
