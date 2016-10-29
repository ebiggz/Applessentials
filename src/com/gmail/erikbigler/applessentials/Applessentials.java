package com.gmail.erikbigler.applessentials;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.erikbigler.applessentials.bossbar.BarMessageHandler;
import com.gmail.erikbigler.applessentials.bossbar.BarMessagePlayerListener;
import com.gmail.erikbigler.applessentials.chatutils.AutoChannel;
import com.gmail.erikbigler.applessentials.chatutils.NSA;
import com.gmail.erikbigler.applessentials.chatutils.Obama;
import com.gmail.erikbigler.applessentials.interactivesigns.ISCmdData;
import com.gmail.erikbigler.applessentials.interactivesigns.SignCommands;
import com.gmail.erikbigler.applessentials.interactivesigns.SignListener;
import com.gmail.erikbigler.applessentials.interactivesigns.SignManager;
import com.gmail.erikbigler.applessentials.listeners.PlayerListener;
import com.gmail.erikbigler.applessentials.resstuff.ResCmds;
import com.gmail.erikbigler.applessentials.sgvotesystem.VoteLobby;
import com.gmail.erikbigler.applessentials.spawncompass.ChatHider;
import com.gmail.erikbigler.applessentials.spawncompass.CompassHandler;
import com.gmail.erikbigler.applessentials.spawncompass.CompassItem;
import com.gmail.erikbigler.applessentials.spawncompass.FriendsCmds;
import com.gmail.erikbigler.applessentials.spawncompass.MenuManager;
import com.gmail.erikbigler.applessentials.spawncompass.PlayerHider;
import com.gmail.erikbigler.applessentials.toolbox.AdminHelpMenu;
import com.gmail.erikbigler.applessentials.toolbox.MiscCmds;
import com.gmail.erikbigler.applessentials.toolbox.PIMenuData;
import com.gmail.erikbigler.applessentials.toolbox.PlayerInfo;
import com.gmail.erikbigler.applessentials.toolbox.PlayerInfoListener;
import com.gmail.erikbigler.applessentials.toolbox.ServerInfoCommand;
import com.gmail.erikbigler.applessentials.toolbox.WarnPoints;
import com.gmail.erikbigler.applessentials.utils.ConfigAccessor;
import com.gmail.erikbigler.applessentials.utils.FancyMenu;
import com.gmail.erikbigler.applessentials.utils.Time;
import com.gmail.erikbigler.applessentials.utils.Utils;

public class Applessentials extends JavaPlugin {

	public static HashMap<Player, PIMenuData> playerInfoMenus = new HashMap<Player, PIMenuData>();
	public static List<Player> serverInfoMenus = new ArrayList<Player>();
	public static HashMap<Player, ISCmdData> signSelectors = new HashMap<Player, ISCmdData>();
	public static HashMap<Player, Player> friendRequests = new HashMap<Player, Player>();
	public static ArrayList<Player> nonVIPs = new ArrayList<Player>();
	public static List<Player> lookingVoteBallot = new ArrayList<Player>();
	public static List<String> arenaNicks = new ArrayList<String>();
	public static List<Player> playersHidingChat = new ArrayList<Player>();
	public static List<Player> playersHidingPMs = new ArrayList<Player>();
	public static List<Player> playersHidingPlayers = new ArrayList<Player>();
	public static List<Player> disabledSpawnMenu = new ArrayList<Player>();
	public static List<Player> playerSettingsMenu = new ArrayList<Player>();
	public static List<Player> playerCompassMenu = new ArrayList<Player>();
	public static List<Player> playerFriendListMenu = new ArrayList<Player>();

	private static HashMap<String, String> worldAndGamemodes = new HashMap<String, String>();

	public static List<WarnPunishment> warnPunishments = new ArrayList<WarnPunishment>();

	private static SliceManager sm;
	private static SignManager signm;
	private static BarMessageHandler bmh;
	private static MenuManager mm;
	private static World spawnWorld;
	//private static VoteLobby vl;

