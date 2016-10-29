package com.gmail.erikbigler.applessentials.spawncompass;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.erikbigler.applessentials.Applessentials;
import com.gmail.erikbigler.applessentials.Slice;
import com.gmail.erikbigler.applessentials.utils.Utils;


public class CompassHandler implements Listener {

	@EventHandler(priority=EventPriority.MONITOR)
	public void spawnInvClick(InventoryClickEvent e) {
		if(!(e.getWhoClicked() instanceof Player)) return;
		Player p = (Player) e.getWhoClicked();
		if(p.getWorld() != Applessentials.getSpawnWorld()) return;
		if(p.getGameMode() == GameMode.CREATIVE) return;
		e.setCancelled(true);

		if(Applessentials.playerCompassMenu.contains(p)) {
			int slot = e.getSlot();
			CompassItem citem = Applessentials.getMenuManager().getClickedItemAtSlot(slot, p);
			if(citem == null) return;
			if(citem.hasTeleport()) {
				p.teleport(citem.getTpLoc());
			}
			if(citem.hasCommands()) {
				for(String command : citem.getCommands()) {
					p.performCommand(command);
				}
			}
			p.closeInventory();
			if(Applessentials.playerCompassMenu.contains(p)) {
				Applessentials.playerCompassMenu.remove(p);
			}
		}
	}

	@EventHandler(priority=EventPriority.MONITOR)
	public void invSettingsClick(InventoryClickEvent e) {
		if(!(e.getWhoClicked() instanceof Player)) return;
		Player p = (Player) e.getWhoClicked();
		if(Applessentials.playerSettingsMenu.contains(p)) {
			e.setCancelled(true);
			int slot = e.getSlot();
			if(slot == 1 || slot == 10) {
				p.performCommand("hideplayers");
				e.getInventory().setContents(Utils.getPlayerSettingsInventory(p.getName()).getContents());
			}
			else if(slot == 3 || slot == 12) {
				p.performCommand("hidechat");
				e.getInventory().setContents(Utils.getPlayerSettingsInventory(p.getName()).getContents());
			}
			else if(slot == 5 || slot == 14) {
				p.performCommand("hidepms");
				e.getInventory().setContents(Utils.getPlayerSettingsInventory(p.getName()).getContents());
				//p.openInventory(Utils.getPlayerSettingsInventory(p.getName()));
			}
			else if(slot == 7 || slot == 16) {
				p.performCommand("friends requests");
				e.getInventory().setContents(Utils.getPlayerSettingsInventory(p.getName()).getContents());
			}
			if(!Applessentials.playerSettingsMenu.contains(p)) {
				Applessentials.playerSettingsMenu.add(p);
			}
		}
	}

