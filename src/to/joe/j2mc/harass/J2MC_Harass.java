package to.joe.j2mc.harass;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.plugin.java.JavaPlugin;

import to.joe.j2mc.core.J2MC_Manager;
import to.joe.j2mc.harass.command.CraftualHarassmentPanda;
import to.joe.j2mc.harass.command.HarassCommand;
import to.joe.j2mc.harass.command.SlapCommand;
import to.joe.j2mc.harass.command.SlayCommand;
import to.joe.j2mc.harass.command.SmiteCommand;

public class J2MC_Harass extends JavaPlugin {

	@Override
	public void onDisable() {
		J2MC_Manager.getLog().info("Harass module disabled");
	}

	@Override
	public void onEnable() {
		J2MC_Manager.getLog().info("Harass module enabled");

		this.getCommand("harass").setExecutor(new HarassCommand(this));
		this.getCommand("slap").setExecutor(new SlapCommand(this));
		this.getCommand("slay").setExecutor(new SlayCommand(this));
		this.getCommand("smite").setExecutor(new SmiteCommand(this));
		
		Bukkit.getServer().getPluginManager().registerEvent(Type.BLOCK_PLACE, new BListener(), Priority.Normal, this);
		Bukkit.getServer().getPluginManager().registerEvent(Type.BLOCK_BREAK, new BListener(), Priority.Normal, this);
		Bukkit.getServer().getPluginManager().registerEvent(Type.PLAYER_COMMAND_PREPROCESS, new ChatListener(), Priority.Normal, this);
		Bukkit.getServer().getPluginManager().registerEvent(Type.PLAYER_CHAT, new ChatListener(), Priority.Normal, this);
	}

	// Listeners after this

	CraftualHarassmentPanda methods = new CraftualHarassmentPanda();

	private class BListener extends BlockListener {

		public void onBlockBreak(BlockBreakEvent event) {

			final Player player = event.getPlayer();
			if (!methods.blockHurt(player, event.getBlock().getLocation())) {
				event.setCancelled(true);
				return;
			}
		}

		public void onBlockPlace(BlockPlaceEvent event) {
			final Player player = event.getPlayer();
			final Block blockPlaced = event.getBlockPlaced();
			if (!methods.blockPlace(player, blockPlaced.getLocation())) {
				event.setCancelled(true);
				return;
			}
		}
	}

	private class ChatListener extends PlayerListener {
		public void onPlayerChat(PlayerChatEvent event) {
			final Player player = event.getPlayer();
			final String message = event.getMessage();
			if (!methods.chat(player, message)) {
				event.setCancelled(true);
				return;
			}
		}

		public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
			final Player player = event.getPlayer();
			final String name = player.getName();
			final String message = event.getMessage();
			if (!methods.chat(player, message)) {
				event.setCancelled(true);
				return;
			}
		}
	}

}