	public static Economy economy = null;
	public static Chat chat = null;
	public static Permission permission = null;
	public static boolean hasPermPlugin = true;
	public static boolean hasEconPlugin = false;

	private static int	tps = 0;
	private long second = 0;
	private static Plugin plugin;

	public void onDisable() {
		getLogger().info("Disabled!");
		Bukkit.getScheduler().cancelTasks(this);
	}

	public void onEnable() {

		plugin = this;
		spawnWorld = Bukkit.getWorld("diego");
		new Utils();
		new FancyMenu();

		PluginManager pm = getServer().getPluginManager();

		sm = new SliceManager();
		signm = new SignManager();
		bmh = new BarMessageHandler();
		mm = new MenuManager();
		//vl = new VoteLobby();

		getCommand("playerinfo").setExecutor(new PlayerInfo());
		getCommand("pi").setExecutor(new PlayerInfo());
		getCommand("allchat").setExecutor(new Obama());
		getCommand("isign").setExecutor(new SignCommands(this));
		getCommand("colors").setExecutor(new GeneralCmds(this));
		getCommand("resmax").setExecutor(new ResCmds());
		getCommand("restool").setExecutor(new ResCmds());
		getCommand("applecraft").setExecutor(new GeneralCmds(this));
		getCommand("speedtest").setExecutor(new GeneralCmds(this));
		getCommand("com").setExecutor(new AdminHelpMenu());
		getCommand("staff").setExecutor(new AdminHelpMenu());
		getCommand("channels").setExecutor(new AdminHelpMenu());
		getCommand("slap").setExecutor(new MiscCmds());
		getCommand("tplastknown").setExecutor(new MiscCmds());
		//getCommand("map").setExecutor(new LobbyCmds());
		getCommand("warn").setExecutor(new WarnPoints());
		getCommand("warninfo").setExecutor(new WarnPoints());
		getCommand("resetwarn").setExecutor(new WarnPoints());
		getCommand("setwarn").setExecutor(new WarnPoints());
		getCommand("unwarn").setExecutor(new WarnPoints());
		getCommand("serverinfo").setExecutor(new ServerInfoCommand());
		getCommand("si").setExecutor(new ServerInfoCommand());
		getCommand("hideplayers").setExecutor(new PlayerHider());
		getCommand("hidechat").setExecutor(new ChatHider());
		getCommand("hidepms").setExecutor(new ChatHider());
		getCommand("hidepm").setExecutor(new ChatHider());
		getCommand("friends").setExecutor(new FriendsCmds());
		getCommand("settings").setExecutor(new FriendsCmds());

		pm.registerEvents(new PlayerListener(this), this);
		pm.registerEvents(new PlayerInfoListener(), this);
		pm.registerEvents(new SignListener(), this);
		pm.registerEvents(new PlayerHider(), this);
		pm.registerEvents(new ChatHider(), this);
		pm.registerEvents(new NSA(this), this);
		pm.registerEvents(new AutoChannel(), this);
		pm.registerEvents(new BarMessagePlayerListener(), this);
		pm.registerEvents(new CompassHandler(), this);
		pm.registerEvents(new FriendsCmds(), this);
		//pm.registerEvents(new LobbyListener(), this);

		//setup vault
		if(!setupVault()) {
			//disable if not
			pm.disablePlugin(this);
			return;
		}

		loadFilesAndData();

		new Time(this);
		ConfigAccessor statsData = new ConfigAccessor("stats.yml");
		statsData.getConfig().set("server-start-timestamp", Time.getTimeWithSecs());
		statsData.saveConfig();

		// in onEnable()
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
		{
			long sec;
			int ticks;

			@Override
			public void run()
			{
				sec = (System.currentTimeMillis() / 1000);

				if(second == sec)
				{
					ticks++;
				}
				else
				{
					second = sec;
					tps = (tps == 0 ? ticks : ((tps + ticks) / 2));
					ticks = 0;
				}
			}
		}, 20, 1);

