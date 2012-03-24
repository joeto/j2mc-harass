package to.joe.j2mc.harass;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import net.minecraft.server.EntityChicken;
import net.minecraft.server.Packet24MobSpawn;
import net.minecraft.server.Packet60Explosion;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import to.joe.j2mc.core.J2MC_Manager;
import to.joe.j2mc.harass.command.HarassCommand;
import to.joe.j2mc.harass.command.SlapCommand;
import to.joe.j2mc.harass.command.SlayCommand;
import to.joe.j2mc.harass.command.SmiteCommand;

public class J2MC_Harass extends JavaPlugin implements Listener {

    private static ArrayList<String> harassees;

    private String[] pandaLines;

    private final Object sync = new Object();
    private final Random random = new Random();

    /**
     * Adds player to harassment list
     * 
     * @param player
     */
    public void harass(String name) {
        synchronized (this.sync) {
            J2MC_Harass.harassees.add(name.toLowerCase());
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (this.isHarassed(event.getPlayer())) {
            final Player player = event.getPlayer();
            final Location location = event.getBlock().getLocation();
            this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

                @Override
                public void run() {
                    player.sendBlockChange(location, Material.SPONGE, (byte) 0);
                }

            });

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (this.isHarassed(event.getPlayer())) {
            final Location location = event.getBlockPlaced().getLocation();
            final Inventory i = event.getPlayer().getInventory();
            i.remove(event.getPlayer().getItemInHand().getType());
            final EntityChicken bawk = new EntityChicken(((CraftWorld) event.getPlayer().getWorld()).getHandle());
            bawk.setLocation(location.getX(), location.getY(), location.getZ(), location.getPitch(), location.getYaw());
            final Packet24MobSpawn pack1 = new Packet24MobSpawn(bawk);
            final Packet60Explosion pack2 = new Packet60Explosion(location.getX(), location.getY(), location.getZ(), 10, new HashSet<Block>());
            ((CraftPlayer) event.getPlayer()).getHandle().netServerHandler.sendPacket(pack1);
            ((CraftPlayer) event.getPlayer()).getHandle().netServerHandler.sendPacket(pack2);
            event.setCancelled(true);
        }
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Harass module disabled");
    }

    // Listeners after this

    @Override
    public void onEnable() {
        J2MC_Harass.harassees = new ArrayList<String>();
        this.getCommand("harass").setExecutor(new HarassCommand(this));
        this.getCommand("slap").setExecutor(new SlapCommand(this));
        this.getCommand("slay").setExecutor(new SlayCommand(this));
        this.getCommand("smite").setExecutor(new SmiteCommand(this));

        try {
            this.pandaLines = this.fileToArray("panda.txt");
        } catch (final Exception e) {
            this.pandaLines = new String[1];
            this.pandaLines[0] = "ololol imma griefer ban me plz";
        }

        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        this.getLogger().info("Harass module enabled");
    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        if (this.isHarassed(event.getPlayer())) {
            final String message = event.getMessage();
            J2MC_Manager.getCore().adminAndLog(ChatColor.DARK_AQUA + "[HARASS]BLOCKED: " + event.getPlayer().getName() + ChatColor.WHITE + ": " + message);
            final String squawk = this.pandaLines[this.random.nextInt(this.pandaLines.length)];
            J2MC_Manager.getCore().getServer().broadcastMessage(ChatColor.WHITE + "<" + event.getPlayer().getName() + ChatColor.WHITE + "> " + squawk);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (this.isHarassed(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    public boolean isHarassed(Player player) {
        return J2MC_Harass.harassees.contains(player.getName().toLowerCase());
    }

    /**
     * LEAVE BRITTANY ALONE! Er, uh, removes player from harassment list
     * 
     * @param name
     */
    public void remove(String name) {
        synchronized (this.sync) {
            J2MC_Harass.harassees.remove(name.toLowerCase());
        }
    }

    private String[] fileToArray(String filename) {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(filename);
        } catch (final FileNotFoundException e2) {
            // e2.printStackTrace();
            this.getLogger().severe("File not found: " + filename);
            final String[] uhOh = new String[1];
            uhOh[0] = "";
            return uhOh;
        }
        final BufferedReader bufferedReader = new BufferedReader(fileReader);
        final List<String> fileLines = new ArrayList<String>();
        String line = null;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                fileLines.add(line);
            }
        } catch (final IOException e1) {
            e1.printStackTrace();
        }
        try {
            bufferedReader.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return fileLines.toArray(new String[fileLines.size()]);
    }

}