	@EventHandler(priority=EventPriority.MONITOR)
	public void itemDrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if(p.getWorld() != Applessentials.getSpawnWorld()) return;
		if(p.getGameMode() == GameMode.CREATIVE) return;
		e.setCancelled(true);
		if(p.hasPermission("applecraft.spawnmenudisable")) {
			p.sendMessage(ChatColor.RED + "[AppleCraft] Inventory interaction is disabled in spawn. Please switch to creative mode to disable this functionality.");
		}
	}

	@EventHandler(priority=EventPriority.MONITOR)
	public void itemPickup(PlayerPickupItemEvent e) {
		Player p = e.getPlayer();
		if(p.getWorld() != Applessentials.getSpawnWorld()) return;
		if(p.getGameMode() == GameMode.CREATIVE) return;
		e.setCancelled(true);
	}

	@EventHandler(priority=EventPriority.MONITOR)
	public void gamemodeChange(PlayerGameModeChangeEvent e) {
		Player p = e.getPlayer();
		if(p.getWorld() != Applessentials.getSpawnWorld()) return;
		if(e.getNewGameMode() == GameMode.SURVIVAL) {
			this.setSpawnInventory(p);
		}
		else if(e.getNewGameMode() == GameMode.CREATIVE) {
			p.getInventory().clear();
		}
	}

	@EventHandler(priority=EventPriority.MONITOR)
	public void invClose(InventoryCloseEvent e) {
		if(e.getPlayer() == null) return;
		if(!(e.getPlayer() instanceof Player)) return;
		Player p = (Player) e.getPlayer();
		if(Applessentials.playerSettingsMenu.contains(p)) {
			Applessentials.playerSettingsMenu.remove(p);
		}
		if(Applessentials.playerCompassMenu.contains(p)) {
			Applessentials.playerCompassMenu.remove(p);
		}
	}

	@EventHandler(priority=EventPriority.LOWEST)
	public void leftClick(EntityDamageByEntityEvent event) {
		if(!(event.getDamager() instanceof Player)) return;
		if(!(event.getEntity() instanceof Player)) return;
		Player damager = (Player) event.getDamager();
		Player damagee = (Player) event.getEntity();
		if(damager.getWorld() != Applessentials.getSpawnWorld()) return;
		if(damager.getItemInHand() != null) {
			if(damager.getItemInHand().getType() != Material.AIR) {
				if(damager.getItemInHand().getType() == Material.SKULL_ITEM) {
					damager.performCommand("friends add " + damagee.getName());
				}
			}
		}
	}

	@EventHandler(priority=EventPriority.LOWEST)
	public void rightClick(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(p.getWorld() != Applessentials.getSpawnWorld()) return;
		if(p.getGameMode() == GameMode.CREATIVE) return;
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(p.getItemInHand() != null) {
				if(p.getItemInHand().getType() != Material.AIR) {
					if(p.getItemInHand().getType() == Material.COMPASS) {
						if(!Applessentials.playerCompassMenu.contains(p)) {
							Applessentials.playerCompassMenu.add(p);
						}
						Inventory menu = Applessentials.getMenuManager().getMenuForPlayer(p);
						p.openInventory(menu);
					}
					else if(p.getItemInHand().getType() == Material.REDSTONE_COMPARATOR) {
						p.performCommand("settings");
					}
					else if(p.getItemInHand().getType() == Material.SKULL_ITEM) {
						p.performCommand("friends");
					}
					e.setCancelled(true);
				}
			}
		}
	}


	@EventHandler (priority = EventPriority.MONITOR)
	public void onJoin(final PlayerJoinEvent event) {
		if(event.getPlayer().getWorld() == Applessentials.getSpawnWorld()) {
			this.setSpawnInventory(event.getPlayer());
		}
		Slice slice = Applessentials.getSliceManager().getSlice(event.getPlayer().getName());
		if(slice.isHidingChat()) {
			Applessentials.playersHidingChat.add(event.getPlayer());
		}
		if(slice.isHidingPMs()) {
			Applessentials.playersHidingPMs.add(event.getPlayer());
		}
		if(slice.isHidingPlayers()) {
			Applessentials.playersHidingPlayers.add(event.getPlayer());
			Bukkit.getScheduler().runTaskAsynchronously(Applessentials.getPlugin(), new Runnable(){
				@Override
				public void run() {
					PlayerHider.hideSpawnPlayersFor(event.getPlayer());
				}
			});
		}
	}

	@EventHandler (priority = EventPriority.MONITOR)
	public void onWorldChange(PlayerChangedWorldEvent event) {
		if(event.getPlayer().getWorld() == Applessentials.getSpawnWorld()) {
			//player went to spawn
			this.setSpawnInventory(event.getPlayer());
		}
	}

	@EventHandler (priority = EventPriority.MONITOR)
	public void onRespawn(PlayerRespawnEvent event) {
		if(event.getPlayer().getWorld() == Applessentials.getSpawnWorld()) {
			//player went to spawn
			this.setSpawnInventory(event.getPlayer());
		}
	}

	private void setSpawnInventory(Player player) {
		player.getInventory().clear();

		//compass
		ItemStack compass = new ItemStack(Material.COMPASS);
		ItemMeta compassMeta = compass.getItemMeta();
		compassMeta.setDisplayName(ChatColor.YELLOW + "Hub Menu");
		compassMeta.setLore(Arrays.asList("*Right-click while","holding to see menu*"));
		compass.setItemMeta(compassMeta);
		player.getInventory().setItem(0, compass);

		//player head
		ItemStack playerhead = new ItemStack(Material.SKULL_ITEM);
		playerhead.setDurability((short) 3);
		ItemMeta playerheadMeta = playerhead.getItemMeta();
		playerheadMeta.setDisplayName(ChatColor.YELLOW + "Friends List");
		playerheadMeta.setLore(Arrays.asList("*Right-click while holding","to see friends*","","*Left-click a player while", "holding to send a request*"));
		playerhead.setItemMeta(playerheadMeta);
		player.getInventory().setItem(4, playerhead);

		//comparator
		ItemStack comparator = new ItemStack(Material.REDSTONE_COMPARATOR);
		ItemMeta comparatorMeta = comparator.getItemMeta();
		comparatorMeta.setDisplayName(ChatColor.YELLOW + "Player Settings");
		comparatorMeta.setLore(Arrays.asList("*Right-click while","holding to see settings*"));
		comparator.setItemMeta(comparatorMeta);
		player.getInventory().setItem(8, comparator);
	}
}
