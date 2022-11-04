package fr.firedragonalex.rankandlevels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import fr.firedragonalex.diamoney.PlayerBank;
import fr.firedragonalex.rankandlevels.gui.ListenerGUI;
import fr.firedragonalex.rankandlevels.rank.Rank;
import fr.firedragonalex.rankandlevels.saveandload.LoadRanks;
import fr.firedragonalex.rankandlevels.saveandload.MySql;
import fr.firedragonalex.rankandlevels.saveandload.SaveAndLoad;

public class Main extends JavaPlugin {
	
	private static Main instance;
	private static HashMap<Integer, HashMap<Rank, ItemStack>> allCustomRewards = new HashMap<>();
	private static HashMap<UUID, Rank> playersRanks = new HashMap<>();
	private static HashMap<UUID, HashMap<Rank, Integer>> levelRewardsAdvancement = new HashMap<>();
	
	@Override
	public void onEnable() {
		
		this.saveDefaultConfig();
		Main.instance = this; 
//		Main.allItemsReward;
//		HashMap page = new HashMap();
//		HashMap column = new HashMap();
		getCommand("levelrewards").setExecutor(new Commands());
		getCommand("feed").setExecutor(new Commands());
		getCommand("craft").setExecutor(new Commands());
		getCommand("enderchest").setExecutor(new Commands());
		getServer().getPluginManager().registerEvents(new Listeners(), this);
		getServer().getPluginManager().registerEvents(new ListenerGUI(), this);
		
		MySql.setDatabase("woswaria_rankandlevels");
		
		LoadRanks.loadAllPlayers();
		
		SaveAndLoad.load();
		
		System.out.println("[RankAndLevels-FDA] Enabled !");
	}
	
	public static Main getInstance() {
		return Main.instance;
	}
	
	public static HashMap<Integer, HashMap<Rank, ItemStack>> getCustomRewards() {
		return Main.allCustomRewards;
	}
	
	public static HashMap<UUID, HashMap<Rank, Integer>> getLevelRewardsAdvancement() {
		return Main.levelRewardsAdvancement;
	}
	
	public static void setRank(Player player, Rank rank) {
		Main.setRank(player.getUniqueId(), rank);
	}
	
	public static void setRank(UUID uuid, Rank rank) {
		if (Main.playersRanks.containsKey(uuid)) {
			Main.playersRanks.replace(uuid, rank);
		} else {
			Main.playersRanks.put(uuid, rank);
		}
		Player player = Bukkit.getPlayer(uuid);
		
		String customName = rank.getColor()+"["+rank.getName()+"] "+player.getName();
		
		player.setDisplayName(customName);
		player.setPlayerListName(customName);
	}
	
	public static Rank getRank(UUID uuid) {
		return Main.playersRanks.get(uuid);
	}
	
	public static Rank getRank(Player player) {
		return Main.playersRanks.get(player.getUniqueId());
	}
	
	public static ItemStack customItem(Material itemType, String name) {
		ItemStack item = new ItemStack(itemType,1);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(name);
		item.setItemMeta(itemMeta);
		return item;
	}
	
	public static ItemStack customHead(String name,String nameTexture) {
		ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD,1);
		SkullMeta playerHeadMeta = (SkullMeta)playerHead.getItemMeta();
		playerHeadMeta.setDisplayName(name);
		playerHeadMeta.setOwner(nameTexture);
		playerHead.setItemMeta(playerHeadMeta);
		return playerHead;
	}
	
	@Override
	public void onDisable() {
		SaveAndLoad.save();
		System.out.println("[RankAndLevels-FDA] Disabled !");
	}
}