		getServer().getScheduler().runTaskTimerAsynchronously(this, new Runnable() {

			@Override
			public void run() {

				if(!Applessentials.serverInfoMenus.isEmpty()) {
					ItemStack[] invUpdate = Utils.getServerInfoInv().getContents();

					getServer().getScheduler().runTask(Applessentials.getPlugin(), new Runnable() {

						private ItemStack[] update;
						@Override
						public void run() {
							for(Player p : Applessentials.serverInfoMenus) {
								p.getOpenInventory().getTopInventory().setContents(update);
							}
						}

						private Runnable init(ItemStack[] update){
							this.update = update;
							return this;
						}

					}.init(invUpdate));
				}
				// TODO Auto-generated method stub

			}

		}, 20*5, 20*5);

		getLogger().info("Enabled!");
	}

	public static int getTPS() {
		return tps;
	}

	private boolean setupVault() {
		Plugin vault =  getServer().getPluginManager().getPlugin("Vault");
		if (vault != null) { //first check that vault exists
			getLogger().info("Hooked into Vault v" + vault.getDescription().getVersion());
			if(!setupEconomy()) { //check for econ plugin
				getLogger().severe("No permissions plugin to handle cash!");
				return false;
			}
			if(!setupChat()) { //check for plugin to handle chat stuff
				getLogger().severe("No chat plugin to handle prefix/suffix!");
				return false;
			}
			if(!setupPermission()) { //check for plugin to handle permissons
				getLogger().severe("No chat plugin to handle permissons!");
				return false;
			}
		} else {
			getLogger().severe("Vault plugin not found!");
			return false;
		}
		return true;
	}

	private boolean setupEconomy() {
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			economy = economyProvider.getProvider();
			hasEconPlugin = true;
		}
		return (economy != null);
	}

	private boolean setupChat()
	{
		RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
		if (chatProvider != null) {
			chat = chatProvider.getProvider();
		}
		return (chat != null);
	}

	public boolean setupPermission() {
		RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
		if (permissionProvider != null) {
			permission = permissionProvider.getProvider();
		}
		return (permission != null);
	}

	public void loadFilesAndData() {
		Bukkit.getScheduler().cancelTasks(this);
		loadConfig();
		loadConfigData();
		loadSignsFile();
		loadSignsData();
		loadStatsFile();
	}

	private void loadConfig() {
		PluginManager pm = getServer().getPluginManager();
		String pluginFolder = getDataFolder().getAbsolutePath();
		(new File(pluginFolder)).mkdirs();
		File configFile = new File(pluginFolder, "config.yml");
		if(!configFile.exists()) {
			saveResource("config.yml", true);
		}
		try {
			reloadConfig();
		} catch (Exception e) {
			getLogger().severe("Exception while loading Applessentials/config.yml");
			pm.disablePlugin(this);
		}
	}

	private void loadConfigData() {
		//load boss bar messages
		ConfigurationSection cs = getConfig().getConfigurationSection("boss-bar-messages");
		bmh.clearWorldMessages();
		if(cs != null) {
			for(String worldName : cs.getKeys(false)) {
				ConfigurationSection messageOptions = cs.getConfigurationSection(worldName);
				if (messageOptions != null) {
					int interval = getConfig().getInt("boss-bar-messages." + worldName + ".interval", 0);
					List<String> messages = getConfig().getStringList("boss-bar-messages." + worldName + ".messages");
					bmh.addWorldMessages(worldName, messages, interval);
				}
			}
		}

		//load warn punishments
		Applessentials.warnPunishments.clear();
		ConfigurationSection cs2 = getConfig().getConfigurationSection("warn-punishments");
		if(cs2 != null) {
			for(String warnPoints : cs2.getKeys(false)) {
				String time = getConfig().getString("warn-punishments." + warnPoints);
				Applessentials.warnPunishments.add(new WarnPunishment(Integer.parseInt(warnPoints), time));
			}
		}

		//load spawn compass data
		ConfigurationSection cs3 = getConfig().getConfigurationSection("spawn-compass-gui");
		int menuRows = cs3.getInt("rows");
		if(menuRows > 6) {
			getLogger().warning("The spawn compass gui rows exceeds 6! Setting to default of 6");
			menuRows = 6;
		}
		Applessentials.getMenuManager().setMenuData(menuRows, cs3.getString("gui-name", "Games Menu"));
		ConfigurationSection slots = cs3.getConfigurationSection("slots");
		Applessentials.getMenuManager().clearMenuItems();
		if(slots != null) {
			for(String slot : slots.getKeys(false)) {
				ConfigurationSection slotData = slots.getConfigurationSection(slot);
				if (slotData != null) {
					int slotInt = Integer.parseInt(slot);
					CompassItem item = new CompassItem(slotData, slotInt);
					if(!item.isValid()) continue;
					Applessentials.getMenuManager().addItem(slotInt, item);
					this.getLogger().info("Loaded compass menu slot: " + slot);
					continue;
				} else {
					System.out.println(slot + " is null");
				}
			}
		} else {
			System.out.println("slots is null");
		}

		//load World/Gametype Relationships
		ConfigurationSection cs4 = getConfig().getConfigurationSection("world-gametype");
		Applessentials.worldAndGamemodes.clear();
		if(cs4 != null) {
			for(String world : cs4.getKeys(false)) {
				String gameType = cs4.getString(world);
				Applessentials.worldAndGamemodes.put(world, gameType);
			}
		}
	}

	private void loadSignsFile() {
		PluginManager pm = getServer().getPluginManager();
		String pluginFolder = this.getDataFolder().getAbsolutePath();
		(new File(pluginFolder)).mkdirs();
		String playerFolder = pluginFolder;
		(new File(playerFolder)).mkdirs();
		File playerDataFile = new File(playerFolder, "signs.yml");
		ConfigAccessor playerData = new ConfigAccessor("signs.yml");

		if (!playerDataFile.exists()) {
			try {
				playerData.saveDefaultConfig();
			} catch (Exception e) {
				pm.disablePlugin(this);
			}
			return;
		} else {
			try {
				playerData.reloadConfig();
			} catch (Exception e) {
				pm.disablePlugin(this);
			}
		}
	}

	private void loadSignsData() {
		ConfigAccessor signData = new ConfigAccessor("signs.yml");
		ConfigurationSection cs = signData.getConfig().getConfigurationSection("Signs");
		if(cs != null) {
			for(String signName : cs.getKeys(false)) {
				ConfigurationSection signOptions = cs.getConfigurationSection(signName);
				if (signOptions != null) {
					Applessentials.getSignManager().loadSign(signName, signOptions);
					this.getLogger().info("Loaded sign: " + signName);
					continue;
				}
			}
		}
	}

	private void loadStatsFile() {
		PluginManager pm = getServer().getPluginManager();
		String pluginFolder = this.getDataFolder().getAbsolutePath();
		(new File(pluginFolder)).mkdirs();
		String playerFolder = pluginFolder;
		(new File(playerFolder)).mkdirs();
		File playerDataFile = new File(playerFolder, "stats.yml");
		ConfigAccessor playerData = new ConfigAccessor("stats.yml");

		if (!playerDataFile.exists()) {
			try {
				playerData.saveDefaultConfig();
			} catch (Exception e) {
				pm.disablePlugin(this);
			}
			return;
		} else {
			try {
				playerData.reloadConfig();
			} catch (Exception e) {
				pm.disablePlugin(this);
			}
		}
	}

	public static World getSpawnWorld() {
		return spawnWorld;
	}

	public static SliceManager getSliceManager() {
		return sm;
	}

	public static SignManager getSignManager() {
		return signm;
	}

	public static BarMessageHandler getBarMessageHandler() {
		return bmh;
	}

	public static VoteLobby getVoteLobby() {
		return null/*vl*/;
	}

	public static MenuManager getMenuManager() {
		return mm;
	}

	public static Plugin getPlugin() {
		return plugin;
	}

	public static String getGameTypeForWorld(String worldName) {
		if(Applessentials.worldAndGamemodes.containsKey(worldName)) {
			return Applessentials.worldAndGamemodes.get(worldName);
		} else {
			return null;
		}
	}
}
